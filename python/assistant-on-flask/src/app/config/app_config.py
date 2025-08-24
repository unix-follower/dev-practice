import dataclasses
import os

import keras
from keras.models import load_model

from src.app.util import constants

_LSTM_MODEL_PATH = os.getenv("APP_LSTM_MODEL_PATH")
_GRU_MODEL_PATH = os.getenv("APP_GRU_MODEL_PATH")


@dataclasses.dataclass
class Config:
    HEALTHZ = {
        "live": "app.api.health.check.liveness",
        "ready": "app.api.health.check.readiness",
    }


_LSTM_MODEL = None
_GRU_MODEL = None


def _get_lstm_model():
    """
    Lazily init LSTM
    """
    global _LSTM_MODEL
    if _LSTM_MODEL is None:
        _LSTM_MODEL = load_model(_LSTM_MODEL_PATH)
    return _LSTM_MODEL


def _get_gru_model():
    """
    Lazily init GRU
    """
    global _GRU_MODEL
    if _GRU_MODEL is None:
        _GRU_MODEL = load_model(_GRU_MODEL_PATH)
    return _GRU_MODEL


def get_model(model_type: str = None) -> keras.Model:
    """
    Choose a GRU or LSTM model. Default is GRU.
    """
    if model_type is None:
        model_type = ""

    match model_type.upper():
        case constants.LSTM:
            model = _get_lstm_model()
        case constants.GRU:
            model = _get_gru_model()
        case _:
            model = _get_gru_model()

    return model
