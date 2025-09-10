package org.example.assistantonsbservlet.chemistry;

import org.example.assistantonsbservlet.api.chemistry.model.resp.ChemistryCalculatorResp;

public sealed interface MolarMassCalculator permits MolarMassCalc {
    String FORMULA_IN_TYPE = "formula";
    String SMILES_IN_TYPE = "smiles";

    void setStrategy(String strategy);

    void setInputType(String inputType);

    ChemistryCalculatorResp calculate(String input);
}
