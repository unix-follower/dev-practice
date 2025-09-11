package org.example.assistantonsbservlet.chemistry;

import org.example.assistantonsbservlet.api.chemistry.model.req.ChemCalculateMoleReq;
import org.example.assistantonsbservlet.api.chemistry.model.resp.ChemistryCalculatorResp;

public sealed interface MoleCalculator permits MoleCalc {
    void setInputType(String inputType);

    ChemistryCalculatorResp calculate(ChemCalculateMoleReq request);
}
