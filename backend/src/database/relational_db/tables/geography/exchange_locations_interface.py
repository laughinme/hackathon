from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from utils import dist_expression
from .exchange_locations_table import ExchangeLocation


class ExchangeLocationsInterface:
    def __init__(self, session: AsyncSession):
        self.session = session
        
    async def list_all(
        self, 
        limit: int,
    ) -> list[ExchangeLocation]:
        locations = await self.session.scalars(
            select(ExchangeLocation).limit(limit)
        )
        return list(locations.all())
    
    async def list_filtered(
        self,
        limit: int,
        lat: float,
        lon: float,
        city_id: int
    ) -> list[ExchangeLocation]:
        stmt = (
            select(ExchangeLocation)
            .where(
                ExchangeLocation.city_id == city_id,
                ExchangeLocation.is_active == True
            )
            .order_by(dist_expression(ExchangeLocation, lat, lon))
            .limit(limit)
        )
        locations = await self.session.scalars(stmt)
        return list(locations.all())
    
    async def get_by_id(self, id: int) -> ExchangeLocation | None:
        return await self.session.scalar(
            select(ExchangeLocation)
            .where(ExchangeLocation.id == id)
        )

    async def nearest_point(self, lat: float, lon: float) -> "ExchangeLocation | None":
        return await self.session.scalar(
            select(ExchangeLocation)
            .order_by(dist_expression(ExchangeLocation, lat, lon))
            .limit(1)
        )
