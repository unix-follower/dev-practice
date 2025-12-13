package org.example.assistantonsbservlet.physics;

public enum HenryUnit {
    HENRIES, // H
    MILLIHENRIES, // mH
    MICROHENRIES, // μH
    NANOJOULES; // nH

    public static final double ONE_MICROHENRY_IN_HENRIES = 1e-6;

    public static double microHenriesToHenries(double microHenries) {
        return microHenries * ONE_MICROHENRY_IN_HENRIES;
    }
}
