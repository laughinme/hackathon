from uuid import UUID
from fastapi import HTTPException

from database.relational_db import (
    CitiesInterface,
    User,
    ExchangeLocationsInterface,
    ExchangeLocation
)


class GeoService:
    def __init__(
        self,
        cities_repo: CitiesInterface,
        el_repo: ExchangeLocationsInterface,
    ):
        self.cities_repo = cities_repo
        self.el_repo = el_repo
        
    async def list_cities(self):
        cities = await self.cities_repo.list_all()
        return cities
    
    async def get_location(self, location_id: int):
        return await self.el_repo.get_by_id(location_id)
    
    async def list_locations(
        self,
        user: User,
        filter: bool,
        limit: int,
    ):
        if not filter:
            return await self.el_repo.list_all(limit)
        
        if user.latitude is None or user.longitude is None or user.city_id is None:
            raise HTTPException(412, detail='You should set your coordinates and city first')
        
        locations = await self.el_repo.list_filtered(limit, user.latitude, user.longitude, user.city_id)
        return locations
    
    async def nearest_point(self, user: User):
        if user.latitude is None or user.longitude is None:
            raise HTTPException(412, detail='You should set your coordinates first')
        
        return await self.el_repo.nearest_point(user.latitude, user.longitude)
