"""
Loads RNN model from the file path and makes a prediction.
"""

import numpy as np

# pylint: disable=no-name-in-module,import-error
from tensorflow.keras.models import load_model
from app import app_config as config


def _get_model(model_type: str = None):
    """
    :param model_type: Example LSTM, GRU. Default: GRU.
    """
    if model_type is None:
        model_type = ""

    match model_type.upper():
        case "LSTM":
            model = load_model(config.LSTM_MODEL_PATH)
        case "GRU":
            model = load_model(config.GRU_MODEL_PATH)
        case _:
            model = load_model(config.GRU_MODEL_PATH)

    return model


def predict(prices: np.ndarray, model_type: str = None) -> np.ndarray:
    """
    :param prices: prices to evaluate
    :param model_type: Example LSTM, GRU. Default: GRU.
    :return: prediction
    """
    model = _get_model(model_type)
    return model.predict(prices)
