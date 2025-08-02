from uuid import UUID
from sqlalchemy.orm import mapped_column, Mapped, relationship
from sqlalchemy import Uuid, ForeignKey, Integer
from sqlalchemy.dialects.postgresql import ENUM

from domain.statistics import Interaction
from ..table_base import Base
from ..mixins import CreatedAtMixin


class BookEvent(CreatedAtMixin, Base):
    __tablename__ = "book_events"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    
    book_id: Mapped[UUID] = mapped_column(
        Uuid(as_uuid=True), ForeignKey('books.id', ondelete='CASCADE'), nullable=False
    )
    user_id: Mapped[UUID] = mapped_column(
        Uuid(as_uuid=True), ForeignKey('users.id', ondelete='CASCADE'), nullable=False
    )
    
    interaction: Mapped[Interaction] = mapped_column(ENUM(Interaction), nullable=False)
