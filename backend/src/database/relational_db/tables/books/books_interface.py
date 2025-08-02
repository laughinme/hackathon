from uuid import UUID
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from utils import dist_expression
from .books_table import Book
from ..geography import ExchangeLocation
from ..users import User


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
    
    async def recommended_books(self, user: User) -> list[Book]:
        books = await self.session.scalars(
            select(Book)
            .join(ExchangeLocation)
            .where(
                Book.genre.in_(user.favorite_genres),
                Book.language == user.language,
                ExchangeLocation.city_id == user.city_id
            )
            .order_by(
                dist_expression(ExchangeLocation, user.latitude, user.longitude) # type: ignore
            )
        )
        
        return list(books.all())
    
    async def list_books(self) -> list[Book]:
        books = await self.session.scalars(
            select(Book)
        )
        
        return list(books.all())
