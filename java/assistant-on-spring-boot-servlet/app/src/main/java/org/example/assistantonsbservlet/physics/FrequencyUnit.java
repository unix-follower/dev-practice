package org.example.assistantonsbservlet.physics;

public enum FrequencyUnit {
    HERTZ, // Hz
    KILOHERTZ, // kHz
    MEGAHERTZ, // MHz
    GIGAHERTZ, // GHz
    TERAHERTZ, // THz
    REVOLUTIONS_PER_MINUTE; // rpm

    public static double hzToKHz(double hertz) {
        return hertz / 1000;
    }

    public static double hzToMHz(double hertz) {
        return hertz / 1_000_000;
    }

    public static double thzToHz(double terahertz) {
        return terahertz * 1e12;
    }
}
