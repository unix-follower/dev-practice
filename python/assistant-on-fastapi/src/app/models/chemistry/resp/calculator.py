from decimal import Decimal

from pydantic import field_serializer

from src.app.models.common import CamelCaseDtoModel


class CalculatorResp(CamelCaseDtoModel):
    result: float
    result_scientific_notation: float

    @field_serializer("result_scientific_notation")
    def serialize_result_scientific_notation(self, result_scientific_notation: float, _info):
        return f"{Decimal(result_scientific_notation):.16E}"
