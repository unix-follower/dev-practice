from typing import Annotated

from fastapi import Depends
from sqlalchemy.ext.asyncio import AsyncEngine

from src.app import constants
from src.app.config.config import Settings
from src.app.config.object_factory import ObjectFactory
from src.app.db.database import DocumentEmbeddingAsyncDatabaseConnection
from src.app.internal.document_embedding_facade import DocumentEmbeddingApiFacade
from src.app.internal.food_predict_api_facade import FoodPredictApiFacade, FoodPredictFacade

object_factory: ObjectFactory = None


def init_dependency_container():
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
    connection: Annotated[
        DocumentEmbeddingAsyncDatabaseConnection, Depends(get_document_embedding_async_db_connection)
    ],
):
    return await connection.create_engine()


async def get_document_embedding_facade(
    engine: Annotated[AsyncEngine, Depends(get_document_embedding_db_engine)],
) -> DocumentEmbeddingApiFacade:
    return DocumentEmbeddingApiFacade(engine)


def available_ml_models():
    return {"foodb-3feat"}


async def get_food_predict_api_facade() -> FoodPredictApiFacade:
    return FoodPredictFacade(available_ml_models())
