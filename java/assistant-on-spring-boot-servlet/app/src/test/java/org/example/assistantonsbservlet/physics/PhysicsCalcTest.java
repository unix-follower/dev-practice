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
import static org.junit.jupiter.api.Assertions.assertThrows;

class PhysicsCalcTest {
    private static final double DELTA1 = 0.1;
    private static final double DELTA2 = 0.01;
    private static final double DELTA3 = 0.001;
    private static final double DELTA4 = 0.0001;
    private static final double DELTA5 = 0.00001;
    private static final double DELTA6 = 0.000001;
    private static final double DELTA7 = 0.0000001;
    private static final double DELTA8 = 0.00000001;
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
    class Mechanics {
        @Test
        void testPotentialEnergy() {
            // given
            final double mass = 0.1;
            final double height = 2.5;
            final double gravityAcc = AccelerationUnit.GRAVITATIONAL_ACCELERATION_ON_EARTH;
            // when
            final double energy = PhysicsCalc.Mechanics.potentialEnergy(mass, height, gravityAcc);
            // then
            assertEquals(2.451663, energy, DELTA6);
        }

        @Test
        void testElasticPotentialEnergy() {
            // given
            final byte springForceConstant = 80;
            final double springStretchLength = 0.15;
            // when
            final double springPotentialEnergy = PhysicsCalc.Mechanics
                .elasticPotentialEnergy(springForceConstant, springStretchLength);
            // then
            assertEquals(0.9, springPotentialEnergy, DELTA1);
        }

        @Test
        void testElongationOfString() {
            // given
            final byte springForceConstant = 15;
            final byte springPotentialEnergy = 98;
            // when
            final double springStretchLength = PhysicsCalc.Mechanics
                .elongationOfString(springForceConstant, springPotentialEnergy);
            // then
            assertEquals(3.614784, springStretchLength, DELTA6);
        }

        @Test
        void testKineticEnergy() {
            // given
            final double massKg = 0.45;
            final double velocity = 38.4;
            // when
            final double energy = PhysicsCalc.Mechanics.kineticEnergy(massKg, velocity);
            // then
            assertEquals(331.776, energy, DELTA3);
        }

        @Test
        void testImpactEnergyDistance() {
            // given
            final double golfBallMass = MassUnit.gToKg(45.9);
            final byte velocity = 5;
            final double collisionDistance = 0.5;
            // when
            final double[] energy = PhysicsCalc.Mechanics
                .impactEnergyDistance(golfBallMass, velocity, collisionDistance);
            // then
            assertArrayEquals(new double[]{1.148, 2.295, 0.574}, energy, DELTA3);
        }

        @Test
        void testImpactEnergyTime() {
            // given
            final double golfBallMass = MassUnit.gToKg(45.9);
            final byte velocity = 5;
            final byte collisionTimeSeconds = 2;
            // when
            final double[] energy = PhysicsCalc.Mechanics
                .impactEnergyTime(golfBallMass, velocity, collisionTimeSeconds);
            // then
            assertArrayEquals(new double[]{0.1148, 0.2295, 0.574}, energy, DELTA3);
        }

        static List<Arguments> recoilEnergyArgs() {
            return List.of(
                // AK-74 w/ 5.45x39mm
                Arguments.of(3.4, 880, 1.5, 1303.8, 3.6, new double[]{1.3744, 3.4, 4.948}, DELTA3),
                // Barrett M82 w/ .50BMG
                Arguments.of(41.9, 902, 15.2, 1436.7, 14, new double[]{4.259, 126.99, 59.63}, DELTA2),
                // Glock 17 w/ 9mm Luger
                Arguments.of(8, 374, 0.39, 1900.6, 0.905, new double[]{4.125, 7.7, 3.733}, DELTA3),
                // Glock 20 w/ 10mm Auto
                Arguments.of(11.7, 338, 0.5, 1654.5, 1.11, new double[]{4.308, 10.3, 4.782}, DELTA3),
                // M14 w/ 7.62x51mm
                Arguments.of(10.1, 845, 3.1, 1574.8, 4.5, new double[]{2.9814, 20, 13.416}, DELTA3),
                // M16A2 w/ 5.56x45mm
                Arguments.of(3.6, 985, 1.68, 2156.4, 3.99, new double[]{1.7967, 6.44, 7.169}, DELTA3),
                // Mauser 4.1 w/ 6.5x55mm
                Arguments.of(9.1, 800, 3, 1611.8, 4.1, new double[]{2.955, 17.9, 12.115}, DELTA3),
                // Remington Model 700 w/ 9.3x62mm
                Arguments.of(18.5, 710, 1.6, 5049, 4.5, new double[]{4.714, 50, 21.213}, DELTA3),
                // Springfield 1911A w/ .45ACP
                Arguments.of(14.9, 259, 0.47, 1713.3, 1.11, new double[]{4.202, 9.8, 4.664}, DELTA3)
            );
        }

        @ParameterizedTest
        @MethodSource("recoilEnergyArgs")
        void testRecoilEnergy(double bulletMass, double bulletVelocity, double powderChargeMass,
                              double velocityOfCharge, double firearmMass, double[] expectedResult, double delta) {
            // when
            final double[] recoil = PhysicsCalc.Mechanics
                .recoilEnergy(bulletMass, bulletVelocity, powderChargeMass, velocityOfCharge, firearmMass);
            // then
            assertArrayEquals(expectedResult, recoil, delta);
        }

