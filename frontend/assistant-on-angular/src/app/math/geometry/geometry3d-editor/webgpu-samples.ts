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
  bindGroup?: GPUBindGroup
  uniformBuffer?: GPUBuffer
  uniformValues?: Float32Array
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
      uniformValues!.set([scale / aspect, scale], kScaleOffset)
      device.queue.writeBuffer(uniformBuffer!, 0, uniformValues!)

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
      uniformValues!.set([scale / aspect, scale], kScaleOffset)
      device.queue.writeBuffer(uniformBuffer!, 0, uniformValues!)

      pass.setBindGroup(0, bindGroup)
      pass.draw(3)
    }
    pass.end()

    const commandBuffer = encoder.finish()
    device.queue.submit([commandBuffer])
  }

  setupObserver(device, canvas, render)
}

export async function runExample6(webGpuCtx: GPUCanvasContext, device: GPUDevice) {
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

      @group(0) @binding(0) var<storage, read> ourStruct: OurStruct;
      @group(0) @binding(1) var<storage, read> otherStruct: OtherStruct;

      @vertex fn vs(
        @builtin(vertex_index) vertexIndex : u32
      ) -> @builtin(position) vec4f {
        let pos = array(
          vec2f( 0.0,  0.5), // top center
          vec2f(-0.5, -0.5), // bottom left
          vec2f( 0.5, -0.5)  // bottom right
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
    label: "storage buffer like uniform",
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
      usage: GPUBufferUsage.STORAGE | GPUBufferUsage.COPY_DST,
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
      usage: GPUBufferUsage.STORAGE | GPUBufferUsage.COPY_DST,
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
      uniformValues!.set([scale / aspect, scale], kScaleOffset)
      device.queue.writeBuffer(uniformBuffer!, 0, uniformValues!)

      pass.setBindGroup(0, bindGroup)
      pass.draw(3)
    }

    pass.end()

    const commandBuffer = encoder.finish()
    device.queue.submit([commandBuffer])
  }

  setupObserver(device, canvas, render)
}

