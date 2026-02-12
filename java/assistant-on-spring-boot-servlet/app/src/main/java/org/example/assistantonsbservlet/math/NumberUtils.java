package org.example.assistantonsbservlet.math;

public final class NumberUtils {
    private NumberUtils() {
    }

    public static void checkGreater0(double value) {
        final int inclusiveBound = 0;
        if (value <= inclusiveBound) {
            checkGreater(value, inclusiveBound);
        }
    }

    public static void checkGreater(double value, double inclusiveBound) {
        if (value <= inclusiveBound) {
            throw new IllegalArgumentException("This value must be > " + inclusiveBound);
        }
    }

    public static void checkLessOrEq(double value, double inclusiveBound) {
        if (value > inclusiveBound) {
            throw new IllegalArgumentException("This value must be <= " + inclusiveBound);
        }
    }
}
