"use client"

import React, { useEffect, useRef, useState } from "react"
import OutlinedInput from "@mui/material/OutlinedInput"
import InputLabel from "@mui/material/InputLabel"
import MenuItem from "@mui/material/MenuItem"
import FormControl from "@mui/material/FormControl"
import ListItemText from "@mui/material/ListItemText"
import Select, { SelectChangeEvent } from "@mui/material/Select"
import Checkbox from "@mui/material/Checkbox"
import { useTranslation } from "react-i18next"
import i18n from "@/config/i18n"
import * as webglUtils from "@/lib/utils/webglUtils"
import { SquareIcon, TriangleIcon } from "@/app/_components/common/fontAwesomeIcons"
import Tooltip from "@mui/material/Tooltip"
import WebGLRenderingCtx, { useWebGLRenderingCtx } from "@/lib/hooks/webGLHooks"
import { translate, rotate, scale, projection } from "@/lib/features/math/linalgUtils"
import smallRectangleVertexShader, { rectangleVertexShader } from "./shaders/rectangleVert"
import smallRectangleFragmentShader, { rectangleFragmentShader } from "./shaders/rectangleFrag"
import triangleVertexShader from "./shaders/triangleVert"
import triangleFragmentShader from "./shaders/triangleFrag"
import letterFVertexShader from "./shaders/letterFVert"
import letterFFragmentShader from "./shaders/letterFFrag"
import imgProcessingVertexShader from "./shaders/imageProcessingVert"
import imgProcessingFragmentShader from "./shaders/imageProcessingFrag"
import { X_AXIS_INDEX, Y_AXIS_INDEX } from "@/lib/constants"
import InputSlider from "./InputSlider"
import { toRadians } from "@/lib/features/math/conversionCalculator"
import UnitCircle from "@/app/math/geometry/_components/UnitCircle"

interface SceneSettings {
  gl: WebGL2RenderingContext
  translateX?: number
  translateY?: number
  rotationX?: number
  rotationY?: number
}

function clearCanvas(gl: WebGL2RenderingContext) {
  gl.clearColor(0, 0, 0, 0)
  gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT)
}

