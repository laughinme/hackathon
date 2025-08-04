from fastapi import APIRouter

def get_exchanges_router() -> APIRouter:
    # from .authors import get_authors_router
    
    router = APIRouter(
        prefix='/exchanges',
        tags=['Exchanges'],
        responses={401: {"description": "Not authorized"}}
    )

    # router.include_router(get_authors_router())
    
    return router
