import aiofiles
import shutil

from uuid import UUID, uuid4
from pathlib import Path
from fastapi import UploadFile, status, HTTPException

from core.config import Settings
from domain.users import UserPatch, GenresPatch
from database.relational_db import (
    UoW,
    UserInterface, 
    User, 
    UserGenreInterface,
    GenresInterface,
    CitiesInterface,
    LanguagesInterface
)
from .exceptions import IncorrectGenreId, IncorrectCityId

settings = Settings() # type: ignore

class UserService:
    def __init__(
        self,
        uow: UoW,
        user_repo: UserInterface,
        ug_repo: UserGenreInterface,
        genres_repo: GenresInterface,
        cities_repo: CitiesInterface,
        lang_repo: LanguagesInterface
        
    ):
        self.uow = uow
        self.user_repo = user_repo
        self.ug_repo = ug_repo
        self.genres_repo = genres_repo
        self.cities_repo = cities_repo
        self.lang_repo = lang_repo
        
    async def get_user(self, user_id: UUID | str) -> User | None:
        return await self.user_repo.get_by_id(user_id)
        
    async def patch_user(self, payload: UserPatch, user: User):
        data = payload.model_dump(exclude_none=True)
        
        city_id = data.get('city_id')
        if city_id is not None:
            if await self.cities_repo.get_by_id(city_id) is None:
                raise IncorrectCityId
        
        if (genres := data.pop('favorite_genres', None)) is not None:
            await self.set_genres(genres, user)
        
        for field, value in data.items():
            setattr(user, field, value)
            
        await self.uow.session.flush()
            
        await self.uow.session.refresh(user)
            
    async def set_genres(self, new_ids: set[int], user: User):
        genres = await self.genres_repo.get_by_ids(new_ids)
        if len(genres) != len(new_ids):
            raise IncorrectGenreId
        
        current_ids = [pair.genre_id for pair in await self.ug_repo.list_ids(user.id)]
        if current_ids and current_ids != list(new_ids):
            raise HTTPException(400, detail='IDs cannot be changed after being set.')
        
        await self.ug_repo.bulk_add(new_ids, user.id)
        
        await self.uow.session.refresh(user)
        
    async def list_languages(self):
        return await self.lang_repo.list_all()

    async def add_picture(
        self,
        file: UploadFile,
        user: User
    ) -> None:
        folder = Path(settings.MEDIA_DIR, "users", str(user.id))
        if folder.exists():
            shutil.rmtree(folder)
        folder.mkdir(parents=True, exist_ok=True)

        if file.content_type not in ("image/jpeg", "image/png"):
            raise HTTPException(
                status.HTTP_415_UNSUPPORTED_MEDIA_TYPE,
                detail="Only jpg / png allowed"
            )

        ext  = ".jpg" if file.content_type == "image/jpeg" else ".png"
        name = f"{uuid4()}{ext}"

        async with aiofiles.open(folder / name, "wb") as out:
            while chunk := await file.read(1024 * 1024):
                await out.write(chunk)

        url = f"{settings.SITE_URL}/{settings.MEDIA_DIR}/users/{user.id}/{name}"

        user.avatar_url = url

    async def nearby(self, user: User, radius_km: int):
        lat, lon = user.latitude, user.longitude
        if lat is None or lon is None:
            raise HTTPException(412, detail='You should set your coordinates first')
        
        return await self.user_repo.nearby_users(lat, lon, radius_km)
