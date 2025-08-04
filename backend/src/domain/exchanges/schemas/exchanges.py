from uuid import UUID
from datetime import datetime, UTC, timedelta
from pydantic import BaseModel, Field, field_validator

from ..enums import ExchangeProgress
from ...books import BookModel
from ...users import UserShare
from ...common import TimestampModel

def ensure_utc_future(v: datetime | None) -> datetime | None:
    if v is None:
        return v
    if v.tzinfo is None or v.tzinfo != UTC:
        raise ValueError('datetime must be provided in UTC timezone (+00:00)')
    if v <= datetime.now(UTC):
        raise ValueError('datetime must be set in the future')

    return v

class ExchangeModel(TimestampModel):
    """
    Schema needed to represent Exchange object.
    `exchange_location` can be obtained from book nested object
    """
    id: UUID = Field(..., description='Exchange uuid')
    owner: UserShare
    requester: UserShare
    book: BookModel
    
    progress: ExchangeProgress
    cancel_reason: str | None = Field(None, description='Filled only if exchange was canceled')
    comment: str | None = Field(None, description='A comment requester can send to owner')
    is_active: bool = Field(...)
    
    meeting_time: datetime | None = Field(
        None,
        description='ISO-8601 datetime in UTC. No timezone specified throws an error'
    )
    # exchange_location: ExchangeLocation = Field()
    

class ExchangeCreate(BaseModel):
    """Exchange request creation schema"""
    meeting_time: datetime | None = Field(None)
    comment: str | None = Field(None, description='A comment requester can send to owner')
    
    _ensure_utc_future = field_validator('meeting_time', mode='after')(ensure_utc_future)


class ExchangeEdit(BaseModel):
    """Owner can edit terms of exchange with this schema."""
    meeting_time: datetime | None = Field(
        None,
        description='ISO-8601 datetime in UTC. No timezone specified throws an error'
    )

    _ensure_utc_future = field_validator('meeting_time', mode='after')(ensure_utc_future)


class ExchangeCancel(BaseModel):
    """Both owner and requester can cancel exchange with this schema"""
    cancel_reason: str | None = Field(None)
