from pydantic import BaseModel, Field, field_validator


class GenreModel(BaseModel):
    id: int = Field(...)
    name: str = Field(...)
