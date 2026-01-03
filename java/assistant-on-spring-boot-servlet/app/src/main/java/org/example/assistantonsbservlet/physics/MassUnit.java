package org.example.assistantonsbservlet.physics;

public enum MassUnit {
    MICROGRAM, // μg
    MILLIGRAM, // mg
    GRAM, // g
    DECAGRAM, // dag
    KILOGRAM, // kg
    METRIC_TON, // t
    GRAIN, // gr
    DRACHM, // dr
    OUNCE, // oz
    POUND, // lb
    STONE, // st
    US_SHORT_TON, // US ton
    IMPERIAL_TON, // long ton
    MASSES_OF_EARTH, // Earths
    ELECTRON_REST_MASS, // me
    ATOMIC_MASS_UNITS, // amu, u
    TROY_OUNCE; // oz t

    public static double grToKg(double grains) {
        return grains * 0.00006479891;
    }

    public static double gramsToKg(double grams) {
        return grams / 1000;
    }

    public static double kgToGrams(double kg) {
        return kg * 1000;
    }
}