function computeMatrix({ gl, translateX = 200, translateY = 150 }: SceneSettings) {
  let matrix = projection(gl.canvas.width, gl.canvas.height)
  matrix = translate(matrix, translateX, translateY)
  matrix = rotate(matrix, 0)
  return scale(matrix, 1, 1)
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

function setRectangle(gl: WebGL2RenderingContext, x: number, y: number, width: number, height: number) {
  const x1 = x
  const x2 = x + width
  const y1 = y
  const y2 = y + height
  // prettier-ignore
  gl.bufferData(gl.ARRAY_BUFFER, new Float32Array([
    x1, y1,
    x2, y1,
    x1, y2,
    x1, y2,
    x2, y1,
    x2, y2
  ]), gl.STATIC_DRAW)
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

function drawIsoscelesTriangle({ gl, translateX = 200, translateY = 150 }: SceneSettings) {
  const program = webglUtils.createProgramFromSources(gl, [triangleVertexShader, triangleFragmentShader])!

  // look up where the vertex data needs to go.
  const positionLocation = gl.getAttribLocation(program, "a_position")

  // lookup uniforms
  const matrixLocation = gl.getUniformLocation(program, "u_matrix")

  // Create set of attributes
  const vao = gl.createVertexArray()
  gl.bindVertexArray(vao)

  const buffer = gl.createBuffer()
  gl.bindBuffer(gl.ARRAY_BUFFER, buffer)
  setTriangleGeometry(gl)

  // tell the position attribute how to pull data out of the current ARRAY_BUFFER
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

    // Tell it to use the pair of shaders
    gl.useProgram(program)

    // Bind the attribute/buffer set we want.
    gl.bindVertexArray(vao)

    const matrix = computeMatrix({ gl, translateX, translateY })
    // Set the matrix.
    gl.uniformMatrix3fv(matrixLocation, false, matrix)

    drawTriangles({ gl, count: 3 })
  }

  drawScene(gl)
}

function drawSampleRectangle(gl: WebGL2RenderingContext) {
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

function drawRectangle({ gl, translateX, translateY }: SceneSettings) {
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
  // Set the matrix.
  gl.uniformMatrix3fv(matrixLocation, false, matrix)
  drawTriangles({ gl })
}

function drawLetterF({ gl, translateX = 0, translateY = 0, rotationX = 0, rotationY = 1 }: SceneSettings) {
  const program = webglUtils.createProgramFromSources(gl, [letterFVertexShader, letterFFragmentShader])!

  const positionAttributeLocation = gl.getAttribLocation(program, "a_position")
  const resolutionUniformLocation = gl.getUniformLocation(program, "u_resolution")
  const colorLocation = gl.getUniformLocation(program, "u_color")
  const translationLocation = gl.getUniformLocation(program, "u_translation")
  const rotationLocation = gl.getUniformLocation(program, "u_rotation")

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

  const translation = [translateX, translateY]
  const rotation = [rotationX, rotationY]
  const color = [Math.random(), Math.random(), Math.random(), 1]

  webglUtils.resizeCanvasToDisplaySize(gl.canvas as HTMLCanvasElement)

  gl.viewport(0, 0, gl.canvas.width, gl.canvas.height)
  clearCanvas(gl)
  gl.useProgram(program)
  gl.bindVertexArray(vao)

  gl.uniform2f(resolutionUniformLocation, gl.canvas.width, gl.canvas.height)
  gl.uniform4fv(colorLocation, color)
  gl.uniform2fv(translationLocation, translation)
  gl.uniform2fv(rotationLocation, rotation)

  const primitiveType = gl.TRIANGLES
  const count = 18
  gl.drawArrays(primitiveType, offset, count)
}

function createConvolutionKernal() {
  // prettier-ignore
  return {
    normal: [
      0, 0, 0,
      0, 1, 0,
      0, 0, 0,
    ],
    gaussianBlur: [
      0.045, 0.122, 0.045,
      0.122, 0.332, 0.122,
      0.045, 0.122, 0.045,
    ],
    gaussianBlur2: [
      1, 2, 1,
      2, 4, 2,
      1, 2, 1,
    ],
    gaussianBlur3: [
      0, 1, 0,
      1, 1, 1,
      0, 1, 0,
    ],
    unsharpen: [
      -1, -1, -1,
      -1,  9, -1,
      -1, -1, -1,
    ],
    sharpness: [
       0, -1,  0,
      -1,  5, -1,
       0, -1,  0,
    ],
    sharpen: [
      -1, -1, -1,
      -1, 16, -1,
      -1, -1, -1,
    ],
    edgeDetect: [
      -0.125, -0.125, -0.125,
      -0.125,  1,     -0.125,
      -0.125, -0.125, -0.125,
    ],
    edgeDetect2: [
      -1, -1, -1,
      -1,  8, -1,
      -1, -1, -1,
    ],
    edgeDetect3: [
      -5, 0, 0,
       0, 0, 0,
       0, 0, 5,
    ],
    edgeDetect4: [
      -1, -1, -1,
       0,  0,  0,
       1,  1,  1,
    ],
    edgeDetect5: [
      -1, -1, -1,
       2,  2,  2,
      -1, -1, -1,
    ],
    edgeDetect6: [
      -5, -5, -5,
      -5, 39, -5,
      -5, -5, -5,
    ],
    sobelHorizontal: [
       1,  2,  1,
       0,  0,  0,
      -1, -2, -1,
    ],
    sobelVertical: [
      1,  0, -1,
      2,  0, -2,
      1,  0, -1,
    ],
    previtHorizontal: [
       1,  1,  1,
       0,  0,  0,
      -1, -1, -1,
    ],
    previtVertical: [
      1,  0, -1,
      1,  0, -1,
      1,  0, -1,
    ],
    boxBlur: [
      0.111, 0.111, 0.111,
      0.111, 0.111, 0.111,
      0.111, 0.111, 0.111,
    ],
    triangleBlur: [
      0.0625, 0.125, 0.0625,
      0.125,  0.25,  0.125,
      0.0625, 0.125, 0.0625,
    ],
    emboss: [
      -2, -1,  0,
      -1,  1,  1,
       0,  1,  2,
    ],
  }
}

function createAndSetupTexture(gl: WebGL2RenderingContext) {
  const texture = gl.createTexture()
  gl.activeTexture(gl.TEXTURE0)
  gl.bindTexture(gl.TEXTURE_2D, texture)

  gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE)
  gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.CLAMP_TO_EDGE)
  gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.NEAREST)
  gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.NEAREST)
  return texture
}

function computeKernelWeight(kernels: number[]) {
  const weight = kernels.reduce((prev, curr) => prev + curr)
  return weight <= 0 ? 1 : weight
}

