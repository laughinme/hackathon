from uuid import UUID, uuid4
from sqlalchemy.orm import mapped_column, Mapped, relationship
from sqlalchemy import Uuid, String, Boolean, select, ForeignKey, Integer
from sqlalchemy.dialects.postgresql import ARRAY, ENUM
from sqlalchemy.ext.asyncio import AsyncSession

from domain.books import Condition
from ..table_base import Base
from ..mixins import TimestampMixin


class Book(TimestampMixin, Base):
    __tablename__ = "books"

    id: Mapped[UUID] = mapped_column(Uuid(as_uuid=True), default=uuid4, primary_key=True)
    owner_id: Mapped[UUID] = mapped_column(
        Uuid(as_uuid=True), ForeignKey('users.id', ondelete='CASCADE'), nullable=False
    )
    city_id: Mapped[int] = mapped_column(
        Integer, ForeignKey('cities.id', ondelete='CASCADE'), nullable=False, index=True
    )
    
    title: Mapped[str] = mapped_column(String, nullable=False)
    description: Mapped[str] = mapped_column(String, nullable=True)
    extra_terms: Mapped[str] = mapped_column(String, nullable=True)
    author: Mapped[int] = mapped_column(
        Integer, ForeignKey('authors.id', ondelete='CASCADE'), nullable=False
    )
    genre: Mapped[int]= mapped_column(
        Integer, ForeignKey('genres.id', ondelete='CASCADE'), nullable=False
    )
    language: Mapped[str] = mapped_column(String(2), nullable=False)
    pages: Mapped[int] = mapped_column(Integer, nullable=True)
    condition: Mapped[Condition] = mapped_column(ENUM(Condition), nullable=False)
    photo_urls: Mapped[list[str]] = mapped_column(ARRAY(String, dimensions=1), nullable=False, default=[])
    exchange_location_id: Mapped[int] = mapped_column(
        Integer, ForeignKey('exchange_locations.id', ondelete='CASCADE'), nullable=False
    )
    
    is_available: Mapped[bool] = mapped_column(Boolean, nullable=False, default=True)
    

    @classmethod
    async def from_id(cls, session: AsyncSession, book_id: UUID | str) -> "Book | None":
        return await session.get(cls, book_id)
    
    
    @classmethod
    async def from_owner_id(cls, session: AsyncSession, user_id: UUID | str) -> "Book | None":
        return await session.scalar(
            select(cls)
            .where(cls.owner_id == user_id)
        )
