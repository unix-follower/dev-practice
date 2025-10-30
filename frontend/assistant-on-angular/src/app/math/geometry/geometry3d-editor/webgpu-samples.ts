/// <reference types="@webgpu/types" />

// const redTriangleShader = /* wgsl */ `
// @vertex fn vs(
//   @builtin(vertex_index) vertexIndex : u32
// ) -> @builtin(position) vec4f {
//   let pos = array(
//     vec2f( 0.0,  0.5), // top center
//     vec2f(-0.5, -0.5), // bottom left
//     vec2f( 0.5, -0.5)  // bottom right
//   );
//
//   return vec4f(pos[vertexIndex], 0.0, 1.0);
// }
//
// @fragment fn fs() -> @location(0) vec4f {
//   return vec4f(1.0, 0.0, 0.0, 1.0);
// }
// `

// const rgbTriangleShader = /* wgsl */ `
// struct OurVertexShaderOutput {
//   @builtin(position) position: vec4f,
//   @location(0) color: vec4f,
// };
//
// @vertex fn vs(
//   @builtin(vertex_index) vertexIndex : u32
// ) -> OurVertexShaderOutput {
//   let pos = array(
//     vec2f( 0.0,  0.5),  // top center
//     vec2f(-0.5, -0.5),  // bottom left
//     vec2f( 0.5, -0.5)   // bottom right
//   );
//   var color = array<vec4f, 3>(
//     vec4f(1, 0, 0, 1), // red
//     vec4f(0, 1, 0, 1), // green
//     vec4f(0, 0, 1, 1), // blue
//   );
//
//   var vsOutput: OurVertexShaderOutput;
//   vsOutput.position = vec4f(pos[vertexIndex], 0.0, 1.0);
//   vsOutput.color = color[vertexIndex];
//   return vsOutput;
// }
//
// @fragment fn fs(fsInput: OurVertexShaderOutput) -> @location(0) vec4f {
//   return fsInput.color;
// }
// `

// const checkerboardTriangleShader = /* wgsl */ `
// struct OurVertexShaderOutput {
//   @builtin(position) position: vec4f,
// };
//
// @vertex fn vs(
//   @builtin(vertex_index) vertexIndex : u32
// ) -> OurVertexShaderOutput {
//   let pos = array(
//     vec2f( 0.0,  0.5),  // top center
//     vec2f(-0.5, -0.5),  // bottom left
//     vec2f( 0.5, -0.5)   // bottom right
//   );
//
//   var vsOutput: OurVertexShaderOutput;
//   vsOutput.position = vec4f(pos[vertexIndex], 0.0, 1.0);
//   return vsOutput;
// }
//
// @fragment fn fs(fsInput: OurVertexShaderOutput) -> @location(0) vec4f {
//   let red = vec4f(1, 0, 0, 1);
//   let cyan = vec4f(0, 1, 1, 1);
//
//   let grid = vec2u(fsInput.position.xy) / 8;
//   let checker = (grid.x + grid.y) % 2 == 1;
//
//   return select(red, cyan, checker);
// }
// `

const checkerboardTriangleVsShader = /* wgsl */ `
struct OurVertexShaderOutput {
  @builtin(position) position: vec4f,
};

@vertex fn vs(
  @builtin(vertex_index) vertexIndex : u32
) -> OurVertexShaderOutput {
  let pos = array(
    vec2f( 0.0,  0.5),  // top center
    vec2f(-0.5, -0.5),  // bottom left
    vec2f( 0.5, -0.5)   // bottom right
  );

  var vsOutput: OurVertexShaderOutput;
  vsOutput.position = vec4f(pos[vertexIndex], 0.0, 1.0);
  return vsOutput;
}
`

const checkerboardTriangleFsShader = /* wgsl */ `@fragment fn fs(@builtin(position) pixelPosition: vec4f) -> @location(0) vec4f {
  let red = vec4f(1, 0, 0, 1);
  let cyan = vec4f(0, 1, 1, 1);

  let grid = vec2u(pixelPosition.xy) / 8;
  let checker = (grid.x + grid.y) % 2 == 1;

  return select(red, cyan, checker);
}
`

