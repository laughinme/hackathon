from fastapi import APIRouter, Depends


def get_cities_router() -> APIRouter:
    from .list import router as list_router
    
    router = APIRouter(
        prefix='/cities',
    )

    router.include_router(list_router)
    
    return router
