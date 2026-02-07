package org.example.assistantonsbservlet.physics;

public enum MassUnit {
    PICOGRAM, // pg
    NANOGRAM, // ng
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
    SOLAR, // masses of the Sun, Suns
    ELECTRON_REST_MASS, // me
    PROTON_REST_MASS, // mp
    NEUTRON_REST_MASS, // nm
    ATOMIC_MASS_UNITS, // amu, u
    TROY_OUNCE; // oz t

    public enum Molar {
        GRAMS_PER_MOLE, // g/mol
        KILOGRAMS_PER_MOLE, // kg/mol
        KILOGRAMS_PER_KILOMOLE, // kg/kmol
        POUNDS_PER_POUND_MOLE // lb/lbmol
    }

    public static final double SOLAR_KG = 1.989e30;
    public static final double EARTH_KG = 5.9722e24;

    public static double grToKg(double grains) {
        return grains * 0.00006479891;
    }

    public static double gramsToKg(double grams) {
        return grams / 1000;
    }

    public static double kgToGrams(double kg) {
        return kg * 1000;
    }

    public static double ergsToBethe(double ergs) {
        return ergs / 1e51;
    }

    public static double amuToKg(double atomicMassUnits) {
        return atomicMassUnits * 1.66053906660e-27;
    }

    public static double meToKg(double electronRestMass) {
        return electronRestMass / 1.098e30;
    }

    public static double earthsToKg(double earths) {
        return earths * EARTH_KG;
    }

    public static double sunsToKg(double suns) {
        return suns * SOLAR_KG;
    }
}
