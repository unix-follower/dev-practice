from sqlalchemy import String, BigInteger, Sequence
from sqlalchemy.dialects.postgresql import JSONB
from sqlalchemy.orm import Mapped, mapped_column

from app.db.vector_type import VectorType
from app.db.models import AbstractDbModel

ID = "id"
CONTENT = "content"
METADATA = "metadata"
EMBEDDING = "embedding"


# pylint: disable=too-few-public-methods, unsubscriptable-object
class DocumentEmbedding(AbstractDbModel):
    __tablename__ = "document_embedding"

    document_id_seq = Sequence("document_embedding_id_seq", start=1)

    content: Mapped[str] = mapped_column(String())
    metadata_: Mapped[dict] = mapped_column(JSONB(), name=METADATA)
    embedding: Mapped[list[float]] = mapped_column(VectorType(1536))
    id_: Mapped[int] = mapped_column(
        BigInteger(),
        document_id_seq,
        name=ID,
        primary_key=True,
        nullable=False,
        autoincrement=True,
        default=None,
        server_default=document_id_seq.next_value(),
    )

    def as_dict(self):
        return {
            CONTENT: self.content,
            METADATA: self.metadata_,
            EMBEDDING: self.embedding,
        }
