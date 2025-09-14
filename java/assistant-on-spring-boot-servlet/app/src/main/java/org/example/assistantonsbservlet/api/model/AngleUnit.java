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

    public static AngleUnit getResultUnitOrRadians(AngleUnit unit) {
        var resultUnit = AngleUnit.RADIANS;
        if (unit != null) {
            resultUnit = unit;
        }
        return resultUnit;
    }
}
