import ErrorCode from "./error-code"
import AppError from "./AppError"

export default class MistralAIIntegrationError extends AppError {
  constructor(message: string, errorCode: ErrorCode = ErrorCode.MISTRAL_AI_INTEGRATION_FAILED) {
    super(message, errorCode)

    Object.setPrototypeOf(this, MistralAIIntegrationError.prototype)
  }
}
