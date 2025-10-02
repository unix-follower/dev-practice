package org.example.assistantonsbservlet.health;

public final class HealthCalculator {
    private HealthCalculator() {
    }

    public static final class BodyMeasurements {
        private BodyMeasurements() {
        }

        /**
         * @return BMI = weight/height². The units are kg/m²
         */
        public static double bmi(double heightInM, double weightInKg) {
            return weightInKg / (heightInM * heightInM);
        }

        /**
         * @return ABSI = WC/(BMI^(2/3) * height^½). The units are kg/m²
         */
        public static double absi(double heightInM, double weightInKg, double waistCircumferenceInM) {
            final double bodyMassIdx = bmi(heightInM, weightInKg);
            return waistCircumferenceInM / (Math.pow(bodyMassIdx, (2. / 3)) * Math.pow(heightInM, 0.5));
        }

        /**
         * @return ABSI_(z score) = (ABSI - ABSI_mean)/ABSI_sd
         */
        public static double absiZScore(double absi, double absiMean, double absiStandardDeviation) {
            return (absi - absiMean) / absiStandardDeviation;
        }
    }
}
