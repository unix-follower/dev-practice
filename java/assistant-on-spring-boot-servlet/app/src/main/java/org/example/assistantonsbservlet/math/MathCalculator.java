package org.example.assistantonsbservlet.math;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.math.linear.EigenDecompositionImpl;
import org.apache.commons.math.linear.LUDecompositionImpl;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.SingularValueDecompositionImpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class MathCalculator {
    public static final double ONE_FOURTH = 0.25;
    public static final double ONE_HALF = 0.5;

    private static final String DIVISION_BY_ZERO = "Division by zero";
    private static final String MISMATCHED_DIMENSIONS = "Mismatched dimensions";

    private MathCalculator() {
    }

    public static final class Arithmetic {
        private Arithmetic() {
        }

        /**
         * @return n!
         */
        public static long factorial(long number) {
            if (number < 0) {
                throw new IllegalArgumentException("Factorial is not defined for negative numbers.");
            }
            long result = 1;
            for (long i = 1; i <= number; i++) {
                if (result > Long.MAX_VALUE / i) {
                    throw new ArithmeticException("Factorial result exceeds the range of long.");
                }
                result *= i;
            }
            return result;
        }

        public static BigInteger factorial(BigInteger number) {
            var result = BigInteger.ONE;
            for (var i = BigInteger.ONE; i.compareTo(number) <= 0; i = i.add(BigInteger.ONE)) {
                result = result.multiply(i);
            }
            return result;
        }
    }

    public static final class Geometry {
        private static final String TRIANGLE_SSS_ERR_MSG = "Side length (%s) must be less than the sum " +
            "of the other two sides to form a triangle";

        private Geometry() {
        }

        /**
         * @return π = circumference / diameter
         */
        public static double pi(double circumference, double diameter) {
            return circumference / diameter;
        }

        /**
         * @return a = πr² = π × (d / 2)²
         */
        public static double circleAreaOfDiameter(double diameter) {
            final double halfDiameter = diameter / 2;
            return Math.PI * halfDiameter * halfDiameter;
        }

        /**
         * @return a = c² / 4π
         */
        public static double circleAreaOfCircumference(double circumference) {
            return (circumference * circumference) / (4 * Math.PI);
        }

        /**
         * @return c = πr
         */
        public static double circleCircumference(double radius) {
            return 2 * Math.PI * radius;
        }

        /**
         * @return c = πd
         */
        public static double circleCircumferenceOfDiameter(double diameter) {
            return Math.PI * diameter;
        }

        /**
         * @return c = 2√(πa)
         */
        public static double circleCircumferenceOfArea(double area) {
            return 2 * Math.sqrt(Math.PI * area);
        }

        /**
         * @return d = 2r
         */
        public static double circleDiameter(double radius) {
            return 2 * radius;
        }

        /**
         * @return d = c / π
         */
        public static double circleDiameterOfCircumference(double circumference) {
            return circumference / Math.PI;
        }

        /**
         * @return d = 2√(a / π)
         */
        public static double circleDiameterOfArea(double area) {
            return 2 * Math.sqrt(area / Math.PI);
        }

        /**
         * @return r = d / 2
         */
        public static double circleRadius(double diameter) {
            return diameter / 2;
        }

        /**
         * @return r = c / 2π
         */
        public static double circleRadiusOfCircumference(double circumference) {
            return circumference / (Math.PI * 2);
        }

        /**
         * @return r = √(a / π)
         */
        public static double circleRadiusOfArea(double area) {
            return Math.sqrt(area / Math.PI);
        }

        /**
         * @return A = (y₁ − y₀)/(x₁ − x₀)
         */
        public static double averageRateOfChange(double startPointX, double startPointY,
                                                 double endpointX, double endpointY) {
            if (startPointX == endpointX) {
                throw new IllegalArgumentException("The x-values must not be the same to avoid division by zero.");
            }
            return (endpointY - startPointY) / (endpointX - startPointX);
        }

        // Triangle calculators

        /**
         * If the shorter leg length 'a' is known:
         * b = a√3
         * c = 2a
         *
         * @return [a, b, c]
         */
        public static double[] triangle306090SolveWithA(double sideA) {
            return new double[]{sideA, sideA * Math.sqrt(3), 2 * sideA};
        }

        /**
         * If the longer leg length b is known:
         * a = b√3/3
         * c = 2b√3/3
         *
         * @return [a, b, c]
         */
        public static double[] triangle306090SolveWithB(double sideB) {
            final double sqrtResult = Math.sqrt(3) / 3;
            return new double[]{sideB * sqrtResult, sideB, 2 * sideB * sqrtResult};
        }

        /**
         * If the hypotenuse c is known:
         * a = c/2
         * b = c√3/2
         *
         * @return [a, b, c]
         */
        public static double[] triangle306090SolveWithC(double sideC) {
            final double sideA = sideC / 2;
            return new double[]{sideA, sideA * Math.sqrt(3), sideC};
        }

        // 2D geometry

        /**
         * @return area = ½ * a * b. The units are cm²
         */
        public static double area(double sideA, double sideB) {
            return 0.5 * sideA * sideB;
        }

        /**
         * @return perimeter = a + b + c. The units are cm
         */
        public static double perimeter(double sideA, double sideB, double hypotenuse) {
            return sideA + sideB + hypotenuse;
        }

        /**
         * c = √(a² + b²)
         *
         * @return a² + b² = c². The units are cm
         */
        public static double[] pythagoreanTheoremWithLegAndHypotenuse(double side, double hypotenuse) {
            final double squaredSide = side * side;
            final double squaredHypotenuse = hypotenuse * hypotenuse;
            final double squaredSide2 = squaredHypotenuse - squaredSide;
            return new double[]{side, Math.sqrt(squaredSide2), hypotenuse};
        }

        public static double[] pythagoreanTheoremWithLegs(double sideA, double sideB) {
            final double squaredSideA = sideA * sideA;
            final double squaredSideB = sideB * sideB;
            final double squaredHypotenuse = squaredSideA + squaredSideB;
            return new double[]{sideA, sideB, Math.sqrt(squaredHypotenuse)};
        }

        /**
         * @return area = a². The units are cm²
         */
        public static double areaOfSquare(double sideLength) {
            checkGreater0(sideLength);
            return sideLength * sideLength;
        }

        /**
         * @return area = a * b. The units are cm²
         */
        public static double areaOfRectangle(double sideLengthA, double sideLengthB) {
            checkGreater0(sideLengthA);
            checkGreater0(sideLengthB);
            return sideLengthA * sideLengthB;
        }

        private static void checkGreater0(double value) {
            if (value <= 0) {
                throw new IllegalArgumentException("This value must be greater than 0");
            }
        }

        /**
         * @return area = b * h / 2. The units are cm²
         */
        public static double areaOfTriangleWithBaseAndHeight(double base, double height) {
            checkGreater0(base);
            checkGreater0(height);
            return base * height / 2;
        }

        /**
         * Given three sides (SSS).
         *
         * @return area = 0.25 * √((a + b + c) * (-a + b + c) * (a - b + c) * (a + b - c)). The units are cm²
         */
        public static double areaOfTriangleWithSSS(double sideLengthA, double sideLengthB, double sideLengthC) {
            checkGreater0(sideLengthA);
            checkGreater0(sideLengthB);
            checkGreater0(sideLengthC);

            if (sideLengthA > sideLengthB + sideLengthC) {
                throw new IllegalArgumentException(String.format(TRIANGLE_SSS_ERR_MSG, "a"));
            }

            final double legsSum = sideLengthA + sideLengthC;
            if (legsSum < sideLengthB) {
                throw new IllegalArgumentException(String.format(TRIANGLE_SSS_ERR_MSG, "b"));
            }

            final double aAndBSum = sideLengthA + sideLengthB;
            if (aAndBSum == sideLengthC) {
                throw new IllegalArgumentException(String.format(TRIANGLE_SSS_ERR_MSG, "c"));
            }

            return ONE_FOURTH * Math.sqrt(
                (aAndBSum + sideLengthC)
                    * (-sideLengthA + sideLengthB + sideLengthC)
                    * (legsSum - sideLengthB)
                    * (aAndBSum - sideLengthC)
            );
        }

        /**
         * Given two sides and the angle between them (SAS).
         *
         * @return area = ½ * a * b * sin(γ). The units are cm²
         */
        public static double areaOfTriangleWithSAS(double sideLengthA, double sideLengthB, double angleGammaRadians) {
            checkGreater0(sideLengthA);
            checkGreater0(sideLengthB);
            checkGreater0(angleGammaRadians);

            return ONE_HALF * sideLengthA * sideLengthB * Math.sin(angleGammaRadians);
        }

        /**
         * Given two angles and the side between them (ASA).
         *
         * @return area = a² * sin(β) * sin(γ) / (2 * sin(β + γ)). The units are cm²
         */
        public static double areaOfTriangleWithASA(
            double sideLengthA, double angleBetaRadians, double angleGammaRadians) {
            return sideLengthA * sideLengthA * Math.sin(angleBetaRadians) * Math.sin(angleGammaRadians)
                / (2 * Math.sin(angleBetaRadians + angleGammaRadians));
        }

        /**
         * @return area = πr². The units are cm²
         */
        public static double circleArea(double radius) {
            checkGreater0(radius);
            return Math.PI * radius * radius;
        }

        /**
         * @return area = ½ * πr². The units are cm²
         */
        public static double semicircleArea(double radius) {
            return ONE_HALF * circleArea(radius);
        }

        /**
         * @return area = r² * α / 2. The units are cm²
         */
        public static double sectorArea(double radius, double angleAlphaRadians) {
            checkGreater0(radius);
            return radius * radius * angleAlphaRadians / 2;
        }

        /**
         * @return area = π * a * b. The units are cm²
         */
        public static double ellipseArea(double radiusA, double radiusB) {
            checkGreater0(radiusA);
            checkGreater0(radiusB);
            return Math.PI * radiusA * radiusB;
        }

        /**
         * @return area = (a + b) * h / 2. The units are cm²
         */
        public static double trapezoidArea(double sideA, double sideB, double height) {
            checkGreater0(sideA);
            checkGreater0(sideB);
            checkGreater0(height);
            return (sideA + sideB) * height / 2;
        }

        /**
         * @return area = b * h. The units are cm²
         */
        public static double parallelogramAreaWithBaseAndHeight(double base, double height) {
            checkGreater0(base);
            checkGreater0(height);
            return base * height;
        }

        private static double areaWithSidesAndAngle(double sideA, double sideB, double angleAlphaRadians) {
            checkGreater0(sideA);
            checkGreater0(sideB);
            return sideA * sideB * Math.sin(angleAlphaRadians);
        }

        /**
         * @return area = a * b * sin(α). The units are cm²
         */
        public static double parallelogramAreaWithSidesAndAngle(double sideA, double sideB, double angleAlphaRadians) {
            return areaWithSidesAndAngle(sideA, sideB, angleAlphaRadians);
        }

        /**
         * @return area = e * f * sin(θ). The units are cm²
         */
        public static double parallelogramAreaWithDiagonalsAndAngle(
            double diagonal1, double diagonal2, double angleThetaRadians) {
            checkGreater0(diagonal1);
            checkGreater0(diagonal2);
            return diagonal1 * diagonal2 * Math.sin(angleThetaRadians);
        }

        /**
         * @return area = a * h. The units are cm²
         */
        public static double rhombusAreaWithSideAndHeight(double side, double height) {
            checkGreater0(side);
            checkGreater0(height);
            return side * height;
        }

        private static double areaWithDiagonals(double diagonal1, double diagonal2) {
            checkGreater0(diagonal1);
            checkGreater0(diagonal2);
            return diagonal1 * diagonal2 / 2;
        }

        /**
         * @return area = (e * f) / 2. The units are cm²
         */
        public static double rhombusAreaWithDiagonals(double diagonal1, double diagonal2) {
            return areaWithDiagonals(diagonal1, diagonal2);
        }

        /**
         * @return area = a² * sin(α). The units are cm²
         */
        public static double rhombusAreaWithSideAndAngle(double side, double angleAlphaRadians) {
            checkGreater0(side);
            return side * side * Math.sin(angleAlphaRadians);
        }

        /**
         * @return area = (e * f) / 2. The units are cm²
         */
        public static double kiteAreaWithDiagonals(double diagonal1, double diagonal2) {
            return areaWithDiagonals(diagonal1, diagonal2);
        }

        /**
         * @return area = a * b * sin(α). The units are cm²
         */
        public static double kiteAreaWithSidesAndAngle(double sideA, double sideB, double angleAlphaRadians) {
            return areaWithSidesAndAngle(sideA, sideB, angleAlphaRadians);
        }

        /**
         * @return area = a² * √(25 + 10√5) / 4. The units are cm²
         */
        public static double pentagonArea(double sideLength) {
            checkGreater0(sideLength);
            return sideLength * sideLength * Math.sqrt(25 + 10 * Math.sqrt(5)) / 4;
        }

        /**
         * @return area = 3/2 * √3 * a². The units are cm²
         */
        public static double hexagonArea(double sideLength) {
            checkGreater0(sideLength);
            return 3. / 2 * Math.sqrt(3) * sideLength * sideLength;
        }

        /**
         * Alternative: area = perimeter * apothem / 2.
         *
         * @return area = 2 * (1 + √2) * a². The units are cm²
         */
        public static double octagonArea(double sideLength) {
            checkGreater0(sideLength);
            return 2 * (1 + Math.sqrt(2)) * sideLength * sideLength;
        }

        /**
         * @return area = πR² - πr² = π(R² - r²). The units are cm²
         */
        public static double annulusArea(double radius, double innerRadius) {
            checkGreater0(radius);
            checkGreater0(innerRadius);

            if (radius < innerRadius) {
                throw new IllegalArgumentException("Radius R should be greater than radius r");
            }

            final double radiusSquared = radius * radius;
            final double innerRadiusSquared = innerRadius * innerRadius;
            return Math.PI * (radiusSquared - innerRadiusSquared);
        }

        /**
         * @return area = e * f * sin(α). The units are cm²
         */
        public static double irregularQuadrilateralArea(double diagonal1, double diagonal2, double angleAlphaRadians) {
            checkGreater0(diagonal1);
            checkGreater0(diagonal2);
            return diagonal1 * diagonal2 * Math.sin(angleAlphaRadians);
        }

        /**
         * @return area = n * a² * cot(π/n) / 4. The units are cm²
         */
        public static double polygonArea(int numberOfSides, double sideLength) {
            checkGreater0(numberOfSides);
            checkGreater0(sideLength);
            return numberOfSides * sideLength * sideLength * Trigonometry.cot(Math.PI / numberOfSides) / 4;
        }
    }

    public static final class CoordinateGeometry {
        private CoordinateGeometry() {
        }

        private static void check2dSize(double[] vector) {
            if (vector == null || vector.length != 2) {
                throw new IllegalArgumentException("The 2d vector is required");
            }
        }

        private static void check3dSize(double[] vector) {
            if (vector == null || vector.length != 3) {
                throw new IllegalArgumentException("The 3d vector is required");
            }
        }

        private static void check2dOr3dSize(double[] vector) {
            if (vector == null || vector.length < 2 || vector.length > 3) {
                throw new IllegalArgumentException("The 2d or 3d vector is required");
            }
        }

        private static void checkSameDimensions(double[] vectorA, double[] vectorB) {
            if (vectorA.length != vectorB.length) {
                throw new IllegalArgumentException(MISMATCHED_DIMENSIONS);
            }
        }

        private static void checkSameDimensions(double[][] matrixA, double[][] matrixB) {
            if (matrixA.length != matrixB.length) {
                throw new IllegalArgumentException(MISMATCHED_DIMENSIONS);
            }

            if (matrixA[Constants.ARR_1ST_INDEX].length != matrixB[Constants.ARR_1ST_INDEX].length) {
                throw new IllegalArgumentException(MISMATCHED_DIMENSIONS);
            }
        }

        /**
         * @return |v| = √(x² + y² + z²)
         */
        public static double vectorMagnitude(double[] vector) {
            return Math.sqrt(Arrays.stream(vector).map(m -> m * m).sum());
        }

        /**
         * û = u / |u|
         * where:
         * û — Unit vector;
         * u — Arbitrary vector in the form (x, y, z);
         * |u| — Magnitude of the vector u.
         */
        public static Triple<Double, double[], Double> unitVector(double[] vector) {
            final double magnitude = vectorMagnitude(vector);
            final double[] result = Arrays.stream(vector).map(m -> m / magnitude).toArray();
            final double resultMagnitude = vectorMagnitude(result);
            return Triple.of(magnitude, result, resultMagnitude);
        }

        /**
         * Find one of the missing components.
         * For example, find z |v| = √(x² + y² + ?)
         */
        public static Pair<double[], Double> findMissingUnitVectorComponent(double[] unitVector) {
            final double sum = Arrays.stream(unitVector).map(m -> {
                if (m > 1) {
                    throw new IllegalArgumentException("Unit vector components must be less than or equal to 1");
                }
                return m;
            }).sum();
            final double[] result = Arrays.copyOf(unitVector, unitVector.length + 1);
            result[result.length - 1] = sum;
            final double resultMagnitude = vectorMagnitude(result);
            return Pair.of(result, resultMagnitude);
        }

        /**
         * @return v × w = (v₂w₃ - v₃w₂, v₃w₁ - v₁w₃, v₁w₂ - v₂w₁)
         */
        public static double[] crossProduct(double[] vectorA, double[] vectorB) {
            if (vectorA.length != 3 || vectorB.length != 3) {
                throw new IllegalArgumentException("The cross product can only be applied to 3D vectors");
            }

            final var a = vectorA;
            final var b = vectorB;
            final int i1 = Constants.ARR_1ST_INDEX;
            final int i2 = Constants.ARR_2ND_INDEX;
            final int i3 = Constants.ARR_3RD_INDEX;
            return new double[]{
                a[i2] * b[i3] - a[i3] * b[i2],
                a[i3] * b[i1] - a[i1] * b[i3],
                a[i1] * b[i2] - a[i2] * b[i1],
            };
        }

        /**
         * @return a⋅b = a₁b₁ + a₂b₂ + a₃b₃
         */
        public static double dotProduct(double[] vectorA, double[] vectorB) {
            checkSameDimensions(vectorA, vectorB);

            double result = 0;
            for (int i = 0; i < vectorA.length; i++) {
                result += vectorA[i] * vectorB[i];
            }
            return result;
        }

        /**
         * @return c_ij=a_i1 * b_1j + a_i2 * b_2j +...+ a_in * b_nj = ∑_k a_ik * b_kj
         */
        public static double dotProduct(double[][] matrixA, double[][] matrixB) {
            checkSameDimensions(matrixA, matrixB);

            double result = 0;
            for (int i = 0; i < matrixA.length; i++) {
                for (int j = 0; j < matrixA[i].length; j++) {
                    result += matrixA[i][j] * matrixB[i][j];
                }
            }
            return result;
        }

        public static double[] dotProductAndAngleBetween(double[] vectorA, double[] vectorB) {
            return dotProductAndAngleBetween(vectorA, vectorB, 4, RoundingMode.HALF_UP);
        }

        /**
         * a⋅b = |a| × |b| × cos α
         * cos α = a⋅b / (|a| × |b|)
         */
        public static double[] dotProductAndAngleBetween(
            double[] vectorA, double[] vectorB, int scale, RoundingMode roundingMode) {
            final var dot = BigDecimal.valueOf(dotProduct(vectorA, vectorB))
                .setScale(scale, roundingMode);
            final var magnitudeA = BigDecimal.valueOf(vectorMagnitude(vectorA))
                .setScale(scale, roundingMode);
            final var magnitudeB = BigDecimal.valueOf(vectorMagnitude(vectorB))
                .setScale(scale, roundingMode);

            final double angleRadians = dot.divide(magnitudeA.multiply(magnitudeB), roundingMode).doubleValue();
            return new double[]{
                dot.doubleValue(),
                magnitudeA.doubleValue(),
                magnitudeB.doubleValue(),
                Math.acos(angleRadians)
            };
        }

        /**
         * @return d = ∣a₁ − b₁∣ + ... + ∣a_N − b_N∣
         */
        public static double manhattanDistance(double[] vectorA, double[] vectorB) {
            checkSameDimensions(vectorA, vectorB);

            double result = 0;
            for (int i = 0; i < vectorA.length; i++) {
                result += Math.abs(vectorA[i] - vectorB[i]);
            }
            return result;
        }

        /**
         * The theta is in radians.
         * r = √(x² + y²)
         * θ = tan⁻¹(y/x)
         * z = z
         */
        public static double[] cartesianToCylindricalCoordinates(double[] coordinates) {
            check2dOr3dSize(coordinates);

            final double x = coordinates[Constants.X_INDEX];
            final double y = coordinates[Constants.Y_INDEX];
            final double radius = Math.sqrt(x * x + y * y);
            final double theta = Math.atan(y / x);

            if (coordinates.length == 3) {
                return new double[]{radius, theta, coordinates[Constants.Z_INDEX]};
            }
            return new double[]{radius, theta};
        }

        /**
         * The theta is in radians.
         * x = ρ * cos(θ)
         * y = ρ * sin(θ)
         * z = z
         */
        public static double[] cylindricalToCartesianCoordinates(double[] coordinates) {
            check2dOr3dSize(coordinates);

            final double radius = coordinates[Constants.R_INDEX];
            final double theta = coordinates[Constants.THETA_INDEX];
            final double x = radius * Math.cos(theta);
            final double y = radius * Math.sin(theta);

            if (coordinates.length == 3) {
                return new double[]{x, y, coordinates[Constants.Z_INDEX]};
            }
            return new double[]{x, y};
        }

        /**
         * Same as {@link #cartesianToCylindricalCoordinates}, but supports 2d only.
         */
        public static double[] cartesianToPolarCoordinates(double[] coordinates) {
            check2dSize(coordinates);
            return cartesianToCylindricalCoordinates(coordinates);
        }

        /**
         * Same as {@link #cylindricalToCartesianCoordinates}, but supports 2d only.
         */
        public static double[] polarToCartesianCoordinates(double[] coordinates) {
            check2dSize(coordinates);
            return cylindricalToCartesianCoordinates(coordinates);
        }

        /**
         * r = √(x² + y² + z²)
         * θ = cos⁻¹(z/r)
         * φ = tan⁻¹(y/x)
         */
        public static double[] cartesianToSphericalCoordinates(double[] coordinates) {
            check3dSize(coordinates);

            final double x = coordinates[Constants.X_INDEX];
            final double y = coordinates[Constants.Y_INDEX];
            final double z = coordinates[Constants.Z_INDEX];

            final double radius = Math.sqrt(x * x + y * y + z * z);
            final double theta = Math.acos(z / radius);
            final double phi = Math.atan(y / x);
            return new double[]{radius, theta, phi};
        }

        /**
         * x = r * sin θ * cos φ
         * y = r * sin θ * sin φ
         * z = r * cos θ
         */
        public static double[] sphericalToCartesianCoordinates(double[] coordinates) {
            check3dSize(coordinates);

            final double radius = coordinates[Constants.X_INDEX];
            final double theta = coordinates[Constants.Y_INDEX];
            final double phi = coordinates[Constants.Z_INDEX];

            final double x = radius * Math.sin(theta) * Math.cos(phi);
            final double y = radius * Math.sin(theta) * Math.sin(phi);
            final double z = radius * Math.cos(theta);
            return new double[]{x, y, z};
        }

        public static Pair<double[], Double> vectorProjection(double[] vectorA, double[] vectorB) {
            checkSameDimensions(vectorA, vectorB);

            final double dotProduct = dotProduct(vectorA, vectorB);
            final double squaredNormOfB = dotProduct(vectorB, vectorB);
            if (squaredNormOfB == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }

            final double projectionFactor = dotProduct / squaredNormOfB;

            final double[] result = new double[vectorA.length];
            for (int i = 0; i < vectorA.length; i++) {
                result[i] = projectionFactor * vectorB[i];
            }
            return Pair.of(result, projectionFactor);
        }

        /**
         * @return Δx = x₂-x₁ or Δy = y₂-y₁
         */
        public static double deltaDistance(double point2, double point1) {
            return point2 - point1;
        }

        /**
         * For 1d, d = √(x₂-x₁)²)
         * For 2d, d = √((x₂-x₁)² + (y₂-y₁)²)
         * For 3d, d = √((x₂-x₁)² + (y₂-y₁)² + (z₂-z₁)²)
         * For 4d, d = √((x₂-x₁)² + (y₂-y₁)² + (z₂-z₁)² + (k₂-k₁)²)
         */
        public static double distance(double[] pointACoords, double[] pointBCoords) {
            checkSameDimensions(pointACoords, pointBCoords);

            final double x1 = pointACoords[Constants.X_INDEX];
            final double x2 = pointBCoords[Constants.X_INDEX];
            final double deltaX = deltaDistance(x2, x1);

            final int dimension = pointACoords.length;
            switch (dimension) {
                case 1 -> {
                    return Math.sqrt(Math.pow(Math.abs(x2 - x1), 2));
                }
                case 2 -> {
                    final double y1 = pointACoords[Constants.Y_INDEX];
                    final double y2 = pointBCoords[Constants.Y_INDEX];

                    return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaDistance(y2, y1), 2));
                }
                case 3 -> {
                    final double y1 = pointACoords[Constants.Y_INDEX];
                    final double y2 = pointBCoords[Constants.Y_INDEX];
                    final double z1 = pointACoords[Constants.Z_INDEX];
                    final double z2 = pointBCoords[Constants.Z_INDEX];

                    return Math.sqrt(
                        Math.pow(deltaX, 2) + Math.pow(deltaDistance(y2, y1), 2) + Math.pow(deltaDistance(z2, z1), 2)
                    );
                }
                case 4 -> {
                    final double y1 = pointACoords[Constants.Y_INDEX];
                    final double y2 = pointBCoords[Constants.Y_INDEX];
                    final double z1 = pointACoords[Constants.Z_INDEX];
                    final double z2 = pointBCoords[Constants.Z_INDEX];
                    final double k1 = pointACoords[Constants.K_INDEX];
                    final double k2 = pointBCoords[Constants.K_INDEX];

                    return Math.sqrt(
                        Math.pow(deltaX, 2) + Math.pow(deltaDistance(y2, y1), 2) +
                            Math.pow(deltaDistance(z2, z1), 2) + Math.pow(deltaDistance(k2, k1), 2)
                    );
                }
                default -> throw new UnsupportedOperationException();
            }
        }

        /**
         * @return d = |mx₁ − y₁ + b| / √(m² + 1)
         */
        public static double distanceBetweenPointsAndStraightLine(
            double[] pointCoords, double slope, double yIntercept) {
            check2dSize(pointCoords);

            final double x1 = pointCoords[Constants.X_INDEX];
            final double y1 = pointCoords[Constants.Y_INDEX];

            return Math.abs(slope * x1 - y1 + yIntercept) / Math.sqrt(slope * slope + 1);
        }

        /**
         * The slope has to be the same for both lines.
         *
         * @return d = |b₂ − b₁| / √(m² + 1)
         */
        public static double distanceBetweenParallelLines(
            double slope, double line1YIntercept, double line2YIntercept) {
            return Math.abs(line2YIntercept - line1YIntercept) / Math.sqrt(slope * slope + 1);
        }

        /**
         * @return y = (x - x₁) * (y₂ - y₁) / (x₂ - x₁) + y₁
         */
        public static double linearInterpolation(double[] pointACoords, double[] pointBCoords, double midpointX) {
            check2dSize(pointACoords);
            checkSameDimensions(pointACoords, pointBCoords);

            final double x1 = pointACoords[Constants.X_INDEX];
            final double y1 = pointACoords[Constants.Y_INDEX];
            final double x2 = pointBCoords[Constants.X_INDEX];
            final double y2 = pointBCoords[Constants.Y_INDEX];

            final double deltaY = deltaDistance(y2, y1);
            final double deltaX = deltaDistance(x2, x1);
            return (midpointX - x1) * deltaY / deltaX + y1;
        }

        /**
         * x_f = xᵢ*cos(θ) − yᵢ*sin(θ)
         * y_f = xᵢ*sin(θ) + yᵢ*cos(θ)
         */
        public static double[] rotation(double[] pointCoords, double angleThetaRadians) {
            final double[] originCoords = {0, 0};
            return rotationAroundPoint(pointCoords, originCoords, angleThetaRadians);
        }

        /**
         * x_f = xₒ + (xᵢ-xₒ) cos(θ) − (yᵢ-yₒ) sin(θ)
         * y_f = yₒ + (xᵢ-xₒ) sin(θ) + (yᵢ-yₒ) cos(θ)
         */
        public static double[] rotationAroundPoint(
            double[] pointCoords, double[] pivotCoords, double angleThetaRadians) {
            checkSameDimensions(pointCoords, pivotCoords);
            check2dSize(pointCoords);

            final double xi = pointCoords[Constants.X_INDEX];
            final double yi = pointCoords[Constants.Y_INDEX];
            final double xo = pivotCoords[Constants.X_INDEX];
            final double yo = pivotCoords[Constants.Y_INDEX];

            final double sine = Math.sin(angleThetaRadians);
            final double cosine = Math.cos(angleThetaRadians);
            return new double[]{
                xo + (xi - xo) * cosine - (yi - yo) * sine,
                yo + (xi - xo) * sine + (yi - yo) * cosine
            };
        }

        /**
         * @return m = (y₂ - y₁) / (x₂ - x₁)
         */
        public static double slope(double[] pointACoords, double[] pointBCoords) {
            checkSameDimensions(pointACoords, pointBCoords);
            check2dSize(pointACoords);

            final double x1 = pointACoords[Constants.X_INDEX];
            final double y1 = pointACoords[Constants.Y_INDEX];
            final double x2 = pointBCoords[Constants.X_INDEX];
            final double y2 = pointBCoords[Constants.Y_INDEX];

            return (y2 - y1) / (x2 - x1);
        }

        /**
         * @return m = −a/b
         */
        public static double slope(double coefficientOfX, double coefficientOfY) {
            return -coefficientOfX / coefficientOfY;
        }

        public static double slopeFromKnownIntercepts(double xIntercept, double yIntercept) {
            return -yIntercept / xIntercept;
        }

        public static double areaUnderSlope(double x1, double x2, double slope) {
            final double deltaX = deltaDistance(x2, x1);
            final double deltaY = slope * deltaX;
            return deltaX * deltaY / 2;
        }

        /**
         * x_c = −c/a
         * y_c = −c/b
         */
        public static double[] intercept(double coefficientOfX, double coefficientOfY, double constantTerm) {
            final double xIntercept = -constantTerm / coefficientOfX;
            final double yIntercept = -constantTerm / coefficientOfY;
            return new double[]{xIntercept, yIntercept};
        }

        /**
         * y = mx + b
         */
        public static double[] intercept(double slopeTerm, double constantTerm) {
            return intercept(slopeTerm, -1, constantTerm);
        }

        /**
         * @return b = y₁ - m * x₁
         */
        public static double slopeInterceptConstantTerm(double x1, double y1, double slope) {
            return y1 - slope * x1;
        }

        /**
         * x = (x₁ + x₂)/2
         */
        public static double midpoint(double pointA, double pointB) {
            return (pointA + pointB) / 2;
        }

        /**
         * x = (x₁ + x₂)/2
         * y = (y₁ + y₂)/2
         */
        public static double[] midpoint(double[] pointACoords, double[] pointBCoords) {
            final double x1 = pointACoords[Constants.X_INDEX];
            final double y1 = pointACoords[Constants.Y_INDEX];
            final double x2 = pointBCoords[Constants.X_INDEX];
            final double y2 = pointBCoords[Constants.Y_INDEX];
            return new double[]{midpoint(x1, x2), midpoint(y1, y2)};
        }

        public static double endpointWithGivenMidpoint(double point, double midpoint) {
            return 2 * midpoint - point;
        }
    }

    /**
     * <table>
     *     <tr><th>Degrees</td><th>Radians</th><th>sin(α)</th><th>cos(α)</th><th>tan(α)</th><th>cot(α)</th></tr>
     *     <tr><td>0°</td><td>0</td><td>0</td><td>1</td><td>0</td><td>Undefined</td></tr>
     *     <tr><td>15°</td><td>π/12</td><td>(√6−√2)/4</td><td>(√6+√2)/4</td><td></td><td></td></tr>
     *     <tr><td>30°</td><td>π/6</td><td>0.5</td><td>√3/2</td><td>√3/3</td><td>√3</td></tr>
     *     <tr><td>45°</td><td>π/4</td><td>√2/2</td><td>√2/2</td><td>1</td><td>1</td></tr>
     *     <tr><td>60°</td><td>π/3</td><td>√3/2</td><td>0.5</td><td>√3</td><td>√3/3</td></tr>
     *     <tr><td>75°</td><td>5π/12</td><td>(√6+√2)/4</td><td>(√6-√2)/4</td><td></td><td></td></tr>
     *     <tr><td>90°</td><td>π/2</td><td>1</td><td>0</td><td>Undefined</td><td>0</td></tr>
     *     <tr><td>105°</td><td>7π/12</td><td>(√6+√2)/4</td><td>-(√6-√2)/4</td><td></td><td></td></tr>
     *     <tr><td>120°</td><td>2π/3</td><td>√3/2</td><td>-0.5</td><td>-√3</td><td>-√3/3</td></tr>
     *     <tr><td>135°</td><td>3π/4</td><td>√2/2</td><td>-√2/2</td><td>-1</td><td>-1</td></tr>
     *     <tr><td>150°</td><td>5π/6</td><td>0.5</td><td>-√3/2</td><td>-√3/3</td><td>-√3</td></tr>
     *     <tr><td>165°</td><td>11π/12</td><td>(√6−√2)/4</td><td>-(√6+√2)/4</td><td></td><td></td></tr>
     *     <tr><td>180°</td><td>π</td><td>0</td><td>-1</td><td>0</td><td>Undefined</td></tr>
     *     <tr><td>210°</td><td>7π/6</td><td>-0.5</td><td>-√3/2</td><td>√3/3</td><td>√3</td></tr>
     *     <tr><td>225°</td><td>5π/4</td><td>-√2/2</td><td>-√2/2</td><td>1</td><td>1</td></tr>
     *     <tr><td>240°</td><td>4π/3</td><td>-√3/2</td><td>-0.5</td><td>√3</td><td>√3/3</td></tr>
     *     <tr><td>270°</td><td>3π/2</td><td>-1</td><td>0</td><td>Undefined</td><td>0</td></tr>
     *     <tr><td>300°</td><td>5π/3</td><td>-√3/2</td><td>0.5</td><td>-√3</td><td>-√3/3</td></tr>
     *     <tr><td>315°</td><td>7π/4</td><td>-√2/2</td><td>√2/2</td><td>-1</td><td>-1</td></tr>
     *     <tr><td>330°</td><td>11π/6</td><td>-0.5</td><td>√3/2</td><td>-√3/3</td><td>-√3</td></tr>
     *     <tr><td>360°</td><td>2π</td><td>0</td><td>1</td><td>0</td><td>Undefined</td></tr>
     * </table>
     */
    public static final class Trigonometry {
        public static final double PI_OVER_12 = Math.PI / 12;
        public static final double PI_OVER_6 = Math.PI / 6;
        public static final double PI_OVER_4 = Math.PI / 4;
        public static final double PI_OVER_3 = Math.PI / 3;
        public static final double PI5_OVER_12 = 5 * Math.PI / 12;
        public static final double PI_OVER_2 = Math.PI / 2;
        public static final double PI7_OVER_12 = 7 * Math.PI / 12;
        public static final double PI2_OVER_3 = 2 * Math.PI / 3;
        public static final double PI3_OVER_4 = 3 * Math.PI / 4;
        public static final double PI5_OVER_6 = 5 * Math.PI / 6;
        public static final double PI11_OVER_12 = 11 * Math.PI / 12;
        public static final double PI7_OVER_6 = 7 * Math.PI / 6;
        public static final double PI5_OVER_4 = 5 * Math.PI / 4;
        public static final double PI4_OVER_3 = 4 * Math.PI / 3;
        public static final double PI3_OVER_2 = 3 * Math.PI / 2;
        public static final double PI5_OVER_3 = 5 * Math.PI / 3;
        public static final double PI7_OVER_4 = 7 * Math.PI / 4;
        public static final double PI11_OVER_6 = 11 * Math.PI / 6;
        public static final double PI2 = 2 * Math.PI;

        private Trigonometry() {
        }

        public static double csc(double hypotenuse, double opposite) {
            return hypotenuse / opposite;
        }

        /**
         * csc(α) = c / a
         * csc(x) = (x² + y²) / y
         * csc(x) = sin⁻¹(x)
         * D(csc) = {x : x ≠ k*180°, k ∈ ℤ}
         * <p/>The cosecant function
         * <br/>- is odd: csc(x) = -csc(x)
         * <br/>- is periodic: csc(x) = csc(x + 360°)
         * <br/>- doesn't always exist.
         *
         * @return csc(x) = 1 / sin(x)
         */
        public static double csc(double angleRadians) {
            final double sinResult = Math.sin(angleRadians);
            if (sinResult == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            return 1 / sinResult;
        }

        public static double sec(double hypotenuse, double adjacent) {
            return hypotenuse / adjacent;
        }

        /**
         * sec(α) = c / b
         * sec(α) = (√(x² + y²)) / x
         * sec(x) = (cos(x))⁻¹
         * D(sec) = {x : x ≠ 90° + k*180°, k ∈ X}
         * <p/>The secant function
         * <br/>- is even: sec(α) = sec(-α)
         * <br/>- is periodic: sec(x) = sec(x + 360°)
         * <br/>- doesn't always exist.
         *
         * @return sec(x) = 1 / cos(x)
         */
        public static double sec(double angleRadians) {
            final double cosResult = Math.cos(angleRadians);
            if (cosResult == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            return 1 / cosResult;
        }

        public static double cot(double adjacent, double opposite) {
            return adjacent / opposite;
        }

        /**
         * cot(α) = b / c
         * cot(α) = x / y
         * cot(x) = (tan(x))⁻¹
         * cot(x) = cos(x) / sin(x)
         * D(cot) = {x : x ≠ k*180°, k ∈ ℤ}
         * <p/>The cotangent function
         * <br/>- is odd: cot(x) = -cot(-x)
         * <br/>- is periodic: cot(x) = cot(x + 360°)
         * <br/>- doesn't always exist.
         *
         * @return cot(α) = 1 / tan(α)
         */
        public static double cot(double angleRadians) {
            final double tanResult = Math.tan(angleRadians);
            if (tanResult == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            return 1 / tanResult;
        }

        /**
         * sin(2θ) = sin(θ+θ) = sin(θ)cos(θ) + cos(θ)sin(θ)
         *
         * @return sin(2θ) = 2sin(θ)cos(θ)
         */
        public static double sinDoubleAngle(double angleThetaRadians) {
            return 2 * Math.sin(angleThetaRadians) * Math.cos(angleThetaRadians);
        }

        /**
         * cos(2θ) = cos²(θ) − sin²(θ)
         * cos(2θ) = 2cos²(θ) − 1
         *
         * @return cos(2θ) = 1 − 2sin²(θ)
         */
        public static double cosDoubleAngle(double angleThetaRadians) {
            return 1 - 2 * Math.sin(angleThetaRadians) * Math.sin(angleThetaRadians);
        }

        /**
         * tan(2θ) = tan(θ+θ) = (tan(θ)+tan(θ)) / (1 − tan(θ)*tan(θ))
         *
         * @return tan(2θ) = (2tan(θ)) / (1−tan²(θ))
         */
        public static double tanDoubleAngle(double angleThetaRadians) {
            return (2 * Math.tan(angleThetaRadians)) / (1 - Math.tan(angleThetaRadians) * Math.tan(angleThetaRadians));
        }

        /**
         * sin²(x/2) = (1−cos(x)) / 2
         *
         * @return sin²(x/2) = ±√((1−cos(x)) / 2)
         */
        public static double sinHalfAngle(double angleRadians) {
            return Math.sqrt((1 - Math.cos(angleRadians)) / 2);
        }

        /**
         * cos²(x/2) = (1+cos(x)) / 2
         *
         * @return cos(x/2) = ±√((1+cos(x)) / 2)
         */
        public static double cosHalfAngle(double angleRadians) {
            return Math.sqrt((1 + Math.cos(angleRadians)) / 2);
        }

        /**
         * tan²(x/2) = (1-cos(x)) / (1+cos(x))
         *
         * @return tan(x/2) = ±√((1-cos(x)) / (1+cos(x)))
         */
        public static double tanHalfAngle(double angleRadians) {
            final double cosine = Math.cos(angleRadians);
            return Math.sqrt((1 - cosine) / (1 + cosine));
        }

        public static double sinTripleAngleIdentity(double theta) {
            return 3 * Math.sin(theta) - 4 * Math.pow(Math.sin(theta), 3);
        }

        public static double cosTripleAngleIdentity(double theta) {
            return 4 * Math.pow(Math.cos(theta), 3) - 3 * Math.cos(theta);
        }

        public static double tanTripleAngleIdentity(double theta) {
            final double numerator = 3 * Math.tan(theta) - Math.pow(Math.tan(theta), 3);
            final double denominator = 1 - 3 * Math.tan(theta) * Math.tan(theta);
            if (denominator == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            return numerator / denominator;
        }

        public static double lawOfCosine(double sideA, double sideB, double angle) {
            return Math.sqrt(sideA * sideA + sideB * sideB - 2 * sideA * sideB * Math.cos(angle));
        }

        public static double sinAngleSum(double angleAlpha, double angleBeta) {
            return Math.sin(angleAlpha) * Math.cos(angleBeta) + Math.cos(angleAlpha) * Math.sin(angleBeta);
        }

        public static double sinAngleSubtract(double angleAlpha, double angleBeta) {
            return Math.sin(angleAlpha) * Math.cos(angleBeta) - Math.cos(angleAlpha) * Math.sin(angleBeta);
        }

        public static double cosAngleSum(double angleAlpha, double angleBeta) {
            return Math.cos(angleAlpha) * Math.cos(angleBeta) - Math.sin(angleAlpha) * Math.sin(angleBeta);
        }

        public static double cosAngleSubtract(double angleAlpha, double angleBeta) {
            return Math.cos(angleAlpha) * Math.cos(angleBeta) + Math.sin(angleAlpha) * Math.sin(angleBeta);
        }

        public static double tangentAngleSum(double angleAlpha, double angleBeta) {
            final double denominator = 1 - Math.tan(angleAlpha) * Math.tan(angleBeta);
            if (denominator == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            final double numerator = Math.tan(angleAlpha) + Math.tan(angleBeta);
            return numerator / denominator;
        }

        public static double tanAngleSubtract(double angleAlpha, double angleBeta) {
            final double denominator = 1 + Math.tan(angleAlpha) * Math.tan(angleBeta);
            if (denominator == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            final double numerator = Math.tan(angleAlpha) - Math.tan(angleBeta);
            return numerator / denominator;
        }

        public static double cotangentAngleSum(double angleAlpha, double angleBeta) {
            final double cotAlpha = cot(angleAlpha);
            final double cotBeta = cot(angleBeta);
            final double denominator = cotAlpha + cotBeta;
            if (denominator == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            return (cotAlpha * cotBeta - 1) / denominator;
        }

        public static double cotangentAngleSubtract(double angleAlpha, double angleBeta) {
            final double cotAlpha = cot(angleAlpha);
            final double cotBeta = cot(angleBeta);
            final double denominator = cotAlpha - cotBeta;
            if (denominator == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            return (cotAlpha * cotBeta + 1) / denominator;
        }

        public static double secantAngleSum(double angleAlpha, double angleBeta) {
            final double denominator = cot(angleAlpha) * Math.tan(angleBeta);
            if (denominator == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            return 1 / (Math.cos(angleAlpha) * Math.cos(angleBeta) / denominator);
        }

        public static double secantAngleSubtract(double angleAlpha, double angleBeta) {
            final double denominator = cot(angleAlpha) * Math.tan(angleBeta);
            if (denominator == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            return 1 / (Math.cos(angleAlpha) * Math.cos(angleBeta) / denominator);
        }

        public static double cosecantAngleSum(double angleAlpha, double angleBeta) {
            final double denominator = Math.sin(angleAlpha) * Math.cos(angleBeta)
                + Math.cos(angleAlpha) * Math.sin(angleBeta);
            if (denominator == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            final double numerator = Math.sin(angleAlpha) * Math.sin(angleBeta);
            return numerator / denominator;
        }

        public static double cosecantAngleSubtract(double angleAlpha, double angleBeta) {
            final double denominator = Math.sin(angleAlpha) * Math.cos(angleBeta)
                - Math.cos(angleAlpha) * Math.sin(angleBeta);
            if (denominator == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            final double numerator = Math.sin(angleAlpha) * Math.sin(angleBeta);
            return numerator / denominator;
        }

        /**
         * sin(α) = y/1 = y
         * sin(α) = opposite/hypotenuse = a/c
         * Range: −1 ≤ sin(α) ≤ 1
         * Period: 2π
         * An odd function i.e. sin(−α) = −sin(α)
         */
        public static double sin(double angleRadians) {
            return Math.sin(angleRadians);
        }

        /**
         * y(t) = A * sin(2πft + φ)
         *
         * @return y(t) = A * sin(ωt+φ)
         */
        public static double sinusoid(double amplitude, double anglePhiRadians,
                                      double oscillationFrequency, long timeSeconds) {
            final double angularFrequency = 2 * Math.PI * oscillationFrequency;
            return amplitude * Math.sin(angularFrequency * timeSeconds + anglePhiRadians);
        }

        public static int quadrant(double angleAlphaRadians) {
            if (0 < angleAlphaRadians && angleAlphaRadians <= Math.PI / 2) {
                // 0<α≤π/2
                return 1;
            } else if (Math.PI / 2 < angleAlphaRadians && angleAlphaRadians <= Math.PI) {
                // π/2<α≤π
                return 2;
            } else if (Math.PI < angleAlphaRadians && angleAlphaRadians <= 3 * Math.PI / 2) {
                // π<α≤3π/2
                return 3;
            } else {
                // 3π/2<α≤2π
                return 4;
            }
        }

        /**
         * cos(α) = x/1 = x
         * cos(α) = adjacent / hypotenuse = b / c
         * Range: −1 ≤ cos(α) ≤ 1
         * Period: 2π
         * An even function i.e. cos(−α) = cos(α)
         */
        public static double cos(double angleRadians) {
            return Math.sin(angleRadians);
        }

        /**
         * tan(α) = y/x = sin(α)/cos(α)
         * tan(α) = opposite / adjacent = a / b
         * Range: −1 ≤ cos(α) ≤ 1
         * Period: 2π
         * An even function i.e. cos(−α) = cos(α)
         */
        public static double tan(double angleRadians) {
            return Math.tan(angleRadians);
        }

        /**
         * @return (a - b) / (a + b) = tan(0.5(α - β)) / tan(0.5(α + β))
         */
        public static double lawOfTangents(double angleAlphaRadians, double angleBetaRadians) {
            return tan(0.5 * (angleAlphaRadians - angleBetaRadians))
                / tan(0.5 * (angleAlphaRadians + angleBetaRadians));
        }
    }

    public static final class LinearAlgebra {
        private LinearAlgebra() {
        }

        public static double determinant(double[][] matrix) {
            final var realMatrix = MatrixUtils.createRealMatrix(matrix);
            return new LUDecompositionImpl(realMatrix).getDeterminant();
        }

        public static double l2norm(double[][] matrix) {
            final var realMatrix = MatrixUtils.createRealMatrix(matrix);
            return new SingularValueDecompositionImpl(realMatrix).getNorm();
        }

        public static double l2norm(double[] vector) {
            final var realVector = MatrixUtils.createRealVector(vector);
            return realVector.getNorm();
        }

        public static double rank(double[][] matrix) {
            final var realMatrix = MatrixUtils.createRealMatrix(matrix);
            return new SingularValueDecompositionImpl(realMatrix).getRank();
        }

        public static double[] eigenvalues(double[][] matrix) {
            final var realMatrix = MatrixUtils.createRealMatrix(matrix);
            return new EigenDecompositionImpl(realMatrix, 0).getRealEigenvalues();
        }
    }

    public static final class Calculus {
        private Calculus() {
        }

        /**
         * y = mx + b
         *
         * @return ∫(ax + b)dx = (a/2)x^2 + bx + C
         */
        public static double indefiniteLinearIntegral(
            double x, double slope, double constantTerm, double constantOfIntegration) {
            return (slope / 2) * x * x + constantTerm * x + constantOfIntegration;
        }

        /**
         * y = mx + b
         *
         * @return ∫ₐ^b f(x)dx = F(b) - F(a)
         */
        public static double definiteLinearIntegral(double x1, double x2, double slope, double constantTerm) {
            final double fx2 = (slope / 2) * x2 * x2 + constantTerm * x2;
            final double fx1 = (slope / 2) * x1 * x1 + constantTerm * x1;
            return fx2 - fx1;
        }
    }

    public static final class Stats {
        private Stats() {
        }

        public static final class Descriptive {
            private Descriptive() {
            }

            public static double quartile1(double[] data) {
                Arrays.sort(data);
                final double q1Position = (data.length + 1) / 4.0;
                return interpolate(data, q1Position);
            }

            public static double quartile2(double[] data) {
                Arrays.sort(data);
                final double q2Position = (data.length + 1) / 2.0;
                return interpolate(data, q2Position);
            }

            public static double quartile3(double[] data) {
                Arrays.sort(data);
                final double q3Position = 3 * (data.length + 1) / 4.0;
                return interpolate(data, q3Position);
            }

            /**
             * @return IQR = Q3 – Q1
             */
            public static double[] iqr(double[] data) {
                final double q1 = quartile1(data);
                final double q3 = quartile3(data);
                return new double[]{q3 - q1, q1, q3};
            }

            /**
             * x < Q1 - 1.5 * IQR or x > Q3 + 1.5 * IQR
             */
            public static double[] outliers(double[] data) {
                if (data == null || data.length == 0) {
                    return new double[0];
                }

                final double[] interquartileRange = iqr(data);
                final double range = interquartileRange[0];
                final double q1 = interquartileRange[1];
                final double q3 = interquartileRange[2];
                final double upperBoundary = q3 + (1.5 * range);
                final double lowerBoundary = q1 - (1.5 * range);

                return Arrays.stream(data)
                    .filter(value -> value < lowerBoundary || value > upperBoundary)
                    .toArray();
            }

            private static double interpolate(double[] sortedData, double position) {
                // Convert to 0-based index
                final int lowerIndex = (int) Math.floor(position) - 1;
                final int upperIndex = (int) Math.ceil(position) - 1;

                if (lowerIndex == upperIndex) {
                    // Position is an integer, return the exact value
                    return sortedData[lowerIndex];
                } else {
                    // Position is fractional, interpolate between two values
                    final double lowerValue = sortedData[lowerIndex];
                    final double upperValue = sortedData[upperIndex];
                    return lowerValue + (position - (lowerIndex + 1)) * (upperValue - lowerValue);
                }
            }

            /**
             * @return MSE = (1 / n) * Σ(yᵢ - ŷᵢ)²
             */
            public static double mse(double[] predictedValues, double[] actualValues) {
                checkPredictedValuesSameLength(predictedValues, actualValues);
                checkNonEmpty(predictedValues);

                final int n = predictedValues.length;
                double sumOfSquaredErrors = 0;
                for (int i = 0; i < n; i++) {
                    sumOfSquaredErrors += Math.pow(actualValues[i] - predictedValues[i], 2);
                }
                return (1.0 / n) * sumOfSquaredErrors;
            }

            private static void checkPredictedValuesSameLength(double[] predictedValues, double[] actualValues) {
                final int n = predictedValues.length;
                if (n != actualValues.length) {
                    throw new IllegalArgumentException(
                        "The lengths of predictedValues and actualValues must be equal.");
                }
            }

            private static void checkNonEmpty(double[] data) {
                if (data.length == 0) {
                    throw new IllegalArgumentException("The input arrays must not be empty.");
                }
            }

            /**
             * @return RMSE = √MSE
             */
            public static double rmse(double[] predictedValues, double[] actualValues) {
                return Math.sqrt(mse(predictedValues, actualValues));
            }

            /**
             * @return MAE = (1/n) * Σ|yᵢ - xᵢ|
             */
            public static double mae(double[] predictedValues, double[] actualValues) {
                checkPredictedValuesSameLength(predictedValues, actualValues);
                checkNonEmpty(predictedValues);

                final int n = predictedValues.length;
                double absSumErrors = 0;
                for (int i = 0; i < n; i++) {
                    absSumErrors += Math.abs(actualValues[i] - predictedValues[i]);
                }
                return (1.0 / n) * absSumErrors;
            }

            /**
             * @return μ = ∑X / N
             */
            public static double mean(double[] data) {
                checkNonEmpty(data);
                return Arrays.stream(data).sum() / data.length;
            }

            /**
             * @return Center = (N + 1) / 2
             */
            public static double median(double[] data) {
                checkNonEmpty(data);
                return (data.length + 1.0) / 2;
            }

            public static double[] mode(double[] data) {
                checkNonEmpty(data);
                final var frequencyTable = new HashMap<Double, Integer>();
                for (double value : data) {
                    frequencyTable.merge(value, 1, Integer::sum);
                }

                final int maxFrequency = frequencyTable.values().stream().max(Integer::compare).orElse(0);

                return frequencyTable.entrySet().stream()
                    .filter(entry -> entry.getValue() == maxFrequency && maxFrequency > 1)
                    .map(Map.Entry::getKey)
                    .mapToDouble(Double::doubleValue)
                    .toArray();
            }

            /**
             * @return σ² = (1/N) * ∑ᴺᵢ₌₁(xᵢ−μ)²
             */
            public static double variance(double[] data) {
                checkNonEmpty(data);
                final double meanValue = mean(data);
                final double squaredDiffSum = Arrays.stream(data)
                    .map(value -> Math.pow(value - meanValue, 2))
                    .sum();
                return (1.0 / data.length) * squaredDiffSum;
            }

            /**
             * @return √(σ²)
             */
            public static double std(double[] data) {
                return Math.sqrt(variance(data));
            }
        }

        public static final class ProbabilityTheory {
            private ProbabilityTheory() {
            }

            /**
             * @return (tp + tn) / (tp + tn + fp + fn)
             */
            public static double accuracy(double truePositive, double trueNegative,
                                          double falsePositive, double falseNegative) {
                return (truePositive + trueNegative) / (truePositive + trueNegative + falsePositive + falseNegative);
            }

            /**
             * Sensitivity = TP / (TP + FN)
             * Specificity = TN / (FP + TN)
             *
             * @return (Sensitivity × Prevalence) + (Specificity × (1 − Prevalence))
             */
            public static double accuracy(double sensitivity, double specificity, double prevalence) {
                return (sensitivity * prevalence) + (specificity * (1 - prevalence));
            }

            /**
             * @return (|(Vo − Va)|/Va) × 100
             */
            public static double percentError(double observedValue, double acceptedValue) {
                return (Math.abs(observedValue - acceptedValue) / acceptedValue) * 100;
            }

            /**
             * @return tp / (tp + fp)
             */
            public static double precision(double truePositive, double falsePositive) {
                return truePositive / (truePositive + falsePositive);
            }

            /**
             * @return tp / (tp + fn)
             */
            public static double recall(double truePositive, double falseNegative) {
                return truePositive / (truePositive + falseNegative);
            }

            public static double f1Score(double precision, double recall) {
                return 2 * ((precision * recall) / (precision + recall));
            }

            /**
             * @return ₙCᵣ = n! / (r!(n - r)!)
             */
            public static long combinations(long totalObjects, long sampleSize) {
                checkCombinationInputs(totalObjects, sampleSize);
                final long numerator = Arithmetic.factorial(totalObjects);
                final long denominator = Arithmetic.factorial(sampleSize)
                    * Arithmetic.factorial(totalObjects - sampleSize);
                return numerator / denominator;
            }

            private static void checkNonNegativeCombinationInputs(long totalObjects, long sampleSize) {
                if (totalObjects < 0 || sampleSize < 0) {
                    throw new IllegalArgumentException("Inputs must be non-negative.");
                }
            }

            private static void checkCombinationInputs(long totalObjects, long sampleSize) {
                checkNonNegativeCombinationInputs(totalObjects, sampleSize);
                if (totalObjects == 0 && sampleSize > 0) {
                    throw new IllegalArgumentException(
                        "Cannot choose combinations with replacement from zero objects.");
                }
            }

            /**
             * @return Cᴿ(n, r) = (n + r - 1)! / (r!(n - 1)!)
             */
            public static long combinationsWithReplacement(long totalObjects, long sampleSize) {
                checkCombinationInputs(totalObjects, sampleSize);
                final long numerator = Arithmetic.factorial(totalObjects + sampleSize - 1);
                final long denominator = Arithmetic.factorial(sampleSize) * Arithmetic.factorial(totalObjects - 1);
                return numerator / denominator;
            }

            /**
             * @return ₙPᵣ = n! / ((n - r)!)
             */
            public static long permutations(long totalObjects, long sampleSize) {
                checkNonNegativeCombinationInputs(totalObjects, sampleSize);
                if (sampleSize > totalObjects) {
                    throw new IllegalArgumentException("sampleSize cannot be greater than totalObjects.");
                }

                final long numerator = Arithmetic.factorial(totalObjects);
                final long denominator = Arithmetic.factorial(totalObjects - sampleSize);
                return numerator / denominator;
            }

            /**
             * @return Pᴿ(n, r) = nʳ
             */
            public static double permutationsWithReplacement(long totalObjects, long sampleSize) {
                checkNonNegativeCombinationInputs(totalObjects, sampleSize);
                return Math.pow(totalObjects, sampleSize);
            }
        }

        public static final class Distributions {
            private Distributions() {
            }

            /**
             * @return P(X=r) = nCr * pʳ * (1 - p)ⁿ⁻ʳ
             */
            public static double binomialDistribution(
                long numberOfEvents, long numberOfRequiredSuccesses, double probabilityOfOneSuccess) {
                if (numberOfEvents < 0 || numberOfRequiredSuccesses < 0) {
                    throw new IllegalArgumentException(
                        "Number of events and number of required successes must be non-negative."
                    );
                }
                if (probabilityOfOneSuccess < 0 || probabilityOfOneSuccess > 1) {
                    throw new IllegalArgumentException("Probability of one success must be between 0 and 1.");
                }
                if (numberOfRequiredSuccesses > numberOfEvents) {
                    throw new IllegalArgumentException(
                        "Number of required successes cannot exceed the number of events.");
                }

                final long numberOfCombinations = ProbabilityTheory
                    .combinations(numberOfEvents, numberOfRequiredSuccesses);

                return numberOfCombinations * Math.pow(probabilityOfOneSuccess, numberOfRequiredSuccesses) *
                    Math.pow((1 - probabilityOfOneSuccess), (double) numberOfEvents - numberOfRequiredSuccesses);
            }

            /**
             * @return P(X = x) = e^(-λ) * λˣ / x!
             */
            public static double poissonDistribution(long numberOfOccurrences, double rateOfSuccess) {
                if (numberOfOccurrences < 0) {
                    throw new IllegalArgumentException("numberOfOccurrences must be non-negative.");
                }
                if (rateOfSuccess < 0) {
                    throw new IllegalArgumentException("rateOfSuccess must be non-negative.");
                }

                return Math.pow(Math.E, -rateOfSuccess) * Math.pow(rateOfSuccess, numberOfOccurrences)
                    / Arithmetic.factorial(numberOfOccurrences);
            }

            /***
             * Calculates the probability density function (PDF) of the normal distribution:
             * @return P(x) = (1 / (σ * √(2π))) * e^(-((x - μ)²) / (2σ²))
             */
            public static double normalDistribution(double mean, double standardDeviation, double rawScoreValue) {
                if (standardDeviation <= 0) {
                    throw new IllegalArgumentException("Standard deviation must be positive.");
                }

                final double multiplier = 1 / (standardDeviation * Math.sqrt(2 * Math.PI));
                final double exponent = -Math.pow(rawScoreValue - mean, 2) / (2 * Math.pow(standardDeviation, 2));
                return multiplier * Math.exp(exponent);
            }

            /***
             * Calculates the probability mass function (PMF)
             * @return P = (1 - p)ˣ * p
             */
            public static double geometricDistributionPMF(long numberOfFailures, double probabilityOfSuccess) {
                if (numberOfFailures < 0) {
                    throw new IllegalArgumentException("numberOfFailures must be non-negative.");
                }
                if (probabilityOfSuccess <= 0 || probabilityOfSuccess > 1) {
                    throw new IllegalArgumentException("probabilityOfSuccess must be in the range (0, 1].");
                }

                return Math.pow(1 - probabilityOfSuccess, numberOfFailures) * probabilityOfSuccess;
            }
        }
    }
}
