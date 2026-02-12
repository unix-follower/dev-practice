package org.example.assistantonsbservlet.physics;

public class ViscosityUnit {
    public enum Dynamic {
        POISE, // P; 1 P = 1 dyn⋅s/cm², 1 P = 1 g/(cm⋅s)
        CENTIPOISE, // cP
        MILLIPASCAL_SECONDS, // mPa⋅s; 1 mPa⋅s = 1 cP
        PASCAL_SECONDS, // Pa⋅s
        SLUGS_PER_FOOT_SECOND, // slugs/(ft⋅s)
        POUND_FORCE_SECONDS_PER_SQUARE_FOOT, // lbf⋅s/ft²
        POUNDS_PER_FOOT_SECOND, // lb/(ft⋅s)
        DYNES_SECOND_PER_SQUARE_CENTIMETER, // dyn⋅s/cm²
        GRAMS_PER_CENTIMETER_SECOND, // g/(cm⋅s)
        KILOGRAMS_PER_METER_SECOND, // kg/(m⋅s)
        REYNS; // reyn

        public static double poiseToCentipoise(double poises) {
            return poises * 100;
        }

        public static double poiseToMillipascalSeconds(double poises) {
            return poises * 100;
        }

        public static double poiseToPascalSeconds(double poises) {
            return poises * 0.1;
        }

        public static double poiseToSlugsPerFootSecond(double poises) {
            return poises * 0.00209;
        }

        public static double poiseToPoundForceSecondPerSquareFoot(double poises) {
            return poises * 0.00209;
        }

        public static double poiseToPoundsPerFootSecond(double poises) {
            return poises * 0.0672;
        }

        public static double poiseToKilogramPerMeterSecond(double poises) {
            return poises * 0.1;
        }

        public static double poiseToReyn(double poises) {
            return poises * 0.0000145;
        }
    }

    public enum Kinematic {
        STOKES, // St; 1 St = 1 cm²/s
        CENTISTOKES, // cSt
        SQUARE_MILLIMETERS_PER_SECOND, // mm²/s
        SQUARE_CENTIMETERS_PER_SECOND, // cm²/s
        SQUARE_METERS_PER_SECOND, // m²/s
        SQUARE_INCHES_PER_SECOND; // in²/s

        public static double stokeToCentistoke(double stokes) {
            return stokes * 100;
        }

        public static double stokeToSquareMillimetersPerSecond(double stokes) {
            return stokes * 100;
        }

        public static double stokeToSquareMetersPerSecond(double stokes) {
            return stokes * 0.0001;
        }

        public static double stokeToSquareInchesPerSecond(double stokes) {
            return stokes * 0.155;
        }

        public static double stokeToSquareFeetPerSecond(double stokes) {
            return stokes * 0.00108;
        }
    }
}
