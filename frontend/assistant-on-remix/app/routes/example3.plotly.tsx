import React from "react"

const Plot = React.lazy(() => import("react-plotly.js"))

export default function Example3Plotly() {
  const [isComponentMounted, setIsComponentMounted] = React.useState(false)

  React.useEffect(() => {
    setIsComponentMounted(true)
  }, [])

  if (!isComponentMounted) {
    return null
  }

  const chartOptions = {
    x: [1, -1, 0],
    type: "histogram",
    nbinsx: 20,
    marker: {
      color: "blue",
    },
  }

  const layout = {
    xaxis: { title: "x", range: [-1.5, 1.5] },
    yaxis: { title: "y" },
    title: "Histogram",
  }

  return (
    <React.Suspense fallback={<div>Loading plot...</div>}>
      <Plot
        // @ts-expect-error they use type string in the docs
        data={[chartOptions]}
        layout={layout}
      />
    </React.Suspense>
  )
}
