package org.example.assistantonsbservlet.chemistry;

import org.example.assistantonsbservlet.api.chemistry.model.req.ChemCalculateMolarMassReq;
import org.example.assistantonsbservlet.api.chemistry.model.req.ChemCalculateMoleReq;
import org.example.assistantonsbservlet.api.chemistry.model.resp.ChemistryCalculatorResp;

public sealed interface CalculatorApiFacade permits CalculatorFacade {
    ChemistryCalculatorResp calculateMolarMass(ChemCalculateMolarMassReq body);

    ChemistryCalculatorResp calculateMole(ChemCalculateMoleReq body);
}
