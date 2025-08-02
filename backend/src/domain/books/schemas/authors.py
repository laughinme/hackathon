from pydantic import BaseModel, Field


class AuthorModel(BaseModel):
    id: int = Field(...)
    name: str = Field(...)
