import json
from sqlalchemy import Row

from app import constants
from app.db.models.document_embedding import DocumentEmbedding
from app.models.document_embedding import (
    CreateDocumentEmbeddingRequestDto,
    SearchVectorSimilarityResponseDto,
)


def to_dict_of_document_embedding_list(
    document_embeddings: list[CreateDocumentEmbeddingRequestDto],
):
    docs = []
    for doc in document_embeddings:
        document = DocumentEmbedding(
            metadata_=doc.metadata, content=doc.content, embedding=doc.embedding
        )
        docs.append(document.as_dict())
    return docs


def to_search_vector_similarity_response_dto(row: Row):
    (
        id_,
        content,
        metadata,
        similarity,
    ) = row.t[constants.ARRAY_FIRST_INDEX]
    metadata_json = None
    if metadata:
        metadata_json = json.loads(metadata)
    return SearchVectorSimilarityResponseDto.model_construct(
        id_=int(id_),
        content=content,
        metadata=metadata_json,
        similarity=float(similarity),
    )
