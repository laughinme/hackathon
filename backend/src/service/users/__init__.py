from fastapi import Depends

from database.relational_db import UserInterface, get_uow, UoW
from .user_service import UserService


async def get_user_service(
    uow: UoW = Depends(get_uow),
) -> UserService:
    user_repo = UserInterface(uow.session)
    return UserService(user_repo)
