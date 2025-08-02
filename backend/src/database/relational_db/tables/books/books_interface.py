from uuid import UUID
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from .books_table import Book


class BooksInterface:
    def __init__(self, session: AsyncSession):
        self.session = session

    async def by_id(self, id: UUID) -> Book | None:
        book = await self.session.scalar(
            select(Book)
            .where(Book.id == id)
        )
        
        return book
    
    def add(self, book: Book):
        self.session.add(book)

    async def check_ownership(self, book_id: UUID, user_id: UUID) -> Book | None:
        book = await self.session.scalar(
            select(Book)
            .where(Book.id == book_id, Book.owner_id == user_id)
        )
        
        return book
