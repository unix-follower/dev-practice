package org.example.assistantonsbservlet.api.chemistry.model.req;

public record ChemCalculateMoleReq(
    String strategy,
    String formula,
    String smiles,
    Double mass,
    Double molecularWeight
) implements ChemCalculatorFormulaInput {
}
