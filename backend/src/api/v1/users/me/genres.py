from typing import Annotated
from fastapi import APIRouter, Depends, HTTPException

from database.relational_db import User
from domain.users import UserModel, GenresPatch, GenresModel
from core.config import Settings
from core.security import auth_user
from service.users import UserService, get_user_service

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
    svc: Annotated[UserService, Depends(get_user_service)],
):
    await svc.replace_genres(payload, user)
    return user