interface RenderImageParams {
  gl: WebGL2RenderingContext
  image: HTMLImageElement
  convolutionKernels: ReturnType<typeof createConvolutionKernal>
  convolutionKernel: string
  effects: string[]
}

function renderImage({ gl, image, convolutionKernels, convolutionKernel, effects }: RenderImageParams) {
  if (!gl) {
    return
  }

  const program = webglUtils.createProgramFromSources(gl, [imgProcessingVertexShader, imgProcessingFragmentShader])!

  const positionAttributeLocation = gl.getAttribLocation(program, "a_position")
  const texCoordAttributeLocation = gl.getAttribLocation(program, "a_texCoord")

  const resolutionLocation = gl.getUniformLocation(program, "u_resolution")
  const imageLocation = gl.getUniformLocation(program, "u_image")
  const kernelLocation = gl.getUniformLocation(program, "u_kernel[0]")
  const kernelWeightLocation = gl.getUniformLocation(program, "u_kernelWeight")
  const flipYLocation = gl.getUniformLocation(program, "u_flipY")

  const vao = gl.createVertexArray()
  gl.bindVertexArray(vao)

  const positionBuffer = gl.createBuffer()
  gl.enableVertexAttribArray(positionAttributeLocation)
  gl.bindBuffer(gl.ARRAY_BUFFER, positionBuffer)

  const size = 2
  const type = gl.FLOAT
  const normalize = false
  const stride = 0
  const offset = 0
  gl.vertexAttribPointer(positionAttributeLocation, size, type, normalize, stride, offset)

  const textureCoordinatesBuffer = gl.createBuffer()
  gl.bindBuffer(gl.ARRAY_BUFFER, textureCoordinatesBuffer)
  // prettier-ignore
  gl.bufferData(gl.ARRAY_BUFFER, new Float32Array([
    0.0,  0.0,
    1.0,  0.0,
    0.0,  1.0,
    0.0,  1.0,
    1.0,  0.0,
    1.0,  1.0,
  ]), gl.STATIC_DRAW)

  gl.enableVertexAttribArray(texCoordAttributeLocation)
  gl.vertexAttribPointer(texCoordAttributeLocation, size, type, normalize, stride, offset)

  const originalImageTexture = createAndSetupTexture(gl)
  // Upload the image into the texture.
  const mipLevel = 0
  const internalFormat = gl.RGBA
  const srcFormat = gl.RGBA
  const srcType = gl.UNSIGNED_BYTE
  gl.texImage2D(gl.TEXTURE_2D, mipLevel, internalFormat, srcFormat, srcType, image)

  const textures: WebGLTexture[] = []
  const framebuffers: WebGLFramebuffer[] = []
  for (let i = 0; i < 2; i++) {
    const texture = createAndSetupTexture(gl)
    textures.push(texture)

    const border = 0
    const data = null // no data = create a blank texture
    gl.texImage2D(gl.TEXTURE_2D, mipLevel, internalFormat, image.width, image.height, border, srcFormat, srcType, data)

    const fbo = gl.createFramebuffer()
    framebuffers.push(fbo)
    gl.bindFramebuffer(gl.FRAMEBUFFER, fbo)

    const attachmentPoint = gl.COLOR_ATTACHMENT0
    gl.framebufferTexture2D(gl.FRAMEBUFFER, attachmentPoint, gl.TEXTURE_2D, texture, mipLevel)
  }

  gl.bindBuffer(gl.ARRAY_BUFFER, positionBuffer)
  setRectangle(gl, 0, 0, image.width, image.height)

  function drawWithKernel(name: string, kernels: { [key: string]: number[] }) {
    gl.uniform1fv(kernelLocation, kernels[name])
    gl.uniform1f(kernelWeightLocation, computeKernelWeight(kernels[name]))

    const primitiveType = gl.TRIANGLES
    const offset = 0
    const count = 6
    gl.drawArrays(primitiveType, offset, count)
  }

  function drawEffects() {
    webglUtils.resizeCanvasToDisplaySize(gl.canvas as HTMLCanvasElement)

    gl.viewport(0, 0, gl.canvas.width, gl.canvas.height)
    clearCanvas(gl)
    gl.useProgram(program)

    gl.bindVertexArray(vao)

    // start with the original image on unit 0
    gl.activeTexture(gl.TEXTURE0)
    gl.bindTexture(gl.TEXTURE_2D, originalImageTexture)
    gl.uniform1i(imageLocation, 0)

    // don't y flip images while drawing to the textures
    gl.uniform1f(flipYLocation, 1)

    let count = 0
    for (const effect of effects) {
      const index = count % 2
      setFramebuffer(framebuffers[index], image.width, image.height)
      drawWithKernel(effect, convolutionKernels)

      // for the next draw, use the texture we just rendered to
      gl.bindTexture(gl.TEXTURE_2D, textures[index])

      // increment count so we use the other texture next time
      count++
    }

    gl.uniform1f(flipYLocation, -1) // need to y flip for canvas
    setFramebuffer(null, gl.canvas.width, gl.canvas.height)

    clearCanvas(gl)
    drawWithKernel(convolutionKernel, convolutionKernels)
  }

  function setFramebuffer(fbo: WebGLFramebuffer | null, width: number, height: number) {
    gl.bindFramebuffer(gl.FRAMEBUFFER, fbo)
    gl.uniform2f(resolutionLocation, width, height)
    gl.viewport(0, 0, width, height)
  }

  drawEffects()
}

