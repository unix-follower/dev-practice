from app.errors.app_exception import AppException
from app.errors.error_code import ErrorCode


class DbException(AppException):
    def __init__(self, message, error_code=ErrorCode.DB_FAILED):
        super().__init__(message, error_code)
