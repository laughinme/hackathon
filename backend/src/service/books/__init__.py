from fastapi import Depends

from database.relational_db import (
    get_uow,
    UoW,
    GenresInterface,
)
from .books_service import BookService


async def get_books_service(
    uow: UoW = Depends(get_uow),
) -> BookService:
    genres_repo = GenresInterface(uow.session)
    return BookService(genres_repo)
