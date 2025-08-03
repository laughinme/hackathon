from uuid import UUID
from sqlalchemy import update
from sqlalchemy.ext.asyncio import AsyncSession

from domain.statistics import Interaction
from .book_stats_table import BookStats


class BookStatsInterface:
    def __init__(self, session: AsyncSession):
        self.session = session
    
    async def add(self, stats: BookStats) -> BookStats:
        self.session.add(stats)
        return stats

    async def update_book_interaction(
        self, 
        book_id: UUID,
        interaction: Interaction
    ):
        stmt = (
            update(BookStats)
            .where(BookStats.book_id == book_id)
        )
        match interaction:
            case Interaction.CLICK:
                stmt = stmt.values(views=BookStats.views + 1)
            case Interaction.LIKE:
                stmt = stmt.values(likes=BookStats.likes + 1)
            case Interaction.RESERVE:
                stmt = stmt.values(reserves=BookStats.reserves + 1)
            
        await self.session.execute(stmt)
