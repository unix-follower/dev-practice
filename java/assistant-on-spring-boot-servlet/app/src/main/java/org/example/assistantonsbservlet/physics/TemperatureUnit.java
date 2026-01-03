package org.example.assistantonsbservlet.physics;

public enum TemperatureUnit {
    CELSIUS, // °C
    FAHRENHEIT, // °F
    KELVIN, // K
    RANKINE, // °R
    DELISLE, // °D
    NEWTON, // °N
    REAUMUR, // °Ré
    ROMER; // °Rø

    public static double celsiusToKelvin(double celsius) {
        return celsius + 273.15;
    }
}
