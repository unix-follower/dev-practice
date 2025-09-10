package org.example.assistantonsbservlet.api.chemistry.model.req;

public record ChemistryCalculatorReq(
    String strategy,
    String formula,
    String smiles
) {
}
