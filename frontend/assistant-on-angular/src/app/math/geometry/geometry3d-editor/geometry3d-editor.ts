/// <reference types="@webgpu/types" />
import { Component, ElementRef, Inject, OnInit, PLATFORM_ID, ViewChild } from "@angular/core"
import { isPlatformBrowser } from "@angular/common"
import { ActivatedRoute } from "@angular/router"
import {
  runExample1,
  runExample2,
  runExample3,
  runExample4,
  runExample5,
  runExample6,
  runExample7,
  runExample8,
  runExample9,
  runExample10,
  runExample11,
  runExample12,
} from "./webgpu-samples"

@Component({
  selector: "app-geometry3d-editor",
  imports: [],
  templateUrl: "./geometry3d-editor.html",
  styleUrl: "./geometry3d-editor.css",
})
export default class Geometry3dEditor implements OnInit {
  @ViewChild("mainCanvas") canvasRef!: ElementRef<HTMLCanvasElement>
  private webGpuCtx!: GPUCanvasContext
  private exampleIndex: number = 0

  constructor(
    @Inject(PLATFORM_ID) private platformId: object,
    private route: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.route.queryParamMap.subscribe((params) => {
      this.exampleIndex = Number.parseInt(params.get("exampleNo") || "1") - 1
    })
  }

  async ngAfterViewInit() {
    if (isPlatformBrowser(this.platformId)) {
      if (this.canvasRef && this.canvasRef.nativeElement) {
        const canvas = this.canvasRef.nativeElement
        this.webGpuCtx = canvas.getContext("webgpu")!
        const adapter = await navigator.gpu?.requestAdapter()
        const device = await adapter?.requestDevice()
        console.debug(`adapter=${adapter}`)
        console.debug(`this.webgpuCtx=${this.webGpuCtx}`)
        console.debug(`device=${device}`)
        if (!device) {
          throw new Error("The browser does not support WebGPU device")
        }

        const runExample = [
          (webGpuCtx: GPUCanvasContext, device: GPUDevice) => runExample1(webGpuCtx, device),
          (_webGpuCtx: GPUCanvasContext, device: GPUDevice) => runExample2(device),
          (webGpuCtx: GPUCanvasContext, device: GPUDevice) => runExample3(webGpuCtx, device),
          (webGpuCtx: GPUCanvasContext, device: GPUDevice) => runExample4(webGpuCtx, device),
          (webGpuCtx: GPUCanvasContext, device: GPUDevice) => runExample5(webGpuCtx, device),
          (webGpuCtx: GPUCanvasContext, device: GPUDevice) => runExample6(webGpuCtx, device),
          (webGpuCtx: GPUCanvasContext, device: GPUDevice) => runExample7(webGpuCtx, device),
          (webGpuCtx: GPUCanvasContext, device: GPUDevice) => runExample8(webGpuCtx, device),
          (webGpuCtx: GPUCanvasContext, device: GPUDevice) => runExample9(webGpuCtx, device),
          (webGpuCtx: GPUCanvasContext, device: GPUDevice) => runExample10(webGpuCtx, device),
          (webGpuCtx: GPUCanvasContext, device: GPUDevice) => runExample11(webGpuCtx, device),
          (webGpuCtx: GPUCanvasContext, device: GPUDevice) => runExample12(webGpuCtx, device),
        ][this.exampleIndex]
        await runExample(this.webGpuCtx, device)
      }
    }
  }
}
