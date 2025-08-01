from fastapi import Depends

from database.relational_db import (
    UserInterface,
    get_uow,
    UoW,
    UserGenreInterface,
    GenresInterface,
    CitiesInterface
)
from .user_service import UserService


async def get_user_service(
    uow: UoW = Depends(get_uow),
) -> UserService:
    user_repo = UserInterface(uow.session)
    ug_repo = UserGenreInterface(uow.session)
    genres_repo = GenresInterface(uow.session)
    cities_repo = CitiesInterface(uow.session)
    return UserService(uow, user_repo, ug_repo, genres_repo, cities_repo)
