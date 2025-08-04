from uuid import UUID
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from .exchanges_table import Exchange
from ..recommendations import UserInterest
from ..statistics import BookStats
from ..users import User


class ExchangesInterface:
    def __init__(self, session: AsyncSession):
        self.session = session

    async def by_id(self, id: UUID) -> Exchange | None:
        exchange = await self.session.scalar(
            select(Exchange)
            .where(Exchange.id == id)
        )
        
        return exchange
    
    def add(self, obj: Exchange):
        self.session.add(obj)
        
    async def list_all(
        self, 
        only_active: bool = True, 
        limit: int = 50,
    ) -> list[Exchange]:
        stmt = (
            select(Exchange)
            .order_by(Exchange.created_at.desc())
            .limit(limit)
        )
       
        if only_active:
            stmt = stmt.where(Exchange.is_active)

        result = await self.session.scalars(stmt)
        return list(result)
    
    async def by_requester(
        self, requester_id: UUID, only_active: bool = True, limit: int = 50
    ) -> list[Exchange]:
        stmt = (
            select(Exchange)
            .where(Exchange.requester_id == requester_id)
            .order_by(Exchange.created_at.desc())
            .limit(limit)
        )
       
        if only_active:
            stmt = stmt.where(Exchange.is_active)

        result = await self.session.scalars(stmt)
        return list(result)

    async def by_owner(
        self, owner_id: UUID, only_active: bool = True, limit: int = 50
    ) -> list[Exchange]:
        stmt = (
            select(Exchange)
            .where(Exchange.owner_id == owner_id)
            .order_by(Exchange.created_at.desc())
            .limit(limit)
        )
        if only_active:
            stmt = stmt.where(Exchange.is_active)

        result = await self.session.scalars(stmt)
        return list(result)

    async def by_book_for_requester(self, book_id: UUID, user_id: UUID) -> Exchange | None:
        return await self.session.scalar(
            select(Exchange)
            .where(
                Exchange.book_id == book_id,
                Exchange.requester_id == user_id
            )
        )
