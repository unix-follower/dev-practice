package org.example.assistantonsbservlet.chemistry;

import java.util.Arrays;

/**
 * <table>
 *     <tr><th>Phase change</th><th>From</th><th>To</th></tr>
 *     <tr><td>Melting</td><td>Solid</td><td>Liquid</td></tr>
 *     <tr><td>Freezing</td><td>Liquid</td><td>Solid</td></tr>
 *     <tr><td>Vaporization</td><td>Liquid</td><td>Gas</td></tr>
 *     <tr><td>Condensation</td><td>Gas</td><td>Liquid</td></tr>
 *     <tr><td>Sublimation</td><td>Solid</td><td>Gas</td></tr>
 *     <tr><td>Deposition</td><td>Gas</td><td>Solid</td></tr>
 * </table>
 */
public final class ChemCalc {
    private ChemCalc() {
    }

    private static void checkProtonsNum(int protons) {
        if (protons < 1 || protons > 118) {
            throw new IllegalArgumentException("Invalid protons number");
        }
    }

    public static boolean isNeutralCharge(int protons, int electrons) {
        checkProtonsNum(protons);
        return protons == electrons;
    }

    public static boolean isCation(int protons, int electrons) {
        checkProtonsNum(protons);
        return protons > electrons;
    }

    public static boolean isAnion(int protons, int electrons) {
        checkProtonsNum(protons);
        return protons < electrons;
    }

    public static final class General {
        private General() {
        }

        /**
         * @return Number of protons = Atomic Number
         */
        public static int protons(int atomicNumber) {
            return atomicNumber;
        }

        public static int atomicNumber(int protons) {
            return protons;
        }

        public static double neutrons(double atomicMass, int atomicNumber) {
            return atomicMass - atomicNumber;
        }

        public static double electrons(int atomicNumber, double charge) {
            return atomicNumber - charge;
        }

        /**
         * @return The number of neutral electrons i.e. no charge
         */
        public static double neutralElectrons(int atomicNumber) {
            return electrons(atomicNumber, 0);
        }

        public static double atomicMass(int protons, int neutrons) {
            checkProtonsNum(protons);
            checkNeutronsNum(neutrons);
            return (double) protons + neutrons;
        }

        private static void checkNeutronsNum(int neutrons) {
            if (neutrons < 1 || neutrons > 177) {
                throw new IllegalArgumentException("Invalid neutrons number");
            }
        }

        public static int charge(int protons, int electrons) {
            return protons - electrons;
        }

        public static double averageAtomicMass(double[][] isotopes) {
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

        public static int bondOrder(int bondingElectrons, int antibondingElectrons) {
            if (bondingElectrons < 0 || antibondingElectrons < 0) {
                throw new IllegalArgumentException("The input must be non-negative");
            }

            if (bondingElectrons < antibondingElectrons) {
                throw new IllegalArgumentException(
                    "There are always more bonding electrons than antibonding electrons");
            }

            return (bondingElectrons - antibondingElectrons) / 2;
        }
    }
}
