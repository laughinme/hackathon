from typing import Annotated
from uuid import UUID
from fastapi import APIRouter, Depends, Path, HTTPException

from domain.books import BookModel
from core.config import Settings
from core.security import auth_user
from service.books import BookService, get_books_service
from database.relational_db import User

router = APIRouter()
config = Settings() # pyright: ignore[reportCallIssue]


@router.get(
    path='/',
    response_model=BookModel,
    summary='Get specific book by its id',
)
async def get_book(
    book_id: Annotated[UUID, Path(...)],
    user: Annotated[User, Depends(auth_user)],
    svc: Annotated[BookService, Depends(get_books_service)],
):
    book = await svc.get_book(book_id, user)
    if book is None:
        raise HTTPException(404, detail='Book with this `book_id` not found')
    
    return book
