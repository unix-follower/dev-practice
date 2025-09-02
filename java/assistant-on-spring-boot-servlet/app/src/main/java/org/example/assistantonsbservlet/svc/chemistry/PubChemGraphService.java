package org.example.assistantonsbservlet.svc.chemistry;

import org.example.assistantonsbservlet.api.ChemistryGraphResponse;

public interface PubChemGraphService {
    ChemistryGraphResponse getAllGraphs(int page, int pageSize);

    ChemistryGraphResponse getCompoundDataByName(String compoundName, int page, int pageSize);
}
