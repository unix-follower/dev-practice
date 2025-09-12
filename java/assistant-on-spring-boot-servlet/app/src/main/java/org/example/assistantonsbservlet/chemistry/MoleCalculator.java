package org.example.assistantonsbservlet.chemistry;

import org.example.assistantonsbservlet.api.chemistry.model.req.ChemCalculateMoleReq;
import org.example.assistantonsbservlet.api.model.resp.CalculatorScalarResponse;

public sealed interface MoleCalculator extends StoichiometryCalculator permits MoleCalc {
    void setInputType(String inputType);

    CalculatorScalarResponse calculate(ChemCalculateMoleReq request);
}
