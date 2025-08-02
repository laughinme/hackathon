from typing import Annotated
from fastapi import APIRouter, Depends, HTTPException

from database.relational_db import User
from domain.books import BookModel, BookCreate
from core.config import Settings
from core.security import auth_user
from service.books import BookService, get_books_service

router = APIRouter()
config = Settings() # pyright: ignore[reportCallIssue]


@router.post(
    path='/create',
    response_model=BookModel,
    status_code=201,
    summary='Create a book',
)
async def create_book(
    payload: BookCreate,
    user: Annotated[User, Depends(auth_user)],
    svc: Annotated[BookService, Depends(get_books_service)],
):
    book = await svc.create_book(payload, user)
    return book
