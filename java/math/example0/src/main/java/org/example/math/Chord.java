package org.example.math;

import java.util.function.DoubleFunction;

public class Chord {
    private Chord() {
    }

    public static double slopeAngle(double x1, double x2) {
        final DoubleFunction<Double> f = (double x) -> Math.pow(x, 2);

        final var x2Square = f.apply(x2);
        final var x1Square = f.apply(x1);

        final var denominatorSubtractionResult = x2 - x1;
        return (x2Square - x1Square) / denominatorSubtractionResult;
    }
}