interface GeometryWebGLEditorProps {
  translations: Record<string, string | Record<string, string>>
}

export default function GeometryWebGLEditor({ translations }: GeometryWebGLEditorProps) {
  useEffect(() => {
    i18n.addResourceBundle("en", "translation", translations, true, true)
  }, [translations])

  const canvasRef = useRef<HTMLCanvasElement>(null)
  const [webGLContext, setWebGLContext] = useState<WebGL2RenderingContext | null>(null)

  useEffect(() => {
    const { current: currentCanvasRef } = canvasRef
    if (!currentCanvasRef) {
      return
    }

    const gl = currentCanvasRef!.getContext("webgl2")
    if (!gl) {
      throw new Error("WebGL is not supported")
    }
    setWebGLContext(gl)

    const webglProgram = drawSampleRectangle(gl)

    return () => {
      if (webglProgram) {
        const { webglContext, program } = webglProgram
        webglContext.deleteProgram(program)
      }
    }
  }, [setWebGLContext])

  return (
    <WebGLRenderingCtx value={webGLContext}>
      <div id="geometry-webgl-editor" className="grid grid-cols-[20%_60%_20%] grid-rows-1 gap-4">
        <ToolPanel />
        <canvas ref={canvasRef} style={{ width: "100%", height: "100%" }} />
        <UnitCircle />
      </div>
    </WebGLRenderingCtx>
  )
}

const ITEM_HEIGHT = 42
const ITEM_PADDING_TOP = 8
const MenuProps = {
  PaperProps: {
    style: {
      maxHeight: ITEM_HEIGHT * 4.5 + ITEM_PADDING_TOP,
      width: 100,
    },
  },
}

function createImageEffects() {
  return [
    "normal",
    "gaussianBlur",
    "gaussianBlur2",
    "gaussianBlur3",
    "unsharpen",
    "sharpness",
    "sharpen",
    "edgeDetect",
    "edgeDetect2",
    "edgeDetect3",
    "edgeDetect4",
    "edgeDetect5",
    "edgeDetect6",
    "sobelHorizontal",
    "sobelVertical",
    "previtHorizontal",
    "previtVertical",
    "boxBlur",
    "triangleBlur",
    "emboss",
  ]
}

