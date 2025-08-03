from typing import Annotated
from fastapi import APIRouter, Depends, HTTPException

from database.relational_db import User
from domain.users import UserModel, GenresPatch
from core.config import Settings
from core.security import auth_user
from service.users import UserService, get_user_service
from service.statistics import StatService, get_stats_service

router = APIRouter()
config = Settings() # pyright: ignore[reportCallIssue]


@router.put(
    path='/genres',
    response_model=UserModel,
    summary='Replace favorite genres'
)
async def update_genres(
    payload: GenresPatch,
    user: Annotated[User, Depends(auth_user)],
    user_svc: Annotated[UserService, Depends(get_user_service)],
    stat_svc: Annotated[StatService, Depends(get_stats_service)],
):
    await stat_svc.set_interests(payload.favorite_genres, user)
    await user_svc.set_genres(payload, user)
    return user
