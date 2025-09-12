package org.example.assistantonsbservlet.chemistry;

import org.example.assistantonsbservlet.api.model.resp.CalculatorScalarResponse;

public sealed interface MolarMassCalculator extends GeneralChemCalculator permits MolarMassCalc {
    void setStrategy(String strategy);

    void setInputType(String inputType);

    CalculatorScalarResponse calculate(String input);
}
