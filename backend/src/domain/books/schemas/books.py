from uuid import UUID
from pydantic import BaseModel, Field

from ..enums import Condition

class BookModel(BaseModel):
    owner_id: UUID = Field(...)
    city_id: int = Field(...)

    title: str = Field(...)
    description: str | None = Field(None)
    extra_terms: str | None = Field(None)

    author: int = Field(...)
    genre: int = Field(...)
    language: str = Field(..., min_length=2, max_length=2)
    pages: int | None = Field(None)
    condition: Condition = Field(...)
    photo_urls: list[str] = Field(default_factory=list)

    exchange_location_id: int = Field(...)
    is_available: bool = Field(...)
