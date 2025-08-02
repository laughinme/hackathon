from fastapi import APIRouter, Depends


def get_exchange_locations_router() -> APIRouter:
    from .list import router as list_router
    from .nearest_point import router as nearest_point_router
    
    router = APIRouter(
        prefix='/exchange_locations',
        responses={412: {"description": "You should complete onboarding first"}},
    )

    router.include_router(list_router)
    router.include_router(nearest_point_router)
    
    return router