export async function runExample7(webGpuCtx: GPUCanvasContext, device: GPUDevice) {
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

      struct VSOutput {
        @builtin(position) position: vec4f,
        @location(0) color: vec4f,
      }

      @group(0) @binding(0) var<storage, read> ourStructs: array<OurStruct>;
      @group(0) @binding(1) var<storage, read> otherStructs: array<OtherStruct>;

      @vertex fn vs(
        @builtin(vertex_index) vertexIndex : u32,
        @builtin(instance_index) instanceIndex: u32
      ) -> VSOutput {
        let pos = array(
          vec2f( 0.0,  0.5),  // top center
          vec2f(-0.5, -0.5),  // bottom left
          vec2f( 0.5, -0.5)   // bottom right
        );

        let otherStruct = otherStructs[instanceIndex];
        let ourStruct = ourStructs[instanceIndex];

        var vsOut: VSOutput;
        vsOut.position = vec4f(
            pos[vertexIndex] * otherStruct.scale + ourStruct.offset, 0.0, 1.0);
        vsOut.color = ourStruct.color;
        return vsOut;
      }

      @fragment fn fs(vsOut: VSOutput) -> @location(0) vec4f {
        return vsOut.color;
      }
    `,
  })

  const pipeline = device.createRenderPipeline({
    label: "split storage buffer pipeline",
    layout: "auto",
    vertex: {
      module,
    },
    fragment: {
      module,
      targets: [{ format: presentationFormat }],
    },
  })

  const kNumObjects = 100

  const staticStorageUnitSize =
    4 * 4 + // color is 4 32bit floats (4bytes each)
    2 * 4 + // offset is 2 32bit floats (4bytes each)
    2 * 4 // padding
  const storageUnitSize = 2 * 4 // scale is 2 32bit floats (4bytes each)
  const staticStorageBufferSize = staticStorageUnitSize * kNumObjects
  const storageBufferSize = storageUnitSize * kNumObjects

  const staticStorageBuffer = device.createBuffer({
    label: "static storage for objects",
    size: staticStorageBufferSize,
    usage: GPUBufferUsage.STORAGE | GPUBufferUsage.COPY_DST,
  })

  const storageBuffer = device.createBuffer({
    label: "changing storage for objects",
    size: storageBufferSize,
    usage: GPUBufferUsage.STORAGE | GPUBufferUsage.COPY_DST,
  })

  const staticStorageValues = new Float32Array(staticStorageBufferSize / 4)
  const storageValues = new Float32Array(storageBufferSize / 4)

  const kColorOffset = 0
  const kOffsetOffset = 4

  const kScaleOffset = 0

  const objectInfos: ObjInfo[] = []

  for (let i = 0; i < kNumObjects; i++) {
    const staticOffset = i * (staticStorageUnitSize / 4)

    staticStorageValues.set([rand(), rand(), rand(), 1], staticOffset + kColorOffset)
    staticStorageValues.set([rand(-0.9, 0.9), rand(-0.9, 0.9)], staticOffset + kOffsetOffset)

    objectInfos.push({
      scale: rand(0.2, 0.5),
    })
  }
  device.queue.writeBuffer(staticStorageBuffer, 0, staticStorageValues)

  const bindGroup = device.createBindGroup({
    label: "bind group for objects",
    layout: pipeline.getBindGroupLayout(0),
    entries: [
      { binding: 0, resource: { buffer: staticStorageBuffer } },
      { binding: 1, resource: { buffer: storageBuffer } },
    ],
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
    renderPassDescriptor.colorAttachments[0].view = webGpuCtx.getCurrentTexture().createView()

    const encoder = device.createCommandEncoder()
    const pass = encoder.beginRenderPass(renderPassDescriptor)
    pass.setPipeline(pipeline)

    const aspect = canvas.width / canvas.height

    objectInfos.forEach(({ scale }, ndx) => {
      const offset = ndx * (storageUnitSize / 4)
      storageValues.set([scale / aspect, scale], offset + kScaleOffset)
    })
    device.queue.writeBuffer(storageBuffer, 0, storageValues)

    pass.setBindGroup(0, bindGroup)
    pass.draw(3, kNumObjects)

    pass.end()

    const commandBuffer = encoder.finish()
    device.queue.submit([commandBuffer])
  }

  setupObserver(device, canvas, render)
}

function createCircleVertices({
  radius = 1,
  numSubdivisions = 24,
  innerRadius = 0,
  startAngle = 0,
  endAngle = Math.PI * 2,
} = {}) {
  // 2 triangles per subdivision, 3 verts per tri, 2 values (xy) each.
  const numVertices = numSubdivisions * 3 * 2
  const vertexData = new Float32Array(numSubdivisions * 2 * 3 * 2)

  let offset = 0
  const addVertex = (x: number, y: number) => {
    vertexData[offset++] = x
    vertexData[offset++] = y
  }

  // 2 vertices per subdivision
  //
  // 0--1 4
  // | / /|
  // |/ / |
  // 2 3--5
  for (let i = 0; i < numSubdivisions; i++) {
    const angle1 = startAngle + (i * (endAngle - startAngle)) / numSubdivisions
    const angle2 = startAngle + ((i + 1) * (endAngle - startAngle)) / numSubdivisions

    const c1 = Math.cos(angle1)
    const s1 = Math.sin(angle1)
    const c2 = Math.cos(angle2)
    const s2 = Math.sin(angle2)

    // first triangle
    addVertex(c1 * radius, s1 * radius)
    addVertex(c2 * radius, s2 * radius)
    addVertex(c1 * innerRadius, s1 * innerRadius)

    // second triangle
    addVertex(c1 * innerRadius, s1 * innerRadius)
    addVertex(c2 * radius, s2 * radius)
    addVertex(c2 * innerRadius, s2 * innerRadius)
  }

  return {
    vertexData,
    numVertices,
  }
}

function createCircleInnerOuterVertices({
  radius = 1,
  numSubdivisions = 24,
  innerRadius = 0,
  startAngle = 0,
  endAngle = Math.PI * 2,
} = {}) {
  // 2 triangles per subdivision, 3 verts per tri, 5 values (xyrgb) each.
  const numVertices = numSubdivisions * 3 * 2
  const vertexData = new Float32Array(numVertices * (2 + 3))

  let offset = 0
  const addVertex = (x: number, y: number, r: number, g: number, b: number) => {
    vertexData[offset++] = x
    vertexData[offset++] = y
    vertexData[offset++] = r
    vertexData[offset++] = g
    vertexData[offset++] = b
  }

  const innerColor: [number, number, number] = [1, 1, 1]
  const outerColor: [number, number, number] = [0.1, 0.1, 0.1]

  // 2 vertices per subdivision
  //
  // 0--1 4
  // | / /|
  // |/ / |
  // 2 3--5
  for (let i = 0; i < numSubdivisions; i++) {
    const angle1 = startAngle + (i * (endAngle - startAngle)) / numSubdivisions
    const angle2 = startAngle + ((i + 1) * (endAngle - startAngle)) / numSubdivisions

    const c1 = Math.cos(angle1)
    const s1 = Math.sin(angle1)
    const c2 = Math.cos(angle2)
    const s2 = Math.sin(angle2)

    // first triangle
    addVertex(c1 * radius, s1 * radius, ...outerColor)
    addVertex(c2 * radius, s2 * radius, ...outerColor)
    addVertex(c1 * innerRadius, s1 * innerRadius, ...innerColor)

    // second triangle
    addVertex(c1 * innerRadius, s1 * innerRadius, ...innerColor)
    addVertex(c2 * radius, s2 * radius, ...outerColor)
    addVertex(c2 * innerRadius, s2 * innerRadius, ...innerColor)
  }

  return {
    vertexData,
    numVertices,
  }
}

function createCircleWithNormalizedRGBAValues({
  radius = 1,
  numSubdivisions = 24,
  innerRadius = 0,
  startAngle = 0,
  endAngle = Math.PI * 2,
} = {}) {
  // 2 triangles per subdivision, 3 verts per tri
  const numVertices = numSubdivisions * 3 * 2
  // 2 32-bit values for position (xy) and 1 32-bit value for color (rgb_)
  // The 32-bit color value will be written/read as 4 8-bit values
  const vertexData = new Float32Array(numVertices * (2 + 1))
  const colorData = new Uint8Array(vertexData.buffer)

  let offset = 0
  let colorOffset = 8
  const addVertex = (x: number, y: number, r: number, g: number, b: number) => {
    vertexData[offset++] = x
    vertexData[offset++] = y
    offset += 1 // skip the color
    colorData[colorOffset++] = r * 255
    colorData[colorOffset++] = g * 255
    colorData[colorOffset++] = b * 255
    colorOffset += 9 // skip extra byte and the position
  }

  const innerColor: [number, number, number] = [1, 1, 1]
  const outerColor: [number, number, number] = [0.1, 0.1, 0.1]

  // 2 vertices per subdivision
  //
  // 0--1 4
  // | / /|
  // |/ / |
  // 2 3--5
  for (let i = 0; i < numSubdivisions; i++) {
    const angle1 = startAngle + (i * (endAngle - startAngle)) / numSubdivisions
    const angle2 = startAngle + ((i + 1) * (endAngle - startAngle)) / numSubdivisions

    const c1 = Math.cos(angle1)
    const s1 = Math.sin(angle1)
    const c2 = Math.cos(angle2)
    const s2 = Math.sin(angle2)

    // first triangle
    addVertex(c1 * radius, s1 * radius, ...outerColor)
    addVertex(c2 * radius, s2 * radius, ...outerColor)
    addVertex(c1 * innerRadius, s1 * innerRadius, ...innerColor)

    // second triangle
    addVertex(c1 * innerRadius, s1 * innerRadius, ...innerColor)
    addVertex(c2 * radius, s2 * radius, ...outerColor)
    addVertex(c2 * innerRadius, s2 * innerRadius, ...innerColor)
  }

  return {
    vertexData,
    numVertices,
  }
}

function createCircleVerticesWithIndices({
  radius = 1,
  numSubdivisions = 24,
  innerRadius = 0,
  startAngle = 0,
  endAngle = Math.PI * 2,
} = {}) {
  const numVertices = (numSubdivisions + 1) * 2
  const vertexData = new Float32Array(numVertices * (2 + 1))
  const colorData = new Uint8Array(vertexData.buffer)

  let offset = 0
  let colorOffset = 8
  const addVertex = (x: number, y: number, r: number, g: number, b: number) => {
    vertexData[offset++] = x
    vertexData[offset++] = y
    offset += 1 // skip the color
    colorData[colorOffset++] = r * 255
    colorData[colorOffset++] = g * 255
    colorData[colorOffset++] = b * 255
    colorOffset += 9 // skip extra byte and the position
  }

  const innerColor: [number, number, number] = [1, 1, 1]
  const outerColor: [number, number, number] = [0.1, 0.1, 0.1]

  // 2 vertices per subdivision
  //
  // 1  3  5  7  9 ...
  //
  // 0  2  4  6  8 ...
  for (let i = 0; i <= numSubdivisions; ++i) {
    const angle = startAngle + (i * (endAngle - startAngle)) / numSubdivisions

    const c1 = Math.cos(angle)
    const s1 = Math.sin(angle)

    addVertex(c1 * radius, s1 * radius, ...outerColor)
    addVertex(c1 * innerRadius, s1 * innerRadius, ...innerColor)
  }

  const indexData = new Uint32Array(numSubdivisions * 6)
  let ndx = 0

  // 1st tri  2nd tri  3rd tri  4th tri
  // 0 1 2    2 1 3    2 3 4    4 3 5
  //
  // 0--2        2     2--4        4  .....
  // | /        /|     | /        /|
  // |/        / |     |/        / |
  // 1        1--3     3        3--5  .....
  for (let i = 0; i < numSubdivisions; ++i) {
    const ndxOffset = i * 2

    // first triangle
    indexData[ndx++] = ndxOffset
    indexData[ndx++] = ndxOffset + 1
    indexData[ndx++] = ndxOffset + 2

    // second triangle
    indexData[ndx++] = ndxOffset + 2
    indexData[ndx++] = ndxOffset + 1
    indexData[ndx++] = ndxOffset + 3
  }

  return {
    vertexData,
    indexData,
    numVertices: indexData.length,
  }
}

export async function runExample8(webGpuCtx: GPUCanvasContext, device: GPUDevice) {
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

      struct Vertex {
        position: vec2f,
      };

      struct VSOutput {
        @builtin(position) position: vec4f,
        @location(0) color: vec4f,
      };

      @group(0) @binding(0) var<storage, read> ourStructs: array<OurStruct>;
      @group(0) @binding(1) var<storage, read> otherStructs: array<OtherStruct>;
      @group(0) @binding(2) var<storage, read> pos: array<Vertex>;

      @vertex fn vs(
        @builtin(vertex_index) vertexIndex : u32,
        @builtin(instance_index) instanceIndex: u32
      ) -> VSOutput {
        let otherStruct = otherStructs[instanceIndex];
        let ourStruct = ourStructs[instanceIndex];

        var vsOut: VSOutput;
        vsOut.position = vec4f(
            pos[vertexIndex].position * otherStruct.scale + ourStruct.offset, 0.0, 1.0);
        vsOut.color = ourStruct.color;
        return vsOut;
      }

      @fragment fn fs(vsOut: VSOutput) -> @location(0) vec4f {
        return vsOut.color;
      }
    `,
  })

  const pipeline = device.createRenderPipeline({
    label: "storage buffer vertices",
    layout: "auto",
    vertex: {
      module,
    },
    fragment: {
      module,
      targets: [{ format: presentationFormat }],
    },
  })

  const kNumObjects = 100
  const objectInfos: ObjInfo[] = []

  const staticUnitSize =
    4 * 4 + // color is 4 32bit floats (4bytes each)
    2 * 4 + // offset is 2 32bit floats (4bytes each)
    2 * 4 // padding
  const changingUnitSize = 2 * 4
  const staticStorageBufferSize = staticUnitSize * kNumObjects
  const changingStorageBufferSize = changingUnitSize * kNumObjects

  const staticStorageBuffer = device.createBuffer({
    label: "static storage for objects",
    size: staticStorageBufferSize,
    usage: GPUBufferUsage.STORAGE | GPUBufferUsage.COPY_DST,
  })

  const changingStorageBuffer = device.createBuffer({
    label: "changing storage for objects",
    size: changingStorageBufferSize,
    usage: GPUBufferUsage.STORAGE | GPUBufferUsage.COPY_DST,
  })

  const kColorOffset = 0
  const kOffsetOffset = 4

  const kScaleOffset = 0

  {
    const staticStorageValues = new Float32Array(staticStorageBufferSize / 4)
    for (let i = 0; i < kNumObjects; i++) {
      const staticOffset = i * (staticUnitSize / 4)

      staticStorageValues.set([rand(), rand(), rand(), 1], staticOffset + kColorOffset)
      staticStorageValues.set([rand(-0.9, 0.9), rand(-0.9, 0.9)], staticOffset + kOffsetOffset)

      objectInfos.push({
        scale: rand(0.2, 0.5),
      })
    }
    device.queue.writeBuffer(staticStorageBuffer, 0, staticStorageValues)
  }

  const storageValues = new Float32Array(changingStorageBufferSize / 4)

  const { vertexData, numVertices } = createCircleVertices({
    radius: 0.5,
    innerRadius: 0.25,
  })
  const vertexStorageBuffer = device.createBuffer({
    label: "storage buffer vertices",
    size: vertexData.byteLength,
    usage: GPUBufferUsage.STORAGE | GPUBufferUsage.COPY_DST,
  })
  device.queue.writeBuffer(vertexStorageBuffer, 0, vertexData)

  const bindGroup = device.createBindGroup({
    label: "bind group for objects",
    layout: pipeline.getBindGroupLayout(0),
    entries: [
      { binding: 0, resource: { buffer: staticStorageBuffer } },
      { binding: 1, resource: { buffer: changingStorageBuffer } },
      { binding: 2, resource: { buffer: vertexStorageBuffer } },
    ],
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
    renderPassDescriptor.colorAttachments[0].view = webGpuCtx.getCurrentTexture().createView()

    const encoder = device.createCommandEncoder()
    const pass = encoder.beginRenderPass(renderPassDescriptor)
    pass.setPipeline(pipeline)

    const aspect = canvas.width / canvas.height

    objectInfos.forEach(({ scale }, ndx) => {
      const offset = ndx * (changingUnitSize / 4)
      storageValues.set([scale / aspect, scale], offset + kScaleOffset)
    })
    device.queue.writeBuffer(changingStorageBuffer, 0, storageValues)

    pass.setBindGroup(0, bindGroup)
    pass.draw(numVertices, kNumObjects)

    pass.end()

    const commandBuffer = encoder.finish()
    device.queue.submit([commandBuffer])
  }

  setupObserver(device, canvas, render)
}

