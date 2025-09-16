import { dot, distance } from "./utils"

function destinationMatrix(dst?: Float32Array, length: number = 9) {
  return dst || new Float32Array(length)
}

export function multiply(
  matrixA: Float32Array | Array<number>,
  matrixB: Float32Array | Array<number>,
  dst?: Float32Array,
) {
  dst = destinationMatrix(dst)

  const a00 = matrixA[0] // 0 * 3 + 0
  const a01 = matrixA[1] // 0 * 3 + 1
  const a02 = matrixA[2] // 0 * 3 + 2
  const a10 = matrixA[3] // 1 * 3 + 0
  const a11 = matrixA[4] // 1 * 3 + 1
  const a12 = matrixA[5] // 1 * 3 + 2
  const a20 = matrixA[6] // 2 * 3 + 0
  const a21 = matrixA[7] // 2 * 3 + 1
  const a22 = matrixA[8] // 2 * 3 + 2

  const b00 = matrixB[0] // 0 * 3 + 0
  const b01 = matrixB[1] // 0 * 3 + 1
  const b02 = matrixB[2] // 0 * 3 + 2
  const b10 = matrixB[3] // 1 * 3 + 0
  const b11 = matrixB[4] // 1 * 3 + 1
  const b12 = matrixB[5] // 1 * 3 + 2
  const b20 = matrixB[6] // 2 * 3 + 0
  const b21 = matrixB[7] // 2 * 3 + 1
  const b22 = matrixB[8] // 2 * 3 + 2

  dst[0] = b00 * a00 + b01 * a10 + b02 * a20
  dst[1] = b00 * a01 + b01 * a11 + b02 * a21
  dst[2] = b00 * a02 + b01 * a12 + b02 * a22
  dst[3] = b10 * a00 + b11 * a10 + b12 * a20
  dst[4] = b10 * a01 + b11 * a11 + b12 * a21
  dst[5] = b10 * a02 + b11 * a12 + b12 * a22
  dst[6] = b20 * a00 + b21 * a10 + b22 * a20
  dst[7] = b20 * a01 + b21 * a11 + b22 * a21
  dst[8] = b20 * a02 + b21 * a12 + b22 * a22

  return dst
}

/**
 * @param dst an optional matrix to store the result
 * @return a 3x3 identity matrix
 */
export function identity(dst?: Float32Array) {
  dst = destinationMatrix(dst)
  dst[0] = 1
  dst[1] = 0
  dst[2] = 0
  dst[3] = 0
  dst[4] = 1
  dst[5] = 0
  dst[6] = 0
  dst[7] = 0
  dst[8] = 1

  return dst
}

/**
 * @param width width in pixels
 * @param height height in pixels
 * @param dst an optional matrix to store the result
 * @return a projection matrix that converts from pixels to clipspace with Y = 0 at the top.
 */
export function projection(width: number, height: number, dst?: Float32Array) {
  dst = destinationMatrix(dst)
  // this matrix flips the Y axis so 0 is at the top.

  dst[0] = 2 / width
  dst[1] = 0
  dst[2] = 0
  dst[3] = 0
  dst[4] = -2 / height
  dst[5] = 0
  dst[6] = -1
  dst[7] = 1
  dst[8] = 1

  return dst
}

/**
 * @param matrix the matrix to be multiplied
 * @param width width in pixels
 * @param height height in pixels
 * @param dst an optional matrix to store the result
 * @return the result of multiplication by a 2D projection matrix
 */
export function project(matrix: Float32Array | Array<number>, width: number, height: number, dst?: Float32Array) {
  return multiply(matrix, projection(width, height), dst)
}

export function translation(translateX: number, translateY: number, dst?: Float32Array) {
  dst = destinationMatrix(dst)

  dst[0] = 1
  dst[1] = 0
  dst[2] = 0
  dst[3] = 0
  dst[4] = 1
  dst[5] = 0
  dst[6] = translateX
  dst[7] = translateY
  dst[8] = 1

  return dst
}

/**
 * @return the result of multiplication by a 2D translation matrix
 */
export function translate(
  matrix: Float32Array | Array<number>,
  translateX: number,
  translateY: number,
  dst?: Float32Array,
) {
  return multiply(matrix, translation(translateX, translateY), dst)
}

/**
 * @param angleInRadians amount to rotate in radians
 * @param dst an optional matrix to store the result
 * @return a 2D rotation matrix
 */
