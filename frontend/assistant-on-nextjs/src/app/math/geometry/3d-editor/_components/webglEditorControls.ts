import * as webglUtils from "@/lib/utils/webglUtils"
import smallRectangleVertexShader, { rectangleVertexShader } from "./shaders/rectangleVert"
import smallRectangleFragmentShader, { rectangleFragmentShader } from "./shaders/rectangleFrag"
import triangleVertexShader from "./shaders/triangleVert"
import triangleFragmentShader from "./shaders/triangleFrag"
import letterFVertexShader, {
  letterFWithTransformedMatrixVertexShader,
  letterFWithTransformedMatrixVertex3dShader,
  withTextureVertex3dShader,
} from "./shaders/letterFVert"
import letterFFragmentShader, { letterFFragment3dShader, withTextureFragment3dShader } from "./shaders/letterFFrag"
import * as linalg from "@/lib/features/math/linalgCalculator"
import { toRadians } from "@/lib/features/math/conversionCalculator"
import { clearCanvas } from "./webglEditorCommons"

export type TextureOption = "REPEAT" | "CLAMP_TO_EDGE" | "MIRRORED_REPEAT"

export interface SceneSettings {
  gl: WebGL2RenderingContext
  fieldOfViewRadians?: number
  translation?: number[]
  translateX?: number
  translateY?: number
  rotationRadians?: number
  rotation?: number[]
  rotationX?: number
  rotationY?: number
  scale?: number[]
  scaleX?: number
  scaleY?: number
  transformedMatrix?: Float32Array | number[]
  threeD?: boolean
  cameraAngleRadians?: number
  animationEnabled?: boolean
  textureWrapS?: TextureOption
  textureWrapT?: TextureOption
}

function computeMatrix({ gl, translateX = 200, translateY = 150 }: SceneSettings) {
  let matrix = linalg.projection(gl.canvas.width, gl.canvas.height)
  matrix = linalg.translate(matrix, translateX, translateY)
  matrix = linalg.rotate(matrix, 0)
  return linalg.scale(matrix, 1, 1)
}

function drawTriangles({ gl, offset = 0, count = 6 }: { gl: WebGL2RenderingContext; offset?: number; count?: number }) {
  const primitiveType = gl.TRIANGLES
  gl.drawArrays(primitiveType, offset, count)
}

function setTriangleGeometry(gl: WebGL2RenderingContext) {
  gl.bufferData(gl.ARRAY_BUFFER, new Float32Array([0, -100, 150, 125, -175, 100]), gl.STATIC_DRAW)
}

function setRectangleGeometry(gl: WebGL2RenderingContext) {
  gl.bufferData(
    gl.ARRAY_BUFFER,
    new Float32Array([-150, -100, 150, -100, -150, 100, 150, -100, -150, 100, 150, 100]),
    gl.STATIC_DRAW,
  )
}

function setRandomRectangleColors(gl: WebGL2RenderingContext) {
  // Pick 2 random colors.
  const r1 = Math.random()
  const b1 = Math.random()
  const g1 = Math.random()
  const r2 = Math.random()
  const b2 = Math.random()
  const g2 = Math.random()

  gl.bufferData(
    gl.ARRAY_BUFFER,
    new Float32Array([r1, b1, g1, 1, r1, b1, g1, 1, r1, b1, g1, 1, r2, b2, g2, 1, r2, b2, g2, 1, r2, b2, g2, 1]),
    gl.STATIC_DRAW,
  )
}

function setLetterFGeometry(gl: WebGL2RenderingContext) {
  gl.bufferData(
    gl.ARRAY_BUFFER,
    // prettier-ignore
    new Float32Array([
      // left column
      0, 0,
      30, 0,
      0, 150,
      0, 150,
      30, 0,
      30, 150,

      // top rung
      30, 0,
      100, 0,
      30, 30,
      30, 30,
      100, 0,
      100, 30,

      // middle rung
      30, 60,
      67, 60,
      30, 90,
      30, 90,
      67, 60,
      67, 90]),
    gl.STATIC_DRAW,
  )
}

function setLetterFGeometry3d(gl: WebGL2RenderingContext, arrayPostProcessor?: (positions: Float32Array) => void) {
  // prettier-ignore
  const points = new Float32Array([
    // left column front
    0,   0,  0,
    0, 150,  0,
    30,   0,  0,
    0, 150,  0,
    30, 150,  0,
    30,   0,  0,

    // top rung front
    30,   0,  0,
    30,  30,  0,
    100,   0,  0,
    30,  30,  0,
    100,  30,  0,
    100,   0,  0,

    // middle rung front
    30,  60,  0,
    30,  90,  0,
    67,  60,  0,
    30,  90,  0,
    67,  90,  0,
    67,  60,  0,

    // left column back
    0,   0,  30,
    30,   0,  30,
    0, 150,  30,
    0, 150,  30,
    30,   0,  30,
    30, 150,  30,

    // top rung back
    30,   0,  30,
    100,   0,  30,
    30,  30,  30,
    30,  30,  30,
    100,   0,  30,
    100,  30,  30,

    // middle rung back
    30,  60,  30,
    67,  60,  30,
    30,  90,  30,
    30,  90,  30,
    67,  60,  30,
    67,  90,  30,

    // top
    0,   0,   0,
    100,   0,   0,
    100,   0,  30,
    0,   0,   0,
    100,   0,  30,
    0,   0,  30,

    // top rung right
    100,   0,   0,
    100,  30,   0,
    100,  30,  30,
    100,   0,   0,
    100,  30,  30,
    100,   0,  30,

    // under top rung
    30,   30,   0,
    30,   30,  30,
    100,  30,  30,
    30,   30,   0,
    100,  30,  30,
    100,  30,   0,

    // between top rung and middle
    30,   30,   0,
    30,   60,  30,
    30,   30,  30,
    30,   30,   0,
    30,   60,   0,
    30,   60,  30,

    // top of middle rung
    30,   60,   0,
    67,   60,  30,
    30,   60,  30,
    30,   60,   0,
    67,   60,   0,
    67,   60,  30,

    // right of middle rung
    67,   60,   0,
    67,   90,  30,
    67,   60,  30,
    67,   60,   0,
    67,   90,   0,
    67,   90,  30,

    // bottom of middle rung.
    30,   90,   0,
    30,   90,  30,
    67,   90,  30,
    30,   90,   0,
    67,   90,  30,
    67,   90,   0,

    // right of bottom
    30,   90,   0,
    30,  150,  30,
    30,   90,  30,
    30,   90,   0,
    30,  150,   0,
    30,  150,  30,

    // bottom
    0,   150,   0,
    0,   150,  30,
    30,  150,  30,
    0,   150,   0,
    30,  150,  30,
    30,  150,   0,

    // left side
    0,   0,   0,
    0,   0,  30,
    0, 150,  30,
    0,   0,   0,
    0, 150,  30,
    0, 150,   0,
  ])
  if (arrayPostProcessor) {
    arrayPostProcessor(points)
  }
  gl.bufferData(gl.ARRAY_BUFFER, points, gl.STATIC_DRAW)
}

