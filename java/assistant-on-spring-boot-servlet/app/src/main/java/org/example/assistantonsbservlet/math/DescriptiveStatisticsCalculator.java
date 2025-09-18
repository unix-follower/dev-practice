package org.example.assistantonsbservlet.math;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public interface DescriptiveStatisticsCalculator {
    static double quartile1(double[] data) {
        Arrays.sort(data);
        final double q1Position = (data.length + 1) / 4.0;
        return interpolate(data, q1Position);
    }

    static double quartile2(double[] data) {
        Arrays.sort(data);
        final double q2Position = (data.length + 1) / 2.0;
        return interpolate(data, q2Position);
    }

    static double quartile3(double[] data) {
        Arrays.sort(data);
        final double q3Position = 3 * (data.length + 1) / 4.0;
        return interpolate(data, q3Position);
    }

    /**
     * @return IQR = Q3 – Q1
     */
    static double[] iqr(double[] data) {
        final double q1 = quartile1(data);
        final double q3 = quartile3(data);
        return new double[]{q3 - q1, q1, q3};
    }

    /**
     * x < Q1 - 1.5 * IQR or x > Q3 + 1.5 * IQR
     */
    static double[] outliers(double[] data) {
        if (data == null || data.length == 0) {
            return new double[0];
        }

        final double[]  interquartileRange = iqr(data);
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
    static double mse(double[] predictedValues, double[] actualValues) {
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
            throw new IllegalArgumentException("The lengths of predictedValues and actualValues must be equal.");
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
    static double rmse(double[] predictedValues, double[] actualValues) {
        return Math.sqrt(mse(predictedValues, actualValues));
    }

    /**
     * @return MAE = (1/n) * Σ|yᵢ - xᵢ|
     */
    static double mae(double[] predictedValues, double[] actualValues) {
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
    static double mean(double[] data) {
        checkNonEmpty(data);
        return Arrays.stream(data).sum() / data.length;
    }

    /**
     * @return Center = (N + 1) / 2
     */
    static double median(double[] data) {
        checkNonEmpty(data);
        return (data.length + 1.0) / 2;
    }

    static double[] mode(double[] data) {
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
    static double variance(double[] data) {
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
    static double std(double[] data) {
        return Math.sqrt(variance(data));
    }
}
