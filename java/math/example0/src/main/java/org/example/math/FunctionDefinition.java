package org.example.math;

import java.util.function.DoubleUnaryOperator;

public class FunctionDefinition {
    private FunctionDefinition() {
    }

    public static double fDefinition(double number) {
        final DoubleUnaryOperator f = (double x) -> 3 * Math.pow(x, 2) + 2 * x - 5;
        return f.applyAsDouble(number);
    }
}
