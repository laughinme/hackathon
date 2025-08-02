from uuid import UUID
from pydantic import BaseModel, Field

from .authors import AuthorModel
from .genres import GenreModel
from ..enums import Condition
from ...geo import CityModel, ExchangeLocation


class BookModel(BaseModel):
    id: int = Field(...)
    owner_id: UUID = Field(...)
    city: CityModel = Field(...)

    title: str = Field(...)
    description: str | None = Field(None)
    extra_terms: str | None = Field(None)

    author: AuthorModel = Field(...)
    genre: GenreModel = Field(...)
    language: str = Field(..., min_length=2, max_length=2)
    pages: int | None = Field(None)
    condition: Condition = Field(...)
    photo_urls: list[str] = Field(default_factory=list)

    exchange_location: ExchangeLocation = Field(...)
    is_available: bool = Field(...)