export async function runExample9(webGpuCtx: GPUCanvasContext, device: GPUDevice) {
  const { canvas } = webGpuCtx
  const presentationFormat = navigator.gpu.getPreferredCanvasFormat()
  webGpuCtx.configure({
    device,
    format: presentationFormat,
  })

  const module = device.createShaderModule({
    code: /* wgsl */ `
      struct Vertex {
        @location(0) position: vec2f,
        @location(1) color: vec4f,
        @location(2) offset: vec2f,
        @location(3) scale: vec2f,
      };

      struct VSOutput {
        @builtin(position) position: vec4f,
        @location(0) color: vec4f,
      };

      @vertex fn vs(
        vert: Vertex,
      ) -> VSOutput {
        var vsOut: VSOutput;
        vsOut.position = vec4f(
            vert.position * vert.scale + vert.offset, 0.0, 1.0);
        vsOut.color = vert.color;
        return vsOut;
      }

      @fragment fn fs(vsOut: VSOutput) -> @location(0) vec4f {
        return vsOut.color;
      }
    `,
  })

  const pipeline = device.createRenderPipeline({
    label: "flat colors",
    layout: "auto",
    vertex: {
      module,
      buffers: [
        {
          arrayStride: 2 * 4, // 2 floats, 4 bytes each
          attributes: [
            { shaderLocation: 0, offset: 0, format: "float32x2" }, // position
          ],
        },
        {
          arrayStride: 6 * 4, // 6 floats, 4 bytes each
          stepMode: "instance",
          attributes: [
            { shaderLocation: 1, offset: 0, format: "float32x4" }, // color
            { shaderLocation: 2, offset: 16, format: "float32x2" }, // offset
          ],
        },
        {
          arrayStride: 2 * 4, // 2 floats, 4 bytes each
          stepMode: "instance",
          attributes: [
            { shaderLocation: 3, offset: 0, format: "float32x2" }, // scale
          ],
        },
      ],
    },
    fragment: {
      module,
      targets: [{ format: presentationFormat }],
    },
  })

  const kNumObjects = 100
  const objectInfos: ObjInfo[] = []

  const staticUnitSize =
    4 * 4 + // color is 4 32bit floats (4bytes each)
    2 * 4 // offset is 2 32bit floats (4bytes each)
  const changingUnitSize = 2 * 4 // scale is 2 32bit floats (4bytes each)
  const staticVertexBufferSize = staticUnitSize * kNumObjects
  const changingVertexBufferSize = changingUnitSize * kNumObjects

  const staticVertexBuffer = device.createBuffer({
    label: "static vertex for objects",
    size: staticVertexBufferSize,
    usage: GPUBufferUsage.VERTEX | GPUBufferUsage.COPY_DST,
  })

  const changingVertexBuffer = device.createBuffer({
    label: "changing vertex for objects",
    size: changingVertexBufferSize,
    usage: GPUBufferUsage.VERTEX | GPUBufferUsage.COPY_DST,
  })

  const kColorOffset = 0
  const kOffsetOffset = 4

  const kScaleOffset = 0

  {
    const staticVertexValues = new Float32Array(staticVertexBufferSize / 4)
    for (let i = 0; i < kNumObjects; i++) {
      const staticOffset = i * (staticUnitSize / 4)

      staticVertexValues.set([rand(), rand(), rand(), 1], staticOffset + kColorOffset)
      staticVertexValues.set([rand(-0.9, 0.9), rand(-0.9, 0.9)], staticOffset + kOffsetOffset)

      objectInfos.push({
        scale: rand(0.2, 0.5),
      })
    }
    device.queue.writeBuffer(staticVertexBuffer, 0, staticVertexValues)
  }

  const vertexValues = new Float32Array(changingVertexBufferSize / 4)

  const { vertexData, numVertices } = createCircleVertices({
    radius: 0.5,
    innerRadius: 0.25,
  })
  const vertexBuffer = device.createBuffer({
    label: "vertex buffer vertices",
    size: vertexData.byteLength,
    usage: GPUBufferUsage.VERTEX | GPUBufferUsage.COPY_DST,
  })
  device.queue.writeBuffer(vertexBuffer, 0, vertexData)

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
    pass.setVertexBuffer(0, vertexBuffer)
    pass.setVertexBuffer(1, staticVertexBuffer)
    pass.setVertexBuffer(2, changingVertexBuffer)

    const aspect = canvas.width / canvas.height

    objectInfos.forEach(({ scale }, ndx) => {
      const offset = ndx * (changingUnitSize / 4)
      vertexValues.set([scale / aspect, scale], offset + kScaleOffset)
    })
    device.queue.writeBuffer(changingVertexBuffer, 0, vertexValues)

    pass.draw(numVertices, kNumObjects)

    pass.end()

    const commandBuffer = encoder.finish()
    device.queue.submit([commandBuffer])
  }

  setupObserver(device, canvas, render)
}

