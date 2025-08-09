package org.example.assistantonsbservlet.svc.chemistry;

import org.example.assistantonsbservlet.api.chemistry.organic.dbs.pubchem.fda.model.FoodAdditiveSubstanceResponseDto;

import java.util.List;

public interface PubChemFdaApiFacade {
    /**
     * @param page 1-based page
     */
    List<FoodAdditiveSubstanceResponseDto> getAll(int page, int pageSize);
}
