package org.example.assistantonsbservlet.math;

import java.math.BigInteger;

public interface ArithmeticCalculator {
    /**
     * @return n!
     */
    static long factorial(long number) {
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

    static BigInteger factorial(BigInteger number) {
        var result = BigInteger.ONE;
        for (var i = BigInteger.ONE; i.compareTo(number) <= 0; i = i.add(BigInteger.ONE)) {
            result = result.multiply(i);
        }
        return result;
    }
}
