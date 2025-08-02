from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from .books_table import Book


class BooksInterface:
    def __init__(self, session: AsyncSession):
        self.session = session

    async def by_id(self, id: int) -> Book | None:
        book = await self.session.scalar(
            select(Book)
            .where(Book.id == id)
        )
        
        return book
