import React, { useRef, useState } from "react"

import Footer from "~/components/common/Footer"
import "~/components/geometry/translation.css"

function Navbar() {
  return (
    <header>
      <nav>
        <span>Geometry</span>
      </nav>
    </header>
  )
}

export default function Page() {
  const [rotationDegree, setRotationDegree] = useState(0)
  const [rotationRadians, setRotationRadians] = useState(0.0)
  const triangleImageRef = useRef<HTMLImageElement | null>(null)

  function degreesToRadians(degrees: number) {
    return degrees * (Math.PI / 180)
  }

  function radiansToDegrees(radians: number) {
    return radians * (180 / Math.PI)
  }

  function updateRotationDegree(rotationDegree: number) {
    setRotationDegree(rotationDegree)
    document.documentElement.style.setProperty("--rotation-degree", rotationDegree.toString())
    if (triangleImageRef) {
      const imgElement = triangleImageRef.current
      if (imgElement) {
        imgElement.style.transform = `rotate(${rotationDegree}deg)`
      }
    }
  }

  function updateRotationRadians(rotation: number) {
    setRotationRadians(rotation)
    document.documentElement.style.setProperty("--rotation-radians", rotation.toString())
    if (triangleImageRef) {
      const imgElement = triangleImageRef.current
      if (imgElement) {
        imgElement.style.transform = `rotate(${rotation}rad)`
      }
    }
  }

  function handleRotationDegreeInput(e: React.ChangeEvent<HTMLInputElement>) {
    const { value } = e.target
    const rotationDegree = Number.parseFloat(value)
    updateRotationDegree(rotationDegree)
    updateRotationRadians(degreesToRadians(rotationDegree))
  }

  function handleRotationRadiansInput(e: React.ChangeEvent<HTMLInputElement>) {
    const { value } = e.target
    const rotationRadians = Number.parseFloat(value)
    updateRotationRadians(rotationRadians)
    updateRotationDegree(radiansToDegrees(rotationRadians))
  }

  return (
    <>
      <Navbar />
      <div className="grid-container">
        <img ref={triangleImageRef} className="triangle" src="" alt="" />
        <div className="slide-container">
          <p>
            <label htmlFor="rotation-degree-slider">{rotationDegree}°</label>
            <input
              id="rotation-degree-slider"
              className="slider"
              type="range"
              min="0"
              max="360"
              value={rotationDegree}
              onInput={handleRotationDegreeInput}
            />
          </p>
          <p>
            <label htmlFor="rotation-radians-slider">{rotationRadians} rad</label>
            <input
              id="rotation-radians-slider"
              className="slider"
              type="range"
              min="0"
              max={2 * Math.PI}
              step="0.001"
              value={rotationRadians}
              onInput={handleRotationRadiansInput}
            />
          </p>
        </div>
      </div>
      <Footer />
    </>
  )
}
