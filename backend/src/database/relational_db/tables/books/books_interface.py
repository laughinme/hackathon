from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from .genres_table import Genre


class BooksInterface:
    def __init__(self, session: AsyncSession):
        self.session = session
