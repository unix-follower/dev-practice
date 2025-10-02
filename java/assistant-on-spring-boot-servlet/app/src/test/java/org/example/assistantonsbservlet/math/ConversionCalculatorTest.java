package org.example.assistantonsbservlet.math;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConversionCalculatorTest {
    @Test
    void convertGramsToCalories() {
        // given
        final int carbohydrate = 23;
        final int protein = 4;
        final int fat = 9;
        // when
        final double[] results = ConversionCalculator.gramsToCalories(carbohydrate, protein, fat);
        // then
        assertNotNull(results);
        assertEquals(4, results.length);
        final double carbohydrateKcal = results[0];
        assertEquals(92, carbohydrateKcal, 0.1);
        final double proteinKcal = results[1];
        assertEquals(16, proteinKcal, 0.1);
        final double fatKcal = results[2];
        assertEquals(81, fatKcal, 0.1);
        final double totalKilocalories = results[3];
        assertEquals(189, totalKilocalories, 0.1);
    }

    @Test
    void convertGramsToCaloriesWithKnownAlcoholMass() {
        // given
        final int carbohydrate = 23;
        final int protein = 4;
        final int fat = 9;
        final double alcohol = 39.462;
        // when
        final double[] results = ConversionCalculator.gramsToCaloriesWithKnownAlcoholMass(
            carbohydrate, protein, fat, alcohol);
        // then
        assertNotNull(results);
        assertEquals(5, results.length);
        final double carbohydrateKcal = results[0];
        assertEquals(92, carbohydrateKcal, 0.1);
        final double proteinKcal = results[1];
        assertEquals(16, proteinKcal, 0.1);
        final double fatKcal = results[2];
        assertEquals(81, fatKcal, 0.1);
        final double alcoholKcal = results[3];
        assertEquals(110.4936, alcoholKcal, 0.1);
        final double totalKilocalories = results[4];
        assertEquals(299.4936, totalKilocalories, 0.1);
    }

    @Test
    void convertGramsToCaloriesWithAlcohol() {
        // given
        final int carbohydrate = 23;
        final int protein = 4;
        final int fat = 9;
        final double alcoholInMl = 50;
        // when
        final double[] results = ConversionCalculator.gramsToCalories(
            carbohydrate, protein, fat, 0.4, alcoholInMl);
        // then
        assertNotNull(results);
        assertEquals(5, results.length);
        final double carbohydrateKcal = results[0];
        assertEquals(92, carbohydrateKcal, 0.1);
        final double proteinKcal = results[1];
        assertEquals(16, proteinKcal, 0.1);
        final double fatKcal = results[2];
        assertEquals(81, fatKcal, 0.1);
        final double alcoholKcal = results[3];
        assertEquals(110.4936, alcoholKcal, 0.1);
        final double totalKilocalories = results[4];
        assertEquals(299.4936, totalKilocalories, 0.1);
    }

    @Test
    void convertGramsToMilliliters() {
        // given
        final double densityOfHoney = 1.42; // g/ml
        final int massInG = 100;
        // when
        final double volume = ConversionCalculator.gramsToMilliliters(densityOfHoney, massInG);
        // then
        assertEquals(70.42, volume, 0.01);
    }

    @Test
    void convertMillilitersToGrams() {
        // given
        final double densityOfHoney = 1.42; // g/ml
        final double volumeInMl = 70.42;
        // when
        final double massInG = ConversionCalculator.millilitersToGrams(densityOfHoney, volumeInMl);
        // then
        assertEquals(100, massInG, 0.1);
    }

    @Nested
    class LengthAndArea {
        @Test
        void convertHectaresToAcres() {
            // given
            final double hectares = 2;
            // when
            final double result = ConversionCalculator.LengthAndArea.hectaresToAcres(hectares);
            // then
            assertEquals(4.94211, result, 0.1);
        }

        @Test
        void convertAcresToHectares() {
            // given
            final double acres = 4.94211;
            // when
            final double result = ConversionCalculator.LengthAndArea.acresToHectares(acres);
            // then
            assertEquals(2, result, 0.1);
        }

        @Test
        void convertAcresToSquareMeters() {
            // given
            final double acres = 2;
            // when
            final double result = ConversionCalculator.LengthAndArea.acresToSquareMeters(acres);
            // then
            assertEquals(8093.71, result, 0.1);
        }

        @Test
        void convertAcresToSquareFeet() {
            // given
            final double acres = 0.25;
            // when
            final double result = ConversionCalculator.LengthAndArea.acresToSquareFeet(acres);
            // then
            assertEquals(10_890, result, 0.1);
        }

        @Test
        void convertAcresToSquareMiles() {
            // given
            final double acres = 8;
            // when
            final double result = ConversionCalculator.LengthAndArea.acresToSquareMiles(acres);
            // then
            assertEquals(0.0125, result, 0.1);
        }

        @Test
        void convertSquareMetersToSquareYards() {
            // given
            final double squareMeters = 50;
            // when
            final double result = ConversionCalculator.LengthAndArea.squareMetersToSquareYards(squareMeters);
            // then
            assertEquals(59.7995, result, 0.1);
        }

        @Test
        void convertAresToHectares() {
            // given
            final double ares = 100;
            // when
            final double result = ConversionCalculator.LengthAndArea.aresToHectares(ares);
            // then
            assertEquals(1, result, 0.1);
        }

        @Test
        void convertHectaresToAres() {
            // given
            final double hectares = 1;
            // when
            final double result = ConversionCalculator.LengthAndArea.hectaresToAres(hectares);
            // then
            assertEquals(100, result, 0.1);
        }

        @Test
        void convertMetersToAstronomicalUnits() {
            // given
            final double meters = 149_597_870_700.0;
            // when
            final double result = ConversionCalculator.LengthAndArea.metersToAstronomicalUnits(meters);
            // then
            assertEquals(1, result, 0.1);
        }

        @Test
        void convertJupiterDistanceFromSunInAstronomicalUnitsToMeters() {
            // given
            final double au = 5.2;
            // when
            final double result = ConversionCalculator.LengthAndArea.astronomicalUnitsToMeters(au);
            // then
            assertEquals(777_908_927_640.0, result, 0.1);
        }

        @Test
        void convertJupiterDistanceFromSunInAstronomicalUnitsToKilometers() {
            // given
            final double au = 5.2;
            // when
            final double result = ConversionCalculator.LengthAndArea.astronomicalUnitsToKilometers(au);
            // then
            assertEquals(777_908_927.64, result, 0.01);
        }

        @Test
        void convertDistanceToProximaCentauriInLightYearsToAstronomicalUnits() {
            // given
            final double lightYears = 4.2465;
            // when
            final double resultAu = ConversionCalculator.LengthAndArea.lightYearsToAstronomicalUnits(lightYears);
            // then
            assertEquals(268_552.9065, resultAu, 0.0001);
        }

        @Test
        void convertAstronomicalUnitsToParsecs() {
            // given
            final double au = 206_264.8;
            // when
            final double resultPc = ConversionCalculator.LengthAndArea.astronomicalUnitsToParsecs(au);
            // then
            assertEquals(1, resultPc, 0.1);
        }

        @Test
        void convertDecimetersToMeters() {
            // given
            final double dm = 34.98;
            // when
            final double meter = ConversionCalculator.LengthAndArea.decimetersToMeters(dm);
            // then
            assertEquals(3.498, meter, 0.001);
        }

        @Test
        void convertMetersToDecimeters() {
            // given
            final double meter = 76.4;
            // when
            final double dm = ConversionCalculator.LengthAndArea.metersToDecimeters(meter);
            // then
            assertEquals(764, dm, 0.1);
        }
    }
}
