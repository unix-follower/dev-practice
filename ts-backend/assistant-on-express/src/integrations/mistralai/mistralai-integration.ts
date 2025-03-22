import { ChatMistralAI } from "@langchain/mistralai"
import MistralAIIntegrationError from "@/errors/MistralAIIntegrationError"
import ErrorCode from "@/errors/error-code"

function defaultMistralAIConfig(apiKey: string) {
  return {
    model: "codestral-latest",
    apiKey,
    temperature: 0.5,
    maxTokens: 200,
    maxRetries: 0,
  }
}

function getMistralAIApiKey() {
  const apiKey = process.env.MISTRAL_API_KEY
  if (!apiKey) {
    throw new MistralAIIntegrationError("The API key is not set", ErrorCode.MISTRAL_AI_INTEGRATION_MISCONFIGURED)
  }
  return apiKey
}

export function createLangChainChatMistralAI() {
  const config = defaultMistralAIConfig(getMistralAIApiKey())
  return new ChatMistralAI(config)
}
