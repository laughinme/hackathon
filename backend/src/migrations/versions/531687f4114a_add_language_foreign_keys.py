"""add language foreign keys, migrate data

Revision ID: 531687f4114a
Revises: 92933b07e67c
Create Date: 2025-08-05 15:21:54.954549

This revision makes the *existing* ``books.language`` (NOT NULL) and ``users.language``
columns into foreign key references to the lookup table ``languages(code)``.

Steps:
1.  Add a new column ``language_code`` (initially **nullable**) to *books* and
   *users*.
2.  Copy all existing values from ``language`` → ``language_code``.
3.  Make ``books.language_code`` **NOT NULL** (``users`` may stay nullable).
4.  Add foreign key constraints with deterministic names so they can be
   dropped later.
5.  Remove the old ``language`` columns.

The *downgrade* path reverses this and copies the data back.
"""
from __future__ import annotations

from typing import Sequence, Union

from alembic import op
import sqlalchemy as sa

# revision identifiers, used by Alembic.
revision: str = "531687f4114a"
down_revision: Union[str, Sequence[str], None] = "92933b07e67c"
branch_labels = None
depends_on = None

FK_BOOKS = "fk_books_language_code"
FK_USERS = "fk_users_language_code"


def upgrade() -> None:  # noqa: D401
    """Upgrade schema and migrate language data to the lookup FK."""
    conn = op.get_bind()

    # 1. Add new columns (nullable=True for the data‑migration phase)
    op.add_column(
        "books",
        sa.Column("language_code", sa.String(length=2), nullable=True),
    )
    op.add_column(
        "users",
        sa.Column("language_code", sa.String(length=2), nullable=True),
    )

    # 2. Copy existing data
    conn.execute(sa.text("UPDATE books SET language_code = language"))
    conn.execute(sa.text("UPDATE users SET language_code = language"))

    # 3. Tighten nullability for books (books.language had NOT NULL before)
    op.alter_column("books", "language_code", nullable=False)

    # 4. Create FK constraints (explicit names for easy downgrade)
    op.create_foreign_key(FK_BOOKS, "books", "languages", ["language_code"], ["code"])
    op.create_foreign_key(FK_USERS, "users", "languages", ["language_code"], ["code"])

    # 5. Drop old columns
    op.drop_column("books", "language")
    op.drop_column("users", "language")



def downgrade() -> None:  # noqa: D401
    """Revert to the pre‑FK schema, preserving data."""
    conn = op.get_bind()

    # 1. Re‑add plain ``language`` columns
    op.add_column(
        "books",
        sa.Column("language", sa.String(length=2), nullable=False),
    )
    op.add_column(
        "users",
        sa.Column("language", sa.String(length=2), nullable=True),
    )

    # 2. Copy data back from *_code
    conn.execute(sa.text("UPDATE books SET language = language_code"))
    conn.execute(sa.text("UPDATE users SET language = language_code"))

    # 3. Drop FK constraints and the *_code columns
    op.drop_constraint(FK_USERS, "users", type_="foreignkey")
    op.drop_column("users", "language_code")

    op.drop_constraint(FK_BOOKS, "books", type_="foreignkey")
    op.drop_column("books", "language_code")
