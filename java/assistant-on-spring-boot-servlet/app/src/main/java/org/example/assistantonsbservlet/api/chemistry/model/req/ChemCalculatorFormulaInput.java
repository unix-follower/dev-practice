package org.example.assistantonsbservlet.api.chemistry.model.req;

public sealed interface ChemCalculatorFormulaInput permits ChemCalculateMolarMassReq, ChemCalculateMoleReq {
    String formula();
    String smiles();
}
