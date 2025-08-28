"use client"

import React, { Suspense, useEffect, useRef, useState } from "react"
import cytoscape, { type ElementDefinition } from "cytoscape"
import CytoscapeContext from "@/lib/chemistryHooks"
import ToolPanel from "./ToolPanel"
import i18n from "@/config/i18n"

interface CompoundGraphViewerProps {
  compoundDataPromise?: Promise<ElementDefinition[]>
  compoundData: ElementDefinition[]
  translations: Record<string, string | Record<string, string>>
}

export default function CompoundGraphViewer({ translations, compoundData }: CompoundGraphViewerProps) {
  const cyContainerRef = useRef<HTMLDivElement>(null)
  const [cyInstance, setCyInstance] = useState<cytoscape.Core | null>(null)

  useEffect(() => {
    const cy = cytoscape({
      container: cyContainerRef.current,
      elements: compoundData,
      layout: { name: "grid" },
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

    setCyInstance(cy)

    return () => cy.destroy()
  }, [compoundData])

  useEffect(() => {
    i18n.addResourceBundle("en", "translation", translations, true, true)
  }, [translations])

  return (
    <CytoscapeContext value={cyInstance}>
      <Suspense fallback={<div>Loading Tool Panel...</div>}>
        <ToolPanel />
      </Suspense>
      <div ref={cyContainerRef} style={{ width: "100%", height: "500px" }} />
    </CytoscapeContext>
  )
}
