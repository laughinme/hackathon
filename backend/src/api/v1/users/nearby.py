from typing import Annotated
from fastapi import APIRouter, Depends, Query

from database.relational_db import User
from domain.users import UserNearby
from core.config import Settings
from core.security import auth_user
from service.users import UserService, get_user_service

router = APIRouter()
config = Settings() # pyright: ignore[reportCallIssue]


@router.get(
    path='/nearby',
    response_model=list[UserNearby],
    summary='Get nearby users (not tested yet)'
)
async def nearby_users(
    user: Annotated[User, Depends(auth_user)],
    svc: Annotated[UserService, Depends(get_user_service)],
    radius_km: int = Query(5, description='Max distance to the user'),
):
    users = await svc.nearby(user, radius_km)
    return users
