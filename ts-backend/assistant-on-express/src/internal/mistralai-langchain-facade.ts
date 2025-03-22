import { ChatMistralAI } from "@langchain/mistralai"

import { HumanMessage, SystemMessage } from "@langchain/core/messages"
import { ChatPromptValueInterface } from "@langchain/core/prompt_values"

export default class MistralAILangChainFacade {
  private _chatMistralAI: ChatMistralAI

  constructor(chatMistralAI: ChatMistralAI) {
    this._chatMistralAI = chatMistralAI

    Object.setPrototypeOf(this, MistralAILangChainFacade.prototype)
  }

  async chatFromChatTemplate(templateValue: ChatPromptValueInterface) {
    return this._chatMistralAI.invoke(templateValue)
  }

  async chat(messages: Array<SystemMessage | HumanMessage>) {
    return this._chatMistralAI.invoke(messages)
  }

  async streamChat(messages: Array<SystemMessage | HumanMessage>, abortSignal: AbortSignal) {
    return this._chatMistralAI.stream(messages, {
      signal: abortSignal,
    })
  }
}
