package org.example0.math

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

class FunctionDefinitionTest {
    private val logger = LoggerFactory.getLogger(getClass)

    @Test
    def test_numbers(): Any = {
        var x = 0
        val f0 = example0(x)
        logger.info("f({}) = {}", x, f0.toInt)
        assertEquals(-5, f0, 0)

        x = 2
        val f2 = example0(x)
        logger.info("f({}) = {}", x, f2.toInt)
        assertEquals(11, f2, 0)

        x = -1
        val f_minus_1 = example0(x)
        logger.info("f({}) = {}", x, f_minus_1.toInt)
        assertEquals(-4, f_minus_1, 0)
    }
}
