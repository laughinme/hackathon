from fastapi import APIRouter


def get_books_router() -> APIRouter:
    from .graphs import router as graphs_router
    from .book_id import get_book_id_router
    
    router = APIRouter(prefix='/books')

    router.include_router(graphs_router)
    router.include_router(get_book_id_router())
    
    return router
