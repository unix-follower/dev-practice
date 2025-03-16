import React from "react"
import * as d3 from "d3"
import { X_INDEX, Y_INDEX } from "app/utils/constants"

function LinePlot() {
  const data: [number, number][] = React.useMemo(
    () => [
      [0, 3],
      [1, 1],
      [2, 3],
    ],
    [],
  )

  const width = 640
  const height = 400
  const marginTop = 20
  const marginRight = 20
  const marginBottom = 20
  const marginLeft = 20

  const xScale = d3
    .scaleLinear()
    .domain([0, data.length - 1])
    .range([marginLeft, width - marginRight])

  const yScale = d3
    .scaleLinear()
    .domain([0, data.length])
    .range([height - marginBottom, marginTop])

  const line = d3
    .line()
    .x((dataPoint) => xScale(dataPoint[X_INDEX]))
    .y((dataPoint) => yScale(dataPoint[Y_INDEX]))

  const xAxisRef = React.useRef<SVGSVGElement>(null)
  const yAxisRef = React.useRef<SVGSVGElement>(null)

  React.useEffect(() => {
    const xAxis = d3.axisBottom(xScale).ticks(data.length - 1)
    const yAxis = d3.axisLeft(yScale).ticks(data.length)
    if (xAxisRef.current) {
      d3.select(xAxisRef.current).call(xAxis)
    }

    if (yAxisRef.current) {
      d3.select(yAxisRef.current).call(yAxis)
    }
  }, [xAxisRef, yAxisRef, xScale, yScale, data])

  return (
    <svg width={width} height={height}>
      <path fill="none" stroke="currentColor" strokeWidth="1.5" d={line(data) ?? undefined} />
      <g fill="white" stroke="currentColor" strokeWidth="1.5">
        {data.map((dataPoint, i) => (
          <circle key={i} cx={xScale(dataPoint[X_INDEX])} cy={yScale(dataPoint[Y_INDEX])} r="2.5" />
        ))}
      </g>
      <g ref={xAxisRef} transform={`translate(0, ${height - marginBottom})`} />
      <g ref={yAxisRef} transform={`translate(${marginLeft}, 0)`} />
    </svg>
  )
}

function ScatterPlot() {
  const svgRef = React.useRef<SVGSVGElement>(null)

  React.useEffect(() => {
    const data = [
      { x: 0, y: 3 },
      { x: 1, y: 1 },
      { x: 2, y: 3 },
    ]
    const width = 400
    const height = 400
    const margin = { top: 20, right: 20, bottom: 40, left: 40 }

    const svg = d3.select(svgRef.current!).attr("width", width).attr("height", height)

    function addAxis(xScale: d3.ScaleLinear<number, number>, yScale: d3.ScaleLinear<number, number>) {
      const xAxis = d3.axisBottom(xScale).ticks(4)
      const yAxis = d3.axisLeft(yScale).ticks(4)

      svg
        .append("g")
        .attr("transform", `translate(0,${height - margin.bottom})`)
        .call(xAxis)
        .attr("class", "axis")

      svg.append("g").attr("transform", `translate(${margin.left},0)`).call(yAxis).attr("class", "axis")
    }

    function createScatterPlot(xScale: d3.ScaleLinear<number, number>, yScale: d3.ScaleLinear<number, number>) {
      svg
        .selectAll(".dot")
        .data(data)
        .enter()
        .append("circle")
        .attr("class", "dot")
        .attr("cx", (dataPoint) => xScale(dataPoint.x))
        .attr("cy", (dataPoint) => yScale(dataPoint.y))
        .attr("r", 5)
    }

    function setXLabel(text: string) {
      svg
        .append("text")
        .attr("text-anchor", "end")
        .attr("x", width / 2 + margin.left)
        .attr("y", height - 5)
        .text(text)
    }

    function setYLabel(text: string) {
      svg
        .append("text")
        .attr("text-anchor", "end")
        .attr("x", -height / 2 + margin.top)
        .attr("y", 15)
        .attr("transform", "rotate(-90)")
        .text(text)
    }

    const xScale = d3
      .scaleLinear()
      .domain([0, 3.5])
      .range([margin.left, width - margin.right])

    const yScale = d3
      .scaleLinear()
      .domain([0, 3.5])
      .range([height - margin.bottom, margin.top])

    addAxis(xScale, yScale)
    createScatterPlot(xScale, yScale)
    setXLabel("x")
    setYLabel("y")
  }, [])

  return <svg ref={svgRef}></svg>
}

export default function Example2D3() {
  return (
    <>
      <LinePlot />
      <ScatterPlot />
    </>
  )
}
