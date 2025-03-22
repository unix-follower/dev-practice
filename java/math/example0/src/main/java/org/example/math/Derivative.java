package org.example.math;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.FiniteDifferencesDifferentiator;

public class Derivative {
    private Derivative() {
    }

    public static Pair<Double, Double> xSquare(double x) {
        final UnivariateFunction f = x1 -> Math.pow(x1, 2);
        final var differentiator = new FiniteDifferencesDifferentiator(5, 0.01);
        final var differentiableFn = differentiator.differentiate(f);
        final var ds = differentiableFn.value(new DerivativeStructure(1, 1, 0, x));
        return Pair.of(ds.getValue(), ds.getPartialDerivative(1));
    }

    public static Pair<Double, Double> axInPowerN(double a, double n, double x) {
        final UnivariateFunction f = x1 -> a * Math.pow(x1, n);
        final var differentiator = new FiniteDifferencesDifferentiator(5, 0.01);
        final var differentiableFn = differentiator.differentiate(f);
        final var ds = differentiableFn.value(new DerivativeStructure(1, 1, 0, x));
        return Pair.of(ds.getValue(), ds.getPartialDerivative(1));
    }

    public static Pair<Double, Double> sinusoid(double x) {
        final UnivariateFunction f = Math::sin;
        final var differentiator = new FiniteDifferencesDifferentiator(5, 0.01);
        final var differentiableFn = differentiator.differentiate(f);
        final var ds = differentiableFn.value(new DerivativeStructure(1, 1, 0, x));
        return Pair.of(ds.getValue(), ds.getPartialDerivative(1));
    }
}
