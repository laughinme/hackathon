from uuid import UUID
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from domain.statistics import Interaction
from .book_events_table import BookEvent
from ..geography import ExchangeLocation


class BookEventsInterface:
    def __init__(self, session: AsyncSession):
        self.session = session
        
    def add(self, book_view: BookEvent):
        self.session.add(book_view)
        
    async def record_event(
        self, 
        book_id: UUID,
        user_id: UUID, 
        interaction: Interaction
    ):
        event = BookEvent(
            book_id=book_id,
            user_id=user_id,
            interaction=interaction
        )
        self.add(event)
