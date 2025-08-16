package org.example.assistantonsbservlet.api;

import java.util.List;

public record ChemistryGraphResponse(
    long totalCompounds,
    long totalElements,
    long totalEdges,
    List<Node> nodes,
    List<Edge> edges
) {
}
