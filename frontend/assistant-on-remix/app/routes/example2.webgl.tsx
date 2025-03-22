import React from "react"

const MOUSE_PRIMARY_BUTTON = 1

function paintDarkBackground(gl: WebGLRenderingContext) {
  gl.clearColor(0.1, 0.1, 0.1, 1.0)
  gl.clear(gl.COLOR_BUFFER_BIT)
}

function drawGrid(gl: WebGLRenderingContext, color: number[]) {
  const gridShaderCode = `
    attribute vec2 position;
    void main() {
      gl_Position = vec4(position, 0.0, 1.0);
    }
  `
  const gridFragmentShaderCode = `
    precision mediump float;
    uniform vec3 color;
    void main() {
      gl_FragColor = vec4(color, 1.0);
    }
  `

  const gridProgram = createProgram(gl, gridShaderCode, gridFragmentShaderCode)
  const gridPositionLocation = gl.getAttribLocation(gridProgram, "position")
  const gridColorLocation = gl.getUniformLocation(gridProgram, "color")

  gl.useProgram(gridProgram)
  gl.uniform3fv(gridColorLocation, color)

  const gridBuffer = gl.createBuffer()
  gl.bindBuffer(gl.ARRAY_BUFFER, gridBuffer)
  const gridLines = []
  for (let i = -20; i <= 20; i += 1) {
    // Adjust grid range and density
    gridLines.push(i, -20, i, 20)
    gridLines.push(-20, i, 20, i)
  }
  gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(gridLines), gl.STATIC_DRAW)
  gl.enableVertexAttribArray(gridPositionLocation)
  gl.vertexAttribPointer(gridPositionLocation, 2, gl.FLOAT, false, 0, 0)
  gl.drawArrays(gl.LINES, 0, gridLines.length / 2)
}

function createProgram(gl: WebGLRenderingContext, vertexShaderSource: string, fragmentShaderSource: string) {
  const vertexShader = gl.createShader(gl.VERTEX_SHADER)!
  gl.shaderSource(vertexShader, vertexShaderSource)
  gl.compileShader(vertexShader)

  const fragmentShader = gl.createShader(gl.FRAGMENT_SHADER)!
  gl.shaderSource(fragmentShader, fragmentShaderSource)
  gl.compileShader(fragmentShader)

  const program = gl.createProgram()!
  gl.attachShader(program, vertexShader)
  gl.attachShader(program, fragmentShader)
  gl.linkProgram(program)
  return program
}

export default function Example2WebGL() {
  const canvasRef = React.useRef<HTMLCanvasElement>(null)
  const offsetRef = React.useRef({ x: 0, y: 0 })
  const zoomRef = React.useRef(0.1) // Start with a zoomed-out value

  React.useEffect(() => {
    const canvas = canvasRef.current
    if (canvas == null) {
      return
    }

    const gl = canvas.getContext("webgl")

    if (!gl) {
      alert("Unable to initialize WebGL. Your browser may not support it.")
      return
    }

    const drawScene = () => {
      paintDarkBackground(gl)

      const greyGridColor = [0.5, 0.5, 0.5]
      drawGrid(gl, greyGridColor)

      const vertices = [
        0 + offsetRef.current.x,
        3 * zoomRef.current + offsetRef.current.y,
        1 * zoomRef.current + offsetRef.current.x,
        1 * zoomRef.current + offsetRef.current.y,
        2 * zoomRef.current + offsetRef.current.x,
        3 * zoomRef.current + offsetRef.current.y,
      ]

      const vertexBuffer = gl.createBuffer()
      gl.bindBuffer(gl.ARRAY_BUFFER, vertexBuffer)
      gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW)

      const vertexShaderCode = `
        attribute vec2 coordinates;
        void main(void) {
          gl_Position = vec4(coordinates, 0.0, 1.0);
          gl_PointSize = 10.0;
        }
      `

      const vertexShader = gl.createShader(gl.VERTEX_SHADER)!
      gl.shaderSource(vertexShader, vertexShaderCode)
      gl.compileShader(vertexShader)

      const fragmentShaderCode = `
        void main(void) {
          gl_FragColor = vec4(0.0, 0.0, 1.0, 1.0); // Blue
        }
      `

      const fragmentShader = gl.createShader(gl.FRAGMENT_SHADER)!
      gl.shaderSource(fragmentShader, fragmentShaderCode)
      gl.compileShader(fragmentShader)

      const shaderProgram = gl.createProgram()!
      gl.attachShader(shaderProgram, vertexShader)
      gl.attachShader(shaderProgram, fragmentShader)
      gl.linkProgram(shaderProgram)
      gl.useProgram(shaderProgram)

      const coord = gl.getAttribLocation(shaderProgram, "coordinates")
      gl.vertexAttribPointer(coord, 2, gl.FLOAT, false, 0, 0)
      gl.enableVertexAttribArray(coord)

      gl.drawArrays(gl.POINTS, 0, 3)
    }

    drawScene()

    function handleMouseMove(event: MouseEvent) {
      if (event.buttons === MOUSE_PRIMARY_BUTTON) {
        offsetRef.current.x += event.movementX / 100
        offsetRef.current.y -= event.movementY / 100
        drawScene()
      }
    }

    function handleWheel(event: WheelEvent) {
      const zoomAdjustment = event.deltaY > 0 ? 0.9 : 1.1
      zoomRef.current *= zoomAdjustment
      drawScene()
    }

    canvas.addEventListener("mousemove", handleMouseMove)
    canvas.addEventListener("wheel", handleWheel)

    return () => {
      canvas.removeEventListener("mousemove", handleMouseMove)
      canvas.removeEventListener("wheel", handleWheel)
    }
  }, [])

  return <canvas ref={canvasRef} style={{ width: "100vw", height: "100vh" }}></canvas>
}
