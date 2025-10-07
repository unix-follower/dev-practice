package org.example.assistantonsbservlet.math;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MathCalculatorTest {
    @Nested
    class CoordinateGeometry {
        @Test
        void calculateVectorMagnitude2d() {
            // given
            final int x = -3;
            final int y = 8;
            final double[] vector = {x, y};
            // when
            final double magnitude = MathCalculator.CoordinateGeometry.vectorMagnitude(vector);
            // then
            assertEquals(8.544, magnitude, 0.001);
        }

        @Test
        void calculateVectorMagnitude3d() {
            // given
            final int x = -3;
            final int y = 8;
            final int z = 2;
            final double[] vector = {x, y, z};
            // when
            final double magnitude = MathCalculator.CoordinateGeometry.vectorMagnitude(vector);
            // then
            assertEquals(8.775, magnitude, 0.001);
        }

        @Test
        void calculateVectorMagnitude5d() {
            // given
            final int x = 3;
            final int y = 1;
            final int z = 2;
            final int t = -3;
            final int w = 4;
            final double[] vector = {x, y, z, t, w};
            // when
            final double magnitude = MathCalculator.CoordinateGeometry.vectorMagnitude(vector);
            // then
            assertEquals(6.245, magnitude, 0.001);
        }

        @Test
        void calculateUnitVector() {
            // given
            final int x = 8;
            final int y = -3;
            final int z = 5;
            final double[] vector = {x, y, z};
            // when
            final var unitVectorResultData = MathCalculator.CoordinateGeometry.unitVector(vector);
            // then
            final double vectorMagnitude = unitVectorResultData.getLeft();
            final double[] unitVectorResult = unitVectorResultData.getMiddle();
            final double unitVectorResultMagnitude = unitVectorResultData.getRight();
            assertEquals(9.9, vectorMagnitude, 0.1);

            assertNotNull(unitVectorResult);
            assertEquals(3, unitVectorResult.length);
            assertEquals(0.80812, unitVectorResult[Constants.X_INDEX], 0.00001);
            assertEquals(-0.303046, unitVectorResult[Constants.Y_INDEX], 0.000001);
            assertEquals(0.50508, unitVectorResult[Constants.Z_INDEX], 0.00001);

            assertEquals(1, unitVectorResultMagnitude, 0.1);
        }

        @Test
        void testFindMissingUnitVectorComponent() {
            // given
            final double x = 0.80812;
            final double y = -0.303046;
            final double[] unitVector = {x, y};
            // when
            final var unitVectorResultData = MathCalculator.CoordinateGeometry
                .findMissingUnitVectorComponent(unitVector);
            // then
            final double[] unitVectorResult = unitVectorResultData.getLeft();
            final double magnitude = unitVectorResultData.getRight();
            assertEquals(1, magnitude, 0.1);

            assertNotNull(unitVectorResult);
            assertEquals(3, unitVectorResult.length);
            assertEquals(x, unitVectorResult[Constants.X_INDEX], 0.00001);
            assertEquals(y, unitVectorResult[Constants.Y_INDEX], 0.000001);
            assertEquals(0.50508, unitVectorResult[Constants.Z_INDEX], 0.00001);
        }
    }
}
