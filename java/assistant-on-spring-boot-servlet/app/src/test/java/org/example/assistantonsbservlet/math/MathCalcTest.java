package org.example.assistantonsbservlet.math;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math.complex.Complex;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MathCalcTest {
    private static final double DELTA1 = 0.1;
    private static final double DELTA2 = 0.01;
    private static final double DELTA3 = 0.001;
    private static final double DELTA4 = 0.0001;
    private static final double DELTA5 = 0.00001;
    private static final double DELTA6 = 0.000001;
    private static final double DELTA8 = 0.00000001;
    private static final double DELTA9 = 0.000000001;

    @Nested
    class Arithmetic {
        @ParameterizedTest
        @CsvSource({
            "2,true",
            "3,true",
            "5,true",
            "7,true",
            "11,true",
            "13,true",
            "17,true",
            "19,true",
            "23,true",
            "29,true",
            "31,true",
            "37,true",
            "41,true",
            "43,true",
            "47,true",
            "53,true",
            "59,true",
            "61,true",
            "67,true",
            "71,true",
            "73,true",
            "79,true",
            "83,true",
            "89,true",
            "97,true",
        })
        void testIsPrime(double number, boolean expectedResult) {
            // when
            final boolean prime = MathCalc.Arithmetic.isPrime(number);
            // then
            assertEquals(expectedResult, prime);
        }

        @ParameterizedTest
        @CsvSource({
            "5,9,true",
            "6,11,true",
            "18,30,false",
            "18,35,true",
        })
        void testIsCoprime(long x, long x1, boolean expectedResult) {
            // when
            final boolean coprime = MathCalc.Arithmetic.isCoprime(x, x1);
            // then
            assertEquals(expectedResult, coprime);
        }

        @ParameterizedTest
        @CsvSource({
            "2,false",
            "3,false",
            "5,false",
            "6,true",
            "7,false",
            "8,true",
            "9,true",
            "10,true",
        })
        void testIsCompositeNumber(long number, boolean expectedResult) {
            // when
            final boolean compositeNumber = MathCalc.Arithmetic.isCompositeNumber(number);
            // then
            assertEquals(expectedResult, compositeNumber);
        }

        static List<Arguments> primeFactorizationArgs() {
            return List.of(
                Arguments.of(24, new long[]{2, 2, 2, 3}),
                Arguments.of(80, new long[]{2, 2, 2, 2, 5}),
                Arguments.of(121, new long[]{11, 11}),
                Arguments.of(121.0, new long[]{11, 11})
            );
        }

        @ParameterizedTest
        @MethodSource("primeFactorizationArgs")
        void testPrimeFactorization(double number, long[] expectedResult) {
            // when
            final long[] primes = MathCalc.Arithmetic.primeFactorization(number);
            // then
            assertArrayEquals(expectedResult, primes);
        }

        static List<Arguments> lcmArgs() {
            return List.of(
                Arguments.of(new double[]{18, 24}, 72),
                Arguments.of(new double[]{2, 4, 6, 8, 10, 12}, 120)
            );
        }

        @ParameterizedTest
        @MethodSource("lcmArgs")
        void testLcmWithPrimeFactorization(double[] numbers, long expectedResult) {
            // when
            final long lcm = MathCalc.Arithmetic.lcmWithPrimeFactorization(numbers);
            // then
            assertEquals(expectedResult, lcm);
        }

        @ParameterizedTest
        @MethodSource("lcmArgs")
        void testLcmWithGcf(double[] numbers, long expectedResult) {
            // when
            final long lcm = MathCalc.Arithmetic.lcmWithGcf(numbers);
            // then
            assertEquals(expectedResult, lcm);
        }

        @ParameterizedTest
        @CsvSource({
            "2,3,4,5,4"
        })
        void testLcmOverGcfOfFractions(long numerator1, long denominator1,
                                       long numerator2, long denominator2, long expectedResult) {
            // given
            final long[] fraction1 = new long[]{numerator1, denominator1};
            final long[] fraction2 = new long[]{numerator2, denominator2};
            // when
            final long lcm = MathCalc.Arithmetic.lcmOverGcfOfFractions(fraction1, fraction2);
            // then
            assertEquals(expectedResult, lcm);
        }

        static List<Arguments> gcfArgs() {
            return List.of(
                Arguments.of(new double[]{72, 40}, 8),
                Arguments.of(new double[]{72, 40, 32}, 8),
                Arguments.of(new double[]{33_264, 35_640}, 2376)
            );
        }

        @ParameterizedTest
        @MethodSource("gcfArgs")
        void testGcfWithEuclideanAlg(double[] numbers, long expectedResult) {
            // when
            final long gcf = MathCalc.Arithmetic.gcfWithEuclideanAlg(numbers);
            // then
            assertEquals(expectedResult, gcf);
        }

        @ParameterizedTest
        @MethodSource("gcfArgs")
        void testGcfWithBinaryAlg(double[] numbers, long expectedResult) {
            // when
            final long gcf = MathCalc.Arithmetic.gcfWithBinaryAlg(numbers);
            // then
            assertEquals(expectedResult, gcf);
        }

        @ParameterizedTest
        @MethodSource("gcfArgs")
        void testGcfWithCommonFactors(double[] numbers, long expectedResult) {
            // when
            final long gcf = MathCalc.Arithmetic.gcfWithCommonFactors(numbers);
            // then
            assertEquals(expectedResult, gcf);
        }

        @ParameterizedTest
        @MethodSource("gcfArgs")
        void testGcfWithLcm(double[] numbers, long expectedResult) {
            // when
            final long gcf = MathCalc.Arithmetic.gcfWithLcm(numbers);
            // then
            assertEquals(expectedResult, gcf);
        }

        static List<Arguments> factorArgs() {
            return List.of(
                Arguments.of(1, new long[]{1}),
                Arguments.of(2, new long[]{1, 2}),
                Arguments.of(3, new long[]{1, 3}),
                Arguments.of(4, new long[]{1, 2, 4}),
                Arguments.of(5, new long[]{1, 5}),
                Arguments.of(6, new long[]{1, 2, 3, 6}),
                Arguments.of(7, new long[]{1, 7}),
                Arguments.of(8, new long[]{1, 2, 4, 8}),
                Arguments.of(9, new long[]{1, 3, 9}),
                Arguments.of(10, new long[]{1, 2, 5, 10}),
                Arguments.of(11, new long[]{1, 11}),
                Arguments.of(12, new long[]{1, 2, 3, 4, 6, 12}),
                Arguments.of(13, new long[]{1, 13}),
                Arguments.of(14, new long[]{1, 2, 7, 14}),
                Arguments.of(15, new long[]{1, 3, 5, 15}),
                Arguments.of(16, new long[]{1, 2, 4, 8, 16}),
                Arguments.of(17, new long[]{1, 17}),
                Arguments.of(18, new long[]{1, 2, 3, 6, 9, 18}),
                Arguments.of(19, new long[]{1, 19}),
                Arguments.of(20, new long[]{1, 2, 4, 5, 10, 20}),
                Arguments.of(21, new long[]{1, 3, 7, 21}),
                Arguments.of(22, new long[]{1, 2, 11, 22}),
                Arguments.of(23, new long[]{1, 23}),
                Arguments.of(24, new long[]{1, 2, 3, 4, 6, 8, 12, 24}),
                Arguments.of(25, new long[]{1, 5, 25}),
                Arguments.of(26, new long[]{1, 2, 13, 26}),
                Arguments.of(27, new long[]{1, 3, 9, 27}),
                Arguments.of(28, new long[]{1, 2, 4, 7, 14, 28}),
                Arguments.of(29, new long[]{1, 29}),
                Arguments.of(30, new long[]{1, 2, 3, 5, 6, 10, 15, 30}),
                Arguments.of(31, new long[]{1, 31}),
                Arguments.of(32, new long[]{1, 2, 4, 8, 16, 32}),
                Arguments.of(33, new long[]{1, 3, 11, 33}),
                Arguments.of(34, new long[]{1, 2, 17, 34}),
                Arguments.of(35, new long[]{1, 5, 7, 35}),
                Arguments.of(36, new long[]{1, 2, 3, 4, 6, 9, 12, 18, 36}),
                Arguments.of(37, new long[]{1, 37}),
                Arguments.of(39, new long[]{1, 3, 13, 39}),
                Arguments.of(40, new long[]{1, 2, 4, 5, 8, 10, 20, 40}),
                Arguments.of(41, new long[]{1, 41}),
                Arguments.of(42, new long[]{1, 2, 3, 6, 7, 14, 21, 42}),
                Arguments.of(43, new long[]{1, 43}),
                Arguments.of(44, new long[]{1, 2, 4, 11, 22, 44}),
                Arguments.of(45, new long[]{1, 3, 5, 9, 15, 45}),
                Arguments.of(46, new long[]{1, 2, 23, 46}),
                Arguments.of(47, new long[]{1, 47}),
                Arguments.of(48, new long[]{1, 2, 3, 4, 6, 8, 12, 16, 24, 48}),
                Arguments.of(49, new long[]{1, 7, 49}),
                Arguments.of(50, new long[]{1, 2, 5, 10, 25, 50}),
                Arguments.of(51, new long[]{1, 3, 17, 51}),
                Arguments.of(52, new long[]{1, 2, 4, 13, 26, 52}),
                Arguments.of(53, new long[]{1, 53}),
                Arguments.of(54, new long[]{1, 2, 3, 6, 9, 18, 27, 54}),
                Arguments.of(55, new long[]{1, 5, 11, 55}),
                Arguments.of(56, new long[]{1, 2, 4, 7, 8, 14, 28, 56}),
                Arguments.of(57, new long[]{1, 3, 19, 57}),
                Arguments.of(58, new long[]{1, 2, 29, 58}),
                Arguments.of(59, new long[]{1, 59}),
                Arguments.of(60, new long[]{1, 2, 3, 4, 5, 6, 10, 12, 15, 20, 30, 60}),
                Arguments.of(61, new long[]{1, 61}),
                Arguments.of(62, new long[]{1, 2, 31, 62}),
                Arguments.of(63, new long[]{1, 3, 7, 9, 21, 63}),
                Arguments.of(64, new long[]{1, 2, 4, 8, 16, 32, 64}),
                Arguments.of(65, new long[]{1, 5, 13, 65}),
                Arguments.of(66, new long[]{1, 2, 3, 6, 11, 22, 33, 66}),
                Arguments.of(67, new long[]{1, 67}),
                Arguments.of(68, new long[]{1, 2, 4, 17, 34, 68}),
                Arguments.of(69, new long[]{1, 3, 23, 69}),
                Arguments.of(70, new long[]{1, 2, 5, 7, 10, 14, 35, 70}),
                Arguments.of(71, new long[]{1, 71}),
                Arguments.of(72, new long[]{1, 2, 3, 4, 6, 8, 9, 12, 18, 24, 36, 72}),
                Arguments.of(73, new long[]{1, 73}),
                Arguments.of(74, new long[]{1, 2, 37, 74}),
                Arguments.of(75, new long[]{1, 3, 5, 15, 25, 75}),
                Arguments.of(76, new long[]{1, 2, 4, 19, 38, 76}),
                Arguments.of(77, new long[]{1, 7, 11, 77}),
                Arguments.of(78, new long[]{1, 2, 3, 6, 13, 26, 39, 78}),
                Arguments.of(79, new long[]{1, 79}),
                Arguments.of(80, new long[]{1, 2, 4, 5, 8, 10, 16, 20, 40, 80}),
                Arguments.of(81, new long[]{1, 3, 9, 27, 81}),
                Arguments.of(82, new long[]{1, 2, 41, 82}),
                Arguments.of(83, new long[]{1, 83}),
                Arguments.of(84, new long[]{1, 2, 3, 4, 6, 7, 12, 14, 21, 28, 42, 84}),
                Arguments.of(85, new long[]{1, 5, 17, 85}),
                Arguments.of(86, new long[]{1, 2, 43, 86}),
                Arguments.of(87, new long[]{1, 3, 29, 87}),
                Arguments.of(88, new long[]{1, 2, 4, 8, 11, 22, 44, 88}),
                Arguments.of(89, new long[]{1, 89}),
                Arguments.of(90, new long[]{1, 2, 3, 5, 6, 9, 10, 15, 18, 30, 45, 90}),
                Arguments.of(91, new long[]{1, 7, 13, 91}),
                Arguments.of(92, new long[]{1, 2, 4, 23, 46, 92}),
                Arguments.of(93, new long[]{1, 3, 31, 93}),
                Arguments.of(94, new long[]{1, 2, 47, 94}),
                Arguments.of(95, new long[]{1, 5, 19, 95}),
                Arguments.of(96, new long[]{1, 2, 3, 4, 6, 8, 12, 16, 24, 32, 48, 96}),
                Arguments.of(97, new long[]{1, 97}),
                Arguments.of(98, new long[]{1, 2, 7, 14, 49, 98}),
                Arguments.of(99, new long[]{1, 3, 9, 11, 33, 99}),
                Arguments.of(100, new long[]{1, 2, 4, 5, 10, 20, 25, 50, 100}),
                Arguments.of(104, new long[]{1, 2, 4, 8, 13, 26, 52, 104}),
                Arguments.of(105, new long[]{1, 3, 5, 7, 15, 21, 35, 105}),
                Arguments.of(108, new long[]{1, 2, 3, 4, 6, 9, 12, 18, 27, 36, 54, 108}),
                Arguments.of(110, new long[]{1, 2, 5, 10, 11, 22, 55, 110}),
                Arguments.of(112, new long[]{1, 2, 4, 7, 8, 14, 16, 28, 56, 112}),
                Arguments.of(117, new long[]{1, 3, 9, 13, 39, 117}),
                Arguments.of(120, new long[]{1, 2, 3, 4, 5, 6, 8, 10, 12, 15, 20, 24, 30, 40, 60, 120}),
                Arguments.of(121, new long[]{1, 11, 121}),
                Arguments.of(125, new long[]{1, 5, 25, 125}),
                Arguments.of(126, new long[]{1, 2, 3, 6, 7, 9, 14, 18, 21, 42, 63, 126}),
                Arguments.of(130, new long[]{1, 2, 5, 10, 13, 26, 65, 130}),
                Arguments.of(132, new long[]{1, 2, 3, 4, 6, 11, 12, 22, 33, 44, 66, 132}),
                Arguments.of(135, new long[]{1, 3, 5, 9, 15, 27, 45, 135}),
                Arguments.of(140, new long[]{1, 2, 4, 5, 7, 10, 14, 20, 28, 35, 70, 140}),
                Arguments.of(144, new long[]{1, 2, 3, 4, 6, 8, 9, 12, 16, 18, 24, 36, 48, 72, 144}),
                Arguments.of(147, new long[]{1, 3, 7, 21, 49, 147}),
                Arguments.of(150, new long[]{1, 2, 3, 5, 6, 10, 15, 25, 30, 50, 75, 150}),
                Arguments.of(162, new long[]{1, 2, 3, 6, 9, 18, 27, 54, 81, 162}),
                Arguments.of(169, new long[]{1, 13, 169}),
                Arguments.of(175, new long[]{1, 5, 7, 25, 35, 175}),
                Arguments.of(180, new long[]{1, 2, 3, 4, 5, 6, 9, 10, 12, 15, 18, 20, 30, 36, 45, 60, 90, 180}),
                Arguments.of(189, new long[]{1, 3, 7, 9, 21, 27, 63, 189}),
                Arguments.of(192, new long[]{1, 2, 3, 4, 6, 8, 12, 16, 24, 32, 48, 64, 96, 192}),
                Arguments.of(196, new long[]{1, 2, 4, 7, 14, 28, 49, 98, 196}),
                Arguments.of(200, new long[]{1, 2, 4, 5, 8, 10, 20, 25, 40, 50, 100, 200}),
                Arguments.of(210, new long[]{1, 2, 3, 5, 6, 7, 10, 14, 15, 21, 30, 35, 42, 70, 105, 210}),
                Arguments.of(216, new long[]{1, 2, 3, 4, 6, 8, 9, 12, 18, 24, 27, 36, 54, 72, 108, 216}),
                Arguments.of(225, new long[]{1, 3, 5, 9, 15, 25, 45, 75, 225}),
                Arguments.of(240, new long[]{1, 2, 3, 4, 5, 6, 8, 10, 12, 15, 16, 20, 24, 30, 40, 48, 60,
                    80, 120, 240}),
                Arguments.of(245, new long[]{1, 5, 7, 35, 49, 245}),
                Arguments.of(250, new long[]{1, 2, 5, 10, 25, 50, 125, 250}),
                Arguments.of(256, new long[]{1, 2, 4, 8, 16, 32, 64, 128, 256}),
                Arguments.of(270, new long[]{1, 2, 3, 5, 6, 9, 10, 15, 18, 27, 30, 45, 54, 90, 135, 270}),
                Arguments.of(288, new long[]{1, 2, 3, 4, 6, 8, 9, 12, 16, 18, 24, 32, 36, 48, 72, 96, 144, 288}),
                Arguments.of(294, new long[]{1, 2, 3, 6, 7, 14, 21, 42, 49, 98, 147, 294}),
                Arguments.of(300, new long[]{1, 2, 3, 4, 5, 6, 10, 12, 15, 20, 25, 30, 50, 60, 75, 100, 150, 300}),
                Arguments.of(343, new long[]{1, 7, 49, 343}),
                Arguments.of(360, new long[]{1, 2, 3, 4, 5, 6, 8, 9, 10, 12, 15, 18, 20, 24, 30, 36, 40,
                    45, 60, 72, 90, 120, 180, 360}),
                Arguments.of(375, new long[]{1, 3, 5, 15, 25, 75, 125, 375}),
                Arguments.of(400, new long[]{1, 2, 4, 5, 8, 10, 16, 20, 25, 40, 50, 80, 100, 200, 400}),
                Arguments.of(500, new long[]{1, 2, 4, 5, 10, 20, 25, 50, 100, 125, 250, 500}),
                Arguments.of(625, new long[]{1, 5, 25, 125, 625})
            );
        }

        @ParameterizedTest
        @MethodSource("factorArgs")
        void testFactor(double number, long[] expectedResult) {
            // when
            final long[] factors = MathCalc.Arithmetic.factor(number);
            // then
            assertArrayEquals(expectedResult, factors);
        }

        static List<Arguments> commonFactorsArgs() {
            return List.of(
                Arguments.of(2.0, 4.0, new long[]{1, 2}),
                Arguments.of(2, 4, new long[]{1, 2}),
                Arguments.of(3, 9, new long[]{1, 3}),
                Arguments.of(4, 6, new long[]{1, 2}),
                Arguments.of(4, 8, new long[]{1, 2, 4}),
                Arguments.of(5, 10, new long[]{1, 5}),
                Arguments.of(6, 12, new long[]{1, 2, 3, 6}),
                Arguments.of(7, 9, new long[]{1})
            );
        }

        @ParameterizedTest
        @MethodSource("commonFactorsArgs")
        void testCommonFactors(double number1, double number2, long[] expectedResult) {
            // when
            final long[] factors = MathCalc.Arithmetic.commonFactors(number1, number2);
            // then
            assertArrayEquals(expectedResult, factors);
        }

        @ParameterizedTest
        @CsvSource({
            "1,false",
            "2,true",
            "2.0,true",
            "2.1,false",
            "3,false",
            "4,true",
            "5,false",
            "6,true",
        })
        void testIsDivisibleBy2(double number, boolean expectedResult) {
            // when
            final boolean divisible = MathCalc.Arithmetic.isDivisibleBy2(number);
            // then
            assertEquals(expectedResult, divisible);
        }

        @ParameterizedTest
        @CsvSource({
            "1,false",
            "2,false",
            "3,true",
            "3.0,true",
            "3.1,false",
            "4,false",
            "5,false",
            "6,true",
            "9,true",
        })
        void testIsDivisibleBy3(double number, boolean expectedResult) {
            // when
            final boolean divisible = MathCalc.Arithmetic.isDivisibleBy3(number);
            // then
            assertEquals(expectedResult, divisible);
        }

        @ParameterizedTest
        @CsvSource({
            "1,false",
            "2,false",
            "3,false",
            "4,true",
            "4.0,true",
            "4.1,false",
            "5,false",
            "6,false",
            "8,true",
        })
        void testIsDivisibleBy4(double number, boolean expectedResult) {
            // when
            final boolean divisible = MathCalc.Arithmetic.isDivisibleBy4(number);
            // then
            assertEquals(expectedResult, divisible);
        }

        @ParameterizedTest
        @CsvSource({
            "1,false",
            "2,false",
            "3,false",
            "4,false",
            "5,true",
            "5.0,true",
            "5.1,false",
            "6,false",
            "10,true",
        })
        void testIsDivisibleBy5(double number, boolean expectedResult) {
            // when
            final boolean divisible = MathCalc.Arithmetic.isDivisibleBy5(number);
            // then
            assertEquals(expectedResult, divisible);
        }

        @ParameterizedTest
        @CsvSource({
            "1,false",
            "2,false",
            "3,false",
            "4,false",
            "5,false",
            "6,true",
            "6.0,true",
            "6.1,false",
            "7,false",
            "12,true",
        })
        void testIsDivisibleBy6(double number, boolean expectedResult) {
            // when
            final boolean divisible = MathCalc.Arithmetic.isDivisibleBy6(number);
            // then
            assertEquals(expectedResult, divisible);
        }

        static List<Arguments> isDivisibleBy7Args() {
            return List.of(
                Arguments.of(1, false),
                Arguments.of(2, false),
                Arguments.of(3, false),
                Arguments.of(4, false),
                Arguments.of(5, false),
                Arguments.of(6, false),
                Arguments.of(7, true),
                Arguments.of(7.0, true),
                Arguments.of(7.1, false),
                Arguments.of(9, false),
                Arguments.of(49, true),
                Arguments.of(77, true),
                Arguments.of(777, true)
            );
        }

        @ParameterizedTest
        @MethodSource("isDivisibleBy7Args")
        void testIsDivisibleBy7(double number, boolean expectedResult) {
            // when
            final boolean divisible = MathCalc.Arithmetic.isDivisibleBy7(number);
            // then
            assertEquals(expectedResult, divisible);
        }

        @ParameterizedTest
        @MethodSource("isDivisibleBy7Args")
        void testIsDivisibleBy7ViaSubtractTwiceLastDigit(double number, boolean expectedResult) {
            // when
            final boolean divisible = MathCalc.Arithmetic.isDivisibleBy7ViaSubtractTwiceLastDigit(number);
            // then
            assertEquals(expectedResult, divisible);
        }

        @ParameterizedTest
        @MethodSource("isDivisibleBy7Args")
        void testIsDivisibleBy7ViaReverseOrder(double number, boolean expectedResult) {
            // when
            final boolean divisible = MathCalc.Arithmetic.isDivisibleBy7ViaReverseOrder(number);
            // then
            assertEquals(expectedResult, divisible);
        }

        @ParameterizedTest
        @CsvSource({
            "1,false",
            "2,false",
            "3,false",
            "4,false",
            "5,false",
            "6,false",
            "8,true",
            "8.0,true",
            "8.1,false",
            "9,false",
            "16,true",
            "88,true",
            "888,true",
        })
        void testIsDivisibleBy8(double number, boolean expectedResult) {
            // when
            final boolean divisible = MathCalc.Arithmetic.isDivisibleBy8(number);
            // then
            assertEquals(expectedResult, divisible);
        }

        @ParameterizedTest
        @CsvSource({
            "1,false",
            "2,false",
            "3,false",
            "4,false",
            "5,false",
            "6,false",
            "9,true",
            "9.0,true",
            "9.1,false",
            "18,true",
            "81,true",
            "999,true",
        })
        void testIsDivisibleBy9(double number, boolean expectedResult) {
            // when
            final boolean divisible = MathCalc.Arithmetic.isDivisibleBy9(number);
            // then
            assertEquals(expectedResult, divisible);
        }

        @ParameterizedTest
        @CsvSource({
            "1,false",
            "2,false",
            "3,false",
            "4,false",
            "5,false",
            "6,false",
            "10,true",
            "10.0,true",
            "10.1,false",
            "11,false",
            "20,true",
            "100,true",
        })
        void testIsDivisibleBy10(double number, boolean expectedResult) {
            // when
            final boolean divisible = MathCalc.Arithmetic.isDivisibleBy10(number);
            // then
            assertEquals(expectedResult, divisible);
        }

        static List<Arguments> solveProportionArgs() {
            return List.of(
                Arguments.of(new double[]{7, 12}, new double[]{Double.POSITIVE_INFINITY, 96},
                    new double[]{56, 96}), // 7/12 = x/96
                Arguments.of(new double[]{7, 12}, new double[]{56, Double.POSITIVE_INFINITY},
                    new double[]{56, 96}), // 7/12 = 56/x
                Arguments.of(new double[]{56, 96}, new double[]{7, Double.POSITIVE_INFINITY},
                    new double[]{7, 12}), // 56/96 = 7/x
                Arguments.of(new double[]{56, 96}, new double[]{Double.POSITIVE_INFINITY, 12},
                    new double[]{7, 12}) // 56/96 = x/12
            );
        }

        @ParameterizedTest
        @MethodSource("solveProportionArgs")
        void testSolveProportion(double[] proportion, double[] proportionWithUnknown, double[] expectedResult) {
            // when
            final double[] result = MathCalc.Arithmetic.solveProportion(proportion, proportionWithUnknown);
            // then
            assertArrayEquals(expectedResult, result, DELTA1);
        }

        @Test
        void testFindEquivalentRatio() {
            // given
            // 3/8 = x/27
            final double[] ratio = new double[]{3, 8};
            final double[] ratioWithUnknown = new double[]{Double.POSITIVE_INFINITY, 72};
            // when
            final double[] result = MathCalc.Arithmetic.findEquivalentRatio(ratio, ratioWithUnknown);
            // then
            assertArrayEquals(new double[]{27, 72}, result, DELTA1);
        }

        @Test
        void testScaleUpRatio() {
            // given
            final double[] ratio = new double[]{3, 8};
            final byte coefficient = 2;
            // when
            final double[] result = MathCalc.Arithmetic.scaleUpRatio(ratio, coefficient);
            // then
            assertArrayEquals(new double[]{6, 16}, result, DELTA1);
        }

        @Test
        void testScaleDownRatio() {
            // given
            final double[] ratio = new double[]{27, 72};
            final byte coefficient = 4;
            // when
            final double[] result = MathCalc.Arithmetic.scaleDownRatio(ratio, coefficient);
            // then
            assertArrayEquals(new double[]{6.75, 18}, result, DELTA2);
        }

        @Test
        void testSimplifyRatio() {
            // given
            final double[] ratio = new double[]{27, 72};
            // when
            final double[] result = MathCalc.Arithmetic.simplifyRatio(ratio);
            // then
            assertArrayEquals(new double[]{3, 8}, result, DELTA2);
        }

        @Test
        void testSimplifyRatio1toN() {
            // given
            final double[] ratio = new double[]{27, 72};
            // when
            final double[] result = MathCalc.Arithmetic.simplifyRatio1toN(ratio);
            // then
            assertArrayEquals(new double[]{1, 2.6667}, result, DELTA4);
        }

        @Test
        void testSimplifyRatioNto1() {
            // given
            final double[] ratio = new double[]{27, 72};
            // when
            final double[] result = MathCalc.Arithmetic.simplifyRatioNto1(ratio);
            // then
            assertArrayEquals(new double[]{0.375, 1}, result, DELTA3);
        }

        @Test
        void testGoldenRatioGivenLongerSection() {
            // given
            final double longerSection = 0.850651;
            // when
            final double[] result = MathCalc.Arithmetic.goldenRatioGivenLongerSection(longerSection);
            // then
            assertArrayEquals(new double[]{longerSection, 0.526, 1.376}, result, DELTA3);
        }

        @Test
        void testGoldenRatioGivenShorterSection() {
            // given
            final double shorterSection = 0.526;
            // when
            final double[] result = MathCalc.Arithmetic.goldenRatioGivenShorterSection(shorterSection);
            // then
            assertArrayEquals(new double[]{0.851, shorterSection, 1.377}, result, DELTA3);
        }

        @Test
        void testGoldenRatioGivenWhole() {
            // given
            final double whole = 1.377;
            // when
            final double[] result = MathCalc.Arithmetic.goldenRatioGivenWhole(whole);
            // then
            assertArrayEquals(new double[]{0.851, 0.526, whole}, result, DELTA3);
        }

        static List<Arguments> addFractionsArgs() {
            return List.of(
                Arguments.of(new long[]{3, 5}, new long[]{1, 5}, new long[]{4, 5}), // 3/5 + 1/5 = 4/5
                Arguments.of(new long[]{2, 5}, new long[]{3, 10}, new long[]{7, 10}), // 2/5 + 3/10 = 7/10
                // 1/2 + 3/5 = 11/10 = 1 1/10
                Arguments.of(new long[]{1, 2}, new long[]{3, 5}, new long[]{1, 1, 10}),
                // 2 3/5 + 1 1/2 = 13/5 + 3/2 = 41/10 = 4 1/10
                Arguments.of(new long[]{2, 3, 5}, new long[]{1, 1, 2}, new long[]{4, 1, 10})
            );
        }

        @ParameterizedTest
        @MethodSource("addFractionsArgs")
        void testAddFractions(long[] fraction1, long[] fraction2, long[] expectedResult) {
            // when
            final long[] result = MathCalc.Arithmetic.addFractions(fraction1, fraction2);
            // then
            assertArrayEquals(expectedResult, result);
        }

        static List<Arguments> subtractFractionsArgs() {
            return List.of(
                Arguments.of(new long[]{3, 5}, new long[]{1, 5}, new long[]{2, 5}), // 3/5 - 1/5 = 2/5
                // 2/5 - 3/10 = 4/10 - 3/10 = 1/10
                Arguments.of(new long[]{2, 5}, new long[]{3, 10}, new long[]{1, 10}),
                // 2 3/5 − 1 1/2 = 13/5 − 3/2 = 11/10 = 1 1/10
                Arguments.of(new long[]{2, 3, 5}, new long[]{1, 1, 2}, new long[]{1, 1, 10})
            );
        }

        @ParameterizedTest
        @MethodSource("subtractFractionsArgs")
        void testSubtractFractions(long[] fraction1, long[] fraction2, long[] expectedResult) {
            // when
            final long[] result = MathCalc.Arithmetic.subtractFractions(fraction1, fraction2);
            // then
            assertArrayEquals(expectedResult, result);
        }

        static List<Arguments> multiplyFractionsArgs() {
            return List.of(
                Arguments.of(new long[]{2, 3}, new long[]{5, 6}, new long[]{5, 9}), // 2/3 ⋅ 5/6 = 10/18 = 5/9
                Arguments.of(new long[]{3, 1}, new long[]{5, 7}, new long[]{2, 1, 7}), // 3/1 ⋅ 5/7 = 15/7 = 2 1/7
                // 2 1/2 ⋅ 3 1/4 = 5/2 ⋅ 13/4 = 65/8 = 8 1/8
                Arguments.of(new long[]{2, 1, 2}, new long[]{3, 1, 4}, new long[]{8, 1, 8})
            );
        }

        @ParameterizedTest
        @MethodSource("multiplyFractionsArgs")
        void testMultiplyFractions(long[] fraction1, long[] fraction2, long[] expectedResult) {
            // when
            final long[] result = MathCalc.Arithmetic.multiplyFractions(fraction1, fraction2);
            // then
            assertArrayEquals(expectedResult, result);
        }

        static List<Arguments> divideFractionsArgs() {
            return List.of(
                Arguments.of(new long[]{1, 2}, new long[]{3, 5}, new long[]{5, 6}), // 1/2 ÷ 3/5 = 1/2 ⋅ 5/3 = 5/6
                // 2 1/2 ÷ 3 1/4 = 5/2 ÷ 13/4 = 5/2 ⋅ 4/13 = 10/13
                Arguments.of(new long[]{2, 1, 2}, new long[]{3, 1, 4}, new long[]{10, 13})
            );
        }

        @ParameterizedTest
        @MethodSource("divideFractionsArgs")
        void testDivideFractions(long[] fraction1, long[] fraction2, long[] expectedResult) {
            // when
            final long[] result = MathCalc.Arithmetic.divideFractions(fraction1, fraction2);
            // then
            assertArrayEquals(expectedResult, result);
        }

        @Test
        void testSimplifyFraction() {
            // given
            final long[] fraction = new long[]{42, 126}; // 42/126 = 1/3
            // when
            final long[] result = MathCalc.Arithmetic.simplifyFraction(fraction);
            // then
            assertArrayEquals(new long[]{1, 3}, result);
        }

        static List<Arguments> decimalToFractionArgs() {
            return List.of(
                Arguments.of(0.32, new long[]{8, 25}),
                Arguments.of(0.3333, new long[]{3333, 10000})
            );
        }

        @ParameterizedTest
        @MethodSource("decimalToFractionArgs")
        void testDecimalToFraction(double decimal, long[] expectedResult) {
            // when
            final long[] fraction = MathCalc.Arithmetic.decimalToFraction(decimal);
            // then
            assertArrayEquals(expectedResult, fraction);
        }

        static List<Arguments> fractionToDecimalArgs() {
            return List.of(
                Arguments.of(new long[]{2, 1, 2}, 2.5),
                Arguments.of(new long[]{3, 1, 4}, 3.25)
            );
        }

        @ParameterizedTest
        @MethodSource("fractionToDecimalArgs")
        void testFractionToDecimal(long[] fraction, double expectedResult) {
            // when
            final double decimal = MathCalc.Arithmetic.fractionToDecimal(fraction);
            // then
            assertEquals(expectedResult, decimal, DELTA3);
        }

        @Test
        void testAverage() {
            // given
            final double[] dataset = new double[]{24., 55., 17., 87., 100.};
            // when
            final double average = MathCalc.Arithmetic.average(dataset);
            // then
            assertEquals(56.6, average, DELTA1);
        }

        @Test
        void testWeightedAverage() {
            // given
            final double[][] dataset = new double[][]{
                {0.25, 75.}, {0.25, 90.}, {0.25, 88.}, {0.15, 70.}, {0.10, 86.}
            };
            // when
            final double weightedAverage = MathCalc.Arithmetic.weightedAverage(dataset);
            // then
            assertEquals(82.35, weightedAverage, DELTA2);
        }

        @Test
        void testPartPercentOfWhole() {
            // given
            final byte percent = 80;
            final byte whole = 30;
            // when
            final double part = MathCalc.Arithmetic.partPercentOfWhole(whole, percent);
            // then
            assertEquals(24, part, DELTA1);
        }

        @Test
        void testPercentOfWhole() {
            // given
            final byte part = 27;
            final byte whole = 30;
            // when
            final double percent = MathCalc.Arithmetic.percentOfWhole(part, whole);
            // then
            assertEquals(90, percent, DELTA1);
        }

        @Test
        void testPartPercentOfWhatWhole() {
            // given
            final byte percent = 25;
            final short whole = 4000;
            // when
            final double part = MathCalc.Arithmetic.partPercentOfWhatWhole(whole, percent);
            // then
            assertEquals(1000, part, DELTA1);
        }

        @Test
        void testWholePercentOfWhatPart() {
            // given
            final byte part = 10;
            final byte percent = 5;
            // when
            final double whole = MathCalc.Arithmetic.wholePercentOfWhatPart(part, percent);
            // then
            assertEquals(200, whole, DELTA1);
        }

        @Test
        void testIncreasedByPercent() {
            // given
            final byte whole = 5;
            final byte percentIncrease = 30;
            // when
            final double finalWhole = MathCalc.Arithmetic.increasedByPercent(whole, percentIncrease);
            // then
            assertEquals(6.5, finalWhole, DELTA1);
        }

        @Test
        void testOriginalBeforePercentIncrease() {
            // given
            final double finalWhole = 6.5;
            final byte percentIncrease = 30;
            // when
            final double whole = MathCalc.Arithmetic.originalBeforePercentIncrease(finalWhole, percentIncrease);
            // then
            assertEquals(5, whole, DELTA1);
        }

        @Test
        void testDecreasedByPercent() {
            // given
            final double whole = 48.89;
            final byte percentDecrease = 10;
            // when
            final double finalWhole = MathCalc.Arithmetic.decreasedByPercent(whole, percentDecrease);
            // then
            assertEquals(44, finalWhole, DELTA1);
        }

        @Test
        void testOriginalBeforePercentDecrease() {
            // given
            final byte finalWhole = 44;
            final byte percentDecrease = 10;
            // when
            final double whole = MathCalc.Arithmetic.originalBeforePercentDecrease(finalWhole, percentDecrease);
            // then
            assertEquals(48.89, whole, DELTA2);
        }

        static List<Arguments> percentageChangeArgs() {
            return List.of(
                Arguments.of(60., 72., 20., DELTA1),
                Arguments.of(50., -22., -144., DELTA1),
                Arguments.of(-10., -25., -150., DELTA1),
                Arguments.of(253_339_000., 310_384_000., 22.517, DELTA3),
                Arguments.of(5., 20., 300., DELTA1),
                Arguments.of(20., 10., -50., DELTA1),
                Arguments.of(2., 3., 50., DELTA1),
                Arguments.of(5., 4., -20., DELTA1)
            );
        }

        @ParameterizedTest
        @MethodSource("percentageChangeArgs")
        void testPercentageChange(double initial, double finalValue, double expectedResult, double delta) {
            // when
            final double percentageChange = MathCalc.Arithmetic.percentageChange(initial, finalValue);
            // then
            assertEquals(expectedResult, percentageChange, delta);
        }

        static List<Arguments> averagePercentageArgs() {
            return List.of(
                Arguments.of(new double[]{80., 40.}, 60., DELTA1),
                Arguments.of(new double[]{80., 80., 80., 80., 40.}, 72., DELTA1)
            );
        }

        @ParameterizedTest
        @MethodSource("averagePercentageArgs")
        void testAveragePercentage(double[] percents, double expectedResult, double delta) {
            // when
            final double averagePercentage = MathCalc.Arithmetic.averagePercentage(percents);
            // then
            assertEquals(expectedResult, averagePercentage, delta);
        }

        static List<Arguments> weightedAveragePercentageArgs() {
            return List.of(
                // (80%⋅4+40%⋅1) / (4+1) = 72%
                Arguments.of(new double[][]{{4., 80.}, {1., 40.}}, 72., DELTA1),
                // (300⋅64%+450⋅42%+250⋅36%) / (300+450+250) = 47.1%
                Arguments.of(new double[][]{{300., 64.}, {450., 42.}, {250., 36.}}, 47.1, DELTA1)
            );
        }

        @ParameterizedTest
        @MethodSource("weightedAveragePercentageArgs")
        void testWeightedAveragePercentage(double[][] weightedPercents, double expectedResult, double delta) {
            // when
            final double weightedAvgPercent = MathCalc.Arithmetic.weightedAveragePercentage(weightedPercents);
            // then
            assertEquals(expectedResult, weightedAvgPercent, delta);
        }

        @Test
        void testPercentToGoal() {
            // given
            final short progress = 8_500;
            final short goal = 15_500;
            // when
            final double percent = MathCalc.Arithmetic.percentToGoal(progress, goal);
            // then
            assertEquals(54.84, percent, DELTA2);
        }

        @Test
        void testPercentagePoint() {
            // given
            final byte percent1 = 5;
            final byte percent2 = 7;
            // when
            final double percentagePoint = MathCalc.Arithmetic.percentagePoint(percent1, percent2);
            final double percentChange = MathCalc.Arithmetic.percentageChange(percent1, percent2);
            // then
            assertEquals(2, percentagePoint, DELTA1);
            assertEquals(40, percentChange, DELTA1);
        }

        @Test
        void testPercentError() {
            // given
            final short trueValue = 343;
            final short observedValue = 329;
            // when
            final double percentError = MathCalc.Arithmetic.percentError(trueValue, observedValue);
            // then
            assertEquals(-4.082, percentError, DELTA3);
        }

        @Test
        void testPercentageDifference() {
            // given
            final short value1 = 70;
            final short value2 = 85;
            // when
            final double percentageDifference = MathCalc.Arithmetic.percentageDifference(value1, value2);
            final double difference = MathCalc.Arithmetic.percentagePoint(value1, value2);
            // then
            assertEquals(19.355, percentageDifference, DELTA3);
            assertEquals(15, difference, DELTA1);
        }

        @Test
        void testPercentOfPercent() {
            // given
            final byte percent1 = 40;
            final byte percent2 = 90;
            // when
            final double[] cumulativePercentResults = MathCalc.Arithmetic.percentOfPercent(percent1, percent2);
            // then
            assertNotNull(cumulativePercentResults);
            assertEquals(3, cumulativePercentResults.length);
            final double cumulativePercent = cumulativePercentResults[Constants.ARR_1ST_INDEX];
            assertEquals(36, cumulativePercent, DELTA1);
            final double firstPercent = cumulativePercentResults[Constants.ARR_2ND_INDEX];
            assertEquals(0.4, firstPercent, DELTA1);
            final double secondPercent = cumulativePercentResults[Constants.ARR_3RD_INDEX];
            assertEquals(0.9, secondPercent, DELTA1);
        }

        @Test
        void testPercentTime() {
            // given
            final byte hoursSpent = 2;
            final byte totalHours = 8;
            // when
            final double percentOfTotalTime = MathCalc.Arithmetic.percentTime(hoursSpent, totalHours);
            // then
            assertEquals(25, percentOfTotalTime, DELTA1);
        }
    }

    @Nested
    class Algebra {
        static List<Arguments> gammaFunctionArgs() {
            return List.of(
                Arguments.of(10, 362880.0000000015),
                Arguments.of(10.1, 454760.751441586)
            );
        }

        @ParameterizedTest
        @MethodSource("gammaFunctionArgs")
        void testGammaFunction(double x, double expectedResult) {
            // when
            final double result = MathCalc.Algebra.gammaFunction(x);
            // then
            assertEquals(expectedResult, result, DELTA9);
        }

        @Test
        void testSquareRootMultiply() {
            // given
            final byte x = 3;
            final byte y = 4;
            // when
            final double result = MathCalc.Algebra.squareRootMultiply(x, y);
            // then
            assertEquals(3.4641, result, DELTA4);
        }

        @Test
        void testSquareRootDivide() {
            // given
            final byte x = 8;
            final byte y = 4;
            // when
            final double result = MathCalc.Algebra.squareRootDivide(x, y);
            // then
            assertEquals(1.414214, result, DELTA6);
        }

        static List<Arguments> squareRootWithExponentArgs() {
            return List.of(
                Arguments.of(2, 4, 4),
                Arguments.of(5, 3, 11.18033989),
                Arguments.of(4, 5, 32)
            );
        }

        @ParameterizedTest
        @MethodSource("squareRootWithExponentArgs")
        void testSquareRootWithExponent(double x, double exponent, double expectedResult) {
            // when
            final double result = MathCalc.Algebra.squareRootWithExponent(x, exponent);
            // then
            assertEquals(expectedResult, result, DELTA8);
        }

        static List<Arguments> squareRootWithComplexNumberArgs() {
            return List.of(
                Arguments.of(-9, 3),
                Arguments.of(-13, Math.sqrt(13)),
                Arguments.of(-49, 7)
            );
        }

        @ParameterizedTest
        @MethodSource("squareRootWithComplexNumberArgs")
        void testSquareRootWithComplexNumber(double x, double expectedResult) {
            // when
            final double result = MathCalc.Algebra.squareRootWithComplexNumber(x);
            // then
            assertEquals(expectedResult, result, DELTA8);
        }

        @ParameterizedTest
        @CsvSource({
            "1,1",
            "2,1.26",
            "3,1.44",
            "4,1.59",
            "5,1.71",
            "8,2",
            "10,2.15",
            "27,3",
            "64,4",
            "125,5",
            "216,6",
            "343,7",
            "512,8",
            "729,9",
            "1000,10",
        })
        void testCubeRoot(double number, double expectedResult) {
            // when
            final double result = MathCalc.Algebra.cubeRoot(number);
            // then
            assertEquals(expectedResult, result, DELTA2);
        }

        @ParameterizedTest
        @CsvSource({
            "1296,4,6",
            "450,3.14,6.99797",
        })
        void testNthRoot(double number, double degree, double expectedResult) {
            // when
            final double result = MathCalc.Algebra.nthRoot(number, degree);
            // then
            assertEquals(expectedResult, result, DELTA5);
        }

        static List<Arguments> addRadicalsArgs() {
            return List.of(
                Arguments.of(new double[]{4, 7}, new double[]{2, 4, 7}, new double[]{3, 4, 7}), // ∜7 + 2∜7 = 3∜7
                Arguments.of(new double[]{5, 3, 5}, new double[]{3, 3, 5}, new double[]{8, 3, 5}) // 5∛5 + 3∛5 = 8∛5
            );
        }

        @ParameterizedTest
        @MethodSource("addRadicalsArgs")
        void testAddRadicals(double[] radical1, double[] radical2, double[] expectedResult) {
            // when
            final double[] result = MathCalc.Algebra.addRadicals(radical1, radical2);
            // then
            assertArrayEquals(expectedResult, result, DELTA5);
        }

        static List<Arguments> subtractRadicalsArgs() {
            return List.of(
                // 5*⁶√9 - 2*⁶√9 = 3*⁶√9
                Arguments.of(new double[]{5, 6, 9}, new double[]{2, 6, 9}, new double[]{3, 6, 9})
            );
        }

        @ParameterizedTest
        @MethodSource("subtractRadicalsArgs")
        void testSubtractRadicals(double[] radical1, double[] radical2, double[] expectedResult) {
            // when
            final double[] result = MathCalc.Algebra.subtractRadicals(radical1, radical2);
            // then
            assertArrayEquals(expectedResult, result, DELTA5);
        }

        static List<Arguments> multiplyRadicalsArgs() {
            return List.of(
                Arguments.of(new double[]{4, 7}, new double[]{4, 4}, new double[]{1, 4, 28}), // ∜7 * ∜4 = ∜28
                Arguments.of(new double[]{4, 3, 3}, new double[]{2, 3, 5}, new double[]{8, 3, 15}), // 4∛3 * 2∛5 = 8∛15
                Arguments.of(new double[]{5, 3, 2}, new double[]{3, 12}, new double[]{5, 3, 24}) // 5∛2 * ∛12 = 5∛24
            );
        }

        @ParameterizedTest
        @MethodSource("multiplyRadicalsArgs")
        void testMultiplyRadicals(double[] radical1, double[] radical2, double[] expectedResult) {
            // when
            final double[] result = MathCalc.Algebra.multiplyRadicals(radical1, radical2);
            // then
            assertArrayEquals(expectedResult, result, DELTA5);
        }

        static List<Arguments> divideRadicalsArgs() {
            return List.of(
                Arguments.of(new double[]{5, 8}, new double[]{5, 4}, new double[]{1, 5, 2}), // ⁵√8 / ⁵√4 = ⁵√2
                // 5∛15 / 2∛5 = 2.5∛3
                Arguments.of(new double[]{5, 3, 15}, new double[]{2, 3, 5}, new double[]{2.5, 3, 3})
            );
        }

        @ParameterizedTest
        @MethodSource("divideRadicalsArgs")
        void testDivideRadicals(double[] radical1, double[] radical2, double[] expectedResult) {
            // when
            final double[] result = MathCalc.Algebra.divideRadicals(radical1, radical2);
            // then
            assertArrayEquals(expectedResult, result, DELTA1);
        }

        static List<Arguments> simplifyRadicalArgs() {
            return List.of(
                Arguments.of(new double[]{3, 192}, new double[]{4, 3, 3}), // ∛192 = 4∛3
                Arguments.of(new double[]{2, 288}, new double[]{12, 2, 2}) // √288 = 12√2
            );
        }

        @ParameterizedTest
        @MethodSource("simplifyRadicalArgs")
        void testSimplifyRadical(double[] radical, double[] expectedResult) {
            // when
            final double[] result = MathCalc.Algebra.simplifyRadical(radical);
            // then
            assertArrayEquals(expectedResult, result, DELTA1);
        }

        static List<Arguments> simplifyRadicalsSumArgs() {
            return List.of(
                // 2√6 + ∜64 = 2√6 + 2∜4
                Arguments.of(new double[]{2, 6}, new double[]{4, 64}, new double[][]{{1, 2, 6}, {2, 4, 4}})
            );
        }

        @ParameterizedTest
        @MethodSource("simplifyRadicalsSumArgs")
        void testSimplifyRadicalsSum(double[] radical1, double[] radical2, double[][] expectedResult) {
            // when
            final double[][] result = MathCalc.Algebra.simplifyRadicalsSum(radical1, radical2);
            // then
            assertNotNull(result);
            assertEquals(2, result.length);
            assertArrayEquals(expectedResult[Constants.ARR_1ST_INDEX], result[Constants.ARR_1ST_INDEX], DELTA1);
            assertArrayEquals(expectedResult[Constants.ARR_2ND_INDEX], result[Constants.ARR_2ND_INDEX], DELTA1);
        }

        static List<Arguments> simplifyRadicalsProductArgs() {
            return List.of(
                Arguments.of(new double[]{2, 2, 6}, new double[]{4, 64}, new double[]{8, 4, 9}) // 2√6 * ∜64 = 8∜9
            );
        }

        @ParameterizedTest
        @MethodSource("simplifyRadicalsProductArgs")
        void testSimplifyRadicalsProduct(double[] radical1, double[] radical2, double[] expectedResult) {
            // when
            final double[] result = MathCalc.Algebra.simplifyRadicalsProduct(radical1, radical2);
            // then
            assertArrayEquals(expectedResult, result, DELTA1);
        }

        static List<Arguments> simplifyRadicalsQuotientArgs() {
            return List.of(
                // 2√6 / ∜64 = 0.03125∜9,437,184
                Arguments.of(new double[]{2, 2, 6}, new double[]{4, 64}, new double[]{0.03125, 4, 9_437_184})
            );
        }

        @ParameterizedTest
        @MethodSource("simplifyRadicalsQuotientArgs")
        void testSimplifyRadicalsQuotient(double[] radical1, double[] radical2, double[] expectedResult) {
            // when
            final double[] result = MathCalc.Algebra.simplifyRadicalsQuotient(radical1, radical2);
            // then
            assertArrayEquals(expectedResult, result, DELTA1);
        }

        @Test
        void testAddExponentsLaw() {
            // given
            final byte base = 5;
            final double[] exponents = new double[]{3, 2};
            // when
            final double result = MathCalc.Algebra.addExponentsLaw(base, exponents);
            // then
            assertEquals(3_125, result, DELTA1);
        }

        @Test
        void testSubtractExponentsLaw() {
            // given
            final byte base = 5;
            final double[] exponents = new double[]{3, 2};
            // when
            final double result = MathCalc.Algebra.subtractExponentsLaw(base, exponents);
            // then
            assertEquals(5, result, DELTA1);
        }

        @Test
        void testNegativeExponent() {
            // given
            final byte base = 5;
            final double exponent = -4;
            // when
            final double result = MathCalc.Algebra.negativeExponent(base, exponent);
            // then
            assertEquals(0.0016, result, DELTA4);
        }

        @Test
        void testLog() {
            // given
            final byte number = 4;
            // when
            final double logarithm = MathCalc.Algebra.log(number);
            // then
            assertEquals(0.602, logarithm, DELTA3);
        }

        @Test
        void testLog10ProductRule() {
            // given
            final double a = 5.89;
            final double b = 4.73;
            // when
            final double logarithm = MathCalc.Algebra.logProductRule(a, b);
            // then
            assertEquals(1.444976, logarithm, DELTA6);
        }

        static List<Arguments> logProductRuleWithBaseArgs() {
            return List.of(
                // log₄(500) = log₄(4 × 125) = log₄(4) + log₄(125) = 1 + 3.483 = 4.483
                Arguments.of(4, 125, 4, 4.483, DELTA3),
                // log₅(100) = log₅(10 × 10) = log₅(10) + log₅(10) = 1.4307 + 1.4307 = 2.8614
                Arguments.of(10, 10, 5, 2.8614, DELTA3)
            );
        }

        @ParameterizedTest
        @MethodSource("logProductRuleWithBaseArgs")
        void testLogProductRuleWithBase(double a, double b, double base, double expectedResult, double delta) {
            // when
            final double logarithm = MathCalc.Algebra.logProductRule(a, b, base);
            // then
            assertEquals(expectedResult, logarithm, delta);
        }

        static List<Arguments> logQuotientRuleArgs() {
            return List.of(
                // log₃(10) = log₃(20 / 2) = log₃(20) - log₃(2) = 2.727 - 0.6309 = 2.096
                Arguments.of(20, 2, 3, 2.096, DELTA3),
                // log₄(64) = log₄(128 / 2) = log₄(128) - log₄(2) = 3.5 - 0.5 = 3
                Arguments.of(128, 2, 4, 3, DELTA3)
            );
        }

        @ParameterizedTest
        @MethodSource("logQuotientRuleArgs")
        void testLogQuotientRule(double a, double b, double base, double expectedResult, double delta) {
            // when
            final double logarithm = MathCalc.Algebra.logQuotientRule(a, b, base);
            // then
            assertEquals(expectedResult, logarithm, delta);
        }

        static List<Arguments> logPowerRuleArgs() {
            return List.of(
                // log₇(64) = log₇(8²) = 2 × log₇(8) = 2 × 1.0686 = 2.1372
                Arguments.of(8, 2, 7, 2.1372, DELTA4),
                // log₇(8) = log₇(2³) = 3 × log₇(2) = 3 × 0.3562 = 1.0686
                Arguments.of(2, 3, 7, 1.0686, DELTA4),
                // log₇(4) = log₇(2²) = 2 × log₇(2) = 2 × 0.3562 = 0.7124
                Arguments.of(2, 2, 7, 0.7124, DELTA4),
                // log₃(729) = log₃(9³) = 3 × log₃(9) = 3 × 2 = 6
                Arguments.of(9, 3, 3, 6, DELTA1)
            );
        }

        @ParameterizedTest
        @MethodSource("logPowerRuleArgs")
        void testLogPowerRule(double number, double exponent, double base, double expectedResult, double delta) {
            // when
            final double logarithm = MathCalc.Algebra.logPowerRule(number, exponent, base);
            // then
            assertEquals(expectedResult, logarithm, delta);
        }

        static List<Arguments> antilogArgs() {
            return List.of(
                Arguments.of(3, Math.E, 20.085537, DELTA6),
                Arguments.of(3, 10, 1000, DELTA1),
                Arguments.of(3, 2, 8, DELTA1)
            );
        }

        @ParameterizedTest
        @MethodSource("antilogArgs")
        void testAntilog(double logarithm, double base, double expectedResult, double delta) {
            // when
            final double antilog = MathCalc.Algebra.antilog(logarithm, base);
            // then
            assertEquals(expectedResult, antilog, delta);
        }

        static List<Arguments> logAddArgs() {
            return List.of(
                // 3 log₆ 4 + log₆ 9 = log₆(4³ × 9) = log₆ 576 = 3.5474
                Arguments.of(3, 4, 1, 9, 6, 3.5474, DELTA4)
            );
        }

        @ParameterizedTest
        @MethodSource("logAddArgs")
        void testLogAdd(double x, double a, double y, double b, double base, double expectedResult, double delta) {
            // when
            final double logarithm = MathCalc.Algebra.logAdd(a, x, b, y, base);
            // then
            assertEquals(expectedResult, logarithm, delta);
        }

        static List<Arguments> logSubtractArgs() {
            return List.of(
                // 3 log₆ 4 - log₆ 9 = log₆(4³ / 9) = log₆ 7.111 = 1.09482
                Arguments.of(3, 4, 1, 9, 6, 1.09482, DELTA5)
            );
        }

        @ParameterizedTest
        @MethodSource("logSubtractArgs")
        void testLogSubtract(double x, double a, double y, double b, double base, double expectedResult, double delta) {
            // when
            final double logarithm = MathCalc.Algebra.logSubtract(a, x, b, y, base);
            // then
            assertEquals(expectedResult, logarithm, delta);
        }

        static List<Arguments> logMultiplyNumberArgs() {
            return List.of(
                // 3 log₆ 4 = log₆(4³) = log₆ 64 = 2.3211
                Arguments.of(3, 4, 6, 2.3211, DELTA4)
            );
        }

        @ParameterizedTest
        @MethodSource("logMultiplyNumberArgs")
        void testLogMultiplyNumber(double x, double a, double base, double expectedResult, double delta) {
            // when
            final double logarithm = MathCalc.Algebra.logMultiplyNumber(a, x, base);
            // then
            assertEquals(expectedResult, logarithm, delta);
        }

        @Test
        void testLogChangeOfBase() {
            // given
            final byte number = 100;
            final byte base = 2;
            // when
            final double logarithm = MathCalc.Algebra.logChangeOfBase(number, base);
            // then
            assertEquals(6.644, logarithm, DELTA3);
        }

        @Test
        void testNegativeLog() {
            // given
            final byte number = 8;
            final byte base = 2;
            // when
            final double logarithm = MathCalc.Algebra.negativeLog(number, base);
            // then
            assertEquals(-3, logarithm, DELTA1);
        }

        static List<Arguments> logChangeOfBaseArgs() {
            return List.of(
                Arguments.of(9, 27, 3, 0.667, DELTA3), // log₂₇(9) = log₃(9)/log₃(27) ≈ 2/3 ≈ 0.667
                // log₅(1000) = log₁₀(1000)/log₁₀(5) ≈ 3/0.699 ≈ 4.292
                Arguments.of(1000, 5, 10, 4.292, DELTA3),
                // log₂(20) = log₁₀(20)/log₁₀(2) ≈ 1.301/0.301 ≈ 4.322
                Arguments.of(20, 2, 10, 4.322, DELTA3),
                // log₁₀(20) = logₑ(20)/logₑ(10) ≈ 2.996/2.303 ≈ 1.301
                Arguments.of(20, 10, Math.E, 1.301, DELTA3)
            );
        }

        @ParameterizedTest
        @MethodSource("logChangeOfBaseArgs")
        void testLogChangeOfBase(double x, double base, double newBase, double expectedResult, double delta) {
            // when
            final double logarithm = MathCalc.Algebra.logChangeOfBase(x, base, newBase);
            // then
            assertEquals(expectedResult, logarithm, delta);
        }

        static List<Arguments> lnArgs() {
            return List.of(
                Arguments.of(1, 0, DELTA1),
                Arguments.of(Math.E, 1, DELTA1),
                Arguments.of(2, 0.6931, DELTA4),
                Arguments.of(4, 1.3863, DELTA4)
            );
        }

        @ParameterizedTest
        @MethodSource("lnArgs")
        void testLn(double number, double expectedResult, double delta) {
            // when
            final double logarithm = MathCalc.Algebra.ln(number);
            // then
            assertEquals(expectedResult, logarithm, delta);
        }

        @Test
        void testLnChangeOfBase() {
            // given
            final byte number = 100;
            final byte base = 2;
            // when
            final double logarithm = MathCalc.Algebra.lnChangeOfBase(number, base);
            // then
            assertEquals(6.644, logarithm, DELTA3);
        }

        static List<Arguments> log2Args() {
            return List.of(
                Arguments.of(MathCalc.ONE_EIGHTH, -3, DELTA1),
                Arguments.of(MathCalc.ONE_FOURTH, -2, DELTA1),
                Arguments.of(MathCalc.ONE_HALF, -1, DELTA1),
                Arguments.of(1, 0, DELTA1),
                Arguments.of(2, 1, DELTA1),
                Arguments.of(4, 2, DELTA1),
                Arguments.of(8, 3, DELTA1),
                Arguments.of(16, 4, DELTA1),
                Arguments.of(32, 5, DELTA1),
                Arguments.of(64, 6, DELTA1),
                Arguments.of(128, 7, DELTA1),
                Arguments.of(256, 8, DELTA1)
            );
        }

        @ParameterizedTest
        @MethodSource("log2Args")
        void testLog2(double number, double expectedResult, double delta) {
            // when
            final double logarithm = MathCalc.Algebra.log2(number);
            // then
            assertEquals(expectedResult, logarithm, delta);
        }

        static List<Arguments> doublingTimeArgs() {
            return List.of(
                Arguments.of(2, 15, 4.95948, DELTA5),
                Arguments.of(2, 430, 0.415629, DELTA6),
                Arguments.of(2, 2, 35.0028, DELTA4)
            );
        }

        @ParameterizedTest
        @MethodSource("doublingTimeArgs")
        void testDoublingTime(double initialAmount, double increase, double expectedResult, double delta) {
            // when
            final double doublingTime = MathCalc.Algebra.doublingTime(initialAmount, increase);
            // then
            assertEquals(expectedResult, doublingTime, delta);
        }

        static List<Arguments> conjugateArgs() {
            return List.of(
                // 2+2i => 2-2i
                Arguments.of(new Complex(2, 2), new Complex(2, -2), DELTA1),
                // 5-i => 5+i
                Arguments.of(new Complex(5, -1), new Complex(5, 1), DELTA1),
                Arguments.of(new Complex(-7, 0), new Complex(-7, 0), DELTA1), // -7 => -7
                // 7+32i => 7-32i
                Arguments.of(new Complex(7, 32), new Complex(7, -32), DELTA1),
                // 1.2-2.5i => 1.2+2.5i
                Arguments.of(new Complex(1.2, -2.5), new Complex(1.2, 2.5), DELTA1),
                // 6+0.7i => 6-0.7i
                Arguments.of(new Complex(6, 0.7), new Complex(6, -0.7), DELTA1),
                Arguments.of(new Complex(0, 1), new Complex(0, -1), DELTA1), // i => -i
                Arguments.of(new Complex(0, -3), new Complex(0, 3), DELTA1), // -3i => 3i
                Arguments.of(new Complex(18, 0), new Complex(18, 0), DELTA1), // 18 => 18
                // -9-0.03i => -9+0.03i
                Arguments.of(new Complex(-9, -0.03), new Complex(-9, 0.03), DELTA1)
            );
        }

        @ParameterizedTest
        @MethodSource("conjugateArgs")
        void testConjugate(Complex complex, Complex expectedResult, double delta) {
            // when
            final var conjugate = complex.conjugate(); // conjugate(a+bi) = a-bi
            // then
            assertEquals(expectedResult.getReal(), conjugate.getReal(), delta);
            assertEquals(expectedResult.getImaginary(), conjugate.getImaginary(), delta);
        }

        static List<Arguments> binomialCoefficientArgs() {
            return List.of(
                Arguments.of(4, 2, 6),
                Arguments.of(6, 2, 15)
            );
        }

        @ParameterizedTest
        @MethodSource("binomialCoefficientArgs")
        void testBinomialCoefficient(long totalItems, long numberOfItemsChosen, long expectedResult) {
            // when
            final long result = MathCalc.Algebra.binomialCoefficient(totalItems, numberOfItemsChosen);
            // then
            assertEquals(expectedResult, result);
        }

        static List<Arguments> multiplyBinomialsArgs() {
            return List.of(
                // (3x−2)(x+5) = (3 × 1)x2² + ((3 × 5) + (-2 × 1))x + (-2 × 5)
                // = 3x² + (15 + -2)x + -10 = 3x² + 13x + -10
                Arguments.of(
                    new double[]{-2, 3},
                    new double[]{5, 1},
                    new double[]{-10, 13, 3},
                    DELTA1
                )
            );
        }

        @ParameterizedTest
        @MethodSource("multiplyBinomialsArgs")
        void testMultiplyBinomials(double[] binomial1, double[] binomial2, double[] expectedResult, double delta) {
            // when
            final double[] result = MathCalc.Algebra.multiplyBinomials(binomial1, binomial2);
            // then
            assertArrayEquals(expectedResult, result, delta);
        }

        static List<Arguments> discriminantArgs() {
            return List.of(
                // quadratic
                Arguments.of(new double[]{-18, 21, -8}, -135, DELTA1), // -8x² + 21x - 18
                Arguments.of(new double[]{-7, 2, 9}, 256, DELTA1), // 0 = -7x² + 2x + 9; D = ±√256=±16
                Arguments.of(new double[]{6, 10, -1}, 124, DELTA1), // 6x² + 10x - 1 = 0
                // cubic
                Arguments.of(new double[]{2, -3, 0, 1}, 0, DELTA1), // x³ - 3x + 2 = 0
                // quartic
                Arguments.of(new double[]{1, 5, 0, -1, 4}, -238_383, DELTA1) // 4x⁴ - x³ + 5x + 1
            );
        }

        @ParameterizedTest
        @MethodSource("discriminantArgs")
        void testDiscriminant(double[] polynomialTerms, double expectedResult, double delta) {
            // when
            final double discriminant = MathCalc.Algebra.discriminant(polynomialTerms);
            // then
            assertEquals(expectedResult, discriminant, delta);
        }

        static List<Arguments> addPolynomialsArgs() {
            return List.of(
                // P(x) = 4x⁴ - x³ + 5x + 1
                // Q(x) = x⁵ + 4x⁴ - 7x³ - 3x² + x + 12
                // x⁵ + 8x⁴ - 8x³ - 3x² + 6x + 13
                Arguments.of(
                    new double[]{1, 5, 0, -1, 4},
                    new double[]{12, 1, -3, -7, 4, 1},
                    new double[]{13, 6, -3, -8, 8, 1},
                    DELTA1
                )
            );
        }

        @ParameterizedTest
        @MethodSource("addPolynomialsArgs")
        void testAddPolynomials(double[] polynomial1, double[] polynomial2, double[] expectedResult, double delta) {
            // when
            final double[] added = MathCalc.Algebra.addPolynomials(polynomial1, polynomial2);
            // then
            assertArrayEquals(expectedResult, added, delta);
        }

        static List<Arguments> subtractPolynomialsArgs() {
            return List.of(
                // P(x) = 4x⁴ - x³ + 5x + 1
                // Q(x) = x⁵ + 4x⁴ - 7x³ - 3x² + x + 12
                // -x⁵ + 6x³ + 3x² + 4x - 11
                Arguments.of(
                    new double[]{1, 5, 0, -1, 4},
                    new double[]{12, 1, -3, -7, 4, 1},
                    new double[]{-11, 4, 3, 6, 0, -1},
                    DELTA1
                )
            );
        }

        @ParameterizedTest
        @MethodSource("subtractPolynomialsArgs")
        void testSubtractPolynomials(double[] polynomial1, double[] polynomial2,
                                     double[] expectedResult, double delta) {
            // when
            final double[] subtracted = MathCalc.Algebra.subtractPolynomials(polynomial1, polynomial2);
            // then
            assertArrayEquals(expectedResult, subtracted, delta);
        }

        static List<Arguments> multiplyPolynomialsArgs() {
            return List.of(
                // x⁴ - 3x² + 2x + 4
                // -0.5x² + x - 2
                // -0.5x⁶ + x⁵ - 0.5x⁴ - 4x³ + 6x² - 8
                Arguments.of(
                    new double[]{4, 2, -3, 0, 1},
                    new double[]{-2, 1, -MathCalc.ONE_HALF},
                    new double[]{-8, 0, 6, -4, -MathCalc.ONE_HALF, 1, -MathCalc.ONE_HALF},
                    DELTA1
                )
            );
        }

        @ParameterizedTest
        @MethodSource("multiplyPolynomialsArgs")
        void testMultiplyPolynomials(double[] polynomial1, double[] polynomial2,
                                     double[] expectedResult, double delta) {
            // when
            final double[] multiplied = MathCalc.Algebra.multiplyPolynomials(polynomial1, polynomial2);
            // then
            assertArrayEquals(expectedResult, multiplied, delta);
        }

        static List<Arguments> dividePolynomialsArgs() {
            return List.of(
                // (x⁴ - 27x³ + 239x² - 753x + 540) / (x - 1) = x³ - 26x² + 213x - 540
                Arguments.of(
                    new double[]{540, -753, 239, -27, 1},
                    new double[]{-1, 1},
                    new double[]{-540, 213, -26, 1},
                    DELTA1
                )
            );
        }

        @ParameterizedTest
        @MethodSource("dividePolynomialsArgs")
        void testDividePolynomials(double[] polynomial1, double[] polynomial2,
                                   double[] expectedResult, double delta) {
            // when
            final double[] divided = MathCalc.Algebra.dividePolynomials(polynomial1, polynomial2);
            // then
            assertArrayEquals(expectedResult, divided, delta);
        }

        static List<Arguments> quadraticStdFormulaToVertexArgs() {
            return List.of(
                // Standard form: f(x) = 4x² + 4x - 3
                // Vertex form: f(x) = 4(x + 0.5)² - 4
                Arguments.of(new double[]{-3, 4, 4}, new double[]{-4, -MathCalc.ONE_HALF, 4}, DELTA1),
                // Standard form: f(x) = x² + 1
                // Vertex form: f(x) = x² + 1
                Arguments.of(new double[]{1, 0, 1}, new double[]{1, 0, 1}, DELTA1)
            );
        }

        @ParameterizedTest
        @MethodSource("quadraticStdFormulaToVertexArgs")
        void testQuadraticStdFormulaToVertex(double[] quadratic, double[] expectedResult, double delta) {
            // when
            final double[] terms = MathCalc.Algebra.quadraticStdFormulaToVertex(quadratic);
            // then
            assertArrayEquals(expectedResult, terms, delta);
        }

        static List<Arguments> quadraticStdFormulaToFactoredArgs() {
            return List.of(
                // Standard form: f(x) = 4x² + 4x - 3
                // Vertex form: f(x) = 4(x - 0.5)(x + 1.5)
                Arguments.of(new double[]{-3, 4, 4}, new double[]{4, 0.5, -1.5}, DELTA1),
                // Standard form: f(x) = x² + 1
                // Factored form: f(x) = (x + i)(x - i)
                Arguments.of(new double[]{1, 0, 1}, new double[]{1, 0, 1}, DELTA1)
            );
        }

        @ParameterizedTest
        @MethodSource("quadraticStdFormulaToFactoredArgs")
        void testQuadraticStdFormulaToFactored(double[] quadratic, double[] expectedResult, double delta) {
            // when
            final double[] terms = MathCalc.Algebra.quadraticStdFormulaToFactored(quadratic);
            // then
            assertArrayEquals(expectedResult, terms, delta);
        }

        static List<Arguments> quadraticRootsArgs() {
            return List.of(
                // 4x² + 3x – 7 = -4 – x
                // 4x² + (3 + 1)x + (-7 + 4) = 0
                // 4x² + 4x - 3 = 0
                Arguments.of(new double[]{-3, 4, 4}, new double[]{MathCalc.ONE_HALF, -1.5}, DELTA1),
                // x² + 1 = 0
                // Δ = b² – 4ac = 0² - 4×1×1 = -4
                // (0 + 2i) / (2×1) = i
                // (0 - 2i) / (2×1) = -i
                Arguments.of(new double[]{1, 0, 1}, new double[]{0, 1}, DELTA1)
            );
        }

        @ParameterizedTest
        @MethodSource("quadraticRootsArgs")
        void testQuadraticRoots(double[] quadratic, double[] expectedResult, double delta) {
            // when
            final double[] roots = MathCalc.Algebra.quadraticRoots(quadratic);
            // then
            assertArrayEquals(expectedResult, roots, delta);
        }
    }

    @Nested
    class Geometry {
        @Test
        void calculateTriangle306090SolveWithA() {
            // given
            final float sideA = 6.35f; // cm
            // when
            final double[] sides = MathCalc.Geometry.triangle306090SolveWithA(sideA);
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
            final double[] sides = MathCalc.Geometry.triangle306090SolveWithB(sideB);
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
            final double[] sides = MathCalc.Geometry.triangle306090SolveWithC(sideC);
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
            final double[] sides = MathCalc.Geometry
                .pythagoreanTheoremForRightTriangleWithLegAndHypotenuse(sideA, hypotenuse);
            // then
            assertNotNull(sides);
            assertEquals(3, sides.length);

            final double sideB = sides[Constants.ARR_2ND_INDEX];

            assertEquals(sideA, sides[Constants.ARR_1ST_INDEX], 0.1);
            assertEquals(expectedResult, sideB, 0.1);
            assertEquals(hypotenuse, sides[Constants.ARR_3RD_INDEX], 0.000001);

            final double area = MathCalc.Geometry.area(sideA, sideB);
            assertEquals(expectedArea, area, 0.1);

            final double perimeter = MathCalc.Geometry.perimeter(sideA, sideB, hypotenuse);
            assertEquals(expectedPerimeter, perimeter, 0.00001);
        }

        @Test
        void testPythagoreanTheoremForRightTriangleWithLegs() {
            // given
            final byte sideA = 7; // cm
            final byte sideB = 9; // cm
            // when
            final double[] sides = MathCalc.Geometry.pythagoreanTheoremForRightTriangleWithLegs(sideA, sideB);
            // then
            assertNotNull(sides);
            assertEquals(3, sides.length);

            assertEquals(sideA, sides[Constants.ARR_1ST_INDEX], 0.1);
            assertEquals(sideB, sides[Constants.ARR_2ND_INDEX], 0.1);
            final double hypotenuse = 11.40175; // cm
            assertEquals(hypotenuse, sides[Constants.ARR_3RD_INDEX], 0.00001);

            final double area = MathCalc.Geometry.area(sideA, sideB);
            assertEquals(31.5, area, 0.1);

            final double perimeter = MathCalc.Geometry.perimeter(sideA, sideB, hypotenuse);
            assertEquals(27.40175, perimeter, 0.00001);
        }

        @Test
        void testAreaOfSquare() {
            // given
            final byte sideLength = 4; // cm
            // when
            final double area = MathCalc.Geometry.areaOfSquare(sideLength);
            // then
            assertEquals(16, area, 0.1);
        }

        @Test
        void testAreaOfRectangle() {
            // given
            final byte sideLengthA = 2; // cm
            final byte sideLengthB = 4; // cm
            // when
            final double area = MathCalc.Geometry.areaOfRectangle(sideLengthA, sideLengthB);
            // then
            assertEquals(8, area, 0.1);
        }

        @Test
        void testAreaOfTriangleWithBaseAndHeight() {
            // given
            final byte base = 8; // cm
            final byte height = 4; // cm
            // when
            final double area = MathCalc.Geometry.areaOfTriangleWithBaseAndHeight(base, height);
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
            final double area = MathCalc.Geometry.areaOfTriangleWithSSS(sideLengthA, sideLengthB, sideLengthC);
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
                () -> MathCalc.Geometry.areaOfTriangleWithSSS(sideLengthA, sideLengthB, sideLengthC));
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
                () -> MathCalc.Geometry.areaOfTriangleWithSSS(sideLengthA, sideLengthB, sideLengthC));
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
                () -> MathCalc.Geometry.areaOfTriangleWithSSS(sideLengthA, sideLengthB, sideLengthC));
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
            final double area = MathCalc.Geometry.areaOfTriangleWithSAS(
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
            final double area = MathCalc.Geometry.areaOfTriangleWithASA(
                sideLengthA, angleBetaRadians, angleGammaRadians);
            // then
            assertEquals(0.866, area, 0.001);
        }

        @Test
        void testCircleArea() {
            // given
            final byte radius = 5; // cm
            // when
            final double area = MathCalc.Geometry.circleArea(radius);
            // then
            assertEquals(78.54, area, 0.01);
        }

        @Test
        void testSemicircleArea() {
            // given
            final byte radius = 5; // cm
            // when
            final double area = MathCalc.Geometry.semicircleArea(radius);
            // then
            assertEquals(39.27, area, 0.01);
        }

        @Test
        void testSectorArea() {
            // given
            final byte radius = 5; // cm
            final double angleAlphaRadians = Math.toRadians(30);
            // when
            final double area = MathCalc.Geometry.sectorArea(radius, angleAlphaRadians);
            // then
            assertEquals(6.545, area, 0.001);
        }

        @Test
        void testEllipseArea() {
            // given
            final byte radiusA = 5; // cm
            final byte radiusB = 3; // cm
            // when
            final double area = MathCalc.Geometry.ellipseArea(radiusA, radiusB);
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
            final double area = MathCalc.Geometry.trapezoidArea(sideA, sideB, height);
            // then
            assertEquals(20, area, 0.1);
        }

        @Test
        void testParallelogramAreaWithBaseAndHeight() {
            // given
            final byte base = 8; // cm
            final byte height = 4; // cm
            // when
            final double area = MathCalc.Geometry.parallelogramAreaWithBaseAndHeight(base, height);
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
            final double area = MathCalc.Geometry
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
            final double area = MathCalc.Geometry
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
            final double area = MathCalc.Geometry.rhombusAreaWithSideAndHeight(side, height);
            // then
            assertEquals(24, area, 0.1);
        }

        @Test
        void testRhombusAreaWithDiagonals() {
            // given
            final byte diagonal1 = 6; // cm
            final byte diagonal2 = 4; // cm
            // when
            final double area = MathCalc.Geometry.rhombusAreaWithDiagonals(diagonal1, diagonal2);
            // then
            assertEquals(12, area, 0.1);
        }

        @Test
        void testRhombusAreaWithSideAndAngle() {
            // given
            final byte side = 4; // cm
            final double angleAlphaRadians = Math.toRadians(60);
            // when
            final double area = MathCalc.Geometry.rhombusAreaWithSideAndAngle(side, angleAlphaRadians);
            // then
            assertEquals(13.856, area, 0.001);
        }

        @Test
        void testKiteAreaWithDiagonals() {
            // given
            final byte diagonal1 = 4; // cm
            final byte diagonal2 = 10; // cm
            // when
            final double area = MathCalc.Geometry.kiteAreaWithDiagonals(diagonal1, diagonal2);
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
            final double area = MathCalc.Geometry
                .kiteAreaWithSidesAndAngle(sideA, sideB, angleAlphaRadians);
            // then
            assertEquals(9.9, area, 0.1);
        }

        @Test
        void testPentagonArea() {
            // given
            final byte sideLength = 4; // cm
            // when
            final double area = MathCalc.Geometry.pentagonArea(sideLength);
            // then
            assertEquals(27.53, area, 0.01);
        }

        @Test
        void testHexagonArea() {
            // given
            final byte sideLength = 4; // cm
            // when
            final double area = MathCalc.Geometry.hexagonArea(sideLength);
            // then
            assertEquals(41.57, area, 0.01);
        }

        @Test
        void testOctagonArea() {
            // given
            final byte sideLength = 3; // cm
            // when
            final double area = MathCalc.Geometry.octagonArea(sideLength);
            // then
            assertEquals(43.46, area, 0.01);
        }

        @Test
        void testAnnulusArea() {
            // given
            final double innerRadius = 1.5; // cm
            final double radius = 4; // cm
            // when
            final double area = MathCalc.Geometry.annulusArea(radius, innerRadius);
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
            final double area = MathCalc.Geometry
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
            final double area = MathCalc.Geometry.polygonArea(numberOfSides, sideLength);
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
            final double[] heights = MathCalc.Geometry.scaleneTriangleHeight(sideA, sideB, sideC);
            // then
            assertNotNull(heights);
            assertEquals(3, heights.length);
            assertEquals(13.17, heights[Constants.ARR_1ST_INDEX], 0.01);
            final double heightB = heights[Constants.ARR_2ND_INDEX];
            assertEquals(5.644, heightB, 0.001);
            assertEquals(4.648, heights[Constants.ARR_3RD_INDEX], 0.001);

            final double areaCmSquared = MathCalc.Geometry.areaOfTriangleWithSSS(sideA, sideB, sideC);
            assertEquals(39.51, areaCmSquared, 0.01);

            final double perimeter = MathCalc.Geometry.perimeter(sideA, sideB, sideC);
            assertEquals(37, perimeter, 0.1);

            final double[] angles = MathCalc.Trigonometry.lawOfCosSSS(sideA, sideB, sideC);
            assertEquals(0.3384, angles[Constants.ALPHA_INDEX], 0.0001);
            assertEquals(0.8862, angles[Constants.BETA_INDEX], 0.0001);
            assertEquals(1.917, angles[Constants.GAMMA_INDEX], 0.001);
        }

        @Test
        void testEquilateralTriangleHeight() {
            // given
            final byte sides = 5;
            // when
            final double height = MathCalc.Geometry.equilateralTriangleHeight(sides);
            // then
            assertEquals(4.33, height, 0.01);

            final double areaCmSquared = MathCalc.Geometry.equilateralTriangleArea(sides);
            assertEquals(10.825, areaCmSquared, 0.001);

            final double perimeter = MathCalc.Geometry.perimeter(sides, sides, sides);
            assertEquals(15, perimeter, 0.1);
        }

        @Test
        void testIsoscelesTriangleHeight() {
            // given
            final byte sideA = 3;
            final byte sideB = 5;
            // when
            final double[] heights = MathCalc.Geometry.isoscelesTriangleHeight(sideA, sideB);
            // then
            assertNotNull(heights);
            assertEquals(2, heights.length);
            assertEquals(2.764, heights[Constants.ARR_1ST_INDEX], 0.001);
            final double heightB = heights[Constants.ARR_2ND_INDEX];
            assertEquals(1.6583, heightB, 0.0001);

            final double areaCmSquared = MathCalc.Geometry.isoscelesTriangleArea(sideB, heightB);
            assertEquals(4.146, areaCmSquared, 0.001);

            final double perimeter = MathCalc.Geometry.perimeter(sideA, sideB, sideA);
            assertEquals(11, perimeter, 0.1);

            final double[] angles = MathCalc.Trigonometry.lawOfCosSSS(sideB, sideA, sideA);
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
            final double[] heights = MathCalc.Geometry.rightTriangleHeight(sideA, sideB, sideC);
            // then
            assertNotNull(heights);
            assertEquals(3, heights.length);
            assertEquals(4, heights[Constants.ARR_1ST_INDEX], 0.1);
            assertEquals(3, heights[Constants.ARR_2ND_INDEX], 0.1);
            assertEquals(2.4, heights[Constants.ARR_3RD_INDEX], 0.1);

            final double areaCmSquared = MathCalc.Geometry.areaOfTriangleWithSSS(sideA, sideB, sideC);
            assertEquals(6, areaCmSquared, 0.1);

            final double perimeter = MathCalc.Geometry.perimeter(sideA, sideB, sideC);
            assertEquals(12, perimeter, 0.1);

            final double[] angles = MathCalc.Trigonometry.lawOfCosSSS(sideA, sideB, sideC);
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
            final double areaCmSquared = MathCalc.Geometry.heronFormulaUsingSemiperimeter(sideA, sideB, sideC);
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
            final double areaCmSquared = MathCalc.Geometry.heronFormulaUsingQuadProduct(sideA, sideB, sideC);
            // then
            assertEquals(30, areaCmSquared, 0.1);
        }

        @Test
        void testAreaWithBaseAndHeight() {
            // given
            final byte sideA = 12;
            final byte sideB = 5;
            final byte sideC = 13;
            final double[] heights = MathCalc.Geometry.scaleneTriangleHeight(sideA, sideB, sideC);
            final double height = heights[Constants.ARR_3RD_INDEX];
            // when
            final double areaCmSquared = MathCalc.Geometry.areaWithBaseAndHeight(sideC, height);
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
            final boolean equilateral = MathCalc.Geometry.isEquilateralTriangle(sideA, sideB, sideC);
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
            final boolean equilateral = MathCalc.Geometry.isScaleneTriangle(sideA, sideB, sideC);
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
            final boolean acute = MathCalc.Geometry.isAcuteTriangle(angleAlphaRad, angleBetaRad, angleGammaRad);
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
            final boolean acute = MathCalc.Geometry.isAcuteTriangleWithSSA(angleAlphaRad, sideA, sideB);
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
            final boolean right = MathCalc.Geometry.isRightTriangle(angleAlphaRad, angleBetaRad, angleGammaRad);
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
            final boolean right = MathCalc.Geometry.isRightTriangleWithSSA(angleAlphaRad, sideA, sideB);
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
            final boolean obtuse = MathCalc.Geometry.isObtuseTriangle(angleAlphaRad, angleBetaRad, angleGammaRad);
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
            final boolean obtuse = MathCalc.Geometry.isObtuseTriangleWithSSA(angleAlphaRad, sideA, sideB);
            // then
            assertEquals(expectedResult, obtuse);
        }

        static List<Arguments> complementaryAngleArgs() {
            return List.of(
                Arguments.of(Math.toRadians(15), Math.toRadians(75)),
                Arguments.of(Math.toRadians(30), Math.toRadians(60)),
                Arguments.of(Math.toRadians(45), Math.toRadians(45)),
                Arguments.of(Math.toRadians(60), Math.toRadians(30)),
                Arguments.of(Math.toRadians(75), Math.toRadians(15)),
                Arguments.of(Math.toRadians(90), Math.toRadians(0))
            );
        }

        @ParameterizedTest
        @MethodSource("complementaryAngleArgs")
        void testComplementaryAngle(double angleRadians, double expectedResult) {
            // when
            final double complementaryAngle = MathCalc.Geometry.complementaryAngle(angleRadians);
            // then
            assertEquals(expectedResult, complementaryAngle, 0.000001);
        }

        static List<Arguments> isComplementaryAngleArgs() {
            return List.of(
                Arguments.of(Math.toRadians(15), Math.toRadians(75), true),
                Arguments.of(Math.toRadians(30), Math.toRadians(60), true),
                Arguments.of(Math.toRadians(45), Math.toRadians(45), true),
                Arguments.of(Math.toRadians(60), Math.toRadians(30), true),
                Arguments.of(Math.toRadians(75), Math.toRadians(15), true),
                Arguments.of(Math.toRadians(60), Math.toRadians(15), false),
                Arguments.of(Math.toRadians(60), Math.toRadians(35), false),
                Arguments.of(Math.toRadians(60), Math.toRadians(45), false),
                Arguments.of(Math.toRadians(90), Math.toRadians(10), false)
            );
        }

        @ParameterizedTest
        @MethodSource("isComplementaryAngleArgs")
        void testIsComplementaryAngle(double angleAlphaRadians, double angleBetaRadians, boolean expectedResult) {
            // when
            final boolean complementary = MathCalc.Geometry
                .isComplementaryAngle(angleAlphaRadians, angleBetaRadians);
            // then
            assertEquals(expectedResult, complementary);
        }

        @Test
        void testSupplementaryAngle() {
            // given
            final double angleRadians = Math.toRadians(30);
            // when
            final double supplementaryAngle = MathCalc.Geometry.supplementaryAngle(angleRadians);
            // then
            assertEquals(2.618, supplementaryAngle, 0.001); // 150°
        }

        static List<Arguments> isSupplementaryAngleArgs() {
            return List.of(
                Arguments.of(Math.toRadians(60), Math.toRadians(40), false),
                Arguments.of(Math.toRadians(60), Math.toRadians(60), false),
                Arguments.of(Math.toRadians(120), Math.toRadians(60), true),
                Arguments.of(Math.toRadians(45), Math.toRadians(135), true),
                Arguments.of(Math.toRadians(90), Math.toRadians(90), true)
            );
        }

        @ParameterizedTest
        @MethodSource("isSupplementaryAngleArgs")
        void testIsSupplementaryAngle(double angleAlphaRadians, double angleBetaRadians, boolean expectedResult) {
            // when
            final boolean supplementary = MathCalc.Geometry
                .isSupplementaryAngles(angleAlphaRadians, angleBetaRadians);
            // then
            assertEquals(expectedResult, supplementary);
        }

        static List<Arguments> coterminalAngleArgs() {
            return List.of(
                Arguments.of(Math.toRadians(420), Math.toRadians(60)),
                Arguments.of(Math.toRadians(-858), Math.toRadians(222))
            );
        }

        @ParameterizedTest
        @MethodSource("coterminalAngleArgs")
        void testCoterminalAngle(double angleRadians, double expectedResult) {
            // when
            final double coterminalAngle = MathCalc.Geometry.coterminalAngle(angleRadians);
            // then
            assertEquals(expectedResult, coterminalAngle, 0.000001);
        }

        @Test
        void testCoterminalAngles() {
            // given
            final double angleRadians = Math.toRadians(45);
            final byte min = -4;
            final byte max = 4;
            // when
            final double[] coterminalAngles = MathCalc.Geometry.coterminalAngles(angleRadians, min, max);
            // then
            assertNotNull(coterminalAngles);
            assertEquals(8, coterminalAngles.length);
            final double delta = 0.000001;
            assertEquals(Math.toRadians(-1395), coterminalAngles[Constants.ARR_1ST_INDEX], delta);
            assertEquals(Math.toRadians(-1035), coterminalAngles[Constants.ARR_2ND_INDEX], delta);
            assertEquals(Math.toRadians(-675), coterminalAngles[Constants.ARR_3RD_INDEX], delta);
            assertEquals(Math.toRadians(-315), coterminalAngles[Constants.ARR_4TH_INDEX], delta);
            assertEquals(Math.toRadians(405), coterminalAngles[Constants.ARR_5TH_INDEX], delta);
            assertEquals(Math.toRadians(765), coterminalAngles[Constants.ARR_6TH_INDEX], delta);
            assertEquals(Math.toRadians(1125), coterminalAngles[Constants.ARR_7TH_INDEX], delta);
            assertEquals(Math.toRadians(1485), coterminalAngles[Constants.ARR_8TH_INDEX], delta);
        }

        static List<Arguments> areCoterminalAngleArgs() {
            return List.of(
                Arguments.of(Math.toRadians(-170), Math.toRadians(550), true),
                Arguments.of(Math.toRadians(-170), Math.toRadians(540), false)
            );
        }

        @ParameterizedTest
        @MethodSource("areCoterminalAngleArgs")
        void testAreCoterminalAngle(double angleAlphaRad, double angleBetaRad, boolean expectedResult) {
            // given
            final byte rotations = 2;
            // when
            final boolean coterminalAngle = MathCalc.Geometry
                .areCoterminalAngles(angleAlphaRad, angleBetaRad, rotations);
            // then
            assertEquals(expectedResult, coterminalAngle);
        }

        static List<Arguments> referenceAngleArgs() {
            return List.of(
                Arguments.of(Math.toRadians(0), Math.toRadians(0)),
                Arguments.of(Math.toRadians(30), Math.toRadians(30)),
                Arguments.of(Math.toRadians(90), Math.toRadians(90)),
                Arguments.of(Math.toRadians(120), Math.toRadians(60)),
                Arguments.of(Math.toRadians(180), Math.toRadians(0)),
                Arguments.of(Math.toRadians(210), Math.toRadians(30)),
                Arguments.of(Math.toRadians(270), Math.toRadians(90)),
                Arguments.of(Math.toRadians(300), Math.toRadians(60)),
                Arguments.of(MathCalc.Trigonometry.PI2, Math.toRadians(0)),
                Arguments.of(Math.toRadians(610), Math.toRadians(70))
            );
        }

        @ParameterizedTest
        @MethodSource("referenceAngleArgs")
        void testReferenceAngle(double angleRadians, double expectedResult) {
            // when
            final double referenceAngle = MathCalc.Geometry.referenceAngle(angleRadians);
            // then
            assertEquals(expectedResult, referenceAngle, 0.000001);
        }

        @Test
        void testCentralAngleGivenArcLengthRadius() {
            final short arcLengthInMeters = 235;
            final double radiusInMeters = 149.6;
            // when
            final double centralAngle = MathCalc.Geometry
                .centralAngleGivenArcLengthRadius(arcLengthInMeters, radiusInMeters);
            // then
            assertEquals(MathCalc.Trigonometry.PI_OVER_2, centralAngle, 0.0001);
        }

        @Test
        void testArcLength() {
            final double centralAngleRad = MathCalc.Trigonometry.PI_OVER_2;
            final double radiusInMeters = 149.6;
            // when
            final double arcLengthInMeters = MathCalc.Geometry.arcLength(centralAngleRad, radiusInMeters);
            // then
            assertEquals(235, arcLengthInMeters, 0.1);
        }

        @Test
        void testClockAngle() {
            final byte hours = 7;
            final byte minutes = 0;
            // when
            final double[] angles = MathCalc.Geometry.clockAngle(hours, minutes);
            // then
            assertNotNull(angles);
            assertEquals(2, angles.length);
            assertEquals(Math.toRadians(150), angles[Constants.ARR_1ST_INDEX], DELTA6);
            assertEquals(Math.toRadians(210), angles[Constants.ARR_2ND_INDEX], DELTA6);
        }
    }

    @Nested
    class CoordinateGeometry {
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
            final double distance = MathCalc.CoordinateGeometry.manhattanDistance(vectorA, vectorB);
            // then
            assertEquals(expectedResult, distance, 0.1);
        }

        @Test
        void testCartesianToCylindricalCoordinates() {
            // given
            final double[] cartesianCoords = {2, 5, 3};
            // when
            final double[] cylindricalCoords = MathCalc.CoordinateGeometry
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
            final double[] cartesianCoords = MathCalc.CoordinateGeometry
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
            final double[] polarCoords = MathCalc.CoordinateGeometry.cartesianToPolarCoordinates(cartesianCoords);
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
            final double[] cartesianCoords = MathCalc.CoordinateGeometry.polarToCartesianCoordinates(polarCoords);
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
            final double[] sphericalCoords = MathCalc.CoordinateGeometry
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
            final double[] cartesianCoords = MathCalc.CoordinateGeometry
                .sphericalToCartesianCoordinates(sphericalCoords);
            // then
            assertNotNull(cartesianCoords);
            assertEquals(3, cartesianCoords.length);
            assertEquals(2, cartesianCoords[Constants.X_INDEX], 0.1);
            assertEquals(5, cartesianCoords[Constants.Y_INDEX], 0.1);
            assertEquals(3, cartesianCoords[Constants.Z_INDEX], 0.1);
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
            final double distance = MathCalc.CoordinateGeometry.distance(pointACoords, pointBCoords);
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
            final double abDistance = MathCalc.CoordinateGeometry.distance(pointACoords, pointBCoords);
            final double bcDistance = MathCalc.CoordinateGeometry.distance(pointBCoords, pointCCoords);
            final double acDistance = MathCalc.CoordinateGeometry.distance(pointACoords, pointCCoords);
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
            final double distance = MathCalc.CoordinateGeometry
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
            final double distance = MathCalc.CoordinateGeometry
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
            final double[] resultCoords = MathCalc.CoordinateGeometry.rotation(pointCoords, angleTheta);
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
            final double[] resultCoords = MathCalc.CoordinateGeometry
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
            final double slope = MathCalc.CoordinateGeometry.slope(pointACoords, pointBCoords);
            final double angleTheta = Math.atan(slope);
            final double distance = MathCalc.CoordinateGeometry.distance(pointACoords, pointBCoords);
            final double deltaX = MathCalc.CoordinateGeometry.deltaDistance(
                pointBCoords[Constants.X_INDEX], pointACoords[Constants.X_INDEX]);
            final double deltaY = MathCalc.CoordinateGeometry.deltaDistance(
                pointBCoords[Constants.Y_INDEX], pointACoords[Constants.Y_INDEX]);
            final double constantTerm = MathCalc.CoordinateGeometry.slopeInterceptConstantTerm(
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
            final double slope = MathCalc.CoordinateGeometry.slopeFromKnownIntercepts(xIntercept, yIntercept);
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
            final double area = MathCalc.CoordinateGeometry.areaUnderSlope(x1, x2, slope);
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
            final double[] intercepts = MathCalc.CoordinateGeometry.intercept(a, b, c);
            final double slope = MathCalc.CoordinateGeometry.slope(a, b);
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
            final double[] intercepts = MathCalc.CoordinateGeometry.intercept(slopeTerm, constantTerm);
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
            final double midpointY = MathCalc.CoordinateGeometry
                .linearInterpolation(pointACoords, pointBCoords, midpointX);
            final double slope = MathCalc.CoordinateGeometry.slope(pointACoords, pointBCoords);
            final double deltaY = MathCalc.CoordinateGeometry.deltaDistance(
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
            final double[] midpointCoords = MathCalc.CoordinateGeometry.midpoint(pointACoords, pointBCoords);
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
            final double coordinate = MathCalc.CoordinateGeometry.endpointWithGivenMidpoint(point, midpoint);
            // then
            assertEquals(8, coordinate, 0.1);
        }
    }

    @Nested
    class Trigonometry {
        static List<Arguments> sinParams() {
            return List.of(
                Arguments.of(MathCalc.Trigonometry.PI_OVER_12, 0.2588190451),
                Arguments.of(MathCalc.Trigonometry.PI_OVER_6, 0.5),
                Arguments.of(MathCalc.Trigonometry.PI_OVER_4, 0.7071067812),
                Arguments.of(MathCalc.Trigonometry.PI_OVER_3, 0.8660254038),
                Arguments.of(MathCalc.Trigonometry.PI5_OVER_12, 0.9659258263),
                Arguments.of(MathCalc.Trigonometry.PI_OVER_2, 1),
                Arguments.of(MathCalc.Trigonometry.PI7_OVER_12, 0.9659258263),
                Arguments.of(MathCalc.Trigonometry.PI2_OVER_3, 0.8660254038),
                Arguments.of(MathCalc.Trigonometry.PI3_OVER_4, 0.7071067812),
                Arguments.of(MathCalc.Trigonometry.PI5_OVER_6, 0.5),
                Arguments.of(MathCalc.Trigonometry.PI11_OVER_12, 0.2588190451)
            );
        }

        @ParameterizedTest
        @MethodSource("sinParams")
        void testSin(double angleAlphaRadians, double expectedResult) {
            // when
            final double sine = MathCalc.Trigonometry.sin(angleAlphaRadians);
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
            final double sinusoid = MathCalc.Trigonometry.sinusoid(
                amplitude, anglePhiRadians, oscillationFrequency, timeSeconds);
            // then
            assertEquals(0.51423008, sinusoid, 0.00000001);
        }

        static List<Arguments> quadrantParams() {
            return List.of(
                Arguments.of(MathCalc.Trigonometry.PI_OVER_12, 1),
                Arguments.of(MathCalc.Trigonometry.PI_OVER_6, 1),
                Arguments.of(MathCalc.Trigonometry.PI_OVER_4, 1),
                Arguments.of(MathCalc.Trigonometry.PI_OVER_3, 1),
                Arguments.of(MathCalc.Trigonometry.PI5_OVER_12, 1),
                Arguments.of(MathCalc.Trigonometry.PI_OVER_2, 1),
                Arguments.of(MathCalc.Trigonometry.PI7_OVER_12, 2),
                Arguments.of(MathCalc.Trigonometry.PI2_OVER_3, 2),
                Arguments.of(MathCalc.Trigonometry.PI3_OVER_4, 2),
                Arguments.of(MathCalc.Trigonometry.PI5_OVER_6, 2),
                Arguments.of(MathCalc.Trigonometry.PI11_OVER_12, 2),
                Arguments.of(Math.PI, 2),
                Arguments.of(MathCalc.Trigonometry.PI7_OVER_6, 3),
                Arguments.of(MathCalc.Trigonometry.PI5_OVER_4, 3),
                Arguments.of(MathCalc.Trigonometry.PI4_OVER_3, 3),
                Arguments.of(MathCalc.Trigonometry.PI3_OVER_2, 3),
                Arguments.of(MathCalc.Trigonometry.PI5_OVER_3, 4),
                Arguments.of(MathCalc.Trigonometry.PI7_OVER_4, 4),
                Arguments.of(MathCalc.Trigonometry.PI11_OVER_6, 4),
                Arguments.of(MathCalc.Trigonometry.PI2, 4)
            );
        }

        @ParameterizedTest
        @MethodSource("quadrantParams")
        void testQuadrant(double angleAlphaRadians, int expectedResult) {
            // when
            final int quadrant = MathCalc.Trigonometry.quadrant(angleAlphaRadians);
            // then
            assertEquals(expectedResult, quadrant);
        }

        @Test
        void testLawOfTangents() {
            // given
            final double angleAlphaRadians = Math.toRadians(30);
            final double angleBetaRadians = Math.toRadians(45);
            // when
            final double result = MathCalc.Trigonometry.lawOfTangents(angleAlphaRadians, angleBetaRadians);
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
            final double result = MathCalc.Trigonometry.cot(angleAlphaRadians);
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
            final double result = MathCalc.Trigonometry.cot(adjacent, opposite);
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
            final double result = MathCalc.Trigonometry.sec(angleAlphaRadians);
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
            final double result = MathCalc.Trigonometry.sec(hypotenuse, adjacent);
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
            final double result = MathCalc.Trigonometry.csc(angleAlphaRadians);
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
            final double result = MathCalc.Trigonometry.csc(hypotenuse, opposite);
            // then
            assertEquals(expectedResult, result, delta);
        }

        @Test
        void testCosHalfAngle() {
            // given
            final double angleRadians = Math.toRadians(30);
            // when
            final double result = MathCalc.Trigonometry.cosHalfAngle(angleRadians);
            // then
            assertEquals(0.96592583, result, 0.00000001);
        }

        @Test
        void testSinHalfAngle() {
            // given
            final double angleRadians = Math.toRadians(30);
            // when
            final double result = MathCalc.Trigonometry.sinHalfAngle(angleRadians);
            // then
            assertEquals(0.25881905, result, 0.00000001);
        }

        @Test
        void testTanHalfAngle() {
            // given
            final double angleRadians = Math.toRadians(30);
            // when
            final double result = MathCalc.Trigonometry.tanHalfAngle(angleRadians);
            // then
            assertEquals(0.26794919, result, 0.00000001);
        }

        @Test
        void testSinDoubleAngle() {
            // given
            final double angleThetaRadians = Math.toRadians(15);
            // when
            final double result = MathCalc.Trigonometry.sinDoubleAngle(angleThetaRadians);
            // then
            assertEquals(0.5, result, 0.1);
        }

        @Test
        void testCosDoubleAngle() {
            // given
            final double angleThetaRadians = Math.toRadians(15);
            // when
            final double result = MathCalc.Trigonometry.cosDoubleAngle(angleThetaRadians);
            // then
            assertEquals(0.86603, result, 0.00001);
        }

        @Test
        void testTanDoubleAngle() {
            // given
            final double angleThetaRadians = Math.toRadians(15);
            // when
            final double result = MathCalc.Trigonometry.tanDoubleAngle(angleThetaRadians);
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
            final double[] results = MathCalc.Trigonometry
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
            final double[] results = MathCalc.Trigonometry
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
            final double squaredResult = MathCalc.Trigonometry.sinPowerReducing(angleRadians);
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
            final double squaredResult = MathCalc.Trigonometry.cosPowerReducing(angleRadians);
            // then
            assertEquals(0.9330127, squaredResult, 0.0000001);
        }

        @Test
        void testTanPowerReducing() {
            // given
            final double angleRadians = Math.toRadians(15);
            // when
            final double squaredResult = MathCalc.Trigonometry.tanPowerReducing(angleRadians);
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
            final double sideC = MathCalc.Trigonometry.lawOfCosSAS(sideA, sideB, angleRadians);
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
            final double[] results = MathCalc.Trigonometry.lawOfCosSSS(sideA, sideB, sideC);
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
            final double sideB = MathCalc.Trigonometry
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
            final double sideA = MathCalc.Trigonometry
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
            final double angleBetaRad = MathCalc.Trigonometry
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
            final double angleAlphaRad = MathCalc.Trigonometry
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
            final double angleGammaRad = MathCalc.Trigonometry
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
            final double angleGammaRad = MathCalc.Trigonometry
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
            final double angleSumRad = MathCalc.Trigonometry.sinAngleSum(angleAlphaRadians, angleBetaRadians);
            // then
            assertEquals(0.866, angleSumRad, 0.001);
        }

        @Test
        void testSinAngleDifference() {
            // given
            final double angleAlphaRadians = Math.toRadians(30);
            final double angleBetaRadians = Math.toRadians(90);
            // when
            final double angleDiffRad = MathCalc.Trigonometry
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
            final double angleSumRad = MathCalc.Trigonometry.cosAngleSum(angleAlphaRadians, angleBetaRadians);
            // then
            assertEquals(0.259, angleSumRad, 0.001);
        }

        @Test
        void testCosAngleDifference() {
            // given
            final double angleAlphaRadians = Math.toRadians(30);
            final double angleBetaRadians = Math.toRadians(45);
            // when
            final double angleDiffRad = MathCalc.Trigonometry
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
            final double angleSumRad = MathCalc.Trigonometry.tanAngleSum(angleAlphaRadians, angleBetaRadians);
            // then
            assertEquals(3.732, angleSumRad, 0.001);
        }

        @Test
        void testTanAngleDifference() {
            // given
            final double angleAlphaRadians = Math.toRadians(30);
            final double angleBetaRadians = Math.toRadians(45);
            // when
            final double angleDiffRad = MathCalc.Trigonometry
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
            final double angleSumRad = MathCalc.Trigonometry.cotAngleSum(angleAlphaRadians, angleBetaRadians);
            // then
            assertEquals(0.268, angleSumRad, 0.001);
        }

        @Test
        void testCotAngleDifference() {
            // given
            final double angleAlphaRadians = Math.toRadians(30);
            final double angleBetaRadians = Math.toRadians(45);
            // when
            final double angleDiffRad = MathCalc.Trigonometry
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
            final double angleSumRad = MathCalc.Trigonometry.secAngleSum(angleAlphaRadians, angleBetaRadians);
            // then
            assertEquals(3.864, angleSumRad, 0.001);
        }

        @Test
        void testSecAngleDifference() {
            // given
            final double angleAlphaRadians = Math.toRadians(30);
            final double angleBetaRadians = Math.toRadians(45);
            // when
            final double angleDiffRad = MathCalc.Trigonometry
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
            final double angleSumRad = MathCalc.Trigonometry.cscAngleSum(angleAlphaRadians, angleBetaRadians);
            // then
            assertEquals(1.035, angleSumRad, 0.001);
        }

        @Test
        void testCscAngleDifference() {
            // given
            final double angleAlphaRadians = Math.toRadians(30);
            final double angleBetaRadians = Math.toRadians(45);
            // when
            final double angleDiffRad = MathCalc.Trigonometry
                .cscAngleDifference(angleAlphaRadians, angleBetaRadians);
            // then
            assertEquals(-3.864, angleDiffRad, 0.001);
        }

        @Test
        void testFindSinWithCosAndTan() {
            // given
            final double angleAlphaRadians = Math.toRadians(60);
            // when
            final double sine = MathCalc.Trigonometry.findSinWithCosAndTan(angleAlphaRadians);
            // then
            assertEquals(0.8660254, sine, 0.0000001);
        }
    }

    @Nested
    class Seq {
        static List<Arguments> geometricSequenceArgs() {
            return List.of(
                Arguments.of(2, 4, new double[]{2, 8, 32, 128, 512})
            );
        }

        @ParameterizedTest
        @MethodSource("geometricSequenceArgs")
        void testGeometricSequence(double firstTerm, double commonRatio, double[] expectedResult) {
            // given
            final byte limit = 5;
            // when
            final double[] geometricSequence = MathCalc.Seq.geometricSequence(firstTerm, commonRatio, limit);
            // then
            assertArrayEquals(expectedResult, geometricSequence);
        }

        static List<Arguments> geometricSequenceFiniteSumArgs() {
            return List.of(
                Arguments.of(new double[]{2, 8, 32, 128, 512}, 1, 3, 168),
                Arguments.of(new double[]{2, 8, 32, 128, 512}, 2, 3, 160),
                Arguments.of(new double[]{2, 8, 32, 128, 512}, 0, 4, 682)
            );
        }

        @ParameterizedTest
        @MethodSource("geometricSequenceFiniteSumArgs")
        void testGeometricSequenceFiniteSum(double[] sequence, int startIndex, int endIndex, double expectedResult) {
            // when
            final double finiteSum = MathCalc.Seq.geometricSequenceFiniteSum(sequence, startIndex, endIndex);
            // then
            assertEquals(expectedResult, finiteSum, DELTA1);
        }

        @Test
        void testGeometricSequenceCommonRatioForNonConsecutiveTerms() {
            // given
            final double mTermPosition = 3;
            final double mTerm = 32;
            final double nTermPosition = 5;
            final double nTerm = 512;
            // when
            final double commonRatio = MathCalc.Seq
                .geometricSequenceCommonRatioForNonConsecutiveTerms(mTermPosition, mTerm, nTermPosition, nTerm);
            // then
            assertEquals(4, commonRatio, DELTA1);
        }

        @Test
        void testGeometricSequenceCommonRatio() {
            // given
            final double previousTerm = 8;
            final double nTerm = 32;
            // when
            final double commonRatio = MathCalc.Seq.geometricSequenceCommonRatio(previousTerm, nTerm);
            // then
            assertEquals(4, commonRatio, DELTA1);
        }

        static List<Arguments> arithmeticSequenceArgs() {
            return List.of(
                Arguments.of(0, 8, new double[]{0, 8, 16, 24, 32})
            );
        }

        @ParameterizedTest
        @MethodSource("arithmeticSequenceArgs")
        void testArithmeticSequence(double firstTerm, double commonDiff, double[] expectedResult) {
            // given
            final byte limit = 5;
            // when
            final double[] arithmeticSequence = MathCalc.Seq.arithmeticSequence(firstTerm, commonDiff, limit);
            // then
            assertArrayEquals(expectedResult, arithmeticSequence);
        }

        @Test
        void testArithmeticSequenceNthTerm() {
            // given
            final double firstTerm = 0;
            final double commonDiff = 8;
            final byte nthTermPosition = 64;
            // when
            final double nthTerm = MathCalc.Seq.arithmeticSequenceNthTerm(firstTerm, commonDiff, nthTermPosition);
            // then
            assertEquals(504, nthTerm, DELTA1);
        }

        @Test
        void testArithmeticSequenceSumUpToNthTerm() {
            // given
            final double firstTerm = 0;
            final double commonDiff = 8;
            final byte nthTermPosition = 64;
            // when
            final double nthTerm = MathCalc.Seq
                .arithmeticSequenceSum(firstTerm, commonDiff, nthTermPosition);
            // then
            assertEquals(16128, nthTerm, DELTA1);
        }

        @Test
        void testArithmeticSequenceSum() {
            // given
            final double firstTerm = 0;
            final double commonDiff = 8;
            final byte firstTermPosition = 3;
            final byte nthTermPosition = 64;
            // when
            final double nthTerm = MathCalc.Seq
                .arithmeticSequenceSum(firstTerm, commonDiff, firstTermPosition, nthTermPosition);
            // then
            assertEquals(16120, nthTerm, DELTA1);
        }

        static List<Arguments> convolutionArgs() {
            return List.of(
                Arguments.of(new double[]{1, 2, 3}, new double[]{4, 5, 6}, new double[]{4, 13, 28, 27, 18}),
                Arguments.of(new double[]{1, 2, 3, 4, 5}, new double[]{4, 5, 6},
                    new double[]{4, 13, 28, 43, 58, 49, 30})
            );
        }

        @ParameterizedTest
        @MethodSource("convolutionArgs")
        void testConvolution(double[] sequence1, double[] sequence2, double[] expectedResult) {
            // when
            final double[] convolvedSequence = MathCalc.Seq.convolution(sequence1, sequence2);
            // then
            assertArrayEquals(expectedResult, convolvedSequence);
        }

        @Test
        void testPascalTriangle() {
            // given
            final byte rows = 10;
            final int[][] expectedMatrix = new int[][]{
                {1},
                {1, 1},
                {1, 2, 1},
                {1, 3, 3, 1},
                {1, 4, 6, 4, 1},
                {1, 5, 10, 10, 5, 1},
                {1, 6, 15, 20, 15, 6, 1},
                {1, 7, 21, 35, 35, 21, 7, 1},
                {1, 8, 28, 56, 70, 56, 28, 8, 1},
                {1, 9, 36, 84, 126, 126, 84, 36, 9, 1},
                {1, 10, 45, 120, 210, 252, 210, 120, 45, 10, 1},
            };
            // when
            final int[][] matrix = MathCalc.Seq.pascalTriangle(rows);
            // then
            assertArrayEquals(expectedMatrix, matrix);
        }

        @ParameterizedTest
        @CsvSource({
            "0,1",
            "1,2",
            "2,4",
            "3,8",
            "4,16",
            "5,32",
            "6,64",
            "7,128",
            "8,256",
            "9,512",
            "10,1024",
        })
        void testPascalTriangleRowSum(int rowNumber, long expectedResult) {
            // when
            final long rowSum = MathCalc.Seq.pascalTriangleRowSum(rowNumber);
            // then
            assertEquals(expectedResult, rowSum);
        }

        static List<Arguments> harmonicNumberArgs() {
            return List.of(
                Arguments.of(10, 2.929),
                Arguments.of(2.1, 1.5)
            );
        }

        @ParameterizedTest
        @MethodSource("harmonicNumberArgs")
        void testHarmonicNumber(double end, double expectedResult) {
            // when
            final double number = MathCalc.Seq.harmonicNumber(end);
            // then
            assertEquals(expectedResult, number, DELTA3);
        }
    }

    @Nested
    class LinearAlgebra {
        @Test
        void testVectorMagnitude2d() {
            // given
            final int x = -3;
            final int y = 8;
            final double[] vector = {x, y};
            // when
            final double magnitude = MathCalc.LinearAlgebra.vectorMagnitude(vector);
            // then
            assertEquals(8.544, magnitude, DELTA3);
        }

        @Test
        void testVectorMagnitude3d() {
            // given
            final int x = -3;
            final int y = 8;
            final int z = 2;
            final double[] vector = {x, y, z};
            // when
            final double magnitude = MathCalc.LinearAlgebra.vectorMagnitude(vector);
            // then
            assertEquals(8.775, magnitude, DELTA3);
        }

        @Test
        void testVectorMagnitude5d() {
            // given
            final int x = 3;
            final int y = 1;
            final int z = 2;
            final int t = -3;
            final int w = 4;
            final double[] vector = {x, y, z, t, w};
            // when
            final double magnitude = MathCalc.LinearAlgebra.vectorMagnitude(vector);
            // then
            assertEquals(6.245, magnitude, DELTA3);
        }

        @Test
        void testUnitVector() {
            // given
            final int x = 8;
            final int y = -3;
            final int z = 5;
            final double[] vector = {x, y, z};
            // when
            final var unitVectorResultData = MathCalc.LinearAlgebra.unitVector(vector);
            // then
            final double vectorMagnitude = unitVectorResultData.getLeft();
            final double[] unitVectorResult = unitVectorResultData.getMiddle();
            final double unitVectorResultMagnitude = unitVectorResultData.getRight();
            assertEquals(9.9, vectorMagnitude, DELTA3);

            assertNotNull(unitVectorResult);
            assertEquals(3, unitVectorResult.length);
            assertEquals(0.80812, unitVectorResult[Constants.X_INDEX], DELTA5);
            assertEquals(-0.303046, unitVectorResult[Constants.Y_INDEX], DELTA6);
            assertEquals(0.50508, unitVectorResult[Constants.Z_INDEX], DELTA5);

            assertEquals(1, unitVectorResultMagnitude, DELTA1);
        }

        @Test
        void testVectorProjection2d() {
            // given
            final double[] vectorA = {3, 4};
            final double[] vectorB = {2, 6};
            // when
            final var projectionResult = MathCalc.LinearAlgebra.vectorProjection(vectorA, vectorB);
            // then
            assertNotNull(projectionResult);

            final double[] projection = projectionResult.getLeft();
            assertNotNull(projection);
            assertEquals(2, projection.length);
            assertEquals(1.5, projection[Constants.X_INDEX], DELTA1);
            assertEquals(4.5, projection[Constants.Y_INDEX], DELTA1);

            final double projectionFactor = projectionResult.getRight();
            assertEquals(0.75, projectionFactor, DELTA1);
        }

        @Test
        void testVectorProjection3d() {
            // given
            final double[] vectorA = {2, -3, 5};
            final double[] vectorB = {3, 6, -4};
            // when
            final var projectionResult = MathCalc.LinearAlgebra.vectorProjection(vectorA, vectorB);
            // then
            assertNotNull(projectionResult);

            final double[] projection = projectionResult.getLeft();
            assertNotNull(projection);
            assertEquals(3, projection.length);
            assertEquals(-1.5738, projection[Constants.X_INDEX], DELTA4);
            assertEquals(-3.1475, projection[Constants.Y_INDEX], DELTA4);
            assertEquals(2.0984, projection[Constants.Z_INDEX], DELTA4);

            final double projectionFactor = projectionResult.getRight();
            assertEquals(-0.5246, projectionFactor, DELTA4);
        }

        @Test
        void testFindMissingUnitVectorComponent() {
            // given
            final double x = 0.80812;
            final double y = -0.303046;
            final double[] unitVector = {x, y};
            // when
            final var unitVectorResultData = MathCalc.LinearAlgebra
                .findMissingUnitVectorComponent(unitVector);
            // then
            final double[] unitVectorResult = unitVectorResultData.getLeft();
            final double magnitude = unitVectorResultData.getRight();
            assertEquals(1, magnitude, DELTA1);

            assertNotNull(unitVectorResult);
            assertEquals(3, unitVectorResult.length);
            assertEquals(x, unitVectorResult[Constants.X_INDEX], DELTA5);
            assertEquals(y, unitVectorResult[Constants.Y_INDEX], DELTA6);
            assertEquals(0.50508, unitVectorResult[Constants.Z_INDEX], DELTA5);
        }

        @Test
        void testCrossProduct() {
            // given
            final double[] vectorA = {2, 3, 7};
            final double[] vectorB = {1, 2, 4};
            // when
            final double[] resultVector = MathCalc.LinearAlgebra.crossProduct(vectorA, vectorB);
            // then
            assertNotNull(resultVector);
            assertEquals(3, resultVector.length);
            assertEquals(-2, resultVector[Constants.X_INDEX], DELTA1);
            assertEquals(-1, resultVector[Constants.Y_INDEX], DELTA1);
            assertEquals(1, resultVector[Constants.Z_INDEX], DELTA1);
        }

        @Test
        void testUnsupportedDimsForCrossProduct() {
            // given
            final double[] vectorA = {2, 3};
            final double[] vectorB = {1, 2};
            // when
            final var exception = assertThrows(IllegalArgumentException.class,
                () -> MathCalc.LinearAlgebra.crossProduct(vectorA, vectorB));
            // then
            assertEquals("The cross product can only be applied to 3D vectors", exception.getMessage());
        }

        @Test
        void testDotProduct2d() {
            // given
            final double[] vectorA = {5, 4};
            final double[] vectorB = {2, 3};
            // when
            final double result = MathCalc.LinearAlgebra.dotProduct(vectorA, vectorB);
            // then
            assertEquals(22, result, DELTA1);
        }

        @Test
        void testDotProduct3d() {
            // given
            final double[] vectorA = {4, 5, -3};
            final double[] vectorB = {1, -2, -2};
            // when
            final double result = MathCalc.LinearAlgebra.dotProduct(vectorA, vectorB);
            // then
            assertEquals(0, result, DELTA1);
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
            final double result = MathCalc.LinearAlgebra.dotProduct(matrixA, matrixB);
            // then
            // 4*1 + 5*-2 + -3*-2 = [4, -10, 6] = 0
            // 2*5 + -1*1 + 4*3   = [10, -1, 12] = 21
            assertEquals(21, result, DELTA1);
        }

        @Test
        void testDotProduct2dAndAngleBetween() {
            // given
            final double[] vectorA = {5, 4};
            final double[] vectorB = {2, 3};
            // when
            final double[] resultData = MathCalc.LinearAlgebra.dotProductAndAngleBetween(vectorA, vectorB);
            // then
            assertNotNull(resultData);
            assertEquals(4, resultData.length);
            final double dot = resultData[Constants.ARR_1ST_INDEX];
            final double magnitudeA = resultData[Constants.ARR_2ND_INDEX];
            final double magnitudeB = resultData[Constants.ARR_3RD_INDEX];
            final double angleRadians = resultData[Constants.ARR_4TH_INDEX];
            assertEquals(22, dot, DELTA1);
            assertEquals(6.403, magnitudeA, DELTA3);
            assertEquals(3.6056, magnitudeB, DELTA4);
            assertEquals(0.30814, angleRadians, DELTA5);
        }

        @Test
        void testDotProduct3dAndAngleBetween() {
            // given
            final double[] vectorA = {4, 5, 3};
            final double[] vectorB = {1, -2, -2};
            // when
            final double[] resultData = MathCalc.LinearAlgebra.dotProductAndAngleBetween(vectorA, vectorB);
            // then
            assertNotNull(resultData);
            assertEquals(4, resultData.length);
            final double dot = resultData[Constants.ARR_1ST_INDEX];
            final double magnitudeA = resultData[Constants.ARR_2ND_INDEX];
            final double magnitudeB = resultData[Constants.ARR_3RD_INDEX];
            final double angleRadians = resultData[Constants.ARR_4TH_INDEX];
            assertEquals(-12, dot, DELTA1);
            assertEquals(7.071, magnitudeA, DELTA3);
            assertEquals(3, magnitudeB, DELTA4);
            assertEquals(2.172, angleRadians, DELTA3);
        }

        static List<Arguments> vectorAddWithMultiplesArgs() {
            return List.of(
                Arguments.of(new double[]{-3, 2, 8}, 1,
                    new double[]{2, 2, -4}, 3, new double[]{3, 8, -4}, DELTA1)
            );
        }

        @ParameterizedTest
        @MethodSource("vectorAddWithMultiplesArgs")
        void testVectorAddWithMultiples(double[] vectorA, double multipleAlpha,
                                        double[] vectorB, double multipleBeta, double[] expectedResult, double delta) {
            // when
            final double[] result = MathCalc.LinearAlgebra
                .vectorAddWithMultiples(vectorA, multipleAlpha, vectorB, multipleBeta);
            // then
            assertArrayEquals(expectedResult, result, delta);
        }

        static List<Arguments> vectorSubtractWithMultiplesArgs() {
            return List.of(
                Arguments.of(new double[]{-3, 2, 8}, 1,
                    new double[]{2, 2, -4}, 3, new double[]{-9, -4, 20}, DELTA1)
            );
        }

        @ParameterizedTest
        @MethodSource("vectorSubtractWithMultiplesArgs")
        void testVectorSubtractWithMultiples(
            double[] vectorA, double multipleAlpha, double[] vectorB, double multipleBeta,
            double[] expectedResult, double delta) {
            // when
            final double[] result = MathCalc.LinearAlgebra
                .vectorSubtractWithMultiples(vectorA, multipleAlpha, vectorB, multipleBeta);
            // then
            assertArrayEquals(expectedResult, result, delta);
        }

        static List<Arguments> determinantArgs() {
            return List.of(
                // 2x2
                Arguments.of(new double[][]{
                    {2, 5},
                    {4, 1},
                }, -18, DELTA1),
                // 3x3
                Arguments.of(new double[][]{
                    {2, 5, 1},
                    {4, 1, 7},
                    {6, 8, 3},
                }, 70, DELTA1),
                // 4x4
                Arguments.of(new double[][]{
                    {2, 5, 1, 3}, // a₁ - a₄
                    {4, 1, 7, 9}, // b₁ - b₄
                    {6, 8, 3, 2}, // c₁ - c₄
                    {7, 8, 1, 4}, // d₁ - d₄
                }, 630, DELTA1),
                Arguments.of(new double[][]{
                    // col1 + (-2) * col3
                    {0, 5, 1, 3},
                    {-10, 1, 7, 9},
                    {0, 8, 3, 2},
                    {5, 8, 1, 4},
                }, 630, DELTA1),
                // 5x5
                Arguments.of(new double[][]{
                    {2, 5, 1, 3, 1},
                    {4, 1, 7, 9, 2},
                    {6, 8, 3, 2, 3},
                    {7, 8, 1, 4, 4},
                    {1, 2, 3, 4, 5},
                }, 2940, DELTA1),
                // 6x6
                Arguments.of(new double[][]{
                    {2, 5, 1, 3, 1},
                    {4, 1, 7, 9, 2},
                    {6, 8, 3, 2, 3},
                    {7, 8, 1, 4, 4},
                    {1, 2, 3, 4, 5},
                }, 2940, DELTA1),
                // 10x10
                Arguments.of(new double[][]{
                    {2, 5, 1, 3, 1, 2, 3, 4, 5, 6},
                    {4, 1, 7, 9, 2, 3, 4, 5, 6, 7},
                    {6, 8, 3, 25, 3, 4, 15, 6, 7, 8},
                    {7, 8, 1, 4, 4, -5, 6, 7, 8, 9},
                    {1, 2, 33, 4, 5, 61, 7, 8, 9, 10},
                    {24, 50, 42, 51, 36, 77, 8, 9, 10, 11},
                    {3, 4, 5, -6, 7, 81, 99, 10, 11, 12},
                    {4, 3, -6, 7, 8, 9, 10, 11, 12, 13},
                    {5, 2, 7, 8, 9, 11, 11, 12, 13, 14},
                    {6, 1, 8, 9, 10, 11, 12, 13, 14, 25}
                }, -51_561_264_000d, DELTA1)
            );
        }

        @ParameterizedTest
        @MethodSource("determinantArgs")
        void testDeterminant(double[][] matrix, double expectedResult, double delta) {
            // when
            final double result = MathCalc.LinearAlgebra.determinant(matrix);
            // then
            assertEquals(expectedResult, result, delta);
        }

        static List<Arguments> eigenvaluesEigenvectorsOf2x2Args() {
            return List.of(
                // 2x2
                Arguments.of(new double[][]{
                    {2, 5},
                    {4, 1},
                }, Pair.of(new double[]{6, -3}, new double[][]{{1, MathCalc.FOUR_FIFTH}, {1, -1}}), DELTA2)
            );
        }

        @ParameterizedTest
        @MethodSource("eigenvaluesEigenvectorsOf2x2Args")
        void testEigenvaluesEigenvectorsOf2x2(
            double[][] matrix, Pair<double[], double[][]> expectedResult, double delta) {
            // when
            final var result = MathCalc.LinearAlgebra.eigenvaluesEigenvectorsOf2x2(matrix);
            // then
            assertNotNull(result);
            assertArrayEquals(expectedResult.getLeft(), result.getLeft(), delta);
            final double[][] expectedEigenVectors = expectedResult.getRight();
            final double[][] eigenVectors = result.getRight();
            assertEquals(expectedEigenVectors.length, eigenVectors.length);
            for (int i = 0; i < expectedEigenVectors.length; i++) {
                assertArrayEquals(expectedEigenVectors[i], eigenVectors[i], delta);
            }
        }
    }

    @Nested
    class Calculus {
        static List<Arguments> limitArgs() {
            final DoubleUnaryOperator f = x -> x * x + 5; // x²+5
            final DoubleUnaryOperator f2 = x -> 3 * x * x * x + 4 * x + 5; // 3x³+4x+5
            final DoubleUnaryOperator f3 = x -> 6 * x * x + 10 * x + 15; // 6x²+10x+15

            double exponent = 2;
            double maxLimit = 4;
            final DoubleUnaryOperator f4 = x -> (Math.pow(x, exponent) + Math.pow(maxLimit, exponent))
                / (x - maxLimit); // (xⁿ+aⁿ)/(x-a)

            final DoubleUnaryOperator f5 = Math::exp; // limₓ→₀ eˣ = 1
            final DoubleUnaryOperator f6 = x -> (Math.exp(x) - 1) / x; // limₓ→₀ (eˣ-1) / x = 1
            final DoubleUnaryOperator f7 = x -> (Math.pow(2, x) - 1) / x; // limₓ→₀ (aˣ-1) / x = logₑa
            final DoubleUnaryOperator f8 = x -> Math.pow(1 + x, 1 / x); // limₓ→₀ (1 + x)¹/ˣ = e
            final DoubleUnaryOperator f9 = x -> Math.sqrt(7 * x + 22); // limₓ→₋₃ √(7x + 22)
            // limₓ→₁ 5x³-6x²+2x-1
            final DoubleUnaryOperator f10 = x -> 5 * Math.pow(x, 3) - 6 * x * x + 2 * x - 1;
            // limₓ→₋₅ (5x+4) / (x+8)
            final DoubleUnaryOperator f11 = x -> (5 * x + 4) / (x + 8);
            // limₓ→₁ √(51-2x)
            final DoubleUnaryOperator f12 = x -> Math.sqrt(51 - 2 * x);
            final DoubleUnaryOperator f13 = x -> -4. / (2 * x - 5);
            final DoubleUnaryOperator f14 = x -> (2 * x) / (x * x - 7 * x + 6);
            final DoubleUnaryOperator f15 = x -> (3 * x) / ((x - 2) * (x + 2));
            final DoubleUnaryOperator f16 = x -> Math.pow(x - 5, 2) / Math.pow(x - 3, 2);
            // (12x³+12x²) / (x⁴-x²)
            final DoubleUnaryOperator f17 = x -> (12. * Math.pow(x, 3) + 12. * (x * x)) / (Math.pow(x, 4) - x * x);
            // limₓ→₂ (x⁴+3x³-10x²) / (x²-2x) = limₓ→₂ (x²-5x)
            final DoubleUnaryOperator f18 = x -> (Math.pow(x, 4) + 3 * Math.pow(x, 3) - 10 * x * x) / (x * x - 2 * x);
            // limₓ→₅ (12x-60) / (x²-6x+5) = limₓ→₅ 12 / (x - 1)
            final DoubleUnaryOperator f19 = x -> (12. * x - 60) / (x * x - 6 * x + 5);
            // limₓ→₃ (x³-9x) / (x²-3x) = limₓ→₅ (x+3)
            final DoubleUnaryOperator f20 = x -> (Math.pow(x, 3) - 9 * x) / (x * x - 3 * x);
            // {x² for x≤0
            // {ln(x) for x>0
            final DoubleUnaryOperator piecewiseFn1 = x -> x <= 0 ? x * x : BigDecimal.valueOf(MathCalc.Algebra.ln(x))
                .setScale(8, RoundingMode.HALF_EVEN)
                .doubleValue();
            // {sin(x) for 0≤x≤π
            // {x/π - 1 for π<x≤10
            final DoubleUnaryOperator piecewiseFn2 = x -> {
                if (x <= Math.PI) {
                    return MathCalc.Trigonometry.sin(x);
                } else if (x <= 10) {
                    return x / Math.PI - 1;
                } else {
                    throw new IllegalArgumentException();
                }
            };
            // {1/(2x) for x≤-2
            // {2ˣ for -2<x≤0
            final DoubleUnaryOperator piecewiseFn3 = x -> {
                if (x <= -2) {
                    return 1 / (2 * x);
                } else if (x <= 0) {
                    return Math.pow(2, x);
                } else {
                    throw new IllegalArgumentException();
                }
            };
            // {2ˣ-1 for -8≤x<1
            // {√x for x≥1
            final DoubleUnaryOperator piecewiseFn4 = x ->
                x <= -2 ? Math.pow(2, x) - 1 : MathCalc.Algebra.squareRoot(x);

            final DoubleUnaryOperator cot = MathCalc.Trigonometry::cot;
            final DoubleUnaryOperator sin = MathCalc.Trigonometry::sin;
            final DoubleUnaryOperator tan = MathCalc.Trigonometry::tan;

            // limₓ→₃ (x-3) / (√(4x+4)-4) = limₓ→₃ (√(4x+4)+4) / 4
            final DoubleUnaryOperator conjugateFn1 = x ->
                (x - 3) / (MathCalc.Algebra.squareRoot(4 * x + 4) - 4);

            // limₓ→₋₂ (√(3x+10)-2) / (x+2) = limₓ→₋₂ 3 / (√(3x+10)+2)
            final DoubleUnaryOperator conjugateFn2 = x ->
                (MathCalc.Algebra.squareRoot(3 * x + 10) - 2) / (x + 2);

            // limₓ→₃ (x-3) / (2-√(x+1)) = limₓ→₃ -2 - √(x+1), for x ≠ 3
            final DoubleUnaryOperator conjugateFn3 = x ->
                (x - 3) / (2 - MathCalc.Algebra.squareRoot(x + 1));

            // limₓ→₂ (3-√(5x-1)) / (x-2) = limₓ→₂ -5 / (3+√(5x-1)), for x ≠ 2
            final DoubleUnaryOperator conjugateFn4 = x ->
                (3 - MathCalc.Algebra.squareRoot(5 * x - 1)) / (x - 2);

            // limₓ→₋₃ (√(4x+28)-4) / (x+3) = limₓ→₋₃ 4 / (√(4x+28)-4), for x ≠ -3
            final DoubleUnaryOperator conjugateFn5 = x ->
                (MathCalc.Algebra.squareRoot(4 * x + 28) - 4) / (x + 3);

            // limₓ→₋₂ (3-√(6x+21)) / (x+2) = limₓ→₋₂ -6 / (3+√(6x+21)), for x ≠ -2
            final DoubleUnaryOperator conjugateFn6 = x ->
                (3 - MathCalc.Algebra.squareRoot(6 * x + 21)) / (x + 2);

            // limₓ→₁ (x-1) / (√(5x-1)-2) = limₓ→₁ (√(5x-1)+2) / 5, for x ≠ 1
            final DoubleUnaryOperator conjugateFn7 = x ->
                (x - 1) / (MathCalc.Algebra.squareRoot(5 * x - 1) - 2);

            final DoubleUnaryOperator removableDiscontinuityFn = x ->
                (x == 1) ? Double.NaN : (x * x - 1) / (x - 1);

            return List.of(
                Arguments.of("#1", f, 2., 9., DELTA1), // 2²+5=9
                Arguments.of("#2", f2, 0., 5., DELTA1), // 3(0)³+4(0)+5=5
                Arguments.of("#3", f3, 3., 99., DELTA1), // 6(3)²+10(3)+15=99
                Arguments.of("#4", f4, 3., -25., DELTA1), // (3²+4²)/(3-4)=-25
                Arguments.of("#5", f5, 0, 1., DELTA1),
                Arguments.of("#6", f6, 0, 1., DELTA1),
                Arguments.of("#7", f7, 0, MathCalc.Algebra.ln(2), DELTA1),
                Arguments.of("#8", f8, 0, Math.E, DELTA1),
                Arguments.of("#9", f9, -3., 1., DELTA1),
                Arguments.of("#10", f10, 1., 0, DELTA1),
                Arguments.of("#11", f11, -5., -7., DELTA1), // -21/3 = -7
                Arguments.of("#12", f12, 1, 7., DELTA1), // √49 = 7
                Arguments.of("#13", f13, 3, -4., DELTA1),
                Arguments.of("#14", f14, 1, Double.NaN, DELTA1),
                Arguments.of("#15", f15, 4, 1, DELTA1),
                Arguments.of("#16", f16, 3, Double.NaN, DELTA1),
                Arguments.of("#17", f17, -1, -6, DELTA1),
                Arguments.of("#18", f18, 2, 14, DELTA1),
                Arguments.of("#19", f19, 5, 3., DELTA1),
                Arguments.of("#20", f20, 3, 6., DELTA1),
                Arguments.of("piecewise#1", piecewiseFn1, 1, 0, DELTA1),
                Arguments.of("piecewise#2", piecewiseFn2, Math.PI, 0, DELTA1),
                Arguments.of("piecewise#3", piecewiseFn3, -2, Double.NaN, DELTA1), // -1/4 ≠ 1/4
                Arguments.of("piecewise#4", piecewiseFn4, 4, 2, DELTA1), // √4 = 2
                Arguments.of("trig#1", cot, MathCalc.Trigonometry.PI_OVER_4, 1, DELTA1),
                Arguments.of("trig#2", sin, MathCalc.Trigonometry.PI_OVER_6, MathCalc.ONE_HALF, DELTA1),
                Arguments.of("trig#3", cot, 0, Double.NaN, DELTA1),
                Arguments.of("trig#4", tan, 0, 0, DELTA1),
                Arguments.of("conjugate#1", conjugateFn1, 3, 2, DELTA1),
                Arguments.of("conjugate#2", conjugateFn2, -2, MathCalc.THREE_FOURTH, DELTA2),
                Arguments.of("conjugate#3", conjugateFn3, 3, -4, DELTA1),
                Arguments.of("conjugate#4", conjugateFn4, 2, -MathCalc.FIVE_SIXTH, DELTA6),
                Arguments.of("conjugate#5", conjugateFn5, -3, MathCalc.ONE_HALF, DELTA1),
                Arguments.of("conjugate#6", conjugateFn6, -2, -1, DELTA1),
                Arguments.of("conjugate#7", conjugateFn7, 1, MathCalc.FOUR_FIFTH, DELTA1),
                Arguments.of("removableDiscontinuity#1", removableDiscontinuityFn, 1., 2., DELTA1)
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("limitArgs")
        void testLimit(String testName, DoubleUnaryOperator f, double x, double expectedResult, double delta) {
            // when
            final double limit = MathCalc.Calculus.limit(f, x);
            // then
            assertEquals(expectedResult, limit, delta);
        }

        static List<Arguments> limitSumRuleArgs() {
            final DoubleUnaryOperator f = _ -> 3;
            final DoubleUnaryOperator g = _ -> 2;

            return List.of(
                Arguments.of(f, g, -3., 5., DELTA1) // 3+2=5
            );
        }

        @ParameterizedTest
        @MethodSource("limitSumRuleArgs")
        void testLimitSumRule(
            DoubleUnaryOperator f, DoubleUnaryOperator g, double x, double expectedResult, double delta) {
            // when
            final double limit = MathCalc.Calculus.limitSumRule(f, g, x);
            // then
            assertEquals(expectedResult, limit, delta);
        }

        static List<Arguments> oneSidedLimitRHSArgs() {
            final DoubleUnaryOperator f = x -> x <= -4 ? 1 / (x + 1) : Math.pow(2, x);

            return List.of(
                // rhs = limₓ→₋₄⁺ 2ˣ = 2⁻⁴ = (1/2)⁴ = 1/16
                Arguments.of(f, -4., MathCalc.ONE_SIXTEENTH, DELTA1)
            );
        }

        @ParameterizedTest
        @MethodSource("oneSidedLimitRHSArgs")
        void testOneSidedLimitRHS(
            DoubleUnaryOperator f, double x, double expectedResult, double delta) {
            // when
            final double limit = MathCalc.Calculus.oneSidedLimitRHS(f, x);
            // then
            assertEquals(expectedResult, limit, delta);
        }

        static List<Arguments> oneSidedLimitSumArgs() {
            final DoubleUnaryOperator f = x -> x < -3 ? 3 : 0;
            final DoubleUnaryOperator g = _ -> 2;
            final DoubleUnaryOperator f1 = x -> x < -2 ? 1 : 4;
            final DoubleUnaryOperator g1 = x -> x < -2 ? 4 : 1;

            return List.of(
                // lhs = limₓ→₃⁻ f(x) + limₓ→₃⁻ g(x) = 3+2=5
                // rhs = limₓ→₃⁺ f(x) + limₓ→₃⁺ g(x) = 0+2=5
                Arguments.of(f, g, -3., Double.NaN, DELTA1),
                // lhs = limₓ→₋₂⁻ f(x) + limₓ→₋₂⁻ g(x) = 1+4=5
                // rhs = limₓ→₋₂⁺ f(x) + limₓ→₋₂⁺ g(x) = 4+1=5
                Arguments.of(f1, g1, -2., 5., DELTA1)
            );
        }

        @ParameterizedTest
        @MethodSource("oneSidedLimitSumArgs")
        void testOneSidedLimitSum(
            DoubleUnaryOperator f, DoubleUnaryOperator g, double x, double expectedResult, double delta) {
            // when
            final double limit = MathCalc.Calculus.oneSidedLimitSum(f, g, x);
            // then
            assertEquals(expectedResult, limit, delta);
        }

        static List<Arguments> limitDifferenceRuleArgs() {
            final DoubleUnaryOperator f = x -> x < -1 ? 2 : 1;
            final DoubleUnaryOperator g = x -> x < -1 ? 1 : 0;

            return List.of(
                Arguments.of(f, g, -1., Double.NaN, DELTA1)
            );
        }

        @ParameterizedTest
        @MethodSource("limitDifferenceRuleArgs")
        void testLimitDifferenceRule(
            DoubleUnaryOperator f, DoubleUnaryOperator g, double x, double expectedResult, double delta) {
            // when
            final double limit = MathCalc.Calculus.limitDifferenceRule(f, g, x);
            // then
            assertEquals(expectedResult, limit, delta);
        }

        static List<Arguments> oneSidedLimitDifferenceArgs() {
            final DoubleUnaryOperator f = x -> x < -1 ? 2 : 1;
            final DoubleUnaryOperator g = x -> x < -1 ? 1 : 0;

            return List.of(
                Arguments.of(f, g, -1., 1., DELTA1)
            );
        }

        @ParameterizedTest
        @MethodSource("oneSidedLimitDifferenceArgs")
        void testOneSidedLimitDifference(
            DoubleUnaryOperator f, DoubleUnaryOperator g, double x, double expectedResult, double delta) {
            // when
            final double limit = MathCalc.Calculus.oneSidedLimitDifference(f, g, x);
            // then
            assertEquals(expectedResult, limit, delta);
        }

        @Test
        void testLimitConstantMultipleRuleWithEquationTerms() {
            // given f(x) = x² + 3x - 4
            final DoubleUnaryOperator xSquaredFn = x -> x * x;
            final DoubleUnaryOperator plus3xFn = x -> 3 * x;
            final DoubleUnaryOperator minus4Fn = _ -> -4;
            final DoubleUnaryOperator[] equationTerms = {xSquaredFn, plus3xFn, minus4Fn};
            final BiFunction<DoubleUnaryOperator[], Double, Double> f = (terms, x) ->
                Arrays.stream(terms).mapToDouble(fn -> fn.applyAsDouble(x)).sum();
            final byte constant = 5;
            final double x = 3;
            // when
            final Pair<double[], Double> result = MathCalc.Calculus
                .limitConstantMultipleRule(f, equationTerms, constant, x);
            // then
            assertNotNull(result.getLeft());
            assertNotNull(result.getRight());
            assertArrayEquals(new double[]{45, 45, -20}, result.getLeft(), DELTA1);
            assertEquals(70, result.getRight(), DELTA1);
        }

        static List<Arguments> limitConstantMultipleRuleArgs() {
            final DoubleUnaryOperator f = _ -> 2;

            return List.of(
                Arguments.of(f, 5., 3., 10., DELTA1) // 5*2=10
            );
        }

        @ParameterizedTest
        @MethodSource("limitConstantMultipleRuleArgs")
        void testLimitConstantMultipleRule(
            DoubleUnaryOperator f, double constant, double x, double expectedResult, double delta) {
            // when
            final double limit = MathCalc.Calculus.limitConstantMultipleRule(f, constant, x);
            // then
            assertEquals(expectedResult, limit, delta);
        }

        static List<Arguments> limitProductRuleArgs() {
            final DoubleUnaryOperator f = _ -> 2;
            final DoubleUnaryOperator g = _ -> 4;

            return List.of(
                Arguments.of(f, g, -1., 8., DELTA1) // limₓ→₋₁ f(x) * limₓ→₋₁ g(x) = 2*4=8
            );
        }

        @ParameterizedTest
        @MethodSource("limitProductRuleArgs")
        void testLimitProductRule(
            DoubleUnaryOperator f, DoubleUnaryOperator g, double x, double expectedResult, double delta) {
            // when
            final double limit = MathCalc.Calculus.limitProductRule(f, g, x);
            // then
            assertEquals(expectedResult, limit, delta);
        }

        static List<Arguments> oneSidedLimitProductRuleArgs() {
            final DoubleUnaryOperator f = x -> x < 3 ? 1 : 5;
            final DoubleUnaryOperator g = x -> x < 3 ? 5 : 1;
            final DoubleUnaryOperator f1 = x -> x < -2 ? 4 : 2;
            final DoubleUnaryOperator g1 = x -> 2;

            return List.of(
                // lhs = limₓ→₃⁻ f(x) * limₓ→₃⁻ g(x) = 1*5=5
                // rhs = limₓ→₃⁺ f(x) * limₓ→₃⁺ g(x) = 5*1=5
                Arguments.of(f, g, 3., 5., DELTA1),
                // lhs = limₓ→₋₂⁻ f(x) * limₓ→₋₂⁻ g(x) = 4*2=8
                // rhs = limₓ→₋₂⁺ f(x) * limₓ→₋₂⁺ g(x) = 2*2=4
                Arguments.of(f1, g1, -2., Double.NaN, DELTA1)
            );
        }

        @ParameterizedTest
        @MethodSource("oneSidedLimitProductRuleArgs")
        void testOneSidedLimitProductRule(
            DoubleUnaryOperator f, DoubleUnaryOperator g, double x, double expectedResult, double delta) {
            // when
            final double limit = MathCalc.Calculus.oneSidedLimitProductRule(f, g, x);
            // then
            assertEquals(expectedResult, limit, delta);
        }

        static List<Arguments> limitQuotientRuleArgs() {
            final DoubleUnaryOperator f = _ -> 1;
            final DoubleUnaryOperator g = _ -> 0;

            return List.of(
                Arguments.of(f, g, -1., Double.NaN, DELTA1) // limₓ→₋₁ f(x) / limₓ→₋₁ g(x) = 1/0
            );
        }

        @ParameterizedTest
        @MethodSource("limitQuotientRuleArgs")
        void testLimitQuotientRule(
            DoubleUnaryOperator f, DoubleUnaryOperator g, double x, double expectedResult, double delta) {
            // when
            final double limit = MathCalc.Calculus.limitQuotientRule(f, g, x);
            // then
            assertEquals(expectedResult, limit, delta);
        }

        static List<Arguments> limitOfCompositeFunctionsArgs() {
            final DoubleUnaryOperator f = _ -> -1;
            final DoubleUnaryOperator g = x -> x < -1 ? 3 : -1;
            final DoubleUnaryOperator g1 = _ -> 3;
            final DoubleUnaryOperator h = _ -> 1;
            final DoubleUnaryOperator g2 = x -> x < 0 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
            final DoubleUnaryOperator h1 = x -> x * 0;
            final DoubleUnaryOperator g3 = _ -> 0;
            final DoubleUnaryOperator h2 = x -> x < 0 ? 1 : -1;

            return List.of(
                // limₓ→₃ g(f(x)) = limₓ→₃ g(limₓ→₃ f(3)) = limₓ→₋₁ g(-1)
                Arguments.of(f, g, 3., Double.NaN, DELTA1),
                // limₓ→₋₁ h(g(x)) = limₓ→₋₁ h(limₓ→₋₁ g(-1)) = limₓ→₃ h(3)
                Arguments.of(g1, h, 3., 1., DELTA1),
                // limₓ→₋₁ h(g(x)) = limₓ→₋₁ h(limₓ→₋₁ g(-1)) = limₓ→∞ h(±∞)
                Arguments.of(g2, h1, -1., Double.NaN, DELTA1),
                // limₓ→₋₂ h(g(x)) = limₓ→₋₂ h(limₓ→₋₂ g(-2)) = limₓ→₀ h(0)
                Arguments.of(g3, h2, -2., Double.NaN, DELTA1)
            );
        }

        @ParameterizedTest
        @MethodSource("limitOfCompositeFunctionsArgs")
        void testLimitOfCompositeFunctions(
            DoubleUnaryOperator f, DoubleUnaryOperator g, double x, double expectedResult, double delta) {
            // when
            final double limit = MathCalc.Calculus.limitOfCompositeFunctions(f, g, x);
            // then
            assertEquals(expectedResult, limit, delta);
        }

        @Test
        void testDerivativeConstantRule() {
            // given f(x) = 3
            final DoubleUnaryOperator constantFn = _ -> 3;
            // when
            final double result = MathCalc.Calculus.derivativeConstantRule(constantFn);
            // then
            assertEquals(0, result, DELTA1);
        }

        @Test
        void testDerivativeConstantMultipleRule() {
            // given f(x) = x² + 3x - 4
            final DoubleUnaryOperator f = x -> x * x + 3 * x - 4;
            final byte constant = 5;
            final double x = 1;
            // when
            final double result = MathCalc.Calculus.derivativeConstantMultipleRule(f, constant, x);
            // then
            assertEquals(25, result, DELTA1); // 10x + 15
        }

        @Test
        void testDerivativeConstantMultipleRuleWithEquationTerms() {
            // given f(x) = x² + 3x - 4
            final DoubleUnaryOperator xSquaredFn = x -> x * x;
            final DoubleUnaryOperator plus3xFn = x -> 3 * x;
            final DoubleUnaryOperator minus4Fn = _ -> -4;
            final DoubleUnaryOperator[] equationTerms = {xSquaredFn, plus3xFn, minus4Fn};
            final BiFunction<DoubleUnaryOperator[], Double, Double> f = (terms, x) ->
                Arrays.stream(terms).mapToDouble(fn -> fn.applyAsDouble(x)).sum();
            final byte constant = 5;
            final double x = 1;
            // when
            final Pair<double[], Double> result = MathCalc.Calculus
                .derivativeConstantMultipleRule(f, equationTerms, constant, x);
            // then
            assertNotNull(result.getLeft());
            assertNotNull(result.getRight());
            assertArrayEquals(new double[]{10, 15, 0}, result.getLeft(), DELTA1);
            assertEquals(25, result.getRight(), DELTA1); // 10x + 15
        }

        @Test
        void testIntegralConstantMultipleRule() {
            // given f(x) = x² + 3x - 4
            final DoubleUnaryOperator f = x -> x * x + 3 * x - 4;
            final double lowerLimit = 2;
            final double upperLimit = 3;
            final int numberOfIntervals = 1000;
            final byte constant = 5;
            // when
            final double result = MathCalc.Calculus
                .integralConstantMultipleRule(f, lowerLimit, upperLimit, numberOfIntervals, constant);
            // then
            assertEquals(49.16, result, DELTA1); // 5x³/3 + 15x²/2 - 20x
        }

        static List<Arguments> partialDerivativeForwardDifferenceWrtXArgs() {
            // f(x,y) = 3x²y + 2y² + 7x
            final DoubleBinaryOperator f = (x, y) -> 3 * x * x * y + 2 * y * y + 7 * x;
            final DoubleBinaryOperator f2 = (x, _) -> 8 * x * x; // f(x,y) = f(x,2) = 8x²
            // f(x,y) = 1/5(x² - 2xy) + 3
            final DoubleBinaryOperator f3 = (x, y) -> MathCalc.ONE_FIFTH * (x * x - 2 * x * y) + 3;

            return List.of(
                Arguments.of(f, new double[]{2, 3}, 43, DELTA1), // 6xy + 7 = 6 * 2 * 3 + 7 = 43
                Arguments.of(f2, new double[]{3, 2}, 48, DELTA1), // 16 * 3 = 48
                Arguments.of(f3, new double[]{0, 2}, -4. / 5, DELTA5) // ∂/∂x(x²)=2x; 1/5(2(0)-2(2)*1)+0 = -4/5
            );
        }

        @ParameterizedTest
        @MethodSource("partialDerivativeForwardDifferenceWrtXArgs")
        void testPartialDerivativeForwardDifferenceWrtX(
            DoubleBinaryOperator f, double[] args, double expectedResult, double delta) {
            // given
            final double h = MathCalc.Calculus.NUMERICAL_APPROXIMATION;
            // when
            final double result = MathCalc.Calculus.partialDerivativeForwardDifferenceWrtX(
                f, args[Constants.X_INDEX], args[Constants.Y_INDEX], h);
            // then
            assertEquals(expectedResult, result, delta);
        }

        static List<Arguments> partialDerivativeDifferenceRuleWrtYArgs() {
            // f(x,y) = 3x²y + 2y² + 7x
            final DoubleBinaryOperator f = (x, y) -> 3 * x * x * y + 2 * y * y + 7 * x;
            // f(x,y) = 1/5(x² - 2xy) + 3
            final DoubleBinaryOperator f2 = (x, y) -> MathCalc.ONE_FIFTH * (x * x - 2 * x * y) + 3;
            return List.of(
                Arguments.of(f, new double[]{2, 3}, 24., DELTA1), // 3x² + 4y = 24
                Arguments.of(f2, new double[]{2, 0}, -4. / 5, DELTA3) // 1/5(0-2(2)*1)+0 = -2/5*2 = -4/5
            );
        }

        @ParameterizedTest
        @MethodSource("partialDerivativeDifferenceRuleWrtYArgs")
        void testPartialDerivativeForwardDifferenceWrtY(
            DoubleBinaryOperator f, double[] args, double expectedResult, double delta) {
            // given
            final double h = MathCalc.Calculus.NUMERICAL_APPROXIMATION;
            // when
            final double result = MathCalc.Calculus.partialDerivativeForwardDifferenceWrtY(
                f, args[Constants.X_INDEX], args[Constants.Y_INDEX], h);
            // then
            assertEquals(expectedResult, result, delta);
        }

        static List<Arguments> partialDerivativeSumRuleWrtXArgs() {
            final DoubleBinaryOperator f = (x, y) -> x * x + 3 * x * y + y * y; // f(x,y) = x² + 3xy + y²
            final DoubleBinaryOperator g = (x, y) -> 2 * x + y; // g(x,y) = 2xy
            return List.of(
                // ∂/∂x(f+g) = (2*2 + 3*3) + 2 = 13 + 2 = 15
                Arguments.of(f, g, new double[]{2, 3}, 15, DELTA1)
            );
        }

        @ParameterizedTest
        @MethodSource("partialDerivativeSumRuleWrtXArgs")
        void testPartialDerivativeSumRuleWrtX(
            DoubleBinaryOperator f, DoubleBinaryOperator g, double[] args, double expectedResult, double delta) {
            // when
            final double result = MathCalc.Calculus.partialDerivativeSumRuleWrtX(
                f, g, args[Constants.X_INDEX], args[Constants.Y_INDEX]);
            // then
            assertEquals(expectedResult, result, delta);
        }

        static List<Arguments> partialDerivativeSumRuleWrtYArgs() {
            final DoubleBinaryOperator f = (x, y) -> x * x + 3 * x * y + y * y; // f(x,y) = x² + 3xy + y²
            final DoubleBinaryOperator g = (x, y) -> 2 * x + y; // g(x,y) = 2xy
            return List.of(
                // ∂/∂y(f+g) = (3*2 + 2*3) + 1 = 12 + 1 = 13
                Arguments.of(f, g, new double[]{2, 3}, 13, DELTA1)
            );
        }

        @ParameterizedTest
        @MethodSource("partialDerivativeSumRuleWrtYArgs")
        void testPartialDerivativeSumRuleWrtY(
            DoubleBinaryOperator f, DoubleBinaryOperator g, double[] args, double expectedResult, double delta) {
            // when
            final double result = MathCalc.Calculus.partialDerivativeSumRuleWrtY(
                f, g, args[Constants.X_INDEX], args[Constants.Y_INDEX]);
            // then
            assertEquals(expectedResult, result, delta);
        }
    }
}
