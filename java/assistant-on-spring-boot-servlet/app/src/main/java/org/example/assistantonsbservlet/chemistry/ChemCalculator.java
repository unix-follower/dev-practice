package org.example.assistantonsbservlet.chemistry;

import java.util.Arrays;

public final class ChemCalculator {
    private ChemCalculator() {
    }

    public static final class General {
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
            checkProtonsNum(protons);
            checkNeutronsNum(neutrons);
            return (double) protons + neutrons;
        }

        private static void checkNeutronsNum(int neutrons) {
            if (neutrons < 1 || neutrons > 177) {
                throw new IllegalArgumentException("Invalid neutrons number");
            }
        }

        private static void checkProtonsNum(int protons) {
            if (protons < 1 || protons > 118) {
                throw new IllegalArgumentException("Invalid protons number");
            }
        }

        static int charge(int protons, int electrons) {
            return protons - electrons;
        }

        static double averageAtomicMass(double[][] isotopes) {
            if (isotopes.length < 1 || isotopes.length > 10) {
                throw new IllegalArgumentException();
            }

            final double isotopesSum = Arrays.stream(isotopes)
                .mapToDouble(isotopeData -> {
                    if (isotopeData.length < 2) {
                        throw new IllegalArgumentException();
                    }

                    final double isotopeMass = isotopeData[0];
                    final double isotopeNaturalAbundance = isotopeData[1];
                    return isotopeMass * isotopeNaturalAbundance;
                })
                .sum();
            return isotopesSum / 100;
        }
    }
}
