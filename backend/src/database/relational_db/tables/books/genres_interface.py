from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from .genres_table import Genre


class GenresInterface:
    def __init__(self, session: AsyncSession):
        self.session = session
        
    async def list_all(self) -> list[Genre]:
        genres = await self.session.scalars(
            select(Genre)
        )
        return list(genres.all())
    
    async def get_by_ids(self, ids: set[int]) -> list[Genre]:
        genres = (
            await self.session.scalars(
                select(Genre)
                .where(Genre.id.in_(ids))
            )
        ).all()
        
        return list(genres)
