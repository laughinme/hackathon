import aiofiles
import shutil
import logging

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
    AuthorsInterface,
    Author,
    Genre,
    BookEventsInterface,
)
from domain.books import BookCreate, BookPatch
from domain.statistics import Interaction

settings = Settings() # type: ignore
logger = logging.getLogger(__name__)

class BookService:
    def __init__(
        self,
        uow: UoW,
        genre_repo: GenresInterface,
        books_repo: BooksInterface,
        authors_repo: AuthorsInterface,
        events_repo: BookEventsInterface,
    ):
        self.genre_repo = genre_repo
        self.books_repo = books_repo
        self.uow = uow
        self.authors_repo = authors_repo
        self.events_repo = events_repo

    async def list_genres(self):
        genres = await self.genre_repo.list_all()
        return genres
    
    async def list_authors(self):
        authors = await self.authors_repo.list_all()
        return authors

    async def _apply_user_flags(self, books: list[Book], user: User):
        ids = [b.id for b in books]
        events = await self.events_repo.list_by_user_books(ids, user.id)
        liked = {e.book_id for e in events if e.interaction == Interaction.LIKE}
        viewed = {e.book_id for e in events if e.interaction == Interaction.CLICK}
        for b in books:
            setattr(b, 'is_liked_by_user', b.id in liked)
            setattr(b, 'is_viewed_by_user', b.id in viewed)
        return books

    async def get_book(self, book_id: UUID, user: User | None = None) -> Book | None:
        book = await self.books_repo.by_id(book_id)
        if book and user is not None:
            await self._apply_user_flags([book], user)
        return book
    
    async def get_author(self, author_id: int) -> Author | None:
        return await self.authors_repo.by_id(author_id)
    
    async def get_genre(self, genre_id: int) -> Genre | None:
        return await self.genre_repo.by_id(genre_id)

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
                logger.error(f'Incorrect media type uploaded: {f.content_type}')
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

    async def list_books(self, user: User, limit: int, filter: bool = False, query: str | None = None):
        if filter:
            lat, lon = user.latitude, user.longitude
            books = await self.books_repo.recommended_books(user, lat, lon, limit, query)
        else:
            books = await self.books_repo.list_books()

        await self._apply_user_flags(books, user)
        return books

    async def list_user_books(self, user: User, limit: int):
        books = await self.books_repo.list_user_books(user.id, limit)
        await self._apply_user_flags(books, user)
        return books

    async def edit_book(self, payload: BookPatch, book_id: UUID, user: User):
        data = payload.model_dump(exclude_none=True)
        
        # Not implemented yet
        # if (is_available := data.get('is_available')) is not None:
        #     if not is_available:
        #         data['unavailable_manual'] = True
        
        book = await self.get_book(book_id, user)
        if book is None:
            raise HTTPException(404, detail='Book with this id not found')
        if book.owner_id != user.id:
            raise HTTPException(403, detail='You dont have access to this resource')
            
        for field, value in data.items():
            setattr(book, field, value)
            
        await self.uow.session.refresh(book)
        return book
