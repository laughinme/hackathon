from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from .authors_table import Author


class AuthorsInterface:
    def __init__(self, session: AsyncSession):
        self.session = session
        
    async def by_id(self, id: int) -> Author | None:
        author = await self.session.scalar(
            select(Author)
            .where(Author.id == id)
        )
        
        return author
        
    async def list_all(self) -> list[Author]:
        authors = await self.session.scalars(
            select(Author)
        )
        return list(authors.all())
    
    async def get_by_ids(self, ids: set[int]) -> list[Author]:
        authors = (
            await self.session.scalars(
                select(Author)
                .where(Author.id.in_(ids))
            )
        ).all()
        
        return list(authors)
