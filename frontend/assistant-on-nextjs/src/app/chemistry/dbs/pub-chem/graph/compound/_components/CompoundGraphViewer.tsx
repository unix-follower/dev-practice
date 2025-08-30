"use client"

import React, { Suspense, useCallback, useEffect, useRef, useState } from "react"
import cytoscape, { type ElementDefinition, NodeSingular } from "cytoscape"
import CytoscapeContext from "@/lib/chemistryHooks"
import ToolPanel from "./ToolPanel"
import i18n from "@/config/i18n"
import { useAppDispatch, useAppSelector } from "@/lib/hooks"
import { getTappedNodeId, setTappedNodeId } from "@/lib/features/chemistry/compoundGraphViewerSlice"

interface CompoundGraphViewerProps {
  compoundDataPromise?: Promise<ElementDefinition[]>
  compoundData: ElementDefinition[]
  translations: Record<string, string | Record<string, string>>
}

const ELEMENT_COLOR = "#0074D9"
const COMPOUND_COLOR = "#FF851B"

function getNodeColor(data: cytoscape.NodeDataDefinition) {
  if (data && data.label === "Compound") {
    return COMPOUND_COLOR
  }
  return ELEMENT_COLOR
}

export default function CompoundGraphViewer({ translations, compoundData }: CompoundGraphViewerProps) {
  const cyContainerRef = useRef<HTMLDivElement>(null)
  const [cyInstance, setCyInstance] = useState<cytoscape.Core | null>(null)
  const dispatch = useAppDispatch()
  const tappedNodeId = useAppSelector(getTappedNodeId)

  const handleTapNode = useCallback(
    (event: cytoscape.EventObject) => {
      const node = event.target as cytoscape.SingularData as NodeSingular
      console.log("Node clicked:", node)
      if (tappedNodeId && tappedNodeId === node.id()) {
        node.style("background-color", getNodeColor(node.data()))
        dispatch(setTappedNodeId(null))
      } else {
        const prevTappedNode = cyInstance?.nodes()
          .filter((ele) => ele.id() == tappedNodeId)
          .first()
        if (prevTappedNode) {
          prevTappedNode.style("background-color", getNodeColor(prevTappedNode.data()))
        }

        node.style("background-color", "black")
        dispatch(setTappedNodeId(node.id()))
      }
    },
    [dispatch, cyInstance, tappedNodeId],
  )

  useEffect(() => {
    const cy = cytoscape({
      container: cyContainerRef.current,
      elements: compoundData,
      layout: { name: "grid" },
      style: [
        {
          selector: "node[label='Element']",
          style: {
            "background-color": ELEMENT_COLOR,
            "text-wrap": "wrap",
            "font-size": "12px",
            color: "#FFFFFF",
          },
        },
        {
          selector: "node[label='Compound']",
          style: {
            "background-color": COMPOUND_COLOR,
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
    if (cyInstance) {
      cyInstance.on("tap", "node", handleTapNode)
    }
  }, [cyInstance, handleTapNode])

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
