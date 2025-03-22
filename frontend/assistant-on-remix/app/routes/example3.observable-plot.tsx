import React from "react"
import * as math from "mathjs"
import * as Plot from "@observablehq/plot"

export default function Exaample3ObservablePlot() {
  const plotDivRef = React.useRef<HTMLDivElement>(null)

  const data = React.useMemo(() => {
    const coefficientMatrix = math.matrix([
      [1, 1, 0],
      [2, -1, 3],
      [1, -2, -1],
    ])
    const constantVector = math.matrix([0, 3, 3])

    const solution = math.lusolve(coefficientMatrix, constantVector)
    console.debug(solution)

    const result = solution.toArray().flat()
    console.debug(result)
    return result.map((value) => ({ value }))
  }, [])

  React.useEffect(() => {
    const { current: currentHtmlRef } = plotDivRef
    if (!currentHtmlRef) {
      return
    }

    const plot = Plot.plot({
      marks: [Plot.rectY(data, Plot.binX({ y: "count" }, { x: "value", thresholds: 20 }))],
      x: {
        label: "x",
        domain: [-1.5, 1.5],
      },
      y: {
        label: "y",
        grid: true,
      },
      width: 600,
      height: 300,
      color: {
        range: ["steelblue"],
      },
    })

    currentHtmlRef.appendChild(plot)
    return () => {
      currentHtmlRef.innerHTML = ""
    }
  }, [data])

  return (
    <div>
      <div ref={plotDivRef}></div>
    </div>
  )
}
