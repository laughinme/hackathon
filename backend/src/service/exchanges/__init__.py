from fastapi import Depends

from database.relational_db import (
    get_uow,
    UoW,
    BookEventsInterface,
    UserInterestInterface,
    BooksInterface,
    ExchangesInterface
)
from .exchanges_service import ExchangeService


async def get_exchanges_service(
    uow: UoW = Depends(get_uow),
) -> ExchangeService:
    book_repo = BooksInterface(uow.session)
    ex_repo = ExchangesInterface(uow.session)
    
    return ExchangeService(uow, book_repo, ex_repo)
