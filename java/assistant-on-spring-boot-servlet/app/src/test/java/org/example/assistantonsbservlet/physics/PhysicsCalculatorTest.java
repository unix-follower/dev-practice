package org.example.assistantonsbservlet.physics;

import org.example.assistantonsbservlet.math.ConversionCalculator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PhysicsCalculatorTest {
    @Nested
    class Kinematics {
        @Test
        void calculateArrowSpeed() {
            // given
            final double ibo = 300; // ft/s
            final double drawLengthOfBow = 32; // in
            final double peakDrawWeight = 70; // lb
            final double arrowWeight = 400; // gr
            final double additionalWeightOnString = 5; // gr
            // when
            final double result = PhysicsCalculator.Kinematics.arrowSpeed(ibo, drawLengthOfBow, peakDrawWeight,
                additionalWeightOnString, arrowWeight);
            // then
            assertEquals(301.67, result, 0.1); // ft/s
        }

        @Test
        void calculateBallisticCoefficient() {
            // given
            final double projectileMass = ConversionCalculator.VolumeAndWeight.gramsToPounds(50);
            final double dragCoefficient = 0.51;
            final double crossSectionArea = ConversionCalculator.LengthAndArea
                .squaredMillimetersToSquaredInches(132.73);

            // when
            final double result = PhysicsCalculator.Kinematics.ballisticCoefficient(
                projectileMass, dragCoefficient, crossSectionArea);
            // then
            assertEquals(1.0506, result, 0.1); // lb/in²
        }
    }
}
