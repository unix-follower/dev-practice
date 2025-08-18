package org.example.db.pubchem.graph;

import org.example.db.pubchem.graph.model.CompoundDataProjection;

import java.util.List;

public interface CompoundRepository {
    List<CompoundDataProjection> findAll(int offset, int limit);

    List<CompoundDataProjection> findCompoundDataByName(int offset, int limit, String compoundName);
}
