from pydantic import BaseModel, Field


class CityModel(BaseModel):
    id: int = Field(...)
    name: str = Field(...)
