"use client"

import React, { useEffect, useRef, useState } from "react"
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

    function clearCanvas(gl: WebGL2RenderingContext) {
      gl.clearColor(0, 0, 0, 0)
      gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT)
    }

    function draw(gl: WebGL2RenderingContext) {
      const primitiveType = gl.TRIANGLES
      const offset = 0
      const count = 6
      gl.drawArrays(primitiveType, offset, count)
    }

    function main() {
      const gl = currentCanvasRef!.getContext("webgl2")
      if (!gl) {
        return
      }
      setWebGLContext(gl)

      const program = webglUtils.createProgramFromSources(gl, [
        smallRectangleVertexShader,
        smallRectangleFragmentShader,
      ])!

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

      draw(gl)

      return { webglContext: gl, program }
    }

    const webglProgram = main()

    return () => {
      if (webglProgram) {
        const { webglContext, program } = webglProgram
        webglContext.deleteProgram(program)
      }
    }
  })

  return (
    <WebGLRenderingCtx value={webGLContext}>
      <div id="geometry-webgl-editor" className="grid grid-cols-[12%_88%] grid-rows-1 gap-4">
        <ToolPanel />
        <canvas ref={canvasRef} style={{ width: "100vw", height: "100vh" }} />
      </div>
    </WebGLRenderingCtx>
  )
}

function ToolPanel() {
  const { t } = useTranslation()
  const gl = useWebGLRenderingCtx()

  function handleDrawIsoscelesTriangleClick() {
    if (!gl) {
      return
    }

    function drawIsoscelesTriangle(gl: WebGL2RenderingContext) {
      const program = webglUtils.createProgramFromSources(gl, [triangleVertexShader, triangleFragmentShader])!

      // look up where the vertex data needs to go.
      const positionLocation = gl.getAttribLocation(program, "a_position")

      // lookup uniforms
      const matrixLocation = gl.getUniformLocation(program, "u_matrix")

      // Create set of attributes
      const vao = gl.createVertexArray()
      gl.bindVertexArray(vao)

      // Create a buffer.
      const buffer = gl.createBuffer()
      gl.bindBuffer(gl.ARRAY_BUFFER, buffer)

      // Set Geometry.
      setGeometry(gl)

      // tell the position attribute how to pull data out of the current ARRAY_BUFFER
      gl.enableVertexAttribArray(positionLocation)
      const size = 2
      const type = gl.FLOAT
      const normalize = false
      const stride = 0
      const offset = 0
      gl.vertexAttribPointer(positionLocation, size, type, normalize, stride, offset)

      function computeMatrix(gl: WebGL2RenderingContext) {
        let matrix = projection(gl.canvas.width, gl.canvas.height)
        matrix = translate(matrix, 200, 150)
        matrix = rotate(matrix, 0)
        return scale(matrix, 1, 1)
      }

      function drawScene(gl: WebGL2RenderingContext) {
        webglUtils.resizeCanvasToDisplaySize(gl.canvas as HTMLCanvasElement)

        gl.viewport(0, 0, gl.canvas.width, gl.canvas.height)

        // Clear the canvas
        gl.clearColor(0, 0, 0, 0)
        gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT)

        // Tell it to use the pair of shaders
        gl.useProgram(program)

        // Bind the attribute/buffer set we want.
        gl.bindVertexArray(vao)

        const matrix = computeMatrix(gl)
        // Set the matrix.
        gl.uniformMatrix3fv(matrixLocation, false, matrix)

        // Draw the geometry.
        const offset = 0
        const count = 3
        gl.drawArrays(gl.TRIANGLES, offset, count)
      }

      drawScene(gl)
    }

    // Fill the buffer with the values that define a triangle.
    function setGeometry(gl: WebGL2RenderingContext) {
      gl.bufferData(gl.ARRAY_BUFFER, new Float32Array([0, -100, 150, 125, -175, 100]), gl.STATIC_DRAW)
    }

    drawIsoscelesTriangle(gl)
  }

  function handleDrawRectangleClick() {
    function main() {
      if (!gl) {
        return
      }

      // setup GLSL program
      const program = webglUtils.createProgramFromSources(gl, [rectangleVertexShader, rectangleFragmentShader])!

      // look up where the vertex data needs to go.
      const positionLocation = gl.getAttribLocation(program, "a_position")
      const colorLocation = gl.getAttribLocation(program, "a_color")

      // lookup uniforms
      const matrixLocation = gl.getUniformLocation(program, "u_matrix")

      // Create set of attributes
      const vao = gl.createVertexArray()
      gl.bindVertexArray(vao)

      // Create a buffer for the positons.
      let buffer = gl.createBuffer()
      gl.bindBuffer(gl.ARRAY_BUFFER, buffer)

      // Set Geometry.
      setGeometry(gl)

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
      // Set the colors.
      setColors(gl)

      // tell the color attribute how to pull data out of the current ARRAY_BUFFER
      gl.enableVertexAttribArray(colorLocation)
      size = 4
      gl.vertexAttribPointer(colorLocation, size, type, normalize, stride, offset)

      function computeMatrix(gl: WebGL2RenderingContext) {
        let matrix = projection(gl.canvas.width, gl.canvas.height)
        matrix = translate(matrix, 200, 150)
        matrix = rotate(matrix, 0)
        return scale(matrix, 1, 1)
      }

      drawScene(gl)

      // Draw the scene.
      function drawScene(gl: WebGL2RenderingContext) {
        webglUtils.resizeCanvasToDisplaySize(gl.canvas as HTMLCanvasElement)

        gl.viewport(0, 0, gl.canvas.width, gl.canvas.height)

        // Clear the canvas
        gl.clearColor(0, 0, 0, 0)
        gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT)

        // Tell it to use our program (pair of shaders)
        gl.useProgram(program)

        // Bind the attribute/buffer set we want.
        gl.bindVertexArray(vao)

        const matrix = computeMatrix(gl)
        // Set the matrix.
        gl.uniformMatrix3fv(matrixLocation, false, matrix)

        // Draw the geometry.
        const offset = 0
        const count = 6
        gl.drawArrays(gl.TRIANGLES, offset, count)
      }
    }

    // Fill the buffer with the values that define a rectangle.
    function setGeometry(gl: WebGL2RenderingContext) {
      gl.bufferData(
        gl.ARRAY_BUFFER,
        new Float32Array([-150, -100, 150, -100, -150, 100, 150, -100, -150, 100, 150, 100]),
        gl.STATIC_DRAW,
      )
    }

    // Fill the buffer with colors for the 2 triangles
    // that make the rectangle.
    function setColors(gl: WebGL2RenderingContext) {
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

    main()
  }

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
      <div>
        <span>{t("geometry3DEditorPage.lights")}</span>
      </div>
    </div>
  )
}
