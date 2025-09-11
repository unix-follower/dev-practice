package org.example.assistantonsbservlet.chemistry;

import org.example.assistantonsbservlet.api.chemistry.model.req.ChemCalculateMolarMassReq;
import org.example.assistantonsbservlet.api.chemistry.model.req.ChemCalculateMoleReq;
import org.example.assistantonsbservlet.api.model.resp.CalculatorResponse;

public sealed interface ChemCalculatorApiFacade permits ChemCalculatorFacade {
    CalculatorResponse calculate(ChemCalculateMolarMassReq body);

    CalculatorResponse calculate(ChemCalculateMoleReq body);
}
