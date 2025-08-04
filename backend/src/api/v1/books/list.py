from typing import Annotated
from fastapi import APIRouter, Depends, Query

from database.relational_db import User
from domain.books import BookModel
from core.config import Settings
from core.security import auth_user
from service.books import BookService, get_books_service

router = APIRouter()
config = Settings() # pyright: ignore[reportCallIssue]


@router.get(
    path='/',
    response_model=list[BookModel],
    summary='List all books without filters',
)
async def get_books(
    user: Annotated[User, Depends(auth_user)],
    svc: Annotated[BookService, Depends(get_books_service)],
    limit: int = Query(50, description='Number of books to return'),
):
    books = await svc.list_books(user, limit)
    return books


@router.get(
    path='/my',
    response_model=list[BookModel],
    summary='List all books that belong to the current user',
)
async def get_my_books(
    user: Annotated[User, Depends(auth_user)],
    svc: Annotated[BookService, Depends(get_books_service)],
    limit: int = Query(50, description='Number of books to return'),
):
    books = await svc.list_user_books(user, limit)
    return books
