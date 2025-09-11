package org.example.assistantonsbservlet.api.model.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.example.assistantonsbservlet.convert.ScientificNotationSerializer;

public record CalculatorResponse(
    double result,
    @JsonSerialize(using = ScientificNotationSerializer.class)
    double resultScientificNotation
) {
    public CalculatorResponse(double result) {
        this(result, result);
    }
}
