package org.example.assistantonsbservlet.math;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConversionCalculatorTest {
    @Nested
    class LengthAndArea {
        @Test
        void calculateHectaresToAcres() {
            // given
            final double hectares = 2;
            // when
            final double result = ConversionCalculator.LengthAndArea.hectaresToAcres(hectares);
            // then
            assertEquals(4.94211, result, 0.1);
        }

        @Test
        void calculateAcresToHectares() {
            // given
            final double acres = 4.94211;
            // when
            final double result = ConversionCalculator.LengthAndArea.acresToHectares(acres);
            // then
            assertEquals(2, result, 0.1);
        }

        @Test
        void calculateAcresToSquareMeters() {
            // given
            final double acres = 2;
            // when
            final double result = ConversionCalculator.LengthAndArea.acresToSquareMeters(acres);
            // then
            assertEquals(8093.71, result, 0.1);
        }

        @Test
        void calculateAcresToSquareFeet() {
            // given
            final double acres = 0.25;
            // when
            final double result = ConversionCalculator.LengthAndArea.acresToSquareFeet(acres);
            // then
            assertEquals(10_890, result, 0.1);
        }

        @Test
        void calculateAcresToSquareMiles() {
            // given
            final double acres = 8;
            // when
            final double result = ConversionCalculator.LengthAndArea.acresToSquareMiles(acres);
            // then
            assertEquals(0.0125, result, 0.1);
        }

        @Test
        void calculateSquareMetersToSquareYards() {
            // given
            final double squareMeters = 50;
            // when
            final double result = ConversionCalculator.LengthAndArea.squareMetersToSquareYards(squareMeters);
            // then
            assertEquals(59.7995, result, 0.1);
        }
    }
}
