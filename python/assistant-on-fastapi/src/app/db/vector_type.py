from typing import Any, Self

from sqlalchemy.types import UserDefinedType


class VectorType(UserDefinedType):
    cache_ok = True

    def __init__(self, vector_size=-1):
        self._vector_size = vector_size

    def get_col_spec(self):
        return f"vector({self._vector_size})" if self._vector_size > 0 else "vector"

    def _with_collation(self, collation: str) -> Self:
        return self

    @property
    def python_type(self) -> type[Any]:
        return type(list)

    def bind_processor(self, dialect):
        def process(value):
            if value is not None:
                return list(value)
            return value

        return process

    def result_processor(self, dialect, coltype):
        def process(value):
            if value is not None:
                return list(value)
            return value

        return process