export function rotation(angleInRadians: number, dst?: Float32Array) {
  const cosine = Math.cos(angleInRadians)
  const sine = Math.sin(angleInRadians)

  dst = destinationMatrix(dst)

  dst[0] = cosine
  dst[1] = -sine
  dst[2] = 0
  dst[3] = sine
  dst[4] = cosine
  dst[5] = 0
  dst[6] = 0
  dst[7] = 0
  dst[8] = 1

  return dst
}

/**
 * @return the result of multiplication by a 2D rotation matrix
 */
export function rotate(matrix: Float32Array | Array<number>, angleInRadians: number, dst?: Float32Array) {
  return multiply(matrix, rotation(angleInRadians), dst)
}

/**
 * @return a 2D scale matrix
 */
export function scaling(scaleX: number, scaleY: number, dst?: Float32Array) {
  dst = destinationMatrix(dst)

  dst[0] = scaleX
  dst[1] = 0
  dst[2] = 0
  dst[3] = 0
  dst[4] = scaleY
  dst[5] = 0
  dst[6] = 0
  dst[7] = 0
  dst[8] = 1

  return dst
}

/**
 * @return the result of multiplication by a 2D scaling matrix
 */
export function scale(matrix: Float32Array | Array<number>, scaleX: number, scaleY: number, dst?: Float32Array) {
  return multiply(matrix, scaling(scaleX, scaleY), dst)
}

export function normalize(x: number, y: number) {
  const length = distance(0, 0, x, y)
  if (length > 0.00001) {
    return [x / length, y / length]
  } else {
    return [0, 0]
  }
}

export function reflect(incidentX: number, incidentY: number, normalX: number, normalY: number) {
  // I - 2.0 * dot(N, I) * N.
  const d = dot(normalX, normalY, incidentX, incidentY)
  return [incidentX - 2 * d * normalX, incidentY - 2 * d * normalY]
}

export function transformPoint(
  matrix: Float32Array | Array<number>,
  transformationVector: Float32Array | Array<number>,
) {
  const [v0, v1] = transformationVector

  const m2 = matrix[2] // 0 * 3 + 2
  const m5 = matrix[5] // 1 * 3 + 2
  const m8 = matrix[8] // 2 * 3 + 2

  const d = v0 * m2 + v1 * m5 + m8

  const m0 = matrix[0] // 0 * 3 + 0
  const m1 = matrix[1] // 0 * 3 + 1
  const m3 = matrix[3] // 1 * 3 + 0
  const m4 = matrix[4] // 1 * 3 + 1
  const m6 = matrix[6] // 2 * 3 + 0
  const m7 = matrix[7] // 2 * 3 + 1

  return [(v0 * m0 + v1 * m3 + m6) / d, (v0 * m1 + v1 * m4 + m7) / d]
}

export function inverse(matrix: Float32Array | Array<number>, dst?: Float32Array) {
  dst = destinationMatrix(dst)

  const m00 = matrix[0] // 0 * 3 + 0
  const m01 = matrix[1] // 0 * 3 + 1
  const m02 = matrix[2] // 0 * 3 + 2
  const m10 = matrix[3] // 1 * 3 + 0
  const m11 = matrix[4] // 1 * 3 + 1
  const m12 = matrix[5] // 1 * 3 + 2
  const m20 = matrix[6] // 2 * 3 + 0
  const m21 = matrix[7] // 2 * 3 + 1
  const m22 = matrix[8] // 2 * 3 + 2

  const b01 = m22 * m11 - m12 * m21
  const b11 = -m22 * m10 + m12 * m20
  const b21 = m21 * m10 - m11 * m20

  const det = m00 * b01 + m01 * b11 + m02 * b21
  const invDet = 1.0 / det

  dst[0] = b01 * invDet
  dst[1] = (-m22 * m01 + m02 * m21) * invDet
  dst[2] = (m12 * m01 - m02 * m11) * invDet
  dst[3] = b11 * invDet
  dst[4] = (m22 * m00 - m02 * m20) * invDet
  dst[5] = (-m12 * m00 + m02 * m10) * invDet
  dst[6] = b21 * invDet
  dst[7] = (-m21 * m00 + m01 * m20) * invDet
  dst[8] = (m11 * m00 - m01 * m10) * invDet

  return dst
}
