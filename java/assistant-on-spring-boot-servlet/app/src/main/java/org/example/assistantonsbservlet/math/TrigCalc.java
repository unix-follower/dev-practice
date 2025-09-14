package org.example.assistantonsbservlet.math;

import org.example.assistantonsbservlet.api.math.model.CalculateCosineReq;
import org.example.assistantonsbservlet.api.model.AngleUnit;
import org.example.assistantonsbservlet.api.model.resp.CalculatorScalarResponse;

public final class TrigCalc implements TrigonometryCalculator {
    @Override
    public CalculatorScalarResponse calculate(CalculateCosineReq request) {
        final String solveFor = request.solveFor() != null ? request.solveFor() : "";
        switch (solveFor) {
            case Constants.COSINE -> {
                return solveForCosine(request);
            }
            case Constants.SIDES -> {
                return solveForSides(request);
            }
            default -> {
                return solveForAngle(request);
            }
        }
    }

    private CalculatorScalarResponse solveForAngle(CalculateCosineReq request) {
        final double angleInRadians = AngleUnit.toRadians(request.angleAlpha(), request.alphaAngleUnit());
        final double result = Math.cos(angleInRadians);
        return new CalculatorScalarResponse(result);
    }

    private static double adjustUnits(AngleUnit resultUnit, double result) {
        if (AngleUnit.DEGREES == resultUnit) {
            result = Math.toDegrees(result);
        }
        return result;
    }

    private CalculatorScalarResponse solveForCosine(CalculateCosineReq request) {
        final var resultUnit = AngleUnit.getResultUnitOrRadians(request.resultUnit());
        double result = Math.acos(request.cosine());
        result = adjustUnits(resultUnit, result);
        return new CalculatorScalarResponse(result, resultUnit);
    }

    private CalculatorScalarResponse solveForSides(CalculateCosineReq request) {
        final double result = request.adjacent() / request.hypotenuse();
        return new CalculatorScalarResponse(result);
    }
}
