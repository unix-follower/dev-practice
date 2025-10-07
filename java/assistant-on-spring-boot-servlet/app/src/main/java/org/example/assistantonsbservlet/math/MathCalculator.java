package org.example.assistantonsbservlet.math;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.math.linear.EigenDecompositionImpl;
import org.apache.commons.math.linear.LUDecompositionImpl;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.SingularValueDecompositionImpl;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class MathCalculator {
    private MathCalculator() {
    }

    public static final class Arithmetic {
        private Arithmetic() {
        }

        /**
         * @return n!
         */
        public static long factorial(long number) {
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

        public static BigInteger factorial(BigInteger number) {
            var result = BigInteger.ONE;
            for (var i = BigInteger.ONE; i.compareTo(number) <= 0; i = i.add(BigInteger.ONE)) {
                result = result.multiply(i);
            }
            return result;
        }
    }

    public static final class Geometry {
        private Geometry() {
        }

        /**
         * @return π = circumference / diameter
         */
        public static double pi(double circumference, double diameter) {
            return circumference / diameter;
        }

        /**
         * @return a = πr²
         */
        public static double circleArea(double radius) {
            return Math.PI * radius * radius;
        }

        /**
         * @return a = πr² = π × (d / 2)²
         */
        public static double circleAreaOfDiameter(double diameter) {
            final double halfDiameter = diameter / 2;
            return Math.PI * halfDiameter * halfDiameter;
        }

        /**
         * @return a = c² / 4π
         */
        public static double circleAreaOfCircumference(double circumference) {
            return (circumference * circumference) / (4 * Math.PI);
        }

        /**
         * @return c = πr
         */
        public static double circleCircumference(double radius) {
            return 2 * Math.PI * radius;
        }

        /**
         * @return c = πd
         */
        public static double circleCircumferenceOfDiameter(double diameter) {
            return Math.PI * diameter;
        }

        /**
         * @return c = 2√(πa)
         */
        public static double circleCircumferenceOfArea(double area) {
            return 2 * Math.sqrt(Math.PI * area);
        }

        /**
         * @return d = 2r
         */
        public static double circleDiameter(double radius) {
            return 2 * radius;
        }

        /**
         * @return d = c / π
         */
        public static double circleDiameterOfCircumference(double circumference) {
            return circumference / Math.PI;
        }

        /**
         * @return d = 2√(a / π)
         */
        public static double circleDiameterOfArea(double area) {
            return 2 * Math.sqrt(area / Math.PI);
        }

        /**
         * @return r = d / 2
         */
        public static double circleRadius(double diameter) {
            return diameter / 2;
        }

        /**
         * @return r = c / 2π
         */
        public static double circleRadiusOfCircumference(double circumference) {
            return circumference / (Math.PI * 2);
        }

        /**
         * @return r = √(a / π)
         */
        public static double circleRadiusOfArea(double area) {
            return Math.sqrt(area / Math.PI);
        }

        /**
         * @return A = (y₁ − y₀)/(x₁ − x₀)
         */
        public static double averageRateOfChange(double startPointX, double startPointY,
                                                 double endpointX, double endpointY) {
            if (startPointX == endpointX) {
                throw new IllegalArgumentException("The x-values must not be the same to avoid division by zero.");
            }
            return (endpointY - startPointY) / (endpointX - startPointX);
        }
    }

    public static final class CoordinateGeometry {
        private CoordinateGeometry() {
        }

        public static double vectorMagnitude(double[] vector) {
            return Math.sqrt(Arrays.stream(vector).map(m -> m * m).sum());
        }

        public static Triple<Double, double[], Double> unitVector(double[] vector) {
            final double magnitude = vectorMagnitude(vector);
            final double[] result = Arrays.stream(vector).map(m -> m / magnitude).toArray();
            final double resultMagnitude = vectorMagnitude(result);
            return Triple.of(magnitude, result, resultMagnitude);
        }

        public static Pair<double[], Double> findMissingUnitVectorComponent(double[] unitVector) {
            final double sum = Arrays.stream(unitVector).map(m -> {
                if (m > 1) {
                    throw new IllegalArgumentException("Unit vector components must be less than or equal to 1");
                }
                return m;
            }).sum();
            final double[] result = Arrays.copyOf(unitVector, unitVector.length + 1);
            result[result.length - 1] = sum;
            final double resultMagnitude = vectorMagnitude(result);
            return Pair.of(result, resultMagnitude);
        }
    }

    public static final class Trigonometry {
        private Trigonometry() {
        }

        public static double csc(double opposite, double hypotenuse) {
            return hypotenuse / opposite;
        }

        public static double csc(double theta) {
            final double sinResult = Math.sin(theta);
            if (sinResult == 0) {
                throw new ArithmeticException("Division by zero");
            }
            return 1 / sinResult;
        }

        public static double sec(double adjacent, double hypotenuse) {
            return hypotenuse / adjacent;
        }

        public static double sec(double theta) {
            final double cosResult = Math.cos(theta);
            if (cosResult == 0) {
                throw new ArithmeticException("Division by zero");
            }
            return 1 / cosResult;
        }

        public static double cot(double adjacent, double opposite) {
            return adjacent / opposite;
        }

        public static double cot(double theta) {
            final double tanResult = Math.tan(theta);
            if (tanResult == 0) {
                throw new ArithmeticException("Division by zero");
            }
            return 1 / tanResult;
        }

        public static double sinDoubleAngle(double theta) {
            return 2 * Math.sin(theta) * Math.cos(theta);
        }

        public static double cosDoubleAngle(double theta) {
            return Math.cos(theta) * Math.cos(theta) - Math.sin(theta) * Math.sin(theta);
        }

        public static double tanDoubleAngle(double theta) {
            return (2 * Math.tan(theta)) / (1 - Math.tan(theta) * Math.tan(theta));
        }

        public static double sinHalfAngle(double theta) {
            return Math.sqrt((1 - Math.cos(theta)) / 2);
        }

        public static double cosHalfAngle(double theta) {
            return Math.sqrt((1 + Math.cos(theta)) / 2);
        }

        public static double tanHalfAngle(double theta) {
            return Math.sqrt((1 - Math.cos(theta)) / (1 + Math.cos(theta)));
        }

        public static double sinTripleAngleIdentity(double theta) {
            return 3 * Math.sin(theta) - 4 * Math.pow(Math.sin(theta), 3);
        }

        public static double cosTripleAngleIdentity(double theta) {
            return 4 * Math.pow(Math.cos(theta), 3) - 3 * Math.cos(theta);
        }

        public static double tanTripleAngleIdentity(double theta) {
            final double numerator = 3 * Math.tan(theta) - Math.pow(Math.tan(theta), 3);
            final double denominator = 1 - 3 * Math.tan(theta) * Math.tan(theta);
            if (denominator == 0) {
                throw new ArithmeticException("Division by zero");
            }
            return numerator / denominator;
        }

        public static double lawOfCosine(double sideA, double sideB, double angle) {
            return Math.sqrt(sideA * sideA + sideB * sideB - 2 * sideA * sideB * Math.cos(angle));
        }

        public static double sinAngleSum(double angleAlpha, double angleBeta) {
            return Math.sin(angleAlpha) * Math.cos(angleBeta) + Math.cos(angleAlpha) * Math.sin(angleBeta);
        }

        public static double sinAngleSubtract(double angleAlpha, double angleBeta) {
            return Math.sin(angleAlpha) * Math.cos(angleBeta) - Math.cos(angleAlpha) * Math.sin(angleBeta);
        }

        public static double cosAngleSum(double angleAlpha, double angleBeta) {
            return Math.cos(angleAlpha) * Math.cos(angleBeta) - Math.sin(angleAlpha) * Math.sin(angleBeta);
        }

        public static double cosAngleSubtract(double angleAlpha, double angleBeta) {
            return Math.cos(angleAlpha) * Math.cos(angleBeta) + Math.sin(angleAlpha) * Math.sin(angleBeta);
        }

        public static double tangentAngleSum(double angleAlpha, double angleBeta) {
            final double denominator = 1 - Math.tan(angleAlpha) * Math.tan(angleBeta);
            if (denominator == 0) {
                throw new ArithmeticException("Division by zero");
            }
            final double numerator = Math.tan(angleAlpha) + Math.tan(angleBeta);
            return numerator / denominator;
        }

        public static double tanAngleSubtract(double angleAlpha, double angleBeta) {
            final double denominator = 1 + Math.tan(angleAlpha) * Math.tan(angleBeta);
            if (denominator == 0) {
                throw new ArithmeticException("Division by zero");
            }
            final double numerator = Math.tan(angleAlpha) - Math.tan(angleBeta);
            return numerator / denominator;
        }

        public static double cotangentAngleSum(double angleAlpha, double angleBeta) {
            final double cotAlpha = cot(angleAlpha);
            final double cotBeta = cot(angleBeta);
            final double denominator = cotAlpha + cotBeta;
            if (denominator == 0) {
                throw new ArithmeticException("Division by zero");
            }
            return (cotAlpha * cotBeta - 1) / denominator;
        }

        public static double cotangentAngleSubtract(double angleAlpha, double angleBeta) {
            final double cotAlpha = cot(angleAlpha);
            final double cotBeta = cot(angleBeta);
            final double denominator = cotAlpha - cotBeta;
            if (denominator == 0) {
                throw new ArithmeticException("Division by zero");
            }
            return (cotAlpha * cotBeta + 1) / denominator;
        }

        public static double secantAngleSum(double angleAlpha, double angleBeta) {
            final double denominator = cot(angleAlpha) * Math.tan(angleBeta);
            if (denominator == 0) {
                throw new ArithmeticException("Division by zero");
            }
            return 1 / (Math.cos(angleAlpha) * Math.cos(angleBeta) / denominator);
        }

        public static double secantAngleSubtract(double angleAlpha, double angleBeta) {
            final double denominator = cot(angleAlpha) * Math.tan(angleBeta);
            if (denominator == 0) {
                throw new ArithmeticException("Division by zero");
            }
            return 1 / (Math.cos(angleAlpha) * Math.cos(angleBeta) / denominator);
        }

        public static double cosecantAngleSum(double angleAlpha, double angleBeta) {
            final double denominator = Math.sin(angleAlpha) * Math.cos(angleBeta)
                + Math.cos(angleAlpha) * Math.sin(angleBeta);
            if (denominator == 0) {
                throw new ArithmeticException("Division by zero");
            }
            final double numerator = Math.sin(angleAlpha) * Math.sin(angleBeta);
            return numerator / denominator;
        }

        public static double cosecantAngleSubtract(double angleAlpha, double angleBeta) {
            final double denominator = Math.sin(angleAlpha) * Math.cos(angleBeta)
                - Math.cos(angleAlpha) * Math.sin(angleBeta);
            if (denominator == 0) {
                throw new ArithmeticException("Division by zero in cosecantAngleSubtract");
            }
            final double numerator = Math.sin(angleAlpha) * Math.sin(angleBeta);
            return numerator / denominator;
        }
    }

    public static final class LinearAlgebra {
        private LinearAlgebra() {
        }

        public static double determinant(double[][] matrix) {
            final var realMatrix = MatrixUtils.createRealMatrix(matrix);
            return new LUDecompositionImpl(realMatrix).getDeterminant();
        }

        public static double l2norm(double[][] matrix) {
            final var realMatrix = MatrixUtils.createRealMatrix(matrix);
            return new SingularValueDecompositionImpl(realMatrix).getNorm();
        }

        public static double l2norm(double[] vector) {
            final var realVector  = MatrixUtils.createRealVector(vector);
            return realVector.getNorm();
        }

        public static double rank(double[][] matrix) {
            final var realMatrix = MatrixUtils.createRealMatrix(matrix);
            return new SingularValueDecompositionImpl(realMatrix).getRank();
        }

        public static double[] eigenvalues(double[][] matrix) {
            final var realMatrix = MatrixUtils.createRealMatrix(matrix);
            return new EigenDecompositionImpl(realMatrix, 0).getRealEigenvalues();
        }
    }

    public static final class Stats {
        private Stats() {
        }

        public static final class Descriptive {
            private Descriptive() {
            }

            public static double quartile1(double[] data) {
                Arrays.sort(data);
                final double q1Position = (data.length + 1) / 4.0;
                return interpolate(data, q1Position);
            }

            public static double quartile2(double[] data) {
                Arrays.sort(data);
                final double q2Position = (data.length + 1) / 2.0;
                return interpolate(data, q2Position);
            }

            public static double quartile3(double[] data) {
                Arrays.sort(data);
                final double q3Position = 3 * (data.length + 1) / 4.0;
                return interpolate(data, q3Position);
            }

            /**
             * @return IQR = Q3 – Q1
             */
            public static double[] iqr(double[] data) {
                final double q1 = quartile1(data);
                final double q3 = quartile3(data);
                return new double[]{q3 - q1, q1, q3};
            }

            /**
             * x < Q1 - 1.5 * IQR or x > Q3 + 1.5 * IQR
             */
            public static double[] outliers(double[] data) {
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
            public static double mse(double[] predictedValues, double[] actualValues) {
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
                    throw new IllegalArgumentException(
                        "The lengths of predictedValues and actualValues must be equal.");
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
            public static double rmse(double[] predictedValues, double[] actualValues) {
                return Math.sqrt(mse(predictedValues, actualValues));
            }

            /**
             * @return MAE = (1/n) * Σ|yᵢ - xᵢ|
             */
            public static double mae(double[] predictedValues, double[] actualValues) {
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
            public static double mean(double[] data) {
                checkNonEmpty(data);
                return Arrays.stream(data).sum() / data.length;
            }

            /**
             * @return Center = (N + 1) / 2
             */
            public static double median(double[] data) {
                checkNonEmpty(data);
                return (data.length + 1.0) / 2;
            }

            public static double[] mode(double[] data) {
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
            public static double variance(double[] data) {
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
            public static double std(double[] data) {
                return Math.sqrt(variance(data));
            }
        }

        public static final class ProbabilityTheory {
            private ProbabilityTheory() {
            }

            /**
             * @return (tp + tn) / (tp + tn + fp + fn)
             */
            public static double accuracy(double truePositive, double trueNegative,
                                          double falsePositive, double falseNegative) {
                return (truePositive + trueNegative) / (truePositive + trueNegative + falsePositive + falseNegative);
            }

            /**
             * Sensitivity = TP / (TP + FN)
             * Specificity = TN / (FP + TN)
             * @return (Sensitivity × Prevalence) + (Specificity × (1 − Prevalence))
             */
            public static double accuracy(double sensitivity, double specificity, double prevalence) {
                return (sensitivity * prevalence) + (specificity * (1 - prevalence));
            }

            /**
             * @return (|(Vo − Va)|/Va) × 100
             */
            public static double percentError(double observedValue, double acceptedValue) {
                return (Math.abs(observedValue - acceptedValue) / acceptedValue) * 100;
            }

            /**
             * @return tp / (tp + fp)
             */
            public static double precision(double truePositive, double falsePositive) {
                return truePositive / (truePositive + falsePositive);
            }

            /**
             * @return tp / (tp + fn)
             */
            public static double recall(double truePositive, double falseNegative) {
                return truePositive / (truePositive + falseNegative);
            }

            public static double f1Score(double precision, double recall) {
                return 2 * ((precision * recall) / (precision + recall));
            }

            /**
             * @return ₙCᵣ = n! / (r!(n - r)!)
             */
            public static long combinations(long totalObjects, long sampleSize) {
                checkCombinationInputs(totalObjects, sampleSize);
                final long numerator = Arithmetic.factorial(totalObjects);
                final long denominator = Arithmetic.factorial(sampleSize)
                    * Arithmetic.factorial(totalObjects - sampleSize);
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
                    throw new IllegalArgumentException(
                        "Cannot choose combinations with replacement from zero objects.");
                }
            }

            /**
             * @return Cᴿ(n, r) = (n + r - 1)! / (r!(n - 1)!)
             */
            public static long combinationsWithReplacement(long totalObjects, long sampleSize) {
                checkCombinationInputs(totalObjects, sampleSize);
                final long numerator = Arithmetic.factorial(totalObjects + sampleSize - 1);
                final long denominator = Arithmetic.factorial(sampleSize) * Arithmetic.factorial(totalObjects - 1);
                return numerator / denominator;
            }

            /**
             * @return ₙPᵣ = n! / ((n - r)!)
             */
            public static long permutations(long totalObjects, long sampleSize) {
                checkNonNegativeCombinationInputs(totalObjects, sampleSize);
                if (sampleSize > totalObjects) {
                    throw new IllegalArgumentException("sampleSize cannot be greater than totalObjects.");
                }

                final long numerator = Arithmetic.factorial(totalObjects);
                final long denominator = Arithmetic.factorial(totalObjects - sampleSize);
                return numerator / denominator;
            }

            /**
             * @return Pᴿ(n, r) = nʳ
             */
            public static double permutationsWithReplacement(long totalObjects, long sampleSize) {
                checkNonNegativeCombinationInputs(totalObjects, sampleSize);
                return Math.pow(totalObjects, sampleSize);
            }
        }

        public static final class Distributions {
            private Distributions() {
            }

            /**
             * @return P(X=r) = nCr * pʳ * (1 - p)ⁿ⁻ʳ
             */
            public static double binomialDistribution(
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
                    throw new IllegalArgumentException(
                        "Number of required successes cannot exceed the number of events.");
                }

                final long numberOfCombinations = ProbabilityTheory
                    .combinations(numberOfEvents, numberOfRequiredSuccesses);

                return numberOfCombinations * Math.pow(probabilityOfOneSuccess, numberOfRequiredSuccesses) *
                    Math.pow((1 - probabilityOfOneSuccess), (double) numberOfEvents - numberOfRequiredSuccesses);
            }

            /**
             * @return P(X = x) = e^(-λ) * λˣ / x!
             */
            public static double poissonDistribution(long numberOfOccurrences, double rateOfSuccess) {
                if (numberOfOccurrences < 0) {
                    throw new IllegalArgumentException("numberOfOccurrences must be non-negative.");
                }
                if (rateOfSuccess < 0) {
                    throw new IllegalArgumentException("rateOfSuccess must be non-negative.");
                }

                return Math.pow(Math.E, -rateOfSuccess) * Math.pow(rateOfSuccess, numberOfOccurrences)
                    / Arithmetic.factorial(numberOfOccurrences);
            }

            /***
             * Calculates the probability density function (PDF) of the normal distribution:
             * @return P(x) = (1 / (σ * √(2π))) * e^(-((x - μ)²) / (2σ²))
             */
            public static double normalDistribution(double mean, double standardDeviation, double rawScoreValue) {
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
            public static double geometricDistributionPMF(long numberOfFailures, double probabilityOfSuccess) {
                if (numberOfFailures < 0) {
                    throw new IllegalArgumentException("numberOfFailures must be non-negative.");
                }
                if (probabilityOfSuccess <= 0 || probabilityOfSuccess > 1) {
                    throw new IllegalArgumentException("probabilityOfSuccess must be in the range (0, 1].");
                }

                return Math.pow(1 - probabilityOfSuccess, numberOfFailures) * probabilityOfSuccess;
            }
        }
    }
}
