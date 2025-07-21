package com.example.assistantonsbservlet.svc;

import com.example.assistantonsbservlet.api.pubchem.model.FoodAdditiveSubstanceResponseDto;

import java.util.List;

public interface FoodAdditiveSubstanceApiFacade {
    List<FoodAdditiveSubstanceResponseDto> getAll(int page, int size);
}
