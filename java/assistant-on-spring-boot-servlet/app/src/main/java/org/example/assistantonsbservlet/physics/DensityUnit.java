package org.example.assistantonsbservlet.physics;

/**
 * 1 g/mL = 1000 kg/m³
 * 1 lb/cu yd ≈ 0.037 lb/cu ft;
 * 1 oz/cu in = 108 lb/cu ft
 * 1 lb/US gal ≈ 7.48 lb/cu ft
 */
public enum DensityUnit {
    KILOGRAMS_PER_CUBIC_METER, // kg/m³; 1 kg/L = 1000 kg/m³
    GRAMS_PER_CUBIC_CENTIMETER, // g/cm³; 1 g/cm³ = 0.001 kg/m³
    OUNCES_PER_CUBIC_INCH, // oz/cu in
    POUNDS_PER_CUBIC_INCH, // lb/cu in
    POUNDS_PER_CUBIC_FEET, // lb/cu ft
    POUNDS_PER_CUBIC_YARD // lb/cu yd
}
