package org.example.assistantonsbservlet.api.model;

public enum AngleUnit {
    DEGREES,
    RADIANS;

    public static double toRadians(double angle, AngleUnit unit) {
        double result = angle;
        if (AngleUnit.DEGREES == unit) {
            result = Math.toRadians(angle);
        }
        return result;
    }
}
