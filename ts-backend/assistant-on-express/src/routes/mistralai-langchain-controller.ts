import express from "express"
import { Request, Response, NextFunction } from "express"
import { createLangChainChatMistralAI } from "@/integrations/mistralai/mistralai-integration"

import MistralAILangChainFacade from "@/internal/mistralai-langchain-facade"
import { HumanMessage, SystemMessage } from "@langchain/core/messages"
import { ChatPromptTemplate } from "@langchain/core/prompts"

const router = express.Router()

const messages = [new SystemMessage("Translate the following from English into Chinese"), new HumanMessage("hi!")]
const systemTemplate = "Translate the following from English into {language}"
const promptTemplate = ChatPromptTemplate.fromMessages([
  ["system", systemTemplate],
  ["user", "{text}"],
])

function createLangChainFacade() {
  const model = createLangChainChatMistralAI()
  return new MistralAILangChainFacade(model)
}

// eslint-disable-next-line @typescript-eslint/no-unused-vars
router.get("/api/v1/ml/mistralai/chat", async function (req: Request, res: Response, next: NextFunction) {
  const langChainFacade = createLangChainFacade()
  let messageChunk
  if (req.query.useTemplate) {
    console.debug("useTemplate")
    const chatPromptValue = await promptTemplate.invoke({
      language: "chinese",
      text: "hi!",
    })
    messageChunk = await langChainFacade.chatFromChatTemplate(chatPromptValue)
  } else {
    console.debug("use regular chat")
    messageChunk = await langChainFacade.chat(messages)
  }
  console.debug(messageChunk)
  res.send(messageChunk)
})

router.get("/api/v1/ml/mistralai/chat/streaming", async function (req: Request, res: Response, next: NextFunction) {
  const langChainFacade = createLangChainFacade()
  try {
    const abortController = new AbortController()
    const stream = await langChainFacade.streamChat(messages, abortController.signal)

    res.setHeader("Cache-Control", "no-cache")
    res.setHeader("Content-Type", "text/event-stream")
    res.setHeader("Access-Control-Allow-Origin", "*")
    res.setHeader("Connection", "keep-alive")
    res.setHeader("X-Content-Type-Options", "nosniff")
    res.flushHeaders()

    res.on("close", () => {
      console.debug("The client closed the connection")
      abortController.abort("close")
      res.end()
    })

    let counter = 0
    for await (const chunk of stream) {
      console.debug(`Sending chunk ${++counter}`)
      res.write(`data: ${JSON.stringify(chunk)}\n\n`)
    }
  } finally {
    res.end()
  }
})

export default router
