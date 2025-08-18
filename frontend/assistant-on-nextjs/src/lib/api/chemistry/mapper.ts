import { ChemistryGraphResponse } from "@/lib/api/chemistry/graphModels"
import { ElementDefinition } from "cytoscape"

export function mapGraphResponse(graphResponse: ChemistryGraphResponse) {
  const elements: ElementDefinition[] = graphResponse.nodes.map((node) => ({
    group: "nodes",
    data: {
      id: node.id,
      label: node.label,
      ...node.properties,
    },
  }))
  const edges: ElementDefinition[] = graphResponse.edges.map((edge) => ({
    group: "edges",
    data: {
      id: edge.id,
      label: edge.label,
      source: edge.source,
      target: edge.target,
      ...edge.properties,
    },
  }))
  elements.push(...edges)
  return elements
}
