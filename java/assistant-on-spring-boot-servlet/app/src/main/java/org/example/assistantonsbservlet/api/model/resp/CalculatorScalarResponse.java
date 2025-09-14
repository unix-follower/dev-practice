package org.example.assistantonsbservlet.api.model.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.example.assistantonsbservlet.convert.ScientificNotationJsonSerializer;

public record CalculatorScalarResponse(
    double result,
    Enum<?> unit,
    @JsonSerialize(using = ScientificNotationJsonSerializer.class)
    double resultScientificNotation
) {
    public CalculatorScalarResponse(double result) {
        this(result, null, result);
    }
    public CalculatorScalarResponse(double result, Enum<?> unit) {
        this(result, unit, result);
    }
}
