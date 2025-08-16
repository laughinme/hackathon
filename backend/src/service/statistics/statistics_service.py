from uuid import UUID
from fastapi import HTTPException

from core.config import Settings
from database.relational_db import (
    UoW,
    User,
    BookEventsInterface,
    UserInterestInterface,
    BooksInterface,
    BookStatsInterface,
    UserInterface,
)
from domain.statistics import Interaction

settings = Settings() # type: ignore

class StatService:
    def __init__(
        self,
        uow: UoW,
        be_repo: BookEventsInterface,
        ui_repo: UserInterestInterface,
        book_repo: BooksInterface,
        bs_repo: BookStatsInterface,
        user_repo: UserInterface,
    ):
        self.uow = uow
        self.be_repo = be_repo
        self.ui_repo = ui_repo
        self.book_repo = book_repo
        self.bs_repo = bs_repo
        self.user_repo = user_repo
        
    async def record_interaction(
        self, 
        book_id: UUID, 
        user: User, 
        interaction: Interaction
    ):
        event_coef = {
            Interaction.CLICK: 1,
            Interaction.LIKE: 3,
            Interaction.RESERVE: 5
        }
        
        book = await self.book_repo.by_id(book_id)
        if book is None:
            raise HTTPException(404, 'Book with this id not found')
        
        event_id = await self.be_repo.record_event(book_id, user.id, interaction)
        if event_id is None:
            await self.ui_repo.edit_coef(-event_coef[interaction], book.genre_id, user.id)  
        else:
            await self.ui_repo.edit_coef(event_coef[interaction], book.genre_id, user.id)  
            await self.bs_repo.update_book_interaction(book_id, interaction)

    async def set_interests(self, genre_ids: set[int], user: User):
        coef = 5 # Adjustable
        records = [
            {'user_id': user.id, 'genre_id': id, 'coef': coef} 
            for id in genre_ids
        ]
        
        await self.ui_repo.upsert(records)
        
    async def active_users(self, days: int):
        return await self.be_repo.users_by_day(days)

    async def new_registrations(self, days: int):
        return await self.user_repo.registrations_by_days(days)

    async def books_stats(self, days: int, book_id: UUID | None = None):
        return await self.be_repo.stats_by_days(days, book_id)
