package org.example.assistantonsbservlet.physics;

import org.example.assistantonsbservlet.math.Constants;
import org.example.assistantonsbservlet.math.ConversionCalculator;
import org.example.assistantonsbservlet.math.MathCalc;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PhysicsCalcTest {
    private static final double DELTA1 = 0.1;
    private static final double DELTA2 = 0.01;
    private static final double DELTA3 = 0.001;
    private static final double DELTA4 = 0.0001;
    private static final double DELTA5 = 0.00001;
    private static final double DELTA6 = 0.000001;
    private static final double DELTA7 = 0.0000001;
    private static final double DELTA9 = 0.000000001;

    private static void assertMatrixEquals(double[][] expectedResult, double[][] result, double delta) {
        assertNotNull(result);
        assertEquals(expectedResult.length, result.length);
        for (int i = 0; i < expectedResult.length; i++) {
            assertArrayEquals(expectedResult[i], result[i], delta);
        }
    }

    @Nested
    class Kinematics {
        @Test
        void calculateArrowSpeed() {
            // given
            final double ibo = 300; // ft/s
            final double drawLengthOfBow = 32; // in
            final double peakDrawWeight = 70; // lb
            final double arrowWeight = 400; // gr
            final double additionalWeightOnString = 5; // gr
            // when
            final double result = PhysicsCalc.Kinematics.arrowSpeed(ibo, drawLengthOfBow, peakDrawWeight,
                additionalWeightOnString, arrowWeight);
            // then
            assertEquals(301.67, result, 0.1);
        }

        @Test
        void calculateBallisticCoefficient() {
            // given
            final double projectileMass = ConversionCalculator.VolumeAndWeight.gramsToPounds(50);
            final double dragCoefficient = 0.51;
            final double crossSectionArea = ConversionCalculator.LengthAndArea
                .squareMillimetersToSquareInches(132.73);

            // when
            final double result = PhysicsCalc.Kinematics.ballisticCoefficient(
                projectileMass, dragCoefficient, crossSectionArea);
            // then
            assertEquals(1.0506, result, 0.1);
        }

        @Test
        void calculateMomentum() {
            // given
            final double mass = 65; // kg
            final double velocity = 2;
            // when
            final double result = PhysicsCalc.Kinematics.momentum(mass, velocity);
            // then
            assertEquals(130, result, 0.1);
        }

        @Test
        void calculateVelocityMagnitude2d() {
            // given
            final double velocityXDirection = 2;
            final double velocityYDirection = 3;
            final double[] velocity2d = new double[]{velocityXDirection, velocityYDirection};
            // when
            final double magnitude = PhysicsCalc.Kinematics.velocityMagnitude(velocity2d);
            // then
            assertEquals(3.6056, magnitude, 0.1);
        }

        @Test
        void calculateMomentumMagnitude2d() {
            // given
            final double mass = 65; // kg
            final double velocityXDirection = 2;
            final double velocityYDirection = 3;
            final double[] velocity2d = new double[]{velocityXDirection, velocityYDirection};
            // when
            final double magnitude = PhysicsCalc.Kinematics.momentumMagnitude(mass, velocity2d);
            // then
            assertEquals(234.364, magnitude, 0.1);
        }

        @Test
        void calculateVelocityMagnitude3d() {
            // given
            final double velocityXDirection = 2;
            final double velocityYDirection = 3;
            final double velocityZDirection = 1;
            final double[] velocity3d = new double[]{velocityXDirection, velocityYDirection, velocityZDirection};
            // when
            final double magnitude = PhysicsCalc.Kinematics.velocityMagnitude(velocity3d);
            // then
            assertEquals(3.742, magnitude, 0.1);
        }

        @Test
        void calculateMomentumMagnitude3d() {
            // given
            final double mass = 65; // kg
            final double velocityXDirection = 2;
            final double velocityYDirection = 3;
            final double velocityZDirection = 1;
            final double[] velocity3d = new double[]{velocityXDirection, velocityYDirection, velocityZDirection};
            // when
            final double magnitude = PhysicsCalc.Kinematics.momentumMagnitude(mass, velocity3d);
            // then
            assertEquals(243.23, magnitude, 0.1);
        }

        @Test
        void calculateVelocityOfDesiredMomentum() {
            // given
            final double momentum = 195;
            final double mass = 65; // kg
            // when
            final double velocity = PhysicsCalc.Kinematics.velocityOfDesiredMomentum(momentum, mass);
            // then
            assertEquals(3, velocity, 0.1);
        }

        @Test
        void calculateConservationOfMomentumWithUnknownCollisionType() {
            // given
            final double obj1Mass = 8; // kg
            final double obj1InitialVelocity = 10; // m/s
            final double obj1FinalVelocity = 4; // m/s
            final double stationaryObj2Mass = 4; // kg
            final double stationaryObj2InitialVelocity = 0;
            // when
            final double obj2FinalVelocity = PhysicsCalc.Kinematics.conservationOfMomentum(
                obj1Mass, obj1InitialVelocity, obj1FinalVelocity,
                stationaryObj2Mass, stationaryObj2InitialVelocity
            );
            // then
            assertEquals(12, obj2FinalVelocity, 0.1);
        }

        @Test
        void calculateConservationOfMomentumWithPerfectlyElasticCollisionType() {
            // given
            final double obj1Mass = 8; // kg
            final double obj1InitialVelocity = 10; // m/s
            final double stationaryObj2Mass = 4; // kg
            final double stationaryObj2InitialVelocity = 0;
            // when
            final double[] finalVelocities = PhysicsCalc.Kinematics.conservationOfMomentum(
                obj1Mass, obj1InitialVelocity,
                stationaryObj2Mass, stationaryObj2InitialVelocity, CollisionType.PERFECTLY_ELASTIC);
            // then
            assertNotNull(finalVelocities);
            assertEquals(2, finalVelocities.length);
            assertEquals(3.333, finalVelocities[0], 0.001);
            assertEquals(13.333, finalVelocities[1], 0.001);
        }

        @Test
        void calculateConservationOfMomentumWithPerfectlyInelasticCollisionType() {
            // given
            final double obj1Mass = 8; // kg
            final double obj1InitialVelocity = 10; // m/s
            final double stationaryObj2Mass = 4; // kg
            final double stationaryObj2InitialVelocity = 0;
            // when
            final double[] finalVelocities = PhysicsCalc.Kinematics.conservationOfMomentum(
                obj1Mass, obj1InitialVelocity,
                stationaryObj2Mass, stationaryObj2InitialVelocity, CollisionType.PERFECTLY_INELASTIC);
            // then
            assertNotNull(finalVelocities);
            assertEquals(2, finalVelocities.length);
            assertEquals(6.667, finalVelocities[0], 0.001);
            assertEquals(6.667, finalVelocities[1], 0.001);
        }

        @Test
        void calculateDisplacementUsingConstantVelocity() {
            // given
            final long time = ConversionCalculator.Time.hoursToSeconds(2);
            final double averageVelocity = 31.3889; // m/s
            // when
            final double displacement = PhysicsCalc.Kinematics.displacement(averageVelocity, time);
            // then
            assertEquals(226_000, displacement, 0.1);
        }

        @Test
        void calculateDisplacementUsingAccelerationAndInitialVelocity() {
            // given
            final long time = ConversionCalculator.Time.hoursToSeconds(2);
            final double initialVelocity = 2; // m/s
            final double acceleration = 0.5; // m/s²
            // when
            final double displacementInMeters = PhysicsCalc.Kinematics.displacement(
                acceleration, initialVelocity, time);
            // then
            assertEquals(12_974_400, displacementInMeters);
        }

        @Test
        void calculateDisplacementUsingInitialAndFinalVelocity() {
            // given
            final long time = ConversionCalculator.Time.hoursToSeconds(2);
            final double initialVelocity = 2; // m/s
            final double finalVelocity = 3602; // m/s
            // when
            final double displacementInMeters = PhysicsCalc.Kinematics.displacementOfVelocities(
                initialVelocity, finalVelocity, time);
            // then
            assertEquals(12_974_400, displacementInMeters, 0.1);
        }

        @Test
        void calculateFreeFallVelocity() {
            // given
            final int timeInSec = 8;
            final int initialVelocity = 0;
            // when
            final double velocity = PhysicsCalc.Kinematics.freeFallVelocity(initialVelocity, timeInSec);
            // then
            assertEquals(78.45, velocity, 0.01); // m/s
        }

        @Test
        void calculateFreeFallDistance() {
            // given
            final int timeInSec = 8;
            // when
            final double distanceInM = PhysicsCalc.Kinematics.freeFallDistance(timeInSec);
            // then
            assertEquals(313.81, distanceInM, 0.01);
        }

        @Test
        void calculateFreeFallDistanceWithAirResistance() {
            // given
            final double airResistanceCoef = 0.24;
            final double terminalVelocity = 55.4;
            // when
            final double dragForce = PhysicsCalc.Kinematics.freeFallDistanceWithAirResistance(
                airResistanceCoef, terminalVelocity);
            // then
            assertEquals(736.6, dragForce, 0.1);
        }

        @Test
        void calculateTerminalVelocityOfHumanSkydiver() {
            // given
            final int massInKg = 75;
            final double gravitationalAcceleration = 9.81; // m/s²
            final double densityOfFluid = 1.204; // kg/m³
            final double crossSectionalArea = 0.18; // m²
            final double dragCoef = 0.7;
            // when
            final double terminalVelocity = PhysicsCalc.Kinematics.terminalVelocity(
                massInKg, gravitationalAcceleration, densityOfFluid, dragCoef, crossSectionalArea);
            // then
            assertEquals(98.48, terminalVelocity, 0.01);
        }

        @Test
        void calculateTerminalVelocityOfBaseball() {
            // given
            final double massInKg = 0.14883;
            final double gravitationalAcceleration = 9.81; // m/s²
            final double densityOfFluid = 1.2041; // kg/m³
            final double crossSectionalArea = 0.004393; // m²
            final double dragCoef = PhysicsCalc.DragCoefficient.BASEBALL;
            // when
            final double terminalVelocity = PhysicsCalc.Kinematics.terminalVelocity(
                massInKg, gravitationalAcceleration, densityOfFluid, dragCoef, crossSectionalArea);
            // then
            assertEquals(41.056, terminalVelocity, 0.001);
        }

        @Test
        void calculateTerminalVelocityOfGolfBall() {
            // given
            final double massInKg = 0.03544;
            final double gravitationalAcceleration = 9.81; // m/s²
            final double densityOfFluid = 1.2041; // kg/m³
            final double crossSectionalArea = 0.001385442; // m²
            final double dragCoef = PhysicsCalc.DragCoefficient.GOLF_BALL;
            // when
            final double terminalVelocity = PhysicsCalc.Kinematics.terminalVelocity(
                massInKg, gravitationalAcceleration, densityOfFluid, dragCoef, crossSectionalArea);
            // then
            assertEquals(32.734, terminalVelocity, 0.001);
        }

        @Test
        void calculateWeightOfFreeFallingBody() {
            // given
            final double massInKg = 60;
            // when
            final double weight = PhysicsCalc.Kinematics.weightOfFreeFallingBody(massInKg);
            // then
            assertEquals(588.399, weight, 0.01);
        }

        @Test
        void calculateFriction() {
            // given
            final double frictionCoef = 0.13;
            final double normalForceInNewtons = 250;
            // when
            final double friction = PhysicsCalc.Kinematics.friction(frictionCoef, normalForceInNewtons);
            // then
            assertEquals(32.5, friction, 0.1);
        }

        @Test
        void calculateEnergyLostToFriction() {
            // given
            final double frictionCoef = 0.13;
            final double distanceTraveled = 100;
            final double massInKg = 0.03544;
            final double gravitationalAcceleration = 9.81; // m/s²
            final double theta = 0.1;
            // when
            final double friction = PhysicsCalc.Kinematics.energyLostToFriction(
                frictionCoef, distanceTraveled, massInKg, gravitationalAcceleration, theta);
            // then
            assertEquals(4.49, friction, 0.01);
        }

        @Test
        void calculateAircraftHeading() {
            // given
            final int trueAirspeedInKnots = 100;
            final int windSpeedInKnots = 20;
            final double courseInRadians = 0.08726646259971647;
            final double windDirectionInRadians = 1.0471975511965976;
            final double windCorrectionAngle = PhysicsCalc.Kinematics.windCorrectionAngle(
                trueAirspeedInKnots, windSpeedInKnots, courseInRadians, windDirectionInRadians);
            // when
            final double headingInRadians = PhysicsCalc.Kinematics.aircraftHeading(
                courseInRadians, windCorrectionAngle);
            // then
            assertEquals(0.252, headingInRadians, 0.001);
        }
    }

    @Nested
    class Dynamics {
        @Test
        void calculateAccelerationWithSpeedDifference() {
            // given
            final int deltaTimeInSec = 6;
            final int initialVelocity = 100; // m/s
            final int finalVelocity = 120; // m/s
            // when
            final double acceleration = PhysicsCalc.Dynamics.acceleration(
                initialVelocity, finalVelocity, deltaTimeInSec);
            // then
            assertEquals(3.333, acceleration, 0.001);
        }

        @Test
        void calculateAccelerationWithMassAndForce() {
            // given
            final int massInKg = 60;
            final int netForceInNewtons = 1000;
            // when
            final double acceleration = PhysicsCalc.Dynamics.acceleration(massInKg, netForceInNewtons);
            // then
            assertEquals(16.667, acceleration, 0.001);
        }

        @Test
        void calculateAccelerationWithDistanceTraveled() {
            // given
            final int initialVelocity = 100; // m/s
            final int distanceInM = 200;
            final int timeInSec = 6;
            // when
            final double acceleration = PhysicsCalc.Dynamics.accelerationWithDeltaDistance(
                initialVelocity, distanceInM, timeInSec);
            // then
            assertEquals(-22.22, acceleration, 0.01);
        }

        @Test
        void testNormalForceWithHorizontalSurfaceAndDownwardExternalForce() {
            // given
            final byte massInKg = 100;
            final short outsideForce = 250; // Newtons
            final double outsideForceAngle = Math.toRadians(45);
            // when
            final double normalForce = PhysicsCalc.Dynamics
                .normalForceWithHorizontalSurfaceAndDownwardExternalForce(massInKg, outsideForce, outsideForceAngle);
            // then
            assertEquals(1157.4, normalForce, DELTA1);
        }

        @Test
        void testNormalForceWithHorizontalSurfaceAndUpwardExternalForce() {
            // given
            final byte massInKg = 100;
            final short outsideForce = 250; // Newtons
            final double outsideForceAngle = Math.toRadians(45);
            // when
            final double normalForce = PhysicsCalc.Dynamics
                .normalForceWithHorizontalSurfaceAndUpwardExternalForce(massInKg, outsideForce, outsideForceAngle);
            // then
            assertEquals(803.9, normalForce, DELTA1);
        }

        @Test
        void testNormalForceWithHorizontalSurface() {
            // given
            final double massInKg = 0.6;
            // when
            final double normalForce = PhysicsCalc.Dynamics.normalForceWithHorizontalSurface(massInKg);
            // then
            assertEquals(5.884, normalForce, DELTA1);
        }

        @Test
        void testNormalForceWithInclinedSurface() {
            // given
            final double massInKg = 2.5;
            final double inclinationAngle = Math.toRadians(15);
            // when
            final double normalForce = PhysicsCalc.Dynamics.normalForceWithInclinedSurface(massInKg, inclinationAngle);
            // then
            assertEquals(23.68, normalForce, DELTA2);
        }

        static List<Arguments> netForceArgs() {
            return List.of(
                Arguments.of(new double[][]{{10, 0}, {15, Math.PI}}, new double[]{-5, 0, 5, Math.PI}, DELTA1),
                Arguments.of(new double[][]{
                    {10, 0}, {15, Math.PI}, {2, Math.toRadians(5)}, {4, Math.toRadians(10)}, {8, Math.toRadians(15)},
                    {16, Math.toRadians(20)}, {32, Math.toRadians(25)}, {64, Math.toRadians(30)},
                    {128, Math.toRadians(35)}, {256, Math.toRadians(40)},
                }, new double[]{409.08, 291.91, 502.55, Math.toRadians(35.51)}, DELTA2)
            );
        }

        @ParameterizedTest
        @MethodSource("netForceArgs")
        void testNetForce(double[][] forces, double[] expectedResult, double delta) {
            // when
            final double[] resultantForce = PhysicsCalc.Dynamics.netForce(forces);
            // then
            assertArrayEquals(expectedResult, resultantForce, delta);
        }
    }

    @Nested
    class Electromagnetism {
        static List<Arguments> conductivityOfArgs() {
            return List.of(
                Arguments.of("Ag", 62_893_082, DELTA9),
                Arguments.of("Annealed Cu", 58_479_532, DELTA9),
                Arguments.of("Au", 40_983_607, DELTA9),
                Arguments.of("Al", 37_735_849, DELTA9),
                Arguments.of("W", 17_857_143, DELTA9),
                Arguments.of("Li", 10_775_862, DELTA9),
                Arguments.of("Fe", 10_298_661, DELTA9),
                Arguments.of("Pt", 9_433_962, DELTA9),
                Arguments.of("Hg", 1_020_408, DELTA9),
                Arguments.of("C", 1_538.5, DELTA9),
                Arguments.of("Si", 0.0015625, DELTA9),
                Arguments.of("SiO2", 1e-13, DELTA9),
                Arguments.of("C2F4", 1e-24, DELTA9),
                Arguments.of("Cu", 59_523_810, DELTA9)
            );
        }

        @ParameterizedTest
        @MethodSource("conductivityOfArgs")
        void testConductivityOf(String chemicalSymbol, double expectedResultSiemensPerMeter, double delta) {
            // when
            final double resistivity = PhysicsCalc.Electromagnetism.conductivityOf(chemicalSymbol);
            // then
            assertEquals(expectedResultSiemensPerMeter, resistivity, delta);
        }

        static List<Arguments> resistivityOfArgs() {
            return List.of(
                Arguments.of("Ag", 1.59e-8, DELTA9),
                Arguments.of("Annealed Cu", 1.71e-8, DELTA9),
                Arguments.of("Au", 2.44e-8, DELTA9),
                Arguments.of("Al", 2.65e-8, DELTA9),
                Arguments.of("W", 5.6e-8, DELTA9),
                Arguments.of("Li", 9.28e-8, DELTA9),
                Arguments.of("Fe", 9.71e-8, DELTA9),
                Arguments.of("Pt", 1.06e-7, DELTA9),
                Arguments.of("Hg", 9.8e-7, DELTA9),
                Arguments.of("C", 0.00065, DELTA9),
                Arguments.of("Si", 640, DELTA1),
                Arguments.of("SiO2", 10_000_000_000_000d, DELTA1),
                Arguments.of("C2F4", 1e+24, DELTA1),
                Arguments.of("Cu", 1.68e-8, DELTA1)
            );
        }

        @ParameterizedTest
        @MethodSource("resistivityOfArgs")
        void testResistivityOf(String chemicalSymbol, double expectedResultOhmMeters, double delta) {
            // when
            final double resistivity = PhysicsCalc.Electromagnetism.resistivityOf(chemicalSymbol);
            // then
            assertEquals(expectedResultOhmMeters, resistivity, delta);
        }

        @Test
        void testConductivityToResistivity() {
            // given
            final double conductivity = 105.2;
            // when
            final double resistivity = PhysicsCalc.Electromagnetism.conductivityToResistivity(conductivity);
            // then
            assertEquals(0.009506, resistivity, DELTA6);
        }

        @Test
        void testResistivityToConductivity() {
            // given
            final double resistivity = 0.009506;
            // when
            final double conductivity = PhysicsCalc.Electromagnetism.resistivityToConductivity(resistivity);
            // then
            assertEquals(105.2, conductivity, DELTA1);
        }
    }

    @Nested
    class Electronics {
        static List<Arguments> electricalChargeInCapacitorArgs() {
            return List.of(
                Arguments.of(220, 25, 5_500, DELTA1)
            );
        }

        @ParameterizedTest
        @MethodSource("electricalChargeInCapacitorArgs")
        void testElectricalChargeInCapacitor(
            double capacitanceMicroFarads, double voltageVolts, double expectedResult, double delta) {
            // when
            final double capacity = PhysicsCalc.Electronics
                .electricalChargeInCapacitor(capacitanceMicroFarads, voltageVolts);
            // then
            assertEquals(expectedResult, capacity, delta);
        }

        static List<Arguments> energyStoredInCapacitorArgs() {
            return List.of(
                Arguments.of(300, 20, 0.06, DELTA2),
                Arguments.of(0.00012, 1.5, 1.35e-10, DELTA9)
            );
        }

        @ParameterizedTest
        @MethodSource("energyStoredInCapacitorArgs")
        void testEnergyStoredInCapacitor(
            double capacityMicroFarads, double voltageVolts, double expectedResult, double delta) {
            // given
            final double farads = CapacityUnit.microFaradsToFarads(capacityMicroFarads);
            // when
            final double storedEnergyInJ = PhysicsCalc.Electronics.energyStoredInCapacitor(farads, voltageVolts);
            // then
            assertEquals(expectedResult, storedEnergyInJ, delta);
        }

        static List<Arguments> capacitorSizeArgs() {
            return List.of(
                Arguments.of(64, 16, MathCalc.ONE_HALF, DELTA1)
            );
        }

        @ParameterizedTest
        @MethodSource("capacitorSizeArgs")
        void testCapacitorSize(
            double startupEnergyMicroJoules, double voltageVolts, double expectedResult, double delta) {
            // when
            final double capacitorSize = PhysicsCalc.Electronics.capacitorSize(startupEnergyMicroJoules, voltageVolts);
            // then
            assertEquals(expectedResult, capacitorSize, delta);
        }

        static List<Arguments> ohmsLawPowerArgs() {
            return List.of(
                Arguments.of(6, 18, 108, DELTA1)
            );
        }

        @ParameterizedTest
        @MethodSource("ohmsLawPowerArgs")
        void testOhmsLawPower(double currentAmperes, double voltageVolts, double expectedResultW, double delta) {
            // when
            final double powerWatts = PhysicsCalc.Electronics.ohmsLawPower(currentAmperes, voltageVolts);
            // then
            assertEquals(expectedResultW, powerWatts, delta);
        }

        static List<Arguments> capacitorInSeriesArgs() {
            return List.of(
                Arguments.of(new double[]{2000, 5, 6, 0.2}, 0.1863, DELTA4)
            );
        }

        @ParameterizedTest
        @MethodSource("capacitorInSeriesArgs")
        void testCapacitorInSeries(double[] capacitorsMicroFarads, double expectedResultInMicroFarads, double delta) {
            // when
            final double capacitanceInSeries = PhysicsCalc.Electronics.capacitorInSeries(capacitorsMicroFarads);
            // then
            assertEquals(expectedResultInMicroFarads, capacitanceInSeries, delta);
        }

        @Test
        void testCapacitorInParallel() {
            // given
            final double[] capacitors = new double[]{
                CapacityUnit.milliFaradsToFarads(30),
                CapacityUnit.milliFaradsToFarads(0.5),
                CapacityUnit.milliFaradsToFarads(6),
                CapacityUnit.milliFaradsToFarads(0.75)
            };
            // when
            final double capacitance = PhysicsCalc.Electronics.capacitorInParallel(capacitors);
            // then
            assertEquals(37.25, CapacityUnit.faradsToMilliFarads(capacitance), DELTA2);
        }

        static List<Arguments> resistorBand4ValueArgs() {
            return List.of(
                Arguments.of(ResistorColorCode.GREEN, ResistorColorCode.RED, ResistorColorCode.MultiplierBand.RED,
                    ResistorColorCode.Tolerance.GOLD, new double[]{5200, 4940, 5460}, DELTA2)
            );
        }

        @ParameterizedTest
        @MethodSource("resistorBand4ValueArgs")
        void testResistorBand4Value(
            ResistorColorCode band1, ResistorColorCode band2,
            ResistorColorCode.MultiplierBand multiplierBand, ResistorColorCode.Tolerance tolerance,
            double[] expectedResult, double delta) {
            // when
            final double[] resistorValues = PhysicsCalc.Electronics
                .resistorBand4Value(band1, band2, multiplierBand, tolerance);
            // then
            assertArrayEquals(expectedResult, resistorValues, delta);
        }

        static List<Arguments> resistorBand5ValueArgs() {
            return List.of(
                Arguments.of(ResistorColorCode.GREEN, ResistorColorCode.RED, ResistorColorCode.BLUE,
                    ResistorColorCode.MultiplierBand.RED, ResistorColorCode.Tolerance.GOLD,
                    new double[]{52600, 49970, 55230}, DELTA1)
            );
        }

        @ParameterizedTest
        @MethodSource("resistorBand5ValueArgs")
        void testResistorBand5Value(
            ResistorColorCode band1, ResistorColorCode band2, ResistorColorCode band3,
            ResistorColorCode.MultiplierBand multiplierBand, ResistorColorCode.Tolerance tolerance,
            double[] expectedResult, double delta) {
            // when
            final double[] resistorValues = PhysicsCalc.Electronics
                .resistorBand5Value(band1, band2, band3, multiplierBand, tolerance);
            // then
            assertArrayEquals(expectedResult, resistorValues, delta);
        }

        static List<Arguments> resistorBand6ValueArgs() {
            return List.of(
                Arguments.of(new ResistorColorCode[]{
                        ResistorColorCode.GREEN, ResistorColorCode.BLACK, ResistorColorCode.BLACK},
                    ResistorColorCode.MultiplierBand.GOLD, ResistorColorCode.Tolerance.BLUE, ResistorColorCode.TCR.RED,
                    25, 50, new double[]{50.0625, 49.875, 50.125}, DELTA4)
            );
        }

        @ParameterizedTest
        @MethodSource("resistorBand6ValueArgs")
        void testResistorBand6Value(
            ResistorColorCode[] bands,
            ResistorColorCode.MultiplierBand multiplierBand, ResistorColorCode.Tolerance tolerance,
            ResistorColorCode.TCR temperatureCoeff, double temperatureStart, double temperatureEnd,
            double[] expectedResult, double delta) {
            // when
            final double[] resistorValues = PhysicsCalc.Electronics.resistorBand6Value(bands, multiplierBand, tolerance,
                temperatureCoeff, temperatureStart, temperatureEnd);
            // then
            assertArrayEquals(expectedResult, resistorValues, delta);
        }

        @Test
        void testInductorEnergy() {
            // given
            final double inductanceHenries = InductanceUnit.microHenriesToHenries(20);
            final double currentAmperes = 0.3;
            // when
            final double storedEnergy = PhysicsCalc.Electronics.inductorEnergy(inductanceHenries, currentAmperes);
            // then
            assertEquals(9e-7, storedEnergy, DELTA7);
        }

        @Test
        void testApparentAcPowerSinglePhase() {
            // given
            final byte currentA = 100;
            final short voltageV = 220;
            // when
            final double apparentPower = PhysicsCalc.Electronics.apparentPowerACSinglePhase(currentA, voltageV);
            // then
            assertEquals(22, apparentPower, DELTA1);
        }

        @Test
        void testAcPowerSinglePhase() {
            // given
            final byte currentA = 100;
            final short voltageV = 220;
            final double powerFactor = 0.8; // 80%
            // when
            final double power = PhysicsCalc.Electronics.acPowerSinglePhase(currentA, voltageV, powerFactor);
            // then
            assertEquals(17.6, power, DELTA1);
        }

        @Test
        void testMotorOutputHorsepowerACSinglePhase() {
            // given
            final byte currentA = 100;
            final short voltageV = 220;
            final double powerFactor = 0.8; // 80%
            final double efficiency = 0.7; // 70%
            // when
            final double horsepower = PhysicsCalc.Electronics
                .motorOutputHorsepowerACSinglePhase(currentA, voltageV, powerFactor, efficiency);
            // then
            assertEquals(16.515, horsepower, DELTA3);
        }

        @Test
        void testApparentAcPowerThreePhase() {
            // given
            final byte currentA = 100;
            final short voltageV = 220;
            // when
            final double apparentPower = PhysicsCalc.Electronics
                .apparentPowerACThreePhase(currentA, voltageV);
            // then
            assertEquals(38.104, apparentPower, DELTA3);
        }

        @Test
        void testAcPowerThreePhase() {
            // given
            final byte currentA = 100;
            final short voltageV = 220;
            final double powerFactor = 0.8; // 80%
            // when
            final double power = PhysicsCalc.Electronics.acPowerThreePhase(currentA, voltageV, powerFactor);
            // then
            assertEquals(30.483, power, DELTA3);
        }

        @Test
        void testMotorOutputHorsepowerACThreePhase() {
            // given
            final byte currentA = 100;
            final short voltageV = 220;
            final double powerFactor = 0.8; // 80%
            final double efficiency = 0.7; // 70%
            // when
            final double horsepower = PhysicsCalc.Electronics
                .motorOutputHorsepowerACThreePhase(currentA, voltageV, powerFactor, efficiency);
            // then
            assertEquals(28.604, horsepower, DELTA3);
        }

        @Test
        void testPowerDirectCurrent() {
            // given
            final byte currentA = 100;
            final short voltageV = 220;
            // when
            final double power = PhysicsCalc.Electronics.powerDirectCurrent(currentA, voltageV);
            // then
            assertEquals(22, power, DELTA1);
        }

        @Test
        void testMotorOutputHorsepowerDirectCurrent() {
            // given
            final byte currentA = 100;
            final short voltageV = 220;
            final double efficiency = 0.7; // 70%
            // when
            final double horsepower = PhysicsCalc.Electronics
                .motorOutputHorsepowerDirectCurrent(currentA, voltageV, efficiency);
            // then
            assertEquals(20.643, horsepower, DELTA3);
        }

        @Test
        void testEquivalentInductanceInParallel() {
            // given
            final double[] parallelInductors = new double[]{5, 10, 15};
            // when
            final double equivalentInductance = PhysicsCalc.Electronics
                .equivalentInductanceInParallel(parallelInductors);
            // then
            assertEquals(2.727, equivalentInductance, DELTA3);
        }

        @Test
        void testMissingInductorInParallel() {
            // given
            final double desiredTotalInductance = 2.727;
            final double[] parallelInductors = new double[]{5, 10};
            // when
            final double inductor = PhysicsCalc.Electronics
                .missingInductorInParallel(parallelInductors, desiredTotalInductance);
            // then
            assertEquals(15, inductor, DELTA1);
        }

        @Test
        void testEquivalentInductanceInSeries() {
            // given
            final double[] inductors = new double[]{5, 10, 15};
            // when
            final double equivalentInductance = PhysicsCalc.Electronics.equivalentInductanceInSeries(inductors);
            // then
            assertEquals(30, equivalentInductance, DELTA1);
        }

        @Test
        void testMissingInductorInSeries() {
            // given
            final double desiredTotalInductance = 30;
            final double[] inductors = new double[]{5, 10};
            // when
            final double inductor = PhysicsCalc.Electronics.missingInductorInSeries(inductors, desiredTotalInductance);
            // then
            assertEquals(15, inductor, DELTA1);
        }

        @Test
        void testEquivalentResistanceInParallel() {
            // given
            final double[] resistors = new double[]{2, 4};
            // when
            final double equivalentResistance = PhysicsCalc.Electronics.equivalentResistanceInParallel(resistors);
            // then
            assertEquals(1.333333, equivalentResistance, DELTA6);
        }

        @Test
        void testMissingResistorInParallel() {
            // given
            final byte desiredTotalResistance = 1;
            final double[] resistors = new double[]{2, 4};
            // when
            final double resistor = PhysicsCalc.Electronics
                .missingResistorInParallel(resistors, desiredTotalResistance);
            // then
            assertEquals(4, resistor, DELTA1);
        }

        @Test
        void testResistorDissipatedPower() {
            // given
            final byte resistance = 100;
            final byte voltage = 125;
            // when
            final double[] results = PhysicsCalc.Electronics.resistorDissipatedPower(resistance, voltage);
            // then
            assertNotNull(results);
            assertEquals(2, results.length);
            final double power = results[Constants.ARR_1ST_INDEX];
            final double current = results[Constants.ARR_2ND_INDEX];
            assertEquals(156.25, power, DELTA2);
            assertEquals(1.25, current, DELTA2);
        }

        @Test
        void testVoltageDividerRR() {
            // given
            final short inputVoltage = 1210;
            final double[] resistors = new double[]{20, 30};
            // when
            final double outputVoltage = PhysicsCalc.Electronics.voltageDividerRR(resistors, inputVoltage);
            // then
            assertEquals(726, outputVoltage, DELTA1);
        }

        @Test
        void testVoltageDividerCC() {
            // given
            final short inputVoltage = 1210;
            final double[] capacitors = new double[]{
                CapacityUnit.microFaradsToFarads(100), CapacityUnit.microFaradsToFarads(200)
            };
            // when
            final double outputVoltage = PhysicsCalc.Electronics.voltageDividerCC(capacitors, inputVoltage);
            // then
            assertEquals(403.3, outputVoltage, DELTA1);
        }

        @Test
        void testVoltageDividerLL() {
            // given
            final short inputVoltage = 1210;
            final double[] inductors = new double[]{
                InductanceUnit.microHenriesToHenries(100), InductanceUnit.microHenriesToHenries(200)
            };
            // when
            final double outputVoltage = PhysicsCalc.Electronics.voltageDividerLL(inductors, inputVoltage);
            // then
            assertEquals(806.7, outputVoltage, DELTA1);
        }

        @Test
        @Disabled
        void testResistorWattageInParallel() {
            // given
            final byte constantVoltage = 125;
            final double[] resistors = new double[]{20, 30, 50};
            // when
            final double[][] results = PhysicsCalc.Electronics.resistorWattageInParallel(resistors, constantVoltage);
            // then
            assertMatrixEquals(new double[][]{
                {20, 60.5, 1210, 73200},
                {30, 40.3, 1210, 48800},
                {50, 24.2, 1210, 48800},
            }, results, DELTA2);
        }

        @Test
        void testResistorNoise() {
            // given
            final short resistance = 20_000;
            final double temperature = TemperatureUnit.celsiusToKelvin(20);
            final short bandwidth = 1000;
            // when
            final double[] noiseResults = PhysicsCalc.Electronics.resistorNoise(resistance, temperature, bandwidth);
            // then
            assertArrayEquals(new double[]{5.69e-7, -122.68, -124.9}, noiseResults, DELTA2);
        }

        @Test
        void testRMSVoltageSineWaveVp() {
            // given
            final short voltage = 220;
            // when
            final double rmsVoltage = PhysicsCalc.Electronics.rmsVoltageSineWaveVp(voltage);
            // then
            assertEquals(155.56, rmsVoltage, DELTA2);
        }

        @Test
        void testRMSVoltageSineWaveVpp() {
            // given
            final short voltage = 220;
            // when
            final double rmsVoltage = PhysicsCalc.Electronics.rmsVoltageSineWaveVpp(voltage);
            // then
            assertEquals(77.78, rmsVoltage, DELTA2);
        }

        @Test
        void testRMSVoltageSineWaveVavg() {
            // given
            final short voltage = 220;
            // when
            final double rmsVoltage = PhysicsCalc.Electronics.rmsVoltageSineWaveVavg(voltage);
            // then
            assertEquals(244.36, rmsVoltage, DELTA2);
        }

        @Test
        void testRMSVoltageSquareWaveVp() {
            // given
            final short voltage = 220;
            // when
            final double rmsVoltage = PhysicsCalc.Electronics.rmsVoltageSquareWaveVp(voltage);
            // then
            assertEquals(220, rmsVoltage, DELTA1);
        }

        @Test
        void testRMSVoltageSquareWaveVpp() {
            // given
            final short voltage = 220;
            // when
            final double rmsVoltage = PhysicsCalc.Electronics.rmsVoltageSquareWaveVpp(voltage);
            // then
            assertEquals(110, rmsVoltage, DELTA1);
        }

        @Test
        void testRMSVoltageSquareWaveVavg() {
            // given
            final short voltage = 220;
            // when
            final double rmsVoltage = PhysicsCalc.Electronics.rmsVoltageSquareWaveVavg(voltage);
            // then
            assertEquals(220, rmsVoltage, DELTA1);
        }

        @Test
        void testRMSVoltageTriangleWaveVp() {
            // given
            final short voltage = 220;
            // when
            final double rmsVoltage = PhysicsCalc.Electronics.rmsVoltageTriangleWaveVp(voltage);
            // then
            assertEquals(127.02, rmsVoltage, DELTA2);
        }

        @Test
        void testRMSVoltageTriangleWaveVpp() {
            // given
            final short voltage = 220;
            // when
            final double rmsVoltage = PhysicsCalc.Electronics.rmsVoltageTriangleWaveVpp(voltage);
            // then
            assertEquals(63.51, rmsVoltage, DELTA2);
        }

        @Test
        void testRMSVoltageTriangleWaveVavg() {
            // given
            final short voltage = 220;
            // when
            final double rmsVoltage = PhysicsCalc.Electronics.rmsVoltageTriangleWaveVavg(voltage);
            // then
            assertEquals(199.51793, rmsVoltage, DELTA5);
        }

        @Test
        void testRMSVoltageSawtoothWaveVp() {
            // given
            final short voltage = 220;
            // when
            final double rmsVoltage = PhysicsCalc.Electronics.rmsVoltageSawtoothWaveVp(voltage);
            // then
            assertEquals(127.02, rmsVoltage, DELTA2);
        }

        @Test
        void testRMSVoltageSawtoothWaveVpp() {
            // given
            final short voltage = 220;
            // when
            final double rmsVoltage = PhysicsCalc.Electronics.rmsVoltageSawtoothWaveVpp(voltage);
            // then
            assertEquals(63.51, rmsVoltage, DELTA2);
        }

        @Test
        void testRMSVoltageSawtoothWaveVavg() {
            // given
            final short voltage = 220;
            // when
            final double rmsVoltage = PhysicsCalc.Electronics.rmsVoltageSawtoothWaveVavg(voltage);
            // then
            assertEquals(199.51793, rmsVoltage, DELTA5);
        }

        @Test
        void testRMSVoltageHalfWaveRectifiedSineWaveVp() {
            // given
            final short voltage = 220;
            // when
            final double rmsVoltage = PhysicsCalc.Electronics.rmsVoltageHalfWaveRectifiedSineWaveVp(voltage);
            // then
            assertEquals(110, rmsVoltage, DELTA2);
        }

        @Test
        void testRMSVoltageHalfWaveRectifiedSineWaveVpp() {
            // given
            final short voltage = 220;
            // when
            final double rmsVoltage = PhysicsCalc.Electronics.rmsVoltageHalfWaveRectifiedSineWaveVpp(voltage);
            // then
            assertEquals(55, rmsVoltage, DELTA1);
        }

        @Test
        void testRMSVoltageHalfWaveRectifiedSineWaveVavg() {
            // given
            final short voltage = 220;
            // when
            final double rmsVoltage = PhysicsCalc.Electronics.rmsVoltageHalfWaveRectifiedSineWaveVavg(voltage);
            // then
            assertEquals(345.6, rmsVoltage, DELTA1);
        }

        @Test
        void testRMSVoltageFullWaveRectifiedSineWaveVp() {
            // given
            final short voltage = 220;
            // when
            final double rmsVoltage = PhysicsCalc.Electronics.rmsVoltageFullWaveRectifiedSineWaveVp(voltage);
            // then
            assertEquals(155.56, rmsVoltage, DELTA2);
        }

        @Test
        void testRMSVoltageFullWaveRectifiedSineWaveVpp() {
            // given
            final short voltage = 220;
            // when
            final double rmsVoltage = PhysicsCalc.Electronics.rmsVoltageFullWaveRectifiedSineWaveVpp(voltage);
            // then
            assertEquals(77.78, rmsVoltage, DELTA2);
        }

        @Test
        void testRMSVoltageFullWaveRectifiedSineWaveVavg() {
            // given
            final short voltage = 220;
            // when
            final double rmsVoltage = PhysicsCalc.Electronics.rmsVoltageFullWaveRectifiedSineWaveVavg(voltage);
            // then
            assertEquals(244.36, rmsVoltage, DELTA2);
        }

        @Test
        void testWireResistance() {
            // given
            final byte lengthMeters = 100;
            final double diameter = LengthUnit.millimetersToMeters(12);
            final double electricalResistivity = PhysicsCalc.Electromagnetism.RESISTIVITY_MAP.get("Annealed Cu");
            // when
            final double resistanceOhms = PhysicsCalc.Electronics
                .wireResistance(lengthMeters, diameter, electricalResistivity);
            // then
            assertEquals(0.01512, resistanceOhms, DELTA5);
        }

        @Test
        void testWireResistanceWithConductance() {
            // given
            final double conductance = 66.14;
            // when
            final double resistanceOhms = PhysicsCalc.Electronics.wireResistance(conductance);
            // then
            assertEquals(0.01512, resistanceOhms, DELTA5);
        }

        @Test
        void testWireConductance() {
            // given
            final double electricalConductivity = PhysicsCalc.Electromagnetism.CONDUCTIVITY_MAP.get("Annealed Cu");
            final double crossSectionalArea = 0.0001131; // m²
            final byte lengthMeters = 100;
            // when
            final double conductanceSiemens = PhysicsCalc.Electronics
                .wireConductance(electricalConductivity, crossSectionalArea, lengthMeters);
            // then
            assertEquals(66.14, conductanceSiemens, DELTA2);
        }
    }
}
