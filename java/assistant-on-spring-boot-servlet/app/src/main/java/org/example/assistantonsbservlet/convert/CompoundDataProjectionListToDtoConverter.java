package org.example.assistantonsbservlet.convert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.assistantonsbservlet.api.ChemistryGraphResponse;
import org.example.assistantonsbservlet.api.Edge;
import org.example.assistantonsbservlet.api.Node;
import org.example.db.pubchem.graph.model.CompoundDataProjection;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

@Component
@WritingConverter
public class CompoundDataProjectionListToDtoConverter implements
    Converter<List<CompoundDataProjection>, ChemistryGraphResponse> {
    private final ObjectMapper mapper;

    public CompoundDataProjectionListToDtoConverter(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ChemistryGraphResponse convert(List<CompoundDataProjection> source) {
        if (source.isEmpty()) {
            final long total = 0;
            return new ChemistryGraphResponse(
                total, total, total, Collections.emptyList(), Collections.emptyList()
            );
        }

        final NavigableSet<Node> seenNodes = new TreeSet<>();
        final NavigableSet<Edge> seenEdges = new TreeSet<>();
        source.forEach(data -> {
            try {
                final var compoundJsonNode = mapper.readTree(data.compound());
                final var compoundNode = mapper.convertValue(compoundJsonNode, Node.class);
                seenNodes.add(compoundNode);

                final var elementJsonNode = mapper.readTree(data.element());
                final var elementNode = mapper.convertValue(elementJsonNode, Node.class);
                seenNodes.add(elementNode);

                final var relationshipJsonNode = mapper.readTree(data.relationship());
                final var relationship = mapper.convertValue(relationshipJsonNode, Edge.class);
                seenEdges.add(relationship);
            } catch (JsonProcessingException e) {
                throw new UncheckedIOException(e);
            }
        });
        final var dataProjection = source.getFirst();
        return new ChemistryGraphResponse(
            dataProjection.totalCompounds(),
            dataProjection.totalElements(),
            dataProjection.totalEdges(),
            seenNodes.stream().toList(),
            seenEdges.stream().toList()
        );
    }
}
