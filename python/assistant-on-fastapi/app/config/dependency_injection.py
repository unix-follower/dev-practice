from fastapi import Depends
from sqlalchemy.ext.asyncio import AsyncEngine

from app import constants
from app.config.config import Settings
from app.config.object_factory import ObjectFactory
from app.db.database import DocumentEmbeddingAsyncDatabaseConnection
from app.internal.document_embedding_facade import DocumentEmbeddingApiFacade

object_factory: ObjectFactory = None


def init_dependency_container():
    # pylint: disable=global-statement
    global object_factory
    if object_factory is None:
        object_factory = ObjectFactory()

    object_factory.register_factory_method(constants.SETTINGS, "s", get_settings)

    return object_factory


def get_settings():
    return Settings()


def get_document_embedding_async_db_connection():
    settings = object_factory.get_object(constants.SETTINGS)
    return DocumentEmbeddingAsyncDatabaseConnection.get_instance(settings)


async def get_document_embedding_db_engine(
    connection: DocumentEmbeddingAsyncDatabaseConnection = Depends(
        get_document_embedding_async_db_connection
    ),
):
    return await connection.create_engine()


async def get_document_embedding_facade(
    engine: AsyncEngine = Depends(get_document_embedding_db_engine),
) -> DocumentEmbeddingApiFacade:
    return DocumentEmbeddingApiFacade(engine)
