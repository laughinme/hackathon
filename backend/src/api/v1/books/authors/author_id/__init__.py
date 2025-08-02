from fastapi import APIRouter, Depends
from core.security import auth_user


def get_specific_author_router() -> APIRouter:
    from .info import router as info_router
    
    router = APIRouter(
        prefix='/{author_id}'
    )

    router.include_router(info_router)
    
    return router
