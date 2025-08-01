from sqlalchemy.orm import mapped_column, Mapped
from sqlalchemy import String, Boolean, select, ForeignKey, Integer, Float
from sqlalchemy.ext.asyncio import AsyncSession

from ..common import dist_expression
from ..table_base import Base
from ..mixins import TimestampMixin


class ExchangeLocation(TimestampMixin, Base):
    __tablename__ = "exchange_locations"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    city_id: Mapped[int] = mapped_column(
        Integer, ForeignKey('cities.id', ondelete='CASCADE'), nullable=False
    )
    
    name: Mapped[str] = mapped_column(String, nullable=False)
    latitude: Mapped[float] = mapped_column(Float, nullable=False)
    longitude: Mapped[float] = mapped_column(Float, nullable=False)
    
    is_active: Mapped[bool] = mapped_column(Boolean, nullable=False, default=True)
    

    @classmethod
    async def from_id(cls, session: AsyncSession, location_id: int) -> "ExchangeLocation | None":
        return await session.get(cls, location_id)
    
    @classmethod
    async def nearest_point(cls, session: AsyncSession, lat, lon) -> "ExchangeLocation | None":
        return await session.scalar(
            select(cls)
            .order_by(dist_expression(cls, lat, lon))
            .limit(1)
        )
