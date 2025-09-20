package org.example.assistantonsbservlet.chemistry;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChemCalculatorTest {
    @Nested
    class General {
        @Test
        void calculateProtons() {
            // given
            final int sodium = 11;
            // when
            final int protons = ChemCalculator.General.protons(sodium);
            // then
            assertEquals(sodium, protons);
        }

        @Test
        void calculateAtomicNumber() {
            // given
            final int sodium = 11;
            // when
            final int atomicNumber = ChemCalculator.General.atomicNumber(sodium);
            // then
            assertEquals(sodium, atomicNumber);
        }

        @Test
        void calculateNeutrons() {
            // given
            final int sodium = 11;
            final double sodiumAtomicMass = 22.99;
            // when
            final double neutrons = ChemCalculator.General.neutrons(sodiumAtomicMass, sodium);
            // then
            assertEquals(12, Math.round(neutrons));
        }

        @Test
        void calculateNeutralElectrons() {
            // given
            final int sodium = 11;
            // when
            final double electrons = ChemCalculator.General.neutralElectrons(sodium);
            // then
            assertEquals(sodium, electrons);
        }

        @Test
        void calculateAtomicMass() {
            // given
            final int sulfur = 16;
            // when
            final double atomicMass = ChemCalculator.General.atomicMass(sulfur, sulfur);
            // then
            assertEquals(32, atomicMass);
        }

        @Test
        void calculateCharge() {
            // given
            final int sulfurProtons = 16;
            final int sulfurElectrons = 18;
            // when
            final double charge = ChemCalculator.General.charge(sulfurProtons, sulfurElectrons);
            // then
            assertEquals(-2, charge);
        }
    }
}
