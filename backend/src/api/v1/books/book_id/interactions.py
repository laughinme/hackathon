from typing import Annotated
from uuid import UUID
from fastapi import APIRouter, Depends, Path

from database.relational_db import User
from domain.statistics import Interaction
from core.config import Settings
from core.security import auth_user
from service.statistics import StatService, get_stats_service

router = APIRouter()
config = Settings() # pyright: ignore[reportCallIssue]


@router.post(
    "/click",
    status_code=204,
    summary="Make sure to send this request when user clicks a book"
)
async def record_click(
    book_id: Annotated[UUID, Path(...)],
    user: Annotated[User, Depends(auth_user)],
    svc: Annotated[StatService, Depends(get_stats_service)]
):
    await svc.record_interaction(book_id, user, Interaction.CLICK)


@router.post(
    "/like",
    status_code=204,
    summary="Record a like"
)
async def like_book(
    book_id: Annotated[UUID, Path(...)],
    user: Annotated[User, Depends(auth_user)],
    svc: Annotated[StatService, Depends(get_stats_service)]
):
    await svc.record_interaction(book_id, user, Interaction.LIKE)
