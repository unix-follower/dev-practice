from collections.abc import Sequence

from sqlalchemy import insert, select, func, cast

from app.db.models.document_embedding import DocumentEmbedding
from app.db.repository import AbstractRepository
from app.db.vector_type import VectorType
from app.models.document_embedding import SearchVectorSimilarityRequestDto


class DocumentEmbeddingRepository(AbstractRepository[int, DocumentEmbedding]):
    async def create_document_embedding(self, document_embeddings: Sequence[dict]):
        return await self._session.execute(
            insert(DocumentEmbedding), document_embeddings
        )

    async def search_vector_similarity(
        self, search_similarity_request: SearchVectorSimilarityRequestDto
    ):
        return await self._session.execute(
            select(
                func.match_documents(
                    cast(search_similarity_request.query_embedding, VectorType),
                    search_similarity_request.match_threshold,
                    search_similarity_request.limit,
                )
            )
        )
