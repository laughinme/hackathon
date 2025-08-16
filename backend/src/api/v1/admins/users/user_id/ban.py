from typing import Annotated
from uuid import UUID
from fastapi import APIRouter, Depends, Path

from core.security import auth_admin
from database.relational_db import User
from domain.admin import BanRequest
from domain.users import UserModel
from service.users import UserService, get_user_service

router = APIRouter()


@router.post(
    path='/ban',
    response_model=UserModel,
    summary='Ban or unban a user',
)
async def set_ban(
    payload: BanRequest,
    user_id: Annotated[UUID, Path(...)],
    _: Annotated[User, Depends(auth_admin)],
    svc: Annotated[UserService, Depends(get_user_service)],
):
    target = await svc.get_user(user_id)
    if target is None:
        from fastapi import HTTPException
        raise HTTPException(404, 'User not found')
    updated = await svc.admin_set_ban(target, payload.banned)
    return updated
