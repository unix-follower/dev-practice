package org.example.assistantonsbservlet.physics;

public enum LengthUnit {
    NANOMETER, // nm
    MICROMETER, // μm
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
    METER_PER_CENTIMETERS, // m/cm
    EARTH_RADII, // R⊕
    SUN_RADII, // R☉
    LIGHT_YEAR, // ly
    ASTRONOMICAL_UNITS, // au
    PARSEC, // pcs
    MEGA_LIGHT_YEAR, // Mly
    MEGA_PARSEC; // Mpcs


    public static double millimetersToMeters(double mm) {
        return mm / 1000;
    }

    public static double centimetersToMeters(double cm) {
        return cm / 100;
    }

    public static double metersToKilometers(double m) {
        return m / 1000;
    }

    public static double kilometersToMeters(double km) {
        return km * 1000;
    }

    public static double squareMeterToSquareFeet(double sqMeters) {
        return sqMeters * 10.7639;
    }
}
