package org.example.db.pubchem.fda.repo;

import jakarta.persistence.EntityManager;
import org.example.db.pubchem.fda.model.FoodAdditiveSubstance;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Lazy
public class FoodAdditiveSubstanceRepositoryImpl implements FoodAdditiveSubstanceRepository {
    private final EntityManager em;

    public FoodAdditiveSubstanceRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<FoodAdditiveSubstance> findAll(final int offset, final int limit) {
        return em.createQuery(
                "SELECT f FROM " + FoodAdditiveSubstance.class.getSimpleName() + " f",
                FoodAdditiveSubstance.class
            )
            .setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();
    }
}
