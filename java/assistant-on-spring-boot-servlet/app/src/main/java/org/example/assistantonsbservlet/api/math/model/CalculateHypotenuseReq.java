package org.example.assistantonsbservlet.api.math.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.assistantonsbservlet.api.model.AngleUnit;
import org.example.assistantonsbservlet.convert.AngleUnitsJsonDeserializer;

public record CalculateHypotenuseReq(
    String solveFor,
    Double cathetusA,
    Double cathetusB,
    Double angleAlpha,
    @JsonDeserialize(using = AngleUnitsJsonDeserializer.class)
    AngleUnit alphaAngleUnit,
    Double angleBeta,
    @JsonDeserialize(using = AngleUnitsJsonDeserializer.class)
    AngleUnit betaAngleUnit,
    Double area
) {
}
