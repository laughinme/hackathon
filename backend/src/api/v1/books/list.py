from typing import Annotated
from fastapi import APIRouter, Depends, HTTPException

from database.relational_db import User
from domain.books import BookModel
from core.config import Settings
from core.security import auth_user
from service.books import BookService, get_books_service

router = APIRouter()
config = Settings() # pyright: ignore[reportCallIssue]


@router.get(
    path='/',
    response_model=BookModel,
    summary='List all available books with filters applied',
    responses={401: {'description': 'Book with this `book_id` not found'}},
)
async def get_books(
    _: Annotated[User, Depends(auth_user)],
    svc: Annotated[BookService, Depends(get_books_service)],
):
    pass
