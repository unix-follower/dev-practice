package org.example.assistantonsbservlet.chemistry;

import org.example.assistantonsbservlet.api.chemistry.model.req.ChemCalculateMolarMassReq;
import org.example.assistantonsbservlet.api.chemistry.model.req.ChemCalculateMoleReq;
import org.example.assistantonsbservlet.api.model.resp.CalculatorScalarResponse;

public sealed interface ChemCalculatorApiFacade permits ChemCalculatorFacade {
    CalculatorScalarResponse calculate(ChemCalculateMolarMassReq body);

    CalculatorScalarResponse calculate(ChemCalculateMoleReq body);
}
