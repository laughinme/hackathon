from uuid import UUID, uuid4
from datetime import datetime
from sqlalchemy.orm import mapped_column, Mapped, relationship
from sqlalchemy import ForeignKey, Integer, Uuid, String, Boolean, DateTime, select, Float
from sqlalchemy.dialects.postgresql import BYTEA, ARRAY
from sqlalchemy.ext.asyncio import AsyncSession

from domain.auth import Role
from ..common import dist_expression
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
    favorite_genres: Mapped[list[str]] = mapped_column(ARRAY(String, dimensions=1), nullable=False, default=[])
    
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
    
    
    # def set_location(self, lat: float, lon: float):
    #     self.location = func.ST_SetSRID(func.ST_MakePoint(lon, lat), 4326)

    @classmethod
    async def from_id(cls, session: AsyncSession, user_id: UUID | str) -> "User | None":
        return await session.get(cls, user_id)
    
    @classmethod
    async def by_email(cls, session: AsyncSession, email: str) -> "User | None":
        return await session.scalar(
            select(cls)
            .where(cls.email == email)
        )
        
    @classmethod
    async def nearby_users(
        cls,
        session: AsyncSession,
        lat: float,
        lon: float,
        radius_km: int = 5,
    ):
        """
        Find users within a radius of `radius_km` kilometers
        using the Haversine formula directly in sql
        """
        stmt = (
            select(cls)
            .where(dist_expression(cls, lat, lon) <= radius_km)
            .order_by(dist_expression(cls, lat, lon))
        )
        return (await session.scalars(stmt)).all()
