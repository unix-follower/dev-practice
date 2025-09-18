package org.example.assistantonsbservlet.math;

import static org.example.assistantonsbservlet.math.ArithmeticCalculator.factorial;

public interface ProbabilityTheoryCalculator {
    /**
     * @return (tp + tn) / (tp + tn + fp + fn)
     */
    static double accuracy(double truePositive, double trueNegative, double falsePositive, double falseNegative) {
        return (truePositive + trueNegative) / (truePositive + trueNegative + falsePositive + falseNegative);
    }

    /**
     * Sensitivity = TP / (TP + FN)
     * Specificity = TN / (FP + TN)
     * @return (Sensitivity × Prevalence) + (Specificity × (1 − Prevalence))
     */
    static double accuracy(double sensitivity, double specificity, double prevalence) {
        return (sensitivity * prevalence) + (specificity * (1 - prevalence));
    }

    /**
     * @return (|(Vo − Va)|/Va) × 100
     */
    static double percentError(double observedValue, double acceptedValue) {
        return (Math.abs(observedValue - acceptedValue) / acceptedValue) * 100;
    }

    /**
     * @return tp / (tp + fp)
     */
    static double precision(double truePositive, double falsePositive) {
        return truePositive / (truePositive + falsePositive);
    }

    /**
     * @return tp / (tp + fn)
     */
    static double recall(double truePositive, double falseNegative) {
        return truePositive / (truePositive + falseNegative);
    }

    static double f1Score(double precision, double recall) {
        return 2 * ((precision * recall) / (precision + recall));
    }

    /**
     * @return ₙCᵣ = n! / (r!(n - r)!)
     */
    static long combinations(long totalObjects, long sampleSize) {
        checkCombinationInputs(totalObjects, sampleSize);
        final long numerator = factorial(totalObjects);
        final long denominator = factorial(sampleSize) * factorial(totalObjects - sampleSize);
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
            throw new IllegalArgumentException("Cannot choose combinations with replacement from zero objects.");
        }
    }

    /**
     * @return Cᴿ(n, r) = (n + r - 1)! / (r!(n - 1)!)
     */
    static long combinationsWithReplacement(long totalObjects, long sampleSize) {
        checkCombinationInputs(totalObjects, sampleSize);
        final long numerator = factorial(totalObjects + sampleSize - 1);
        final long denominator = factorial(sampleSize) * factorial(totalObjects - 1);
        return numerator / denominator;
    }

    /**
     * @return ₙPᵣ = n! / ((n - r)!)
     */
    static long permutations(long totalObjects, long sampleSize) {
        checkNonNegativeCombinationInputs(totalObjects, sampleSize);
        if (sampleSize > totalObjects) {
            throw new IllegalArgumentException("sampleSize cannot be greater than totalObjects.");
        }

        final long numerator = factorial(totalObjects);
        final long denominator = factorial(totalObjects - sampleSize);
        return numerator / denominator;
    }

    /**
     * @return Pᴿ(n, r) = nʳ
     */
    static double permutationsWithReplacement(long totalObjects, long sampleSize) {
        checkNonNegativeCombinationInputs(totalObjects, sampleSize);
        return Math.pow(totalObjects, sampleSize);
    }
}
