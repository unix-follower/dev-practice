from abc import ABC, abstractmethod
from pickle import load

import numpy as np
from sklearn.pipeline import Pipeline

from app import APP_DIR
from app import constants
from app.errors.app_exception import AppException
from app.errors.error_code import ErrorCode
from app.models.foodb import FoodPredictRequestDto, FoodPredictResponseDto


class FoodPredictApiFacade(ABC):
    @abstractmethod
    async def predict(
        self, food_predict_request_dto: FoodPredictRequestDto
    ) -> FoodPredictResponseDto:
        pass


class FoodPredictFacade(FoodPredictApiFacade):
    def __init__(self, available_ml_models: set[str]):
        self._available_ml_models = available_ml_models

    async def predict(self, food_predict_request_dto: FoodPredictRequestDto):
        if food_predict_request_dto.model not in self._available_ml_models:
            raise AppException(
                "Unsupported model", error_code=ErrorCode.UNSUPPORTED_ML_MODEL
            )

        with open(f"{APP_DIR}/ml/models/foodb-scikit-learn-3feat.pkl", "rb") as file:
            pipeline: Pipeline = load(file)

        feature = food_predict_request_dto.feature
        input_data = np.array(
            [feature.name, feature.description, feature.category]
        ).reshape(1, 3)
        prediction = pipeline.predict(input_data)[constants.ARRAY_FIRST_INDEX]
        return FoodPredictResponseDto.model_construct(prediction=prediction)
