import React from "react"
import { useStyleEngine } from "@/lib/hooks/styleEngineHooks"
import styles from "./gpuDetails.module.css"
import { useTranslations } from "next-intl"

interface GpuDetailsProps {
  id: string
  className?: string
}

const details = new Map()
details.set(
  "NVIDIA_GB207-300",
  "NVIDIA's GB207 GPU uses the Blackwell 2.0 architecture and is made using a 5 nm production process at TSMC. With a die size of 149 mm² and a transistor count of 16,900 million it is a small chip. GB207 supports DirectX 12 Ultimate (Feature Level 12_2). For GPU compute applications, OpenCL version 3.0 and CUDA 12.0 can be used. Additionally, the DirectX 12 Ultimate capability guarantees support for hardware-raytracing, variable-rate shading and more, in upcoming video games. It features 2560 shading units, 80 texture mapping units and 32 ROPs. Also included are 80 tensor cores which help improve the speed of machine learning applications. The GPU also contains 20 raytracing acceleration cores.",
)

export default function GpuDetails({ id, className }: GpuDetailsProps) {
  const t = useTranslations("computerScienceHardwarePage")
  const { styleEngine } = useStyleEngine()

  let style = className
  if (styleEngine === "css" && className) {
    style = styles[className]
  }

  if (details.has(id)) {
    return (
      <div className={style}>
        <p>{details.get(id)}</p>)
      </div>
    )
  }

  return (
    <div className={style}>
      <p>{t("noData")}</p>
    </div>
  )
}
