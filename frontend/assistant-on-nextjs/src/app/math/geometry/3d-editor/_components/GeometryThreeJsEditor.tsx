"use client"

import React, { useEffect, useRef } from "react"
import * as THREE from "three"
import WebGL from "three/addons/capabilities/WebGL.js"
import { useTranslation } from "react-i18next"
import i18n from "@/config/i18n"

interface GeometryThreeJsEditorProps {
  translations: Record<string, string | Record<string, string>>
}

export default function GeometryThreeJsEditor({ translations }: GeometryThreeJsEditorProps) {
  useEffect(() => {
    i18n.addResourceBundle("en", "translation", translations, true, true)
  }, [translations])

  const divRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    const { current: currentDivRef } = divRef
    if (!currentDivRef) {
      return
    }

    const scene = new THREE.Scene()
    const camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000)
    const geometry = new THREE.BoxGeometry(1, 1, 1)
    const material = new THREE.MeshBasicMaterial({ color: 0x00ff00 })
    const cube = new THREE.Mesh(geometry, material)
    scene.add(cube)

    camera.position.z = 5

    const renderer = new THREE.WebGLRenderer()
    renderer.setSize(window.innerWidth, window.innerHeight)
    currentDivRef.appendChild(renderer.domElement)

    function animate() {
      cube.rotation.x += 0.01
      cube.rotation.y += 0.01

      renderer.render(scene, camera)
    }

    if (WebGL.isWebGL2Available()) {
      renderer.setAnimationLoop(animate)
    } else {
      const warning = WebGL.getWebGL2ErrorMessage()
      currentDivRef.appendChild(warning)
    }

    return () => {
      const { current } = divRef
      if (current) {
        current.removeChild(renderer.domElement)
      }
    }
  })

  return (
    <div id="geometry-threejs-editor" className="grid grid-cols-[12%_88%] grid-rows-1 gap-4">
      <ToolPanel />
      <div ref={divRef} style={{ width: "100vw", height: "100vh" }} />
    </div>
  )
}

export function ToolPanel() {
  const { t } = useTranslation()

  return (
    <div id="geometry-threejs-editor-tool-panel" className="grid grid-cols-1 grid-rows-3">
      <div>
        <span>{t("geometry3DEditorPage.primitives")}</span>
      </div>
      <div>
        <span>{t("geometry3DEditorPage.lights")}</span>
      </div>
    </div>
  )
}
