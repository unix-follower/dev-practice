package com.example.assistantonsbservlet.db.pubchem.repo;

import com.example.assistantonsbservlet.db.pubchem.model.FoodAdditiveSubstance;

import java.util.List;

public interface FoodAdditiveSubstanceRepository {
    List<FoodAdditiveSubstance> findAll(int page, int limit);
}
