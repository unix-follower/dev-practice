package org.example.assistantonsbservlet.chemistry;

import org.example.assistantonsbservlet.api.chemistry.model.req.ChemCalculateMoleReq;
import org.example.assistantonsbservlet.api.model.resp.CalculatorResponse;

public sealed interface MoleCalculator extends StoichiometryCalculator permits MoleCalc {
    void setInputType(String inputType);

    CalculatorResponse calculate(ChemCalculateMoleReq request);
}
