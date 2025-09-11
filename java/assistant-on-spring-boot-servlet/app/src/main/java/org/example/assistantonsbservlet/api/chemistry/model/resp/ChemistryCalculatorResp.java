package org.example.assistantonsbservlet.api.chemistry.model.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.example.assistantonsbservlet.convert.ScientificNotationSerializer;

public record ChemistryCalculatorResp(
    double result,
    @JsonSerialize(using = ScientificNotationSerializer.class)
    double resultScientificNotation
) {
    public ChemistryCalculatorResp(double result) {
        this(result, result);
    }
}
