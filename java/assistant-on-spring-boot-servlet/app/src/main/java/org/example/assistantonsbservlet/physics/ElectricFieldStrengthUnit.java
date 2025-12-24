package org.example.assistantonsbservlet.physics;

public enum ElectricFieldStrengthUnit {
    NEWTONS_PER_COULOMB, // N/C
    KILONEWTONS_PER_COULOMB, // kN/C
    MILLINEWTONS_PER_COULOMB, // mN/C
    MICRONEWTONS_PER_COULOMB, // μN/C
    NANONEWTONS_PER_COULOMB, // nN/C
    VOLTS_PER_METER; // V/m

    public static final short ONE_KILONEWTONS_PER_COULOMB_IN_NC = 1000;

    public static double kiloNCToNC(double kiloNewtonsPerCoulomb) {
        return kiloNewtonsPerCoulomb * ONE_KILONEWTONS_PER_COULOMB_IN_NC;
    }
}
