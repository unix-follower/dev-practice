package org.example.assistantonsbservlet.math;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MathCalculatorTest {
    @Nested
    class Geometry {
        @Test
        void calculateTriangle306090SolveWithA() {
            // given
            final float sideA = 6.35f; // cm
            // when
            final double[] sides = MathCalculator.Geometry.triangle306090SolveWithA(sideA);
            // then
            assertNotNull(sides);
            assertEquals(3, sides.length);
            assertEquals(sideA, sides[Constants.ARR_1ST_INDEX], 0.01); // cm
            assertEquals(11, sides[Constants.ARR_2ND_INDEX], 0.01); // cm
            assertEquals(12.7, sides[Constants.ARR_3RD_INDEX], 0.1); // cm
        }

        @Test
        void calculateTriangle306090SolveWithB() {
            // given
            final int sideB = 11; // cm
            // when
            final double[] sides = MathCalculator.Geometry.triangle306090SolveWithB(sideB);
            // then
            assertNotNull(sides);
            assertEquals(3, sides.length);
            assertEquals(6.35, sides[Constants.ARR_1ST_INDEX], 0.01); // cm
            assertEquals(sideB, sides[Constants.ARR_2ND_INDEX], 0.1); // cm
            assertEquals(12.7, sides[Constants.ARR_3RD_INDEX], 0.1); // cm
        }

        @Test
        void calculateTriangle306090SolveWithC() {
            // given
            final float sideC = 12.7f; // cm
            // when
            final double[] sides = MathCalculator.Geometry.triangle306090SolveWithC(sideC);
            // then
            assertNotNull(sides);
            assertEquals(3, sides.length);
            assertEquals(6.35, sides[Constants.ARR_1ST_INDEX], 0.01); // cm
            assertEquals(11, sides[Constants.ARR_2ND_INDEX], 0.1); // cm
            assertEquals(sideC, sides[Constants.ARR_3RD_INDEX], 0.1); // cm
        }
    }

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

        @Test
        void testCrossProduct() {
            // given
            final double[] vectorA = {2, 3, 7};
            final double[] vectorB = {1, 2, 4};
            // when
            final double[] resultVector = MathCalculator.CoordinateGeometry.crossProduct(vectorA, vectorB);
            // then
            assertNotNull(resultVector);
            assertEquals(3, resultVector.length);
            assertEquals(-2, resultVector[Constants.X_INDEX], 0.1);
            assertEquals(-1, resultVector[Constants.Y_INDEX], 0.1);
            assertEquals(1, resultVector[Constants.Z_INDEX], 0.1);
        }

        @Test
        void testUnsupportedDimsForCrossProduct() {
            // given
            final double[] vectorA = {2, 3};
            final double[] vectorB = {1, 2};
            // when
            final var exception = assertThrows(IllegalArgumentException.class,
                () -> MathCalculator.CoordinateGeometry.crossProduct(vectorA, vectorB));
            // then
            assertEquals("The cross product can only be applied to 3D vectors", exception.getMessage());
        }

        @Test
        void testDotProduct2d() {
            // given
            final double[] vectorA = {5, 4};
            final double[] vectorB = {2, 3};
            // when
            final double result = MathCalculator.CoordinateGeometry.dotProduct(vectorA, vectorB);
            // then
            assertEquals(22, result, 0.1);
        }

        @Test
        void testDotProduct3d() {
            // given
            final double[] vectorA = {4, 5, -3};
            final double[] vectorB = {1, -2, -2};
            // when
            final double result = MathCalculator.CoordinateGeometry.dotProduct(vectorA, vectorB);
            // then
            assertEquals(0, result, 0.1);
        }

        @Test
        void testDotProductOfMatrix() {
            // given
            final double[][] matrixA = {
                {4, 5, -3},
                {2, -1, 4}
            };
            final double[][] matrixB = {
                {1, -2, -2},
                {5, 1, 3}
            };
            // when
            final double result = MathCalculator.CoordinateGeometry.dotProduct(matrixA, matrixB);
            // then
            // 4*1 + 5*-2 + -3*-2 = [4, -10, 6] = 0
            // 2*5 + -1*1 + 4*3   = [10, -1, 12] = 21
            assertEquals(21, result, 0.1);
        }

        @Test
        void testDotProduct2dAndAngleBetween() {
            // given
            final double[] vectorA = {5, 4};
            final double[] vectorB = {2, 3};
            // when
            final double[] resultData = MathCalculator.CoordinateGeometry.dotProductAndAngleBetween(vectorA, vectorB);
            // then
            assertNotNull(resultData);
            assertEquals(4, resultData.length);
            final double dot = resultData[Constants.ARR_1ST_INDEX];
            final double magnitudeA = resultData[Constants.ARR_2ND_INDEX];
            final double magnitudeB = resultData[Constants.ARR_3RD_INDEX];
            final double angleRadians = resultData[Constants.ARR_4TH_INDEX];
            assertEquals(22, dot, 0.1);
            assertEquals(6.403, magnitudeA, 0.001);
            assertEquals(3.6056, magnitudeB, 0.0001);
            assertEquals(0.30814, angleRadians, 0.00001);
        }

        @Test
        void testDotProduct3dAndAngleBetween() {
            // given
            final double[] vectorA = {4, 5, 3};
            final double[] vectorB = {1, -2, -2};
            // when
            final double[] resultData = MathCalculator.CoordinateGeometry.dotProductAndAngleBetween(vectorA, vectorB);
            // then
            assertNotNull(resultData);
            assertEquals(4, resultData.length);
            final double dot = resultData[Constants.ARR_1ST_INDEX];
            final double magnitudeA = resultData[Constants.ARR_2ND_INDEX];
            final double magnitudeB = resultData[Constants.ARR_3RD_INDEX];
            final double angleRadians = resultData[Constants.ARR_4TH_INDEX];
            assertEquals(-12, dot, 0.1);
            assertEquals(7.071, magnitudeA, 0.001);
            assertEquals(3, magnitudeB, 0.0001);
            assertEquals(2.172, angleRadians, 0.001);
        }

        static List<Arguments> manhattanDistanceParams() {
            return List.of(
                Arguments.of(new double[] {2}, new double[] {3}, 1), // 1d
                Arguments.of(new double[] {2, 9}, new double[] {3, 5}, 5), // 2d
                Arguments.of(new double[] {2, 9, 4}, new double[] {3, 5, 6}, 7), // 3d
                Arguments.of(new double[] {2, 9, 4, 1}, new double[] {3, 5, 6, 7}, 13) // 4d
            );
        }

        @ParameterizedTest
        @MethodSource("manhattanDistanceParams")
        void testManhattanDistance(double[] vectorA, double[] vectorB, double expectedResult) {
            // when
            final double distance = MathCalculator.CoordinateGeometry.manhattanDistance(vectorA, vectorB);
            // then
            assertEquals(expectedResult, distance, 0.1);
        }

        @Test
        void testCartesianToCylindricalCoordinates() {
            // given
            final double[] cartesianCoords = {2, 5, 3};
            // when
            final double[] cylindricalCoords = MathCalculator.CoordinateGeometry
                .cartesianToCylindricalCoordinates(cartesianCoords);
            // then
            assertNotNull(cylindricalCoords);
            assertEquals(3, cylindricalCoords.length);
            assertEquals(5.385, cylindricalCoords[Constants.R_INDEX], 0.001);
            assertEquals(1.1903, cylindricalCoords[Constants.THETA_INDEX], 0.0001);
            assertEquals(3, cylindricalCoords[Constants.Z_INDEX], 0.1);
        }

        @Test
        void testCylindricalToCartesianCoordinates() {
            // given
            final double[] cylindricalCoords = {5.385, 1.1903, 3};
            // when
            final double[] cartesianCoords = MathCalculator.CoordinateGeometry
                .cylindricalToCartesianCoordinates(cylindricalCoords);
            // then
            assertNotNull(cartesianCoords);
            assertEquals(3, cartesianCoords.length);
            assertEquals(2, cartesianCoords[Constants.X_INDEX], 0.1);
            assertEquals(5, cartesianCoords[Constants.Y_INDEX], 0.1);
            assertEquals(3, cartesianCoords[Constants.Z_INDEX], 0.1);
        }

        @Test
        void testCartesianToPolarCoordinates() {
            // given
            final double[] cartesianCoords = {2, 3};
            // when
            final double[] polarCoords = MathCalculator.CoordinateGeometry.cartesianToPolarCoordinates(cartesianCoords);
            // then
            assertNotNull(polarCoords);
            assertEquals(2, polarCoords.length);
            assertEquals(3.6056, polarCoords[Constants.R_INDEX], 0.0001);
            assertEquals(0.9828, polarCoords[Constants.THETA_INDEX], 0.0001);
        }

        @Test
        void testPolarToCartesianCoordinates() {
            // given
            final double[] polarCoords = {3.6056, 0.9828};
            // when
            final double[] cartesianCoords = MathCalculator.CoordinateGeometry.polarToCartesianCoordinates(polarCoords);
            // then
            assertNotNull(cartesianCoords);
            assertEquals(2, cartesianCoords.length);
            assertEquals(2, cartesianCoords[Constants.X_INDEX], 0.1);
            assertEquals(3, cartesianCoords[Constants.Y_INDEX], 0.1);
        }

        @Test
        void testCartesianToSphericalCoordinates() {
            // given
            final double[] cartesianCoords = {2, 5, 3};
            // when
            final double[] sphericalCoords = MathCalculator.CoordinateGeometry
                .cartesianToSphericalCoordinates(cartesianCoords);
            // then
            assertNotNull(sphericalCoords);
            assertEquals(3, sphericalCoords.length);
            assertEquals(6.164, sphericalCoords[Constants.R_INDEX], 0.001);
            assertEquals(1.0625, sphericalCoords[Constants.THETA_INDEX], 0.0001);
            assertEquals(1.1903, sphericalCoords[Constants.PHI_INDEX], 0.0001);
        }

        @Test
        void testSphericalToCartesianCoordinates() {
            // given
            final double[] sphericalCoords = {6.164, 1.0625, 1.1903};
            // when
            final double[] cartesianCoords = MathCalculator.CoordinateGeometry
                .sphericalToCartesianCoordinates(sphericalCoords);
            // then
            assertNotNull(cartesianCoords);
            assertEquals(3, cartesianCoords.length);
            assertEquals(2, cartesianCoords[Constants.X_INDEX], 0.1);
            assertEquals(5, cartesianCoords[Constants.Y_INDEX], 0.1);
            assertEquals(3, cartesianCoords[Constants.Z_INDEX], 0.1);
        }

        @Test
        void testVectorProjection2d() {
            // given
            final double[] vectorA = {3, 4};
            final double[] vectorB = {2, 6};
            // when
            final var projectionResult = MathCalculator.CoordinateGeometry
                .vectorProjection(vectorA, vectorB);
            // then
            assertNotNull(projectionResult);

            final double[] projection = projectionResult.getLeft();
            assertNotNull(projection);
            assertEquals(2, projection.length);
            assertEquals(1.5, projection[Constants.X_INDEX], 0.1);
            assertEquals(4.5, projection[Constants.Y_INDEX], 0.1);

            final double projectionFactor = projectionResult.getRight();
            assertEquals(0.75, projectionFactor, 0.01);
        }

        @Test
        void testVectorProjection3d() {
            // given
            final double[] vectorA = {2, -3, 5};
            final double[] vectorB = {3, 6, -4};
            // when
            final var projectionResult = MathCalculator.CoordinateGeometry
                .vectorProjection(vectorA, vectorB);
            // then
            assertNotNull(projectionResult);

            final double[] projection = projectionResult.getLeft();
            assertNotNull(projection);
            assertEquals(3, projection.length);
            assertEquals(-1.5738, projection[Constants.X_INDEX], 0.0001);
            assertEquals(-3.1475, projection[Constants.Y_INDEX], 0.0001);
            assertEquals(2.0984, projection[Constants.Z_INDEX], 0.0001);

            final double projectionFactor = projectionResult.getRight();
            assertEquals(-0.5246, projectionFactor, 0.0001);
        }

        static List<Arguments> distanceBetween2pointsParams() {
            return List.of(
                Arguments.of(new double[] {3}, new double[] {9}, 6, 0.1), // 1d
                Arguments.of(new double[] {3, 5}, new double[] {9, 15}, 11.6619, 0.0001), // 2d
                Arguments.of(new double[] {3, 5, 2}, new double[] {9, 15, 5}, 12.0416, 0.0001), // 3d
                Arguments.of(new double[] {3, 5, 2, 3}, new double[] {9, 15, 5, 1}, 12.20656, 0.00001) // 4d
            );
        }

        @ParameterizedTest
        @MethodSource("distanceBetween2pointsParams")
        void testDistanceBetween2points(
            double[] pointACoords, double[] pointBCoords, double expectedResult, double delta) {
            // when
            final double distance = MathCalculator.CoordinateGeometry.distance(pointACoords, pointBCoords);
            // then
            assertEquals(expectedResult, distance, delta);
        }

        @Test
        void testDistanceBetween3points() {
            // given
            final double[] pointACoords = {3, 5, 2};
            final double[] pointBCoords = {9, 15, 5};
            final double[] pointCCoords = {2, 7, 1};
            // when
            final double abDistance = MathCalculator.CoordinateGeometry.distance(pointACoords, pointBCoords);
            final double bcDistance = MathCalculator.CoordinateGeometry.distance(pointBCoords, pointCCoords);
            final double acDistance = MathCalculator.CoordinateGeometry.distance(pointACoords, pointCCoords);
            // then
            assertEquals(12.0416, abDistance, 0.0001);
            assertEquals(11.35782, bcDistance, 0.00001);
            assertEquals(2.44949, acDistance, 0.00001);
        }

        @Test
        void testDistanceBetweenPointsAndStraightLine() {
            // given
            final double[] pointCoords = {3, 5};
            final int lineSlope = 2;
            final int lineYIntercept = 6;
            // when
            final double distance = MathCalculator.CoordinateGeometry
                .distanceBetweenPointsAndStraightLine(pointCoords, lineSlope, lineYIntercept);
            // then
            assertEquals(3.130495, distance, 0.000001);
        }

        @Test
        void testDistanceBetweenParallelLines() {
            // given
            final int slope = 2;
            final int line1YIntercept = 6;
            final int line2YIntercept = 8;
            // when
            final double distance = MathCalculator.CoordinateGeometry
                .distanceBetweenParallelLines(slope, line1YIntercept, line2YIntercept);
            // then
            assertEquals(0.894427, distance, 0.000001);
        }

        @Test
        void testRotation() {
            // given
            final double[] pointCoords = {3, 4};
            final double angleTheta = Math.toRadians(60);
            // when
            final double[] resultCoords = MathCalculator.CoordinateGeometry.rotation(pointCoords, angleTheta);
            // then
            assertNotNull(resultCoords);
            assertEquals(2, resultCoords.length);
            assertEquals(-1.964, resultCoords[Constants.X_INDEX], 0.001);
            assertEquals(4.598, resultCoords[Constants.Y_INDEX], 0.001);
        }

        @Test
        void testRotationAroundPoint() {
            // given
            final double[] pointCoords = {3, 4};
            final double angleTheta = Math.toRadians(60);
            final double[] pivotCoords = {1, 2};
            // when
            final double[] resultCoords = MathCalculator.CoordinateGeometry
                .rotationAroundPoint(pointCoords, pivotCoords, angleTheta);
            // then
            assertNotNull(resultCoords);
            assertEquals(2, resultCoords.length);
            assertEquals(0.26795, resultCoords[Constants.X_INDEX], 0.00001);
            assertEquals(4.732, resultCoords[Constants.Y_INDEX], 0.001);
        }

        @Test
        void testSlope() {
            // given
            final double[] pointACoords = {1, 5};
            final double[] pointBCoords = {7, 6};
            // when
            final double slope = MathCalculator.CoordinateGeometry.slope(pointACoords, pointBCoords);
            final double angleTheta = Math.atan(slope);
            final double distance = MathCalculator.CoordinateGeometry.distance(pointACoords, pointBCoords);
            final double deltaX = MathCalculator.CoordinateGeometry.deltaDistance(
                pointBCoords[Constants.X_INDEX], pointACoords[Constants.X_INDEX]);
            final double deltaY = MathCalculator.CoordinateGeometry.deltaDistance(
                pointBCoords[Constants.Y_INDEX], pointACoords[Constants.Y_INDEX]);
            // then
            assertEquals(0.166667, slope, 0.000001);
            assertEquals(0.16515, angleTheta, 0.00001);
            assertEquals(6.0828, distance, 0.0001);
            assertEquals(6, deltaX, 0.1);
            assertEquals(1, deltaY, 0.1);
        }

        @Test
        void testSlopeFromKnownIntercepts() {
            // given
            final int xIntercept = 2;
            final int yIntercept = -3;
            // when
            final double slope = MathCalculator.CoordinateGeometry.slopeFromKnownIntercepts(xIntercept, yIntercept);
            // then
            assertEquals(1.5, slope, 0.1);
        }

        @Test
        void testAreaUnderSlope() {
            // given
            final int x1 = 1;
            final int x2 = 7;
            final double slope = 0.166667;
            // when
            final double area = MathCalculator.CoordinateGeometry.areaUnderSlope(x1, x2, slope);
            // then
            assertEquals(3.000006, area, 0.000001);
        }

        @Test
        void testIntercept() {
            // given
            final int a = 2;
            final int b = 3;
            final int c = -2;
            // when
            final double[] intercepts = MathCalculator.CoordinateGeometry.intercept(a, b, c);
            final double slope = MathCalculator.CoordinateGeometry.slope(a, b);
            // then
            assertNotNull(intercepts);
            assertEquals(2, intercepts.length);
            assertEquals(1, intercepts[Constants.X_INDEX], 0.1);
            assertEquals(0.6667, intercepts[Constants.Y_INDEX], 0.0001);

            assertEquals(-0.6667, slope, 0.0001);
        }

        @Test
        void testInterceptWithKnownSlopeAndConstantTerm() {
            // given
            final int slopeTerm = 2;
            final int constantTerm = -2;
            // when
            final double[] intercepts = MathCalculator.CoordinateGeometry.intercept(slopeTerm, constantTerm);
            // then
            assertNotNull(intercepts);
            assertEquals(2, intercepts.length);
            assertEquals(1, intercepts[Constants.X_INDEX], 0.1);
            assertEquals(-2, intercepts[Constants.Y_INDEX], 0.0001);
        }

        @Test
        void testLinearInterpolation() {
            // given
            final double[] pointACoords = {200, 15};
            final double[] pointBCoords = {300, 20};
            final int midpointX = 250;
            // when
            final double midpointY = MathCalculator.CoordinateGeometry
                .linearInterpolation(pointACoords, pointBCoords, midpointX);
            final double slope = MathCalculator.CoordinateGeometry.slope(pointACoords, pointBCoords);
            final double deltaY = MathCalculator.CoordinateGeometry.deltaDistance(
                pointBCoords[Constants.Y_INDEX], pointACoords[Constants.Y_INDEX]);
            // then
            assertEquals(17.5, midpointY, 0.1);
            assertEquals(0.05, slope, 0.01);
            assertEquals(5, deltaY, 0.1);
        }
    }
}
