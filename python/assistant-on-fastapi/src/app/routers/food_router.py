from typing import Annotated

from fastapi import APIRouter, Depends

from src.app.config.dependency_injection import get_food_predict_api_facade
from src.app.internal.food_predict_api_facade import FoodPredictApiFacade
from src.app.models.foodb import FoodPredictRequestDto, FoodPredictResponseDto

router = APIRouter()


@router.post("/api/v1/chemistry/ml/food/predict", response_model=FoodPredictResponseDto)
async def predict_food(
    food_predict_request_dto: FoodPredictRequestDto,
    food_predict_api_facade: Annotated[FoodPredictApiFacade, Depends(get_food_predict_api_facade)],
):
    return await food_predict_api_facade.predict(food_predict_request_dto)
