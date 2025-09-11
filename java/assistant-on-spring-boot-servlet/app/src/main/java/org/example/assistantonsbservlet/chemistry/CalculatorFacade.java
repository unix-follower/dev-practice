package org.example.assistantonsbservlet.chemistry;

import org.example.assistantonsbservlet.api.chemistry.model.req.ChemCalculateMolarMassReq;
import org.example.assistantonsbservlet.api.chemistry.model.req.ChemCalculateMoleReq;
import org.example.assistantonsbservlet.api.chemistry.model.resp.ChemistryCalculatorResp;
import org.springframework.stereotype.Component;

@Component
public final class CalculatorFacade implements CalculatorApiFacade {
    @Override
    public ChemistryCalculatorResp calculateMolarMass(ChemCalculateMolarMassReq body) {
        final var calc = new MolarMassCalc();
        calc.setStrategy(body.strategy());
        if (body.formula() != null) {
            calc.setInputType(Constants.FORMULA_IN_TYPE);
            return calc.calculate(body.formula());
        } else {
            calc.setInputType(Constants.SMILES_IN_TYPE);
            return calc.calculate(body.smiles());
        }
    }

    @Override
    public ChemistryCalculatorResp calculateMole(ChemCalculateMoleReq body) {
        final var calc = new MoleCalc();
        if (body.formula() != null) {
            calc.setInputType(Constants.FORMULA_IN_TYPE);
        } else if (body.smiles() != null) {
            calc.setInputType(Constants.SMILES_IN_TYPE);
        } else {
            calc.setInputType(Constants.UNKNOWN_SUBSTANCE);
        }
        return calc.calculate(body);
    }
}