export function drawIsoscelesTriangle({ gl, translateX = 200, translateY = 150 }: SceneSettings) {
  const program = webglUtils.createProgramFromSources(gl, [triangleVertexShader, triangleFragmentShader])!

  const positionLocation = gl.getAttribLocation(program, "a_position")
  const matrixLocation = gl.getUniformLocation(program, "u_matrix")

  const vao = gl.createVertexArray()
  gl.bindVertexArray(vao)

  const buffer = gl.createBuffer()
  gl.bindBuffer(gl.ARRAY_BUFFER, buffer)
  setTriangleGeometry(gl)

  gl.enableVertexAttribArray(positionLocation)
  const size = 2
  const type = gl.FLOAT
  const normalize = false
  const stride = 0
  const offset = 0
  gl.vertexAttribPointer(positionLocation, size, type, normalize, stride, offset)

  function drawScene(gl: WebGL2RenderingContext) {
    webglUtils.resizeCanvasToDisplaySize(gl.canvas as HTMLCanvasElement)

    gl.viewport(0, 0, gl.canvas.width, gl.canvas.height)
    clearCanvas(gl)

    gl.useProgram(program)
    gl.bindVertexArray(vao)

    const matrix = computeMatrix({ gl, translateX, translateY })
    gl.uniformMatrix3fv(matrixLocation, false, matrix)

    drawTriangles({ gl, count: 3 })
  }

  drawScene(gl)
}

export function drawSampleRectangle(gl: WebGL2RenderingContext) {
  const program = webglUtils.createProgramFromSources(gl, [smallRectangleVertexShader, smallRectangleFragmentShader])!

  const positionAttributeLocation = gl.getAttribLocation(program, "a_position")
  const resolutionUniformLocation = gl.getUniformLocation(program, "u_resolution")

  const positionBuffer = gl.createBuffer()

  gl.bindBuffer(gl.ARRAY_BUFFER, positionBuffer)

  const positions = [10, 20, 80, 20, 10, 30, 10, 30, 80, 20, 80, 30]
  gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(positions), gl.STATIC_DRAW)

  const vertexArrayObject = gl.createVertexArray()
  gl.bindVertexArray(vertexArrayObject)

  gl.enableVertexAttribArray(positionAttributeLocation)

  const size = 2 // 2 components per iteration
  const type = gl.FLOAT // the data is 32bit floats
  const normalize = false
  const stride = 0 // 0 = move forward size * sizeof(type) each iteration to get the next position
  const offset = 0 // start at the beginning of the buffer
  gl.vertexAttribPointer(positionAttributeLocation, size, type, normalize, stride, offset)

  webglUtils.resizeCanvasToDisplaySize(gl.canvas as HTMLCanvasElement)

  // Tell WebGL how to convert from clip space to pixels
  gl.viewport(0, 0, gl.canvas.width, gl.canvas.height)

  clearCanvas(gl)

  // Tell it to use our program (pair of shaders)
  gl.useProgram(program)

  gl.bindVertexArray(vertexArrayObject)

  gl.uniform2f(resolutionUniformLocation, gl.canvas.width, gl.canvas.height)

  drawTriangles({ gl })

  return { webglContext: gl, program }
}

export function drawRectangle({ gl, translateX, translateY }: SceneSettings) {
  const program = webglUtils.createProgramFromSources(gl, [rectangleVertexShader, rectangleFragmentShader])!

  const positionLocation = gl.getAttribLocation(program, "a_position")
  const colorLocation = gl.getAttribLocation(program, "a_color")

  // lookup uniforms
  const matrixLocation = gl.getUniformLocation(program, "u_matrix")!

  // Create set of attributes
  const vao = gl.createVertexArray()
  gl.bindVertexArray(vao)

  // Create a buffer for the positons.
  let buffer = gl.createBuffer()
  gl.bindBuffer(gl.ARRAY_BUFFER, buffer)

  setRectangleGeometry(gl)

  // tell the position attribute how to pull data out of the current ARRAY_BUFFER
  gl.enableVertexAttribArray(positionLocation)
  let size = 2
  const type = gl.FLOAT
  const normalize = false
  const stride = 0
  const offset = 0
  gl.vertexAttribPointer(positionLocation, size, type, normalize, stride, offset)

  // Create a buffer for the colors.
  buffer = gl.createBuffer()
  gl.bindBuffer(gl.ARRAY_BUFFER, buffer)
  setRandomRectangleColors(gl)

  // tell the color attribute how to pull data out of the current ARRAY_BUFFER
  gl.enableVertexAttribArray(colorLocation)
  size = 4
  gl.vertexAttribPointer(colorLocation, size, type, normalize, stride, offset)

  webglUtils.resizeCanvasToDisplaySize(gl.canvas as HTMLCanvasElement)

  gl.viewport(0, 0, gl.canvas.width, gl.canvas.height)

  clearCanvas(gl)

  // Tell it to use our program (pair of shaders)
  gl.useProgram(program)

  // Bind the attribute/buffer set we want.
  gl.bindVertexArray(vao)

  const matrix = computeMatrix({ gl, translateX, translateY })
  gl.uniformMatrix3fv(matrixLocation, false, matrix)
  drawTriangles({ gl })
}

export function drawLetterF({ gl, translation = [0, 0, 0], rotation = [0, 0, 0], scale = [1, 1, 0] }: SceneSettings) {
  const program = webglUtils.createProgramFromSources(gl, [letterFVertexShader, letterFFragmentShader])!

  const positionAttributeLocation = gl.getAttribLocation(program, "a_position")
  const colorLocation = gl.getUniformLocation(program, "u_color")

  const resolutionUniformLocation = gl.getUniformLocation(program, "u_resolution")
  const translationLocation = gl.getUniformLocation(program, "u_translation")
  const rotationLocation = gl.getUniformLocation(program, "u_rotation")
  const scaleLocation = gl.getUniformLocation(program, "u_scale")

  const positionBuffer = gl.createBuffer()

  const vao = gl.createVertexArray()
  gl.bindVertexArray(vao)

  gl.enableVertexAttribArray(positionAttributeLocation)
  gl.bindBuffer(gl.ARRAY_BUFFER, positionBuffer)

  setLetterFGeometry(gl)

  const size = 2 // 2 components per iteration
  const type = gl.FLOAT // the data is 32bit floats
  const normalize = false
  const stride = 0 // 0 = move forward size * sizeof(type) each iteration to get the next position
  const offset = 0
  gl.vertexAttribPointer(positionAttributeLocation, size, type, normalize, stride, offset)

  const color = [0, 255, 0, 1]

  webglUtils.resizeCanvasToDisplaySize(gl.canvas as HTMLCanvasElement)

  gl.viewport(0, 0, gl.canvas.width, gl.canvas.height)
  clearCanvas(gl)
  gl.useProgram(program)
  gl.bindVertexArray(vao)

  gl.uniform2f(resolutionUniformLocation, gl.canvas.width, gl.canvas.height)
  gl.uniform2fv(translationLocation, translation?.slice(0, 1))
  gl.uniform2fv(rotationLocation, rotation?.slice(0, 1))
  gl.uniform2fv(scaleLocation, scale?.slice(0, 1))
  gl.uniform4fv(colorLocation, color)

  const primitiveType = gl.TRIANGLES
  const count = 18
  gl.drawArrays(primitiveType, offset, count)
}

