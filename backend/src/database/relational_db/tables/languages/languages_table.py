from sqlalchemy.orm import Mapped, mapped_column
from sqlalchemy import String

from ..table_base import Base


class Language(Base):
    __tablename__ = "languages"

    code: Mapped[str] = mapped_column(String(2), primary_key=True)
    name_ru: Mapped[str] = mapped_column(String, nullable=False)
    name_en: Mapped[str] = mapped_column(String, nullable=False)
