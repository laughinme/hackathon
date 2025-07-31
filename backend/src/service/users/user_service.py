from fastapi import Request

from domain.auth import Role
from database.relational_db import UserInterface, User
from .exceptions import NotAuthenticated
from ..auth import TokenService


class UserService:
    def __init__(
        self,
        user_repo: UserInterface,
    ):
        self.user_repo = user_repo
