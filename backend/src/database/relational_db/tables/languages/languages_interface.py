from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from .languages_table import Language


class LanguagesInterface:
    def __init__(self, session: AsyncSession):
        self.session = session

    async def list_all(self) -> list[Language]:
        languages = await self.session.scalars(select(Language))
        return list(languages.all())
