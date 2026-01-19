package org.example.assistantonsbservlet.physics;

public enum ElectricCurrentUnit {
    AMPERE, // A
    MILLIAMPERES, // mA
    MICROAMPERES; // μA

    public static double milliAmperesToAmperes(double milliAmperes) {
        return milliAmperes / 1000;
    }
}
