package org.example.assistantonsbservlet.physics;

public enum PressureUnit {
    MICROPASCAL, // µPa
    MILLIPASCAL, // mPa
    PASCAL, // Pa
    MILLIBAR, // mb
    BAR, // bar
    POUNDS_PER_SQUARE_INCH, // psi
    TECHNICAL_ATMOSPHERE, // at
    STANDARD_ATMOSPHERE, // atm
    TORR, // Torr
    HECTOPASCAL, // hPa
    KILOPASCAL, // kPa
    MEGAPASCAL, // MPa
    GIGAPASCAL, // GPa
    INCH_OF_MERCURY, // inHg
    MILLIMETER_OF_MERCURY; // mmHg

    public static double hpaToPa(double hectopascals) {
        return hectopascals * 100;
    }

    public static double kpaToPa(double kilopascals) {
        return kilopascals / 1000;
    }

    public static double megaPaToPa(double megapascals) {
        return megapascals / 1_000_000;
    }

    public static double gpaToPa(double gigapascals) {
        return gigapascals / 1_000_000_000;
    }

    public static double pascalsToInchesOfWater(double pascals) {
        return pascals * 0.00401463;
    }
}
