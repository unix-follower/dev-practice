package org.example.assistantonsbservlet.physics;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class ToleranceCodeTest {
    private static final double DELTA1 = 0.1;

    static List<Arguments> convertCodeToCapacityArgs() {
        return List.of(
            Arguments.of(100, 10),
            Arguments.of(220, 22),
            Arguments.of(331, 330),
            Arguments.of(102, 1000),
            Arguments.of(152, 1500),
            Arguments.of(472, 4700),
            Arguments.of(562, 5600),
            Arguments.of(333, 33_000),
            Arguments.of(104, 100_000),
            Arguments.of(224, 220_000),
            Arguments.of(225, 2_200_000)
        );
    }

    @ParameterizedTest
    @MethodSource("convertCodeToCapacityArgs")
    void testConvertCodeToCapacity(int code, double expectedResultPF) {
        // when
        final double capacityPF = ToleranceCode.convertCodeToCapacity(code);
        // then
        assertEquals(expectedResultPF, capacityPF, DELTA1);
    }

    static List<Arguments> convertCapacityToCodeArgs() {
        return List.of(
            Arguments.of(1.24, 125)
        );
    }

    @ParameterizedTest
    @MethodSource("convertCapacityToCodeArgs")
    void testConvertCapacityToCode(double capacityMicroFarads, double expectedResult) {
        // given
        final double capacity = CapacityUnit.microFaradsToPicofarads(capacityMicroFarads);
        // when
        final double code = ToleranceCode.convertCapacityToCode(capacity);
        // then
        assertEquals(expectedResult, code, DELTA1);
    }

    static List<Arguments> capacityToleranceRangeArgs() {
        return List.of(
            Arguments.of(ToleranceCode.K, 100, new double[]{90, 110})
        );
    }

    @ParameterizedTest
    @MethodSource("capacityToleranceRangeArgs")
    void testCapacityToleranceRange(ToleranceCode toleranceCode, double capacityNanoFarads, double[] expectedResult) {
        // when
        final double[] toleranceRange = ToleranceCode.capacityToleranceRange(capacityNanoFarads, toleranceCode);
        // then
        assertArrayEquals(expectedResult, toleranceRange, DELTA1);
    }
}
