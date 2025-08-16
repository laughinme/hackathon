from typing import Annotated, TYPE_CHECKING
from pydantic import BaseModel, Field, HttpUrl, constr, confloat
from uuid import UUID

from domain.books.schemas.genres import GenreModel
from ..enums import Gender
from ...geo import CityModel

class UserNearby(BaseModel):
    """
    User schema making possible to share other users public profile data.
    It is used only for nearby users request
    """
    id: UUID = Field(...)
    
    username: str | None = Field(None, description="User's display name")
    avatar_url: HttpUrl | None = Field(None)
    bio: str | None = Field(None)
    age: int | None = Field(None)
    gender: Gender | None = Field(None)
    language_code: Annotated[str, constr(min_length=2, max_length=2)] | None = Field(None)
    
    favorite_genres: list[GenreModel] = Field(...)
    
    city: CityModel = Field(...)
    
    latitude: Annotated[float, confloat(ge=-90, le=90)]  = Field(...)
    longitude: Annotated[float, confloat(ge=-90, le=90)] = Field(...)
    distance: float = Field(..., description='Distance to the current user in km')


class UserShare(BaseModel):
    """
    User schema making possible to share other users public profile data.
    """
    id: UUID = Field(...)
    
    username: str | None = Field(None, description="User's display name")
    avatar_url: HttpUrl | None = Field(None)
    bio: str | None = Field(None)
    age: int | None = Field(None)
    gender: Gender | None = Field(None)
    language_code: Annotated[str, constr(min_length=2, max_length=2)] | None = Field(None)
    
    favorite_genres: list[GenreModel] = Field(...)
    
    city: CityModel | None = Field(None)


class UserBrief(BaseModel):
    id: UUID = Field(...)
    username: str | None = Field(None, description="User's display name")
    avatar_url: HttpUrl | None = Field(None)
