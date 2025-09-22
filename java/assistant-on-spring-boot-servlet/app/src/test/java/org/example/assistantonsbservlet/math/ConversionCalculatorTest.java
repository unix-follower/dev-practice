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
    }
}
