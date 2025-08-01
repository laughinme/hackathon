from fastapi import APIRouter, Depends
from core.security import auth_user


def get_genres_router() -> APIRouter:
    from .list import router as list_router
    
    router = APIRouter(
        prefix='/genres',
    )

    router.include_router(list_router)
    
    return router
