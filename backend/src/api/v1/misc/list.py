from typing import Annotated
from fastapi import APIRouter, Depends

from domain.misc import LanguageModel
from service.users import UserService, get_user_service


router = APIRouter()


@router.get(
    path='/',
    response_model=list[LanguageModel],
    summary='List all languages'
)
async def list_languages(
    svc: Annotated[UserService, Depends(get_user_service)],
):
    return await svc.list_languages()