export function drawLetterFWithTransformedMatrix({ gl, transformedMatrix, threeD = false }: SceneSettings) {
  if (!transformedMatrix) {
    throw Error("The transformed matrix is not provided")
  }

  const offset = 0
  if (threeD) {
    const program = webglUtils.createProgramFromSources(gl, [
      letterFWithTransformedMatrixVertex3dShader,
      letterFFragment3dShader,
    ])!
    const positionAttributeLocation = gl.getAttribLocation(program, "a_position")
    const colorAttributeLocation = gl.getAttribLocation(program, "a_color")

    const matrixLocation = gl.getUniformLocation(program, "u_matrix")

    const positionBuffer = gl.createBuffer()

    const vao = gl.createVertexArray()
    gl.bindVertexArray(vao)

    gl.enableVertexAttribArray(positionAttributeLocation)
    gl.bindBuffer(gl.ARRAY_BUFFER, positionBuffer)

    setLetterFGeometry3d(gl)

    const size = 3
    let type: number = gl.FLOAT // the data is 32bit floats
    let normalize = false
    const stride = 0 // 0 = move forward size * sizeof(type) each iteration to get the next position
    gl.vertexAttribPointer(positionAttributeLocation, size, type, normalize, stride, offset)

    const colorBuffer = gl.createBuffer()
    gl.bindBuffer(gl.ARRAY_BUFFER, colorBuffer)
    setLetterFColors(gl)
    gl.enableVertexAttribArray(colorAttributeLocation)

    type = gl.UNSIGNED_BYTE // the data is 8bit unsigned bytes
    normalize = true // convert from 0-255 to 0.0-1.0
    gl.vertexAttribPointer(colorAttributeLocation, size, type, normalize, stride, offset)

    webglUtils.resizeCanvasToDisplaySize(gl.canvas as HTMLCanvasElement)

    gl.viewport(0, 0, gl.canvas.width, gl.canvas.height)
    clearCanvas(gl)

    gl.enable(gl.DEPTH_TEST)
    gl.enable(gl.CULL_FACE)
    gl.useProgram(program)
    gl.bindVertexArray(vao)
    gl.uniformMatrix4fv(matrixLocation, false, transformedMatrix)
    drawTriangles({ gl, offset, count: 16 * 6 })
  } else {
    const program = webglUtils.createProgramFromSources(gl, [
      letterFWithTransformedMatrixVertexShader,
      letterFFragmentShader,
    ])!

    const positionAttributeLocation = gl.getAttribLocation(program, "a_position")
    const colorLocation = gl.getUniformLocation(program, "u_color")

    const positionBuffer = gl.createBuffer()

    const vao = gl.createVertexArray()
    gl.bindVertexArray(vao)

    gl.enableVertexAttribArray(positionAttributeLocation)
    gl.bindBuffer(gl.ARRAY_BUFFER, positionBuffer)

    setLetterFGeometry(gl)

    const size = 2 // 2 components per iteration
    const type = gl.FLOAT // the data is 32bit floats
    const normalize = false
    const stride = 0 // 0 = move forward size * sizeof(type) each iteration to get the next position
    gl.vertexAttribPointer(positionAttributeLocation, size, type, normalize, stride, offset)

    webglUtils.resizeCanvasToDisplaySize(gl.canvas as HTMLCanvasElement)

    gl.viewport(0, 0, gl.canvas.width, gl.canvas.height)
    clearCanvas(gl)
    gl.useProgram(program)
    gl.bindVertexArray(vao)

    const color = [0, 255, 0, 1]

    const matrixLocation = gl.getUniformLocation(program, "u_matrix")
    gl.uniformMatrix3fv(matrixLocation, false, transformedMatrix)
    gl.uniform4fv(colorLocation, color)
    drawTriangles({ gl, offset, count: 18 })
  }
}
export function drawCircleFs({ gl, fieldOfViewRadians, cameraAngleRadians }: SceneSettings) {
  throwIfAnyInvalidNumber([fieldOfViewRadians, cameraAngleRadians])

  const program = webglUtils.createProgramFromSources(
    gl,
    [letterFWithTransformedMatrixVertex3dShader, letterFFragment3dShader],
    [],
    [],
    (err) => {
      throw err
    },
  )!

  const positionAttributeLocation = gl.getAttribLocation(program, "a_position")
  const colorAttributeLocation = gl.getAttribLocation(program, "a_color")

  const matrixLocation = gl.getUniformLocation(program, "u_matrix")

  const positionBuffer = gl.createBuffer()

  const vao = gl.createVertexArray()
  gl.bindVertexArray(vao)

  gl.enableVertexAttribArray(positionAttributeLocation)

  gl.bindBuffer(gl.ARRAY_BUFFER, positionBuffer)

  function arrayPostProcessor(points: Float32Array) {
    let matrix = linalg.xRotation(Math.PI)
    matrix = linalg.translate3d(matrix, -50, -75, -15)

    for (let i = 0; i < points.length; i += 3) {
      const vector = linalg.transformVector(matrix, [points[i], points[i + 1], points[i + 2], 1])
      points[i] = vector[0]
      points[i + 1] = vector[1]
      points[i + 2] = vector[2]
    }
  }
  setLetterFGeometry3d(gl, arrayPostProcessor)

  const size = 3
  let type: number = gl.FLOAT
  let normalize = false
  const stride = 0
  const offset = 0
  gl.vertexAttribPointer(positionAttributeLocation, size, type, normalize, stride, offset)

  const colorBuffer = gl.createBuffer()
  gl.bindBuffer(gl.ARRAY_BUFFER, colorBuffer)
  setLetterFColors(gl)

  gl.enableVertexAttribArray(colorAttributeLocation)

  type = gl.UNSIGNED_BYTE
  normalize = true
  gl.vertexAttribPointer(colorAttributeLocation, size, type, normalize, stride, offset)

  function drawScene() {
    const numFs = 5
    const radius = 200

    webglUtils.resizeCanvasToDisplaySize(gl.canvas as HTMLCanvasElement)

    gl.viewport(0, 0, gl.canvas.width, gl.canvas.height)

    clearCanvas(gl)

    gl.enable(gl.DEPTH_TEST)
    gl.enable(gl.CULL_FACE)

    gl.useProgram(program)

    gl.bindVertexArray(vao)

    const aspect = gl.canvas.width / gl.canvas.height
    const zNear = 1
    const zFar = 2000
    const projectionMatrix = linalg.perspective(fieldOfViewRadians!, aspect, zNear, zFar)

    let cameraMatrix: Float32Array | number[] = linalg.yRotation(cameraAngleRadians!)
    cameraMatrix = linalg.translate3d(cameraMatrix, 0, 0, radius * 1.5)

    const viewMatrix = linalg.inverse3d(cameraMatrix)

    // create a viewProjection matrix. This will both apply perspective
    // AND move the world so that the camera is effectively the origin
    const viewProjectionMatrix = linalg.multiply3d(projectionMatrix, viewMatrix)

    for (let i = 0; i < numFs; i++) {
      const angle = (i * Math.PI * 2) / numFs

      const x = Math.cos(angle) * radius
      const z = Math.sin(angle) * radius
      const matrix = linalg.translate3d(viewProjectionMatrix, x, 0, z)

      gl.uniformMatrix4fv(matrixLocation, false, matrix)

      const offset = 0
      const count = 16 * 6
      drawTriangles({ gl, offset, count })
    }
  }

  drawScene()
}

