import { StatusCodes } from "http-status-codes"

enum ErrorCode {
  UNKNOWN = -1,
  MISTRAL_AI_INTEGRATION_FAILED = 1,
  MISTRAL_AI_INTEGRATION_MISCONFIGURED = 2,
}

export default ErrorCode

export function toHttpStatusCode(errorCode: ErrorCode): number {
  const errorCodeStatusMap: Record<ErrorCode, number> = {
    [ErrorCode.UNKNOWN]: StatusCodes.INTERNAL_SERVER_ERROR,
    [ErrorCode.MISTRAL_AI_INTEGRATION_FAILED]: StatusCodes.INTERNAL_SERVER_ERROR,
    [ErrorCode.MISTRAL_AI_INTEGRATION_MISCONFIGURED]: StatusCodes.INTERNAL_SERVER_ERROR,
  }

  return errorCodeStatusMap[errorCode] ?? StatusCodes.INTERNAL_SERVER_ERROR
}
