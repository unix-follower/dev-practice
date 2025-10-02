package org.example.assistantonsbservlet.health;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HealthCalculatorTest {
    @Nested
    class BodyMeasurements {
        @Test
        void convertHectaresToAcres() {
            // given
            final double height = 1.7;
            final double weight = 59;
            final double waistCircumference = 0.73;
            // when
            final double result = HealthCalculator.BodyMeasurements.absi(height, weight, waistCircumference);
            // then
            assertEquals(0.07495, result, 0.00001);
        }
    }
}
