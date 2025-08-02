from typing import Annotated
from uuid import UUID
from fastapi import APIRouter, Depends, Path, HTTPException

from domain.books import BookModel
from core.config import Settings
from core.security import auth_user
from service.books import BookService, get_books_service

router = APIRouter()
config = Settings() # pyright: ignore[reportCallIssue]


@router.get(
    path='/',
    response_model=BookModel,
    summary='Get specific book by its id',
    responses={404: {'description': 'Book with this `book_id` not found'}},
)
async def get_book(
    book_id: Annotated[UUID, Path(...)],
    # _: Annotated[User, Depends(auth_user)],
    svc: Annotated[BookService, Depends(get_books_service)],
):
    book = await svc.get_book(book_id)
    if book is None:
        raise HTTPException(404, detail='Book with this `book_id` not found')
    
    return book
