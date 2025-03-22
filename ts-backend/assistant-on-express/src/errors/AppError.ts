import ErrorCode from "./error-code"

export default class AppError extends Error {
  private _errorCode: ErrorCode

  constructor(message: string, errorCode: ErrorCode = ErrorCode.UNKNOWN) {
    super(message)
    this._errorCode = errorCode

    Object.setPrototypeOf(this, AppError.prototype)
  }

  public get errorCode(): ErrorCode {
    return this._errorCode
  }
}
