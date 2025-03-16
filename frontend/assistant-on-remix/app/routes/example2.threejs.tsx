import React from "react"
import * as THREE from "three"
import { OrbitControls } from "three/addons/controls/OrbitControls.js"

function addCirclePoints(scene: THREE.Scene, points: THREE.Vector3[]) {
  points.forEach(({ x, y, z }) => {
    const geometry = new THREE.SphereGeometry(0.1, 32, 32)
    const material = new THREE.MeshBasicMaterial({ color: 0x0000ff })
    const sphere = new THREE.Mesh(geometry, material)
    sphere.position.set(x, y, z)
    scene.add(sphere)
  })
}

const addGrid = (scene: THREE.Scene) => {
  const size = 5
  const divisions = 10
  const gridHelper = new THREE.GridHelper(size, divisions)
  scene.add(gridHelper)
}

const addAxes = (scene: THREE.Scene) => {
  const axesHelper = new THREE.AxesHelper(5)
  scene.add(axesHelper)
}

function createOrbitControls(camera: THREE.PerspectiveCamera, renderer: THREE.WebGLRenderer) {
  const controls = new OrbitControls(camera, renderer.domElement)
  controls.enableDamping = true
  controls.dampingFactor = 0.25
  controls.screenSpacePanning = false
  controls.minDistance = 1
  controls.maxDistance = 100
  controls.maxPolarAngle = Math.PI / 2
  return controls
}

export default function Example2ThreeDScene() {
  const mountRef = React.useRef<HTMLDivElement>(null)
  const points = React.useMemo(
    () => [new THREE.Vector3(0, 3, 0), new THREE.Vector3(1, 1, 0), new THREE.Vector3(2, 3, 0)],
    [],
  )

  React.useEffect(() => {
    const { current: currentHtmlRef } = mountRef
    if (!currentHtmlRef) {
      return
    }

    const scene = new THREE.Scene()
    const camera = new THREE.PerspectiveCamera(75, currentHtmlRef.clientWidth / currentHtmlRef.clientHeight, 0.1, 1000)

    const renderer = new THREE.WebGLRenderer()
    renderer.setSize(currentHtmlRef.clientWidth, currentHtmlRef.clientHeight)
    currentHtmlRef.appendChild(renderer.domElement)

    addCirclePoints(scene, points)
    addGrid(scene)
    addAxes(scene)
    camera.position.z = 5

    const controls = createOrbitControls(camera, renderer)

    const animate = () => {
      requestAnimationFrame(animate)
      controls.update()
      renderer.render(scene, camera)
    }
    animate()
    return () => {
      const { current } = mountRef
      if (current) {
        current.removeChild(renderer.domElement)
      }
    }
  }, [points])

  return <div ref={mountRef} style={{ width: "100vw", height: "100vh" }} />
}
