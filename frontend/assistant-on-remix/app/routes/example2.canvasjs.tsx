import React from "react"

export default function Example2CanvasJS() {
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

  React.useEffect(() => {
    setIsComponentMounted(true)
  }, [])

  if (!isComponentMounted || !canvasJSChart) {
    return <div>Loading plot...</div>
  }

  const chartOptions = {
    animationEnabled: true,
    title: {
      text: "Line and Scatter Plot",
    },
    axisX: {
      title: "x",
      minimum: -0.2,
      maximum: 2.5,
      interval: 1,
    },
    axisY: {
      title: "y",
      minimum: 0,
      maximum: 3.5,
      interval: 1,
    },
    data: [
      {
        type: "line",
        name: "Line Plot",
        showInLegend: true,
        markerType: "circle",
        markerSize: 10,
        dataPoints: [
          { x: 0, y: 3 },
          { x: 1, y: 1 },
          { x: 2, y: 3 },
        ],
      },
      {
        type: "scatter",
        name: "Scatter Plot",
        showInLegend: true,
        markerType: "circle",
        markerSize: 10,
        dataPoints: [
          { x: 0, y: 3 },
          { x: 1, y: 1 },
          { x: 2, y: 3 },
        ],
      },
    ],
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const CanvasChartComponent = canvasJSChart as React.ComponentType<any>

  return <CanvasChartComponent options={chartOptions} />
}
