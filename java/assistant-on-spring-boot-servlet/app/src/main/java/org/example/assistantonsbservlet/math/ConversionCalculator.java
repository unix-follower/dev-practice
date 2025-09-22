package org.example.assistantonsbservlet.math;

public final class ConversionCalculator {
    private ConversionCalculator() {
    }

    public static final class LengthAndArea {
        public static final double ONE_HA_IN_AC = 2.471054;
        public static final double ONE_AC_IN_SQM = 4_046.85642;
        public static final double ONE_SQMM_IN_SQIN = 0.00155;

        private LengthAndArea() {
        }

        static double hectaresToAcres(double areaHectares) {
            return areaHectares * ONE_HA_IN_AC;
        }

        public static double acresToHectares(double areaAcres) {
            return areaAcres / ONE_HA_IN_AC;
        }

        public static double acresToSquareMeters(double areaAcres) {
            return areaAcres * ONE_AC_IN_SQM;
        }

        public static double squaredMillimetersToSquaredInches(double squaredMillimeters) {
            return squaredMillimeters * ONE_SQMM_IN_SQIN;
        }
    }

    public static final class VolumeAndWeight {
        public static final double ONE_G_IN_LB = 0.00220462;

        private VolumeAndWeight() {
        }

        public static double gramsToPounds(double grams) {
            return grams * ONE_G_IN_LB;
        }
    }
}
