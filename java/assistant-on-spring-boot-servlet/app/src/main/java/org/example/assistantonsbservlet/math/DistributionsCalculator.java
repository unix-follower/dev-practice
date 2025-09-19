package org.example.assistantonsbservlet.math;

import static org.example.assistantonsbservlet.math.ArithmeticCalculator.factorial;
import static org.example.assistantonsbservlet.math.ProbabilityTheoryCalculator.combinations;

public interface DistributionsCalculator {
    /**
     * @return P(X=r) = nCr * pʳ * (1 - p)ⁿ⁻ʳ
     */
    static double binomialDistribution(
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
            throw new IllegalArgumentException("Number of required successes cannot exceed the number of events.");
        }

        final long numberOfCombinations = combinations(numberOfEvents, numberOfRequiredSuccesses);

        return numberOfCombinations * Math.pow(probabilityOfOneSuccess, numberOfRequiredSuccesses) *
            Math.pow((1 - probabilityOfOneSuccess), (double) numberOfEvents - numberOfRequiredSuccesses);
    }

    /**
     * @return P(X = x) = e^(-λ) * λˣ / x!
     */
    static double poissonDistribution(long numberOfOccurrences, double rateOfSuccess) {
        if (numberOfOccurrences < 0) {
            throw new IllegalArgumentException("numberOfOccurrences must be non-negative.");
        }
        if (rateOfSuccess < 0) {
            throw new IllegalArgumentException("rateOfSuccess must be non-negative.");
        }

        return Math.pow(Math.E, -rateOfSuccess) * Math.pow(rateOfSuccess, numberOfOccurrences)
            / factorial(numberOfOccurrences);
    }

    /***
     * Calculates the probability density function (PDF) of the normal distribution:
     * @return P(x) = (1 / (σ * √(2π))) * e^(-((x - μ)²) / (2σ²))
     */
    static double normalDistribution(double mean, double standardDeviation, double rawScoreValue) {
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
    static double geometricDistributionPMF(long numberOfFailures, double probabilityOfSuccess) {
        if (numberOfFailures < 0) {
            throw new IllegalArgumentException("numberOfFailures must be non-negative.");
        }
        if (probabilityOfSuccess <= 0 || probabilityOfSuccess > 1) {
            throw new IllegalArgumentException("probabilityOfSuccess must be in the range (0, 1].");
        }

        return Math.pow(1 - probabilityOfSuccess, numberOfFailures) * probabilityOfSuccess;
    }
}
