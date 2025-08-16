from typing import Annotated
from uuid import UUID
from fastapi import APIRouter, Depends, Path

from domain.exchanges import ExchangeModel
from core.config import Settings
from core.security import auth_admin
from service.exchanges import ExchangeService, get_exchanges_service
from database.relational_db import User

router = APIRouter()
config = Settings() # pyright: ignore[reportCallIssue]


@router.post(
    path='/force-finish',
    response_model=ExchangeModel,
    summary='Force finish exchange (admin only)',
)
async def force_finish_exchange(
    exchange_id: Annotated[UUID, Path(...)],
    _: Annotated[User, Depends(auth_admin)],
    svc: Annotated[ExchangeService, Depends(get_exchanges_service)],
):
    return await svc.admin_force_finish(exchange_id)


@router.post(
    path='/force-cancel',
    response_model=ExchangeModel,
    summary='Force cancel exchange (admin only)',
)
async def force_cancel_exchange(
    exchange_id: Annotated[UUID, Path(...)],
    _: Annotated[User, Depends(auth_admin)],
    svc: Annotated[ExchangeService, Depends(get_exchanges_service)],
):
    return await svc.admin_force_cancel(exchange_id)
