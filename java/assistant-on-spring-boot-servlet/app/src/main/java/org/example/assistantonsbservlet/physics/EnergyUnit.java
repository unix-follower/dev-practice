package org.example.assistantonsbservlet.physics;

public enum EnergyUnit {
    NANOJOULES, // nJ
    MICROJOULES, // μJ
    MILLIJOULES, // mJ
    JOULES, // J
    KILOJOULES, // kJ
    FOOT_POUNDS; // FT-LB

    public static final double ONE_JOULE_IN_NANOJOULES = 1e+9;

    public static double joulesToNanoJoules(double joules) {
        return joules * ONE_JOULE_IN_NANOJOULES;
    }
}
