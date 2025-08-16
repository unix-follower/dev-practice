export interface Node {
  id: string
  label: string
  properties: { [key: string]: unknown }
}

export interface Edge {
  id: string
  source: string
  target: string
  label: string
  properties: { [key: string]: unknown }
}

export interface ChemistryGraphResponse {
  totalCompounds: number
  totalElements: number
  totalEdges: number
  nodes: Node[]
  edges: Edge[]
}
