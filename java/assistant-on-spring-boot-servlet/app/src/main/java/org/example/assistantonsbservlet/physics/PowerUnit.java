package org.example.assistantonsbservlet.physics;

import static org.example.assistantonsbservlet.math.MathCalc.Algebra.log;

public enum PowerUnit {
    PICOWATT, // pW, 10⁻¹² W
    NANOWATT, // nW, 10⁻⁹ W
    MICROWATT, // µW, 10⁻⁶ W
    MILLIWATT, // mW, 10⁻³ W
    WATT, // W
    KILOWATT, // kW, 10³ W
    MEGAWATT, // MW, 10⁶ W
    GIGAWATT, // GW, 10⁹ W
    TERAWATT, // TW, 10¹² W
    BRITISH_THERMAL_UNITS_PER_HOUR, // BTU/h
    MECHANICAL_HORSEPOWER, // hp(I)
    METRIC_HORSEPOWER, // hp(M)
    ELECTRIC_HORSEPOWER, // hp(E)
    AIR_HORSEPOWER, // ahp
    BOILER_HORSEPOWER, // hp(S)
    KILOCALORIES_PER_MINUTE, // kcal/min
    KILOCALORIES_PER_HOUR, // kcal/h
    ERGS_PER_SECOND, // erg/s
    TONS_OF_REFRIGERATION; // TR

    public static double milliwattsToWatts(double mW) {
        return mW / 1000;
    }

    public static double kilowattsToWatts(double kW) {
        return kW * 1000;
    }

    public static double mechanicalHorsepowerToWatts(double hp) {
        return hp * 745.7;
    }

    public static double wattsToMechanicalHorsepower(double watts) {
        return watts / 745.699872;
    }

    public static double btuhToWatts(double btuh) {
        return btuh / 3.412;
    }

    public static double decibelMilliwattToWatts(double dBmW) {
        return Math.pow(10, dBmW / 10.0) / 1000.0;
    }

    public static double wattsToDecibelMilliwatt(double watts) {
        return 10 * log(Math.abs(watts) / 0.001) / log(10);
    }

    public static double decibel(double inputPowerWatts, double outputPowerWatts) {
        return 10 * log(outputPowerWatts / inputPowerWatts);
    }

    public enum PWR {
        WATT_PER_KILOGRAM, // W/kg
        KILOWATT_PER_KILOGRAM, // kW/kg
        MECHANICAL_HORSEPOWER_PER_POUND // hp(I)/lb
    }
}
