import logging
from typing import Annotated
from fastapi import APIRouter, Depends, HTTPException

from database.relational_db import User
from domain.users import UserModel, UserPatch
from core.config import Settings
from core.security import auth_user
from service.users import UserService, get_user_service

router = APIRouter()
config = Settings() # pyright: ignore[reportCallIssue]
logger = logging.getLogger(__name__)

@router.get(
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
    payload: UserPatch,
    user: Annotated[User, Depends(auth_user)],
    svc: Annotated[UserService, Depends(get_user_service)],
):
    logger.info(payload.model_dump_json())
    await svc.patch_user(payload, user)
    return user
