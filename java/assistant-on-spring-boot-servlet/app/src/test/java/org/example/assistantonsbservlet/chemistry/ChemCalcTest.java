package org.example.assistantonsbservlet.chemistry;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.config.Isotopes;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.smiles.SmilesParser;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChemCalcTest {
    @Nested
    class General {
        @Test
        void calculateProtons() {
            // given
            final int sodium = 11;
            // when
            final int protons = ChemCalc.General.protons(sodium);
            // then
            assertEquals(sodium, protons);
        }

        @Test
        void calculateAtomicNumber() {
            // given
            final int sodium = 11;
            // when
            final int atomicNumber = ChemCalc.General.atomicNumber(sodium);
            // then
            assertEquals(sodium, atomicNumber);
        }

        @Test
        void calculateNeutrons() {
            // given
            final int sodium = 11;
            final double sodiumAtomicMass = 22.99;
            // when
            final double neutrons = ChemCalc.General.neutrons(sodiumAtomicMass, sodium);
            // then
            assertEquals(12, Math.round(neutrons));
        }

        @Test
        void calculateNeutralElectrons() {
            // given
            final int sodium = 11;
            // when
            final double electrons = ChemCalc.General.neutralElectrons(sodium);
            // then
            assertEquals(sodium, electrons);
        }

        @Test
        void calculateAtomicMass() {
            // given
            final int sulfur = 16;
            // when
            final double atomicMass = ChemCalc.General.atomicMass(sulfur, sulfur);
            // then
            assertEquals(32, atomicMass);
        }

        @Test
        void calculateAverageAtomicMass() {
            // given
            final double chlorine35Mass = 34.96885;
            final double chlorine35NaturalAbundance = 75.78; // %
            final double chlorine37Mass = 36.96590;
            final double chlorine37NaturalAbundance = 24.22; // %
            final double[][] isotopes = new double[][]{
                {chlorine35Mass, chlorine35NaturalAbundance},
                {chlorine37Mass, chlorine37NaturalAbundance}
            };
            // when
            final double avgAtomicMass = ChemCalc.General.averageAtomicMass(isotopes);
            // then
            assertEquals(35.452536, avgAtomicMass, 0.000001);
        }

        @Test
        void calculateAverageAtomicMassWithCDKData() throws IOException {
            // given
            final var isotopes = Isotopes.getInstance();
            final var isotopeList = isotopes.getIsotopes("Cl");

            final var isotopeMassNums = Set.of(35, 37);
            final double[][] chlorineIsotopes = Arrays.stream(isotopeList)
                .filter(isotope -> isotopeMassNums.contains(isotope.getMassNumber()))
                .map(isotope -> new double[]{
                    isotope.getExactMass(), isotope.getNaturalAbundance(),
                })
                .toList()
                .toArray(double[][]::new);

            // when
            final double avgAtomicMass = ChemCalc.General.averageAtomicMass(chlorineIsotopes);
            // then
            final double roundedMass = BigDecimal.valueOf(avgAtomicMass)
                .setScale(4, RoundingMode.HALF_UP).doubleValue();
            assertEquals(35.452, roundedMass, 0.001);
        }

        @Test
        void calculateCharge() {
            // given
            final int sulfurProtons = 16;
            final int sulfurElectrons = 18;
            // when
            final double charge = ChemCalc.General.charge(sulfurProtons, sulfurElectrons);
            // then
            assertEquals(-2, charge);
        }

        @Test
        void calculateBondOrderH2() throws InvalidSmilesException {
            // given
            final int bondingElectrons = 2;
            final int antibondingElectrons = 0;
            // when
            final int bondOrder = ChemCalc.General.bondOrder(bondingElectrons, antibondingElectrons);
            // then
            final var smilesParser = new SmilesParser(DefaultChemObjectBuilder.getInstance());
            final var molecule = smilesParser.parseSmiles("[H][H]");
            final var expectedBondOrder = molecule.bonds().iterator().next().getOrder().numeric();
            assertEquals(expectedBondOrder, bondOrder);
        }

        @Test
        void calculateBondOrderCO() throws InvalidSmilesException {
            // given
            final int bondingElectrons = 8;
            final int antibondingElectrons = 2;
            // when
            final int bondOrder = ChemCalc.General.bondOrder(bondingElectrons, antibondingElectrons);
            // then
            final var smilesParser = new SmilesParser(DefaultChemObjectBuilder.getInstance());
            final var molecule = smilesParser.parseSmiles("[C-]#[O+]");
            final var expectedBondOrder = molecule.bonds().iterator().next().getOrder().numeric();
            assertEquals(expectedBondOrder, bondOrder);
        }
    }
}
