package org.example.assistantonsbservlet.physics;

public enum ElectricalChargeUnit {
    PICOCOULOMB, // pC
    NANOCOULOMB, // nC
    MICROCOULOMB, // μC
    MILLICOULOMB, // mC
    COULOMB, // C
    ELEMENTARY_CHARGE, // e
    AMPERE_HOUR, // Ah
    MILLIAMPERE_HOUR; // mAh

    public static final double ELECTRON_CHARGE = 1.602176634e-19; // In Coulombs

    public static double elementaryChargeToCoulomb(double charge) {
        return charge * ELECTRON_CHARGE;
    }

    public static double newtonPerCoulombToKiloNC(double chargeNC) {
        return chargeNC / 1000;
    }
}