export async function runExample1(webGpuCtx: GPUCanvasContext, device: GPUDevice) {
  const { canvas } = webGpuCtx
  const presentationFormat = navigator.gpu.getPreferredCanvasFormat()
  webGpuCtx.configure({
    device,
    format: presentationFormat,
  })

  const vsModule = device.createShaderModule({
    label: "triangle vertex shader",
    code: checkerboardTriangleVsShader,
  })

  const fsModule = device.createShaderModule({
    label: "triangle fragment shader",
    code: checkerboardTriangleFsShader,
  })

  const pipeline = device.createRenderPipeline({
    label: "our hardcoded red triangle pipeline",
    layout: "auto",
    vertex: {
      entryPoint: "vs",
      module: vsModule,
    },
    fragment: {
      entryPoint: "fs",
      module: fsModule,
      targets: [{ format: presentationFormat }],
    },
  })

  const renderPassDescriptor = {
    label: "our basic canvas renderPass",
    colorAttachments: [
      {
        clearValue: [0.3, 0.3, 0.3, 1],
        loadOp: "clear",
        storeOp: "store",
      },
    ] as unknown as GPURenderPassColorAttachment[],
  }

  function render(device: GPUDevice) {
    renderPassDescriptor.colorAttachments[0].view = webGpuCtx.getCurrentTexture().createView()

    const encoder = device.createCommandEncoder({ label: "our encoder" })

    const pass = encoder.beginRenderPass(renderPassDescriptor)
    pass.setPipeline(pipeline)
    pass.draw(3)
    pass.end()

    const commandBuffer = encoder.finish()
    device.queue.submit([commandBuffer])
  }

  const observer = new ResizeObserver((entries) => {
    for (const entry of entries) {
      const canvas = entry.target as HTMLCanvasElement
      const width = entry.contentBoxSize[0].inlineSize
      const height = entry.contentBoxSize[0].blockSize
      canvas.width = Math.max(1, Math.min(width, device.limits.maxTextureDimension2D))
      canvas.height = Math.max(1, Math.min(height, device.limits.maxTextureDimension2D))
    }
    render(device!)
  })
  observer.observe(canvas as Element)
}

export async function runExample2(device: GPUDevice) {
  const module = device.createShaderModule({
    label: "doubling compute module",
    code: /* wgsl */ `
      @group(0) @binding(0) var<storage, read_write> data: array<f32>;
 
      @compute @workgroup_size(1) fn computeSomething(
        @builtin(global_invocation_id) id: vec3u
      ) {
        let i = id.x;
        data[i] = data[i] * 2.0;
      }
    `,
  })

  const pipeline = device.createComputePipeline({
    label: "doubling compute pipeline",
    layout: "auto",
    compute: {
      module,
    },
  })

  const input = new Float32Array([1, 3, 5])
  const workBuffer = device.createBuffer({
    label: "work buffer",
    size: input.byteLength,
    usage: GPUBufferUsage.STORAGE | GPUBufferUsage.COPY_SRC | GPUBufferUsage.COPY_DST,
  })
  device.queue.writeBuffer(workBuffer, 0, input)

  const resultBuffer = device.createBuffer({
    label: "result buffer",
    size: input.byteLength,
    usage: GPUBufferUsage.MAP_READ | GPUBufferUsage.COPY_DST,
  })

  const bindGroup = device.createBindGroup({
    label: "bindGroup for work buffer",
    layout: pipeline.getBindGroupLayout(0),
    entries: [{ binding: 0, resource: { buffer: workBuffer } }],
  })

  const encoder = device.createCommandEncoder({
    label: "doubling encoder",
  })
  const pass = encoder.beginComputePass({
    label: "doubling compute pass",
  })
  pass.setPipeline(pipeline)
  pass.setBindGroup(0, bindGroup)
  pass.dispatchWorkgroups(input.length)
  pass.end()

  encoder.copyBufferToBuffer(workBuffer, 0, resultBuffer, 0, resultBuffer.size)

  const commandBuffer = encoder.finish()
  device.queue.submit([commandBuffer])

  await resultBuffer.mapAsync(GPUMapMode.READ)
  const result = new Float32Array(resultBuffer.getMappedRange())

  console.log("input", input)
  console.log("result", result)

  resultBuffer.unmap()
}

