import logging
from asyncio import Lock

from sqlalchemy import text
from sqlalchemy.ext.asyncio import (
    AsyncEngine,
    AsyncSession,
    AsyncSessionTransaction,
    create_async_engine,
)

from src.app import constants
from src.app.config.config import Settings
from src.app.errors.db_exceptions import DbException

_logger = logging.getLogger(constants.ROOT)


class AsyncDatabaseConnection:
    _lock = Lock()
    _engine: AsyncEngine = None

    def __init__(self, settings: Settings):
        self._settings = settings

    async def create_engine(self):
        try:
            async with self._lock:
                if self._engine is None:
                    self._engine = create_async_engine(
                        self._settings.model_extra[constants.DOCUMENT_EMBEDDING_DB_URL.lower()],
                    )

            return self._engine
        except Exception as e:
            _logger.error(e)
            raise DbException("Failed to establish database connection") from e

    async def get_db_version(self):
        connection = await self._engine.connect()
        async with AsyncSession(connection).begin() as session_tx:
            session_tx: AsyncSessionTransaction
            result_cursor = await session_tx.session.execute(text("select version()"))
            version = result_cursor.fetchone()
            _logger.info(version)
        await connection.close()
        return version

    async def close(self):
        if self._engine:
            await self._engine.dispose()


class DocumentEmbeddingAsyncDatabaseConnection(AsyncDatabaseConnection):
    _instance = None

    @classmethod
    def get_instance(cls, settings: Settings):
        if cls._instance is None:
            cls._instance = cls(settings)
        return cls._instance
