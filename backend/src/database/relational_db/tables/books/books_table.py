from uuid import UUID, uuid4
from sqlalchemy.orm import mapped_column, Mapped, relationship
from sqlalchemy import Uuid, String, Boolean, ForeignKey, Integer
from sqlalchemy.dialects.postgresql import ARRAY, ENUM

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
    author_id: Mapped[int] = mapped_column(
        Integer, ForeignKey('authors.id', ondelete='CASCADE'), nullable=False
    )
    genre_id: Mapped[int]= mapped_column(
        Integer, ForeignKey('genres.id', ondelete='CASCADE'), nullable=False
    )
    exchange_location_id: Mapped[int] = mapped_column(
        Integer, ForeignKey('exchange_locations.id', ondelete='CASCADE'), nullable=False
    )
    
    title: Mapped[str] = mapped_column(String, nullable=False)
    description: Mapped[str] = mapped_column(String, nullable=True)
    extra_terms: Mapped[str] = mapped_column(String, nullable=True)
    language: Mapped[str] = mapped_column(String(2), nullable=False)
    pages: Mapped[int] = mapped_column(Integer, nullable=True)
    condition: Mapped[Condition] = mapped_column(ENUM(Condition), nullable=False)
    photo_urls: Mapped[list[str]] = mapped_column(ARRAY(String, dimensions=1), nullable=False, default=[])
    
    is_available: Mapped[bool] = mapped_column(Boolean, nullable=False, default=True)
    
    city: Mapped['City'] = relationship(lazy='selectin') # type: ignore
    owner: Mapped['User'] = relationship(lazy='selectin') # type: ignore
    author: Mapped['Author'] = relationship(lazy='selectin') # type: ignore
    genre: Mapped['Genre'] = relationship(lazy='selectin') # type: ignore
    exchange_location: Mapped['ExchangeLocation'] = relationship(lazy='selectin') # type: ignore
