package org.example.assistantonsbservlet.physics;

public enum VoltageUnit {
    MILLIVOLT, // mV
    VOLT, // V
    KILOVOLT, // kV
    MEGAVOLT; // MV

    public static final int ONE_KILOVOLT_IN_VOLTS = 1000;

    public static double voltsToKilovolts(double volts) {
        return volts / ONE_KILOVOLT_IN_VOLTS;
    }

    public static double kilovoltsToVolts(double kilovolts) {
        return kilovolts * ONE_KILOVOLT_IN_VOLTS;
    }
}
