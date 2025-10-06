import React from "react"
import { useStyleEngine } from "@/lib/hooks/styleEngineHooks"
import styles from "./gpuDetails.module.css"
import { useTranslations } from "next-intl"

const details = new Map()
details.set(
  "NVIDIA_GB207-300",
  "NVIDIA's GB207 GPU uses the Blackwell 2.0 architecture and is made using a 5 nm production process at TSMC. With a die size of 149 mm² and a transistor count of 16,900 million it is a small chip. GB207 supports DirectX 12 Ultimate (Feature Level 12_2). For GPU compute applications, OpenCL version 3.0 and CUDA 12.0 can be used. Additionally, the DirectX 12 Ultimate capability guarantees support for hardware-raytracing, variable-rate shading and more, in upcoming video games. It features 2560 shading units, 80 texture mapping units and 32 ROPs. Also included are 80 tensor cores which help improve the speed of machine learning applications. The GPU also contains 20 raytracing acceleration cores.",
)

const twStyleMap = new Map()
twStyleMap.set(
  "tw-template1",
  "line-clamp-1 align-super leading-none font-bold tracking-wide break-keep whitespace-pre text-current lowercase italic line-through decoration-sky-500",
)
twStyleMap.set(
  "tw-template2",
  "truncate text-right align-bottom font-sans text-sm leading-none font-extralight tracking-normal break-all text-inherit decoration-pink-500 decoration-solid decoration-1",
)
twStyleMap.set(
  "tw-template3",
  "line-clamp-none list-decimal border-stone-500 align-text-bottom leading-3 capitalize font-stretch-condensed underline-offset-1 bg-gray-700/70",
)
twStyleMap.set("tw-template4", "line-clamp-1 bg-blue-700 text-red-200 ps-1 pe-3 pl-3 pt-5 pb-2 me-px -mt-1 -mb-2")
twStyleMap.set("tw-template5", "border-teal-950/30 bg-pink-400/[85.2%] pr-1 px-4 py-16 m-2 mb-auto")
twStyleMap.set("tw-template6", "-space-y-px -space-x-3")

interface GpuDetailsProps {
  id: string
  className?: string
}

export default function GpuDetails({ id, className }: GpuDetailsProps) {
  const t = useTranslations("computerScienceHardwarePage")
  const { styleEngine } = useStyleEngine()

  let style = className
  if (styleEngine === "css" && className) {
    style = styles[className]
  } else if (styleEngine === "tw" && twStyleMap.has(className)) {
    style = twStyleMap.get(className)
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
