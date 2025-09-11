package org.example.assistantonsbservlet.chemistry;

import org.example.assistantonsbservlet.api.model.resp.CalculatorResponse;

public sealed interface MolarMassCalculator extends GeneralChemCalculator permits MolarMassCalc {
    void setStrategy(String strategy);

    void setInputType(String inputType);

    CalculatorResponse calculate(String input);
}
