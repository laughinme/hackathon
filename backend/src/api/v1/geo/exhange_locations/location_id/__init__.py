from fastapi import APIRouter


def get_specific_location_router() -> APIRouter:
    from .info import router as info_router
    
    router = APIRouter(
        prefix='/{location_id}'
    )

    router.include_router(info_router)
    
    return router
