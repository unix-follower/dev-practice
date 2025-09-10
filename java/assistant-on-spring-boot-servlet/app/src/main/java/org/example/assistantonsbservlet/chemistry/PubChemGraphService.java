package org.example.assistantonsbservlet.chemistry;

import org.example.assistantonsbservlet.api.ChemistryGraphResponse;

public sealed interface PubChemGraphService permits PubChemGraphServiceImpl, PubChemGraphNoopService {
    ChemistryGraphResponse getAllGraphs(int page, int pageSize);

    ChemistryGraphResponse getCompoundDataByName(String compoundName, int page, int pageSize);
}
