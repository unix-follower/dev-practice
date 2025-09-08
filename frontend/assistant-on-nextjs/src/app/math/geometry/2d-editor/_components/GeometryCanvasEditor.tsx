"use client"

import React, { useEffect, useRef } from "react"
import Tooltip from "@mui/material/Tooltip"
import { useTranslation } from "react-i18next"
import i18n from "@/config/i18n"
import { useAppDispatch, useAppSelector } from "@/lib/hooks"
import { Square, Eraser } from "@/app/_components/common/fontAwesomeIcons"
import {
  getIsDrawRectangleDialogOpened,
  setIsDrawRectangleDialogOpened,
} from "@/lib/features/math/geometry/geometryCanvasEditorSlice"
import DrawRectangleDraggableDialog, {
  NewRectangle,
} from "@/app/math/geometry/2d-editor/_components/DrawRectangleDraggableDialog"

interface GeometryCanvasEditorProps {
  translations: Record<string, string | Record<string, string>>
}

export default function GeometryCanvasEditor({ translations }: GeometryCanvasEditorProps) {
  useEffect(() => {
    i18n.addResourceBundle("en", "translation", translations, true, true)
  }, [translations])

  const canvasRef = useRef<HTMLCanvasElement>(null)

  return (
    <div id="geometry-canvas-editor" className="grid grid-cols-[12%_88%] grid-rows-1 gap-4">
      <ToolPanel canvasRef={canvasRef} />
      <canvas ref={canvasRef} style={{ width: "100vw", height: "100vh" }} />
    </div>
  )
}

interface ToolPanelProps {
  canvasRef: React.RefObject<HTMLCanvasElement | null>
}

function ToolPanel({ canvasRef }: ToolPanelProps) {
  const dispatch = useAppDispatch()
  const isDrawRectangleDialogOpened = useAppSelector(getIsDrawRectangleDialogOpened)

  const { t } = useTranslation()

  function handleDrawRectangleClick() {
    dispatch(setIsDrawRectangleDialogOpened(true))
  }

  function drawRectangle({ x, y, width, height }: NewRectangle) {
    const canvas = canvasRef.current
    if (canvas) {
      const ctx = canvas.getContext("2d")!
      const rectWidth = width || canvas.width / 3
      const rectHeight = height || canvas.height / 3
      ctx.fillRect(x, y, rectWidth, rectHeight)
    }
  }

  function clearCanvas() {
    const canvas = canvasRef.current
    if (canvas) {
      const ctx = canvas.getContext("2d")!
      ctx.clearRect(0, 0, canvas.width, canvas.height)
    }
  }

  return (
    <div id="geometry-canvas-editor-tool-panel" className="grid grid-cols-1 grid-rows-3">
      <div>
        <div>
          <span>{t("geometry2DEditorPage.primitives")}</span>
        </div>
        <Tooltip title={t("geometry2DEditorPage.rectangle")} placement="bottom-end">
          <button onClick={handleDrawRectangleClick}>
            <Square />
          </button>
        </Tooltip>
        {isDrawRectangleDialogOpened && <DrawRectangleDraggableDialog onSubmit={drawRectangle} />}
        <Tooltip title={t("geometry2DEditorPage.clearCanvas")} placement="bottom-end">
          <button onClick={clearCanvas}>
            <Eraser />
          </button>
        </Tooltip>
      </div>
    </div>
  )
}
