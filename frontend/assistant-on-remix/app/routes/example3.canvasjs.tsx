import React from "react"
import * as math from "mathjs"

interface CalcResult {
  [key: number]: number
}

export default function Example3Chartjs() {
  const [canvasJSChart, setCanvasJSChart] = React.useState(null)

  React.useEffect(() => {
    async function loadCanvasJS() {
      // @ts-expect-error the lib doesn't support SSR
      const module = await import("@canvasjs/react-charts")
      setCanvasJSChart(() => module.default.CanvasJSChart)
    }

    loadCanvasJS()
  }, [])

  const [isComponentMounted, setIsComponentMounted] = React.useState(false)

  const dataPoints = React.useMemo(() => {
    const coefficientMatrix = math.matrix([
      [1, 1, 0],
      [2, -1, 3],
      [1, -2, -1],
    ])
    const constantVector = math.matrix([0, 3, 3])

    const result = math.lusolve(coefficientMatrix, constantVector)
    console.debug(result)

    const countResult = result.toArray().reduce((acc, value) => {
      const key = Math.round((value as number) * 100) / 100
      acc[key] = (acc[key] || 0) + 1
      return acc
    }, {} as CalcResult)
    console.debug(countResult)
    const dataPoints = Object.entries(countResult).map(([x, y]) => {
      return {
        x,
        y,
      }
    })
    console.debug(JSON.stringify(dataPoints))
    return dataPoints
  }, [])

  React.useEffect(() => {
    setIsComponentMounted(true)
  }, [])

  if (!isComponentMounted || !canvasJSChart) {
    return <div>Loading plot...</div>
  }

  const chartOptions = {
    animationEnabled: true,
    theme: "light2",
    title: {
      text: "Results Histogram",
    },
    axisX: {
      title: "Value",
      minimum: -1.5,
      maximum: 1.5,
    },
    axisY: {
      title: "Frequency",
      includeZero: true,
    },
    data: [
      {
        type: "column",
        dataPoints,
      },
    ],
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const CanvasChartComponent = canvasJSChart as React.ComponentType<any>

  return <CanvasChartComponent options={chartOptions} />
}
