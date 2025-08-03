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
    path='/for_you',
    response_model=list[BookModel],
    summary='Get books for "For You" page (BETA)',
)
async def for_you(
    user: Annotated[User, Depends(auth_user)],
    svc: Annotated[BookService, Depends(get_books_service)],
    limit: int = Query(50, description='Number of books to return'),
):
    books = await svc.list_books(user, limit, filter=True)
    return books
