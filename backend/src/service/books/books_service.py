from uuid import UUID

from domain.users import UserPatch, GenresPatch
from database.relational_db import (
    UoW,
    UserInterface, 
    User, 
    UserGenreInterface,
    GenresInterface,
    UserGenre
)


class BookService:
    def __init__(
        self,
        genre_repo: GenresInterface,
    ):
        self.genre_repo = genre_repo
        
    async def list_genres(self):
        genres = await self.genre_repo.list_all()
        return genres
