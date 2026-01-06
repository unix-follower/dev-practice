package org.example.assistantonsbservlet.physics;

public enum EnergyUnit {
    NANOJOULE, // nJ
    MICROJOULE, // μJ
    MILLIJOULE, // mJ
    JOULE, // J
    KILOJOULE, // kJ
    MEGAJOULE, // MJ
    MICROELECTRON_VOLT, // μeV
    MILLIELECTRON_VOLT, // meV
    ELECTRON_VOLT, // eV
    KILOELECTRON_VOLT, // keV
    WATT_HOUR, // Wh
    KILOWATT_HOUR, // kWh
    FOOT_POUND, // ft-lb
    CALORIE, // cal
    KILOCALORIE, // kcal
    TON_OF_TNT; // t TNT

    public static final double ONE_JOULE_IN_NANOJOULES = 1e+9;

    public static double joulesToNanoJoules(double joules) {
        return joules * ONE_JOULE_IN_NANOJOULES;
    }
}
