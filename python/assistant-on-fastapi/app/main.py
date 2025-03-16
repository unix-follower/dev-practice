from contextlib import asynccontextmanager

from fastapi import FastAPI, Request, Depends
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse
from fastapi_healthchecks.api.router import HealthcheckRouter, Probe

from app import constants
from app.config.config import Settings
from app.config.dependency_injection import (
    get_document_embedding_async_db_connection,
    init_dependency_container,
)
from app.config.log_settings import configure_logging
from app.errors.app_exception import AppException
from app.errors.error_code import ErrorCode, to_http_status_code
from app.models.common import AppVersionModel
from app.routers.document_embedding import router as document_embedding_router

configure_logging()


def configure_healthcheck(fastapi_app: FastAPI):
    fastapi_app.include_router(
        HealthcheckRouter(
            Probe(
                name="readiness",
                checks=[],
            ),
            Probe(
                name="liveness",
                checks=[],
            ),
        ),
        prefix="/health",
    )


def configure_router(fastapi_app: FastAPI):
    configure_healthcheck(fastapi_app)
    fastapi_app.include_router(document_embedding_router)


@asynccontextmanager
async def configure_app(fastapi_app: FastAPI):
    configure_router(fastapi_app)

    connection = get_document_embedding_async_db_connection()
    await connection.create_engine()
    await connection.get_db_version()

    yield

    await connection.close()


origins = [
    "http://localhost",
    "http://localhost:5173",
]

settings: Settings = init_dependency_container().get_object(constants.SETTINGS)

app = FastAPI(
    title="assistant",
    version=settings.version,
    lifespan=configure_app,
    dependencies=[
        Depends(get_document_embedding_async_db_connection),
    ],
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["POST", "GET"],
    allow_headers=["*"],
)


@app.get("/version", response_model=AppVersionModel)
async def get_version():
    return AppVersionModel(version=settings.version)


@app.exception_handler(Exception)
async def handle_exception(_: Request, e: Exception):
    error_code = ErrorCode.UNKNOWN
    if isinstance(e, AppException):
        error_code = e.error_code

    return JSONResponse(
        content={"errorCode": error_code.value},
        status_code=to_http_status_code(error_code),
    )
