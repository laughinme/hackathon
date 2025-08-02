from fastapi import APIRouter

def get_books_router() -> APIRouter:
    from .authors import get_authors_router
    from .genres import get_genres_router
    from .book_id import get_specific_book_router
    from .list import router as list_router
    from .for_you import router as for_you_router
    from .create import router as create_router
    
    router = APIRouter(
        prefix='/books',
        tags=['Books'],
        responses={401: {"description": "Not authorized"}}
    )

    router.include_router(get_authors_router())
    router.include_router(get_genres_router())
    router.include_router(get_specific_book_router())
    router.include_router(list_router)
    router.include_router(for_you_router)
    router.include_router(create_router)
    
    return router
