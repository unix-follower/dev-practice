package org.example.assistantonsbservlet.chemistry;

import org.example.assistantonsbservlet.api.chemistry.model.req.ChemistryCalculatorReq;
import org.example.assistantonsbservlet.api.chemistry.model.resp.ChemistryCalculatorResp;
import org.springframework.stereotype.Component;

@Component
public final class CalculatorFacade implements CalculatorApiFacade {
    @Override
    public ChemistryCalculatorResp calculateMolarMass(ChemistryCalculatorReq body) {
        final var calc = new MolarMassCalc();
        calc.setStrategy(body.strategy());
        if (body.formula() != null) {
            calc.setInputType(MolarMassCalculator.FORMULA_IN_TYPE);
            return calc.calculate(body.formula());
        } else {
            calc.setInputType(MolarMassCalculator.SMILES_IN_TYPE);
            return calc.calculate(body.smiles());
        }
    }
}
