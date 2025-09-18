package org.example.assistantonsbservlet.math;

public interface GeometryCalculator {
    /**
     * @return π = circumference / diameter
     */
    static double pi(double circumference, double diameter) {
        return circumference / diameter;
    }

    /**
     * @return a = πr²
     */
    static double circleArea(double radius) {
        return Math.PI * radius * radius;
    }

    /**
     * @return a = πr² = π × (d / 2)²
     */
    static double circleAreaOfDiameter(double diameter) {
        final double halfDiameter = diameter / 2;
        return Math.PI * halfDiameter * halfDiameter;
    }

    /**
     * @return a = c² / 4π
     */
    static double circleAreaOfCircumference(double circumference) {
        return (circumference * circumference) / (4 * Math.PI);
    }

    /**
     * @return c = πr
     */
    static double circleCircumference(double radius) {
        return 2 * Math.PI * radius;
    }

    /**
     * @return c = πd
     */
    static double circleCircumferenceOfDiameter(double diameter) {
        return Math.PI * diameter;
    }

    /**
     * @return c = 2√(πa)
     */
    static double circleCircumferenceOfArea(double area) {
        return 2 * Math.sqrt(Math.PI * area);
    }

    /**
     * @return d = 2r
     */
    static double circleDiameter(double radius) {
        return 2 * radius;
    }

    /**
     * @return d = c / π
     */
    static double circleDiameterOfCircumference(double circumference) {
        return circumference / Math.PI;
    }

    /**
     * @return d = 2√(a / π)
     */
    static double circleDiameterOfArea(double area) {
        return 2 * Math.sqrt(area / Math.PI);
    }

    /**
     * @return r = d / 2
     */
    static double circleRadius(double diameter) {
        return diameter / 2;
    }

    /**
     * @return r = c / 2π
     */
    static double circleRadiusOfCircumference(double circumference) {
        return circumference / (Math.PI * 2);
    }

    /**
     * @return r = √(a / π)
     */
    static double circleRadiusOfArea(double area) {
        return Math.sqrt(area / Math.PI);
    }

    /**
     * @return A = (y₁ − y₀)/(x₁ − x₀)
     */
    static double averageRateOfChange(double startPointX, double startPointY, double endpointX, double endpointY) {
        if (startPointX == endpointX) {
            throw new IllegalArgumentException("The x-values must not be the same to avoid division by zero.");
        }
        return (endpointY - startPointY) / (endpointX - startPointX);
    }
}
