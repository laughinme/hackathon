from sqlalchemy.ext.asyncio import AsyncSession


class UoW:
    """Unit-of-Work: single transaction, single session."""
    def __init__(self, session: AsyncSession):
        self.session = session

    async def __aenter__(self):
        return self

    async def __aexit__(self, exc_type, *_):
        if exc_type is None:
            await self.session.commit()
        else:
            await self.session.rollback()
