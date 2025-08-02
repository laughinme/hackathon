from pydantic import BaseModel, Field

from .cities import CityModel

class ExchangeLocation(BaseModel):
    id: int = Field(...)
    city: CityModel = Field(...)
    title: str = Field(...)
    description: str | None = Field(None)
    opening_hours: str = Field(...)

    # Navigation
    address: str = Field(...)
    directions: str | None = Field(None)
    latitude: float = Field(...)
    longitude: float = Field(...)
    
    is_active: bool = Field(...)