function throwIfAnyInvalidNumber(data: (number | undefined)[]) {
  for (const n of data) {
    if (n === undefined || isNaN(n)) {
      throw new Error("Invalid number")
    }
  }
}

export function drawCircleFsWithTrackingCamera({ gl, fieldOfViewRadians, cameraAngleRadians }: SceneSettings) {
  throwIfAnyInvalidNumber([fieldOfViewRadians, cameraAngleRadians])

  const program = webglUtils.createProgramFromSources(
    gl,
    [letterFWithTransformedMatrixVertex3dShader, letterFFragment3dShader],
    [],
    [],
    (err) => {
      throw err
    },
  )!

  const positionAttributeLocation = gl.getAttribLocation(program, "a_position")
  const colorAttributeLocation = gl.getAttribLocation(program, "a_color")
  const matrixLocation = gl.getUniformLocation(program, "u_matrix")

  const positionBuffer = gl.createBuffer()

  const vao = gl.createVertexArray()
  gl.bindVertexArray(vao)

  gl.enableVertexAttribArray(positionAttributeLocation)

  gl.bindBuffer(gl.ARRAY_BUFFER, positionBuffer)
  function arrayPostProcessor(points: Float32Array) {
    let matrix = linalg.xRotation(Math.PI)
    matrix = linalg.translate3d(matrix, -50, -75, -15)

    for (let i = 0; i < points.length; i += 3) {
      const vector = linalg.transformVector(matrix, [points[i], points[i + 1], points[i + 2], 1])
      points[i] = vector[0]
      points[i + 1] = vector[1]
      points[i + 2] = vector[2]
    }
  }
  setLetterFGeometry3d(gl, arrayPostProcessor)

  const size = 3
  let type: number = gl.FLOAT
  let normalize = false
  const stride = 0
  const offset = 0
  gl.vertexAttribPointer(positionAttributeLocation, size, type, normalize, stride, offset)

  const colorBuffer = gl.createBuffer()
  gl.bindBuffer(gl.ARRAY_BUFFER, colorBuffer)
  setLetterFColors(gl)

  gl.enableVertexAttribArray(colorAttributeLocation)

  type = gl.UNSIGNED_BYTE
  normalize = true
  gl.vertexAttribPointer(colorAttributeLocation, size, type, normalize, stride, offset)

  function drawScene() {
    const numFs = 5
    const radius = 200

    webglUtils.resizeCanvasToDisplaySize(gl.canvas as HTMLCanvasElement)

    gl.viewport(0, 0, gl.canvas.width, gl.canvas.height)

    clearCanvas(gl)

    gl.enable(gl.DEPTH_TEST)
    gl.enable(gl.CULL_FACE)

    gl.useProgram(program)
    gl.bindVertexArray(vao)

    const aspect = gl.canvas.width / gl.canvas.height
    const zNear = 1
    const zFar = 2000
    const projectionMatrix = linalg.perspective(fieldOfViewRadians!, aspect, zNear, zFar)

    // Compute the position of the first F
    const fPosition = [radius, 0, 0]

    // Use matrix math to compute a position on the circle.
    let cameraMatrix = linalg.yRotation(cameraAngleRadians!)
    cameraMatrix = linalg.translate3d(cameraMatrix, 0, 50, radius * 1.5)

    // Get the camera's position from the computed matrix
    const cameraPosition = [cameraMatrix[12], cameraMatrix[13], cameraMatrix[14]]

    const up = [0, 1, 0]

    cameraMatrix = linalg.lookAt(cameraPosition, fPosition, up)

    const viewMatrix = linalg.inverse3d(cameraMatrix)

    // create a viewProjection matrix. This will both apply perspective
    // AND move the world so that the camera is effectively the origin
    const viewProjectionMatrix = linalg.multiply3d(projectionMatrix, viewMatrix)

    // Draw 'F's in a circle
    for (let i = 0; i < numFs; i++) {
      const angle = (i * Math.PI * 2) / numFs

      const x = Math.cos(angle) * radius
      const z = Math.sin(angle) * radius
      const matrix = linalg.translate3d(viewProjectionMatrix, x, 0, z)

      gl.uniformMatrix4fv(matrixLocation, false, matrix)

      const count = 16 * 6
      drawTriangles({ gl, offset, count })
    }
  }

  drawScene()
}

