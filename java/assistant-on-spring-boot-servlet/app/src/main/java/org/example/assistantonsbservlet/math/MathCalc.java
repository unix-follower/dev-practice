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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.DoubleUnaryOperator;

public final class MathCalc {
    public static final double ONE_FOURTH = 0.25;
    public static final double ONE_HALF = 0.5;

    private static final String DIVISION_BY_ZERO = "Division by zero";
    private static final String MISMATCHED_DIMENSIONS = "Mismatched dimensions";

    private MathCalc() {
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

    private static void checkGreater0(double value) {
        if (value <= 0) {
            throw new IllegalArgumentException("This value must be greater than 0");
        }
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

        public static long sumOfDigits(double number) {
            failIfNotWholeNumber(number);

            long num = Math.abs((long) number);
            long sum = 0;
            while (num > 0) {
                sum += num % 10;
                num /= 10;
            }
            return sum;
        }

        private static void failIfNotWholeNumber(double number) {
            if (!isWholeNumber(number)) {
                throw new IllegalArgumentException("Only whole numbers are supported");
            }
        }

        public static long sumOfLastDigits(double number, long numOfDigits) {
            failIfNotWholeNumber(number);

            long num = Math.abs((long) number);
            long sum = 0;
            for (long i = 0; i < numOfDigits && num > 0; i++) {
                sum += num % 10;
                num /= 10;
            }
            return sum;
        }

        public static boolean isDivisibleBy2(double number) {
            return isWholeNumber(number) && ((long) number) % 2 == 0;
        }

        public static boolean isDivisibleBy3(double number) {
            return isWholeNumber(number) && sumOfDigits(number) % 3 == 0;
        }

        public static boolean isDivisibleBy4(double number) {
            return isWholeNumber(number) && sumOfLastDigits(number, 2) % 4 == 0;
        }

        public static boolean isDivisibleBy5(double number) {
            if (!isWholeNumber(number)) {
                return false;
            }
            final long lastDigit = Math.abs((long) number) % 10;
            return lastDigit == 0 || lastDigit == 5;
        }

        public static boolean isDivisibleBy6(double number) {
            return isDivisibleBy2(number) && isDivisibleBy3(number);
        }

        public static boolean isDivisibleBy8(double number) {
            return isWholeNumber(number) && sumOfLastDigits(number, 3) % 8 == 0;
        }

        public static boolean isDivisibleBy9(double number) {
            return isWholeNumber(number) && sumOfDigits(number) % 9 == 0;
        }

        public static boolean isDivisibleBy10(double number) {
            if (!isWholeNumber(number)) {
                return false;
            }
            final long lastDigit = Math.abs((long) number) % 10;
            return lastDigit == 0;
        }

        public static boolean isWholeNumber(double number) {
            return (number % 1) == 0;
        }

        public static boolean isPrime(double number) {
            if (!isWholeNumber(number) || number < 2) {
                return false;
            }
            final long n = (long) number;
            if (n == 2) {
                return true;
            }
            if (n % 2 == 0) {
                return false;
            }
            for (long i = 3; i <= Math.sqrt(n); i += 2) {
                if (n % i == 0) {
                    return false;
                }
            }
            return true;
        }

        public static long[] primeFactorization(double number) {
            if (!isWholeNumber(number) || number < 2) {
                return new long[0];
            }
            long num = (long) number;
            final var factors = new ArrayList<Long>();
            for (long i = 2; i <= num / i; i++) {
                while (num % i == 0) {
                    factors.add(i);
                    num /= i;
                }
            }
            if (num > 1) {
                factors.add(num);
            }
            return factors.stream().mapToLong(Long::longValue).toArray();
        }

        /**
         * @return prime number -> exponent
         */
        private static Map<Long, Integer> primeFactorMap(long n) {
            final var map = new HashMap<Long, Integer>();
            for (long i = 2; i * i <= n; i++) {
                while (n % i == 0) {
                    map.put(i, map.getOrDefault(i, 0) + 1);
                    n /= i;
                }
            }
            if (n > 1) {
                map.put(n, map.getOrDefault(n, 0) + 1);
            }
            return map;
        }

        /**
         * 1. Find all numbers as a product of their prime factors.
         * 2. Find the highest power of each prime number.
         * 3. Multiply these values together.
         */
        public static long lcmWithPrimeFactorization(long[] numbers) {
            final var primePowers = new HashMap<Long, Integer>();
            for (long number : numbers) {
                final var factors = primeFactorMap(number);
                for (final var entry : factors.entrySet()) {
                    final long prime = entry.getKey();
                    final int power = entry.getValue();
                    primePowers.put(prime, Math.max(primePowers.getOrDefault(prime, 0), power));
                }
            }
            long lcm = 1;
            for (final var entry : primePowers.entrySet()) {
                lcm *= (long) Math.pow(entry.getKey(), entry.getValue());
            }
            return lcm;
        }
    }

    /**
     * <table>
     *     <tr>
     *         <th>Square root</th><th>Is perfect square?</th>
     *     </tr>
     *     <tr><td>√1 = 1</td><td>✅</td></tr>
     *     <tr><td>√2 ≈ 1.41</td><td>❌</td></tr>
     *     <tr><td>√3 ≈ 1.73</td><td>❌</td></tr>
     *     <tr><td>√4 = 2</td><td>✅</td></tr>
     *     <tr><td>√5 ≈ 2.24</td><td>❌</td></tr>
     *     <tr><td>√7 ≈ 2.65</td><td>❌</td></tr>
     *     <tr><td>√9 = 3</td><td>✅</td></tr>
     *     <tr><td>√11 ≈ 3.32</td><td>❌</td></tr>
     *     <tr><td>√13 ≈ 3.61</td><td>❌</td></tr>
     *     <tr><td>√16 = 4</td><td>✅</td></tr>
     *     <tr><td>√17 ≈ 4.12</td><td>❌</td></tr>
     *     <tr><td>√19 ≈ 4.34</td><td>❌</td></tr>
     *     <tr><td>√25 = 5</td><td>✅</td></tr>
     *     <tr><td>√27 = √(9 × 3) = √9 × √3 = 3√3</td><td>❌</td></tr>
     *     <tr><td>√36 = 6</td><td>✅</td></tr>
     *     <tr><td>√45 = √(9 × 5) = √9 × √5 = 3√5</td><td>❌</td></tr>
     *     <tr><td>√49 = 7</td><td>✅</td></tr>
     *     <tr><td>√52 ≈ 2√13 = 7.22</td><td>❌</td></tr>
     *     <tr><td>√64 = 8</td><td>✅</td></tr>
     *     <tr><td>√81 = 9</td><td>✅</td></tr>
     *     <tr><td>√100 = 10</td><td>✅</td></tr>
     *     <tr><td>√121 = 11</td><td>✅</td></tr>
     *     <tr><td>√144 = 12</td><td>✅</td></tr>
     * </table>
     */
    public static final class Algebra {
        private Algebra() {
        }

        /**
         * @return 𝚪(n) = (n - 1)!
         */
        public static double gammaFunction(double x) {
            if (x == 0) {
                throw new IllegalArgumentException("Gamma function is undefined for 0");
            }

            // Lanczos approximation coefficients
            final double[] p = {
                676.5203681218851,
                -1259.1392167224028,
                771.32342877765313,
                -176.61502916214059,
                12.507343278686905,
                -0.13857109526572012,
                9.9843695780195716e-6,
                1.5056327351493116e-7
            };
            final int g = 7;
            if (x < 0.5) {
                // Reflection formula
                return Math.PI / (Math.sin(Math.PI * x) * gammaFunction(1 - x));
            }
            x -= 1;
            double a = 0.99999999999980993;
            for (int i = 0; i < p.length; i++) {
                a += p[i] / (x + i + 1);
            }
            final double t = x + g + 0.5;
            return Math.sqrt(2 * Math.PI) * Math.pow(t, x + 0.5) * Math.exp(-t) * a;
        }

        // Exponents and logarithms

        /**
         * @return xⁿ * xᵐ = xⁿ⁺ᵐ
         */
        public static double addExponentsLaw(double base, double[] exponents) {
            final double exponent = Arrays.stream(exponents).sum();
            return Math.pow(base, exponent);
        }

        /**
         * @return xⁿ / xᵐ = xⁿ⁻ᵐ
         */
        public static double subtractExponentsLaw(double base, double[] exponents) {
            final double exponent = Arrays.stream(exponents)
                .reduce((left, right) -> left - right).orElse(0);
            return Math.pow(base, exponent);
        }

        /**
         * @return x⁻ⁿ = (1/x)ⁿ
         */
        public static double negativeExponent(double base, double exponent) {
            return Math.pow(1 / base, Math.abs(exponent));
        }

        /**
         * @return xⁿ * yⁿ = (x * y)ⁿ
         */
        public static double multiplyWithSamePower(double x, double y, double exponent) {
            return Math.pow(x * y, exponent);
        }

        /**
         * x = ⁿ√a as xⁿ = a
         * y = ±√x ⟺ y² = x
         * √x = x¹/² = x^0.5
         * x¹/² * y¹/² = (x * y)¹/²
         * (x^0.5)² = x^(0.5*2) = x
         *
         * @return √(x * y) = √x * √y
         */
        public static double squareRootMultiply(double x, double y) {
            return Math.sqrt(x * y);
        }

        /*
         * @return √(x / y) = √x / √y
         */
        public static double squareRootDivide(double x, double y) {
            return Math.sqrt(x / y);
        }

        /**
         * @return √(xⁿ) = (xⁿ)¹/² = xⁿ/²
         */
        public static double squareRootWithExponent(double x, double exponent) {
            return Math.sqrt(Math.pow(x, exponent));
        }

        /**
         * i = √(-1)
         * x = a + bi
         */
        public static double squareRootWithComplexNumber(double x) {
            return Math.sqrt(Math.abs(x));
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

        /**
         * Given three sides (SSS). Heron's formula.
         *
         * @return h = 0.5/b * √((a + b + c) * (-a + b + c) * (a - b + c) * (a + b - c)). The units are cm²
         */
        public static double heightOfTriangleWithSSS(
            double targetSide, double sideLengthA, double sideLengthB, double sideLengthC) {
            final double abSum = sideLengthA + sideLengthB;

            return ONE_HALF / targetSide * Math.sqrt(
                (abSum + sideLengthC)
                    * (-sideLengthA + sideLengthB + sideLengthC)
                    * (sideLengthA - sideLengthB + sideLengthC)
                    * (abSum - sideLengthC)
            );
        }

        /**
         * The basic formulation of the Heron's formula.
         *
         * @return A = √(s(s-a)(s-b)(s-c)). The units are cm²
         */
        public static double heronFormulaUsingSemiperimeter(double sideA, double sideB, double sideC) {
            final double semiperimeter = semiperimeter(sideA, sideB, sideC);
            return Math.sqrt(
                semiperimeter * (semiperimeter - sideA) * (semiperimeter - sideB) * (semiperimeter - sideC)
            );
        }

        /**
         * The Heron's formula w/o semiperimeter
         *
         * @return A = ¼ * √(4a²b² - (a² + b² − c²)²). The units are cm²
         */
        public static double heronFormulaUsingQuadProduct(double sideA, double sideB, double sideC) {
            final double aSquared = sideA * sideA;
            final double bSquared = sideB * sideB;
            final double cSquared = sideC * sideC;
            return ONE_FOURTH * Math.sqrt(4 * aSquared * bSquared - Math.pow(aSquared + bSquared - cSquared, 2));
        }

        /**
         * @return A = (ch)/2. The units are cm²
         */
        public static double areaWithBaseAndHeight(double base, double height) {
            return base * height / 2;
        }

        public static double[] scaleneTriangleHeight(double sideA, double sideB, double sideC) {
            final double sideAHeight = heightOfTriangleWithSSS(sideA, sideA, sideB, sideC);
            final double sideBHeight = heightOfTriangleWithSSS(sideB, sideA, sideB, sideC);
            final double sideCHeight = heightOfTriangleWithSSS(sideC, sideA, sideB, sideC);
            return new double[]{sideAHeight, sideBHeight, sideCHeight};
        }

        /**
         * @return hΔ = a * √3/2. The units are cm²
         */
        public static double equilateralTriangleHeight(double sides) {
            return sides * Math.sqrt(3) / 2;
        }

        /**
         * @return area = √3/4 * a². The units are cm²
         */
        public static double equilateralTriangleArea(double sides) {
            return Math.sqrt(3) / 4 * sides * sides;
        }

        /**
         * hₐ = 2 * area/a = √(a²−(0.5*b)²) * b/a
         * hₐ = b * sin(β)
         * h_b = √(a²−(0.5*b)²)
         * The units are cm²
         */
        public static double[] isoscelesTriangleHeight(double sideA, double sideB) {
            final double heightB = Math.sqrt(sideA * sideA - Math.pow(0.5 * sideB, 2));
            return new double[]{heightB * sideB / sideA, heightB};
        }

        /**
         * @return area = (bh_b)/2. The units are cm²
         */
        public static double isoscelesTriangleArea(double base, double heightB) {
            return (base * heightB) / 2;
        }

        /**
         * The units are cm²
         */
        public static double[] rightTriangleHeight(double sideA, double sideB, double hypotenuse) {
            final double sideAHeight = heightOfTriangleWithSSS(sideA, sideA, sideB, hypotenuse);
            final double sideBHeight = heightOfTriangleWithSSS(sideB, sideA, sideB, hypotenuse);
            return new double[]{sideAHeight, sideBHeight, sideA * sideB / hypotenuse};
        }

        // 2D geometry

        /**
         * @return area = ½ * a * b. The units are cm²
         */
        public static double area(double sideA, double sideB) {
            return 0.5 * sideA * sideB;
        }

        /**
         * For isosceles triangle: 2a + b.
         *
         * @return perimeter = a + b + c. The units are cm
         */
        public static double perimeter(double sideA, double sideB, double hypotenuse) {
            return sideA + sideB + hypotenuse;
        }

        public static double semiperimeter(double sideA, double sideB, double hypotenuse) {
            return perimeter(sideA, sideB, hypotenuse) / 2;
        }

        /**
         * a² + b² = c²
         * <br/> Find a leg (a or b).
         *
         * @return a = √(c² - b²). The units are cm
         */
        public static double[] pythagoreanTheoremForRightTriangleWithLegAndHypotenuse(double side, double hypotenuse) {
            final double squaredSide = side * side;
            final double squaredHypotenuse = hypotenuse * hypotenuse;
            final double squaredSide2 = squaredHypotenuse - squaredSide;
            return new double[]{side, Math.sqrt(squaredSide2), hypotenuse};
        }

        /**
         * a² + b² = c²
         * For a non right-angled triangle:
         * <br/> Find a leg (a or b).
         *
         * @return √c² = a² + b² - 2ab * cos(γ). The units are cm
         */
        public static double[] pythagoreanTheoremWithLegsAndAngle(double sideA, double sideB, double angleGammaRad) {
            final double squaredSideA = sideA * sideA;
            final double squaredSideB = sideB * sideB;
            final double squaredUnkownSide = squaredSideA + squaredSideB - 2 * sideA * sideB * Math.cos(angleGammaRad);
            return new double[]{sideA, sideB, Math.sqrt(squaredUnkownSide)};
        }

        /**
         * a² + b² = c²
         * <br/> Find the hypotenuse (c).
         *
         * @return c = √(a² + b²). The units are cm
         */
        public static double[] pythagoreanTheoremForRightTriangleWithLegs(double sideA, double sideB) {
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

        /**
         * @return area = b * h / 2. The units are cm²
         */
        public static double areaOfTriangleWithBaseAndHeight(double base, double height) {
            checkGreater0(base);
            checkGreater0(height);
            return base * height / 2;
        }

        /**
         * Given three sides (SSS). Heron's formula.
         *
         * @return area = ¼ * √((a + b + c) * (-a + b + c) * (a - b + c) * (a + b - c)). The units are cm²
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

        /**
         * The properties of equilateral triangle:
         * <br/> - all sides are equal
         * <br/> - all angles are equal to 60°
         */
        public static boolean isEquilateralTriangle(double sideA, double sideB, double sideC) {
            return sideA == sideB && sideA == sideC;
        }

        /**
         * The properties of scalene triangle:
         * <br/> - all sides are different
         * <br/> - depending on the angles might be acute, obtuse or right
         */
        public static boolean isScaleneTriangle(double sideA, double sideB, double sideC) {
            return sideA != sideB && sideA != sideC && sideB != sideC;
        }

        /**
         * All angles are less than 90°
         */
        public static boolean isAcuteTriangle(double angleAlphaRad, double angleBetaRad, double angleGammaRad) {
            return angleAlphaRad < Trigonometry.PI_OVER_2 && angleBetaRad < Trigonometry.PI_OVER_2
                && angleGammaRad < Trigonometry.PI_OVER_2;
        }

        public static boolean isAcuteTriangleWithSSA(double angleRad, double sideA, double sideB) {
            final double[] sides = pythagoreanTheoremWithLegsAndAngle(sideA, sideB, angleRad);
            final double sideC = sides[Constants.ARR_3RD_INDEX];
            final double[] angles = Trigonometry.lawOfCosSSS(sideA, sideB, sideC);
            return isAcuteTriangle(
                angles[Constants.ALPHA_INDEX], angles[Constants.BETA_INDEX], angles[Constants.GAMMA_INDEX]);
        }

        /**
         * One of the angles is exactly 90°
         */
        public static boolean isRightTriangle(double angleAlphaRad, double angleBetaRad, double angleGammaRad) {
            return angleAlphaRad == Trigonometry.PI_OVER_2 || angleBetaRad == Trigonometry.PI_OVER_2
                || angleGammaRad == Trigonometry.PI_OVER_2;
        }

        public static boolean isRightTriangleWithSSA(double angleRad, double sideA, double sideB) {
            final double[] sides = pythagoreanTheoremWithLegsAndAngle(sideA, sideB, angleRad);
            final double sideC = sides[Constants.ARR_3RD_INDEX];
            final double[] angles = Trigonometry.lawOfCosSSS(sideA, sideB, sideC);
            return isRightTriangle(
                angles[Constants.ALPHA_INDEX], angles[Constants.BETA_INDEX], angles[Constants.GAMMA_INDEX]);
        }

        /**
         * One of the angles measures more than 90°
         */
        public static boolean isObtuseTriangle(double angleAlphaRad, double angleBetaRad, double angleGammaRad) {
            return angleAlphaRad > Trigonometry.PI_OVER_2 || angleBetaRad > Trigonometry.PI_OVER_2
                || angleGammaRad > Trigonometry.PI_OVER_2;
        }

        public static boolean isObtuseTriangleWithSSA(double angleRad, double sideA, double sideB) {
            final double[] sides = pythagoreanTheoremWithLegsAndAngle(sideA, sideB, angleRad);
            final double sideC = sides[Constants.ARR_3RD_INDEX];
            final double[] angles = Trigonometry.lawOfCosSSS(sideA, sideB, sideC);
            return isObtuseTriangle(
                angles[Constants.ALPHA_INDEX], angles[Constants.BETA_INDEX], angles[Constants.GAMMA_INDEX]);
        }

        // Angle calculators

        /**
         * complementary angle = 90° - angle
         * complementary angle = π/2 - angle
         */
        public static double complementaryAngle(double angleRadians) {
            return Trigonometry.PI_OVER_2 - angleRadians;
        }

        public static boolean isComplementaryAngle(double angleAlphaRad, double angleBetaRad) {
            final double angleSum = angleAlphaRad + angleBetaRad;
            final double epsilon = 1e-9; // tolerance of the difference
            return Math.abs(Trigonometry.PI_OVER_2 - angleSum) <= epsilon;
        }

        /**
         * supplementary angle = 180° - angle
         *
         * @return supplementary angle = π - angle
         */
        public static double supplementaryAngle(double angleRadians) {
            return Math.PI - angleRadians;
        }

        /**
         * α + β = 180° (π) -> true
         * α + β ≠ 180° (π) -> false
         * If true:
         * sin(α) = sin(β)
         * cos(α) = -cos(β)
         * tan(α) = -tan(β)
         */
        public static boolean isSupplementaryAngles(double angleAlphaRad, double angleBetaRad) {
            final double angleSum = angleAlphaRad + angleBetaRad;
            final double epsilon = 1e-9; // tolerance of the difference
            return Math.abs(Math.PI - angleSum) <= epsilon;
        }

        public static double coterminalAngle(double angleRadians) {
            final double quotient = Math.floor(angleRadians / Trigonometry.PI2);
            final double mulResult = Trigonometry.PI2 * quotient;
            return angleRadians - mulResult;
        }

        /**
         * β = α ± (360° * k)
         * β = α ± (2π * k)
         * sin(α) = sin(α ± (360° * k))
         */
        public static double[] coterminalAngles(double angleRadians, int min, int max) {
            final var angles = new ArrayList<Double>();
            for (int k = min; k <= max; k++) {
                final double rotations = Trigonometry.PI2 * k;
                if (rotations != 0) { // skip adding itself
                    angles.add(angleRadians + rotations);
                }
            }
            return angles.stream().mapToDouble(Double::doubleValue).toArray();
        }

        /**
         * β - α = 2π * k
         */
        public static boolean areCoterminalAngles(double angleAlphaRad, double angleBetaRad, int rotations) {
            final double angleDiff = angleBetaRad - angleAlphaRad;
            return angleDiff == Trigonometry.PI2 * rotations;
        }

        public static double subtractFullAngleIfNeeded(double angleRadians) {
            if (angleRadians > Trigonometry.PI2) {
                return subtractFullAngleIfNeeded(angleRadians - Trigonometry.PI2);
            }
            return angleRadians;
        }

        /**
         * 0° to 90°: reference angle = angle
         * 90° to 180°: reference angle = 180° − angle
         * 180° to 270°: reference angle = angle − 180°
         * 270° to 360°: reference angle = 360° − angle
         * 0 to π/2: reference angle = angle
         * π/2 to π: reference angle = π − angle
         * π to 3π/2: reference angle = angle − π
         * 3π/2 to 2π: reference angle = 2π − angle
         */
        public static double referenceAngle(double angleRadians) {
            final double normalizedAngleRad = subtractFullAngleIfNeeded(angleRadians);
            final int quadrant = Trigonometry.quadrant(normalizedAngleRad);
            switch (quadrant) {
                case 1 -> {
                    return normalizedAngleRad;
                }
                case 2 -> {
                    return Math.PI - normalizedAngleRad;
                }
                case 3 -> {
                    return normalizedAngleRad - Math.PI;
                }
                case 4 -> {
                    return Trigonometry.PI2 - normalizedAngleRad;
                }
                default -> throw new IllegalStateException();
            }
        }

        /**
         * @return θ = L / r. The units are radians
         */
        public static double centralAngleGivenArcLengthRadius(double arcLength, double radius) {
            return arcLength / radius;
        }

        /**
         * @return L = θ * r. The units are meters
         */
        public static double arcLength(double centralAngleRad, double radius) {
            return centralAngleRad * radius;
        }

        /**
         * Minute angle = 6° * number of minutes
         * Hour angle = 30° * number of hours + 0.5° * number of minutes
         * Minute to hour hand angle = |Hour angle − Minute angle|
         * Hour to minute hand angle = 360° − Minute to hour hand angle
         */
        public static double[] clockAngle(short hours, short minutes) {
            final double minuteAngle = Math.toRadians(6) * minutes;
            final double hourAngle = Trigonometry.PI_OVER_6 * hours + Math.toRadians(0.5) * minutes;
            final double minuteToHourAngle = Math.abs(hourAngle - minuteAngle);
            final double hourToMinuteAngle = Math.abs(Trigonometry.PI2 - minuteToHourAngle);
            return new double[]{hourToMinuteAngle, minuteToHourAngle};
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
     * <br/><strong>Inverse</strong>
     * <table>
     *     <tr><th>x</th><th colspan="2">arccos(x)</th><th colspan="2">arcsine(x)</th></tr>
     *     <tr><td></td><td>Degrees</td><td>Radians</td><td>Degrees</td><td>Radians</td></tr>
     *     <tr><td>-1</td><td>180°</td><td>π</td><td>-90°</td><td>-π/2</td></tr>
     *     <tr><td>-√3/2</td><td>150°</td><td>5π/6</td><td>-60°</td><td>-π/3</td></tr>
     *     <tr><td>-√2/2</td><td>135°</td><td>3π/4</td><td>-45°</td><td>-π/4</td></tr>
     *     <tr><td>-1/2</td><td>120°</td><td>2π/3</td><td>-30°</td><td>-π/6</td></tr>
     *     <tr><td>0</td><td>90°</td><td>π/2</td><td>0°</td><td>0</td></tr>
     *     <tr><td>1/2</td><td>60°</td><td>π/3</td><td>30°</td><td>π/6</td></tr>
     *     <tr><td>√2/2</td><td>45°</td><td>π/4</td><td>45°</td><td>π/4</td></tr>
     *     <tr><td>√3/2</td><td>30°</td><td>π/6</td><td>60°</td><td>π/3</td></tr>
     *     <tr><td>1</td><td>0°</td><td>0</td><td>90°</td><td>π/2</td></tr>
     * </table>
     * <br/><strong>Inverse Tangent</strong>
     * <table>
     *     <tr><th>x</th><th colspan="2">arctan(x)</th></tr>
     *     <tr><td></td><td>Degrees</td><td>Radians</td></tr>
     *     <tr><td>−∞</td><td>-90°</td><td>-π/2</td></tr>
     *     <tr><td>-3</td><td>−71.565°</td><td>−1.2490</td></tr>
     *     <tr><td>-2</td><td>−63.435°</td><td>−1.1071</td></tr>
     *     <tr><td>-√3</td><td>−60°</td><td>-π/3</td></tr>
     *     <tr><td>-1</td><td>-45°</td><td>-π/4</td></tr>
     *     <tr><td>-√3/3</td><td>-30°</td><td>-π/6</td></tr>
     *     <tr><td>0</td><td>0°</td><td>0</td></tr>
     *     <tr><td>√3/2</td><td>30°</td><td>π/6</td></tr>
     *     <tr><td>1</td><td>45°</td><td>π/4</td></tr>
     *     <tr><td>√3</td><td>60°</td><td>π/3</td></tr>
     *     <tr><td>2</td><td>63.435°</td><td>1.1071</td></tr>
     *     <tr><td>3</td><td>71.565°</td><td>1.2490</td></tr>
     *     <tr><td>∞</td><td>90°</td><td>π/2</td></tr>
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
         * D(csc) = {x : x ≠ k*180°, k ∈ ℤ}.
         * Cofunction: sec(x).
         * <p/>The cosecant function
         * <br/>- is odd: csc(x) = -csc(x)
         * <br/>- is periodic: csc(x) = csc(x + 360°)
         * <br/>- doesn't always exist.
         * <br/>- Range: -∞<y≤-1 ∪ 1≤y<∞
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
         * D(sec) = {x : x ≠ 90° + k*180°, k ∈ X}.
         * Cofunction: csc(x).
         * <p/>The secant function
         * <br/>- is even: sec(α) = sec(-α)
         * <br/>- is periodic: sec(x) = sec(x + 360°)
         * <br/>- doesn't always exist.
         * <br/>- Range: -∞<y≤-1 ∪ 1≤y<∞
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
         * D(cot) = {x : x ≠ k*180°, k ∈ ℤ}.
         * Cofunction: tan(x).
         * <p/>The cotangent function
         * <br/>- is odd: cot(x) = -cot(-x)
         * <br/>- is periodic: cot(x) = cot(x + 360°)
         * <br/>- doesn't always exist.
         * <br/>- Range: -∞<y<∞
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

        /**
         * a = √(b² + c² - 2bc × cos(α))
         * b = √(a² + c² - 2ac × cos(β))
         * c = √(a² + b² - 2ab × cos(γ))
         *
         * @return √c² = a² + b² - 2ab * cos(γ)
         */
        public static double lawOfCosSAS(double sideA, double sideB, double angleGammaRadians) {
            final double sideCSquared = sideA * sideA + sideB * sideB - 2 * sideA * sideB * Math.cos(angleGammaRadians);
            return Math.sqrt(sideCSquared);
        }

        /**
         * Alternative for isosceles triangle:
         * α = arccos((a² + a² - b²) / (2a²))
         * β = arccos((a² + b² - a²) / (2ab)) = arccos(b / (2a))
         * <br/>
         * α = arccos((b² + c² - a²)/(2bc))
         * β = arccos((a² + c² - b²)/(2ac))
         * γ = arccos((a² + b² - c²)/(2ab))
         */
        public static double[] lawOfCosSSS(double sideA, double sideB, double sideC) {
            final double aSquared = sideA * sideA;
            final double bSquared = sideB * sideB;
            final double cSquared = sideC * sideC;
            final double angleAlphaRad = Math.acos((bSquared + cSquared - (-sideA * -sideA)) / (2 * sideB * sideC));
            final double angleBetaRad = Math.acos((aSquared + cSquared - (-sideB * -sideB)) / (2 * sideA * sideC));
            final double angleGammaRad = Math.acos((aSquared + bSquared - (-sideC * -sideC)) / (2 * sideA * sideB));
            return new double[]{angleAlphaRad, angleBetaRad, angleGammaRad};
        }

        /**
         * a / sin(α) = b / sin(β) = c / sin(γ)
         *
         * @return b = a / sin(α) * sin(β)
         */
        public static double lawOfSinGivenSideAAndAnglesAlphaBeta(
            double side, double angleAlphaRadians, double angleBetaRadians) {
            return side / Math.sin(angleAlphaRadians) * Math.sin(angleBetaRadians);
        }

        /**
         * @return a = b * sin(α) / sin(β)
         */
        public static double lawOfSinGivenSideBAndAnglesAlphaBeta(
            double side, double angleAlphaRadians, double angleBetaRadians) {
            return side * Math.sin(angleAlphaRadians) / Math.sin(angleBetaRadians);
        }

        /**
         * @return β = arcsin(b * sin(α) / a)
         */
        public static double lawOfSinGivenSidesABAndAngleAlpha(double sideA, double sideB, double angleAlphaRadians) {
            return Math.asin(sideB * Math.sin(angleAlphaRadians) / sideA);
        }

        /**
         * @return α = arcsin(a * sin(β) / b)
         */
        public static double lawOfSinGivenSidesABAndAngleBeta(double sideA, double sideB, double angleBetaRadians) {
            return Math.asin(sideA * Math.sin(angleBetaRadians) / sideB);
        }

        /**
         * @return γ = arcsin(b * sin(β) / c)
         */
        public static double lawOfSinGivenSidesBCAndAngleBeta(double sideB, double sideC, double angleBetaRadians) {
            return Math.asin(sideB * Math.sin(angleBetaRadians) / sideC);
        }

        /**
         * @return γ = arcsin(c * sin(β) / a)
         */
        public static double lawOfSinGivenSidesACAndAngleAlpha(double sideA, double sideC, double angleAlphaRadians) {
            return Math.asin(sideC * Math.sin(angleAlphaRadians) / sideA);
        }

        /**
         * @return sin(α+β) = sin(α)cos(β) + cos(α)sin(β)
         */
        public static double sinAngleSum(double angleAlpha, double angleBeta) {
            return Math.sin(angleAlpha) * Math.cos(angleBeta) + Math.cos(angleAlpha) * Math.sin(angleBeta);
        }

        /**
         * @return sin(α-β) = sin(α)cos(β) - cos(α)sin(β)
         */
        public static double sinAngleDifference(double angleAlpha, double angleBeta) {
            return Math.sin(angleAlpha) * Math.cos(angleBeta) - Math.cos(angleAlpha) * Math.sin(angleBeta);
        }

        /**
         * @return cos(α+β) = cos(α)cos(β) − sin(α)sin(β)
         */
        public static double cosAngleSum(double angleAlpha, double angleBeta) {
            return Math.cos(angleAlpha) * Math.cos(angleBeta) - Math.sin(angleAlpha) * Math.sin(angleBeta);
        }

        /**
         * @return cos(α-β) = cos(α)cos(β) + sin(α)sin(β)
         */
        public static double cosAngleDifference(double angleAlpha, double angleBeta) {
            return Math.cos(angleAlpha) * Math.cos(angleBeta) + Math.sin(angleAlpha) * Math.sin(angleBeta);
        }

        /**
         * @return tan(α+β) = (tan(α)+tan(β)) / (1−tan(α)tan(β))
         */
        public static double tanAngleSum(double angleAlpha, double angleBeta) {
            final double denominator = 1 - Math.tan(angleAlpha) * Math.tan(angleBeta);
            if (denominator == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            final double numerator = Math.tan(angleAlpha) + Math.tan(angleBeta);
            return numerator / denominator;
        }

        /**
         * @return tan(α-β) = (tan(α)-tan(β)) / (1+tan(α)tan(β))
         */
        public static double tanAngleDifference(double angleAlpha, double angleBeta) {
            final double denominator = 1 + Math.tan(angleAlpha) * Math.tan(angleBeta);
            if (denominator == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            final double numerator = Math.tan(angleAlpha) - Math.tan(angleBeta);
            return numerator / denominator;
        }

        /**
         * @return cot(α+β) = (cot(α)cot(β)−1) / (cot(β)+cot(α))
         */
        public static double cotAngleSum(double angleAlpha, double angleBeta) {
            final double cotAlpha = cot(angleAlpha);
            final double cotBeta = cot(angleBeta);
            final double denominator = cotAlpha + cotBeta;
            if (denominator == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            return (cotAlpha * cotBeta - 1) / denominator;
        }

        /**
         * @return cot(α-β) = (cot(α)cot(β)+1) / (cot(β)-cot(α))
         */
        public static double cotAngleDifference(double angleAlpha, double angleBeta) {
            final double cotAlpha = cot(angleAlpha);
            final double cotBeta = cot(angleBeta);
            final double denominator = cotBeta - cotAlpha;
            if (denominator == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            return (cotAlpha * cotBeta + 1) / denominator;
        }

        /**
         * @return sec(α+β) = (sec(α)sec(β)csc(α)csc(β)) / (csc(α)csc(β)−sec(α)sec(β))
         */
        public static double secAngleSum(double angleAlpha, double angleBeta) {
            final double secAlpha = sec(angleAlpha);
            final double secBeta = sec(angleBeta);
            final double cscAlpha = csc(angleAlpha);
            final double cscBeta = csc(angleBeta);
            final double denominator = cscAlpha * cscBeta - secAlpha * secBeta;
            if (denominator == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            return (secAlpha * secBeta * cscAlpha * cscBeta) / denominator;
        }

        /**
         * @return sec(α-β) = (sec(α)sec(β)csc(α)csc(β)) / (csc(α)csc(β)+sec(α)sec(β))
         */
        public static double secAngleDifference(double angleAlpha, double angleBeta) {
            final double secAlpha = sec(angleAlpha);
            final double secBeta = sec(angleBeta);
            final double cscAlpha = csc(angleAlpha);
            final double cscBeta = csc(angleBeta);
            final double denominator = cscAlpha * cscBeta + secAlpha * secBeta;
            if (denominator == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            return (secAlpha * secBeta * cscAlpha * cscBeta) / denominator;
        }

        /**
         * @return csc(α+β) = (sec(α)sec(β)csc(α)csc(β)) / (sec(α)csc(β)+csc(α)sec(β))
         */
        public static double cscAngleSum(double angleAlpha, double angleBeta) {
            final double secAlpha = sec(angleAlpha);
            final double secBeta = sec(angleBeta);
            final double cscAlpha = csc(angleAlpha);
            final double cscBeta = csc(angleBeta);
            final double denominator = secAlpha * cscBeta + cscAlpha * secBeta;
            if (denominator == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            return (secAlpha * secBeta * cscAlpha * cscBeta) / denominator;
        }

        /**
         * @return csc(α-β) = (sec(α)sec(β)csc(α)csc(β)) / (sec(α)csc(β)-csc(α)sec(β))
         */
        public static double cscAngleDifference(double angleAlpha, double angleBeta) {
            final double secAlpha = sec(angleAlpha);
            final double secBeta = sec(angleBeta);
            final double cscAlpha = csc(angleAlpha);
            final double cscBeta = csc(angleBeta);
            final double denominator = secAlpha * cscBeta - cscAlpha * secBeta;
            if (denominator == 0) {
                throw new ArithmeticException(DIVISION_BY_ZERO);
            }
            return (secAlpha * secBeta * cscAlpha * cscBeta) / denominator;
        }

        /**
         * sin(α) = y/1 = y
         * sin(α) = opposite/hypotenuse = a/c
         * Cofunction: cos(x).
         * <p/>The sine function:
         * <br/>- an odd: sin(−α) = −sin(α).
         * <br/>- period: 2π
         * <br/>- Range: −1 ≤ sin(α) ≤ 1
         */
        public static double sin(double angleRadians) {
            return Math.sin(angleRadians);
        }

        /**
         * @return sin(α) = opposite / hypotenuse = a/c
         */
        public static double sin(double opposite, double hypotenuse) {
            return opposite / hypotenuse;
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

        public static int quadrant(double angleRadians) {
            if (0 <= angleRadians && angleRadians <= Math.PI / 2) {
                // 0<α≤π/2
                return 1;
            } else if (Math.PI / 2 < angleRadians && angleRadians <= Math.PI) {
                // π/2<α≤π
                return 2;
            } else if (Math.PI < angleRadians && angleRadians <= 3 * Math.PI / 2) {
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
         * Cofunction: sin(x).
         * <p/>The cosine function:
         * <br/>- an even: cos(−α) = cos(α).
         * <br/>- period: 2π
         * <br/>- Range: −1 ≤ cos(α) ≤ 1
         */
        public static double cos(double angleRadians) {
            return Math.sin(angleRadians);
        }

        /**
         * @return cos(α) = adjacent / hypotenuse = b / c
         */
        public static double cos(double adjacent, double hypotenuse) {
            return adjacent / hypotenuse;
        }

        /**
         * tan(α) = y/x = sin(α)/cos(α)
         * tan(α) = opposite / adjacent = a / b
         * Cofunction: cot(x).
         * <p/>The tangent function:
         * <br/>- an even: tan(−x) = -tan(x).
         * <br/>- Period: 2π.
         * <br/>- Range: -∞<y<∞
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

        /**
         * A * sin(B(x−C/B)) + D
         *
         * @return f(x) = A * sin(Bx−C) + D
         */
        public static double[] sinPhaseShift(double x, double amplitude, double period,
                                             double phase, double verticalShift) {
            final double phaseShift = phase / period;
            final double periodShift = 2 * Math.PI / period;
            return new double[]{phaseShift, periodShift, amplitude * Math.sin(period * x - phase) + verticalShift};
        }

        /**
         * @return f(x) = A * cos(Bx−C) + D
         */
        public static double[] cosPhaseShift(double x, double amplitude, double period,
                                             double phase, double verticalShift) {
            final double phaseShift = phase / period;
            final double periodShift = 2 * Math.PI / period;
            return new double[]{phaseShift, periodShift, amplitude * Math.cos(period * x - phase) + verticalShift};
        }

        /**
         * @return sin²(x) = (1−cos(2x)) / 2
         */
        public static double sinPowerReducing(double angleRadians) {
            return (1 - Math.cos(2 * angleRadians)) / 2;
        }

        /**
         * @return cos²(x) = (1+cos(2x)) / 2
         */
        public static double cosPowerReducing(double angleRadians) {
            return (1 + Math.cos(2 * angleRadians)) / 2;
        }

        /**
         * @return tan²(x) = (1-cos(2x)) / (1+cos(2x))
         */
        public static double tanPowerReducing(double angleRadians) {
            final double cosine = Math.cos(2 * angleRadians);
            return (1 - cosine) / (1 + cosine);
        }

        /**
         * @return sin(α) = tan(α) * cos(α)
         */
        public static double findSinWithCosAndTan(double angleAlphaRadians) {
            return Math.tan(angleAlphaRadians) * Math.cos(angleAlphaRadians);
        }
    }

    public static final class Seq {
        private Seq() {
        }

        /**
         * @return aₙ = a₁rⁿ⁻¹, n∈N
         */
        public static double[] geometricSequence(double firstTerm, double commonRatio, int limit) {
            final double[] sequence = new double[limit];
            for (int i = 1; i <= limit; i++) {
                final int index = i - 1;
                sequence[index] = firstTerm * Math.pow(commonRatio, index);
            }
            return sequence;
        }

        /**
         * @return aⱼ + … + aₖ
         */
        public static double geometricSequenceFiniteSum(double[] sequence, int startIndex, int endIndex) {
            double sum = 0;
            for (int i = startIndex; i <= endIndex; i++) {
                sum += sequence[i];
            }
            return sum;
        }

        /**
         * aₙ = aₘ * rⁿ⁻ᵐ
         * Alternative: r = ⁽ⁿ⁻ᵐ⁾√(aₙ / aₘ)
         *
         * @return r = (aₙ / aₘ)¹/⁽ⁿ⁻ᵐ⁾
         */
        public static double geometricSequenceCommonRatioForNonConsecutiveTerms(
            double mTermPosition, double mTerm, double nTermPosition, double nTerm) {
            return Math.pow(nTerm / mTerm, 1. / (nTermPosition - mTermPosition));
        }

        /**
         * If |r| > 1, then the series diverges.
         * If |r| < 1, then the series converges.
         * If |r| = 1, then the series is periodic, but its sum diverges.
         *
         * @return r = aₙ / aₙ₋₁
         */
        public static double geometricSequenceCommonRatio(double previousTerm, double nTerm) {
            return nTerm / previousTerm;
        }

        public static double[] arithmeticSequence(double firstTerm, double commonDifference, int limit) {
            final double[] sequence = new double[limit];
            for (int i = 1; i <= limit; i++) {
                sequence[i - 1] = arithmeticSequenceNthTerm(firstTerm, commonDifference, i);
            }
            return sequence;
        }

        /**
         * @return aₙ = a₁ + (n-1)d
         */
        public static double arithmeticSequenceNthTerm(double firstTerm, double commonDifference, int nthTermPosition) {
            return firstTerm + (nthTermPosition - 1) * commonDifference;
        }

        /**
         * @return S = n/2 * (2a₁ + (n−1)d)
         */
        public static double arithmeticSequenceSum(double firstTerm, double commonDiff, int nthTermPosition) {
            return nthTermPosition / 2. * (2 * firstTerm + (nthTermPosition - 1) * commonDiff);
        }

        /**
         * @return aⱼ + ... + aₖ
         */
        public static double arithmeticSequenceSum(
            double firstTerm, double commonDiff, int firstTermPosition, int nthTermPosition) {
            final double firstTermSum = arithmeticSequenceSum(firstTerm, commonDiff, firstTermPosition - 1);
            final double nthTermSum = arithmeticSequenceSum(firstTerm, commonDiff, nthTermPosition);
            return nthTermSum - firstTermSum;
        }

        /**
         * a = {aₙ}ₙ₌₀^∞
         * b = {bₙ}ₙ₌₀^∞
         * c = {cₙ}ₙ₌₀^∞
         *
         * @return cₙ = ∑ₖ₌₀ⁿ aₖ * bₙ₋ₖ
         */
        public static double[] convolution(double[] sequence1, double[] sequence2) {
            Objects.requireNonNull(sequence1);
            Objects.requireNonNull(sequence2);

            final int n = sequence1.length;
            final int m = sequence2.length;
            final int size = n + m - 1;
            final double[] convolvedSequence = new double[size];
            for (int i = 0; i < size; i++) {
                convolvedSequence[i] = 0;
                for (int k = 0; k < n; k++) {
                    final int j = i - k;
                    if (j >= 0 && j < m) {
                        convolvedSequence[i] += sequence1[k] * sequence2[j];
                    }
                }
            }
            return convolvedSequence;
        }

        /**
         * @param rows the first row index 0 is not included. For the size of rows = 10 -> rows = 10 + 1 = 11.
         * @return C(n, k) = C(n-1, k-1) + C(n-1, k)
         */
        public static int[][] pascalTriangle(int rows) {
            checkGreater0(rows);

            final int totalRows = rows + 1;
            final int[][] matrix = new int[totalRows][];
            for (int i = 0; i < totalRows; i++) {
                matrix[i] = new int[i + 1];
                matrix[i][0] = 1;
                matrix[i][i] = 1;
                for (int j = 1; j < i; j++) {
                    matrix[i][j] = matrix[i - 1][j - 1] + matrix[i - 1][j];
                }
            }
            return matrix;
        }

        /**
         * @return 2ⁿ
         */
        public static long pascalTriangleRowSum(int rowNumber) {
            return (long) Math.pow(2, rowNumber);
        }

        /**
         * @return Hₙ = 1/1 + 1/2 + 1/3 + ⋯ + 1/n = ∑ₖ₌₁ⁿ 1/k
         */
        public static double harmonicNumber(double end) {
            checkGreater0(end);

            double sum = 0;
            for (int k = 1; k <= end; k++) {
                sum += 1. / k;
            }
            return sum;
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
         * f(x) = xⁿ
         *
         * @return f'(x) = n * xⁿ⁻¹
         */
        public static double derivativePowerRule(double x, double exponent) {
            return exponent * Math.pow(x, exponent - 1);
        }

        /**
         * f(x) = eˣ
         *
         * @return f'(x) = eˣ
         */
        public static double derivativeExponentialRule(double x) {
            return Math.exp(x);
        }

        /**
         * f(x) = ln(x)
         *
         * @return f'(x) = 1/x
         */
        public static double derivativeLogarithmicRule(double x) {
            return 1.0 / x;
        }

        /**
         * f(x) = u(x) * v(x)
         *
         * @return f'(x) = u'(x) * v(x) + u(x) * v'(x)
         */
        public static double derivativeProductRule(
            double x,
            DoubleUnaryOperator u,
            DoubleUnaryOperator uPrime,
            DoubleUnaryOperator v,
            DoubleUnaryOperator vPrime
        ) {
            return uPrime.applyAsDouble(x) * v.applyAsDouble(x) + u.applyAsDouble(x) * vPrime.applyAsDouble(x);
        }

        /**
         * f(x) = g(h(x))
         *
         * @return f'(x) = g'(h(x)) * h'(x)
         */
        public static double derivativeChainRule(
            double x,
            DoubleUnaryOperator h,
            DoubleUnaryOperator hPrime,
            DoubleUnaryOperator gPrime
        ) {
            return gPrime.applyAsDouble(h.applyAsDouble(x)) * hPrime.applyAsDouble(x);
        }

        /**
         * f(x) = u(x) / v(x)
         *
         * @return f'(x) = (u'(x) * v(x) - u(x) * v'(x)) / (v(x)²)
         */
        public static double derivativeQuotientRule(
            double x,
            DoubleUnaryOperator u,
            DoubleUnaryOperator uPrime,
            DoubleUnaryOperator v,
            DoubleUnaryOperator vPrime
        ) {
            final double numerator = uPrime.applyAsDouble(x) * v.applyAsDouble(x)
                - u.applyAsDouble(x) * vPrime.applyAsDouble(x);
            final double denominator = Math.pow(v.applyAsDouble(x), 2);
            return numerator / denominator;
        }

        /**
         * y = mx + b
         *
         * @return ∫(ax + b)dx = (a/2)x² + bx + C
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

        /**
         * Numerically integrates f(x) from a to b using the Trapezoidal Rule.
         * @param numberOfIntervals (higher = more accurate).
         * @return Approximate value of the definite integral.
         */
        public static double integrateTrapezoidal(
            DoubleUnaryOperator f, double lowerBound, double upperBound, int numberOfIntervals) {
            final double h = (upperBound - lowerBound) / numberOfIntervals;
            double sum = ONE_HALF * (f.applyAsDouble(lowerBound) + f.applyAsDouble(upperBound));
            for (int i = 1; i < numberOfIntervals; i++) {
                sum += f.applyAsDouble(lowerBound + i * h);
            }
            return sum * h;
        }

        /**
         * Numerically integrates f(x) from a to b using Simpson's Rule.
         * @return Approximate value of the definite integral.
         */
        public static double integrateSimpson(
            DoubleUnaryOperator f, double lowerBound, double upperBound, int numberOfIntervals) {
            if (numberOfIntervals % 2 != 0) {
                throw new IllegalArgumentException("numberOfIntervals must be even");
            }
            final double h = (upperBound - lowerBound) / numberOfIntervals;
            double sum = f.applyAsDouble(lowerBound) + f.applyAsDouble(upperBound);
            for (int i = 1; i < numberOfIntervals; i += 2) {
                sum += 4 * f.applyAsDouble(lowerBound + i * h);
            }
            for (int i = 2; i < numberOfIntervals; i += 2) {
                sum += 2 * f.applyAsDouble(lowerBound + i * h);
            }
            return sum * h / 3.0;
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
