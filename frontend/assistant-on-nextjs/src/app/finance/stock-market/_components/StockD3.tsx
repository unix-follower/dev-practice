"use client"

import React, { use, useEffect, useRef } from "react"
import * as d3 from "d3"
import StocksResponseDto from "@/lib/api/finance/stockMarketModel"
import { type D3SelectSVGSVGElement } from "@/lib/utils/d3Utils"

interface DataPoint {
  x: Date
  y: number
}

interface StockDataProps {
  openPrices: DataPoint[]
  closePrices: DataPoint[]
  adjustedClosePrices: DataPoint[]
  lowPrices: DataPoint[]
  highPrices: DataPoint[]
}

type SeriesConfig = { key: keyof StockDataProps; color: string; label: string }[]

function createSeriesConfig(): SeriesConfig {
  return [
    { key: "openPrices", color: "steelblue", label: "Open" },
    { key: "closePrices", color: "green", label: "Close" },
    { key: "adjustedClosePrices", color: "purple", label: "Adj Close" },
    { key: "lowPrices", color: "orange", label: "Low" },
    { key: "highPrices", color: "brown", label: "High" },
  ]
}

interface MarginSettings {
  top: number
  right: number
  bottom: number
  left: number
}

function createScales({
  seriesConfig,
  width,
  height,
  margin,
  dataPoints,
  stockDataProps,
}: {
  seriesConfig: SeriesConfig
  width: number
  height: number
  margin: MarginSettings
  dataPoints: DataPoint[]
  stockDataProps: StockDataProps
}) {
  // X domain from first series (assuming all are same dates range)
  const xScale = d3
    .scaleTime()
    .domain(d3.extent(dataPoints, (d) => d.x) as [Date, Date])
    .range([margin.left, width - margin.right])

  // Y domain: min & max among *all series*
  const allValues = seriesConfig.flatMap((cfg) => stockDataProps[cfg.key].map((d) => d.y))
  const yScale = d3
    .scaleLinear()
    .domain(d3.extent(allValues) as [number, number])
    .nice()
    .range([height - margin.bottom, margin.top])

  return { xScale, yScale }
}

function LinePlot(props: StockDataProps) {
  const width = 640
  const height = 400
  const margin = { top: 20, right: 20, bottom: 30, left: 50 }

  const seriesConfig = createSeriesConfig()

  const { xScale, yScale } = createScales({
    seriesConfig,
    width,
    height,
    margin,
    dataPoints: props.openPrices,
    stockDataProps: props,
  })

  const line = d3
    .line<DataPoint>()
    .x((d) => xScale(d.x))
    .y((d) => yScale(d.y))

  const xAxisRef = React.useRef<SVGGElement>(null)
  const yAxisRef = React.useRef<SVGGElement>(null)

  useEffect(() => {
    if (xAxisRef.current) {
      d3.select(xAxisRef.current).call(d3.axisBottom<Date>(xScale).ticks(6).tickFormat(d3.timeFormat("%b %d")))
    }
    if (yAxisRef.current) {
      d3.select(yAxisRef.current).call(d3.axisLeft(yScale))
    }
  }, [xScale, yScale])

  return (
    <svg width={width} height={height}>
      {/* Series lines */}
      {seriesConfig.map((cfg) => (
        <path key={cfg.key} fill="none" stroke={cfg.color} strokeWidth="1.5" d={line(props[cfg.key]) ?? undefined} />
      ))}
      {/* Optional points */}
      {seriesConfig.map((cfg) => (
        <g key={cfg.key} fill="white" stroke={cfg.color} strokeWidth={1.5}>
          {props[cfg.key].map((d, i) => (
            <circle key={i} cx={xScale(d.x)} cy={yScale(d.y)} r={2} />
          ))}
        </g>
      ))}

      {/* Axes */}
      <g ref={xAxisRef} transform={`translate(0,${height - margin.bottom})`} />
      <g ref={yAxisRef} transform={`translate(${margin.left},0)`} />

      {/* Legend */}
      <g transform={`translate(${width - margin.right - 100},${margin.top})`}>
        {seriesConfig.map((cfg, i) => (
          <g key={cfg.key} transform={`translate(0,${i * 20})`}>
            <rect width="12" height="12" fill={cfg.color}></rect>
            <text x="16" y="10" fontSize="12">
              {cfg.label}
            </text>
          </g>
        ))}
      </g>
    </svg>
  )
}

