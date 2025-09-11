package org.example.assistantonsbservlet.api.chemistry.model.req;

public record ChemCalculateMolarMassReq(
    String strategy,
    String formula,
    String smiles
) implements ChemCalculatorFormulaInput {
}