        @Test
        void testFootPoundsOfEnergy() {
            // given
            final short velocity = 2800;
            final short weight = 150;
            // when
            final double fpe = PhysicsCalc.Mechanics.footPoundsOfEnergy(velocity, weight);
            // then
            assertEquals(2612, fpe, DELTA1);
        }

        @Test
        void testPwr() {
            // given
            final int powerWatts = 216_253;
            final short weightKg = 1858;
            // when
            final double ratio = PhysicsCalc.Mechanics.pwr(powerWatts, weightKg);
            // then
            assertEquals(116.4, ratio, DELTA1); // W/kg
        }

        @Test
        void testSNR() {
            // given
            final short signal = 20;
            final short noise = 5;
            // when
            final double ratio = PhysicsCalc.Mechanics.snr(signal, noise);
            // then
            assertEquals(4, ratio, DELTA1);
        }

        @Test
        void testSnrDifference() {
            // given
            final short signal = 450;
            final short noise = 350;
            // when
            final double difference = PhysicsCalc.Mechanics.snrDifference(signal, noise);
            // then
            assertEquals(100, difference, DELTA1);
        }

        @Test
        void testPowerSNR() {
            // given
            final short signal = 450;
            final short noise = 350;
            // when
            final double ratio = PhysicsCalc.Mechanics.powerSNR(signal, noise);
            // then
            assertEquals(1.0914, ratio, DELTA4);
        }

        @Test
        void testVoltageSNR() {
            // given
            final short signal = 450;
            final short noise = 350;
            // when
            final double ratio = PhysicsCalc.Mechanics.voltageSNR(signal, noise);
            // then
            assertEquals(2.183, ratio, DELTA3);
        }

        @Test
        void testSnrFromCoefficientOfVariation() {
            // given
            final short signalMean = 150;
            final byte noiseStd = 25;
            // when
            final double[] ratios = PhysicsCalc.Mechanics.snrFromCoefficientOfVariation(signalMean, noiseStd);
            // then
            assertArrayEquals(new double[]{6, 36}, ratios, DELTA1);
        }

        static List<Arguments> tntEquivalentArgs() {
            return List.of(
                Arguments.of(4_184_000, 4_184_000, 6.5, new double[]{1, 6.5}, DELTA1), // TNT
                Arguments.of(2_208_812, 4_184_000, 6.5, new double[]{0.5279, 3.4315}, DELTA4), // Baratole
                Arguments.of(4_566_294, 4_184_000, 6.5, new double[]{1.0914, 7.094}, DELTA4), // Composition B
                Arguments.of(4_714_964, 4_184_000, 6.5, new double[]{1.127, 7.325}, DELTA3), // C4
                Arguments.of(4_821_157, 4_184_000, 6.5, new double[]{1.1523, 7.49}, DELTA3), // HMX
                Arguments.of(4_545_056, 4_184_000, 6.5, new double[]{1.0863, 7.061}, DELTA4), // Pentolit 50/50
                Arguments.of(4_757_442, 4_184_000, 6.5, new double[]{1.137, 7.391}, DELTA3), // PBX 9407
                Arguments.of(4_906_112, 4_184_000, 6.5, new double[]{1.1726, 7.622}, DELTA3), // PETN
                Arguments.of(4_821_157, 4_184_000, 6.5, new double[]{1.1523, 7.49}, DELTA3), // RDX
                Arguments.of(4_481_340, 4_184_000, 6.5, new double[]{1.071, 6.962}, DELTA3), // Tetryl
                Arguments.of(3_164_548, 4_184_000, 6.5, new double[]{0.7563, 4.916}, DELTA3), // NQ
                Arguments.of(4_714_964, 4_184_000, 6.5, new double[]{1.127, 7.325}, DELTA3), // NG
                Arguments.of(1_757_280, 4_184_000, 6.5, new double[]{0.42, 2.73}, DELTA2), // Ammonium nitrate
                Arguments.of(3_681_920, 4_184_000, 6.5, new double[]{0.88, 5.72}, DELTA2), // ANFO
                Arguments.of(669_440, 4_184_000, 6.5, new double[]{0.16, 1.04}, DELTA2), // Natural gas
                Arguments.of(4_058_480, 4_184_000, 6.5, new double[]{0.97, 6.305}, DELTA3), // Ammonium picrate
                Arguments.of(4_267_680, 4_184_000, 6.5, new double[]{1.02, 6.63}, DELTA2), // HBX-3
                Arguments.of(5_020_800, 4_184_000, 6.5, new double[]{1.2, 7.8}, DELTA1), // Torpex
                Arguments.of(3_891_120, 4_184_000, 6.5, new double[]{0.93, 6.045}, DELTA3), // Tritonal
                Arguments.of(4_895_280, 4_184_000, 6.5, new double[]{1.17, 7.605}, DELTA3), // Amatol 80/20
                Arguments.of(5_020_800, 4_184_000, 6.5, new double[]{1.2, 7.8}, DELTA1) // Tetrytol 75/25
            );
        }

