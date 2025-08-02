from fastapi import APIRouter, Depends


def get_specific_book_router() -> APIRouter:
    from .info import router as info_router
    from .photos import router as photos_router
    
    router = APIRouter(
        prefix='/{book_id}'
    )

    router.include_router(info_router)
    router.include_router(photos_router)
    
    return router
