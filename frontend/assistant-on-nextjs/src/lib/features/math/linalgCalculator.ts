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

export function multiply3d(a: Float32Array | Array<number>, b: Float32Array | Array<number>) {
  const a00 = a[0] // 0 * 4 + 0
  const a01 = a[1] // 0 * 4 + 1
  const a02 = a[2] // 0 * 4 + 2
  const a03 = a[3] // 0 * 4 + 3
  const a10 = a[4] // 1 * 4 + 0
  const a11 = a[5] // 1 * 4 + 1
  const a12 = a[6] // 1 * 4 + 2
  const a13 = a[7] // 1 * 4 + 3
  const a20 = a[8] // 2 * 4 + 0
  const a21 = a[9] // 2 * 4 + 1
  const a22 = a[10] // 2 * 4 + 2
  const a23 = a[11] // 2 * 4 + 3
  const a30 = a[12] // 3 * 4 + 0
  const a31 = a[13] // 3 * 4 + 1
  const a32 = a[14] // 3 * 4 + 2
  const a33 = a[15] // 3 * 4 + 3

  const b00 = b[0] // 0 * 4 + 0
  const b01 = b[1] // 0 * 4 + 1
  const b02 = b[2] // 0 * 4 + 2
  const b03 = b[3] // 0 * 4 + 3
  const b10 = b[4] // 1 * 4 + 0
  const b11 = b[5] // 1 * 4 + 1
  const b12 = b[6] // 1 * 4 + 2
  const b13 = b[7] // 1 * 4 + 3
  const b20 = b[8] // 2 * 4 + 0
  const b21 = b[9] // 2 * 4 + 1
  const b22 = b[10] // 2 * 4 + 2
  const b23 = b[11] // 2 * 4 + 3
  const b30 = b[12] // 3 * 4 + 0
  const b31 = b[13] // 3 * 4 + 1
  const b32 = b[14] // 3 * 4 + 2
  const b33 = b[15] // 3 * 4 + 3

  // prettier-ignore
  return [
    b00 * a00 + b01 * a10 + b02 * a20 + b03 * a30,
    b00 * a01 + b01 * a11 + b02 * a21 + b03 * a31,
    b00 * a02 + b01 * a12 + b02 * a22 + b03 * a32,
    b00 * a03 + b01 * a13 + b02 * a23 + b03 * a33,
    b10 * a00 + b11 * a10 + b12 * a20 + b13 * a30,
    b10 * a01 + b11 * a11 + b12 * a21 + b13 * a31,
    b10 * a02 + b11 * a12 + b12 * a22 + b13 * a32,
    b10 * a03 + b11 * a13 + b12 * a23 + b13 * a33,
    b20 * a00 + b21 * a10 + b22 * a20 + b23 * a30,
    b20 * a01 + b21 * a11 + b22 * a21 + b23 * a31,
    b20 * a02 + b21 * a12 + b22 * a22 + b23 * a32,
    b20 * a03 + b21 * a13 + b22 * a23 + b23 * a33,
    b30 * a00 + b31 * a10 + b32 * a20 + b33 * a30,
    b30 * a01 + b31 * a11 + b32 * a21 + b33 * a31,
    b30 * a02 + b31 * a12 + b32 * a22 + b33 * a32,
    b30 * a03 + b31 * a13 + b32 * a23 + b33 * a33,
  ]
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
 * @return a projection matrix that converts from pixels to clip space.
 */
export function orthographic(left: number, right: number, bottom: number, top: number, near: number, far: number) {
  // prettier-ignore
  return [
    2 / (right - left), 0, 0, 0,
    0, 2 / (top - bottom), 0, 0,
    0, 0, 2 / (near - far), 0,

    (left + right) / (left - right),
    (bottom + top) / (bottom - top),
    (near + far) / (near - far),
    1,
  ]
}

/**
 * @param width width in pixels
 * @param height height in pixels
 * @param dst an optional matrix to store the result
 * @return a projection matrix that converts from pixels to clip space with Y = 0 at the top.
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

export function projection3d(width: number, height: number, depth: number) {
  // Note: This matrix flips the Y axis so 0 is at the top.
  // prettier-ignore
  return [
    2 / width, 0, 0, 0,
    0, -2 / height, 0, 0,
    0, 0, 2 / depth, 0,
    -1, 1, 0, 1,
  ]
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

export function translation3d(tx: number, ty: number, tz: number) {
  // prettier-ignore
  return [
    1,  0,  0,  0,
    0,  1,  0,  0,
    0,  0,  1,  0,
    tx, ty, tz, 1,
  ]
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

export function translate3d(matrix: Float32Array | Array<number>, tx: number, ty: number, tz: number) {
  return multiply3d(matrix, translation3d(tx, ty, tz))
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

export function xRotation(angleInRadians: number) {
  const c = Math.cos(angleInRadians)
  const s = Math.sin(angleInRadians)

  // prettier-ignore
  return [
    1, 0, 0, 0,
    0, c, s, 0,
    0, -s, c, 0,
    0, 0, 0, 1,
  ]
}

export function yRotation(angleInRadians: number) {
  const c = Math.cos(angleInRadians)
  const s = Math.sin(angleInRadians)

  // prettier-ignore
  return [
    c, 0, -s, 0,
    0, 1, 0, 0,
    s, 0, c, 0,
    0, 0, 0, 1,
  ]
}

export function zRotation(angleInRadians: number) {
  const c = Math.cos(angleInRadians)
  const s = Math.sin(angleInRadians)

  // prettier-ignore
  return [
    c, s, 0, 0,
    -s, c, 0, 0,
    0, 0, 1, 0,
    0, 0, 0, 1,
  ]
}

/**
 * @return the result of multiplication by a 2D rotation matrix
 */
export function rotate(matrix: Float32Array | Array<number>, angleInRadians: number, dst?: Float32Array) {
  return multiply(matrix, rotation(angleInRadians), dst)
}

export function xRotate(matrix: Float32Array | Array<number>, angleInRadians: number) {
  return multiply3d(matrix, xRotation(angleInRadians))
}

export function yRotate(matrix: Float32Array | Array<number>, angleInRadians: number) {
  return multiply3d(matrix, yRotation(angleInRadians))
}

export function zRotate(matrix: Float32Array | Array<number>, angleInRadians: number) {
  return multiply3d(matrix, zRotation(angleInRadians))
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

export function scaling3d(sx: number, sy: number, sz: number) {
  // prettier-ignore
  return [
    sx, 0,  0,  0,
    0, sy,  0,  0,
    0,  0, sz,  0,
    0,  0,  0,  1,
  ]
}

/**
 * @return the result of multiplication by a 2D scaling matrix
 */
export function scale(matrix: Float32Array | Array<number>, scaleX: number, scaleY: number, dst?: Float32Array) {
  return multiply(matrix, scaling(scaleX, scaleY), dst)
}

export function scale3d(matrix: Float32Array | Array<number>, sx: number, sy: number, sz: number) {
  return multiply3d(matrix, scaling3d(sx, sy, sz))
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
