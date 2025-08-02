from fastapi import Depends

from database.relational_db import (
    get_uow,
    UoW,
    CitiesInterface,
    ExchangeLocationsInterface,
)
from .geo_service import GeoService


async def get_geo_service(
    uow: UoW = Depends(get_uow),
) -> GeoService:
    cities_repo = CitiesInterface(uow.session)
    el_repo = ExchangeLocationsInterface(uow.session)
    return GeoService(cities_repo, el_repo)
