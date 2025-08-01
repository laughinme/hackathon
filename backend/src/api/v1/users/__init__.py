from fastapi import APIRouter, Depends
from core.security import auth_user


def get_users_router() -> APIRouter:
    from .me import get_me_router
    
    router = APIRouter(
        prefix='/users',
        tags=['Users'],
        # dependencies=[Depends(auth_user)],
        responses={401: {"description": "Not authorized"}}
    )

    router.include_router(get_me_router())
    
    return router
