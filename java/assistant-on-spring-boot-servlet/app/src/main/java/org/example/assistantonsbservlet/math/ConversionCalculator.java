package org.example.assistantonsbservlet.math;

public final class ConversionCalculator {
    private ConversionCalculator() {
    }

    public static final class LengthAndArea {
        public static final double ONE_HECTARE_IN_ACRE = 2.471054;
        public static final double ONE_ACRE_IN_SQUARE_METER = 4_046.85642;
        public static final double ONE_ACRE_IN_SQUARE_MILE = 0.0015625;
        public static final double ONE_ACRE_IN_SQUARE_FEET = 43_560;
        public static final double ONE_SQUARE_METER_IN_SQUARE_YARD = 1.19599;
        public static final double ONE_SQUARE_MILLIMETER_IN_SQUARE_INCH = 0.00155;
        public static final double ONE_ARE_IN_HECTARE = 100;

        private LengthAndArea() {
        }

        public static double hectaresToAcres(double areaInHectares) {
            return areaInHectares * ONE_HECTARE_IN_ACRE;
        }

        public static double acresToHectares(double areaInAcres) {
            return areaInAcres / ONE_HECTARE_IN_ACRE;
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

        /**
         * @return Hectares = Ares / 100
         */
        public static double aresToHectares(double areaInAres) {
            return areaInAres / ONE_ARE_IN_HECTARE;
        }

        /**
         * @return Ares = Hectares * 100
         */
        public static double hectaresToAres(double areaInHectares) {
            return areaInHectares * ONE_ARE_IN_HECTARE;
        }
    }

    public static final class VolumeAndWeight {
        public static final double ONE_GRAM_IN_POUND = 0.00220462;

        private VolumeAndWeight() {
        }

        public static double gramsToPounds(double grams) {
            return grams * ONE_GRAM_IN_POUND;
        }
    }

    public static final class Time {
        public static final int ONE_HOUR_IN_SECONDS = 3600;

        private Time() {
        }

        public static long hoursToSeconds(long hours) {
            return hours * ONE_HOUR_IN_SECONDS;
        }
    }
}
