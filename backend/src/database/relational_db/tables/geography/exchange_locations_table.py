from sqlalchemy.orm import mapped_column, Mapped, relationship
from sqlalchemy import String, Boolean, ForeignKey, Integer, Float
from sqlalchemy.dialects.postgresql import ARRAY

from ..table_base import Base
from ..mixins import TimestampMixin


class ExchangeLocation(TimestampMixin, Base):
    __tablename__ = "exchange_locations"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    city_id: Mapped[int] = mapped_column(
        Integer, ForeignKey('cities.id', ondelete='CASCADE'), nullable=False
    )
    
    title: Mapped[str] = mapped_column(String, nullable=False)
    description: Mapped[str] = mapped_column(String, nullable=True)
    
    opening_hours: Mapped[str | None] = mapped_column(String, nullable=True, comment='OSM format: Mo-Fr 09:00-19:00; Sa ...')
    # photo_urls: Mapped[list[str]] = mapped_column(ARRAY(String, dimensions=1), nullable=True)
    
    # Navigation
    address: Mapped[str] = mapped_column(String, nullable=False)
    directions: Mapped[str | None] = mapped_column(String, nullable=True, comment='Как добраться')
    latitude: Mapped[float] = mapped_column(Float, nullable=False)
    longitude: Mapped[float] = mapped_column(Float, nullable=False)
    
    is_active: Mapped[bool] = mapped_column(Boolean, nullable=False, default=True)
    
    city: Mapped['City'] = relationship(lazy='selectin') # type: ignore
