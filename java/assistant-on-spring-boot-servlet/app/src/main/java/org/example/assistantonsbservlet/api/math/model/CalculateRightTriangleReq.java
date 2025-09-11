package org.example.assistantonsbservlet.api.math.model;

public record CalculateRightTriangleReq(
    Double cathetusA,
    Double cathetusB,
    Double hypotenuse
) {
}
