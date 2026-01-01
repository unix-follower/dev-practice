package org.example.assistantonsbservlet.physics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PowerUnitTest {
    private static final double DELTA1 = 0.1;
    private static final double DELTA3 = 0.001;
    private static final double DELTA6 = 0.000001;

    @Test
    void testDecibelMilliwattToWatts() {
        // given
        final byte dBmW = -30;
        // when
        final double watts = PowerUnit.decibelMilliwattToWatts(dBmW);
        // then
        assertEquals(0.000001, watts, DELTA6);
    }

    @Test
    void testWattsToDecibelMilliwatt() {
        // given
        final double watts = 0.000001;
        // when
        final double dBmW = PowerUnit.wattsToDecibelMilliwatt(watts);
        // then
        assertEquals(-30, dBmW, DELTA1);
    }

    @Test
    void testDecibel() {
        // given
        final byte inputPowerWatts = 10;
        final byte outputPowerWatts = 50;
        // when
        final double dBGainOrLoss = PowerUnit.decibel(inputPowerWatts, outputPowerWatts);
        // then
        assertEquals(6.989, dBGainOrLoss, DELTA3);
    }
}
