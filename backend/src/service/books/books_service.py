from database.relational_db import (
    Book,
    BooksInterface,
    GenresInterface,
    UserGenre
)


class BookService:
    def __init__(
        self,
        genre_repo: GenresInterface,
        books_repo: BooksInterface
    ):
        self.genre_repo = genre_repo
        self.books_repo = books_repo
        
    async def list_genres(self):
        genres = await self.genre_repo.list_all()
        return genres
    
    async def get_book(self, book_id: int) -> Book | None:
        return await self.books_repo.by_id(book_id)
