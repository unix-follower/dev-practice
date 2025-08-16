package org.example.db.pubchem.fda.repo;

import org.example.db.pubchem.fda.model.FoodAdditiveSubstance;

import java.util.List;

public interface FoodAdditiveSubstanceRepository {
    List<FoodAdditiveSubstance> findAll(int offset, int limit);
}