        @ParameterizedTest
        @MethodSource("tntEquivalentArgs")
        void testTntEquivalent(double explosiveDetonationHeat, double tntDetonationHeat, double explosiveWeight,
                               double[] expectedResult, double delta) {
            // when
            final double[] equivalent = PhysicsCalc.Mechanics
                .tntEquivalent(explosiveDetonationHeat, tntDetonationHeat, explosiveWeight);
            // then
            assertArrayEquals(expectedResult, equivalent, delta);
        }

        @Test
        void testWork() {
            // given
            final byte force = 50; // N
            final double angleOfForceRad = 0.5236; // 30°
            final byte displacement = 100; // m
            // when
            final double work = PhysicsCalc.Mechanics.work(force, angleOfForceRad, displacement);
            // then
            assertEquals(4330.1, work, DELTA1);
        }

        @Test
        void testWorkFromVelocityChange() {
            // given
            final byte massKg = 2;
            final byte initialSpeed = 10; // m/s
            final byte finalSpeed = 35; // m/s
            // when
            final double work = PhysicsCalc.Mechanics.workFromVelocityChange(massKg, initialSpeed, finalSpeed);
            // then
            assertEquals(1125, work, DELTA1);
        }

        @Test
        void testPower() {
            // given
            final short workJ = 9000;
            final byte timeSeconds = 60;
            // when
            final double power = PhysicsCalc.Mechanics.power(workJ, timeSeconds);
            // then
            assertEquals(150, power, DELTA1);
        }

        @Test
        void testEirpWithKnownTotalCableLoss() {
            // given
            final byte totalCableLoss = 3;
            final byte transmitterOutputPower = 21;
            final byte antennaGain = 11;
            final byte numberOfConnectors = 2;
            final double connectorLoss = MathCalc.ONE_HALF;
            // when
            final double eirp = PhysicsCalc.Mechanics.eirpWithKnownTotalCableLoss(totalCableLoss,
                transmitterOutputPower, antennaGain, numberOfConnectors, connectorLoss);
            // then
            assertEquals(28, eirp, DELTA1);
        }

        @Test
        void testEirpWithKnownCableLossPerUnitOfLength() {
            // given
            final double cableLoss = 0.3;
            final byte cableLength = 2;
            final byte transmitterOutputPower = 21;
            final byte antennaGain = 11;
            final byte numberOfConnectors = 2;
            final double connectorLoss = MathCalc.ONE_HALF;
            // when
            final double eirp = PhysicsCalc.Mechanics.eirpWithKnownCableLossPerUnitOfLength(cableLoss, cableLength,
                transmitterOutputPower, antennaGain, numberOfConnectors, connectorLoss);
            // then
            assertEquals(30.4, eirp, DELTA1);
        }

        static List<Arguments> eirpWithKnownCableTypeAndFrequencyForCableType9914Args() {
            // Cable length = 1
            return List.of(
                // Frequency = 150 MHz
                Arguments.of(0.05577, 21, 11, 2, MathCalc.ONE_HALF, 30.944, DELTA3),
                // Frequency = 450 MHz
                Arguments.of(0.09514, 21, 11, 2, MathCalc.ONE_HALF, 30.905, DELTA3),
                // Frequency = 900 MHz
                Arguments.of(0.13451, 21, 11, 2, MathCalc.ONE_HALF, 30.865, DELTA3),
                // Frequency = 1800 MHz
                Arguments.of(0.20013, 21, 11, 2, MathCalc.ONE_HALF, 30.8, DELTA1),
                // Frequency = 2400 MHz
                Arguments.of(0.2395, 21, 11, 2, MathCalc.ONE_HALF, 30.76, DELTA2)
            );
        }

        static List<Arguments> eirpWithKnownCableTypeAndFrequencyForCableTypeRG58Args() {
            // Cable length = 1
            return List.of(
                // Frequency = 150 MHz
                Arguments.of(1.739, 21, 11, 2, MathCalc.ONE_HALF, 29.26, DELTA2),
                // Frequency = 450 MHz
                Arguments.of(0.3445, 21, 11, 2, MathCalc.ONE_HALF, 30.656, DELTA3),
                // Frequency = 900 MHz
                Arguments.of(0.5249, 21, 11, 2, MathCalc.ONE_HALF, 30.475, DELTA3),
                // Frequency = 1800 MHz
                Arguments.of(0.6923, 21, 11, 2, MathCalc.ONE_HALF, 30.31, DELTA2),
                // Frequency = 2400 MHz
                Arguments.of(0.8136, 21, 11, 2, MathCalc.ONE_HALF, 30.186, DELTA3)
            );
        }

        static List<Arguments> eirpWithKnownCableTypeAndFrequencyForCableTypeRG142Args() {
            // Cable length = 1
            return List.of(
                // Frequency = 150 MHz
                Arguments.of(0.10499, 21, 11, 2, MathCalc.ONE_HALF, 30.895, DELTA3),
                // Frequency = 450 MHz
                Arguments.of(0.2067, 21, 11, 2, MathCalc.ONE_HALF, 30.793, DELTA3),
                // Frequency = 900 MHz
                Arguments.of(0.30184, 21, 11, 2, MathCalc.ONE_HALF, 30.7, DELTA1),
                // Frequency = 1800 MHz
                Arguments.of(0.4265, 21, 11, 2, MathCalc.ONE_HALF, 30.573, DELTA1),
                // Frequency = 2400 MHz
                Arguments.of(0.6857, 21, 11, 2, MathCalc.ONE_HALF, 30.314, DELTA3)
            );
        }

