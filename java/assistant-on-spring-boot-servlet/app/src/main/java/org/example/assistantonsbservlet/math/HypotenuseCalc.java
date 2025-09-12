package org.example.assistantonsbservlet.math;

import org.example.assistantonsbservlet.api.math.model.CalculateHypotenuseReq;
import org.example.assistantonsbservlet.api.model.AngleUnit;
import org.example.assistantonsbservlet.api.model.resp.CalculatorScalarResponse;

public final class HypotenuseCalc implements HypotenuseCalculator {
    @Override
    public CalculatorScalarResponse calculate(CalculateHypotenuseReq request) {
        switch (request.solveFor()) {
            case Constants.SIDE_AND_OPPOSITE_ANGLE -> {
                return solveForSideAndOppositeAngle(request);
            }
            case Constants.SIDE_AND_ADJACENT_ANGLE -> {
                return solveForSideAndAdjacentAngle(request);
            }
            case Constants.AREA_AND_SIDE -> {
                return solveForAreaAndSide(request);
            }
            default -> throw new UnsupportedOperationException();
        }
    }

    private CalculatorScalarResponse solveForSideAndOppositeAngle(CalculateHypotenuseReq request) {
        final double oppositeSide;
        final double oppositeAngleInRadians;
        if (request.cathetusA() != null) {
            oppositeSide = request.cathetusA();
            oppositeAngleInRadians = AngleUnit.toRadians(request.angleAlpha(), request.alphaAngleUnit());
        } else {
            oppositeSide = request.cathetusB();
            oppositeAngleInRadians = AngleUnit.toRadians(request.angleBeta(), request.betaAngleUnit());
        }

        final double hypotenuse = oppositeSide / Math.sin(oppositeAngleInRadians);
        return new CalculatorScalarResponse(hypotenuse);
    }

    private CalculatorScalarResponse solveForSideAndAdjacentAngle(CalculateHypotenuseReq request) {
        final double adjacentSide;
        final double adjacentAngleInRadians;
        if (request.cathetusA() != null) {
            adjacentSide = request.cathetusA();
            adjacentAngleInRadians = AngleUnit.toRadians(request.angleBeta(), request.betaAngleUnit());
        } else {
            adjacentSide = request.cathetusB();
            adjacentAngleInRadians = AngleUnit.toRadians(request.angleAlpha(), request.alphaAngleUnit());
        }

        final double hypotenuse = adjacentSide / Math.cos(adjacentAngleInRadians);
        return new CalculatorScalarResponse(hypotenuse);
    }

    private CalculatorScalarResponse solveForAreaAndSide(CalculateHypotenuseReq request) {
        final double side;
        if (request.cathetusA() != null) {
            side = request.cathetusA();
        } else {
            side = request.cathetusB();
        }

        final double hypotenuse = Math.sqrt(Math.pow(side, 2) + Math.pow(request.area() * 2 / side, 2));
        return new CalculatorScalarResponse(hypotenuse);
    }
}
