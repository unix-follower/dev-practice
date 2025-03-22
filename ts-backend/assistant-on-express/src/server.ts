import "module-alias/register"
import http from "http"
import app from "./app"

/**
 * Normalize a port into a number, string, or false.
 */
function normalizePort(val: number | string) {
  // @ts-expect-error it is safe to get a NaN value
  const port = parseInt(val, 10)

  if (isNaN(port)) {
    // named pipe
    return val
  }

  if (port >= 0) {
    // port number
    return port
  }

  return false
}

const port = normalizePort(process.env.PORT || "3000")
app.set("port", port)

const server = http.createServer(app)

server.listen(port)
server.on("error", onError)
server.on("listening", onListening)

function onError(error: Error) {
  // @ts-expect-error https://expressjs.com/en/guide/error-handling.html
  if (error.syscall !== "listen") {
    throw error
  }

  const bind = typeof port === "string" ? "Pipe " + port : "Port " + port

  // @ts-expect-error https://expressjs.com/en/guide/error-handling.html
  if (error.code === "EACCES") {
    console.error(`${bind} requires elevated privileges`)
    process.exit(1)
    // @ts-expect-error https://expressjs.com/en/guide/error-handling.html
  } else if (error.code === "EADDRINUSE") {
    console.error(`${bind} is already in use`)
    process.exit(1)
  } else {
    console.error(error)
    throw error
  }
}

function onListening() {
  const addr = server.address()!
  const bind = typeof addr === "string" ? "pipe " + addr : "port " + addr.port
  console.info(`Listening on ${bind}`)
}