        static List<Arguments> eirpWithKnownCableTypeAndFrequencyForCableTypeRG174Args() {
            // Cable length = 1
            return List.of(
                // Frequency = 150 MHz
                Arguments.of(0.3379, 21, 11, 2, MathCalc.ONE_HALF, 30.66, DELTA2),
                // Frequency = 450 MHz
                Arguments.of(0.05938, 21, 11, 2, MathCalc.ONE_HALF, 30.94, DELTA2),
                // Frequency = 900 MHz
                Arguments.of(0.8497, 21, 11, 2, MathCalc.ONE_HALF, 30.15, DELTA2),
                // Frequency = 1800 MHz
                Arguments.of(1.2303, 21, 11, 2, MathCalc.ONE_HALF, 29.77, DELTA2),
                // Frequency = 2400 MHz
                Arguments.of(1.4272, 21, 11, 2, MathCalc.ONE_HALF, 29.57, DELTA2)
            );
        }

        static List<Arguments> eirpWithKnownCableTypeAndFrequencyForCableTypeRG213UArgs() {
            // Cable length = 1
            return List.of(
                // Frequency = 150 MHz
                Arguments.of(0.08202, 21, 11, 2, MathCalc.ONE_HALF, 30.92, DELTA2),
                // Frequency = 450 MHz
                Arguments.of(0.16404, 21, 11, 2, MathCalc.ONE_HALF, 30.836, DELTA3),
                // Frequency = 900 MHz
                Arguments.of(0.24934, 21, 11, 2, MathCalc.ONE_HALF, 30.75, DELTA2),
                // Frequency = 1800 MHz
                Arguments.of(0.33465, 21, 11, 2, MathCalc.ONE_HALF, 30.665, DELTA3),
                // Frequency = 2400 MHz
                Arguments.of(0.4068, 21, 11, 2, MathCalc.ONE_HALF, 30.593, DELTA3)
            );
        }

        static List<Arguments> eirpWithKnownCableTypeAndFrequencyForCableTypeLMR195Args() {
            // Cable length = 1
            return List.of(
                // Frequency = 150 MHz
                Arguments.of(0.14436, 21, 11, 2, MathCalc.ONE_HALF, 30.856, DELTA3),
                // Frequency = 450 MHz
                Arguments.of(0.2559, 21, 11, 2, MathCalc.ONE_HALF, 30.744, DELTA3),
                // Frequency = 900 MHz
                Arguments.of(0.3642, 21, 11, 2, MathCalc.ONE_HALF, 30.636, DELTA3),
                // Frequency = 1800 MHz
                Arguments.of(0.5249, 21, 11, 2, MathCalc.ONE_HALF, 30.475, DELTA3),
                // Frequency = 2400 MHz
                Arguments.of(0.5545, 21, 11, 2, MathCalc.ONE_HALF, 30.446, DELTA3)
            );
        }

        static List<Arguments> eirpWithKnownCableTypeAndFrequencyForCableTypeLMR240Args() {
            // Cable length = 1
            return List.of(
                // Frequency = 150 MHz
                Arguments.of(0.09843, 21, 11, 2, MathCalc.ONE_HALF, 30.9, DELTA1),
                // Frequency = 450 MHz
                Arguments.of(0.1739, 21, 11, 2, MathCalc.ONE_HALF, 30.826, DELTA3),
                // Frequency = 900 MHz
                Arguments.of(0.24934, 21, 11, 2, MathCalc.ONE_HALF, 30.75, DELTA2),
                // Frequency = 1800 MHz
                Arguments.of(0.3576, 21, 11, 2, MathCalc.ONE_HALF, 30.64, DELTA2),
                // Frequency = 2400 MHz
                Arguments.of(0.4134, 21, 11, 2, MathCalc.ONE_HALF, 30.587, DELTA3)
            );
        }

        static List<Arguments> eirpWithKnownCableTypeAndFrequencyForCableTypeLMR400Args() {
            // Cable length = 1
            return List.of(
                // Frequency = 150 MHz
                Arguments.of(0.04921, 21, 11, 2, MathCalc.ONE_HALF, 30.95, DELTA2),
                // Frequency = 450 MHz
                Arguments.of(0.08858, 21, 11, 2, MathCalc.ONE_HALF, 30.91, DELTA2),
                // Frequency = 900 MHz
                Arguments.of(0.12795, 21, 11, 2, MathCalc.ONE_HALF, 30.87, DELTA2),
                // Frequency = 1800 MHz
                Arguments.of(0.187, 21, 11, 2, MathCalc.ONE_HALF, 30.81, DELTA2),
                // Frequency = 2400 MHz
                Arguments.of(0.21654, 21, 11, 2, MathCalc.ONE_HALF, 30.783, DELTA3)
            );
        }

