import React from "react"
import * as Plot from "@observablehq/plot"

export default function Exaample2ObservablePlot() {
  const divRef = React.useRef<HTMLDivElement>(null)

  const data = React.useMemo(
    () => [
      { x: 0, y: 3 },
      { x: 1, y: 1 },
      { x: 2, y: 3 },
    ],
    [],
  )

  React.useEffect(() => {
    const { current: currentHtmlRef } = divRef
    if (!currentHtmlRef) {
      return
    }

    const chart = Plot.plot({
      marks: [Plot.dot(data, { x: "x", y: "y", r: 5, fill: "blue" })],
      x: {
        label: "x",
        ticks: [0, 1, 2, 3],
      },
      y: {
        label: "y",
        ticks: [0, 1, 2, 3],
      },
      width: 400,
      height: 400,
      color: {
        type: "ordinal",
        legend: true,
      },
      marginLeft: 50,
      marginTop: 10,
      marginBottom: 40,
      marginRight: 10,
    })

    currentHtmlRef.appendChild(chart)
    return () => {
      currentHtmlRef.innerHTML = ""
    }
  }, [data])

  return <div ref={divRef} />
}
