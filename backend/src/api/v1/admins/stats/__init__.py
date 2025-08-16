from fastapi import APIRouter


def get_stats_router() -> APIRouter:
    from .users import router as users_router
    from .books import get_books_router
    
    router = APIRouter(prefix='/stats')

    router.include_router(users_router)
    router.include_router(get_books_router())
    
    return router
