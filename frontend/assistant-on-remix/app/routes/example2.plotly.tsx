import React from "react"

const Plot = React.lazy(() => import("react-plotly.js"))

export default function Example2Plotly() {
  const [isComponentMounted, setIsComponentMounted] = React.useState(false)

  React.useEffect(() => {
    setIsComponentMounted(true)
  }, [])

  if (!isComponentMounted) {
    return null
  }

  const xValues = [0, 1, 2]
  const yValues = [3, 1, 3]

  const layout = {
    title: "Line and Scatter Plot",
    xaxis: {
      title: "x",
      range: [-0.2, 2.5],
      tickvals: [0, 1, 2, 3],
    },
    yaxis: {
      title: "y",
      range: [0, 3.5],
      tickvals: [0, 1, 2, 3],
    },
  }

  return (
    <React.Suspense fallback={<div>Loading plot...</div>}>
      <Plot
        data={[
          {
            x: xValues,
            y: yValues,
            type: "scatter",
            mode: "lines+markers",
            marker: { color: "blue", size: 10 },
            name: "Line Plot",
          },
        ]}
        layout={layout}
      />
    </React.Suspense>
  )
}
