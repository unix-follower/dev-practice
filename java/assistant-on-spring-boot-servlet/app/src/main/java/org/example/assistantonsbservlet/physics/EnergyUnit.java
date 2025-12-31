package org.example.assistantonsbservlet.physics;

public enum EnergyUnit {
    NANOJOULE, // nJ
    MICROJOULE, // μJ
    MILLIJOULE, // mJ
    JOULE, // J
    KILOJOULE, // kJ
    MEGAJOULE, // MJ
    WATT_HOUR, // Wh
    KILOWATT_HOUR, // kWh
    FOOT_POUND, // ft-lb
    KILOCALORIE, // kcal
    ELECTRONVOLT, // eV
    TON_OF_TNT; // t TNT

    public static final double ONE_JOULE_IN_NANOJOULES = 1e+9;

    public static double joulesToNanoJoules(double joules) {
        return joules * ONE_JOULE_IN_NANOJOULES;
    }
}
