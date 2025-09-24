package org.example.assistantonsbservlet.math;

public final class ConversionCalculator {
    private ConversionCalculator() {
    }

    public static final class LengthAndArea {
        public static final double ONE_HECTARES_IN_ACRE = 2.471054;
        public static final double ONE_ACRE_IN_SQUARE_METER = 4_046.85642;
        public static final double ONE_ACRE_IN_SQUARE_MILE = 0.0015625;
        public static final double ONE_ACRE_IN_SQUARE_FEET = 43_560;
        public static final double ONE_SQUARE_METER_IN_SQUARE_YARD = 1.19599;
        public static final double ONE_SQUARE_MILLIMETER_IN_SQUARE_INCH = 0.00155;

        private LengthAndArea() {
        }

        static double hectaresToAcres(double areaInHectares) {
            return areaInHectares * ONE_HECTARES_IN_ACRE;
        }

        public static double acresToHectares(double areaInAcres) {
            return areaInAcres / ONE_HECTARES_IN_ACRE;
        }

        public static double acresToSquareMeters(double areaInAcres) {
            return areaInAcres * ONE_ACRE_IN_SQUARE_METER;
        }

        public static double acresToSquareMiles(double areaInAcres) {
            return areaInAcres * ONE_ACRE_IN_SQUARE_MILE;
        }

        public static double acresToSquareFeet(double areaInAcres) {
            return areaInAcres * ONE_ACRE_IN_SQUARE_FEET;
        }

        public static double squareMillimetersToSquareInches(double areaInSquareMillimeters) {
            return areaInSquareMillimeters * ONE_SQUARE_MILLIMETER_IN_SQUARE_INCH;
        }

        public static double squareMetersToSquareYards(double areaInSquareMeters) {
            return areaInSquareMeters * ONE_SQUARE_METER_IN_SQUARE_YARD;
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
