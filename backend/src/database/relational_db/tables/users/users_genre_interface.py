from uuid import UUID
from pydantic import EmailStr
from sqlalchemy import select, delete
from sqlalchemy.dialects.postgresql import insert
from sqlalchemy.ext.asyncio import AsyncSession

from .user_genre_table import UserGenre
from ..books import Genre


class UserGenreInterface:
    def __init__(self, session: AsyncSession):
        self.session = session
    
    async def add(self, user_genre: UserGenre):
        self.session.add(user_genre)
        
    async def list_current_ids(self, user_id: UUID) -> list[UserGenre]:
        current_genres = (
            await self.session.scalars(
                select(UserGenre)
                .where(UserGenre.user_id == user_id)
            )
        ).all()
        
        return list(current_genres)
    
    async def bulk_add(self, ids: set[int], user_id: UUID):
        stmt = (
            insert(UserGenre)
            .values([{'genre_id': id, 'user_id': user_id} for id in ids])
            .on_conflict_do_nothing()
            # .returning(UserGenre)
        )
        await self.session.execute(stmt)
        
    async def delete_pairs(self, ids: set[int], user_id: UUID) -> None:
        await self.session.execute(
                delete(UserGenre)
                .where(
                    UserGenre.user_id == user_id,
                    UserGenre.genre_id.in_(ids)
                )
            )
