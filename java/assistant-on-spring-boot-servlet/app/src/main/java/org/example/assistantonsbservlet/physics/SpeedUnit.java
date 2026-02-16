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

    /**
     * In vacuum ~3.00 * 10⁸ m/s. Also, 300 * 10⁶ m/s
     */
    public static final int SPEED_OF_LIGHT = 299_792_458;

    public static final double SOUND_SPEED = 343.2; // Normal room temperature at 20°C; m/s or 1130 ft/s
    public static final double SOUND_SPEED_IN_DRY_AIR = 331.3; // at 0°C; m/s
    public static final double SOUND_SPEED_IN_AIR_KELVIN_REF_POINT = 273.15; // at 0°C

    public static final double EARTH_ANGULAR_VELOCITY = 0.0000727; // 2π/24h rad/s

    public static final int LIGHT_MACH_NUMBER = 873_259; // ≈ 299702547/343.2
    /**
     * International Space Station (ISS). The ISS orbits Earth with a constant speed of 7.66 km/s
     */
    public static final double ISS_MACH_NUMBER = 22.33;

    public static double lightSpeedToMetersPerSecond(double lightSpeed) {
        return lightSpeed * 299792543.55986;
    }
}
