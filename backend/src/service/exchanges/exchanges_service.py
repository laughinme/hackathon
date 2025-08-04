import logging

from uuid import UUID
from fastapi import HTTPException

from core.config import Settings
from database.relational_db import (
    UoW,
    BooksInterface,
    User,
    ExchangesInterface,
    Exchange,
    Book,
)
from domain.exchanges import ExchangeCreate, ExchangeProgress

settings = Settings() # type: ignore
logger = logging.getLogger(__name__)

class ExchangeService:
    def __init__(
        self,
        uow: UoW,
        books_repo: BooksInterface,
        ex_repo: ExchangesInterface,
        
    ):
        self.uow = uow
        self.books_repo = books_repo
        self.ex_repo = ex_repo
        
    async def _ensure_book(self, book_id: UUID) -> Book:
        book = await self.books_repo.by_id(book_id)
        if book is None:
            raise HTTPException(404, detail='Book with this `book_id` not found.')
        return book
        
    async def request_exchange(self, book_id: UUID, user: User, payload: ExchangeCreate):
        book = await self._ensure_book(book_id)
        if book.owner_id == user.id:
            raise HTTPException(400, detail="You can't reserve your own book")
        
        exchange = Exchange(
            book_id=book_id,
            owner_id=book.owner_id,
            requester_id=user.id,
            exchange_location_id=book.exchange_location_id,
            progress=ExchangeProgress.CREATED,
            meeting_time=payload.meeting_time,
            comment=payload.comment,
        )
        self.ex_repo.add(exchange)
        await self.uow.session.flush()
        
        await self.uow.session.refresh(
            exchange, 
            ['book', 'exchange_location', 'owner', 'requester']
        )
        return exchange
