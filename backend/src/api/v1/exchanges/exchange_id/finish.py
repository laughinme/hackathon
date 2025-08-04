from typing import Annotated
from uuid import UUID
from fastapi import APIRouter, Depends, Path, HTTPException

from domain.exchanges import ExchangeModel, ExchangeProgress as EP
from core.config import Settings
from core.security import auth_user
from service.exchanges import ExchangeService, get_exchanges_service
from database.relational_db import User

router = APIRouter()
config = Settings() # pyright: ignore[reportCallIssue]


@router.patch(
    path='/finish',
    response_model=ExchangeModel,
    summary='Finish exchange process',
)
async def complete_exchange(
    exchange_id: Annotated[UUID, Path(...)],
    user: Annotated[User, Depends(auth_user)],
    svc: Annotated[ExchangeService, Depends(get_exchanges_service)],
):
    return await svc.confirm_exchange(exchange_id, user)
