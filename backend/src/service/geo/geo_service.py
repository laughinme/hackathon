from uuid import UUID

from database.relational_db import (
    CitiesInterface
)


class GeoService:
    def __init__(
        self,
        cities_repo: CitiesInterface,
    ):
        self.cities_repo = cities_repo
        
    async def list_cities(self):
        genres = await self.cities_repo.list_all()
        return genres
