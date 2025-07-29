package org.example.assistantonsbservlet.db.pubchem.repo;

import org.example.assistantonsbservlet.db.pubchem.model.FoodAdditiveSubstance;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class FoodAdditiveSubstanceRepositoryImpl implements FoodAdditiveSubstanceRepository {
    private final EntityManager em;

    public FoodAdditiveSubstanceRepositoryImpl(final EntityManager em) {
        this.em = em;
    }

    /**
     * @param page 1-based page
     */
    @Override
    public List<FoodAdditiveSubstance> findAll(final int page, final int limit) {
        return em.createQuery(
                "SELECT f FROM " + FoodAdditiveSubstance.class.getSimpleName() + " f",
                FoodAdditiveSubstance.class
            )
            .setFirstResult((page - 1) * limit)
            .setMaxResults(limit)
            .getResultList();
    }
}
