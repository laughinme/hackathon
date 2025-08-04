from uuid import UUID
from sqlalchemy import select, func
from sqlalchemy.ext.asyncio import AsyncSession

from .exchanges_table import Exchange
from ..recommendations import UserInterest
from ..statistics import BookStats
from ..geography import ExchangeLocation
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
    