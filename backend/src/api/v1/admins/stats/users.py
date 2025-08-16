from typing import Annotated
from fastapi import APIRouter, Depends, Query

from database.relational_db import User
from domain.statistics import ActiveUsersGraph
from core.config import Settings
from core.security import auth_admin
from service.statistics import StatService, get_stats_service

router = APIRouter()
config = Settings() # pyright: ignore[reportCallIssue]


@router.get(    
    path='/active-users',
    response_model=list[ActiveUsersGraph],
    summary='Get graph data for active users by days',
)
async def active_users(
    _: Annotated[User, Depends(auth_admin)],
    svc: Annotated[StatService, Depends(get_stats_service)],
    days: int = Query(30, description='Number of days back to retrieve data for'),
):
    return await svc.active_users(days)
