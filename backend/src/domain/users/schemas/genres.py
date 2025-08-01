from pydantic import BaseModel, Field, field_validator


class GenresModel(BaseModel):
    id: int = Field(...)
    name: str = Field(...)
