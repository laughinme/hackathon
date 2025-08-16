from uuid import UUID
from datetime import datetime
from sqlalchemy import select, and_, or_, func
from sqlalchemy.ext.asyncio import AsyncSession

from .exchanges_table import Exchange
from domain.exchanges import ExchangeProgress


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

    async def admin_list_exchanges(
        self,
        *,
        progress: ExchangeProgress | None = None,
        only_active: bool | None = None,
        limit: int = 50,
        cursor_created_at: datetime | None = None,
        cursor_id: UUID | None = None,
    ) -> list[Exchange]:
        stmt = select(Exchange)

        if progress is not None:
            stmt = stmt.where(Exchange.progress == progress)
        elif only_active:
            stmt = stmt.where(Exchange.is_active)

        if cursor_created_at is not None and cursor_id is not None:
            stmt = stmt.where(
                or_(
                    Exchange.created_at < cursor_created_at,
                    and_(Exchange.created_at == cursor_created_at, Exchange.id < cursor_id),
                )
            )

        stmt = stmt.order_by(Exchange.created_at.desc(), Exchange.id.desc()).limit(limit)

        rows = await self.session.scalars(stmt)
        return list(rows.all())
    
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

    async def exists_finished_for_book(self, book_id: UUID, exclude_id: UUID | None = None) -> bool:
        stmt = select(func.count()).select_from(Exchange).where(
            Exchange.book_id == book_id,
            Exchange.progress == ExchangeProgress.FINISHED,
        )
        if exclude_id is not None:
            stmt = stmt.where(Exchange.id != exclude_id)

        result = await self.session.execute(stmt)
        count = result.scalar_one()
        return bool(count and count > 0)
