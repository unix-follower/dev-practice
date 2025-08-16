package org.example.assistantonsbservlet.api;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.Map;

public record Edge(
    String id,
    @JsonAlias("start_id")
    String source,
    @JsonAlias("end_id")
    String target,
    String label,
    Map<String, Object> properties
) implements Comparable<Edge> {
    @Override
    public int compareTo(Edge edge) {
        return id.compareTo(edge.id());
    }
}
