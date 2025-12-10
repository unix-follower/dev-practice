package org.example.assistantonsbservlet.physics;

public enum ResistorColorCode {
    BLACK,
    BROWN,
    RED,
    ORANGE,
    YELLOW,
    GREEN,
    BLUE,
    VIOLET,
    GRAY,
    WHITE;

    public enum MultiplierBand {
        BLACK(1),
        BROWN(10),
        RED(100),
        ORANGE(1, ElectricalResistanceUnit.KILOOHMS),
        YELLOW(10, ElectricalResistanceUnit.KILOOHMS),
        GREEN(100, ElectricalResistanceUnit.KILOOHMS),
        BLUE(1, ElectricalResistanceUnit.MEGAOHMS),
        VIOLET(10, ElectricalResistanceUnit.MEGAOHMS),
        GRAY(100, ElectricalResistanceUnit.MEGAOHMS),
        WHITE(1, ElectricalResistanceUnit.GIGAOHMS),
        GOLD(0.1),
        SILVER(0.01);

        private final double multiplier;

        MultiplierBand(double multiplier) {
            this(multiplier, ElectricalResistanceUnit.OHM);
        }

        MultiplierBand(double multiplier, ElectricalResistanceUnit multiplierUnit) {
            switch (multiplierUnit) {
                case KILOOHMS -> this.multiplier = ElectricalResistanceUnit.kiloOhmsToOhms(multiplier);
                case MEGAOHMS -> this.multiplier = ElectricalResistanceUnit.megaOhmsToOhms(multiplier);
                case GIGAOHMS -> this.multiplier = ElectricalResistanceUnit.gigaOhmsToOhms(multiplier);
                default -> this.multiplier = multiplier;
            }
        }

        public double getMultiplier() {
            return multiplier;
        }
    }

    public enum Tolerance {
        BROWN(1),
        RED(2),
        GREEN(0.5),
        BLUE(0.25),
        VIOLET(0.1),
        GRAY(0.05),
        GOLD(5),
        SILVER(10);

        private final double tolerancePercent;

        Tolerance(double tolerancePercent) {
            this.tolerancePercent = tolerancePercent / 100;
        }

        public double getTolerancePercent() {
            return tolerancePercent;
        }
    }

    /**
     * Temperature Coefficient of Resistance
     */
    public enum TCR {
        BROWN(100),
        RED(50),
        YELLOW(25),
        ORANGE(15),
        BLUE(10),
        VIOLET(5);

        private final double tempCoeff;

        /**
         *
         * @param ppmTemperature ppm/°C (Parts Per Million per Degree Celsius)
         */
        TCR(double ppmTemperature) {
            this.tempCoeff = ppmTemperature / 1_000_000;
        }

        public double getTempCoeff() {
            return tempCoeff;
        }
    }
}
