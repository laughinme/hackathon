from fastapi import APIRouter


def get_misc_router() -> APIRouter:
    from .list import router as list_router

    router = APIRouter(
        prefix='/languages',
        tags=['Languages'],
        responses={401: {"description": "Not authorized"}}
    )

    router.include_router(list_router)

    return router
