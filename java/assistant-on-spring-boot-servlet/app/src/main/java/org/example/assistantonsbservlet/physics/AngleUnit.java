package org.example.assistantonsbservlet.physics;

public enum AngleUnit {
    DEGREE, // deg
    RADIAN, // rad
    MILLIRADIAN, // mrad
    MICRORADIAN, // μrad
    PI_RADIAN, // π rad
    GRADIAN, // gon
    MINUTE_OF_ARC, // arcmin
    SECOND_OF_ARC, // arcsec
    STERADIAN, // sr
    SQUARE_DEGREES, // deg²
    SQUARE_MINUTES_OF_ARC, // sq'
    SQUARE_SECONDS_OF_ARC; // sq''

    public static double degToArcseconds(double degrees) {
        return degrees * 3600;
    }

    public static double degToArcminutes(double degrees) {
        return degrees * 60;
    }
}