export async function runExample3(webGpuCtx: GPUCanvasContext, device: GPUDevice) {
  const { canvas } = webGpuCtx
  const presentationFormat = navigator.gpu.getPreferredCanvasFormat()
  webGpuCtx.configure({
    device,
    format: presentationFormat,
  })

  const module = device.createShaderModule({
    code: /* wgsl */ `
      struct OurStruct {
        color: vec4f,
        scale: vec2f,
        offset: vec2f,
      };

      @group(0) @binding(0) var<uniform> ourStruct: OurStruct;

      @vertex fn vs(
        @builtin(vertex_index) vertexIndex : u32
      ) -> @builtin(position) vec4f {
        let pos = array(
          vec2f( 0.0,  0.5),  // top center
          vec2f(-0.5, -0.5),  // bottom left
          vec2f( 0.5, -0.5)   // bottom right
        );

        return vec4f(
          pos[vertexIndex] * ourStruct.scale + ourStruct.offset, 0.0, 1.0);
      }

      @fragment fn fs() -> @location(0) vec4f {
        return ourStruct.color;
      }
    `,
  })

  const pipeline = device.createRenderPipeline({
    label: "triangle with uniforms",
    layout: "auto",
    vertex: {
      module,
    },
    fragment: {
      module,
      targets: [{ format: presentationFormat }],
    },
  })

  const uniformBufferSize =
    4 * 4 + // color is 4 32bit floats (4bytes each)
    2 * 4 + // scale is 2 32bit floats (4bytes each)
    2 * 4 // offset is 2 32bit floats (4bytes each)
  const uniformBuffer = device.createBuffer({
    label: "uniforms for triangle",
    size: uniformBufferSize,
    usage: GPUBufferUsage.UNIFORM | GPUBufferUsage.COPY_DST,
  })

  const uniformValues = new Float32Array(uniformBufferSize / 4)

  const kColorOffset = 0
  const kScaleOffset = 4
  const kOffsetOffset = 6

  uniformValues.set([0, 1, 0, 1], kColorOffset)
  uniformValues.set([-0.5, -0.25], kOffsetOffset)

  const bindGroup = device.createBindGroup({
    label: "triangle bind group",
    layout: pipeline.getBindGroupLayout(0),
    entries: [{ binding: 0, resource: { buffer: uniformBuffer } }],
  })

  const renderPassDescriptor = {
    label: "our basic canvas renderPass",
    colorAttachments: [
      {
        clearValue: [0.3, 0.3, 0.3, 1],
        loadOp: "clear",
        storeOp: "store",
      },
    ] as unknown as GPURenderPassColorAttachment[],
  }

  function render() {
    const aspect = canvas.width / canvas.height
    uniformValues.set([0.5 / aspect, 0.5], kScaleOffset)

    device.queue.writeBuffer(uniformBuffer, 0, uniformValues)

    renderPassDescriptor.colorAttachments[0].view = webGpuCtx.getCurrentTexture().createView()

    const encoder = device.createCommandEncoder()
    const pass = encoder.beginRenderPass(renderPassDescriptor)
    pass.setPipeline(pipeline)
    pass.setBindGroup(0, bindGroup)
    pass.draw(3)
    pass.end()

    const commandBuffer = encoder.finish()
    device.queue.submit([commandBuffer])
  }

  setupObserver(device, canvas, render)
}

function setupObserver(device: GPUDevice, canvas: HTMLCanvasElement | OffscreenCanvas, render: () => void) {
  const observer = new ResizeObserver((entries) => {
    for (const entry of entries) {
      const canvas = entry.target as HTMLCanvasElement
      const width = entry.contentBoxSize[0].inlineSize
      const height = entry.contentBoxSize[0].blockSize
      canvas.width = Math.max(1, Math.min(width, device.limits.maxTextureDimension2D))
      canvas.height = Math.max(1, Math.min(height, device.limits.maxTextureDimension2D))
      // re-render
      render()
    }
  })
  observer.observe(canvas as Element)
}

const rand = (min?: number, max?: number) => {
  if (min === undefined) {
    min = 0
    max = 1
  } else if (max === undefined) {
    max = min
    min = 0
  }
  return min + Math.random() * (max - min)
}

