package org.example.assistantonsbservlet.chemistry;

public class ChemCalculator {
    private ChemCalculator() {
    }

    public static class General {
        private General() {
        }

        /**
         * @return Number of protons = Atomic Number
         */
        static int protons(int atomicNumber) {
            return atomicNumber;
        }

        static int atomicNumber(int protons) {
            return protons;
        }

        static double neutrons(double atomicMass, int atomicNumber) {
            return atomicMass - atomicNumber;
        }

        static double electrons(int atomicNumber, double charge) {
            return atomicNumber - charge;
        }

        /**
         * @return The number of neutral electrons i.e. no charge
         */
        static double neutralElectrons(int atomicNumber) {
            return electrons(atomicNumber, 0);
        }

        static double atomicMass(int protons, int neutrons) {
            return (double) protons + neutrons;
        }

        static int charge(int protons, int electrons) {
            return protons - electrons;
        }
    }
}
