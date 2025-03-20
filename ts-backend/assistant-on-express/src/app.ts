import config from "dotenv"
import express from "express"
import { Request, Response, NextFunction } from "express"
import createError from "http-errors"
import cookieParser from "cookie-parser"
import logger from "morgan"

const configFilePaths = [".env"]
if (process.env.NODE_ENV === "local") {
  configFilePaths.push(".env.local")
}
config.config({ path: configFilePaths })

import indexRouter from "./routes"

const app = express()

app.use(logger("dev"))
app.use(express.json())
app.use(express.urlencoded({ extended: false }))
app.use(cookieParser())

app.use("/", indexRouter)

// catch 404 and forward to error handler
app.use(function (req: Request, res: Response, next: NextFunction) {
  next(createError(404))
})

// error handler
app.use(function (err: Error, req: Request, res: Response, _: NextFunction) {
  // set locals, only providing error in development
  res.locals.message = err.message
  res.locals.error = req.app.get("env") === "development" ? err : {}

  // @ts-expect-error let descendants define status field
  res.status(err.status || 500)
  res.send("error")
})

export default app
