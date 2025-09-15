package org.example.assistantonsbservlet.api.math.model;

import org.example.assistantonsbservlet.api.model.AngleUnit;

public record CalculateSineReq(
    String solveFor,
    AngleUnit resultUnit,
    Double angleAlpha,
    AngleUnit alphaAngleUnit,
    Double sine,
    Double opposite,
    Double hypotenuse
) {
}
