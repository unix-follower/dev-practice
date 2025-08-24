from typing import TypeVar

from sqlalchemy.ext.asyncio import AsyncSession

PK = TypeVar("PK")
T = TypeVar("T")


class BaseRepository[PK, T]:
    def __init__(self, session: AsyncSession):
        self._session = session
