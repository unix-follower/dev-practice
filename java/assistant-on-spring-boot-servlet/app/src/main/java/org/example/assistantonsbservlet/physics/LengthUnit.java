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
    ANGSTROM, // ångström Å
    LIGHT_YEAR, // ly
    ASTRONOMICAL_UNITS, // au
    PARSEC, // pcs
    MEGA_LIGHT_YEAR, // Mly
    MEGA_PARSEC; // Mpcs

    public static final double EARTH_EQUATORIAL_RADIUS = 6.3781e6;
    public static final double EARTH_POLAR_RADIUS = 6.3568e6;
    public static final double EARTH_RADIUS = 6.371e6;

    /**
     * The measured radius of the Sun’s photosphere.
     */
    public static final double SUN_RADIUS = 6.96342e8;
    public static final double NOMINAL_SOLAR_RADIUS = 6.957e8;

    public static double nanometersToMeters(double nm) {
        return nm / 1e9;
    }

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

    public static double kmphToMetersPerSecond(double kmPerHour) {
        return kilometersToMeters(kmPerHour) / 3600;
    }

    public static double squareMeterToSquareFeet(double sqMeters) {
        return sqMeters * 10.7639;
    }

    /**
     * Recommended by the International Astronomical Union (IAU).
     *
     * @return equatorial radius of the Earth
     */
    public static double earthRadiiToMeters(double earthRadii) {
        return earthRadii * EARTH_EQUATORIAL_RADIUS;
    }

    /**
     * This is the globally-averaged value used for most general calculations.
     */
    public static double avgEarthRadiiToMeters(double earthRadii) {
        return earthRadii * EARTH_RADIUS;
    }

    /**
     * This is the distance from the center to the North or South Pole.
     */
    public static double polarEarthRadiiToMeters(double earthRadii) {
        return earthRadii * EARTH_POLAR_RADIUS;
    }

    public static double sunRadiusToMeters(double sunRadius) {
        return sunRadius * NOMINAL_SOLAR_RADIUS;
    }

    public static double astronomicalUnitsToMeters(double au) {
        return au * 1.496e11;
    }
}
