from uuid import UUID
from sqlalchemy.orm import mapped_column, Mapped
from sqlalchemy import ForeignKey, Integer, Uuid

from ..table_base import Base
from ..mixins import TimestampMixin


class BookStats(TimestampMixin, Base):
    __tablename__ = "book_stats"

    book_id: Mapped[UUID] = mapped_column(
        Uuid(as_uuid=True), ForeignKey('books.id', ondelete='CASCADE'), primary_key=True
    )
    
    views: Mapped[int] = mapped_column(Integer, nullable=False, default=0)
    likes: Mapped[int] = mapped_column(Integer, nullable=False, default=0)
    reserves: Mapped[int] = mapped_column(Integer, nullable=False, default=0)
