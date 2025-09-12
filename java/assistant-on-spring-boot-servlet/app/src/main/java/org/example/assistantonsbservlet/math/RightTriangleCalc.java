package org.example.assistantonsbservlet.math;

import org.example.assistantonsbservlet.api.math.model.CalculateRightTriangleReq;
import org.example.assistantonsbservlet.api.model.resp.CalculatorResponse;

public final class RightTriangleCalc implements RightTriangleCalculator {
    @Override
    public CalculatorResponse calculate(CalculateRightTriangleReq request) {
        if (request.cathetusA() != null && request.cathetusB() != null) {
            return solveForSides(request);
        } else if (request.cathetusA() != null) {
            return solveForSideAndHypotenuse(request.cathetusA(), request.hypotenuse());
        } else {
            return solveForSideAndHypotenuse(request.cathetusB(), request.hypotenuse());
        }
    }

    private CalculatorResponse solveForSides(CalculateRightTriangleReq request) {
        final double sumOfSquares = Math.pow(request.cathetusA(), 2) + Math.pow(request.cathetusB(), 2);
        final double hypotenuse = Math.sqrt(sumOfSquares);
        return new CalculatorResponse(hypotenuse);
    }

    private CalculatorResponse solveForSideAndHypotenuse(double side, double hypotenuse) {
        final double unknownSide = Math.sqrt(Math.pow(hypotenuse, 2) - Math.pow(side, 2));
        return new CalculatorResponse(unknownSide);
    }
}
