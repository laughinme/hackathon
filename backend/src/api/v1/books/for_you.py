from typing import Annotated
from fastapi import APIRouter, Depends, Query, HTTPException

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
    query: str = Query("", max_length=50),
    limit: int | None = Query(None, ge=1, le=50),
):
    if query == "":
        if limit is not None:
            raise HTTPException(400, detail="Limit is not allowed when query is empty")
        limit_ = 50
    else:
        limit_ = limit or 10

    books = await svc.list_books(user, limit_, filter=True, query=query)
    return books
