const rectangleFragmentShader = `#version 300 es

precision highp float;
out vec4 outColor;

void main() {
  // redish-purple
  outColor = vec4(1, 0, 0.5, 1);
}
`

export default rectangleFragmentShader
