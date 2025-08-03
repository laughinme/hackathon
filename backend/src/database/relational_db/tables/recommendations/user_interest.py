from uuid import UUID
from sqlalchemy.orm import mapped_column, Mapped
from sqlalchemy import ForeignKey, Integer, Uuid, Float

from ..table_base import Base
from ..mixins import TimestampMixin


class UserInterest(TimestampMixin, Base):
    __tablename__ = "user_interest"

    user_id: Mapped[UUID] = mapped_column(
        Uuid(as_uuid=True), ForeignKey('users.id', ondelete='CASCADE'), primary_key=True
    )
    genre_id: Mapped[int] = mapped_column(
        Integer, ForeignKey('genres.id', ondelete='CASCADE'), primary_key=True
    )
    
    coef: Mapped[float] = mapped_column(Float, nullable=False, default=1)
