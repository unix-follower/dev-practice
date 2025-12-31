package org.example.assistantonsbservlet.physics;

public enum PowerUnit {
    MILLIWATTS, // mW
    WATTS, // W
    KILOWATTS, // kW
    MEGAWATTS, // MW
    GIGAWATTS, // GW
    TERAWATTS, // TW
    BRITISH_THERMAL_UNITS_PER_HOUR, // BTU/h
    MECHANICAL_HORSEPOWER, // hp(I)
    METRIC_HORSEPOWER; // hp(M)

    public enum PWR {
        WATT_PER_KILOGRAM, // W/kg
        KILOWATT_PER_KILOGRAM, // kW/kg
        MECHANICAL_HORSEPOWER_PER_POUND // hp(I)/lb
    }
}