export async function runExample10(webGpuCtx: GPUCanvasContext, device: GPUDevice) {
  const { canvas } = webGpuCtx
  const presentationFormat = navigator.gpu.getPreferredCanvasFormat()
  webGpuCtx.configure({
    device,
    format: presentationFormat,
  })

  const module = device.createShaderModule({
    code: /* wgsl */ `
      struct Vertex {
        @location(0) position: vec2f,
        @location(1) color: vec4f,
        @location(2) offset: vec2f,
        @location(3) scale: vec2f,
        @location(4) perVertexColor: vec4f,
      };

      struct VSOutput {
        @builtin(position) position: vec4f,
        @location(0) color: vec4f,
      };

      @vertex fn vs(
        vert: Vertex,
      ) -> VSOutput {
        var vsOut: VSOutput;
        vsOut.position = vec4f(
            vert.position * vert.scale + vert.offset, 0.0, 1.0);
        vsOut.color = vert.color * vert.perVertexColor;
        return vsOut;
      }

      @fragment fn fs(vsOut: VSOutput) -> @location(0) vec4f {
        return vsOut.color;
      }
    `,
  })

  const pipeline = device.createRenderPipeline({
    label: "per vertex color",
    layout: "auto",
    vertex: {
      module,
      buffers: [
        {
          arrayStride: 5 * 4, // 5 floats, 4 bytes each
          attributes: [
            { shaderLocation: 0, offset: 0, format: "float32x2" }, // position
            { shaderLocation: 4, offset: 8, format: "float32x3" }, // perVertexColor
          ],
        },
        {
          arrayStride: 6 * 4, // 6 floats, 4 bytes each
          stepMode: "instance",
          attributes: [
            { shaderLocation: 1, offset: 0, format: "float32x4" }, // color
            { shaderLocation: 2, offset: 16, format: "float32x2" }, // offset
          ],
        },
        {
          arrayStride: 2 * 4, // 2 floats, 4 bytes each
          stepMode: "instance",
          attributes: [
            { shaderLocation: 3, offset: 0, format: "float32x2" }, // scale
          ],
        },
      ],
    },
    fragment: {
      module,
      targets: [{ format: presentationFormat }],
    },
  })

  const kNumObjects = 100
  const objectInfos: ObjInfo[] = []

  const staticUnitSize =
    4 * 4 + // color is 4 32bit floats (4bytes each)
    2 * 4 // offset is 2 32bit floats (4bytes each)
  const changingUnitSize = 2 * 4 // scale is 2 32bit floats (4bytes each)
  const staticVertexBufferSize = staticUnitSize * kNumObjects
  const changingVertexBufferSize = changingUnitSize * kNumObjects

  const staticVertexBuffer = device.createBuffer({
    label: "static vertex for objects",
    size: staticVertexBufferSize,
    usage: GPUBufferUsage.VERTEX | GPUBufferUsage.COPY_DST,
  })

  const changingVertexBuffer = device.createBuffer({
    label: "changing vertex for objects",
    size: changingVertexBufferSize,
    usage: GPUBufferUsage.VERTEX | GPUBufferUsage.COPY_DST,
  })

  const kColorOffset = 0
  const kOffsetOffset = 4

  const kScaleOffset = 0

  {
    const staticVertexValues = new Float32Array(staticVertexBufferSize / 4)
    for (let i = 0; i < kNumObjects; ++i) {
      const staticOffset = i * (staticUnitSize / 4)

      staticVertexValues.set([rand(), rand(), rand(), 1], staticOffset + kColorOffset)
      staticVertexValues.set([rand(-0.9, 0.9), rand(-0.9, 0.9)], staticOffset + kOffsetOffset)

      objectInfos.push({
        scale: rand(0.2, 0.5),
      })
    }
    device.queue.writeBuffer(staticVertexBuffer, 0, staticVertexValues)
  }

  const vertexValues = new Float32Array(changingVertexBufferSize / 4)

  const { vertexData, numVertices } = createCircleInnerOuterVertices({
    radius: 0.5,
    innerRadius: 0.25,
  })
  const vertexBuffer = device.createBuffer({
    label: "vertex buffer vertices",
    size: vertexData.byteLength,
    usage: GPUBufferUsage.VERTEX | GPUBufferUsage.COPY_DST,
  })
  device.queue.writeBuffer(vertexBuffer, 0, vertexData)

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
    pass.setVertexBuffer(0, vertexBuffer)
    pass.setVertexBuffer(1, staticVertexBuffer)
    pass.setVertexBuffer(2, changingVertexBuffer)

    const aspect = canvas.width / canvas.height

    objectInfos.forEach(({ scale }, ndx) => {
      const offset = ndx * (changingUnitSize / 4)
      vertexValues.set([scale / aspect, scale], offset + kScaleOffset)
    })
    device.queue.writeBuffer(changingVertexBuffer, 0, vertexValues)

    pass.draw(numVertices, kNumObjects)

    pass.end()

    const commandBuffer = encoder.finish()
    device.queue.submit([commandBuffer])
  }

  setupObserver(device, canvas, render)
}

