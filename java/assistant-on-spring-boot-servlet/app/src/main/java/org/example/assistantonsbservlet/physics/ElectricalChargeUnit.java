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

    public static double elementaryChargeToCoulomb(double charge) {
        return charge * 1.602176634e-19;
    }

    public static double newtonPerCoulombToKiloNC(double chargeNC) {
        return chargeNC / 1000;
    }
}
