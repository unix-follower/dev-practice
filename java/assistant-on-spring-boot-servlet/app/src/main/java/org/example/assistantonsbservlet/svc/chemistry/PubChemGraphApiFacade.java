package org.example.assistantonsbservlet.svc.chemistry;

import org.example.assistantonsbservlet.api.ChemistryGraphResponse;

public interface PubChemGraphApiFacade {
    ChemistryGraphResponse getAll(int page, int pageSize);

    ChemistryGraphResponse getCompoundDataByName(int page, int pageSize, String compoundName);
}
