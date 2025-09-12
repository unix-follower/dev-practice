package org.example.assistantonsbservlet.api.model.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.example.assistantonsbservlet.convert.ScientificNotationJsonSerializer;

public record CalculatorScalarResponse(
    double result,
    @JsonSerialize(using = ScientificNotationJsonSerializer.class)
    double resultScientificNotation
) {
    public CalculatorScalarResponse(double result) {
        this(result, result);
    }
}
