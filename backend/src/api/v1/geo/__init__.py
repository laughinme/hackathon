from fastapi import APIRouter, Depends
from core.security import auth_user


def get_geo_router() -> APIRouter:
    from .cities import get_cities_router
    
    router = APIRouter(
        prefix='/geo',
        tags=['Geography'],
        responses={401: {"description": "Not authorized"}}
    )

    router.include_router(get_cities_router())
    
    return router
