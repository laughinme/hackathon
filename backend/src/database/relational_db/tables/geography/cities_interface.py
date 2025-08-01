from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from .cities_table import City


class CitiesInterface:
    def __init__(self, session: AsyncSession):
        self.session = session
        
    async def list_all(self) -> list[City]:
        cities = await self.session.scalars(
            select(City)
        )
        return list(cities.all())
    
    async def get_by_id(self, id: int) -> City | None:
        return await self.session.scalar(
            select(City)
            .where(City.id == id)
        )
