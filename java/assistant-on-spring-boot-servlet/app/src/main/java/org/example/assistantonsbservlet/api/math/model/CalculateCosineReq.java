package org.example.assistantonsbservlet.api.math.model;

import org.example.assistantonsbservlet.api.model.AngleUnit;

public record CalculateCosineReq(
    String solveFor,
    AngleUnit resultUnit,
    Double angleAlpha,
    AngleUnit alphaAngleUnit,
    Double cosine,
    Double adjacent,
    Double hypotenuse
) {
}
