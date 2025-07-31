from fastapi import APIRouter


def get_v1_router() -> APIRouter:
    from .users import get_users_router
    from .auth import get_auth_routers
    from .admins import get_admins_router

    router = APIRouter(prefix='/v1')

    router.include_router(get_users_router())
    router.include_router(get_auth_routers())
    router.include_router(get_admins_router())
    
    return router
