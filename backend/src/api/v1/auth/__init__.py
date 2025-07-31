from fastapi import APIRouter


def get_auth_routers() -> APIRouter:
    from .registration import router as register_router
    from .login import router as login_router
    from .refresh import router as refresh_router
    
    router = APIRouter(prefix='/auth', tags=['Auth'])
    
    router.include_router(register_router)
    router.include_router(login_router)
    router.include_router(refresh_router)
    
    return router
