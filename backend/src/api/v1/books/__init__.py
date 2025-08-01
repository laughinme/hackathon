from fastapi import APIRouter, Depends
from core.security import auth_user


def get_books_router() -> APIRouter:
    from .genres import get_genres_router
    from .book_id import get_specific_book_router
    
    router = APIRouter(
        prefix='/books',
        tags=['Books'],
        responses={401: {"description": "Not authorized"}}
    )

    router.include_router(get_genres_router())
    router.include_router(get_specific_book_router())
    
    return router
