package org.example.assistantonsbservlet.math;

public final class ConversionCalculator {
    public static final byte KCAL_IN_ONE_GRAM_OF_CARBOHYDRATE = 4; // [3.57, 4.12] ~ 4 kcal
    public static final byte KCAL_IN_ONE_GRAM_OF_PROTEIN = 4;
    public static final byte KCAL_IN_ONE_GRAM_OF_FAT = 9;
    public static final byte KCAL_IN_ONE_GRAM_OF_ALCOHOL = 7;
    public static final double DENSITY_OF_ALCOHOL = 0.78924;

//    1 ampere = 1 coulomb/second
//    1 coulomb = 1 ampere * second
//    1 watt = 1 joule/second
//    1 volt = 1 watt/ampere
//    1 volt = 1 joule/coulomb
//    1 ohm = 1 volt/ampere

    private ConversionCalculator() {
    }

    /**
     * @return radians = π/180° * degrees
     */
    public static double degreesToRadians(double degrees) {
        return Math.toRadians(degrees);
    }

    public static double[] gramsToCalories(double carbohydrateInGrams, double proteinInGrams, double fatInGrams) {
        final double carbohydrateKcal = KCAL_IN_ONE_GRAM_OF_CARBOHYDRATE * carbohydrateInGrams;
        final double proteinKcal = KCAL_IN_ONE_GRAM_OF_PROTEIN * proteinInGrams;
        final double fatKcal = KCAL_IN_ONE_GRAM_OF_FAT * fatInGrams;
        return new double[]{carbohydrateKcal, proteinKcal, fatKcal, carbohydrateKcal + proteinKcal + fatKcal};
    }

    public static double[] gramsToCaloriesWithKnownAlcoholMass(
        double carbohydrateInGrams, double proteinInGrams, double fatInGrams, double alcoholInGrams) {
        final double volume = gramsToMilliliters(DENSITY_OF_ALCOHOL, alcoholInGrams);
        return gramsToCalories(carbohydrateInGrams, proteinInGrams, fatInGrams, 0.4, volume);
    }

    public static double[] gramsToCalories(
        double carbohydrateInGrams, double proteinInGrams, double fatInGrams,
        double alcoholByVolumePercent, double alcoholInMl) {
        final double[] kcals = gramsToCalories(carbohydrateInGrams, proteinInGrams, fatInGrams);
        final double alcoholKcal = KCAL_IN_ONE_GRAM_OF_ALCOHOL * alcoholInMl * alcoholByVolumePercent
            * DENSITY_OF_ALCOHOL;
        return new double[]{kcals[0], kcals[1], kcals[2], alcoholKcal, kcals[3] + alcoholKcal};
    }

    /**
     * @return Volume [ml] = Mass [g] / Density [g/ml]
     */
    public static double gramsToMilliliters(double densityOfIngredient, double massInG) {
        return massInG / densityOfIngredient;
    }

    /**
     * @return Mass [g] = Density [g/ml] × Volume [ml]
     */
    public static double millilitersToGrams(double densityOfIngredient, double volumeInMl) {
        return densityOfIngredient * volumeInMl;
    }

    public static final class LengthAndArea {
        public static final double ONE_HECTARE_IN_ACRE = 2.471054;
        public static final double ONE_ACRE_IN_SQUARE_METER = 4_046.85642;
        public static final double ONE_ACRE_IN_SQUARE_MILE = 0.0015625;
        public static final double ONE_ACRE_IN_SQUARE_FEET = 43_560;
        public static final double ONE_SQUARE_METER_IN_SQUARE_YARD = 1.19599;
        public static final double ONE_SQUARE_MILLIMETER_IN_SQUARE_INCH = 0.00155;
        public static final double ONE_ARE_IN_HECTARE = 100;
        public static final double ONE_KILOMETER_IN_METERS = 1000;
        public static final double ONE_ASTRONOMICAL_UNIT_IN_METERS = 149_597_870_700.0;
        public static final double ONE_ASTRONOMICAL_UNIT_IN_KILOMETERS = 149_597_870.7;
        public static final double ONE_ASTRONOMICAL_UNIT_IN_MILES = 92_955_807;
        public static final double ONE_ASTRONOMICAL_UNIT_IN_LIGHT_YEARS = 0.000015813;
        public static final double ONE_LIGHT_YEAR_IN_ASTRONOMICAL_UNITS = 63_241;
        public static final double ARCSECOND_IN_RADIANS = Math.PI / (180 * 3600);
        public static final double ONE_METER_IN_DECIMETERS = 10;

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

        public static double metersToKilometers(double meters) {
            return meters / ONE_KILOMETER_IN_METERS;
        }

        public static double metersToAstronomicalUnits(double meters) {
            return meters / ONE_ASTRONOMICAL_UNIT_IN_METERS;
        }

        public static double astronomicalUnitsToMeters(double au) {
            return au * ONE_ASTRONOMICAL_UNIT_IN_METERS;
        }

        public static double astronomicalUnitsToKilometers(double au) {
            return au * ONE_ASTRONOMICAL_UNIT_IN_KILOMETERS;
        }

        public static double astronomicalUnitsToMiles(double au) {
            return au * ONE_ASTRONOMICAL_UNIT_IN_MILES;
        }

        public static double lightYearsToAstronomicalUnits(double lightYears) {
            return lightYears * ONE_LIGHT_YEAR_IN_ASTRONOMICAL_UNITS;
        }

        public static double astronomicalUnitsToLightYears(double au) {
            return au * ONE_ASTRONOMICAL_UNIT_IN_LIGHT_YEARS;
        }

        public static double astronomicalUnitsToParsecs(double au) {
            final double auToParsec = 1 / Math.tan(ARCSECOND_IN_RADIANS);
            return au / auToParsec;
        }

        public static double decimetersToMeters(double decimeters) {
            return decimeters / ONE_METER_IN_DECIMETERS;
        }

        public static double metersToDecimeters(double meters) {
            return meters * ONE_METER_IN_DECIMETERS;
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
