from uuid import UUID
from sqlalchemy import select, func
from sqlalchemy.ext.asyncio import AsyncSession

from utils import dist_expression
from .books_table import Book
from ..recommendations import UserInterest
from ..statistics import BookStats
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
    
    async def recommended_books(self, user: User, lat: float, lon: float, limit: int) -> list[Book]:
        dist_km = dist_expression(ExchangeLocation, lat, lon)
        
        w_geo, w_pop, w_rec, w_int = 1.0, 2.0, 1.5, 1.0
        fresh_period = 3
        
        geo_score = func.least(10 / (1 + dist_km), 10)
        popularity_score = func.log(1 + BookStats.views + BookStats.likes*3 + BookStats.reserves*4)
        recent_score = func.exp( - (func.extract("epoch", func.now() - Book.created_at) / 86400) / fresh_period)
        interest_score = func.least(UserInterest.coef, 30)
        
        score = w_geo*geo_score + w_pop*popularity_score + w_rec*recent_score + w_int*interest_score

        stmt = (
            select(Book)
            .join(ExchangeLocation)
            .outerjoin(BookStats, Book.id == BookStats.book_id)
            .outerjoin(
                UserInterest,
                (UserInterest.user_id == user.id) & (UserInterest.genre_id == Book.genre_id)
            )
            .where(
                ExchangeLocation.city_id == user.city_id,
                Book.is_available
            )
            .order_by(score.desc())
            .limit(limit)
        )
        if user.language is not None:
            stmt = stmt.where(Book.language == user.language)
        
        books = await self.session.scalars(stmt)
        
        return list(books.all())
    
    async def list_books(self) -> list[Book]:
        books = await self.session.scalars(
            select(Book)
        )
        return list(books.all())

    async def list_user_books(self, user_id: UUID, limit: int) -> list[Book]:
        books = await self.session.scalars(
            select(Book)
            .where(Book.owner_id == user_id)
            .limit(limit)
        )
        return list(books.all())
