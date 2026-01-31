package org.example.assistantonsbservlet.physics;

public enum SpeedUnit {
    METERS_PER_SECOND, // m/s
    METERS_PER_MINUTE, // m/min
    KILOMETERS_PER_HOUR, // km/h
    KILOMETERS_PER_SECOND, // km/s
    FEET_PER_SECOND, // ft/s
    FEET_PER_MINUTE, // ft/min
    MILES_PER_HOUR, // mph
    MILES_PER_SECOND, // mi/s
    KNOT, // kn
    LIGHT_SPEED; // c

    public static double lightSpeedToMetersPerSecond(double lightSpeed) {
        return lightSpeed * 299792543.55986;
    }
}
