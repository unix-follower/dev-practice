"use client"

import React, { useEffect, useRef } from "react"
import { useTranslation } from "react-i18next"
import i18n from "@/config/i18n"
import * as webglUtils from "@/lib/utils/webglUtils"

const vertexShaderSource = `#version 300 es

in vec2 a_position;

uniform vec2 u_resolution;

void main() {
  // convert the position from pixels to 0.0 to 1.0
  vec2 zeroToOne = a_position / u_resolution;

  // convert from 0->1 to 0->2
  vec2 zeroToTwo = zeroToOne * 2.0;

  // convert from 0->2 to -1->+1 (clipspace)
  vec2 clipSpace = zeroToTwo - 1.0;

  gl_Position = vec4(clipSpace, 0, 1);
}
`

const fragmentShaderSource = `#version 300 es

precision highp float;
out vec4 outColor;

void main() {
  // redish-purple
  outColor = vec4(1, 0, 0.5, 1);
}
`

interface GeometryWebGLEditorProps {
  translations: Record<string, string | Record<string, string>>
}

export default function GeometryWebGLEditor({ translations }: GeometryWebGLEditorProps) {
  useEffect(() => {
    i18n.addResourceBundle("en", "translation", translations, true, true)
  }, [translations])

  const canvasRef = useRef<HTMLCanvasElement>(null)

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

      const program = webglUtils.createProgramFromSources(gl, [vertexShaderSource, fragmentShaderSource])!

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
    <div id="geometry-threejs-editor" className="grid grid-cols-[12%_88%] grid-rows-1 gap-4">
      <ToolPanel />
      <canvas ref={canvasRef} style={{ width: "100vw", height: "100vh" }} />
    </div>
  )
}

function ToolPanel() {
  const { t } = useTranslation()

  return (
    <div id="geometry-threejs-editor-tool-panel" className="grid grid-cols-1 grid-rows-3">
      <div>
        <span>{t("geometry3DEditorPage.primitives")}</span>
      </div>
      <div>
        <span>{t("geometry3DEditorPage.lights")}</span>
      </div>
    </div>
  )
}