interface ObjInfo {
  scale: number
  bindGroup: GPUBindGroup
  uniformBuffer: GPUBuffer
  uniformValues: Float32Array
}

export async function runExample4(webGpuCtx: GPUCanvasContext, device: GPUDevice) {
  const { canvas } = webGpuCtx
  const presentationFormat = navigator.gpu.getPreferredCanvasFormat()
  webGpuCtx.configure({
    device,
    format: presentationFormat,
  })

  const module = device.createShaderModule({
    code: /* wgsl */ `
      struct OurStruct {
        color: vec4f,
        scale: vec2f,
        offset: vec2f,
      };

      @group(0) @binding(0) var<uniform> ourStruct: OurStruct;

      @vertex fn vs(
        @builtin(vertex_index) vertexIndex : u32
      ) -> @builtin(position) vec4f {
        let pos = array(
          vec2f( 0.0,  0.5),  // top center
          vec2f(-0.5, -0.5),  // bottom left
          vec2f( 0.5, -0.5)   // bottom right
        );

        return vec4f(
          pos[vertexIndex] * ourStruct.scale + ourStruct.offset, 0.0, 1.0);
      }

      @fragment fn fs() -> @location(0) vec4f {
        return ourStruct.color;
      }
    `,
  })

  const pipeline = device.createRenderPipeline({
    label: "multiple uniforms",
    layout: "auto",
    vertex: {
      module,
    },
    fragment: {
      module,
      targets: [{ format: presentationFormat }],
    },
  })

  const uniformBufferSize = 4 * 4 + 2 * 4 + 2 * 4

  const kColorOffset = 0
  const kScaleOffset = 4
  const kOffsetOffset = 6

  const kNumObjects = 100
  const objectInfos: ObjInfo[] = []

  for (let i = 0; i < kNumObjects; i++) {
    const uniformBuffer = device.createBuffer({
      label: `uniforms for obj: ${i}`,
      size: uniformBufferSize,
      usage: GPUBufferUsage.UNIFORM | GPUBufferUsage.COPY_DST,
    })

    const uniformValues = new Float32Array(uniformBufferSize / 4)
    uniformValues.set([rand(), rand(), rand(), 1], kColorOffset)
    uniformValues.set([rand(-0.9, 0.9), rand(-0.9, 0.9)], kOffsetOffset)

    const bindGroup = device.createBindGroup({
      label: `bind group for obj: ${i}`,
      layout: pipeline.getBindGroupLayout(0),
      entries: [{ binding: 0, resource: { buffer: uniformBuffer } }],
    })

    objectInfos.push({
      scale: rand(0.2, 0.5),
      uniformBuffer,
      uniformValues,
      bindGroup,
    })
  }

  const renderPassDescriptor = {
    label: "our basic canvas renderPass",
    colorAttachments: [
      {
        clearValue: [0.3, 0.3, 0.3, 1],
        loadOp: "clear",
        storeOp: "store",
      },
    ] as unknown as GPURenderPassColorAttachment[],
  }

  function render() {
    renderPassDescriptor.colorAttachments[0].view = webGpuCtx.getCurrentTexture().createView()

    const encoder = device.createCommandEncoder()
    const pass = encoder.beginRenderPass(renderPassDescriptor)
    pass.setPipeline(pipeline)

    const aspect = canvas.width / canvas.height

    for (const { scale, bindGroup, uniformBuffer, uniformValues } of objectInfos) {
      uniformValues.set([scale / aspect, scale], kScaleOffset)
      device.queue.writeBuffer(uniformBuffer, 0, uniformValues)

      pass.setBindGroup(0, bindGroup)
      pass.draw(3)
    }
    pass.end()

    const commandBuffer = encoder.finish()
    device.queue.submit([commandBuffer])
  }

  setupObserver(device, canvas, render)
}

