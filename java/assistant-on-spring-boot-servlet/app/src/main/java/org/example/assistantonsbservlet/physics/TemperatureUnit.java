package org.example.assistantonsbservlet.physics;

public enum TemperatureUnit {
    CELSIUS, // °C
    FAHRENHEIT, // °F
    KELVIN, // K
    RANKINE, // °R
    DELISLE, // °D or °De
    NEWTON, // °N
    REAUMUR, // °Ré
    ROMER; // °Rø

    /**
     * The Sun's effective temperature.
     */
    public static final short SOLAR_TEMPERATURE = 5778; // K
    public static final short NOMINAL_SOLAR_TEMPERATURE = 5772; // K

    public static double celsiusToKelvin(double celsius) {
        return celsius + 273.15;
    }

    public static double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }
}
