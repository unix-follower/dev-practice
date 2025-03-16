package org.example.math;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FunctionDefinitionTest {
    private static final Logger logger = LoggerFactory.getLogger(FunctionDefinitionTest.class);

    @Test
    void test_fDefinition() {
        var x = 0;
        var f0 = FunctionDefinition.fDefinition(x);
        logger.info("f({}) = {}", x, (int) f0);
        assertEquals(-5, f0, 0);

        x = 2;
        final var f2 = FunctionDefinition.fDefinition(x);
        logger.info("f({}) = {}", x, (int) f2);
        assertEquals(11, f2, 0);

        x = -1;
        final var f_minus_1 = FunctionDefinition.fDefinition(x);
        logger.info("f({}) = {}", x, (int) f_minus_1);
        assertEquals(-4, f_minus_1, 0);
    }
}
