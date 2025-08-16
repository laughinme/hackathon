from datetime import date
from pydantic import BaseModel, Field

class BookStatsGraph(BaseModel):
    day: date = Field(...)
    views: int = Field(...)
    likes: int = Field(...)
    reserves: int = Field(...)
