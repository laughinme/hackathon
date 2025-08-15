from fastapi import APIRouter


def get_stats_router() -> APIRouter:
    router = APIRouter(prefix='/stats')

    # router.include_router(settlements_router)
    
    return router