export async function runExample11(webGpuCtx: GPUCanvasContext, device: GPUDevice) {
  const { canvas } = webGpuCtx
  const presentationFormat = navigator.gpu.getPreferredCanvasFormat()
  webGpuCtx.configure({
    device,
    format: presentationFormat,
  })

  const module = device.createShaderModule({
    code: /* wgsl */ `
      struct Vertex {
        @location(0) position: vec2f,
        @location(1) color: vec4f,
        @location(2) offset: vec2f,
        @location(3) scale: vec2f,
        @location(4) perVertexColor: vec3f,
      };

      struct VSOutput {
        @builtin(position) position: vec4f,
        @location(0) color: vec4f,
      };

      @vertex fn vs(
        vert: Vertex,
      ) -> VSOutput {
        var vsOut: VSOutput;
        vsOut.position = vec4f(
            vert.position * vert.scale + vert.offset, 0.0, 1.0);
        vsOut.color = vert.color * vec4f(vert.perVertexColor, 1);
        return vsOut;
      }

      @fragment fn fs(vsOut: VSOutput) -> @location(0) vec4f {
        return vsOut.color;
      }
    `,
  })

  const pipeline = device.createRenderPipeline({
    label: "per vertex color",
    layout: "auto",
    vertex: {
      module,
      buffers: [
        {
          arrayStride: 2 * 4 + 4, // 2 floats, 4 bytes each + 4 bytes
          attributes: [
            { shaderLocation: 0, offset: 0, format: "float32x2" }, // position
            { shaderLocation: 4, offset: 8, format: "unorm8x4" }, // perVertexColor
          ],
        },
        {
          arrayStride: 4 + 2 * 4, // 4 bytes + 2 floats, 4 bytes each
          stepMode: "instance",
          attributes: [
            { shaderLocation: 1, offset: 0, format: "unorm8x4" }, // color
            { shaderLocation: 2, offset: 4, format: "float32x2" }, // offset
          ],
        },
        {
          arrayStride: 2 * 4, // 2 floats, 4 bytes each
          stepMode: "instance",
          attributes: [
            { shaderLocation: 3, offset: 0, format: "float32x2" }, // scale
          ],
        },
      ],
    },
    fragment: {
      module,
      targets: [{ format: presentationFormat }],
    },
  })

  const kNumObjects = 100
  const objectInfos: ObjInfo[] = []

  const staticUnitSize =
    4 + // color is 4 bytes
    2 * 4 // offset is 2 32bit floats (4bytes each)
  const changingUnitSize = 2 * 4 // scale is 2 32bit floats (4bytes each)
  const staticVertexBufferSize = staticUnitSize * kNumObjects
  const changingVertexBufferSize = changingUnitSize * kNumObjects

  const staticVertexBuffer = device.createBuffer({
    label: "static vertex for objects",
    size: staticVertexBufferSize,
    usage: GPUBufferUsage.VERTEX | GPUBufferUsage.COPY_DST,
  })

  const changingVertexBuffer = device.createBuffer({
    label: "changing storage for objects",
    size: changingVertexBufferSize,
    usage: GPUBufferUsage.VERTEX | GPUBufferUsage.COPY_DST,
  })

  const kColorOffset = 0
  const kOffsetOffset = 1

  const kScaleOffset = 0

  {
    const staticVertexValuesU8 = new Uint8Array(staticVertexBufferSize)
    const staticVertexValuesF32 = new Float32Array(staticVertexValuesU8.buffer)
    for (let i = 0; i < kNumObjects; i++) {
      const staticOffsetU8 = i * staticUnitSize
      const staticOffsetF32 = staticOffsetU8 / 4

      staticVertexValuesU8.set([rand() * 255, rand() * 255, rand() * 255, 255], staticOffsetU8 + kColorOffset)
      staticVertexValuesF32.set([rand(-0.9, 0.9), rand(-0.9, 0.9)], staticOffsetF32 + kOffsetOffset)

      objectInfos.push({
        scale: rand(0.2, 0.5),
      })
    }
    device.queue.writeBuffer(staticVertexBuffer, 0, staticVertexValuesF32)
  }

  const vertexValues = new Float32Array(changingVertexBufferSize / 4)

  const { vertexData, numVertices } = createCircleWithNormalizedRGBAValues({
    radius: 0.5,
    innerRadius: 0.25,
  })
  const vertexBuffer = device.createBuffer({
    label: "vertex buffer vertices",
    size: vertexData.byteLength,
    usage: GPUBufferUsage.VERTEX | GPUBufferUsage.COPY_DST,
  })
  device.queue.writeBuffer(vertexBuffer, 0, vertexData)

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
    pass.setVertexBuffer(0, vertexBuffer)
    pass.setVertexBuffer(1, staticVertexBuffer)
    pass.setVertexBuffer(2, changingVertexBuffer)

    const aspect = canvas.width / canvas.height

    objectInfos.forEach(({ scale }, ndx) => {
      const offset = ndx * (changingUnitSize / 4)
      vertexValues.set([scale / aspect, scale], offset + kScaleOffset)
    })
    device.queue.writeBuffer(changingVertexBuffer, 0, vertexValues)

    pass.draw(numVertices, kNumObjects)

    pass.end()

    const commandBuffer = encoder.finish()
    device.queue.submit([commandBuffer])
  }

  setupObserver(device, canvas, render)
}

