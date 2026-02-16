package org.example.assistantonsbservlet.physics;

public class PermeabilityUnit {
    public enum Fluid {
        SQUARE_MICROMETERS, // µm²
        SQUARE_CENTIMETERS, // cm²
        SQUARE_METERS, // m²
        DARCY, // darcys (d)
        MILLIDARCY; // millidarcys (md)

        public static double darcysToSqMeters(double darcys) {
            return darcys * 9.869233e-13;
        }
    }
}
