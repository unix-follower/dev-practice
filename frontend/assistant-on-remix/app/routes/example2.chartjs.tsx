import React from "react"
import { Chart, registerables } from "chart.js"

Chart.register(...registerables)

const LineChart = () => {
  const chartRef = React.useRef<HTMLCanvasElement>(null)

  React.useEffect(() => {
    if (chartRef.current == null) {
      return
    }

    const x = [0, 1, 2, 3]
    const y = [3, 1, 3]

    const lineChart = new Chart(chartRef.current, {
      type: "line",
      data: {
        labels: x,
        datasets: [
          {
            label: "My Dataset",
            data: y,
            fill: false,
            borderColor: "rgb(75, 192, 192)",
            tension: 0.1,
            pointRadius: 5,
          },
        ],
      },
      options: {
        scales: {
          x: {
            title: {
              display: true,
              text: "x",
            },
            min: 0,
            max: 3.5,
          },
          y: {
            title: {
              display: true,
              text: "y",
            },
            min: 0,
            max: 3.5,
          },
        },
      },
    })

    return () => {
      lineChart.destroy()
    }
  }, [])

  return <canvas ref={chartRef} />
}

const ScatterChart = () => {
  const chartRef = React.useRef(null)

  React.useEffect(() => {
    if (chartRef.current == null) {
      return
    }

    const scatterChart = new Chart(chartRef.current, {
      type: "scatter",
      data: {
        datasets: [
          {
            label: "Scatter Dataset",
            data: [
              { x: 0, y: 3 },
              { x: 1, y: 1 },
              { x: 2, y: 3 },
            ],
            backgroundColor: "rgba(255, 99, 132, 1)",
          },
        ],
      },
      options: {
        scales: {
          x: {
            type: "linear",
            position: "bottom",
          },
        },
      },
    })

    return () => {
      scatterChart.destroy()
    }
  }, [])

  return <canvas ref={chartRef} />
}

export default function Example2Chartjs() {
  return (
    <>
      <ScatterChart />
      <LineChart />
    </>
  )
}
