from operator import attrgetter

from sqlalchemy.ext.asyncio import AsyncEngine, AsyncSession, AsyncSessionTransaction

from app.db.document_embedding_repository import DocumentEmbeddingRepository
from app.internal.mapper import document_embedding_mapper
from app.models.document_embedding import (
    CreateDocumentEmbeddingRequestDto,
    SearchVectorSimilarityRequestDto,
)


class DocumentEmbeddingApiFacade:
    def __init__(self, engine: AsyncEngine):
        self._engine = engine

    async def create_document_embedding(
        self, document_embeddings: list[CreateDocumentEmbeddingRequestDto]
    ):
        docs = document_embedding_mapper.to_dict_of_document_embedding_list(
            document_embeddings
        )

        async with self._engine.connect() as connection:
            async with AsyncSession(connection).begin() as session_tx:
                session_tx: AsyncSessionTransaction
                repository = DocumentEmbeddingRepository(session_tx.session)
                await repository.create_document_embedding(docs)

    async def search_vector_similarity(
        self, search_similarity_request: SearchVectorSimilarityRequestDto
    ):
        async with self._engine.connect() as connection:
            async with AsyncSession(connection).begin() as session_tx:
                session_tx: AsyncSessionTransaction
                repository = DocumentEmbeddingRepository(session_tx.session)
                result_cursor = await repository.search_vector_similarity(
                    search_similarity_request
                )
                results = [
                    document_embedding_mapper.to_search_vector_similarity_response_dto(
                        row
                    )
                    for row in result_cursor.fetchall()
                ]
        results = sorted(results, key=attrgetter("similarity"), reverse=True)
        return results
