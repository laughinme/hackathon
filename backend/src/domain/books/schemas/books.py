from uuid import UUID
from pydantic import BaseModel, Field

from domain.common import TimestampModel
from .authors import AuthorModel
from .genres import GenreModel
from ..enums import Condition
from ...geo import ExchangeLocation


class BookModel(TimestampModel, BaseModel):
    id: UUID = Field(...)
    owner_id: UUID = Field(...)

    title: str = Field(...)
    description: str | None = Field(None)
    extra_terms: str | None = Field(None)

    author: AuthorModel = Field(...)
    genre: GenreModel = Field(...)
    language: str = Field(..., min_length=2, max_length=2)
    pages: int | None = Field(None)
    condition: Condition = Field(...)
    photo_urls: list[str] = Field(...)

    exchange_location: ExchangeLocation = Field(...)
    is_available: bool = Field(...)


class BookCreate(BaseModel):
    title: str = Field(...)
    description: str | None = Field(None)
    extra_terms: str | None = Field(None)

    author_id: int = Field(...)
    genre_id: int = Field(...)
    language: str = Field(..., min_length=2, max_length=2)
    pages: int | None = Field(None)
    condition: Condition = Field(...)

    exchange_location_id: int = Field(...)
    is_available: bool = Field(True)
