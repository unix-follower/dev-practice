package org.example.assistantonsbservlet.chemistry;

import org.example.assistantonsbservlet.api.chemistry.model.req.ChemistryCalculatorReq;
import org.example.assistantonsbservlet.api.chemistry.model.resp.ChemistryCalculatorResp;

public sealed interface CalculatorApiFacade permits CalculatorFacade {
    ChemistryCalculatorResp calculateMolarMass(ChemistryCalculatorReq body);
}
