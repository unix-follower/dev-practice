package org.example.assistantonsbservlet.api;

import java.util.Map;

public record Node(
    String id,
    String label,
    Map<String, Object> properties
) implements Comparable<Node> {
    @Override
    public int compareTo(Node node) {
        return id.compareTo(node.id());
    }
}
