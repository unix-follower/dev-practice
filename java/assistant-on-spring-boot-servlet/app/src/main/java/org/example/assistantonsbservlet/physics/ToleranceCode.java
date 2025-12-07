package org.example.assistantonsbservlet.physics;

public enum ToleranceCode {
    B(0.1, false), // ±0.1 pF
    C(0.25, false), // ±0.25 pF
    D(0.5, false), // ±0.5 pF
    F(0.01), // ±1%
    G(0.02), // ±2%
    J(0.05), // ±5%
    K(0.1), // ±10%
    M(0.2), // ±20%
    Z(-0.2, 0.8); // -20%, +80%

    private final double minTolerance;
    private final double maxTolerance;
    private final boolean tolerancePercentUnit;

    ToleranceCode(double tolerance) {
        this(-tolerance, tolerance, true);
    }

    ToleranceCode(double tolerance, boolean tolerancePercentUnit) {
        this(-tolerance, tolerance, tolerancePercentUnit);
    }

    ToleranceCode(double minTolerance, double maxTolerance) {
        this(minTolerance, maxTolerance, true);
    }

    ToleranceCode(double minTolerance, double maxTolerance, boolean tolerancePercentUnit) {
        this.minTolerance = minTolerance;
        this.maxTolerance = maxTolerance;
        this.tolerancePercentUnit = tolerancePercentUnit;
    }

    public double getMinTolerance() {
        return minTolerance;
    }

    public double getMaxTolerance() {
        return maxTolerance;
    }

    /**
     * @return the units are pF
     */
    public static double convertCodeToCapacity(int code) {
        final int capacity = code / 10;
        final int exponent = code % 10;
        return capacity * Math.pow(10, exponent);
    }

    public static double convertCapacityToCode(double capacityPF) {
        // Find exponent E such that C = capacityNanoFarads / 10^E is between 1 and 999
        int exponent = 0;
        double mantissa = capacityPF;
        while (mantissa >= 100) {
            mantissa /= 10;
            exponent++;
        }
        final int firstTwoDigits = (int) Math.round(mantissa);
        return firstTwoDigits * 10. + exponent;
    }

    public static double[] capacityToleranceRange(double capacityNF, ToleranceCode code) {
        if (code.tolerancePercentUnit) {
            return new double[]{
                capacityNF * (1 + code.minTolerance),
                capacityNF * (1 + code.maxTolerance)
            };
        }
        return new double[]{capacityNF - code.minTolerance, capacityNF + code.maxTolerance};
    }
}
