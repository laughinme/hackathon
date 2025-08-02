from fastapi import APIRouter, Depends
from core.security import auth_user


def get_authors_router() -> APIRouter:
    from .list import router as list_router
    
    router = APIRouter(
        prefix='/authors',
    )

    router.include_router(list_router)
    
    return router
