"""add trigram index to languages

Revision ID: df5967f18869
Revises: 531687f4114a
Create Date: 2025-08-05 16:57:55.769072

"""
from typing import Sequence, Union

from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision: str = 'df5967f18869'
down_revision: Union[str, Sequence[str], None] = '531687f4114a'
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    op.execute("CREATE EXTENSION IF NOT EXISTS pg_trgm")
    op.execute(
        "CREATE INDEX IF NOT EXISTS languages_name_ru_trgm "
        "ON languages USING gin (name_ru gin_trgm_ops)"
    )
    op.execute(
        "CREATE INDEX IF NOT EXISTS languages_name_en_trgm "
        "ON languages USING gin (name_en gin_trgm_ops)"
    )


def downgrade() -> None:
    op.execute("DROP INDEX IF EXISTS languages_name_ru_trgm")
    op.execute("DROP INDEX IF EXISTS languages_name_en_trgm")
