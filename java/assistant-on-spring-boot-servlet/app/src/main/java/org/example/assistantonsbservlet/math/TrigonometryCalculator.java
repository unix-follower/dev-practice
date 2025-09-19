package org.example.assistantonsbservlet.math;

import org.example.assistantonsbservlet.api.math.model.CalculateCosineReq;
import org.example.assistantonsbservlet.api.math.model.CalculateSineReq;
import org.example.assistantonsbservlet.api.math.model.CalculateTanReq;
import org.example.assistantonsbservlet.api.model.resp.CalculatorScalarResponse;

public sealed interface TrigonometryCalculator permits TrigCalc {
    CalculatorScalarResponse calculate(CalculateCosineReq request);

    CalculatorScalarResponse calculate(CalculateSineReq request);

    CalculatorScalarResponse calculate(CalculateTanReq request);

    static double csc(double opposite, double hypotenuse) {
        return hypotenuse / opposite;
    }

    static double csc(double theta) {
        final double sinResult = Math.sin(theta);
        if (sinResult == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return 1 / sinResult;
    }

    static double sec(double adjacent, double hypotenuse) {
        return hypotenuse / adjacent;
    }

    static double sec(double theta) {
        final double cosResult = Math.cos(theta);
        if (cosResult == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return 1 / cosResult;
    }

    static double cot(double adjacent, double opposite) {
        return adjacent / opposite;
    }

    static double cot(double theta) {
        final double tanResult = Math.tan(theta);
        if (tanResult == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return 1 / tanResult;
    }

    static double sinDoubleAngle(double theta) {
        return 2 * Math.sin(theta) * Math.cos(theta);
    }

    static double cosDoubleAngle(double theta) {
        return Math.cos(theta) * Math.cos(theta) - Math.sin(theta) * Math.sin(theta);
    }

    static double tanDoubleAngle(double theta) {
        return (2 * Math.tan(theta)) / (1 - Math.tan(theta) * Math.tan(theta));
    }

    static double sinHalfAngle(double theta) {
        return Math.sqrt((1 - Math.cos(theta)) / 2);
    }

    static double cosHalfAngle(double theta) {
        return Math.sqrt((1 + Math.cos(theta)) / 2);
    }

    static double tanHalfAngle(double theta) {
        return Math.sqrt((1 - Math.cos(theta)) / (1 + Math.cos(theta)));
    }

    static double sinTripleAngleIdentity(double theta) {
        return 3 * Math.sin(theta) - 4 * Math.pow(Math.sin(theta), 3);
    }

    static double cosTripleAngleIdentity(double theta) {
        return 4 * Math.pow(Math.cos(theta), 3) - 3 * Math.cos(theta);
    }

    static double tanTripleAngleIdentity(double theta) {
        final double numerator = 3 * Math.tan(theta) - Math.pow(Math.tan(theta), 3);
        final double denominator = 1 - 3 * Math.tan(theta) * Math.tan(theta);
        if (denominator == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return numerator / denominator;
    }

    static double lawOfCosine(double sideA, double sideB, double angle) {
        return Math.sqrt(sideA * sideA + sideB * sideB - 2 * sideA * sideB * Math.cos(angle));
    }

    static double sinAngleSum(double angleAlpha, double angleBeta) {
        return Math.sin(angleAlpha) * Math.cos(angleBeta) + Math.cos(angleAlpha) * Math.sin(angleBeta);
    }

    static double sinAngleSubtract(double angleAlpha, double angleBeta) {
        return Math.sin(angleAlpha) * Math.cos(angleBeta) - Math.cos(angleAlpha) * Math.sin(angleBeta);
    }

    static double cosAngleSum(double angleAlpha, double angleBeta) {
        return Math.cos(angleAlpha) * Math.cos(angleBeta) - Math.sin(angleAlpha) * Math.sin(angleBeta);
    }

    static double cosAngleSubtract(double angleAlpha, double angleBeta) {
        return Math.cos(angleAlpha) * Math.cos(angleBeta) + Math.sin(angleAlpha) * Math.sin(angleBeta);
    }

    static double tangentAngleSum(double angleAlpha, double angleBeta) {
        final double denominator = 1 - Math.tan(angleAlpha) * Math.tan(angleBeta);
        if (denominator == 0) {
            throw new ArithmeticException("Division by zero");
        }
        final double numerator = Math.tan(angleAlpha) + Math.tan(angleBeta);
        return numerator / denominator;
    }

    static double tanAngleSubtract(double angleAlpha, double angleBeta) {
        final double denominator = 1 + Math.tan(angleAlpha) * Math.tan(angleBeta);
        if (denominator == 0) {
            throw new ArithmeticException("Division by zero");
        }
        final double numerator = Math.tan(angleAlpha) - Math.tan(angleBeta);
        return numerator / denominator;
    }

    static double cotangentAngleSum(double angleAlpha, double angleBeta) {
        final double cotAlpha = cot(angleAlpha);
        final double cotBeta = cot(angleBeta);
        final double denominator = cotAlpha + cotBeta;
        if (denominator == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return (cotAlpha * cotBeta - 1) / denominator;
    }

    static double cotangentAngleSubtract(double angleAlpha, double angleBeta) {
        final double cotAlpha = cot(angleAlpha);
        final double cotBeta = cot(angleBeta);
        final double denominator = cotAlpha - cotBeta;
        if (denominator == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return (cotAlpha * cotBeta + 1) / denominator;
    }

    static double secantAngleSum(double angleAlpha, double angleBeta) {
        final double denominator = cot(angleAlpha) * Math.tan(angleBeta);
        if (denominator == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return 1 / (Math.cos(angleAlpha) * Math.cos(angleBeta) / denominator);
    }

    static double secantAngleSubtract(double angleAlpha, double angleBeta) {
        final double denominator = cot(angleAlpha) * Math.tan(angleBeta);
        if (denominator == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return 1 / (Math.cos(angleAlpha) * Math.cos(angleBeta) / denominator);
    }

    static double cosecantAngleSum(double angleAlpha, double angleBeta) {
        final double denominator = Math.sin(angleAlpha) * Math.cos(angleBeta)
            + Math.cos(angleAlpha) * Math.sin(angleBeta);
        if (denominator == 0) {
            throw new ArithmeticException("Division by zero");
        }
        final double numerator = Math.sin(angleAlpha) * Math.sin(angleBeta);
        return numerator / denominator;
    }

    static double cosecantAngleSubtract(double angleAlpha, double angleBeta) {
        final double denominator = Math.sin(angleAlpha) * Math.cos(angleBeta)
            - Math.cos(angleAlpha) * Math.sin(angleBeta);
        if (denominator == 0) {
            throw new ArithmeticException("Division by zero in cosecantAngleSubtract");
        }
        final double numerator = Math.sin(angleAlpha) * Math.sin(angleBeta);
        return numerator / denominator;
    }
}
