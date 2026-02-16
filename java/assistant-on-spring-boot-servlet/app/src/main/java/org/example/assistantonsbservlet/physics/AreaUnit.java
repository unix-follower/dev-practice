package org.example.assistantonsbservlet.physics;

public enum AreaUnit {
    SQUARE_MILLIMETER, // mm²
    SQUARE_CENTIMETER, // cm²
    SQUARE_DECIMETER, // dm²
    SQUARE_METER, // m²
    SQUARE_KILOMETER, // km²
    SQUARE_INCH, // in²
    SQUARE_FEET, // ft²
    SQUARE_YARD, // yd²
    SQUARE_MILE, // mi²
    ARES, // a
    DECARES, // da
    HECTARES, // ha
    ACRES, // ac
    SOCCER_FIELDS, // sf
    CIRCULAR_MIL, // cmil
    KILOCIRCULAR_MIL; // kcmil

    public static double squareMeterToSquareFeet(double sqMeters) {
        return sqMeters * 10.7639;
    }
}
