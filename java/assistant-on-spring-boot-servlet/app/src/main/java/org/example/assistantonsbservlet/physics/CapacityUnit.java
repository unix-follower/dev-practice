package org.example.assistantonsbservlet.physics;

public enum CapacityUnit {
    FARADS, // F
    MILLIFARADS, // mF
    MICROFARADS, // μF
    NANOFARADS, // nF
    PICOFARADS; // pF

    public static final short ONE_FARAD_IN_MILLIFARADS = 1000;
    public static final double ONE_MILLIFARAD_IN_FARADS = 0.001;
    public static final short ONE_MICROFARAD_IN_NANOFARADS = 1000;
    public static final int ONE_MICROFARAD_IN_PICOFARADS = 1_000_000;
    public static final int ONE_FARAD_IN_MICROFARADS = 1_000_000;

    public static double faradsToMicroFarads(double farads) {
        return ONE_FARAD_IN_MICROFARADS * farads;
    }

    public static double faradsToMilliFarads(double farads) {
        return farads * ONE_FARAD_IN_MILLIFARADS;
    }

    public static double milliFaradsToFarads(double milliFarads) {
        return milliFarads * ONE_MILLIFARAD_IN_FARADS;
    }

    public static double microFaradsToFarads(double microFarads) {
        return microFarads / ONE_FARAD_IN_MICROFARADS;
    }

    public static double microFaradsToNanofarads(double microFarads) {
        return microFarads * ONE_MICROFARAD_IN_NANOFARADS;
    }

    public static double microFaradsToPicofarads(double microFarads) {
        return microFarads * ONE_MICROFARAD_IN_PICOFARADS;
    }
}
