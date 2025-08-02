from pydantic import BaseModel, Field

from .cities import CityModel

class ExchangeLocation(BaseModel):
    id: int = Field(...)
    title: str = Field(...)
    description: str | None = Field(None)
    opening_hours: str | None = Field(None)

    # Navigation
    address: str = Field(...)
    directions: str | None = Field(None)
    latitude: float = Field(...)
    longitude: float = Field(...)
    city: CityModel = Field(...)
    
    is_active: bool = Field(...)
