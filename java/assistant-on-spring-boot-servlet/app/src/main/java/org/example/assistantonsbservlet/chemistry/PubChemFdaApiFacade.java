package org.example.assistantonsbservlet.chemistry;

import org.example.assistantonsbservlet.api.chemistry.model.FoodAdditiveSubstanceResponseDto;

import java.util.List;

public sealed interface PubChemFdaApiFacade permits PubChemFdaFacade, PubChemFdaNoopFacade {
    /**
     * @param page 1-based page
     */
    List<FoodAdditiveSubstanceResponseDto> getAll(int page, int pageSize);
}
