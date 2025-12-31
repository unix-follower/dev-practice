package org.example.assistantonsbservlet.physics;

public enum ForceUnit {
    NEWTON, // N
    KILONEWTON, // kN
    MEGANEWTON, // MN
    GIGANEWTON, // GN
    TERANEWTON, // TN
    POUND_FORCE; // lbf

    public static final short ONE_KILONEWTONS_PER_COULOMB_IN_NC = 1000;

    public static double kiloNCToNC(double kiloNewtonsPerCoulomb) {
        return kiloNewtonsPerCoulomb * ONE_KILONEWTONS_PER_COULOMB_IN_NC;
    }
}
