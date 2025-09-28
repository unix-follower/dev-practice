package org.example.assistantonsbservlet.math;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConversionCalculatorTest {
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
    }
}
