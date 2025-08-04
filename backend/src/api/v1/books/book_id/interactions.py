from typing import Annotated
from uuid import UUID
from fastapi import APIRouter, Depends, Path

from database.relational_db import User
from domain.statistics import Interaction
from domain.exchanges import ExchangeCreate, ExchangeModel
from core.config import Settings
from core.security import auth_user
from service.statistics import StatService, get_stats_service
from service.exchanges import ExchangeService, get_exchanges_service

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


@router.post(
    "/reserve",
    status_code=200,
    response_model=ExchangeModel,
    summary="Reserve the book",
    description="This action creates Exchange object, starting exchanging process."
)
async def reserve_book(
    payload: ExchangeCreate,
    book_id: Annotated[UUID, Path(...)],
    user: Annotated[User, Depends(auth_user)],
    stat_svc: Annotated[StatService, Depends(get_stats_service)],
    reserve_svc: Annotated[ExchangeService, Depends(get_exchanges_service)],
):
    await stat_svc.record_interaction(book_id, user, Interaction.RESERVE)
    return await reserve_svc.request_exchange(book_id, user, payload)
