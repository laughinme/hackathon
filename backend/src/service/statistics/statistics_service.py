from uuid import UUID
from fastapi import HTTPException, status

from core.config import Settings
from database.relational_db import (
    UoW,
    User,
    BookEvent,
    BookEventsInterface
)
from domain.statistics import Interaction

settings = Settings() # type: ignore

class StatService:
    def __init__(
        self,
        uow: UoW,
        bv_repo: BookEventsInterface,
    ):
        self.uow = uow
        self.bv_repo = bv_repo
    
    async def record_click(self, book_id: UUID, user: User):
        await self.bv_repo.record_event(
            book_id, user.id, Interaction.CLICK
        )
        
    async def like_book(self, book_id: UUID, user: User):
        await self.bv_repo.record_event(
            book_id, user.id, Interaction.LIKE
        )
        
    async def reserve_book(self, book_id: UUID, user: User):
        await self.bv_repo.record_event(
            book_id, user.id, Interaction.RESERVE
        )
