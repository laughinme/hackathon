import aiofiles
import shutil

from uuid import UUID, uuid4
from pathlib import Path
from fastapi import UploadFile, HTTPException, status

from core.config import Settings
from database.relational_db import (
    Book,
    BooksInterface,
    GenresInterface,
    User,
    UoW,
    AuthorsInterface
)
from domain.books import BookCreate

settings = Settings() # type: ignore

class BookService:
    def __init__(
        self,
        uow: UoW,
        genre_repo: GenresInterface,
        books_repo: BooksInterface,
        authors_repo: AuthorsInterface
    ):
        self.genre_repo = genre_repo
        self.books_repo = books_repo
        self.uow = uow
        self.authors_repo = authors_repo
        
    async def list_genres(self):
        genres = await self.genre_repo.list_all()
        return genres
    
    async def list_authors(self):
        authors = await self.authors_repo.list_all()
        return authors
    
    async def get_book(self, book_id: UUID) -> Book | None:
        return await self.books_repo.by_id(book_id)

    async def create_book(self, payload: BookCreate, user: User):
        book = Book(**payload.model_dump())
        user.books.append(book)
        
        await self.uow.session.flush()
        
        new_book = await self.books_repo.by_id(book.id)
        return new_book
        

    async def add_photos(
        self,
        book_id: UUID,
        files: list[UploadFile],
        user: User
    ) -> Book:
        book = await self.books_repo.by_id(book_id)
        if book is None:
            raise HTTPException(status.HTTP_404_NOT_FOUND, "Book with this id not found")
        if book.owner_id != user.id:
            raise HTTPException(status.HTTP_403_FORBIDDEN, "You don't own this item")

        folder = Path(settings.MEDIA_DIR, "books", str(book_id))
        if folder.exists():
            shutil.rmtree(folder)
        folder.mkdir(parents=True, exist_ok=True)

        book.photo_urls.clear()

        urls: list[str] = []
        for f in files:
            if f.content_type not in ("image/jpeg", "image/png"):
                raise HTTPException(
                    status.HTTP_415_UNSUPPORTED_MEDIA_TYPE,
                    detail="Only jpg / png allowed"
                )

            ext  = ".jpg" if f.content_type == "image/jpeg" else ".png"
            name = f"{uuid4()}{ext}"

            async with aiofiles.open(folder / name, "wb") as out:
                while chunk := await f.read(1024 * 1024):
                    await out.write(chunk)

            url = f"{settings.SITE_URL}/{settings.MEDIA_DIR}/books/{book_id}/{name}"
            urls.append(url)

        book.photo_urls.extend(urls)
        return book
