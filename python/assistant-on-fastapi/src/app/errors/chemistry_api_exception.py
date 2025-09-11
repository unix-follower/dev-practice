from src.app.errors.app_exception import AppException
from src.app.errors.error_code import ErrorCode


class ChemistryApiException(AppException):
    def __init__(self, message: str, error_code: ErrorCode):
        super().__init__(message, error_code)
