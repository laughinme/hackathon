from uuid import UUID
from sqlalchemy.orm import mapped_column, Mapped
from sqlalchemy import String, Integer
from sqlalchemy.ext.asyncio import AsyncSession

from ..table_base import Base


class Genre(Base):
    __tablename__ = "genres"

    id: Mapped[UUID] = mapped_column(Integer, primary_key=True, autoincrement=True)
    name: Mapped[str] = mapped_column(String, nullable=False)


    @classmethod
    async def from_id(cls, session: AsyncSession, genre_id: int):
        return await session.get(cls, genre_id)