function setLetterFColors(gl: WebGLRenderingContext) {
  gl.bufferData(
    gl.ARRAY_BUFFER,
    // prettier-ignore
    new Uint8Array([
      // left column front
      200,  70, 120,
      200,  70, 120,
      200,  70, 120,
      200,  70, 120,
      200,  70, 120,
      200,  70, 120,

      // top rung front
      200,  70, 120,
      200,  70, 120,
      200,  70, 120,
      200,  70, 120,
      200,  70, 120,
      200,  70, 120,

      // middle rung front
      200,  70, 120,
      200,  70, 120,
      200,  70, 120,
      200,  70, 120,
      200,  70, 120,
      200,  70, 120,

      // left column back
      80, 70, 200,
      80, 70, 200,
      80, 70, 200,
      80, 70, 200,
      80, 70, 200,
      80, 70, 200,

      // top rung back
      80, 70, 200,
      80, 70, 200,
      80, 70, 200,
      80, 70, 200,
      80, 70, 200,
      80, 70, 200,

      // middle rung back
      80, 70, 200,
      80, 70, 200,
      80, 70, 200,
      80, 70, 200,
      80, 70, 200,
      80, 70, 200,

      // top
      70, 200, 210,
      70, 200, 210,
      70, 200, 210,
      70, 200, 210,
      70, 200, 210,
      70, 200, 210,

      // top rung right
      200, 200, 70,
      200, 200, 70,
      200, 200, 70,
      200, 200, 70,
      200, 200, 70,
      200, 200, 70,

      // under top rung
      210, 100, 70,
      210, 100, 70,
      210, 100, 70,
      210, 100, 70,
      210, 100, 70,
      210, 100, 70,

      // between top rung and middle
      210, 160, 70,
      210, 160, 70,
      210, 160, 70,
      210, 160, 70,
      210, 160, 70,
      210, 160, 70,

      // top of middle rung
      70, 180, 210,
      70, 180, 210,
      70, 180, 210,
      70, 180, 210,
      70, 180, 210,
      70, 180, 210,

      // right of middle rung
      100, 70, 210,
      100, 70, 210,
      100, 70, 210,
      100, 70, 210,
      100, 70, 210,
      100, 70, 210,

      // bottom of middle rung.
      76, 210, 100,
      76, 210, 100,
      76, 210, 100,
      76, 210, 100,
      76, 210, 100,
      76, 210, 100,

      // right of bottom
      140, 210, 80,
      140, 210, 80,
      140, 210, 80,
      140, 210, 80,
      140, 210, 80,
      140, 210, 80,

      // bottom
      90, 130, 110,
      90, 130, 110,
      90, 130, 110,
      90, 130, 110,
      90, 130, 110,
      90, 130, 110,

      // left side
      160, 160, 220,
      160, 160, 220,
      160, 160, 220,
      160, 160, 220,
      160, 160, 220,
      160, 160, 220,
    ]),
    gl.STATIC_DRAW,
  )
}

export function drawLetterFWithTextureOptions({ gl, textureWrapS, textureWrapT }: SceneSettings) {
  function main() {
    const program = webglUtils.createProgramFromSources(
      gl,
      [withTextureVertex3dShader, withTextureFragment3dShader],
      [],
      [],
      (err) => {
        throw err
      },
    )!

    const positionAttributeLocation = gl.getAttribLocation(program, "a_position")
    const texcoordAttributeLocation = gl.getAttribLocation(program, "a_texcoord")

    const matrixLocation = gl.getUniformLocation(program, "u_matrix")

    const positionBuffer = gl.createBuffer()

    const vao = gl.createVertexArray()

    gl.bindVertexArray(vao)

    gl.enableVertexAttribArray(positionAttributeLocation)

    gl.bindBuffer(gl.ARRAY_BUFFER, positionBuffer)
    setGeometry(gl)

    let size = 3
    const type = gl.FLOAT
    let normalize = false
    const stride = 0
    const offset = 0
    gl.vertexAttribPointer(positionAttributeLocation, size, type, normalize, stride, offset)

    const textureCoordsBuffer = gl.createBuffer()
    gl.bindBuffer(gl.ARRAY_BUFFER, textureCoordsBuffer)
    setTextureCoords(gl)

    gl.enableVertexAttribArray(texcoordAttributeLocation)

    size = 2
    normalize = true // convert from 0-255 to 0.0-1.0
    gl.vertexAttribPointer(texcoordAttributeLocation, size, type, normalize, stride, offset)

    const texture = gl.createTexture()

    gl.activeTexture(gl.TEXTURE0)

    gl.bindTexture(gl.TEXTURE_2D, texture)

    // Fill the texture with a 1x1 blue pixel.
    gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, 1, 1, 0, gl.RGBA, gl.UNSIGNED_BYTE, new Uint8Array([0, 0, 255, 255]))

    const image = new Image()
    image.src = "/images/f-texture.png"
    image.addEventListener("load", function () {
      gl.bindTexture(gl.TEXTURE_2D, texture)
      gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, image)
      gl.generateMipmap(gl.TEXTURE_2D)
      drawScene()
    })

    const wrapS = textureWrapS != undefined ? gl[textureWrapS] : gl.REPEAT
    const wrapT = textureWrapT != undefined ? gl[textureWrapT] : gl.REPEAT

    drawScene()

    function drawScene() {
      webglUtils.resizeCanvasToDisplaySize(gl.canvas as HTMLCanvasElement)

      gl.viewport(0, 0, gl.canvas.width, gl.canvas.height)

      clearCanvas(gl)

      gl.enable(gl.DEPTH_TEST)

      gl.useProgram(program)

      gl.bindVertexArray(vao)

      const scaleFactor = 2.5
      const size = 80 * scaleFactor
      const x = gl.canvas.width / 2 - size / 2
      const y = gl.canvas.height - size - 60

      let matrix = linalg.orthographic(0, gl.canvas.width, gl.canvas.height, 0, -1, 1)
      matrix = linalg.translate3d(matrix, x, y, 0)
      matrix = linalg.scale3d(matrix, size, size, 1)
      matrix = linalg.translate3d(matrix, 0.5, 0.5, 0)

      gl.bindTexture(gl.TEXTURE_2D, texture)
      gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.NEAREST)
      gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, wrapS)
      gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, wrapT)

      gl.uniformMatrix4fv(matrixLocation, false, matrix)

      drawTriangles({ gl, offset: 0, count: 6 })
    }
  }

  function setGeometry(gl: WebGL2RenderingContext) {
    // prettier-ignore
    const positions = new Float32Array([
      -0.5,  0.5, 0.5,
       0.5,  0.5, 0.5,
      -0.5, -0.5, 0.5,
      -0.5, -0.5, 0.5,
       0.5,  0.5, 0.5,
       0.5, -0.5, 0.5,
    ])

    gl.bufferData(gl.ARRAY_BUFFER, positions, gl.STATIC_DRAW)
  }

  function setTextureCoords(gl: WebGL2RenderingContext) {
    gl.bufferData(
      gl.ARRAY_BUFFER,
      // prettier-ignore
      new Float32Array([
        -3, -1,
         2, -1,
        -3,  4,
        -3,  4,
         2, -1,
         2,  4,
      ]),
      gl.STATIC_DRAW,
    )
  }

  main()
}

