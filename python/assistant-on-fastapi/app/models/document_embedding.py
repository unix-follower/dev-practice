from abc import ABC
from pydantic import Field

from app.models.common import CamelCaseDtoModel


class AbstractEmbeddingVector(ABC, CamelCaseDtoModel):
    embedding: list[float]


class CreateDocumentEmbeddingRequestDto(AbstractEmbeddingVector):
    content: str
    metadata: dict


class SearchVectorSimilarityRequestDto(CamelCaseDtoModel):
    query_embedding: list[float]
    match_threshold: float
    limit: int


class SearchVectorSimilarityResponseDto(CamelCaseDtoModel):
    id_: int = Field(alias="id")
    content: str
    similarity: float
