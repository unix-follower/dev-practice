import config from "dotenv"
import express from "express"
import { Request, Response, NextFunction } from "express"
import createError from "http-errors"
import cookieParser from "cookie-parser"
import logger from "morgan"
import { StatusCodes } from "http-status-codes"
import helmet from "helmet"

import ErrorCode, { toHttpStatusCode } from "./errors/error-code"
import AppError from "./errors/AppError"
import mistralAILangChainRouter from "./routes/mistralai-langchain-controller"

const configFilePaths = [".env"]
if (process.env.NODE_ENV === "local") {
  configFilePaths.push(".env.local")
}
config.config({ path: configFilePaths })

const app = express()

app.use(logger("dev"))
app.use(express.json())
app.use(express.urlencoded({ extended: false }))
app.use(cookieParser())
app.use(helmet())

app.use(mistralAILangChainRouter)

// catch 404 and forward to error handler
app.use(function (req: Request, res: Response, next: NextFunction) {
  next(createError(StatusCodes.NOT_FOUND))
})

function errorHandler(err: Error, req: Request, res: Response, _: NextFunction) {
  // console.error(err)
  console.error(err)

  let errorCode = ErrorCode.UNKNOWN
  if (err instanceof AppError) {
    errorCode = err.errorCode
  }

  const status = toHttpStatusCode(errorCode)
  res.status(status)
  res.send({ errorCode: errorCode })
}
app.use(errorHandler)

export default app