export function drawLetterFWithTextureFiltering({ gl }: SceneSettings) {
  function main() {
    const program = webglUtils.createProgramFromSources(
      gl,
      [withTextureVertex3dShader, withTextureFragment3dShader],
      [],
      [],
      (err) => {
        throw err
      },
    )!

    const positionAttributeLocation = gl.getAttribLocation(program, "a_position")
    const textureCoordsAttributeLocation = gl.getAttribLocation(program, "a_texcoord")
    const matrixLocation = gl.getUniformLocation(program, "u_matrix")
    const positionBuffer = gl.createBuffer()

    const vao = gl.createVertexArray()
    gl.bindVertexArray(vao)

    gl.enableVertexAttribArray(positionAttributeLocation)
    gl.bindBuffer(gl.ARRAY_BUFFER, positionBuffer)

    setGeometry(gl)

    let size = 3
    const type = gl.FLOAT
    let normalize = false
    const stride = 0
    const offset = 0
    gl.vertexAttribPointer(positionAttributeLocation, size, type, normalize, stride, offset)

    const textureCoordsBuffer = gl.createBuffer()
    gl.bindBuffer(gl.ARRAY_BUFFER, textureCoordsBuffer)
    setTextureCoords(gl)

    gl.enableVertexAttribArray(textureCoordsAttributeLocation)

    size = 2
    normalize = true
    gl.vertexAttribPointer(textureCoordsAttributeLocation, size, type, normalize, stride, offset)

    let allocateFBTexture = true
    let framebufferWidth = 0 // set at render time
    let framebufferHeight = 0 // set at render time
    const framebuffer = gl.createFramebuffer()
    const fbTexture = gl.createTexture()
    gl.bindTexture(gl.TEXTURE_2D, fbTexture)
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE)
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.CLAMP_TO_EDGE)
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.NEAREST)
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.NEAREST)
    gl.bindFramebuffer(gl.FRAMEBUFFER, framebuffer)
    gl.framebufferTexture2D(gl.FRAMEBUFFER, gl.COLOR_ATTACHMENT0, gl.TEXTURE_2D, fbTexture, 0)

    const texture = gl.createTexture()

    gl.activeTexture(gl.TEXTURE0)

    gl.bindTexture(gl.TEXTURE_2D, texture)

    // Fill the texture with a 1x1 blue pixel.
    gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, 1, 1, 0, gl.RGBA, gl.UNSIGNED_BYTE, new Uint8Array([0, 0, 255, 255]))

    const image = new Image()
    image.src = "/images/mip-low-res-example.png"
    image.addEventListener("load", function () {
      gl.bindTexture(gl.TEXTURE_2D, texture)
      gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, image)
      gl.generateMipmap(gl.TEXTURE_2D)
    })

    const fieldOfViewRadians = toRadians(60)

    requestAnimationFrame(drawScene)

    function drawScene(time: number) {
      time *= 0.001 // convert to seconds

      if (
        webglUtils.resizeCanvasToDisplaySize(gl.canvas as HTMLCanvasElement, window.devicePixelRatio) ||
        allocateFBTexture
      ) {
        allocateFBTexture = false
        framebufferWidth = gl.canvas.width / 4
        framebufferHeight = gl.canvas.height / 4
        gl.bindTexture(gl.TEXTURE_2D, fbTexture)
        gl.texImage2D(
          gl.TEXTURE_2D,
          0,
          gl.RGBA,
          framebufferWidth,
          framebufferHeight,
          0,
          gl.RGBA,
          gl.UNSIGNED_BYTE,
          null,
        )
      }

      gl.bindFramebuffer(gl.FRAMEBUFFER, framebuffer)
      gl.viewport(0, 0, framebufferWidth, framebufferHeight)

      // Clear the framebuffer texture.
      gl.clearColor(0, 0, 0, 1)
      gl.clear(gl.COLOR_BUFFER_BIT)

      gl.enable(gl.DEPTH_TEST)
      gl.enable(gl.CULL_FACE)
      gl.useProgram(program)
      gl.bindVertexArray(vao)

      const aspect = gl.canvas.width / gl.canvas.height
      const zNear = 1
      const zFar = 2000
      const projectionMatrix = linalg.perspective(fieldOfViewRadians, aspect, zNear, zFar)

      const cameraPosition = [0, 0, 3]
      const up = [0, 1, 0]
      const target = [0, 0, 0]

      const cameraMatrix = linalg.lookAt(cameraPosition, target, up)
      const viewMatrix = linalg.inverse3d(cameraMatrix)
      const viewProjectionMatrix = linalg.multiply3d(projectionMatrix, viewMatrix)

      const settings = [
        { x: -1, y: -3, z: -30, filter: gl.NEAREST },
        { x: 0, y: -3, z: -30, filter: gl.LINEAR },
        { x: 1, y: -3, z: -30, filter: gl.NEAREST_MIPMAP_LINEAR },
        { x: -1, y: -1, z: -10, filter: gl.NEAREST },
        { x: 0, y: -1, z: -10, filter: gl.LINEAR },
        { x: 1, y: -1, z: -10, filter: gl.NEAREST_MIPMAP_LINEAR },
        { x: -1, y: 1, z: 0, filter: gl.NEAREST },
        { x: 0, y: 1, z: 0, filter: gl.LINEAR },
        { x: 1, y: 1, z: 0, filter: gl.LINEAR_MIPMAP_NEAREST },
      ]
      const xSpacing = 1.2
      const ySpacing = 0.7
      settings.forEach(function (s) {
        const z = -5 + s.z
        const r = Math.abs(z) * Math.sin(fieldOfViewRadians * 0.5)
        const x = Math.sin(time * 0.2) * r
        const y = Math.cos(time * 0.2) * r * 0.5
        const r2 = 1 + r * 0.2

        gl.bindTexture(gl.TEXTURE_2D, texture)
        gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE)
        gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.CLAMP_TO_EDGE)
        gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, s.filter)
        gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.LINEAR)

        const matrix = linalg.translate3d(viewProjectionMatrix, x + s.x * xSpacing * r2, y + s.y * ySpacing * r2, z)

        gl.uniformMatrix4fv(matrixLocation, false, matrix)

        drawTriangles({ gl, offset: 0, count: 6 })
      })

      gl.bindFramebuffer(gl.FRAMEBUFFER, null)
      gl.viewport(0, 0, gl.canvas.width, gl.canvas.height)

      clearCanvas(gl)

      gl.bindTexture(gl.TEXTURE_2D, fbTexture)
      // prettier-ignore
      gl.uniformMatrix4fv(matrixLocation, false, [
        2, 0, 0, 0,
        0, 2, 0, 0,
        0, 0, 1, 0,
        0, 0, 0, 1,
      ])

      gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT)
      gl.drawArrays(gl.TRIANGLES, 0, 6)

      requestAnimationFrame(drawScene)
    }
  }

  function setGeometry(gl: WebGL2RenderingContext) {
    // prettier-ignore
    const positions = new Float32Array([
      -0.5, -0.5, 0.5,
       0.5, -0.5, 0.5,
      -0.5,  0.5, 0.5,
      -0.5,  0.5, 0.5,
       0.5, -0.5, 0.5,
       0.5,  0.5, 0.5,
    ])

    gl.bufferData(gl.ARRAY_BUFFER, positions, gl.STATIC_DRAW)
  }

  function setTextureCoords(gl: WebGL2RenderingContext) {
    gl.bufferData(
      gl.ARRAY_BUFFER,
      // prettier-ignore
      new Float32Array([
        0, 0,
        1, 0,
        0, 1,
        0, 1,
        1, 0,
        1, 1,
      ]),
      gl.STATIC_DRAW,
    )
  }

  main()
}

