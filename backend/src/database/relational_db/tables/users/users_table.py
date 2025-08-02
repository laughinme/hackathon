from uuid import UUID, uuid4
from datetime import datetime, date
from sqlalchemy.orm import mapped_column, Mapped, relationship
from sqlalchemy import ForeignKey, Integer, Uuid, String, Boolean, DateTime, Float, Date
from sqlalchemy.dialects.postgresql import BYTEA, ENUM

from domain.auth import Role
from domain.users import Gender
from ..table_base import Base
from ..mixins import TimestampMixin


class User(TimestampMixin, Base):
    __tablename__ = "users"

    id: Mapped[UUID] = mapped_column(Uuid(as_uuid=True), default=uuid4, primary_key=True)
    
    # Credentials
    email: Mapped[str] = mapped_column(String, nullable=False, unique=True)
    password_hash: Mapped[bytes] = mapped_column(BYTEA, nullable=False)
    confirmed_at: Mapped[datetime | None] = mapped_column(DateTime(timezone=True), nullable=True)
    
    # Profile info
    username: Mapped[str | None] = mapped_column(String, nullable=True)
    avatar_url: Mapped[str | None] = mapped_column(String, nullable=True)
    bio: Mapped[str | None] = mapped_column(String, nullable=True)
    birth_date: Mapped[date | None] = mapped_column(Date, nullable=True)
    gender: Mapped[Gender | None] = mapped_column(ENUM(Gender), nullable=True)
    
    # Geography
    city_id: Mapped[int | None] = mapped_column(
        Integer, ForeignKey('cities.id', ondelete='RESTRICT'), nullable=True
    )
    latitude: Mapped[float | None] = mapped_column(Float, nullable=True)
    longitude: Mapped[float | None] = mapped_column(Float, nullable=True)
    
    # Service
    # role: Mapped[Role] = mapped_column(Enum(Role), nullable=False, default=Role.GUEST)
    is_onboarded: Mapped[bool] = mapped_column(Boolean, nullable=False, default=False)
    banned: Mapped[bool] = mapped_column(Boolean, nullable=False, default=False)
    
    city: Mapped['City'] = relationship(lazy='selectin') # type: ignore
    favorite_genres: Mapped[list['Genre']] = relationship( # type: ignore
        lazy='selectin',
        secondary='user_favorite_genres'
    )
    books: Mapped[list['Book']] = relationship(lazy='selectin') # type: ignore
    
    @property
    def age(self) -> int | None:
        if not self.birth_date:
            return None
        today = date.today()
        return (
            today.year - self.birth_date.year
            - ((today.month, today.day) < (self.birth_date.month, self.birth_date.day))
        )
