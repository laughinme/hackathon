from typing import Literal
from fastapi import Request
from sqlalchemy.exc import IntegrityError
from passlib.context import CryptContext

from database.relational_db import (
    UserInterface,
    User,
    UoW,
)
from domain.auth import UserRegister, UserLogin
from core.config import Settings
from .exceptions import AlreadyExists, WrongCredentials
from ..tokens import TokenService


config = Settings() # pyright: ignore[reportCallIssue]
pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")

class CredentialsService:
    def __init__(
        self,
        request: Request,
        user_repo: UserInterface,
        token_service: TokenService,
        uow: UoW,
    ):
        self.user_repo = user_repo
        self.token_service = token_service
        self.uow = uow
        self.request = request
        
    @staticmethod
    def _check_password(password: str, password_hash: bytes) -> bool:
        try:
            valid = pwd_context.verify(password.encode(), password_hash)
            if not valid:
                raise WrongCredentials()
        except ValueError:
            raise WrongCredentials()
        
        return valid
        
    @staticmethod
    def _hash_password(password: str) -> bytes:
        return pwd_context.hash(password).encode()
    
    
    async def register(
        self,
        payload: UserRegister,
        src: Literal['web', 'mobile']
    ) -> tuple[str, str, str]:
        
        password_hash = self._hash_password(payload.password)

        user = User(
            email=payload.email,
            password_hash=password_hash,
            # allow_password_login=True,
            username=payload.username
        )
        
        await self.user_repo.add(user)
        
        try:
            await self.uow.session.flush()
        except IntegrityError as e:
            raise AlreadyExists()
        
        access, refresh, csrf = await self.token_service.issue_tokens(user.id, src)
        return access, refresh, csrf
    
    
    async def login(
        self,
        payload: UserLogin,
        src: Literal['web', 'mobile']
    ) -> tuple[str, str, str]:
        user = await self.user_repo.get_by_email(payload.email)
        if user is None:
            raise WrongCredentials()

        self._check_password(payload.password, user.password_hash)
        
        access, refresh, csrf = await self.token_service.issue_tokens(user.id, src)
        return access, refresh, csrf
    
    
    async def logout(self, refresh_token: str) -> None:
        payload = await self.token_service.revoke(refresh_token)
        if payload is None:
            raise WrongCredentials()
