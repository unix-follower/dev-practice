from typing import Annotated

from fastapi import APIRouter, Depends

from src.app.config.dependency_injection import get_document_embedding_facade
from src.app.internal.document_embedding_facade import DocumentEmbeddingApiFacade
from src.app.models.document_embedding import (
    CreateDocumentEmbeddingRequestDto,
    SearchVectorSimilarityRequestDto,
    SearchVectorSimilarityResponseDto,
)

router = APIRouter()


@router.post("/api/v1/ml/embedding")
async def create_document_embedding(
    documents: list[CreateDocumentEmbeddingRequestDto],
    document_embedding_api_facade: Annotated[DocumentEmbeddingApiFacade, Depends(get_document_embedding_facade)],
):
    await document_embedding_api_facade.create_document_embedding(documents)


@router.post(
    "/api/v1/ml/embedding/similarity",
    response_model=list[SearchVectorSimilarityResponseDto],
)
async def search_vector_similarity(
    similarity_request: SearchVectorSimilarityRequestDto,
    document_embedding_api_facade: Annotated[DocumentEmbeddingApiFacade, Depends(get_document_embedding_facade)],
) -> list[SearchVectorSimilarityResponseDto]:
    return await document_embedding_api_facade.search_vector_similarity(similarity_request)
