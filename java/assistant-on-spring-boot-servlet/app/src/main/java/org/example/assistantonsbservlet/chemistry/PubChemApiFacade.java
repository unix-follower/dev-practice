package org.example.assistantonsbservlet.chemistry;

import org.example.assistantonsbservlet.api.ChemistryGraphResponse;
import org.example.assistantonsbservlet.api.chemistry.model.CompoundSDFDataResponse;

public sealed interface PubChemApiFacade permits PubChemFacade {
    ChemistryGraphResponse getAllGraphs(int page, int pageSize);

    ChemistryGraphResponse getCompoundDataByName(String compoundName, int page, int pageSize);

    CompoundSDFDataResponse getCompoundSDFDataByCid(long cid);
}
