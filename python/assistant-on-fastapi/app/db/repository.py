from abc import ABC
from typing import Generic, TypeVar

from sqlalchemy.ext.asyncio import AsyncSession

PK = TypeVar("PK")
T = TypeVar("T")


# pylint: disable=too-few-public-methods
class AbstractRepository(ABC, Generic[PK, T]):
    def __init__(self, session: AsyncSession):
        self._session = session