export function drawTextureWithDistancePolygons({ gl }: SceneSettings) {
  const zDepth = 50

  function main() {
    const program = webglUtils.createProgramFromSources(
      gl,
      [withTextureVertex3dShader, withTextureFragment3dShader],
      [],
      [],
      (err) => {
        throw err
      },
    )!

    const positionAttributeLocation = gl.getAttribLocation(program, "a_position")
    const texcoordAttributeLocation = gl.getAttribLocation(program, "a_texcoord")
    const matrixLocation = gl.getUniformLocation(program, "u_matrix")
    const positionBuffer = gl.createBuffer()

    const vao = gl.createVertexArray()
    gl.bindVertexArray(vao)

    gl.enableVertexAttribArray(positionAttributeLocation)

    gl.bindBuffer(gl.ARRAY_BUFFER, positionBuffer)
    setGeometry(gl)

    let size = 3
    const type = gl.FLOAT
    let normalize = false
    const stride = 0
    const offset = 0
    gl.vertexAttribPointer(positionAttributeLocation, size, type, normalize, stride, offset)

    const textureCoordsBuffer = gl.createBuffer()
    gl.bindBuffer(gl.ARRAY_BUFFER, textureCoordsBuffer)
    setTextureCoords(gl)

    gl.enableVertexAttribArray(texcoordAttributeLocation)

    size = 2
    normalize = true // convert from 0-255 to 0.0-1.0
    gl.vertexAttribPointer(texcoordAttributeLocation, size, type, normalize, stride, offset)

    const mipTexture = gl.createTexture()
    gl.bindTexture(gl.TEXTURE_2D, mipTexture)

    const texture = gl.createTexture()

    gl.activeTexture(gl.TEXTURE0)

    gl.bindTexture(gl.TEXTURE_2D, texture)

    gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, 1, 1, 0, gl.RGBA, gl.UNSIGNED_BYTE, new Uint8Array([0, 0, 255, 255]))

    const image = new Image()
    image.src = "/images/mip-low-res-example.png"
    image.addEventListener("load", function () {
      gl.bindTexture(gl.TEXTURE_2D, texture)
      gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, image)
      gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE)
      gl.generateMipmap(gl.TEXTURE_2D)
      drawScene()
    })

    const textures = [texture, mipTexture]
    const textureIndex = 0

    const fieldOfViewRadians = toRadians(60)

    drawScene()

    function drawScene() {
      webglUtils.resizeCanvasToDisplaySize(gl.canvas as HTMLCanvasElement, window.devicePixelRatio)

      gl.viewport(0, 0, gl.canvas.width, gl.canvas.height)

      clearCanvas(gl)

      gl.enable(gl.DEPTH_TEST)
      gl.enable(gl.CULL_FACE)
      gl.useProgram(program)
      gl.bindVertexArray(vao)

      const aspect = gl.canvas.width / gl.canvas.height
      const zNear = 1
      const zFar = 2000
      const projectionMatrix = linalg.perspective(fieldOfViewRadians, aspect, zNear, zFar)

      const cameraPosition = [0, 0, 2]
      const up = [0, 1, 0]
      const target = [0, 0, 0]

      const cameraMatrix = linalg.lookAt(cameraPosition, target, up)
      const viewMatrix = linalg.inverse3d(cameraMatrix)
      const viewProjectionMatrix = linalg.multiply3d(projectionMatrix, viewMatrix)

      const settings = [
        { x: -1, y: 1, zRot: 0, magFilter: gl.NEAREST, minFilter: gl.NEAREST },
        { x: 0, y: 1, zRot: 0, magFilter: gl.LINEAR, minFilter: gl.LINEAR },
        { x: 1, y: 1, zRot: 0, magFilter: gl.LINEAR, minFilter: gl.NEAREST_MIPMAP_NEAREST },
        { x: -1, y: -1, zRot: 1, magFilter: gl.LINEAR, minFilter: gl.LINEAR_MIPMAP_NEAREST },
        { x: 0, y: -1, zRot: 1, magFilter: gl.LINEAR, minFilter: gl.NEAREST_MIPMAP_LINEAR },
        { x: 1, y: -1, zRot: 1, magFilter: gl.LINEAR, minFilter: gl.LINEAR_MIPMAP_LINEAR },
      ]
      const xSpacing = 1.2
      const ySpacing = 0.7
      settings.forEach(function (s) {
        gl.bindTexture(gl.TEXTURE_2D, textures[textureIndex])
        gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, s.minFilter)
        gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, s.magFilter)

        let matrix = linalg.translate3d(viewProjectionMatrix, s.x * xSpacing, s.y * ySpacing, -zDepth * 0.5)
        matrix = linalg.zRotate(matrix, s.zRot * Math.PI)
        matrix = linalg.scale3d(matrix, 1, 1, zDepth)

        gl.uniformMatrix4fv(matrixLocation, false, matrix)
        gl.drawArrays(gl.TRIANGLES, 0, 6)
      })
    }
  }

  function setGeometry(gl: WebGLRenderingContext) {
    // prettier-ignore
    const positions = new Float32Array([
      -0.5, 0.5, -0.5,
       0.5, 0.5, -0.5,
      -0.5, 0.5,  0.5,
      -0.5, 0.5,  0.5,
       0.5, 0.5, -0.5,
       0.5, 0.5,  0.5,
    ])

    gl.bufferData(gl.ARRAY_BUFFER, positions, gl.STATIC_DRAW)
  }

  function setTextureCoords(gl: WebGLRenderingContext) {
    gl.bufferData(
      gl.ARRAY_BUFFER,
      // prettier-ignore
      new Float32Array([
        0, 0,
        1, 0,
        0, zDepth,
        0, zDepth,
        1, 0,
        1, zDepth,
      ]),
      gl.STATIC_DRAW,
    )
  }

  main()
}

