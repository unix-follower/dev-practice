package org.example.db.pubchem.graph;

import jakarta.persistence.EntityManager;
import org.example.db.pubchem.graph.model.AgGraph;
import org.example.db.pubchem.graph.model.CompoundDataProjection;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unchecked")
@Repository
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Lazy
public class CompoundRepositoryImpl implements CompoundRepository {
    private final EntityManager em;

    public CompoundRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<CompoundDataProjection> findAll(int offset, int limit) {
        return em.createNativeQuery(
                "SELECT * FROM find_all(:offset, :limit)",
                AgGraph.FIND_ALL_RESULT
            )
            .setParameter("offset", offset)
            .setParameter("limit", limit)
            .getResultList();
    }

    @Override
    public List<CompoundDataProjection> findCompoundDataByName(int offset, int limit, String compoundName) {
        return em.createNativeQuery(
                "SELECT * FROM get_compound_data_by_name(:name, :offset, :limit)",
                AgGraph.FIND_ALL_RESULT
            )
            .setParameter("name", compoundName)
            .setParameter("offset", offset)
            .setParameter("limit", limit)
            .getResultList();
    }
}
