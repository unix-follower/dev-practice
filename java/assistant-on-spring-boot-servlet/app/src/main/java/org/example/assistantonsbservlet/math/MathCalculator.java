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
        private Geometry() {
        }

        /**
         * @return π = circumference / diameter
         */
        public static double pi(double circumference, double diameter) {
            return circumference / diameter;
        }

        /**
         * @return a = πr²
         */
        public static double circleArea(double radius) {
            return Math.PI * radius * radius;
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

            return (midpointX - x1) * (y2 - y1) / (x2 - x1) + y1;
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
            return new double[] {
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
            return new double[] {xIntercept, yIntercept};
        }

        /**
         * y = mx + b
         */
        public static double[] intercept(double slopeTerm, double constantTerm) {
            return intercept(slopeTerm, -1, constantTerm);
        }
    }

    public static final class Trigonometry {
        private Trigonometry() {
        }

        public static double csc(double opposite, double hypotenuse) {
            return hypotenuse / opposite;
        }

        public static double csc(double theta) {
            final double sinResult = Math.sin(theta);
            if (sinResult == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            return 1 / sinResult;
        }

        public static double sec(double adjacent, double hypotenuse) {
            return hypotenuse / adjacent;
        }

        public static double sec(double theta) {
            final double cosResult = Math.cos(theta);
            if (cosResult == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            return 1 / cosResult;
        }

        public static double cot(double adjacent, double opposite) {
            return adjacent / opposite;
        }

        public static double cot(double theta) {
            final double tanResult = Math.tan(theta);
            if (tanResult == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            return 1 / tanResult;
        }

        public static double sinDoubleAngle(double theta) {
            return 2 * Math.sin(theta) * Math.cos(theta);
        }

        public static double cosDoubleAngle(double theta) {
            return Math.cos(theta) * Math.cos(theta) - Math.sin(theta) * Math.sin(theta);
        }

        public static double tanDoubleAngle(double theta) {
            return (2 * Math.tan(theta)) / (1 - Math.tan(theta) * Math.tan(theta));
        }

        public static double sinHalfAngle(double theta) {
            return Math.sqrt((1 - Math.cos(theta)) / 2);
        }

        public static double cosHalfAngle(double theta) {
            return Math.sqrt((1 + Math.cos(theta)) / 2);
        }

        public static double tanHalfAngle(double theta) {
            return Math.sqrt((1 - Math.cos(theta)) / (1 + Math.cos(theta)));
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
         * @return ∫(ax + b)dx = (a/2)x^2 + bx + C
         */
        public static double indefiniteLinearIntegral(
            double x, double slope, double constantTerm, double constantOfIntegration) {
            return (slope / 2) * x * x + constantTerm * x + constantOfIntegration;
        }

        /**
         * y = mx + b
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