        static List<Arguments> eirpWithKnownCableTypeAndFrequencyForCableTypeLMR600Args() {
            // Cable length = 1
            return List.of(
                // Frequency = 150 MHz
                Arguments.of(0.03281, 21, 11, 2, MathCalc.ONE_HALF, 30.97, DELTA2),
                // Frequency = 450 MHz
                Arguments.of(0.05577, 21, 11, 2, MathCalc.ONE_HALF, 30.944, DELTA3),
                // Frequency = 900 MHz
                Arguments.of(0.08202, 21, 11, 2, MathCalc.ONE_HALF, 30.92, DELTA2),
                // Frequency = 1800 MHz
                Arguments.of(0.1214, 21, 11, 2, MathCalc.ONE_HALF, 30.88, DELTA2),
                // Frequency = 2400 MHz
                Arguments.of(0.14108, 21, 11, 2, MathCalc.ONE_HALF, 30.86, DELTA2)
            );
        }

        static List<Arguments> eirpWithKnownCableTypeAndFrequencyForCableTypeLMR900Args() {
            // Cable length = 1
            return List.of(
                // Frequency = 150 MHz
                Arguments.of(0.022966, 21, 11, 2, MathCalc.ONE_HALF, 30.98, DELTA2),
                // Frequency = 450 MHz
                Arguments.of(0.03937, 21, 11, 2, MathCalc.ONE_HALF, 30.96, DELTA2),
                // Frequency = 900 MHz
                Arguments.of(0.05577, 21, 11, 2, MathCalc.ONE_HALF, 30.944, DELTA3),
                // Frequency = 1800 MHz
                Arguments.of(0.08202, 21, 11, 2, MathCalc.ONE_HALF, 30.92, DELTA2),
                // Frequency = 2400 MHz
                Arguments.of(0.09514, 21, 11, 2, MathCalc.ONE_HALF, 30.905, DELTA3)
            );
        }

        static List<Arguments> eirpWithKnownCableTypeAndFrequencyForCableTypeUltraFlex400Args() {
            // Cable length = 1
            return List.of(
                // Frequency = 150 MHz
                Arguments.of(0.05906, 21, 11, 2, MathCalc.ONE_HALF, 30.94, DELTA2),
                // Frequency = 450 MHz
                Arguments.of(0.10499, 21, 11, 2, MathCalc.ONE_HALF, 30.895, DELTA3),
                // Frequency = 900 MHz
                Arguments.of(0.1542, 21, 11, 2, MathCalc.ONE_HALF, 30.846, DELTA3),
                // Frequency = 1800 MHz
                Arguments.of(0.2264, 21, 11, 2, MathCalc.ONE_HALF, 30.774, DELTA3),
                // Frequency = 2400 MHz
                Arguments.of(0.2592, 21, 11, 2, MathCalc.ONE_HALF, 30.74, DELTA2)
            );
        }

        static List<Arguments> eirpWithKnownCableTypeAndFrequencyForCableTypeUltraFlex600Args() {
            // Cable length = 1
            return List.of(
                // Frequency = 150 MHz
                Arguments.of(0.03937, 21, 11, 2, MathCalc.ONE_HALF, 30.96, DELTA2),
                // Frequency = 450 MHz
                Arguments.of(0.0689, 21, 11, 2, MathCalc.ONE_HALF, 30.93, DELTA2),
                // Frequency = 900 MHz
                Arguments.of(0.09843, 21, 11, 2, MathCalc.ONE_HALF, 30.9, DELTA1),
                // Frequency = 1800 MHz
                Arguments.of(0.14764, 21, 11, 2, MathCalc.ONE_HALF, 30.85, DELTA2),
                // Frequency = 2400 MHz
                Arguments.of(0.16732, 21, 11, 2, MathCalc.ONE_HALF, 30.83, DELTA2)
            );
        }

        @ParameterizedTest
        @MethodSource("eirpWithKnownCableTypeAndFrequencyForCableType9914Args")
        @MethodSource("eirpWithKnownCableTypeAndFrequencyForCableTypeRG58Args")
        @MethodSource("eirpWithKnownCableTypeAndFrequencyForCableTypeRG142Args")
        @MethodSource("eirpWithKnownCableTypeAndFrequencyForCableTypeRG174Args")
        @MethodSource("eirpWithKnownCableTypeAndFrequencyForCableTypeRG213UArgs")
        @MethodSource("eirpWithKnownCableTypeAndFrequencyForCableTypeLMR195Args")
        @MethodSource("eirpWithKnownCableTypeAndFrequencyForCableTypeLMR240Args")
        @MethodSource("eirpWithKnownCableTypeAndFrequencyForCableTypeLMR400Args")
        @MethodSource("eirpWithKnownCableTypeAndFrequencyForCableTypeLMR600Args")
        @MethodSource("eirpWithKnownCableTypeAndFrequencyForCableTypeLMR900Args")
        @MethodSource("eirpWithKnownCableTypeAndFrequencyForCableTypeUltraFlex400Args")
        @MethodSource("eirpWithKnownCableTypeAndFrequencyForCableTypeUltraFlex600Args")
        void testEirpWithKnownCableTypeAndFrequency(
            double totalCableLoss, double transmitterOutputPower, double antennaGain, double numberOfConnectors,
            double connectorLoss, double expectedResult, double delta) {
            // when
            final double eirp = PhysicsCalc.Mechanics.eirpWithKnownTotalCableLoss(totalCableLoss,
                transmitterOutputPower, antennaGain, numberOfConnectors, connectorLoss);
            // then
            assertEquals(expectedResult, eirp, delta);
        }
    }

