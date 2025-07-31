from uuid import UUID, uuid4
from datetime import datetime
from sqlalchemy.orm import mapped_column, Mapped, relationship
from sqlalchemy import Uuid, String, Boolean, DateTime
from sqlalchemy.dialects.postgresql import BYTEA
from sqlalchemy.ext.asyncio import AsyncSession

from domain.auth import Role
from ..table_base import Base
from ..mixins import TimestampMixin


class User(TimestampMixin, Base):
    __tablename__ = "users"

    id: Mapped[UUID] = mapped_column(Uuid(as_uuid=True), default=uuid4, primary_key=True)
    
    email: Mapped[str] = mapped_column(String, nullable=False)
    password_hash: Mapped[bytes] = mapped_column(BYTEA, nullable=False)
    # allow_password_login: Mapped[bool] = mapped_column(Boolean, nullable=False, default=False)
    confirmed_at: Mapped[datetime] = mapped_column(DateTime(timezone=True), nullable=True)
    
    username: Mapped[str | None] = mapped_column(String, nullable=True)
    avatar_url: Mapped[str | None] = mapped_column(String, nullable=True)
    
    # role: Mapped[Role] = mapped_column(Enum(Role), nullable=False, default=Role.GUEST)
    banned: Mapped[bool] = mapped_column(Boolean, nullable=False, default=False)


    @classmethod
    async def from_id(cls, session: AsyncSession, user_id: UUID | str):
        return await session.get(cls, user_id)
