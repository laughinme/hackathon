from fastapi import Depends

from database.relational_db import (
    get_uow,
    UoW,
    GenresInterface,
    BooksInterface,
    AuthorsInterface,
)
from .books_service import BookService


async def get_books_service(
    uow: UoW = Depends(get_uow),
) -> BookService:
    genres_repo = GenresInterface(uow.session)
    books_repo = BooksInterface(uow.session)
    authors_repo = AuthorsInterface(uow.session)
    return BookService(uow, genres_repo, books_repo, authors_repo)
