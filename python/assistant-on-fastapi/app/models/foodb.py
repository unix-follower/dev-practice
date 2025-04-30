from app.models.common import CamelCaseDtoModel


class FoodFeatureDto(CamelCaseDtoModel):
    name: str
    description: str | None = None
    category: str | None = None


class FoodPredictRequestDto(CamelCaseDtoModel):
    model: str
    feature: FoodFeatureDto


class FoodPredictResponseDto(CamelCaseDtoModel):
    prediction: str
