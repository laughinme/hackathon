from uuid import UUID
from sqlalchemy import update
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.dialects.postgresql import insert

from .user_interest import UserInterest


class UserInterestInterface:
    def __init__(self, session: AsyncSession):
        self.session = session
    
    async def add(self, ui: UserInterest) -> UserInterest:
        self.session.add(ui)
        return ui

    async def upsert(self, values: list[dict]) -> None:
        await self.session.execute(
            insert(UserInterest)
            .values(values)
            .on_conflict_do_nothing()
        )
        
    async def edit_coef(self, coef_delta: float, genre_id: int, user_id: UUID):
        stmt = (
            update(UserInterest)
            .where(
                UserInterest.genre_id == genre_id,
                UserInterest.user_id == user_id,
            )
            .values(coef=UserInterest.coef + coef_delta)
        )
        await self.session.execute(stmt)
