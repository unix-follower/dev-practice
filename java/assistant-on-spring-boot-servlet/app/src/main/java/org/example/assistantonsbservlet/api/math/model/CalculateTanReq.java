package org.example.assistantonsbservlet.api.math.model;

import org.example.assistantonsbservlet.api.model.AngleUnit;

public record CalculateTanReq(
    String solveFor,
    AngleUnit resultUnit,
    Double angleAlpha,
    AngleUnit alphaAngleUnit,
    Double tan,
    Double opposite,
    Double adjacent
) {
}
