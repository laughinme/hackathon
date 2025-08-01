from fastapi import APIRouter


def get_me_router() -> APIRouter:
    from .profile import router as profile_router
    from .genres import router as genres_router
    
    router = APIRouter(prefix='/me')
    
    router.include_router(profile_router)
    router.include_router(genres_router)
    
    return router
