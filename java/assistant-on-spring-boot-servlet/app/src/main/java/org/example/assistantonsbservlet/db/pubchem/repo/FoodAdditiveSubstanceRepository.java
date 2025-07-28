package org.example.assistantonsbservlet.db.pubchem.repo;

import org.example.assistantonsbservlet.db.pubchem.model.FoodAdditiveSubstance;

import java.util.List;

public interface FoodAdditiveSubstanceRepository {
    List<FoodAdditiveSubstance> findAll(int page, int limit);
}