export async function runExample12(webGpuCtx: GPUCanvasContext, device: GPUDevice) {
  const { canvas } = webGpuCtx
  const presentationFormat = navigator.gpu.getPreferredCanvasFormat()
  webGpuCtx.configure({
    device,
    format: presentationFormat,
  })

  const module = device.createShaderModule({
    code: /* wgsl */ `
      struct Vertex {
        @location(0) position: vec2f,
        @location(1) color: vec4f,
        @location(2) offset: vec2f,
        @location(3) scale: vec2f,
        @location(4) perVertexColor: vec3f,
      };

      struct VSOutput {
        @builtin(position) position: vec4f,
        @location(0) color: vec4f,
      };

      @vertex fn vs(
        vert: Vertex,
      ) -> VSOutput {
        var vsOut: VSOutput;
        vsOut.position = vec4f(
            vert.position * vert.scale + vert.offset, 0.0, 1.0);
        vsOut.color = vert.color * vec4f(vert.perVertexColor, 1);
        return vsOut;
      }

      @fragment fn fs(vsOut: VSOutput) -> @location(0) vec4f {
        return vsOut.color;
      }
    `,
  })

  const pipeline = device.createRenderPipeline({
    label: "per vertex color",
    layout: "auto",
    vertex: {
      module,
      buffers: [
        {
          arrayStride: 2 * 4 + 4, // 2 floats, 4 bytes each + 4 bytes
          attributes: [
            { shaderLocation: 0, offset: 0, format: "float32x2" }, // position
            { shaderLocation: 4, offset: 8, format: "unorm8x4" }, // perVertexColor
          ],
        },
        {
          arrayStride: 4 + 2 * 4, // 4 bytes + 2 floats, 4 bytes each
          stepMode: "instance",
          attributes: [
            { shaderLocation: 1, offset: 0, format: "unorm8x4" }, // color
            { shaderLocation: 2, offset: 4, format: "float32x2" }, // offset
          ],
        },
        {
          arrayStride: 2 * 4, // 2 floats, 4 bytes each
          stepMode: "instance",
          attributes: [
            { shaderLocation: 3, offset: 0, format: "float32x2" }, // scale
          ],
        },
      ],
    },
    fragment: {
      module,
      targets: [{ format: presentationFormat }],
    },
  })

  const kNumObjects = 100
  const objectInfos: ObjInfo[] = []

  const staticUnitSize =
    4 + // color is 4 bytes
    2 * 4 // offset is 2 32bit floats (4bytes each)
  const changingUnitSize = 2 * 4 // scale is 2 32bit floats (4bytes each)
  const staticVertexBufferSize = staticUnitSize * kNumObjects
  const changingVertexBufferSize = changingUnitSize * kNumObjects

  const staticVertexBuffer = device.createBuffer({
    label: "static vertex for objects",
    size: staticVertexBufferSize,
    usage: GPUBufferUsage.VERTEX | GPUBufferUsage.COPY_DST,
  })

  const changingVertexBuffer = device.createBuffer({
    label: "changing storage for objects",
    size: changingVertexBufferSize,
    usage: GPUBufferUsage.VERTEX | GPUBufferUsage.COPY_DST,
  })

  const kColorOffset = 0
  const kOffsetOffset = 1

  const kScaleOffset = 0

  {
    const staticVertexValuesU8 = new Uint8Array(staticVertexBufferSize)
    const staticVertexValuesF32 = new Float32Array(staticVertexValuesU8.buffer)
    for (let i = 0; i < kNumObjects; i++) {
      const staticOffsetU8 = i * staticUnitSize
      const staticOffsetF32 = staticOffsetU8 / 4

      staticVertexValuesU8.set([rand() * 255, rand() * 255, rand() * 255, 255], staticOffsetU8 + kColorOffset)
      staticVertexValuesF32.set([rand(-0.9, 0.9), rand(-0.9, 0.9)], staticOffsetF32 + kOffsetOffset)

      objectInfos.push({
        scale: rand(0.2, 0.5),
      })
    }
    device.queue.writeBuffer(staticVertexBuffer, 0, staticVertexValuesF32)
  }

  const vertexValues = new Float32Array(changingVertexBufferSize / 4)

  const { vertexData, indexData, numVertices } = createCircleVerticesWithIndices({
    radius: 0.5,
    innerRadius: 0.25,
  })
  const vertexBuffer = device.createBuffer({
    label: "vertex buffer",
    size: vertexData.byteLength,
    usage: GPUBufferUsage.VERTEX | GPUBufferUsage.COPY_DST,
  })
  device.queue.writeBuffer(vertexBuffer, 0, vertexData)
  const indexBuffer = device.createBuffer({
    label: "index buffer",
    size: indexData.byteLength,
    usage: GPUBufferUsage.INDEX | GPUBufferUsage.COPY_DST,
  })
  device.queue.writeBuffer(indexBuffer, 0, indexData)

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
    pass.setVertexBuffer(0, vertexBuffer)
    pass.setVertexBuffer(1, staticVertexBuffer)
    pass.setVertexBuffer(2, changingVertexBuffer)
    pass.setIndexBuffer(indexBuffer, "uint32")

    const aspect = canvas.width / canvas.height

    objectInfos.forEach(({ scale }, ndx) => {
      const offset = ndx * (changingUnitSize / 4)
      vertexValues.set([scale / aspect, scale], offset + kScaleOffset)
    })
    device.queue.writeBuffer(changingVertexBuffer, 0, vertexValues)

    pass.drawIndexed(numVertices, kNumObjects)

    pass.end()

    const commandBuffer = encoder.finish()
    device.queue.submit([commandBuffer])
  }

  setupObserver(device, canvas, render)
}
