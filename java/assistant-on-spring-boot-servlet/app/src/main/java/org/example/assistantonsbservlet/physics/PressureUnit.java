package org.example.assistantonsbservlet.physics;

public enum PressureUnit {
    MICROPASCAL, // µPa
    MILLIPASCAL, // mPa
    PASCAL, // Pa
    KILOPASCAL, // kPa
    HECTOPASCAL, // hPa
    MEGAPASCAL, // MPa
    GIGAPASCAL, // GPa
    MILLIBAR, // mb
    BAR, // bar
    POUNDS_PER_SQUARE_INCH, // psi
    KILOPOUNDS_PER_SQUARE_INCH, // ksi
    TECHNICAL_ATMOSPHERE, // at
    STANDARD_ATMOSPHERE, // atm
    TORR, // Torr
    INCH_OF_MERCURY, // inHg
    MILLIMETER_OF_MERCURY; // mmHg

    public static final int ATMOSPHERIC_PRESSURE = 101_325;

    public static double hpaToPa(double hectopascals) {
        return hectopascals * 100;
    }

    public static double kpaToPa(double kilopascals) {
        return kilopascals * 1000;
    }

    public static double megaPaToPa(double megapascals) {
        return megapascals / 1_000_000;
    }

    public static double megaPaToGPa(double megapascals) {
        return megapascals / 1_000;
    }

    public static double paToMicroPa(double pascals) {
        return pascals * 1e6;
    }

    public static double gpaToPa(double gigapascals) {
        return gigapascals / 1_000_000_000;
    }

    public static double gpaToMPa(double gigapascals) {
        return gigapascals * 1_000;
    }

    public static double pascalsToInchesOfWater(double pascals) {
        return pascals * 0.00401463;
    }
}
