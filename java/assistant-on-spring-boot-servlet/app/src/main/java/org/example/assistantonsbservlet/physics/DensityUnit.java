package org.example.assistantonsbservlet.physics;

/**
 * 1 g/mL = 1000 kg/m³
 * 1 lb/cu yd ≈ 0.037 lb/cu ft;
 * 1 oz/cu in = 108 lb/cu ft
 * 1 lb/US gal ≈ 7.48 lb/cu ft
 */
public enum DensityUnit {
    KILOGRAMS_PER_CUBIC_METER, // kg/m³; 1 kg/L = 1000 kg/m³
    KILOGRAMS_PER_CUBIC_CENTIMETER, // kg/cm³
    KILOGRAMS_PER_LITER, // kg/L
    GRAMS_PER_LITER, // g/L
    MILLIGRAMS_PER_LITER, // mg/L
    GRAMS_PER_CUBIC_CENTIMETER, // g/cm³; 1 g/cm³ = 0.001 kg/m³
    MILLIGRAMS_PER_CUBIC_CENTIMETER, // mg/cm³
    GRAMS_PER_CUBIC_DECIMETER, // g/dm³
    GRAMS_PER_CUBIC_METER, // g/m³
    OUNCES_PER_CUBIC_INCH, // oz/cu in
    POUNDS_PER_CUBIC_INCH, // lb/cu in
    POUNDS_PER_CUBIC_FEET, // lb/cu ft
    POUNDS_PER_CUBIC_YARD; // lb/cu yd

    /**
     * g/cm³; at 0°C.
     * Some calculations need it in 999.87 kg/m³
     */
    public static final double WATER_DENSITY = 0.99987;
    public static final double AIR_DENSITY = 1.225; // at sea level (15°C and 101.325 kPa) in kg/m³
    public static final double EARTH_DENSITY = 5514.4; // kg/m³
}