function ScatterPlot(props: StockDataProps) {
  const svgRef = useRef<SVGSVGElement>(null)

  const width = 640
  const height = 400

  useEffect(() => {
    function createAxis(svg: D3SelectSVGSVGElement, seriesCfg: SeriesConfig, margin: MarginSettings) {
      const { xScale, yScale } = createScales({
        seriesConfig: seriesCfg,
        width,
        height,
        margin,
        dataPoints: props.openPrices,
        stockDataProps: props,
      })

      svg
        .append("g")
        .attr("transform", `translate(0,${height - margin.bottom})`)
        .call(d3.axisBottom<Date>(xScale).tickFormat(d3.timeFormat("%b %d")))

      svg.append("g").attr("transform", `translate(${margin.left},0)`).call(d3.axisLeft(yScale))

      return { xAxis: xScale, yAxis: yScale }
    }

    function drawPoints(
      svg: D3SelectSVGSVGElement,
      seriesCfg: SeriesConfig,
      xAxis: d3.ScaleTime<number, number>,
      yAxis: d3.ScaleLinear<number, number>,
    ) {
      seriesCfg.forEach((cfg) => {
        svg
          .append("g")
          .selectAll("circle")
          .data(props[cfg.key])
          .enter()
          .append("circle")
          .attr("cx", (d) => xAxis(d.x))
          .attr("cy", (d) => yAxis(d.y))
          .attr("r", 3)
          .attr("fill", cfg.color)
      })
    }

    function addLegend(svg: D3SelectSVGSVGElement, seriesCfg: SeriesConfig, margin: MarginSettings) {
      const legend = svg.append("g").attr("transform", `translate(${width - margin.right - 100},${margin.top})`)

      seriesCfg.forEach((cfg, i) => {
        const g = legend.append("g").attr("transform", `translate(0,${i * 20})`)
        g.append("rect").attr("width", 12).attr("height", 12).attr("fill", cfg.color)
        g.append("text").attr("x", 16).attr("y", 10).attr("font-size", 12).text(cfg.label)
      })
    }

    const margin = { top: 20, right: 20, bottom: 40, left: 50 }
    const seriesConfig = createSeriesConfig()

    const svg = d3.select(svgRef.current!)
    svg.selectAll("*").remove()

    const { xAxis, yAxis } = createAxis(svg, seriesConfig, margin)
    drawPoints(svg, seriesConfig, xAxis, yAxis)
    addLegend(svg, seriesConfig, margin)
  }, [props])

  return <svg ref={svgRef} width={width} height={height}></svg>
}

interface StockD3Props {
  stocksResponsePromise: Promise<StocksResponseDto>
  translations: Record<string, string | Record<string, string>>
}

export default function StockD3({ stocksResponsePromise }: StockD3Props) {
  const stocksResponse = use(stocksResponsePromise)

  const mapStockData = (key: keyof (typeof stocksResponse.stocks)[number]) => {
    return stocksResponse.stocks
      .map((stock) => ({
        x: new Date(stock.dateAt),
        y: stock[key] as number,
      }))
      .sort((a, b) => a.x.getTime() - b.x.getTime())
  }

  const openPrices = mapStockData("open")
  const closePrices = mapStockData("close")
  const adjustedClosePrices = mapStockData("adjustedClose")
  const lowPrices = mapStockData("low")
  const highPrices = mapStockData("high")

  return (
    <>
      <LinePlot
        openPrices={openPrices}
        closePrices={closePrices}
        adjustedClosePrices={adjustedClosePrices}
        lowPrices={lowPrices}
        highPrices={highPrices}
      />
      <ScatterPlot
        openPrices={openPrices}
        closePrices={closePrices}
        adjustedClosePrices={adjustedClosePrices}
        lowPrices={lowPrices}
        highPrices={highPrices}
      />
    </>
  )
}
