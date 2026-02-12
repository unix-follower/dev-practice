package org.example.assistantonsbservlet.physics;

public enum VolumeUnit {
    CUBIC_MILLIMETER, // mm³
    CUBIC_CENTIMETER, // cm³
    CUBIC_METER, // m³
    CUBIC_INCH, // cu in
    CUBIC_FEET, // cu ft
    CUBIC_YARD, // cu yd
    MILLILITER, // ml
    CENTILITER, // cl
    LITER, // l
    GALLON_US, // US gal
    GALLON_UK, // UK gal
    FLUID_OUNCE_US, // US fl oz
    FLUID_OUNCE_UK, // UK fl oz
    US_CUSTOMARY_CUP, // cup; 236.59
    TABLESPOON, // 15ml tbsp
    TEASPOON, // 5ml tsp
    QUART_US, // US qt
    QUART_UK, // UK qt
    PINT_US, // US pt
    PINT_UK; // UK pt

    public static final double EARTH_VOLUME = 1.083e21; // 1.08321e12 km³

    public static double cm3ToMm3(double cubicCentimeters) {
        return cubicCentimeters * 1000;
    }

    /**
     * 1g/cm³ = 1000kg/m³
     *
     * @return kg/m³
     */
    public static double gcm3ToKgm3(double gramsPerCubicCentimeter) {
        return gramsPerCubicCentimeter * 1000;
    }
}
