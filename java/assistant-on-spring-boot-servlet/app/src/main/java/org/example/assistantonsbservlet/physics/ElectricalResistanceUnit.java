package org.example.assistantonsbservlet.physics;

public enum ElectricalResistanceUnit {
    PICOOHMS, // pΩ
    NANOOHMS, // nΩ
    MICROOHMS, // μΩ
    MILLIOHMS, // mΩ
    CENTIOHMS, // cΩ
    OHM, // Ω
    KILOOHMS, // kΩ
    MEGAOHMS, // MΩ
    GIGAOHMS, // GΩ
    TERAOHMS; // TΩ

    /**
     * @return Ohms = kiloOhms * 1000
     */
    public static double kiloOhmsToOhms(double kiloOhms) {
        return kiloOhms * 1000;
    }

    /**
     * Ohms = MegaOhms * 10⁶
     *
     * @return Ohms = MegaOhms * 1,000,000
     */
    public static double megaOhmsToOhms(double kiloOhms) {
        return kiloOhms * 1_000_000;
    }

    /**
     * Ohms = GigaOhms * 10⁹
     *
     * @return Ohms = GigaOhms * 1,000,000,000
     */
    public static double gigaOhmsToOhms(double kiloOhms) {
        return kiloOhms * 1_000_000_000;
    }
}
