package org.example.assistantonsbservlet.physics;

public enum LengthUnit {
    MILLIMETER, // mm
    CENTIMETER, // cm
    METER, // m
    KILOMETER, // km
    INCH, // in
    FEET, // ft
    YARD, // yd
    MILE, // mi
    NAUTICAL_MILE, // nmi
    FEET_PER_INCHES, // ft/in
    METER_PER_CENTIMETERS; // m/cm

    public static double millimetersToMeters(double mm) {
        return mm / 1000;
    }

    public static double centimetersToMeters(double centimeters) {
        return centimeters / 100;
    }
}