    @Nested
    class Statics {
        @Test
        void testPressure() {
            // given
            final int force = 815_210;
            final byte area = 8;
            // when
            final double pressure = PhysicsCalc.Statics.pressure(force, area);
            // then
            assertEquals(101_901.25, pressure, DELTA2);
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

        static List<Arguments> bulletEnergyArgs() {
            return List.of(
                // a 9 mm pistol: HST 124 grain and HST 147 grain
                Arguments.of(124, 350.5, 493.6, DELTA1),
                Arguments.of(147, 304.8, 442.5, DELTA1),
                Arguments.of(250, 914.4, 6773, 1) // a .338 Lapua Magnum
            );
        }

        @ParameterizedTest
        @MethodSource("bulletEnergyArgs")
        void testBulletEnergy(double bulletMass, double bulletVelocity, double expectedResult, double delta) {
            // given
            final double massKg = MassUnit.grToKg(bulletMass);
            // when
            final double energy = PhysicsCalc.Dynamics.bulletEnergy(massKg, bulletVelocity);
            // then
            assertEquals(expectedResult, energy, delta);
        }

        @Test
        void testForce() {
            // given
            final byte massInKg = 2;
            final byte acceleration = 25;
            // when
            final double force = PhysicsCalc.Dynamics.force(massInKg, acceleration);
            // then
            assertEquals(50, force, DELTA1);
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

        static List<Arguments> electricalPowerArgs() {
            return List.of(
                // Electric oven (with resistive heating element): pf=1
                Arguments.of(120, 10, 1, 1200, DELTA1), // Lamps with a standard bulb: pf=1
                Arguments.of(120, 10, 0.93, 1116, DELTA1), // Fluorescent lamps: pf=0.93
                // Common induction motor at half load: pf=0.73, at full load: pf=0.85
                Arguments.of(120, 10, 0.73, 876, DELTA1),
                Arguments.of(120, 10, 0.85, 1020, DELTA1) // Inductive oven: pf=0.85
            );
        }

        @ParameterizedTest
        @MethodSource("electricalPowerArgs")
        void testElectricalPower(
            double voltage, double current, double powerFactor, double expectedPowerWatts, double delta) {
            // when
            final double powerWatts = PhysicsCalc.Electromagnetism.electricalPower(voltage, current, powerFactor);
            // then
            assertEquals(expectedPowerWatts, powerWatts, delta);
        }

        @Test
        void testEnergyDensityOfFields() {
            // given
            final double electricFieldStrength = ElectricFieldStrengthUnit.kiloNCToNC(2000);
            final double magneticField = 0.03;
            // when
            final double energyDensity = PhysicsCalc.Electromagnetism
                .energyDensityOfFields(electricFieldStrength, magneticField);
            // then
            assertEquals(375.8, energyDensity, DELTA1);
        }

        @Test
        void testElectricFieldStrength() {
            // given
            final double energyDensity = 375.8;
            final double magneticField = 0.03;
            // when
            final double strength = PhysicsCalc.Electromagnetism.electricFieldStrength(energyDensity, magneticField);
            // then
            assertEquals(1_999_605, strength, 1);
        }

        @Test
        void testMagneticField() {
            // given
            final double currentInWire = 2.5;
            final double distanceFromWire = LengthUnit.centimetersToMeters(1);
            // when
            final double magneticField = PhysicsCalc.Electromagnetism.magneticField(currentInWire, distanceFromWire);
            // then
            assertEquals(0.00005, magneticField, DELTA5);
        }

        @Test
        void testMagneticFieldInStraightWire() {
            // given
            final double distanceFromWire = LengthUnit.centimetersToMeters(1);
            final double magneticField = 0.00005;
            // when
            final double currentInWire = PhysicsCalc.Electromagnetism
                .magneticFieldInStraightWire(distanceFromWire, magneticField);
            // then
            assertEquals(2.5, currentInWire, DELTA1);
        }

        @Test
        void testAccelerationInElectricField() {
            // given
            final byte massGrams = 100;
            final double chargeCoulombs = 8.011e-22;
            final byte electricField = 7;
            // when
            final double acceleration = PhysicsCalc.Electromagnetism
                .accelerationInElectricField(massGrams, chargeCoulombs, electricField);
            // then
            assertEquals(5.608e-20, acceleration, DELTA9);
        }

        @Test
        void testCapacitance() {
            // given
            final byte area = 120; // mm²
            final double permittivity = PhysicsCalc.VACUUM_PERMITTIVITY; // ε
            final byte separationDistance = 5; // mm
            // when
            final double capacitance = PhysicsCalc.Electromagnetism.capacitance(area, permittivity, separationDistance);
            // then
            assertEquals(2.12496e-13, capacitance, DELTA6);
        }

        @Test
        void testCapacitiveReactance() {
            // given
            final double capacitance = 3e-8;
            final byte frequency = 60;
            // when
            final double reactance = PhysicsCalc.Electromagnetism.capacitiveReactance(capacitance, frequency);
            // then
            assertEquals(88_419.41, reactance, DELTA2);
        }

        @Test
        void testCoulombsLaw() {
            // given
            final double charge = -1.602176634e-19;
            final double distance = 120e-12;
            // when
            final double force = PhysicsCalc.Electromagnetism.coulombsLaw(charge, charge, distance);
            // the
            assertEquals(1.602137e-8, force, DELTA8);
        }

        @Test
        void testCutoffFrequencyRCFilter() {
            // given
            final double resistance = ElectricalResistanceUnit.kiloOhmsToOhms(10);
            final double capacitance = CapacitanceUnit.nanoFaradsToFarads(25);
            // when
            final double fc = PhysicsCalc.Electromagnetism.cutoffFrequencyRCFilter(resistance, capacitance);
            // the
            assertEquals(636.6, fc, DELTA1);
        }

        @Test
        void testCutoffFrequencyRLFilter() {
            // given
            final double resistance = ElectricalResistanceUnit.kiloOhmsToOhms(10);
            final double inductance = InductanceUnit.microHenriesToHenries(25);
            // when
            final double fc = PhysicsCalc.Electromagnetism.cutoffFrequencyRLFilter(resistance, inductance);
            // the
            assertEquals(63_661_977, Math.round(fc), DELTA1);
        }

        @Test
        void testCyclotronFrequency() {
            // given
            final double charge = 1.602e-19;
            final byte magneticFieldStrength = 1;
            final double massKg = 1.672e-27;
            // when
            final double frequency = PhysicsCalc.Electromagnetism
                .cyclotronFrequency(charge, magneticFieldStrength, massKg);
            // then
            final double frequencyKHz = Math.round(FrequencyUnit.hzToKHz(frequency));
            assertEquals(15_249, frequencyKHz, DELTA1);
        }

        @Test
        void testAcWattageSinglePhase() {
            // given
            final byte voltage = 24;
            final double current = 3.75;
            final double powerFactor = MathCalc.ONE_HALF;
            // when
            final double wattage = PhysicsCalc.Electromagnetism.acWattageSinglePhase(voltage, current, powerFactor);
            // then
            assertEquals(45, wattage, DELTA1);
        }

        @Test
        void testAcWattage3PhaseL2L() {
            // given
            final byte voltage = 120;
            final byte current = 5;
            final double powerFactor = 0.8;
            // when
            final double wattage = PhysicsCalc.Electromagnetism.acWattage3PhaseL2L(voltage, current, powerFactor);
            // then
            assertEquals(831.4, wattage, DELTA1);
        }

        @Test
        void testAcWattage3PhaseL2N() {
            // given
            final byte voltage = 120;
            final byte current = 5;
            final double powerFactor = 0.8;
            // when
            final double wattage = PhysicsCalc.Electromagnetism.acWattage3PhaseL2N(voltage, current, powerFactor);
            // then
            assertEquals(1440, wattage, DELTA1);
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
            final double farads = CapacitanceUnit.microFaradsToFarads(capacityMicroFarads);
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
                CapacitanceUnit.milliFaradsToFarads(30),
                CapacitanceUnit.milliFaradsToFarads(0.5),
                CapacitanceUnit.milliFaradsToFarads(6),
                CapacitanceUnit.milliFaradsToFarads(0.75)
            };
            // when
            final double capacitance = PhysicsCalc.Electronics.capacitorInParallel(capacitors);
            // then
            assertEquals(37.25, CapacitanceUnit.faradsToMilliFarads(capacitance), DELTA2);
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
                CapacitanceUnit.microFaradsToFarads(100), CapacitanceUnit.microFaradsToFarads(200)
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

        @Test
        void testCapacitorChargeTimeConstant() {
            // given
            final short resistance = 3000; // Ω
            final double capacitance = CapacitanceUnit.microFaradsToFarads(1000);
            // when
            final double timeConstant = PhysicsCalc.Electronics.capacitorChargeTimeConstant(resistance, capacitance);
            // then
            assertEquals(3, timeConstant, DELTA1);
        }

        static List<Arguments> capacitorChargeTimeArgs() {
            return List.of(
                Arguments.of(3, 5, 15, DELTA1),
                Arguments.of(3, 9, 27, DELTA1)
            );
        }

        @ParameterizedTest
        @MethodSource("capacitorChargeTimeArgs")
        void testCapacitorChargeTime(
            double timeConstant, double multipleTimeConstant, double expectedChargeTimeSeconds, double delta) {
            // when
            final double chargingTime = PhysicsCalc.Electronics.capacitorChargeTime(multipleTimeConstant, timeConstant);
            // then
            assertEquals(expectedChargeTimeSeconds, chargingTime, delta);
        }

        @Test
        void testCapacitorChargeTimeWithInvalidMultiple() {
            // given
            final byte timeConstant = 3;
            final byte multipleTimeConstant = 90;
            // then
            assertThrows(IllegalArgumentException.class, () ->
                // when
                PhysicsCalc.Electronics.capacitorChargeTime(multipleTimeConstant, timeConstant)
            );
        }

        @Test
        void testCapacitorChargeTimeGivenPercentage() {
            // given
            final byte timeConstant = 3;
            final double percentage = 0.5;
            // when
            final double[] results = PhysicsCalc.Electronics
                .capacitorChargeTimeGivenPercentage(percentage, timeConstant);
            // then
            assertNotNull(results);
            assertEquals(2, results.length);

            final double multipleTimeConstant = results[Constants.ARR_1ST_INDEX];
            assertEquals(0.693, multipleTimeConstant, DELTA3);
            final double chargeTime = results[Constants.ARR_2ND_INDEX];
            assertEquals(2.0794, chargeTime, DELTA4);
        }

        @Test
        void testIdealTransformerSecondaryVoltage() {
            // given
            final byte primaryWindings = 5;
            final byte secondaryWindings = 2;
            final short primaryVoltage = 220;
            // when
            final double secondaryVoltage = PhysicsCalc.Electronics
                .idealTransformerSecondaryVoltage(primaryWindings, secondaryWindings, primaryVoltage);
            // then
            assertEquals(88, secondaryVoltage, DELTA1);
        }

        @Test
        void testIdealTransformerPrimaryVoltage() {
            // given
            final byte primaryWindings = 5;
            final byte secondaryWindings = 2;
            final short secondaryVoltage = 88;
            // when
            final double primaryVoltage = PhysicsCalc.Electronics
                .idealTransformerPrimaryVoltage(primaryWindings, secondaryWindings, secondaryVoltage);
            // then
            assertEquals(220, primaryVoltage, DELTA1);
        }

        @Test
        void testIdealTransformerPrimaryCurrent() {
            // given
            final byte primaryWindings = 5;
            final byte secondaryWindings = 2;
            final short secondaryCurrent = 125;
            // when
            final double primaryVoltage = PhysicsCalc.Electronics
                .idealTransformerPrimaryCurrent(primaryWindings, secondaryWindings, secondaryCurrent);
            // then
            assertEquals(50, primaryVoltage, DELTA1);
        }

        @Test
        void testIdealTransformerSecondaryCurrent() {
            // given
            final byte primaryWindings = 5;
            final byte secondaryWindings = 2;
            final short primaryCurrent = 50;
            // when
            final double primaryVoltage = PhysicsCalc.Electronics
                .idealTransformerSecondaryCurrent(primaryWindings, secondaryWindings, primaryCurrent);
            // then
            assertEquals(125, primaryVoltage, DELTA1);
        }

        @Test
        void testSolenoidInductance() {
            // given
            final byte numberOfTurns = 10;
            final byte radius = 6;
            final double length = 1.5;
            // when
            final double inductance = PhysicsCalc.Electronics.solenoidInductance(numberOfTurns, radius, length);
            // then
            assertEquals(0.009475, inductance, DELTA6);
        }

        @Test
        void testSolenoidInductanceSolveForRadius() {
            // given
            final byte numberOfTurns = 10;
            final double inductance = 0.009475;
            final double length = 1.5;
            // when
            final double radius = PhysicsCalc.Electronics
                .solenoidInductanceSolveForRadius(numberOfTurns, length, inductance);
            // then
            assertEquals(6, radius, DELTA1);
        }

        @Test
        void testSolenoidInductanceSolveForRadiusGivenCrossSectionArea() {
            // given
            final double crossSectionalArea = 113.1;
            // when
            final double radius = PhysicsCalc.Electronics
                .solenoidInductanceSolveForRadiusGivenCrossSectionArea(crossSectionalArea);
            // then
            assertEquals(6, radius, DELTA1);
        }

        @Test
        void testSolenoidInductanceSolveForLength() {
            // given
            final byte numberOfTurns = 10;
            final double inductance = 0.009475;
            final double radius = 6;
            // when
            final double length = PhysicsCalc.Electronics
                .solenoidInductanceSolveForLength(numberOfTurns, radius, inductance);
            // then
            assertEquals(1.5, length, DELTA1);
        }

        @Test
        void testSolenoidInductanceSolveForLengthGivenCrossSectionArea() {
            // given
            final byte numberOfTurns = 10;
            final double inductance = 0.009475;
            final double crossSectionalArea = 113.1;
            // when
            final double length = PhysicsCalc.Electronics
                .solenoidInductanceSolveForLengthGivenCrossSectionArea(numberOfTurns, crossSectionalArea, inductance);
            // then
            assertEquals(1.5, length, DELTA1);
        }

        @Test
        void testStepUpVoltageRegulation() {
            // given
            final short voltageNoLoad = 140;
            final byte voltageFullLoad = 120;
            // when
            final double[] stepUpVoltage = PhysicsCalc.Electronics
                .stepUpVoltageRegulation(voltageNoLoad, voltageFullLoad);
            // then
            assertArrayEquals(new double[]{0.1667, 16.67}, stepUpVoltage, DELTA2);
        }

        @Test
        void testStepDownVoltageRegulation() {
            // given
            final short voltageNoLoad = 140;
            final byte voltageFullLoad = 120;
            // when
            final double[] stepDownVoltage = PhysicsCalc.Electronics
                .stepDownVoltageRegulation(voltageNoLoad, voltageFullLoad);
            // then
            assertArrayEquals(new double[]{0.143, 14.28}, stepDownVoltage, DELTA2);
        }
    }
}
