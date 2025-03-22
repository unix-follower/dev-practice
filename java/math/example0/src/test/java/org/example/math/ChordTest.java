package org.example.math;

import org.apache.commons.math3.util.Precision;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChordTest {
    @Test
    void test_slope_angle() {
//        f(x) = x²
        final var ab = Chord.slopeAngle(1, 3);
        assertEquals(4, ab);

        final var ac = Chord.slopeAngle(1, 2);
        assertEquals(3, ac);

        final var ad = Chord.slopeAngle(1, 1.5);
        assertEquals(2.5, ad);

        final var ae = Chord.slopeAngle(1, 1.1);
        assertEquals(2.1, ae);

        final var af = Chord.slopeAngle(1, 1.01);
        assertEquals(2.01, Precision.round(af, 3));
    }
}
