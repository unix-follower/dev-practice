package org.example.assistantonsbservlet.physics;

public enum LengthUnit {
    MILLIMETERS, // m
    METERS; // mm

    public static double millimetersToMeters(double mm) {
        return mm / 1000;
    }
}
