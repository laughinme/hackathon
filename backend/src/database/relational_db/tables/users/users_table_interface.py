from uuid import UUID
from pydantic import EmailStr
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from .users_table import User


class UserInterface:
    def __init__(self, session: AsyncSession):
        self.session = session
    
    async def add(self, user: User) -> User:
        self.session.add(user)
        return user
    
    
    async def get_by_id(self, id: UUID) -> User | None:
        user = await self.session.scalar(
            select(User)
            .where(
                User.id == id
            )
        )
        
        return user
    
    async def get_by_email(self, email: EmailStr) -> User | None:
        user = await self.session.scalar(
            select(User)
            .where(
                User.email == email
            )
        )
        
        return user
