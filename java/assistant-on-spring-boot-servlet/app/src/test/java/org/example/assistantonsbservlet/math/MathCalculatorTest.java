package org.example.assistantonsbservlet.math;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

        static List<Arguments> pythagoreanTheoremForRightTriangleWithLegAndHypotenuseParams() {
            return List.of(
                Arguments.of(4, 8.94427, 8, 16, 20.94427),
                Arguments.of(7.07, 10, 7.07, 25, 24.14214)
            );
        }

        @ParameterizedTest
        @MethodSource("pythagoreanTheoremForRightTriangleWithLegAndHypotenuseParams")
        void testPythagoreanTheoremForRightTriangleWithLegAndHypotenuse(
            double sideA, double hypotenuse, double expectedResult, double expectedArea, double expectedPerimeter) {
            // when
            final double[] sides = MathCalculator.Geometry
                .pythagoreanTheoremForRightTriangleWithLegAndHypotenuse(sideA, hypotenuse);
            // then
            assertNotNull(sides);
            assertEquals(3, sides.length);

            final double sideB = sides[Constants.ARR_2ND_INDEX];

            assertEquals(sideA, sides[Constants.ARR_1ST_INDEX], 0.1);
            assertEquals(expectedResult, sideB, 0.1);
            assertEquals(hypotenuse, sides[Constants.ARR_3RD_INDEX], 0.000001);

            final double area = MathCalculator.Geometry.area(sideA, sideB);
            assertEquals(expectedArea, area, 0.1);

            final double perimeter = MathCalculator.Geometry.perimeter(sideA, sideB, hypotenuse);
            assertEquals(expectedPerimeter, perimeter, 0.00001);
        }

        @Test
        void testPythagoreanTheoremForRightTriangleWithLegs() {
            // given
            final byte sideA = 7; // cm
            final byte sideB = 9; // cm
            // when
            final double[] sides = MathCalculator.Geometry.pythagoreanTheoremForRightTriangleWithLegs(sideA, sideB);
            // then
            assertNotNull(sides);
            assertEquals(3, sides.length);

            assertEquals(sideA, sides[Constants.ARR_1ST_INDEX], 0.1);
            assertEquals(sideB, sides[Constants.ARR_2ND_INDEX], 0.1);
            final double hypotenuse = 11.40175; // cm
            assertEquals(hypotenuse, sides[Constants.ARR_3RD_INDEX], 0.00001);

            final double area = MathCalculator.Geometry.area(sideA, sideB);
            assertEquals(31.5, area, 0.1);

            final double perimeter = MathCalculator.Geometry.perimeter(sideA, sideB, hypotenuse);
            assertEquals(27.40175, perimeter, 0.00001);
        }

        @Test
        void testAreaOfSquare() {
            // given
            final byte sideLength = 4; // cm
            // when
            final double area = MathCalculator.Geometry.areaOfSquare(sideLength);
            // then
            assertEquals(16, area, 0.1);
        }

        @Test
        void testAreaOfRectangle() {
            // given
            final byte sideLengthA = 2; // cm
            final byte sideLengthB = 4; // cm
            // when
            final double area = MathCalculator.Geometry.areaOfRectangle(sideLengthA, sideLengthB);
            // then
            assertEquals(8, area, 0.1);
        }

        @Test
        void testAreaOfTriangleWithBaseAndHeight() {
            // given
            final byte base = 8; // cm
            final byte height = 4; // cm
            // when
            final double area = MathCalculator.Geometry.areaOfTriangleWithBaseAndHeight(base, height);
            // then
            assertEquals(16, area, 0.1);
        }

        @Test
        void testAreaOfTriangleWithSSS() {
            // given
            final byte sideLengthA = 2; // cm
            final byte sideLengthB = 5; // cm
            final byte sideLengthC = 4; // cm
            // when
            final double area = MathCalculator.Geometry.areaOfTriangleWithSSS(sideLengthA, sideLengthB, sideLengthC);
            // then
            assertEquals(3.8, area, 0.1);
        }

        @Test
        void testAreaOfTriangleWithSSSInvalidSideA() {
            // given
            final byte sideLengthA = 7; // cm
            final byte sideLengthB = 5; // cm
            final byte sideLengthC = 1; // cm
            // when
            final var exception = assertThrows(IllegalArgumentException.class,
                () -> MathCalculator.Geometry.areaOfTriangleWithSSS(sideLengthA, sideLengthB, sideLengthC));
            // then
            assertEquals("Side length (a) must be less than the sum of the other two sides to form a triangle",
                exception.getMessage());
        }

        @Test
        void testAreaOfTriangleWithSSSInvalidSideB() {
            // given
            final byte sideLengthA = 2; // cm
            final byte sideLengthB = 5; // cm
            final byte sideLengthC = 2; // cm
            // when
            final var exception = assertThrows(IllegalArgumentException.class,
                () -> MathCalculator.Geometry.areaOfTriangleWithSSS(sideLengthA, sideLengthB, sideLengthC));
            // then
            assertEquals("Side length (b) must be less than the sum of the other two sides to form a triangle",
                exception.getMessage());
        }

        @Test
        void testAreaOfTriangleWithSSSInvalidSideC() {
            // given
            final byte sideLengthA = 2; // cm
            final byte sideLengthB = 2; // cm
            final byte sideLengthC = 4; // cm
            // when
            final var exception = assertThrows(IllegalArgumentException.class,
                () -> MathCalculator.Geometry.areaOfTriangleWithSSS(sideLengthA, sideLengthB, sideLengthC));
            // then
            assertEquals("Side length (c) must be less than the sum of the other two sides to form a triangle",
                exception.getMessage());
        }

        @Test
        void testAreaOfTriangleWithSAS() {
            // given
            final byte sideLengthA = 2; // cm
            final byte sideLengthB = 5; // cm
            final double angleGammaRadians = Math.toRadians(30);
            // when
            final double area = MathCalculator.Geometry.areaOfTriangleWithSAS(
                sideLengthA, sideLengthB, angleGammaRadians);
            // then
            assertEquals(2.5, area, 0.1);
        }

        @Test
        void testAreaOfTriangleWithASA() {
            // given
            final byte sideLengthA = 2; // cm
            final double angleBetaRadians = Math.toRadians(30);
            final double angleGammaRadians = Math.toRadians(60);
            // when
            final double area = MathCalculator.Geometry.areaOfTriangleWithASA(
                sideLengthA, angleBetaRadians, angleGammaRadians);
            // then
            assertEquals(0.866, area, 0.001);
        }

        @Test
        void testCircleArea() {
            // given
            final byte radius = 5; // cm
            // when
            final double area = MathCalculator.Geometry.circleArea(radius);
            // then
            assertEquals(78.54, area, 0.01);
        }

        @Test
        void testSemicircleArea() {
            // given
            final byte radius = 5; // cm
            // when
            final double area = MathCalculator.Geometry.semicircleArea(radius);
            // then
            assertEquals(39.27, area, 0.01);
        }

        @Test
        void testSectorArea() {
            // given
            final byte radius = 5; // cm
            final double angleAlphaRadians = Math.toRadians(30);
            // when
            final double area = MathCalculator.Geometry.sectorArea(radius, angleAlphaRadians);
            // then
            assertEquals(6.545, area, 0.001);
        }

        @Test
        void testEllipseArea() {
            // given
            final byte radiusA = 5; // cm
            final byte radiusB = 3; // cm
            // when
            final double area = MathCalculator.Geometry.ellipseArea(radiusA, radiusB);
            // then
            assertEquals(47.12, area, 0.01);
        }

        @Test
        void testTrapezoidArea() {
            // given
            final byte sideA = 2; // cm
            final byte sideB = 8; // cm
            final byte height = 4; // cm
            // when
            final double area = MathCalculator.Geometry.trapezoidArea(sideA, sideB, height);
            // then
            assertEquals(20, area, 0.1);
        }

        @Test
        void testParallelogramAreaWithBaseAndHeight() {
            // given
            final byte base = 8; // cm
            final byte height = 4; // cm
            // when
            final double area = MathCalculator.Geometry.parallelogramAreaWithBaseAndHeight(base, height);
            // then
            assertEquals(32, area, 0.1);
        }

        @Test
        void testParallelogramAreaWithSidesAndAngle() {
            // given
            final byte sideA = 2; // cm
            final byte sideB = 8; // cm
            final double angleAlphaRadians = Math.toRadians(60);
            // when
            final double area = MathCalculator.Geometry
                .parallelogramAreaWithSidesAndAngle(sideA, sideB, angleAlphaRadians);
            // then
            assertEquals(13.856, area, 0.001);
        }

        @Test
        void testParallelogramAreaWithDiagonalsAndAngle() {
            // given
            final byte diagonal1 = 6; // cm
            final byte diagonal2 = 4; // cm
            final double angleThetaRadians = Math.toRadians(45);
            // when
            final double area = MathCalculator.Geometry
                .parallelogramAreaWithDiagonalsAndAngle(diagonal1, diagonal2, angleThetaRadians);
            // then
            assertEquals(16.97, area, 0.01);
        }

        @Test
        void testRhombusAreaWithSideAndHeight() {
            // given
            final byte side = 4; // cm
            final byte height = 6; // cm
            // when
            final double area = MathCalculator.Geometry.rhombusAreaWithSideAndHeight(side, height);
            // then
            assertEquals(24, area, 0.1);
        }

        @Test
        void testRhombusAreaWithDiagonals() {
            // given
            final byte diagonal1 = 6; // cm
            final byte diagonal2 = 4; // cm
            // when
            final double area = MathCalculator.Geometry.rhombusAreaWithDiagonals(diagonal1, diagonal2);
            // then
            assertEquals(12, area, 0.1);
        }

        @Test
        void testRhombusAreaWithSideAndAngle() {
            // given
            final byte side = 4; // cm
            final double angleAlphaRadians = Math.toRadians(60);
            // when
            final double area = MathCalculator.Geometry.rhombusAreaWithSideAndAngle(side, angleAlphaRadians);
            // then
            assertEquals(13.856, area, 0.001);
        }

        @Test
        void testKiteAreaWithDiagonals() {
            // given
            final byte diagonal1 = 4; // cm
            final byte diagonal2 = 10; // cm
            // when
            final double area = MathCalculator.Geometry.kiteAreaWithDiagonals(diagonal1, diagonal2);
            // then
            assertEquals(20, area, 0.1);
        }

        @Test
        void testKiteAreaWithSidesAndAngle() {
            // given
            final byte sideA = 2; // cm
            final byte sideB = 7; // cm
            final double angleAlphaRadians = Math.toRadians(45);
            // when
            final double area = MathCalculator.Geometry
                .kiteAreaWithSidesAndAngle(sideA, sideB, angleAlphaRadians);
            // then
            assertEquals(9.9, area, 0.1);
        }

        @Test
        void testPentagonArea() {
            // given
            final byte sideLength = 4; // cm
            // when
            final double area = MathCalculator.Geometry.pentagonArea(sideLength);
            // then
            assertEquals(27.53, area, 0.01);
        }

        @Test
        void testHexagonArea() {
            // given
            final byte sideLength = 4; // cm
            // when
            final double area = MathCalculator.Geometry.hexagonArea(sideLength);
            // then
            assertEquals(41.57, area, 0.01);
        }

        @Test
        void testOctagonArea() {
            // given
            final byte sideLength = 3; // cm
            // when
            final double area = MathCalculator.Geometry.octagonArea(sideLength);
            // then
            assertEquals(43.46, area, 0.01);
        }

        @Test
        void testAnnulusArea() {
            // given
            final double innerRadius = 1.5; // cm
            final double radius = 4; // cm
            // when
            final double area = MathCalculator.Geometry.annulusArea(radius, innerRadius);
            // then
            assertEquals(43.2, area, 0.1);
        }

        @Test
        void testIrregularQuadrilateralArea() {
            // given
            final byte diagonal1 = 4; // cm
            final byte diagonal2 = 5; // cm
            final double angleAlphaRadians = Math.toRadians(35);
            // when
            final double area = MathCalculator.Geometry
                .irregularQuadrilateralArea(diagonal1, diagonal2, angleAlphaRadians);
            // then
            assertEquals(11.472, area, 0.001);
        }

        @Test
        void testPolygonArea() {
            // given
            final byte numberOfSides = 12;
            final byte sideLength = 2; // cm
            // when
            final double area = MathCalculator.Geometry.polygonArea(numberOfSides, sideLength);
            // then
            assertEquals(44.785, area, 0.001);
        }

        @Test
        void testScaleneTriangleHeight() {
            // given
            final byte sideA = 6;
            final byte sideB = 14;
            final byte sideC = 17;
            // when
            final double[] heights = MathCalculator.Geometry.scaleneTriangleHeight(sideA, sideB, sideC);
            // then
            assertNotNull(heights);
            assertEquals(3, heights.length);
            assertEquals(13.17, heights[Constants.ARR_1ST_INDEX], 0.01);
            final double heightB = heights[Constants.ARR_2ND_INDEX];
            assertEquals(5.644, heightB, 0.001);
            assertEquals(4.648, heights[Constants.ARR_3RD_INDEX], 0.001);

            final double areaCmSquared = MathCalculator.Geometry.areaOfTriangleWithSSS(sideA, sideB, sideC);
            assertEquals(39.51, areaCmSquared, 0.01);

            final double perimeter = MathCalculator.Geometry.perimeter(sideA, sideB, sideC);
            assertEquals(37, perimeter, 0.1);

            final double[] angles = MathCalculator.Trigonometry.lawOfCosSSS(sideA, sideB, sideC);
            assertEquals(0.3384, angles[Constants.ALPHA_INDEX], 0.0001);
            assertEquals(0.8862, angles[Constants.BETA_INDEX], 0.0001);
            assertEquals(1.917, angles[Constants.GAMMA_INDEX], 0.001);
        }

        @Test
        void testEquilateralTriangleHeight() {
            // given
            final byte sides = 5;
            // when
            final double height = MathCalculator.Geometry.equilateralTriangleHeight(sides);
            // then
            assertEquals(4.33, height, 0.01);

            final double areaCmSquared = MathCalculator.Geometry.equilateralTriangleArea(sides);
            assertEquals(10.825, areaCmSquared, 0.001);

            final double perimeter = MathCalculator.Geometry.perimeter(sides, sides, sides);
            assertEquals(15, perimeter, 0.1);
        }

        @Test
        void testIsoscelesTriangleHeight() {
            // given
            final byte sideA = 3;
            final byte sideB = 5;
            // when
            final double[] heights = MathCalculator.Geometry.isoscelesTriangleHeight(sideA, sideB);
            // then
            assertNotNull(heights);
            assertEquals(2, heights.length);
            assertEquals(2.764, heights[Constants.ARR_1ST_INDEX], 0.001);
            final double heightB = heights[Constants.ARR_2ND_INDEX];
            assertEquals(1.6583, heightB, 0.0001);

            final double areaCmSquared = MathCalculator.Geometry.isoscelesTriangleArea(sideB, heightB);
            assertEquals(4.146, areaCmSquared, 0.001);

            final double perimeter = MathCalculator.Geometry.perimeter(sideA, sideB, sideA);
            assertEquals(11, perimeter, 0.1);

            final double[] angles = MathCalculator.Trigonometry.lawOfCosSSS(sideB, sideA, sideA);
            assertEquals(1.9702, angles[Constants.ALPHA_INDEX], 0.0001);
            assertEquals(0.5857, angles[Constants.BETA_INDEX], 0.0001);
        }

        @Test
        void testRightTriangleHeight() {
            // given
            final byte sideA = 3;
            final byte sideB = 4;
            final byte sideC = 5;
            // when
            final double[] heights = MathCalculator.Geometry.rightTriangleHeight(sideA, sideB, sideC);
            // then
            assertNotNull(heights);
            assertEquals(3, heights.length);
            assertEquals(4, heights[Constants.ARR_1ST_INDEX], 0.1);
            assertEquals(3, heights[Constants.ARR_2ND_INDEX], 0.1);
            assertEquals(2.4, heights[Constants.ARR_3RD_INDEX], 0.1);

            final double areaCmSquared = MathCalculator.Geometry.areaOfTriangleWithSSS(sideA, sideB, sideC);
            assertEquals(6, areaCmSquared, 0.1);

            final double perimeter = MathCalculator.Geometry.perimeter(sideA, sideB, sideC);
            assertEquals(12, perimeter, 0.1);

            final double[] angles = MathCalculator.Trigonometry.lawOfCosSSS(sideA, sideB, sideC);
            assertEquals(0.6435, angles[Constants.ALPHA_INDEX], 0.0001);
            assertEquals(0.9273, angles[Constants.BETA_INDEX], 0.0001);
        }

        @Test
        void testHeronFormulaUsingSemiperimeter() {
            // given
            final byte sideA = 12;
            final byte sideB = 5;
            final byte sideC = 13;
            // when
            final double areaCmSquared = MathCalculator.Geometry.heronFormulaUsingSemiperimeter(sideA, sideB, sideC);
            // then
            assertEquals(30, areaCmSquared, 0.1);
        }

        @Test
        void testHeronFormulaUsingQuadProduct() {
            // given
            final byte sideA = 12;
            final byte sideB = 5;
            final byte sideC = 13;
            // when
            final double areaCmSquared = MathCalculator.Geometry.heronFormulaUsingQuadProduct(sideA, sideB, sideC);
            // then
            assertEquals(30, areaCmSquared, 0.1);
        }

        @Test
        void testAreaWithBaseAndHeight() {
            // given
            final byte sideA = 12;
            final byte sideB = 5;
            final byte sideC = 13;
            final double[] heights = MathCalculator.Geometry.scaleneTriangleHeight(sideA, sideB, sideC);
            final double height = heights[Constants.ARR_3RD_INDEX];
            // when
            final double areaCmSquared = MathCalculator.Geometry.areaWithBaseAndHeight(sideC, height);
            // then
            assertEquals(30, areaCmSquared, 0.1);
        }

        static List<Arguments> isEquilateralTriangleArgs() {
            return List.of(
                Arguments.of(new double[]{12, 5, 13}, false),
                Arguments.of(new double[]{5, 5, 5}, true)
            );
        }

        @ParameterizedTest
        @MethodSource("isEquilateralTriangleArgs")
        void testIsEquilateralTriangle(double[] sides, boolean expectedResult) {
            // given
            final double sideA = sides[Constants.ARR_1ST_INDEX];
            final double sideB = sides[Constants.ARR_2ND_INDEX];
            final double sideC = sides[Constants.ARR_3RD_INDEX];
            // when
            final boolean equilateral = MathCalculator.Geometry.isEquilateralTriangle(sideA, sideB, sideC);
            // then
            assertEquals(expectedResult, equilateral);
        }

        static List<Arguments> isScaleneTriangleArgs() {
            return List.of(
                Arguments.of(new double[]{12, 5, 13}, true),
                Arguments.of(new double[]{5, 5, 5}, false)
            );
        }

        @ParameterizedTest
        @MethodSource("isScaleneTriangleArgs")
        void testIsScaleneTriangle(double[] sides, boolean expectedResult) {
            // given
            final double sideA = sides[Constants.ARR_1ST_INDEX];
            final double sideB = sides[Constants.ARR_2ND_INDEX];
            final double sideC = sides[Constants.ARR_3RD_INDEX];
            // when
            final boolean equilateral = MathCalculator.Geometry.isScaleneTriangle(sideA, sideB, sideC);
            // then
            assertEquals(expectedResult, equilateral);
        }

        private static double[] acuteIsoscelesTriangleAngles() {
            return new double[]{60, 60, 60};
        }

        private static double[] rightScaleneTriangleAngles() {
            return new double[]{30, 60, 90};
        }

        private static double[] rightIsoscelesTriangleAngles() {
            return new double[]{45, 45, 90};
        }

        private static double[] obtuseScaleneTriangleAngles() {
            return new double[]{30, 45, 105};
        }

        static List<Arguments> isAcuteTriangleArgs() {
            return List.of(
                Arguments.of(acuteIsoscelesTriangleAngles(), true),
                Arguments.of(obtuseScaleneTriangleAngles(), false),
                Arguments.of(rightScaleneTriangleAngles(), false),
                Arguments.of(rightIsoscelesTriangleAngles(), false)
            );
        }

        @ParameterizedTest
        @MethodSource("isAcuteTriangleArgs")
        void testIsAcuteTriangle(double[] angles, boolean expectedResult) {
            // given
            final double angleAlphaRad = Math.toRadians(angles[Constants.ALPHA_INDEX]);
            final double angleBetaRad = Math.toRadians(angles[Constants.BETA_INDEX]);
            final double angleGammaRad = Math.toRadians(angles[Constants.GAMMA_INDEX]);
            // when
            final boolean acute = MathCalculator.Geometry.isAcuteTriangle(angleAlphaRad, angleBetaRad, angleGammaRad);
            // then
            assertEquals(expectedResult, acute);
        }

        private static double[] acuteScaleneTriangleWithSSA() {
            return new double[]{45, 4, 5};
        }

        private static double[] rightScaleneTriangleWithSSA() {
            return new double[]{90, 3, 4};
        }

        private static double[] obtuseScaleneTriangleWithSSA() {
            return new double[]{30, 3, 4};
        }

        static List<Arguments> isAcuteTriangleWithSSAArgs() {
            return List.of(
                Arguments.of(acuteScaleneTriangleWithSSA(), true),
                Arguments.of(obtuseScaleneTriangleWithSSA(), false),
                Arguments.of(rightScaleneTriangleWithSSA(), false)
            );
        }

        @ParameterizedTest
        @MethodSource("isAcuteTriangleWithSSAArgs")
        void testIsAcuteTriangleWithSSAArgs(double[] data, boolean expectedResult) {
            // given
            final double angleAlphaRad = Math.toRadians(data[Constants.ALPHA_INDEX]);
            final double sideA = data[Constants.ARR_2ND_INDEX];
            final double sideB = data[Constants.ARR_3RD_INDEX];
            // when
            final boolean acute = MathCalculator.Geometry.isAcuteTriangleWithSSA(angleAlphaRad, sideA, sideB);
            // then
            assertEquals(expectedResult, acute);
        }

        static List<Arguments> isRightTriangleArgs() {
            return List.of(
                Arguments.of(acuteIsoscelesTriangleAngles(), false),
                Arguments.of(obtuseScaleneTriangleAngles(), false),
                Arguments.of(rightScaleneTriangleAngles(), true),
                Arguments.of(rightIsoscelesTriangleAngles(), true)
            );
        }

        @ParameterizedTest
        @MethodSource("isRightTriangleArgs")
        void testIsRightTriangle(double[] angles, boolean expectedResult) {
            // given
            final double angleAlphaRad = Math.toRadians(angles[Constants.ALPHA_INDEX]);
            final double angleBetaRad = Math.toRadians(angles[Constants.BETA_INDEX]);
            final double angleGammaRad = Math.toRadians(angles[Constants.GAMMA_INDEX]);
            // when
            final boolean right = MathCalculator.Geometry.isRightTriangle(angleAlphaRad, angleBetaRad, angleGammaRad);
            // then
            assertEquals(expectedResult, right);
        }

        static List<Arguments> isRightTriangleWithSSAArgs() {
            return List.of(
                Arguments.of(acuteScaleneTriangleWithSSA(), false),
                Arguments.of(obtuseScaleneTriangleWithSSA(), false),
                Arguments.of(rightScaleneTriangleWithSSA(), true)
            );
        }

        @ParameterizedTest
        @MethodSource("isRightTriangleWithSSAArgs")
        void testIsRightTriangleWithSSA(double[] data, boolean expectedResult) {
            // given
            final double angleAlphaRad = Math.toRadians(data[Constants.ALPHA_INDEX]);
            final double sideA = data[Constants.ARR_2ND_INDEX];
            final double sideB = data[Constants.ARR_3RD_INDEX];
            // when
            final boolean right = MathCalculator.Geometry.isRightTriangleWithSSA(angleAlphaRad, sideA, sideB);
            // then
            assertEquals(expectedResult, right);
        }

        static List<Arguments> isObtuseTriangleArgs() {
            return List.of(
                Arguments.of(acuteIsoscelesTriangleAngles(), false),
                Arguments.of(obtuseScaleneTriangleAngles(), true),
                Arguments.of(rightScaleneTriangleAngles(), false),
                Arguments.of(rightIsoscelesTriangleAngles(), false)
            );
        }

        @ParameterizedTest
        @MethodSource("isObtuseTriangleArgs")
        void testIsObtuseTriangle(double[] angles, boolean expectedResult) {
            // given
            final double angleAlphaRad = Math.toRadians(angles[Constants.ALPHA_INDEX]);
            final double angleBetaRad = Math.toRadians(angles[Constants.BETA_INDEX]);
            final double angleGammaRad = Math.toRadians(angles[Constants.GAMMA_INDEX]);
            // when
            final boolean obtuse = MathCalculator.Geometry.isObtuseTriangle(angleAlphaRad, angleBetaRad, angleGammaRad);
            // then
            assertEquals(expectedResult, obtuse);
        }

        static List<Arguments> isObtuseTriangleWithSSAArgs() {
            return List.of(
                Arguments.of(acuteScaleneTriangleWithSSA(), false),
                Arguments.of(obtuseScaleneTriangleWithSSA(), true),
                Arguments.of(rightScaleneTriangleWithSSA(), false)
            );
        }

        @ParameterizedTest
        @MethodSource("isObtuseTriangleWithSSAArgs")
        void testIsObtuseTriangleWithSSA(double[] data, boolean expectedResult) {
            // given
            final double angleAlphaRad = Math.toRadians(data[Constants.ALPHA_INDEX]);
            final double sideA = data[Constants.ARR_2ND_INDEX];
            final double sideB = data[Constants.ARR_3RD_INDEX];
            // when
            final boolean obtuse = MathCalculator.Geometry.isObtuseTriangleWithSSA(angleAlphaRad, sideA, sideB);
            // then
            assertEquals(expectedResult, obtuse);
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
                Arguments.of(new double[]{2}, new double[]{3}, 1), // 1d
                Arguments.of(new double[]{2, 9}, new double[]{3, 5}, 5), // 2d
                Arguments.of(new double[]{2, 9, 4}, new double[]{3, 5, 6}, 7), // 3d
                Arguments.of(new double[]{2, 9, 4, 1}, new double[]{3, 5, 6, 7}, 13) // 4d
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
                Arguments.of(new double[]{3}, new double[]{9}, 6, 0.1), // 1d
                Arguments.of(new double[]{3, 5}, new double[]{9, 15}, 11.6619, 0.0001), // 2d
                Arguments.of(new double[]{3, 5, 2}, new double[]{9, 15, 5}, 12.0416, 0.0001), // 3d
                Arguments.of(new double[]{3, 5, 2, 3}, new double[]{9, 15, 5, 1}, 12.20656, 0.00001) // 4d
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
            final double constantTerm = MathCalculator.CoordinateGeometry.slopeInterceptConstantTerm(
                pointACoords[Constants.X_INDEX], pointACoords[Constants.Y_INDEX], slope);
            // then
            assertEquals(0.166667, slope, 0.000001);
            assertEquals(0.16515, angleTheta, 0.00001);
            assertEquals(6.0828, distance, 0.0001);
            assertEquals(6, deltaX, 0.1);
            assertEquals(1, deltaY, 0.1);
            assertEquals(4.833, constantTerm, 0.001);
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

        @Test
        void testMidpoint() {
            // given
            final double[] pointACoords = {0, 2};
            final double[] pointBCoords = {2, 8};
            // when
            final double[] midpointCoords = MathCalculator.CoordinateGeometry.midpoint(pointACoords, pointBCoords);
            // then
            assertNotNull(midpointCoords);
            assertEquals(2, midpointCoords.length);
            assertEquals(1, midpointCoords[Constants.X_INDEX], 0.1);
            assertEquals(5, midpointCoords[Constants.Y_INDEX], 0.1);
        }

        @Test
        void testEndpointWithGivenMidpoint() {
            // given
            final byte point = 2;
            final byte midpoint = 5;
            // when
            final double coordinate = MathCalculator.CoordinateGeometry.endpointWithGivenMidpoint(point, midpoint);
            // then
            assertEquals(8, coordinate, 0.1);
        }
    }

    @Nested
    class Trigonometry {
        static List<Arguments> sinParams() {
            return List.of(
                Arguments.of(MathCalculator.Trigonometry.PI_OVER_12, 0.2588190451),
                Arguments.of(MathCalculator.Trigonometry.PI_OVER_6, 0.5),
                Arguments.of(MathCalculator.Trigonometry.PI_OVER_4, 0.7071067812),
                Arguments.of(MathCalculator.Trigonometry.PI_OVER_3, 0.8660254038),
                Arguments.of(MathCalculator.Trigonometry.PI5_OVER_12, 0.9659258263),
                Arguments.of(MathCalculator.Trigonometry.PI_OVER_2, 1),
                Arguments.of(MathCalculator.Trigonometry.PI7_OVER_12, 0.9659258263),
                Arguments.of(MathCalculator.Trigonometry.PI2_OVER_3, 0.8660254038),
                Arguments.of(MathCalculator.Trigonometry.PI3_OVER_4, 0.7071067812),
                Arguments.of(MathCalculator.Trigonometry.PI5_OVER_6, 0.5),
                Arguments.of(MathCalculator.Trigonometry.PI11_OVER_12, 0.2588190451)
            );
        }

        @ParameterizedTest
        @MethodSource("sinParams")
        void testSin(double angleAlphaRadians, double expectedResult) {
            // when
            final double sine = MathCalculator.Trigonometry.sin(angleAlphaRadians);
            // then
            assertEquals(expectedResult, sine, 0.0000000001);
        }

        @Test
        void testSinusoid() {
            // given
            final double anglePhiRadians = Math.toRadians(40);
            final byte oscillationFrequency = 10;
            final double amplitude = 0.8;
            final byte timeSeconds = 10;
            // when
            final double sinusoid = MathCalculator.Trigonometry.sinusoid(
                amplitude, anglePhiRadians, oscillationFrequency, timeSeconds);
            // then
            assertEquals(0.51423008, sinusoid, 0.00000001);
        }

        static List<Arguments> quadrantParams() {
            return List.of(
                Arguments.of(MathCalculator.Trigonometry.PI_OVER_12, 1),
                Arguments.of(MathCalculator.Trigonometry.PI_OVER_6, 1),
                Arguments.of(MathCalculator.Trigonometry.PI_OVER_4, 1),
                Arguments.of(MathCalculator.Trigonometry.PI_OVER_3, 1),
                Arguments.of(MathCalculator.Trigonometry.PI5_OVER_12, 1),
                Arguments.of(MathCalculator.Trigonometry.PI_OVER_2, 1),
                Arguments.of(MathCalculator.Trigonometry.PI7_OVER_12, 2),
                Arguments.of(MathCalculator.Trigonometry.PI2_OVER_3, 2),
                Arguments.of(MathCalculator.Trigonometry.PI3_OVER_4, 2),
                Arguments.of(MathCalculator.Trigonometry.PI5_OVER_6, 2),
                Arguments.of(MathCalculator.Trigonometry.PI11_OVER_12, 2),
                Arguments.of(Math.PI, 2),
                Arguments.of(MathCalculator.Trigonometry.PI7_OVER_6, 3),
                Arguments.of(MathCalculator.Trigonometry.PI5_OVER_4, 3),
                Arguments.of(MathCalculator.Trigonometry.PI4_OVER_3, 3),
                Arguments.of(MathCalculator.Trigonometry.PI3_OVER_2, 3),
                Arguments.of(MathCalculator.Trigonometry.PI5_OVER_3, 4),
                Arguments.of(MathCalculator.Trigonometry.PI7_OVER_4, 4),
                Arguments.of(MathCalculator.Trigonometry.PI11_OVER_6, 4),
                Arguments.of(MathCalculator.Trigonometry.PI2, 4)
            );
        }

        @ParameterizedTest
        @MethodSource("quadrantParams")
        void testQuadrant(double angleAlphaRadians, int expectedResult) {
            // when
            final int quadrant = MathCalculator.Trigonometry.quadrant(angleAlphaRadians);
            // then
            assertEquals(expectedResult, quadrant);
        }

        @Test
        void testLawOfTangents() {
            // given
            final double angleAlphaRadians = Math.toRadians(30);
            final double angleBetaRadians = Math.toRadians(45);
            // when
            final double result = MathCalculator.Trigonometry.lawOfTangents(angleAlphaRadians, angleBetaRadians);
            // then
            assertEquals(-0.171572, result, 0.000001);
        }

        static List<Arguments> cotangentOfAngleParams() {
            return List.of(
                Arguments.of(Math.toRadians(30), 1.73205081, 0.00000001),
                Arguments.of(Math.toRadians(45), 1, 0.1),
                Arguments.of(Math.toRadians(60), 0.57735027, 0.00000001),
                Arguments.of(Math.toRadians(75), 0.26794919, 0.00000001)
            );
        }

        @ParameterizedTest
        @MethodSource("cotangentOfAngleParams")
        void testCotangentOfAngle(double angleAlphaRadians, double expectedResult, double delta) {
            // when
            final double result = MathCalculator.Trigonometry.cot(angleAlphaRadians);
            // then
            assertEquals(expectedResult, result, delta);
        }

        static List<Arguments> cotangentParams() {
            return List.of(
                Arguments.of(2 * Math.sqrt(3), 2, 1.73205081, 0.00000001),
                Arguments.of(3 * Math.sqrt(2) / 2, 3 * Math.sqrt(2) / 2, 1, 0.1),
                Arguments.of(4, 4 * Math.sqrt(3), 0.57735027, 0.00000001)
            );
        }

        @ParameterizedTest
        @MethodSource("cotangentParams")
        void testCotangent(double adjacent, double opposite, double expectedResult, double delta) {
            // when
            final double result = MathCalculator.Trigonometry.cot(adjacent, opposite);
            // then
            assertEquals(expectedResult, result, delta);
        }

        static List<Arguments> secantOfAngleParams() {
            return List.of(
                Arguments.of(Math.toRadians(30), 1.15470054, 0.00000001),
                Arguments.of(Math.toRadians(45), 1.41421356, 0.00000001),
                Arguments.of(Math.toRadians(60), 2, 0.1),
                Arguments.of(Math.toRadians(75), 3.86370331, 0.00000001)
            );
        }

        @ParameterizedTest
        @MethodSource("secantOfAngleParams")
        void testSecantOfAngle(double angleAlphaRadians, double expectedResult, double delta) {
            // when
            final double result = MathCalculator.Trigonometry.sec(angleAlphaRadians);
            // then
            assertEquals(expectedResult, result, delta);
        }

        static List<Arguments> secantParams() {
            return List.of(
                Arguments.of(2 * 3, 3 * Math.sqrt(3), 1.15470054, 0.00000001),
                Arguments.of(5 * Math.sqrt(2), 5, 1.41421356, 0.00000001),
                Arguments.of(2 * 4, 4, 2, 0.1)
            );
        }

        @ParameterizedTest
        @MethodSource("secantParams")
        void testSecant(double hypotenuse, double adjacent, double expectedResult, double delta) {
            // when
            final double result = MathCalculator.Trigonometry.sec(hypotenuse, adjacent);
            // then
            assertEquals(expectedResult, result, delta);
        }

        static List<Arguments> cosecantOfAngleParams() {
            return List.of(
                Arguments.of(Math.toRadians(30), 2, 0.1),
                Arguments.of(Math.toRadians(45), 1.41421356, 0.00000001),
                Arguments.of(Math.toRadians(60), 1.15470054, 0.00000001),
                Arguments.of(Math.toRadians(75), 1.03527618, 0.00000001)
            );
        }

        @ParameterizedTest
        @MethodSource("cosecantOfAngleParams")
        void testCosecantOfAngle(double angleAlphaRadians, double expectedResult, double delta) {
            // when
            final double result = MathCalculator.Trigonometry.csc(angleAlphaRadians);
            // then
            assertEquals(expectedResult, result, delta);
        }

        static List<Arguments> cosecantParams() {
            return List.of(
                Arguments.of(2 * 3, 3, 2, 0.1),
                Arguments.of(5 * Math.sqrt(2), 5, 1.41421356, 0.00000001),
                Arguments.of(2 * 4, 4 * Math.sqrt(3), 1.15470054, 0.00000001)
            );
        }

        @ParameterizedTest
        @MethodSource("cosecantParams")
        void testCosecant(double hypotenuse, double opposite, double expectedResult, double delta) {
            // when
            final double result = MathCalculator.Trigonometry.csc(hypotenuse, opposite);
            // then
            assertEquals(expectedResult, result, delta);
        }

        @Test
        void testCosHalfAngle() {
            // given
            final double angleRadians = Math.toRadians(30);
            // when
            final double result = MathCalculator.Trigonometry.cosHalfAngle(angleRadians);
            // then
            assertEquals(0.96592583, result, 0.00000001);
        }

        @Test
        void testSinHalfAngle() {
            // given
            final double angleRadians = Math.toRadians(30);
            // when
            final double result = MathCalculator.Trigonometry.sinHalfAngle(angleRadians);
            // then
            assertEquals(0.25881905, result, 0.00000001);
        }

        @Test
        void testTanHalfAngle() {
            // given
            final double angleRadians = Math.toRadians(30);
            // when
            final double result = MathCalculator.Trigonometry.tanHalfAngle(angleRadians);
            // then
            assertEquals(0.26794919, result, 0.00000001);
        }

        @Test
        void testSinDoubleAngle() {
            // given
            final double angleThetaRadians = Math.toRadians(15);
            // when
            final double result = MathCalculator.Trigonometry.sinDoubleAngle(angleThetaRadians);
            // then
            assertEquals(0.5, result, 0.1);
        }

        @Test
        void testCosDoubleAngle() {
            // given
            final double angleThetaRadians = Math.toRadians(15);
            // when
            final double result = MathCalculator.Trigonometry.cosDoubleAngle(angleThetaRadians);
            // then
            assertEquals(0.86603, result, 0.00001);
        }

        @Test
        void testTanDoubleAngle() {
            // given
            final double angleThetaRadians = Math.toRadians(15);
            // when
            final double result = MathCalculator.Trigonometry.tanDoubleAngle(angleThetaRadians);
            // then
            assertEquals(0.57735, result, 0.00001);
        }

        @Test
        void testSinPhaseShift() {
            // given
            final double amplitude = 0.5; // A
            final byte x = 1;
            final double periodRadians = 2; // B
            final byte phaseShift = 3; // C
            final byte verticalShift = 4; // D
            // when
            final double[] results = MathCalculator.Trigonometry
                .sinPhaseShift(x, amplitude, periodRadians, phaseShift, verticalShift);
            // then
            assertNotNull(results);
            assertEquals(3, results.length);
            assertEquals(1.5, results[Constants.ARR_1ST_INDEX], 0.1);
            assertEquals(3.1416, results[Constants.ARR_2ND_INDEX], 0.001);
            assertEquals(3.5792, results[Constants.ARR_3RD_INDEX], 0.001);
        }

        @Test
        void testCosPhaseShift() {
            // given
            final double amplitude = 0.5; // A
            final byte x = 1;
            final double periodRadians = 2; // B
            final byte phaseShift = 3; // C
            final byte verticalShift = 4; // D
            // when
            final double[] results = MathCalculator.Trigonometry
                .cosPhaseShift(x, amplitude, periodRadians, phaseShift, verticalShift);
            // then
            assertNotNull(results);
            assertEquals(3, results.length);
            assertEquals(1.5, results[Constants.ARR_1ST_INDEX], 0.1);
            assertEquals(3.1416, results[Constants.ARR_2ND_INDEX], 0.001);
            assertEquals(4.27, results[Constants.ARR_3RD_INDEX], 0.01);
        }

        @Test
        void testSinPowerReducing() {
            // given
            final double angleRadians = Math.toRadians(15);
            // when
            final double squaredResult = MathCalculator.Trigonometry.sinPowerReducing(angleRadians);
            // then
            assertEquals(0.0669873, squaredResult, 0.0000001);
            final double inverseDegrees = Math.toDegrees(BigDecimal.valueOf(Math.sqrt(squaredResult))
                .setScale(4, RoundingMode.HALF_UP).doubleValue());
            assertEquals(14.828, inverseDegrees, 0.001);
        }

        @Test
        void testCosPowerReducing() {
            // given
            final double angleRadians = Math.toRadians(15);
            // when
            final double squaredResult = MathCalculator.Trigonometry.cosPowerReducing(angleRadians);
            // then
            assertEquals(0.9330127, squaredResult, 0.0000001);
        }

        @Test
        void testTanPowerReducing() {
            // given
            final double angleRadians = Math.toRadians(15);
            // when
            final double squaredResult = MathCalculator.Trigonometry.tanPowerReducing(angleRadians);
            // then
            assertEquals(0.0718, squaredResult, 0.0001);
        }

        @Test
        void testLawOfCosSAS() {
            // given
            final byte sideA = 5;
            final byte sideB = 6;
            final double angleRadians = Math.toRadians(30);
            // when
            final double sideC = MathCalculator.Trigonometry.lawOfCosSAS(sideA, sideB, angleRadians);
            // then
            assertEquals(3.006406, sideC, 0.000001);
        }

        @Test
        void testLawOfCosSSS() {
            // given
            final byte sideA = 4;
            final byte sideB = 5;
            final byte sideC = 6;
            // when
            final double[] results = MathCalculator.Trigonometry.lawOfCosSSS(sideA, sideB, sideC);
            // then
            assertNotNull(results);
            assertEquals(3, results.length);
            assertEquals(0.7227, results[Constants.ALPHA_INDEX], 0.0001);
            assertEquals(0.9734, results[Constants.BETA_INDEX], 0.0001);
            assertEquals(1.4455, results[Constants.GAMMA_INDEX], 0.0001);
        }

        @Test
        void testLawOfSinGivenSideAAndAnglesAlphaBeta() {
            // given
            final double sideA = 2.3094;
            final double angleAlphaRadians = Math.toRadians(30);
            final double angleBetaRadians = Math.toRadians(60);
            // when
            final double sideB = MathCalculator.Trigonometry
                .lawOfSinGivenSideAAndAnglesAlphaBeta(sideA, angleAlphaRadians, angleBetaRadians);
            // then
            assertEquals(4, sideB, 0.0001);
        }

        @Test
        void testLawOfSinGivenSideBAndAnglesAlphaBeta() {
            // given
            final byte sideB = 4;
            final double angleAlphaRadians = Math.toRadians(30);
            final double angleBetaRadians = Math.toRadians(60);
            // when
            final double sideA = MathCalculator.Trigonometry
                .lawOfSinGivenSideBAndAnglesAlphaBeta(sideB, angleAlphaRadians, angleBetaRadians);
            // then
            assertEquals(2.3094, sideA, 0.0001);
        }

        @Test
        void testLawOfSinGivenSidesABAndAngleAlpha() {
            // given
            final double sideA = 2.3094;
            final byte sideB = 4;
            final double angleAlphaRadians = Math.toRadians(30);
            // when
            final double angleBetaRad = MathCalculator.Trigonometry
                .lawOfSinGivenSidesABAndAngleAlpha(sideA, sideB, angleAlphaRadians);
            // then
            assertEquals(Math.toRadians(60), angleBetaRad, 0.0001);
        }

        @Test
        void testLawOfSinGivenSidesABAndAngleBeta() {
            // given
            final double sideA = 2.3094;
            final byte sideB = 4;
            final double angleBetaRadians = Math.toRadians(60);
            // when
            final double angleAlphaRad = MathCalculator.Trigonometry
                .lawOfSinGivenSidesABAndAngleBeta(sideA, sideB, angleBetaRadians);
            // then
            assertEquals(Math.toRadians(30), angleAlphaRad, 0.0001);
        }

        @Test
        void testLawOfSinGivenSidesBCAndAngleBeta() {
            // given
            final byte sideB = 4;
            final byte sideC = 8;
            final double angleBetaRadians = Math.toRadians(30);
            // when
            final double angleGammaRad = MathCalculator.Trigonometry
                .lawOfSinGivenSidesBCAndAngleBeta(sideC, sideB, angleBetaRadians);
            // then
            assertEquals(Math.toRadians(90), angleGammaRad, 0.1);
        }

        @Test
        void testLawOfSinGivenSidesACAndAngleAlpha() {
            // given
            final double sideA = 2;
            final byte sideC = 4;
            final double angleAlphaRadians = Math.toRadians(30);
            // when
            final double angleGammaRad = MathCalculator.Trigonometry
                .lawOfSinGivenSidesACAndAngleAlpha(sideA, sideC, angleAlphaRadians);
            // then
            assertEquals(Math.toRadians(90), angleGammaRad, 0.1);
        }

        @Test
        void testSinAngleSum() {
            // given
            final double angleAlphaRadians = Math.toRadians(30);
            final double angleBetaRadians = Math.toRadians(90);
            // when
            final double angleSumRad = MathCalculator.Trigonometry.sinAngleSum(angleAlphaRadians, angleBetaRadians);
            // then
            assertEquals(0.866, angleSumRad, 0.001);
        }

        @Test
        void testSinAngleDifference() {
            // given
            final double angleAlphaRadians = Math.toRadians(30);
            final double angleBetaRadians = Math.toRadians(90);
            // when
            final double angleDiffRad = MathCalculator.Trigonometry
                .sinAngleDifference(angleAlphaRadians, angleBetaRadians);
            // then
            assertEquals(-0.866, angleDiffRad, 0.001);
        }

        @Test
        void testCosAngleSum() {
            // given
            final double angleAlphaRadians = Math.toRadians(30);
            final double angleBetaRadians = Math.toRadians(45);
            // when
            final double angleSumRad = MathCalculator.Trigonometry.cosAngleSum(angleAlphaRadians, angleBetaRadians);
            // then
            assertEquals(0.259, angleSumRad, 0.001);
        }

        @Test
        void testCosAngleDifference() {
            // given
            final double angleAlphaRadians = Math.toRadians(30);
            final double angleBetaRadians = Math.toRadians(45);
            // when
            final double angleDiffRad = MathCalculator.Trigonometry
                .cosAngleDifference(angleAlphaRadians, angleBetaRadians);
            // then
            assertEquals(0.966, angleDiffRad, 0.001);
        }

        @Test
        void testTanAngleSum() {
            // given
            final double angleAlphaRadians = Math.toRadians(30);
            final double angleBetaRadians = Math.toRadians(45);
            // when
            final double angleSumRad = MathCalculator.Trigonometry.tanAngleSum(angleAlphaRadians, angleBetaRadians);
            // then
            assertEquals(3.732, angleSumRad, 0.001);
        }

        @Test
        void testTanAngleDifference() {
            // given
            final double angleAlphaRadians = Math.toRadians(30);
            final double angleBetaRadians = Math.toRadians(45);
            // when
            final double angleDiffRad = MathCalculator.Trigonometry
                .tanAngleDifference(angleAlphaRadians, angleBetaRadians);
            // then
            assertEquals(-0.268, angleDiffRad, 0.001);
        }

        @Test
        void testCotAngleSum() {
            // given
            final double angleAlphaRadians = Math.toRadians(30);
            final double angleBetaRadians = Math.toRadians(45);
            // when
            final double angleSumRad = MathCalculator.Trigonometry.cotAngleSum(angleAlphaRadians, angleBetaRadians);
            // then
            assertEquals(0.268, angleSumRad, 0.001);
        }

        @Test
        void testCotAngleDifference() {
            // given
            final double angleAlphaRadians = Math.toRadians(30);
            final double angleBetaRadians = Math.toRadians(45);
            // when
            final double angleDiffRad = MathCalculator.Trigonometry
                .cotAngleDifference(angleAlphaRadians, angleBetaRadians);
            // then
            assertEquals(-3.732, angleDiffRad, 0.001);
        }

        @Test
        void testSecAngleSum() {
            // given
            final double angleAlphaRadians = Math.toRadians(30);
            final double angleBetaRadians = Math.toRadians(45);
            // when
            final double angleSumRad = MathCalculator.Trigonometry.secAngleSum(angleAlphaRadians, angleBetaRadians);
            // then
            assertEquals(3.864, angleSumRad, 0.001);
        }

        @Test
        void testSecAngleDifference() {
            // given
            final double angleAlphaRadians = Math.toRadians(30);
            final double angleBetaRadians = Math.toRadians(45);
            // when
            final double angleDiffRad = MathCalculator.Trigonometry
                .secAngleDifference(angleAlphaRadians, angleBetaRadians);
            // then
            assertEquals(1.035, angleDiffRad, 0.001);
        }

        @Test
        void testCscAngleSum() {
            // given
            final double angleAlphaRadians = Math.toRadians(30);
            final double angleBetaRadians = Math.toRadians(45);
            // when
            final double angleSumRad = MathCalculator.Trigonometry.cscAngleSum(angleAlphaRadians, angleBetaRadians);
            // then
            assertEquals(1.035, angleSumRad, 0.001);
        }

        @Test
        void testCscAngleDifference() {
            // given
            final double angleAlphaRadians = Math.toRadians(30);
            final double angleBetaRadians = Math.toRadians(45);
            // when
            final double angleDiffRad = MathCalculator.Trigonometry
                .cscAngleDifference(angleAlphaRadians, angleBetaRadians);
            // then
            assertEquals(-3.864, angleDiffRad, 0.001);
        }

        @Test
        void testFindSinWithCosAndTan() {
            // given
            final double angleAlphaRadians = Math.toRadians(60);
            // when
            final double sine = MathCalculator.Trigonometry.findSinWithCosAndTan(angleAlphaRadians);
            // then
            assertEquals(0.8660254, sine, 0.0000001);
        }
    }
}
