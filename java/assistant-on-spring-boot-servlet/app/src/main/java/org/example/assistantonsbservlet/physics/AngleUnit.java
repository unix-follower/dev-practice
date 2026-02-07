package org.example.assistantonsbservlet.physics;

import org.example.assistantonsbservlet.math.MathCalc;

public enum AngleUnit {
    DEGREE, // deg
    RADIAN, // rad
    MILLIRADIAN, // mrad
    MICRORADIAN, // μrad
    PI_RADIAN, // π rad
    GRADIAN, // gon
    MINUTE_OF_ARC, // arcmin
    SECOND_OF_ARC, // arcsec
    MILLISECOND_OF_ARC, // marcsec
    MICROSECOND_OF_ARC, // μarcsec
    NANOSECOND_OF_ARC, // narcsec
    STERADIAN, // sr
    SQUARE_DEGREES, // deg²
    SQUARE_MINUTES_OF_ARC, // sq'
    SQUARE_SECONDS_OF_ARC; // sq''

    /**
     * @return radians = π/180° * degrees
     */
    public static double degToRadians(double degrees) {
        // or Math.toRadians(degrees)
        return Math.PI / 180 * degrees;
    }

    public static double degToArcseconds(double degrees) {
        return degrees * 3600;
    }

    public static double degToArcminutes(double degrees) {
        return degrees * 60;
    }

    public static double turnsToRadians(double turns) {
        return MathCalc.Trigonometry.PI2 * turns;
    }

    public static double turnsToDegrees(double turns) {
        return turns * 360;
    }

    public static double marcsecToArcsec(double marcsec) {
        return marcsec / 1000;
    }

    public static double radiansToArcseconds(double radians) {
        return radians * 206_264.8;
    }
}
