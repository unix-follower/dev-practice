package org.example.assistantonsbservlet.physics;

public enum TemperatureUnit {
    CELSIUS, // °C
    FAHRENHEIT, // °F
    KELVIN; // K

    public static double celsiusToKelvin(double celsius) {
        return celsius + 273.15;
    }
}
