from typing import Annotated
from fastapi import APIRouter, Depends, HTTPException

from database.relational_db import User
from domain.users import UserModel
from core.config import Settings
from core.security import auth_user

router = APIRouter()
config = Settings() # pyright: ignore[reportCallIssue]


@router.post(
    path='/',
    response_model=UserModel,
    summary='Get user account info'
)
async def profile(
    user: Annotated[User, Depends(auth_user)]
):
    return user


@router.patch(
    path='/',
    response_model=UserModel,
    summary='Update user info'
)
async def update_profile(
    user: Annotated[User, Depends(auth_user)]
):
    return user
