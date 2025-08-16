"use client"

import React, { useEffect, useRef } from "react"
import cytoscape, { type ElementDefinition } from "cytoscape"

interface CompoundGraphProps {
  compoundData: ElementDefinition[]
}

export default function CompoundGraph({ compoundData }: CompoundGraphProps) {
  const cyRef = useRef(null)

  useEffect(() => {
    const cy = cytoscape({
      container: cyRef.current,
      elements: compoundData,
      style: [
        {
          selector: "node[label='Element']",
          style: {
            "background-color": "#0074D9",
            "text-wrap": "wrap",
            "font-size": "12px",
            color: "#FFFFFF",
          },
        },
        {
          selector: "node[label='Compound']",
          style: {
            "background-color": "#FF851B",
            "text-wrap": "wrap",
            "font-size": "12px",
            color: "#FFFFFF",
          },
        },
        {
          selector: "edge",
          style: {
            "line-color": "#AAAAAA",
            "target-arrow-color": "#AAAAAA",
            "target-arrow-shape": "triangle",
            label: "data(label)",
            "font-size": "10px",
            "text-wrap": "wrap",
            "text-max-width": "80",
            color: "#000000",
          },
        },
      ],
    })

    cy.on("tap", "node", (event) => {
      const node = event.target
      console.log("Node clicked:", node.data())
    })

    cy.on("tap", "edge", (event) => {
      const edge = event.target
      console.log("Edge clicked:", edge.data())
    })

    cy.on("mouseover", "node", (event) => {
      const node = event.target
      console.log("Node:", node.data())
    })

    return () => cy.destroy()
  }, [compoundData])

  return <div ref={cyRef} style={{ width: "100%", height: "500px" }} />
}
