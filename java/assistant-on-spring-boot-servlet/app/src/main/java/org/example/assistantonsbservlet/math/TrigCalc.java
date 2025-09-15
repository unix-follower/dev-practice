package org.example.assistantonsbservlet.math;

import org.example.assistantonsbservlet.api.math.model.CalculateCosineReq;
import org.example.assistantonsbservlet.api.math.model.CalculateSineReq;
import org.example.assistantonsbservlet.api.math.model.CalculateTanReq;
import org.example.assistantonsbservlet.api.model.AngleUnit;
import org.example.assistantonsbservlet.api.model.resp.CalculatorScalarResponse;

import java.util.function.DoubleSupplier;

public final class TrigCalc implements TrigonometryCalculator {
    @Override
    public CalculatorScalarResponse calculate(CalculateCosineReq request) {
        final String solveFor = request.solveFor() != null ? request.solveFor() : Constants.ANGLE;
        switch (solveFor) {
            case Constants.COSINE -> {
                return solveForInverse(request.resultUnit(), () -> Math.acos(request.cosine()));
            }
            case Constants.SIDES -> {
                final double result = request.adjacent() / request.hypotenuse();
                return new CalculatorScalarResponse(result);
            }
            default -> {
                return solveForAngle(request);
            }
        }
    }

    @Override
    public CalculatorScalarResponse calculate(CalculateSineReq request) {
        final String solveFor = request.solveFor() != null ? request.solveFor() : Constants.ANGLE;
        switch (solveFor) {
            case Constants.SINE -> {
                return solveForInverse(request.resultUnit(), () -> Math.asin(request.sine()));
            }
            case Constants.SIDES -> {
                final double result = request.opposite() / request.hypotenuse();
                return new CalculatorScalarResponse(result);
            }
            default -> {
                return solveForAngle(request);
            }
        }
    }

    @Override
    public CalculatorScalarResponse calculate(CalculateTanReq request) {
        final String solveFor = request.solveFor() != null ? request.solveFor() : Constants.ANGLE;
        switch (solveFor) {
            case Constants.TAN -> {
                return solveForInverse(request.resultUnit(), () -> Math.atan(request.tan()));
            }
            case Constants.SIDES -> {
                final double result = request.opposite() / request.adjacent();
                return new CalculatorScalarResponse(result);
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

    private CalculatorScalarResponse solveForAngle(CalculateSineReq request) {
        final double angleInRadians = AngleUnit.toRadians(request.angleAlpha(), request.alphaAngleUnit());
        final double result = Math.sin(angleInRadians);
        return new CalculatorScalarResponse(result);
    }

    private CalculatorScalarResponse solveForInverse(AngleUnit unit, DoubleSupplier inverseSupplier) {
        final var resultUnit = AngleUnit.getResultUnitOrRadians(unit);
        double result = inverseSupplier.getAsDouble();
        result = adjustUnits(resultUnit, result);
        return new CalculatorScalarResponse(result, resultUnit);
    }

    private CalculatorScalarResponse solveForAngle(CalculateTanReq request) {
        final double angleInRadians = AngleUnit.toRadians(request.angleAlpha(), request.alphaAngleUnit());
        final double result = Math.tan(angleInRadians);
        return new CalculatorScalarResponse(result);
    }
}
