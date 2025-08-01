"""seed data

Revision ID: c8c7d89089f9
Revises: 84c5fdd1ed30
Create Date: 2025-08-01 14:32:56.436706

"""
from typing import Sequence, Union

from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision: str = 'c8c7d89089f9'
down_revision: Union[str, Sequence[str], None] = '84c5fdd1ed30'
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


GENRES = [
    "Фэнтези",
    "Научная фантастика",
    "Эпическое фэнтези",
    "Героическое фэнтези",
    "Городское фэнтези",
    "Космическая опера",
    "Дистопия",
    "Постапокалипсис",
    "Детектив",
    "Криминальный триллер",
    "Полиция и расследование",
    "Триллер",
    "Психологический триллер",
    "Шпионский роман",
    "Исторический роман",
    "Военная проза",
    "Приключения",
    "Морские приключения",
    "Хоррор",
    "Готический роман",
    "Мистика",
    "Магический реализм",
    "Литературная проза",
    "Любовный роман",
    "Современный любовный роман",
    "Юмористическая проза",
    "Биография",
    "Мемуары",
    "История",
    "Популярная психология",
    "Саморазвитие",
    "Деловая литература",
    "Экономика",
    "Политология",
    "Социология",
    "Популярная наука",
    "Астрономия",
    "Физика",
    "Биология",
    "Медицина",
    "Кулинария",
    "Путешествия",
    "Искусство",
    "Музыка",
    "Фотография",
    "Комиксы и графические романы",
    "Манга",
    "Детская литература",
    "Подростковая (YA)",
    "Поэзия",
]

AUTHORS = [
    "Уильям Шекспир",
    "Лев Толстой",
    "Фёдор Достоевский",
    "Александр Пушкин",
    "Чарльз Диккенс",
    "Джейн Остин",
    "Франц Кафка",
    "Джордж Оруэлл",
    "Эрнест Хемингуэй",
    "Джек Лондон",
    "Марк Твен",
    "Артур Конан Дойл",
    "Агата Кристи",
    "Жюль Верн",
    "Герберт Уэллс",
    "Айзек Азимов",
    "Рэй Брэдбери",
    "Роберт Хайнлайн",
    "Дж. Р. Р. Толкин",
    "К. С. Льюис",
    "Джордж Мартин",
    "Джоан Роулинг",
    "Стивен Кинг",
    "Нил Гейман",
    "Филип Дик",
    "Умберто Эко",
    "Габриэль Гарсиа Маркес",
    "Харуки Мураками",
    "Чак Паланик",
    "Дан Браун",
    "Пауло Коэльо",
    "Эрих Мария Ремарк",
    "Виктор Гюго",
    "Александр Дюма",
    "Оноре де Бальзак",
    "Альбер Камю",
    "Кадзуо Исигуро",
    "Владимир Набоков",
    "Михаил Булгаков",
    "Николай Гоголь",
    "Иван Тургенев",
    "Джон Гришэм",
    "Маргарет Этвуд",
    "Салман Рушди",
    "Тони Моррисон",
    "Эдгар Аллан По",
    "Герман Гессе",
    "Том Клэнси",
    "Лиана Мориарти",
    "Ю Несбё",
]

CITY_NAME = "Новосибирск"


def _get_table(name: str):
    """Return a lightweight table definition for bulk_insert."""
    return sa.table(
        name,
        sa.column("id", sa.Integer),
        sa.column("name", sa.String),
    )


def upgrade() -> None:
    """Upgrade schema."""
    genres_table = _get_table("genres")
    authors_table = _get_table("authors")
    cities_table = _get_table("cities")

    op.bulk_insert(
        genres_table,
        [{"id": idx, "name": name} for idx, name in enumerate(GENRES, start=1)],
    )

    op.bulk_insert(
        authors_table,
        [{"id": idx, "name": name} for idx, name in enumerate(AUTHORS, start=1)],
    )

    op.bulk_insert(
        cities_table,
        [{"id": 1, "name": CITY_NAME}],
    )


def downgrade() -> None:
    """Downgrade schema."""
    conn = op.get_bind()

    conn.execute(sa.text("DELETE FROM cities WHERE name = :name"), {"name": CITY_NAME})

    conn.execute(
        sa.text(
            "DELETE FROM authors WHERE name = ANY(:names)"
        ),
        {"names": AUTHORS},
    )

    conn.execute(
        sa.text(
            "DELETE FROM genres WHERE name = ANY(:names)"
        ),
        {"names": GENRES},
    )
