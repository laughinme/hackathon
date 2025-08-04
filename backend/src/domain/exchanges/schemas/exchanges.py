from uuid import UUID
from datetime import datetime
from pydantic import BaseModel, Field

from ..enums import ExchangeProgress
from ...geo import ExchangeLocation
from ...books import BookModel
from ...users import UserShare
from ...common import TimestampModel


class ExchangeCreate(BaseModel):
    meeting_time: datetime | None = Field(None)
    comment: str | None = Field(None, description='A comment requester can send to owner')

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
    
    meeting_time: datetime | None = Field(None)
    # exchange_location: ExchangeLocation = Field()
