from uuid import UUID
from pydantic import EmailStr
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from utils.nearest_point import dist_expression
from .users_table import User


class UserInterface:
    def __init__(self, session: AsyncSession):
        self.session = session
    
    async def add(self, user: User) -> User:
        self.session.add(user)
        return user
    
    async def get_by_id(self, id: UUID | str) -> User | None:
        user = await self.session.scalar(
            select(User).where(User.id == id)
        )
        
        return user
    
    async def get_by_email(self, email: EmailStr) -> User | None:
        user = await self.session.scalar(
            select(User).where(User.email == email)
        )
        
        return user
    
    async def nearby_users(
        self,
        lat: float,
        lon: float,
        radius_km: int,
    ) -> list[User]:
        """
        Find users within a radius of `radius_km` kilometers
        using the Haversine formula directly in sql
        """
        dist_expr = dist_expression(User, lat, lon)

        stmt = (
            select(User, dist_expr.label("distance"))
            .where(
                dist_expr <= radius_km,
                User.public,
            )
            .order_by(dist_expr)
        )

        rows = await self.session.execute(stmt)

        users = []
        for user, dist_value in rows:
            setattr(user, "distance", dist_value)
            users.append(user)

        return users