export async function runExample5(webGpuCtx: GPUCanvasContext, device: GPUDevice) {
  const { canvas } = webGpuCtx
  const presentationFormat = navigator.gpu.getPreferredCanvasFormat()
  webGpuCtx.configure({
    device,
    format: presentationFormat,
  })

  const module = device.createShaderModule({
    code: /* wgsl */ `
      struct OurStruct {
        color: vec4f,
        offset: vec2f,
      };

      struct OtherStruct {
        scale: vec2f,
      };

      @group(0) @binding(0) var<uniform> ourStruct: OurStruct;
      @group(0) @binding(1) var<uniform> otherStruct: OtherStruct;

      @vertex fn vs(
        @builtin(vertex_index) vertexIndex : u32
      ) -> @builtin(position) vec4f {
        let pos = array(
          vec2f( 0.0,  0.5),  // top center
          vec2f(-0.5, -0.5),  // bottom left
          vec2f( 0.5, -0.5)   // bottom right
        );

        return vec4f(
          pos[vertexIndex] * otherStruct.scale + ourStruct.offset, 0.0, 1.0);
      }

      @fragment fn fs() -> @location(0) vec4f {
        return ourStruct.color;
      }
    `,
  })

  const pipeline = device.createRenderPipeline({
    label: "multiple uniform buffer",
    layout: "auto",
    vertex: {
      module,
    },
    fragment: {
      module,
      targets: [{ format: presentationFormat }],
    },
  })

  const staticUniformBufferSize =
    4 * 4 + // color is 4 32bit floats (4bytes each)
    2 * 4 + // offset is 2 32bit floats (4bytes each)
    2 * 4 // padding
  const uniformBufferSize = 2 * 4 // scale is 2 32bit floats (4bytes each)

  const kColorOffset = 0
  const kOffsetOffset = 4

  const kScaleOffset = 0

  const kNumObjects = 100
  const objectInfos: ObjInfo[] = []

  for (let i = 0; i < kNumObjects; i++) {
    const staticUniformBuffer = device.createBuffer({
      label: `static uniforms for obj: ${i}`,
      size: staticUniformBufferSize,
      usage: GPUBufferUsage.UNIFORM | GPUBufferUsage.COPY_DST,
    })

    {
      const uniformValues = new Float32Array(staticUniformBufferSize / 4)
      uniformValues.set([rand(), rand(), rand(), 1], kColorOffset)
      uniformValues.set([rand(-0.9, 0.9), rand(-0.9, 0.9)], kOffsetOffset)

      device.queue.writeBuffer(staticUniformBuffer, 0, uniformValues)
    }

    const uniformValues = new Float32Array(uniformBufferSize / 4)
    const uniformBuffer = device.createBuffer({
      label: `changing uniforms for obj: ${i}`,
      size: uniformBufferSize,
      usage: GPUBufferUsage.UNIFORM | GPUBufferUsage.COPY_DST,
    })

    const bindGroup = device.createBindGroup({
      label: `bind group for obj: ${i}`,
      layout: pipeline.getBindGroupLayout(0),
      entries: [
        { binding: 0, resource: { buffer: staticUniformBuffer } },
        { binding: 1, resource: { buffer: uniformBuffer } },
      ],
    })

    objectInfos.push({
      scale: rand(0.2, 0.5),
      uniformBuffer,
      uniformValues,
      bindGroup,
    })
  }

  const renderPassDescriptor = {
    label: "our basic canvas renderPass",
    colorAttachments: [
      {
        clearValue: [0.3, 0.3, 0.3, 1],
        loadOp: "clear",
        storeOp: "store",
      },
    ] as unknown as GPURenderPassColorAttachment[],
  }

  function render() {
    renderPassDescriptor.colorAttachments[0].view = webGpuCtx.getCurrentTexture().createView()

    const encoder = device.createCommandEncoder()
    const pass = encoder.beginRenderPass(renderPassDescriptor)
    pass.setPipeline(pipeline)

    const aspect = canvas.width / canvas.height

    for (const { scale, bindGroup, uniformBuffer, uniformValues } of objectInfos) {
      uniformValues.set([scale / aspect, scale], kScaleOffset)
      device.queue.writeBuffer(uniformBuffer, 0, uniformValues)

      pass.setBindGroup(0, bindGroup)
      pass.draw(3)
    }
    pass.end()

    const commandBuffer = encoder.finish()
    device.queue.submit([commandBuffer])
  }

  setupObserver(device, canvas, render)
}
