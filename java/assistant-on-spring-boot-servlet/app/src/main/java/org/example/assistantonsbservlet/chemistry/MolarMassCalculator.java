package org.example.assistantonsbservlet.chemistry;

import org.example.assistantonsbservlet.api.chemistry.model.resp.ChemistryCalculatorResp;

public sealed interface MolarMassCalculator permits MolarMassCalc {
    void setStrategy(String strategy);

    void setInputType(String inputType);

    ChemistryCalculatorResp calculate(String input);
}