export function drawCubeWithTexture({ gl }: SceneSettings) {
  function main() {
    const program = webglUtils.createProgramFromSources(
      gl,
      [withTextureVertex3dShader, withTextureFragment3dShader],
      [],
      [],
      (err) => {
        throw err
      },
    )!

    const positionAttributeLocation = gl.getAttribLocation(program, "a_position")
    const textureCoordsAttributeLocation = gl.getAttribLocation(program, "a_texcoord")

    const matrixLocation = gl.getUniformLocation(program, "u_matrix")

    const vao = gl.createVertexArray()
    gl.bindVertexArray(vao)

    const positionBuffer = gl.createBuffer()
    gl.bindBuffer(gl.ARRAY_BUFFER, positionBuffer)
    setGeometry(gl)

    gl.enableVertexAttribArray(positionAttributeLocation)

    let size = 3
    const type = gl.FLOAT
    let normalize = false
    const stride = 0
    const offset = 0
    gl.vertexAttribPointer(positionAttributeLocation, size, type, normalize, stride, offset)

    const textureCoordsBuffer = gl.createBuffer()
    gl.bindBuffer(gl.ARRAY_BUFFER, textureCoordsBuffer)
    setTextureCoords(gl)

    gl.enableVertexAttribArray(textureCoordsAttributeLocation)

    size = 2
    normalize = true // convert from 0-255 to 0.0-1.0
    gl.vertexAttribPointer(textureCoordsAttributeLocation, size, type, normalize, stride, offset)

    const texture = gl.createTexture()
    gl.activeTexture(gl.TEXTURE0)
    gl.bindTexture(gl.TEXTURE_2D, texture)
    gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, 1, 1, 0, gl.RGBA, gl.UNSIGNED_BYTE, new Uint8Array([0, 0, 255, 255]))
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.LINEAR_MIPMAP_LINEAR)

    const image = new Image()
    image.src = "/images/wall.jpg"
    image.addEventListener("load", function () {
      gl.bindTexture(gl.TEXTURE_2D, texture)
      gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, image)
      gl.generateMipmap(gl.TEXTURE_2D)
    })

    const fieldOfViewRadians = toRadians(60)
    let modelXRotationRadians = toRadians(0)
    let modelYRotationRadians = toRadians(0)

    let then = 0

    requestAnimationFrame(drawScene)

    function drawScene(time: number) {
      // convert to seconds
      time *= 0.001
      const deltaTime = time - then
      then = time

      webglUtils.resizeCanvasToDisplaySize(gl.canvas as HTMLCanvasElement)

      gl.viewport(0, 0, gl.canvas.width, gl.canvas.height)

      modelYRotationRadians += -0.7 * deltaTime
      modelXRotationRadians += -0.4 * deltaTime

      gl.enable(gl.DEPTH_TEST)
      gl.enable(gl.CULL_FACE)
      gl.useProgram(program)
      gl.bindVertexArray(vao)

      const aspect = gl.canvas.width / gl.canvas.height
      const zNear = 1
      const zFar = 2000
      const projectionMatrix = linalg.perspective(fieldOfViewRadians, aspect, zNear, zFar)

      const cameraPosition = [0, 0, 2]
      const up = [0, 1, 0]
      const target = [0, 0, 0]

      const cameraMatrix = linalg.lookAt(cameraPosition, target, up)
      const viewMatrix = linalg.inverse3d(cameraMatrix)
      const viewProjectionMatrix = linalg.multiply3d(projectionMatrix, viewMatrix)

      let matrix = linalg.xRotate(viewProjectionMatrix, modelXRotationRadians)
      matrix = linalg.yRotate(matrix, modelYRotationRadians)

      gl.uniformMatrix4fv(matrixLocation, false, matrix)
      drawTriangles({ gl, offset: 0, count: 6 * 6 })

      requestAnimationFrame(drawScene)
    }
  }

  function setGeometry(gl: WebGL2RenderingContext) {
    // prettier-ignore
    const positions = new Float32Array(
      [
        -0.5, -0.5, -0.5,
        -0.5,  0.5, -0.5,
         0.5, -0.5, -0.5,
        -0.5,  0.5, -0.5,
         0.5,  0.5, -0.5,
         0.5, -0.5, -0.5,

        -0.5, -0.5, 0.5,
         0.5, -0.5, 0.5,
        -0.5,  0.5, 0.5,
        -0.5,  0.5, 0.5,
         0.5, -0.5, 0.5,
         0.5,  0.5, 0.5,

        -0.5, 0.5, -0.5,
        -0.5, 0.5,  0.5,
         0.5, 0.5, -0.5,
        -0.5, 0.5,  0.5,
         0.5, 0.5,  0.5,
         0.5, 0.5, -0.5,

        -0.5, -0.5, -0.5,
         0.5, -0.5, -0.5,
        -0.5, -0.5,  0.5,
        -0.5, -0.5,  0.5,
         0.5, -0.5, -0.5,
         0.5, -0.5,  0.5,

        -0.5, -0.5, -0.5,
        -0.5, -0.5,  0.5,
        -0.5,  0.5, -0.5,
        -0.5, -0.5,  0.5,
        -0.5,  0.5,  0.5,
        -0.5,  0.5, -0.5,

        0.5, -0.5, -0.5,
        0.5,  0.5, -0.5,
        0.5, -0.5,  0.5,
        0.5, -0.5,  0.5,
        0.5,  0.5, -0.5,
        0.5,  0.5,  0.5,
      ])
    gl.bufferData(gl.ARRAY_BUFFER, positions, gl.STATIC_DRAW)
  }

  function setTextureCoords(gl: WebGL2RenderingContext) {
    gl.bufferData(
      gl.ARRAY_BUFFER,
      // prettier-ignore
      new Float32Array(
        [
          // select the top left image
          0   , 0  ,
          0   , 0.5,
          0.25, 0  ,
          0   , 0.5,
          0.25, 0.5,
          0.25, 0  ,
          // select the top middle image
          0.25, 0  ,
          0.5 , 0  ,
          0.25, 0.5,
          0.25, 0.5,
          0.5 , 0  ,
          0.5 , 0.5,
          // select to top right image
          0.5 , 0  ,
          0.5 , 0.5,
          0.75, 0  ,
          0.5 , 0.5,
          0.75, 0.5,
          0.75, 0  ,
          // select the bottom left image
          0   , 0.5,
          0.25, 0.5,
          0   , 1  ,
          0   , 1  ,
          0.25, 0.5,
          0.25, 1  ,
          // select the bottom middle image
          0.25, 0.5,
          0.25, 1  ,
          0.5 , 0.5,
          0.25, 1  ,
          0.5 , 1  ,
          0.5 , 0.5,
          // select the bottom right image
          0.5 , 0.5,
          0.75, 0.5,
          0.5 , 1  ,
          0.5 , 1  ,
          0.75, 0.5,
          0.75, 1  ,

        ]),
      gl.STATIC_DRAW,
    )
  }

  main()
}
