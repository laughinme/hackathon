from typing import Annotated
from uuid import UUID
from fastapi import APIRouter, Depends, Path, UploadFile, File

from database.relational_db import User
from domain.books import BookModel
from core.config import Settings
from core.security import auth_user
from service.books import BookService, get_books_service

router = APIRouter()
config = Settings() # pyright: ignore[reportCallIssue]


@router.put(
    "/photos",
    response_model=BookModel,
    summary="Upload one or more photos for a book"
)
async def upload_book_photos(
    book_id: Annotated[UUID, Path(...)],
    files: Annotated[list[UploadFile], File(..., description="JPEG or PNG files")],
    user: Annotated[User, Depends(auth_user)],
    svc: Annotated[BookService, Depends(get_books_service)]
):
    return await svc.add_photos(book_id, files, user)
