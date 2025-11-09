package org.example.assistantonsbservlet.physics;

public final class Constants {
    public static final double GRAVITATIONAL_ACCELERATION_IN_M_PER_S2 = 9.80665;
    public static final double AVOGADRO_NUMBER = 6.02214082e23;
    /**
     * ~3.00 * 10⁸ m/s. Also, 300 * 10^⁶ m/s
     */
    public static final double SPEED_OF_LIGHT_IN_M_PER_SEC = 2.99792458e8;
    public static final double ELECTRON_CHARGE_IN_COULOMBS = 1.6021766208e-19;
    public static final double BLINK_OF_AN_EYE_SEC = 0.350; // 350e-3
    /**
     * (6.02214 * 10^23 electrons/mole) / (6.24151 * 10^18 electrons/coulomb) = 96485 coulombs/mole
     * {@link #AVOGADRO_NUMBER} {@link #ONE_COULOMB}
     */
    public static final double FARADAY_CONSTANT = 96_485;
    /**
     * The number of electron charges
     */
    public static final double ONE_COULOMB = 6.241509343e18;

    private Constants() {
    }
}
