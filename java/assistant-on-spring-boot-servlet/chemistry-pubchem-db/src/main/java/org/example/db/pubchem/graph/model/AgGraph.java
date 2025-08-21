package org.example.db.pubchem.graph.model;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;

import java.util.Objects;

@SqlResultSetMapping(
    name = AgGraph.FIND_ALL_RESULT,
    classes = {
        @ConstructorResult(
            targetClass = CompoundDataProjection.class,
            columns = {
                @ColumnResult(name = "total_compounds", type = Long.class),
                @ColumnResult(name = "total_elements", type = Long.class),
                @ColumnResult(name = "total_edges", type = Long.class),
                @ColumnResult(name = "compound", type = String.class),
                @ColumnResult(name = "element", type = String.class),
                @ColumnResult(name = "relationship", type = String.class)
            }
        )
    }
)
@Entity
@Table(name = "ag_graph")
public class AgGraph {
    public static final String FIND_ALL_RESULT = "findAllResult";

    @Id
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AgGraph agGraph)) {
            return false;
        }
        return Objects.equals(id, agGraph.id) && Objects.equals(name, agGraph.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