function ToolPanel() {
  const { t } = useTranslation()
  const gl = useWebGLRenderingCtx()
  const [geometricTranslation, setGeometricTranslation] = useState([0, 0])
  const [sceneRenderFn, setSceneRenderFn] = useState<((settings: SceneSettings) => void) | null>()
  const [selectedConvolutionKernel, setSelectedConvolutionKernel] = useState("normal")
  const [selectedImageEffects, setSelectedImageEffects] = useState<string[]>([])
  const [rotationDegrees, setRotationDegrees] = useState(0)

  const convKernels = createConvolutionKernal()

  useEffect(() => {
    if (sceneRenderFn && gl) {
      const rotationRad = toRadians(rotationDegrees)
      sceneRenderFn({
        gl,
        translateX: geometricTranslation[X_AXIS_INDEX],
        translateY: geometricTranslation[Y_AXIS_INDEX],
        rotationX: Math.sin(rotationRad),
        rotationY: Math.cos(rotationRad),
      })
    }
  }, [sceneRenderFn, gl, geometricTranslation, rotationDegrees])

  function handleDrawIsoscelesTriangleClick() {
    if (!gl) {
      return
    }
    setSceneRenderFn(() => (settings: SceneSettings) => drawIsoscelesTriangle(settings))
  }

  function handleDrawRectangleClick() {
    if (!gl) {
      return
    }
    setSceneRenderFn(() => (settings: SceneSettings) => drawRectangle(settings))
  }

  function handleDrawLetterFClick() {
    if (!gl) {
      return
    }
    setSceneRenderFn(() => (settings: SceneSettings) => drawLetterF(settings))
  }

  function handleConvKernelOnChange(event: React.ChangeEvent<HTMLSelectElement>) {
    setSelectedConvolutionKernel(event.currentTarget.value)
  }

  function handleDrawImageClick() {
    if (!gl) {
      return
    }

    const image = new Image()
    image.src = "/images/wall.jpg"
    setSceneRenderFn(
      () => () =>
        renderImage({
          gl,
          image,
          convolutionKernels: convKernels,
          convolutionKernel: selectedConvolutionKernel,
          effects: selectedImageEffects,
        }),
    )
  }

  function updatePosition(axisIndex: number, position: number) {
    const translation = [...geometricTranslation]
    translation[axisIndex] = position
    setGeometricTranslation(translation)
  }

  function handleXPositionSliderInput(e: React.ChangeEvent<HTMLInputElement>) {
    const x = Number.parseInt(e.target.value)
    updatePosition(X_AXIS_INDEX, x)
  }

  function handleYPositionSliderInput(e: React.ChangeEvent<HTMLInputElement>) {
    const y = Number.parseInt(e.target.value)
    updatePosition(Y_AXIS_INDEX, y)
  }

  const imageEffects = createImageEffects()

  function handleSelectImageEffectsOnChange(event: SelectChangeEvent<typeof imageEffects>) {
    const { value } = event.target
    setSelectedImageEffects(typeof value === "string" ? value.split(",") : value)
  }

  const kernelOptions = Object.keys(convKernels).map((kernel) => <option key={kernel}>{kernel}</option>)

  return (
    <div id="geometry-webgl-editor-tool-panel" className="grid grid-cols-1 grid-rows-3">
      <div>
        <span>{t("geometry3DEditorPage.primitives")}</span>
      </div>
      <Tooltip title="Triangle" placement="bottom-end">
        <button onClick={handleDrawIsoscelesTriangleClick}>
          <TriangleIcon />
        </button>
      </Tooltip>
      <Tooltip title="Rectangle" placement="bottom-end">
        <button onClick={handleDrawRectangleClick}>
          <SquareIcon />
        </button>
      </Tooltip>
      <Tooltip title="Letter F" placement="bottom-end">
        <button onClick={handleDrawLetterFClick}>
          <i>Letter F</i>
        </button>
      </Tooltip>
      <Tooltip title="Image" placement="bottom-end">
        <button onClick={handleDrawImageClick}>
          <i>Image</i>
        </button>
      </Tooltip>
      <select onChange={handleConvKernelOnChange}>{kernelOptions}</select>
      <FormControl sx={{ m: 1, width: 200 }}>
        <InputLabel id="select-image-effects-label">Effect</InputLabel>
        <Select
          labelId="select-image-effects-label"
          id="select-image-effects"
          multiple
          value={selectedImageEffects}
          onChange={handleSelectImageEffectsOnChange}
          input={<OutlinedInput label="select-image-effects-label" />}
          renderValue={(selected) => selected.join(", ")}
          MenuProps={MenuProps}
        >
          {imageEffects.map((name) => (
            <MenuItem key={name} value={name}>
              <Checkbox checked={selectedImageEffects.includes(name)} />
              <ListItemText primary={name} />
            </MenuItem>
          ))}
        </Select>
      </FormControl>
      <div>
        <span>{t("geometry3DEditorPage.lights")}</span>
      </div>
      <div>
        <span>Transformations</span>
      </div>
      <div className="slide-container">
        <p>
          <label htmlFor="x-position-slider">x</label>
          <input
            id="x-position-slider"
            className="slider"
            type="range"
            min="0"
            max={gl?.canvas.width}
            step="1"
            value={geometricTranslation[X_AXIS_INDEX]}
            onInput={handleXPositionSliderInput}
          />
        </p>
        <p>
          <label htmlFor="y-position-slider">y</label>
          <input
            id="y-position-slider"
            className="slider"
            type="range"
            min="0"
            max={gl?.canvas.height}
            step="1"
            value={geometricTranslation[Y_AXIS_INDEX]}
            onInput={handleYPositionSliderInput}
          />
        </p>
      </div>
      <InputSlider label="Rotation" value={rotationDegrees} setValue={setRotationDegrees} />
    </div>
  )
}
