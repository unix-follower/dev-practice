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
        void testMomentum() {
            // given
            final byte massKg = 65;
            final byte velocity = 2;
            // when
            final double momentum = PhysicsCalc.Kinematics.momentum(massKg, velocity);
            // then
            assertEquals(130, momentum, DELTA1);
        }

        @Test
        void testVelocity() {
            // given
            final short distance = 500;
            final short timeSeconds = 180;
            // when
            final double velocity = PhysicsCalc.Kinematics.velocity(distance, timeSeconds);
            // then
            assertEquals(2.77778, velocity, DELTA5);
        }

        @Test
        void testInitialVelocity() {
            // given
            final double finalVelocity = 28.8;
            final double acceleration = 6.95;
            final byte timeSeconds = 4;
            // when
            final double initialVelocity = PhysicsCalc.Kinematics
                .initialVelocity(finalVelocity, acceleration, timeSeconds);
            // then
            assertEquals(1, initialVelocity, DELTA1);
        }

        @Test
        void testFinalVelocity() {
            // given
            final byte initialVelocity = 1;
            final double acceleration = 6.95;
            final byte timeSeconds = 4;
            // when
            final double velocity = PhysicsCalc.Kinematics.finalVelocity(initialVelocity, acceleration, timeSeconds);
            // then
            assertEquals(28.8, velocity, DELTA1);
        }

        @Test
        void testAvgVelocity() {
            // given
            final double[][] velocities = new double[][]{{1, MathCalc.ONE_HALF}, {5, 1}, {10, 3}};
            // when
            final double velocity = PhysicsCalc.Kinematics.avgVelocity(velocities);
            // then
            assertEquals(7.8889, velocity, DELTA4);
        }

        @Test
        void testVelocityMagnitude2d() {
            // given
            final double velocityXDirection = 2;
            final double velocityYDirection = 3;
            final double[] velocity2d = new double[]{velocityXDirection, velocityYDirection};
            // when
            final double magnitude = PhysicsCalc.Kinematics.velocityMagnitude(velocity2d);
            // then
            assertEquals(3.6056, magnitude, DELTA1);
        }

        @Test
        void testMomentumMagnitude2d() {
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
        void testVelocityMagnitude3d() {
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
        void testMomentumMagnitude3d() {
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
        void testTerminalVelocityOfHumanSkydiver() {
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
            assertEquals(98.48, terminalVelocity, DELTA2);
        }

        @Test
        void testTerminalVelocityOfBaseball() {
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
            assertEquals(41.056, terminalVelocity, DELTA3);
        }

        @Test
        void testTerminalVelocityOfGolfBall() {
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
            assertEquals(32.734, terminalVelocity, DELTA3);
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

        @Test
        void testImpulse() {
            // given
            final double massKg = 0.16;
            final double initialVelocity = 2.5;
            final byte finalVelocity = 0;
            // when
            final double impulse = PhysicsCalc.Kinematics.impulse(massKg, initialVelocity, finalVelocity);
            // then
            assertEquals(-0.4, impulse, DELTA1);
        }

        @Test
        void testForceFromImpulse() {
            // given
            final double impulse = -0.4;
            final byte timeIntervalSeconds = 8;
            // when
            final double force = PhysicsCalc.Kinematics.forceFromImpulse(impulse, timeIntervalSeconds);
            // then
            assertEquals(-0.05, force, DELTA2);
        }

        @Test
        void testTimeIntervalOfImpulse() {
            // given
            final double impulse = -0.4;
            final double force = -0.05;
            // when
            final double timeIntervalSeconds = PhysicsCalc.Kinematics.timeIntervalOfImpulse(impulse, force);
            // then
            assertEquals(8, timeIntervalSeconds, DELTA1);
        }

        @Test
        void testInitialMomentumFromImpulse() {
            // given
            final double impulse = -0.4;
            final byte finalMomentum = 0;
            // when
            final double initialMomentum = PhysicsCalc.Kinematics.initialMomentumFromImpulse(impulse, finalMomentum);
            // then
            assertEquals(0.4, initialMomentum, DELTA1);
        }

        @Test
        void testFinalMomentumFromImpulse() {
            // given
            final double impulse = -0.4;
            final double initialMomentum = 0.4;
            // when
            final double finalMomentum = PhysicsCalc.Kinematics.finalMomentumFromImpulse(impulse, initialMomentum);
            // then
            assertEquals(0, finalMomentum, DELTA1);
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
            final double golfBallMass = MassUnit.gramsToKg(45.9);
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
            final double golfBallMass = MassUnit.gramsToKg(45.9);
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

        @Test
        void testPoissonsRatio() {
            // given
            final double transverseStrain = 0.4;
            final double axialStrain = 0.9;
            // when
            final double ratio = PhysicsCalc.Mechanics.poissonsRatio(transverseStrain, axialStrain);
            // then
            assertEquals(0.444444, ratio, DELTA6);
        }

        @Test
        void testShearStress() {
            // given
            final byte forceMagnitude = 10;
            final byte area = 5;
            // when
            final double stress = PhysicsCalc.Mechanics.shearStress(forceMagnitude, area);
            // then
            assertEquals(2, stress, DELTA1);
        }

        @Test
        void testShearStrain() {
            // given
            final byte displacement = 1;
            final byte transverseLength = 2;
            // when
            final double strain = PhysicsCalc.Mechanics.shearStrain(displacement, transverseLength);
            // then
            assertEquals(0.5, strain, DELTA1);
        }

        @Test
        void testShearModulus() {
            // given
            final byte forceMagnitude = 10;
            final byte transverseLength = 2;
            final byte area = 5;
            final byte displacement = 1;
            // when
            final double modulus = PhysicsCalc.Mechanics
                .shearModulus(forceMagnitude, area, displacement, transverseLength);
            // then
            assertEquals(4, modulus, DELTA1);
        }

        @Test
        void testShearModulusFromShearStressAndStrain() {
            // given
            final byte stress = 2;
            final double strain = 0.5;
            // when
            final double modulus = PhysicsCalc.Mechanics
                .shearModulusFromShearStressAndStrain(stress, strain);
            // then
            assertEquals(4, modulus, DELTA1);
        }
    }

    @Nested
    class FluidMechanics {
        @Test
        void testFanMassAirflowInCFM() {
            // given
            final double powerOutput = PowerUnit.wattsToMechanicalHorsepower(25);
            final double pressure = PressureUnit.pascalsToInchesOfWater(350);
            final byte efficiency = 1;
            // when
            final double mass = PhysicsCalc.FluidMechanics.fanMassAirflowInCFM(powerOutput, pressure, efficiency);
            // then
            assertEquals(151.65, mass, DELTA2);
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
        void testAccelerationWithSpeedDifference() {
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
        void testAccelerationWithMassAndForce() {
            // given
            final int massInKg = 60;
            final int netForceInNewtons = 1000;
            // when
            final double acceleration = PhysicsCalc.Dynamics.acceleration(massInKg, netForceInNewtons);
            // then
            assertEquals(16.667, acceleration, 0.001);
        }

        @Test
        void testAccelerationWithDistanceTraveled() {
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

        @Test
        void testDeceleratingForce() {
            // given
            final byte massKg = 50;
            final double initialVelocity = 13.89;
            final int finalVelocity = 0;
            final int changeInTime = 8;
            final double acceleration = PhysicsCalc.Dynamics.acceleration(initialVelocity, finalVelocity, changeInTime);
            // when
            final double deceleratingForce = PhysicsCalc.Dynamics.force(massKg, acceleration);
            // then
            assertEquals(-86.8, deceleratingForce, DELTA1);
        }

        @Test
        void testGravitationalForceBetweenEarthAndSun() {
            // given
            final double earthMassKg = 5.972e24;
            final double sunMassKg = 1.989e30;
            final double distanceFromEarthToSun = LengthUnit.kilometersToMeters(149_600_000);
            // when
            final double force = PhysicsCalc.Dynamics
                .gravitationalForce(earthMassKg, sunMassKg, distanceFromEarthToSun);
            // then
            assertEquals(3.5423960813684978123481e+22, force, 1e22);
        }

        @Test
        void testGravitationalForceBetweenEarthAndMoon() {
            // given
            final double earthMassKg = 5.972e24;
            final double moonMassKg = 7.348e22;
            final double distanceFromEarthToSun = 3.844e8;
            // when
            final double force = PhysicsCalc.Dynamics
                .gravitationalForce(earthMassKg, moonMassKg, distanceFromEarthToSun);
            // then
            assertEquals(198_211_072_907_925_212_312d, force, 1e21);
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

        @Test
        void testLorentzForce() {
            // given
            final double magneticField = MathCalc.ONE_HALF;
            final double charge = 1.602e-19;
            final double velocity = 2.998e7;
            final double angle = MathCalc.Trigonometry.PI_OVER_2;
            // when
            final double force = PhysicsCalc.Electromagnetism.lorentzForce(magneticField, charge, velocity, angle);
            // then
            assertEquals(2.4e-12, force, DELTA1);
        }

        @Test
        void testElectricField() {
            // given
            final double charge = ElectricalChargeUnit.elementaryChargeToCoulomb(10);
            final double distance = 0.00023;
            // when
            final double electricField = PhysicsCalc.Electromagnetism.electricField(charge, distance);
            // then
            assertEquals(0.0002722, ElectricalChargeUnit.newtonPerCoulombToKiloNC(electricField), DELTA7);
        }

        @Test
        void testElectricFieldOfPointCharges() {
            // given
            final double charge = 0.3;
            final double charge2 = 0.2;
            final double distance = 0.000001;
            final double distance2 = 0.00001;
            // when
            final double electricField1 = PhysicsCalc.Electromagnetism.electricField(charge, distance);
            final double electricField2 = PhysicsCalc.Electromagnetism.electricField(charge2, distance2);
            final double electricField = electricField1 + electricField2;
            // then
            assertEquals(2.714240639976e+21, electricField, 1e15);
        }

        @Test
        void testElectricFieldWithRelativePermittivity() {
            // given
            final double charge = ElectricalChargeUnit.elementaryChargeToCoulomb(10);
            final double distance = 0.00023;
            final byte relativePermittivity = 2;
            // when
            final double electricField = PhysicsCalc.Electromagnetism
                .electricField(charge, distance, relativePermittivity);
            // then
            assertEquals(0.0001361, ElectricalChargeUnit.newtonPerCoulombToKiloNC(electricField), DELTA7);
        }

        @Test
        void testElectricPotential() {
            // given
            final byte charge = 10;
            final double distance = 0.00023;
            // when
            final double electricPotential = PhysicsCalc.Electromagnetism.electricPotential(charge, distance);
            // then
            assertEquals(390_763_121_217_391d, electricPotential, 1e14);
        }

        @Test
        void testElectricPotentialWithRelativePermittivity() {
            // given
            final byte charge = 10;
            final double distance = 0.00023;
            final byte relativePermittivity = 2;
            // when
            final double electricPotential = PhysicsCalc.Electromagnetism
                .electricPotential(charge, distance, relativePermittivity);
            // then
            assertEquals(19_538_156_060_8696d, electricPotential, 1e14);
        }

        @Test
        void testElectricPotentialOfPointCharges() {
            // given
            final double charge = 0.3;
            final double charge2 = 0.2;
            final double distance = 0.000001;
            final double distance2 = 0.00001;
            final byte relativePermittivity = 2;
            // when
            final double electricPotential1 = PhysicsCalc.Electromagnetism
                .electricPotential(charge, distance, relativePermittivity);
            final double electricPotential2 = PhysicsCalc.Electromagnetism
                .electricPotential(charge2, distance2, relativePermittivity);
            final double electricPotential = electricPotential1 + electricPotential2;
            // then
            assertEquals(1_438_008_286_080_000d, electricPotential, 1e21);
        }

        @Test
        void testElectricPotentialDifference() {
            // given
            final byte charge = 10;
            final byte electricPotentialEnergy = 15;
            // when
            final double difference = PhysicsCalc.Electromagnetism
                .electricPotentialDifference(charge, electricPotentialEnergy);
            // then
            assertEquals(1.5, difference, DELTA1);
        }

        @Test
        void testFaradayLawMagneticFlux() {
            // given
            final double coilCrossSectionalArea = 0.003;
            final byte turns = 10;
            final double magneticField = 0.4;
            // when
            final double magneticFlux = PhysicsCalc.Electromagnetism
                .faradayLawMagneticFlux(coilCrossSectionalArea, turns, magneticField);
            // then
            assertEquals(0.0012, magneticFlux, DELTA4);
        }

        @Test
        void testFaradayLawInducedVoltage() {
            // given
            final byte turns = 10;
            final double magneticFlux = 0.0012;
            final byte timeSeconds = 8;
            // when
            final double inducedVoltage = PhysicsCalc.Electromagnetism
                .faradayLawInducedVoltage(magneticFlux, turns, timeSeconds);
            // then
            assertEquals(-0.0015, inducedVoltage, DELTA4);
        }

        @Test
        void testFrequencyBandwidth() {
            // given
            final int centerFrequencyHz = 93_700_000;
            final short qualityFactor = 500;
            // when
            final double bandwidth = PhysicsCalc.Electromagnetism.frequencyBandwidth(centerFrequencyHz, qualityFactor);
            // then
            assertEquals(187_400, bandwidth, DELTA1);
        }

        @Test
        void testLowerCutoffFrequency() {
            // given
            final int centerFrequencyHz = 93_700_000;
            final short qualityFactor = 500;
            // when
            final double lower = PhysicsCalc.Electromagnetism.lowerCutoffFrequency(centerFrequencyHz, qualityFactor);
            // then
            assertEquals(93_606_347.0, lower, 1e1);
        }

        @Test
        void testUpperCutoffFrequency() {
            // given
            final int centerFrequencyHz = 93_700_000;
            final short qualityFactor = 500;
            // when
            final double upper = PhysicsCalc.Electromagnetism.upperCutoffFrequency(centerFrequencyHz, qualityFactor);
            // then
            assertEquals(93793747.0, upper, 1e1);
        }

        @Test
        void testPowerDissipationInSeries() {
            // given
            final byte voltage = 12;
            final double[] resistors = {2, 6, 1};
            // when
            final double dissipatedPower = PhysicsCalc.Electromagnetism.powerDissipationInSeries(voltage, resistors);
            // then
            assertEquals(16, dissipatedPower, DELTA1);
        }

        @Test
        void testPowerDissipationInParallel() {
            // given
            final byte voltage = 12;
            final double[] resistors = {2, 6, 1};
            // when
            final double dissipatedPower = PhysicsCalc.Electromagnetism.powerDissipationInParallel(voltage, resistors);
            // then
            assertEquals(240, dissipatedPower, DELTA1);
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

        @Test
        void testRcLowPassFilter() {
            // given
            final double resistance = ElectricalResistanceUnit.kiloOhmsToOhms(3.3);
            final double capacitance = CapacitanceUnit.nanoFaradsToFarads(47);
            // when
            final double cutoffFrequency = PhysicsCalc.Electronics.rcLowPassFilter(resistance, capacitance);
            // then
            assertEquals(1026.1, cutoffFrequency, DELTA1);
        }

        @Test
        void testRlLowPassFilter() {
            // given
            final short resistance = 3300;
            final double inductance = 20;
            // when
            final double cutoffFrequency = PhysicsCalc.Electronics.rlLowPassFilter(resistance, inductance);
            // then
            assertEquals(26.26, cutoffFrequency, DELTA2);
        }

        @Test
        void testInvertingOpAmpLowPassFilter() {
            // given
            final byte feedbackResistance = 12;
            final short capacitance = 345;
            // when
            final double cutoffFrequency = PhysicsCalc.Electronics
                .invertingOpAmpLowPassFilter(feedbackResistance, capacitance);
            // then
            assertEquals(0.00003844, cutoffFrequency, DELTA8);
        }

        @Test
        void testInvertingOpAmpLowPassFilterGain() {
            // given
            final byte inputResistance = 16;
            final byte feedbackResistance = 12;
            // when
            final double gain = PhysicsCalc.Electronics
                .invertingOpAmpLowPassFilterGain(inputResistance, feedbackResistance);
            // then
            assertEquals(-0.75, gain, DELTA2);
        }

        @Test
        void testNonInvertingOpAmpLowPassFilter() {
            // given
            final byte inputResistance = 16;
            final short capacitance = 345;
            // when
            final double cutoffFrequency = PhysicsCalc.Electronics
                .nonInvertingOpAmpLowPassFilter(inputResistance, capacitance);
            // then
            assertEquals(0.00002883, cutoffFrequency, DELTA8);
        }

        @Test
        void testNonInvertingOpAmpLowPassFilterGain() {
            // given
            final byte feedbackResistance = 12;
            final byte positiveToGroundResistance = 17;
            // when
            final double gain = PhysicsCalc.Electronics
                .nonInvertingOpAmpLowPassFilterGain(feedbackResistance, positiveToGroundResistance);
            // then
            assertEquals(1.706, gain, DELTA3);
        }

        @Test
        void testTransformerSize() {
            // given
            final byte loadCurrentAmps = 80;
            final short loadVoltage = 1500;
            // when
            final double minKVARequired = PhysicsCalc.Electronics.transformerSize(loadCurrentAmps, loadVoltage);
            // then
            assertEquals(120, minKVARequired, DELTA1);
        }

        @Test
        void testTransformerSizeWithSpareCapacity() {
            // given
            final byte loadCurrentAmps = 80;
            final short loadVoltage = 1500;
            final byte spareCapacityPercent = 20;
            // when
            final double kVA = PhysicsCalc.Electronics
                .transformerSize(loadCurrentAmps, loadVoltage, spareCapacityPercent);
            // then
            assertEquals(144, kVA, DELTA1);
        }

        @Test
        void testThreePhaseTransformerSize() {
            // given
            final short loadCurrentAmps = 250;
            final short loadVoltage = 2000;
            // when
            final double minKVARequired = PhysicsCalc.Electronics
                .threePhaseTransformerSize(loadCurrentAmps, loadVoltage);
            // then
            assertEquals(866, minKVARequired, DELTA1);
        }

        @Test
        void testThreePhaseTransformerSizeWithSpareCapacity() {
            // given
            final short loadCurrentAmps = 250;
            final short loadVoltage = 2000;
            final byte spareCapacityPercent = 20;
            // when
            final double minKVARequired = PhysicsCalc.Electronics
                .threePhaseTransformerSize(loadCurrentAmps, loadVoltage, spareCapacityPercent);
            // then
            assertEquals(1039.2, minKVARequired, DELTA1);
        }
    }

    @Nested
    class Acoustics {
        @Test
        void testSoundSpeedFromTemperature() {
            // given
            final byte temperature = 20;
            // when
            final double speed = PhysicsCalc.Acoustics.soundSpeed(temperature);
            // then
            assertEquals(343.21, speed, DELTA2);
        }

        @Test
        void testSoundSpeedInWater() {
            // given
            final byte temperature = 20;
            // when
            final double speed = PhysicsCalc.Acoustics.soundSpeedInWater(temperature);
            // then
            assertEquals(1482, speed, 1);
        }

        @Test
        void testSoundSpeed() {
            // given
            final double wavelength = 1.6333;
            final double frequency = 210;
            // when
            final double speedSound = PhysicsCalc.Acoustics.soundSpeed(wavelength, frequency);
            // then
            assertEquals(PhysicsCalc.SOUND_SPEED, speedSound, DELTA1);
        }

        @Test
        void testSoundPressureLevel() {
            // given
            final double referencePressure = 0.00002;
            final double chainsawSoundWavePressure = 6.4;
            // when
            final double spl = PhysicsCalc.Acoustics.soundPressureLevel(referencePressure, chainsawSoundWavePressure);
            // then
            assertEquals(110.103, spl, DELTA3);
        }

        @Test
        void testSoundIntensityLevel() {
            // given
            final double referenceIntensity = 0.000000000001;
            final double chainsawSoundIntensity = 0.1;
            // when
            final double sil = PhysicsCalc.Acoustics.soundIntensityLevel(referenceIntensity, chainsawSoundIntensity);
            // then
            assertEquals(110, sil, DELTA1);
        }

        @Test
        void testSoundIntensityAtDistance() {
            // given
            final double chainsawSoundIntensity = 0.1;
            final double distance = 2.23906;
            final double power = chainsawSoundIntensity * MathCalc.Trigonometry.PI4 * distance * distance;
            // when
            final double soundIntensity = PhysicsCalc.Acoustics.soundIntensityAtDistance(power, distance);
            // then
            assertEquals(0.1, soundIntensity, DELTA1);
        }

        @Test
        void testBeatFrequency() {
            // given
            final double firstWaveFrequency = 1400;
            final double secondWaveFrequency = 1560;
            // when
            final double beatFrequency = PhysicsCalc.Acoustics.beatFrequency(firstWaveFrequency, secondWaveFrequency);
            // then
            assertEquals(160, beatFrequency, DELTA1);
        }

        @Test
        void testSoundWavelength() {
            // given
            final short speedOfSound = PhysicsCalc.SOUND_SPEED;
            final short frequency = 210;
            // when
            final double wavelength = PhysicsCalc.Acoustics.soundWavelength(speedOfSound, frequency);
            // then
            assertEquals(1.6333, wavelength, DELTA4);
        }

        @Test
        void testSoundFrequency() {
            // given
            final short speedOfSound = PhysicsCalc.SOUND_SPEED;
            final double wavelength = 1.6333;
            // when
            final double frequency = PhysicsCalc.Acoustics.soundFrequency(speedOfSound, wavelength);
            // then
            assertEquals(210, frequency, DELTA1);
        }

        @Test
        void testSoundAbsorptionCoefficient() {
            // given
            final double incidentSoundIntensity = 0.9;
            final double absorbedSoundIntensity = 0.3;
            // when
            final double coeff = PhysicsCalc.Acoustics
                .soundAbsorptionCoefficient(incidentSoundIntensity, absorbedSoundIntensity);
            // then
            assertEquals(0.3333, coeff, DELTA4);
        }

        @Test
        void testTotalRoomSoundAbsorption() {
            // given
            final double[] surfaceAreas = {15, 2};
            final double[] absorptionCoefficients = {0.3, 0.2};
            // when
            final double totalAbsorption = PhysicsCalc.Acoustics
                .totalRoomSoundAbsorption(surfaceAreas, absorptionCoefficients);
            // then
            assertEquals(52.74, LengthUnit.squareMeterToSquareFeet(totalAbsorption), DELTA2);
        }

        @Test
        void testAvgSoundAbsorptionCoefficient() {
            // given
            final double absorptionOfRoom = 4.9;
            final byte totalSurfaceInRoom = 17;
            // when
            final double averageCoeff = PhysicsCalc.Acoustics
                .avgSoundAbsorptionCoefficient(absorptionOfRoom, totalSurfaceInRoom);
            // then
            assertEquals(0.28824, averageCoeff, DELTA5);
        }
    }

    @Nested
    class Optics {
        @Test
        void testLightSpeed() {
            // given
            final byte timeSeconds = 60;
            // when
            final double distance = PhysicsCalc.Optics.lightSpeed(timeSeconds);
            // then
            assertEquals(17_987_547_480.0, distance, DELTA1);
        }

        @Test
        void testAngularResolution() {
            // given
            final double wavelength = 5.5e-8;
            final double apertureDiameter = 0.002;
            // when
            final double resolution = PhysicsCalc.Optics.angularResolution(wavelength, apertureDiameter);
            // then
            assertEquals(0.00003355, resolution, DELTA8);
        }

        @Test
        void testBinocularsRange() {
            // given
            final double objectHeight = 6;
            final double objectAngularHeight = 1;
            // when
            final double distanceToObject = PhysicsCalc.Optics.binocularsRange(objectHeight, objectAngularHeight);
            // then
            assertEquals(6000, distanceToObject, DELTA1);
        }

        @Test
        void testTelescopeEyepieceFocalLength() {
            // given
            final short objectiveFocalPoint = 675;
            final byte magnification = 27;
            // when
            final double eyepieceFocalLength = PhysicsCalc.Optics
                .telescopeEyepieceFocalLength(objectiveFocalPoint, magnification);
            // then
            assertEquals(25, eyepieceFocalLength, DELTA1);
        }

        @Test
        void testTelescopeObjectiveFocalPoint() {
            // given
            final short objectiveDiameter = 135;
            final byte fRatio = 5;
            // when
            final double telescopeFocalLength = PhysicsCalc.Optics
                .telescopeObjectiveFocalPoint(objectiveDiameter, fRatio);
            // then
            assertEquals(675, telescopeFocalLength, DELTA1);
        }

        @Test
        void testTelescopeMagnification() {
            // given
            final byte eyepieceFocalLength = 4;
            final short telescopeFocalLength = 400;
            // when
            final double magnification = PhysicsCalc.Optics
                .telescopeMagnification(telescopeFocalLength, eyepieceFocalLength);
            // then
            assertEquals(100, magnification, DELTA1);
        }

        @Test
        void testTelescopeFOV() {
            // given
            final byte apparentFOV = PhysicsCalc.Optics.TELESCOPE_STD_FOV;
            final byte magnification = 100;
            // when
            final double fov = PhysicsCalc.Optics.telescopeFOV(apparentFOV, magnification);
            // then
            assertEquals(1872, fov, DELTA1);
        }

        @Test
        void testTelescopeAreaFOV() {
            // given
            final short fov = 1872;
            // when
            final double areaFOV = PhysicsCalc.Optics.telescopeAreaFOV(fov);
            // then
            assertEquals(0.21237, areaFOV, DELTA1);
        }

        @Test
        void testTelescopeScopeFOV() {
            // given
            final short magnification = 27;
            final byte eyepieceFOV = PhysicsCalc.Optics.TELESCOPE_STD_FOV;
            // when
            final double scopeFOV = PhysicsCalc.Optics.telescopeScopeFOV(magnification, eyepieceFOV);
            // then
            assertEquals(1.93, scopeFOV, DELTA2);
        }

        @Test
        void testTelescopeMinMagnification() {
            // given
            final short objectiveDiameter = 135; // mm
            // when
            final double minMagnification = PhysicsCalc.Optics.telescopeMinMagnification(objectiveDiameter);
            // then
            assertEquals(19.286, minMagnification, DELTA3);
        }

        @Test
        void testTelescopeResolvingPower() {
            // given
            final short objectiveDiameter = 135; // mm
            // when
            final double resolvingPower = PhysicsCalc.Optics.telescopeResolvingPower(objectiveDiameter);
            // then
            assertEquals(0.86, resolvingPower, DELTA2);
        }

        @Test
        void testTelescopeExitPupilDiameter() {
            // given
            final short objectiveDiameter = 135; // mm
            final byte magnification = 27;
            // when
            final double exitPupilDiameter = PhysicsCalc.Optics
                .telescopeExitPupilDiameter(objectiveDiameter, magnification);
            // then
            assertEquals(5, exitPupilDiameter, DELTA1);
        }

        @Test
        void testTelescopeSurfaceBrightness() {
            // given
            final byte exitPupilDiameter = 5;
            // when
            final double surfaceBrightness = PhysicsCalc.Optics.telescopeSurfaceBrightness(exitPupilDiameter);
            // then
            assertEquals(50, surfaceBrightness, DELTA1);
        }

        @Test
        void testTelescopeStarMagnitudeLimit() {
            // given
            final short objectiveDiameter = 135; // mm
            // when
            final double magnitudeLimit = PhysicsCalc.Optics.telescopeStarMagnitudeLimit(objectiveDiameter);
            // then
            assertEquals(12.7, magnitudeLimit, DELTA1);
        }
    }

    @Nested
    class Thermodynamics {
        @Test
        void testThermalConductivity() {
            // given
            final double brickWallThermalConductivity = 0.8;
            final byte temperatureDifference = 20;
            final double distance = 0.35;
            // when
            final double heatFlux = PhysicsCalc.Thermodynamics
                .thermalConductivity(brickWallThermalConductivity, temperatureDifference, distance);
            // then
            assertEquals(-45.71, heatFlux, DELTA2);
        }

        @Test
        void testThermalEnergy() {
            // given
            final byte degreesOfFreedom = PhysicsCalc.MONOATOMIC_GAS_DEGREES_OF_FREEDOM;
            final double molarMass = MassUnit.gramsToKg(10);
            final byte temperature = 20;
            final byte molesOfGas = 4;
            // when
            final double[] energyData = PhysicsCalc.Thermodynamics
                .thermalEnergy(degreesOfFreedom, molarMass, temperature, molesOfGas);
            // then
            assertArrayEquals(new double[]{4.142e-22, 223.35, 997.73}, energyData, DELTA2);
        }

        @Test
        void testThermalLinearExpansionChangeInLength() {
            // given
            final double copperLinearExpansionCoeff = 16.6e-6;
            final byte initialLength = 12;
            final byte initialTemperature = 1;
            final byte finalTemperature = 60;
            // when
            final double changeInLength = PhysicsCalc.Thermodynamics.thermalLinearExpansionChangeInLength(
                copperLinearExpansionCoeff, initialLength, initialTemperature, finalTemperature);
            // then
            assertEquals(0.0117528, changeInLength, DELTA7);
        }

        @Test
        void testThermalLinearExpansionFinalLength() {
            // given
            final byte initialLength = 12;
            final double changeInLength = 0.0117528;
            // when
            final double finalLength = PhysicsCalc.Thermodynamics
                .thermalLinearExpansionFinalLength(initialLength, changeInLength);
            // then
            assertEquals(12.0117528, finalLength, DELTA7);
        }

        @Test
        void testThermalVolumetricExpansionChangeInLength() {
            // given
            final double copperVolumetricExpansionCoeff = 3 * 16.6e-6;
            final byte initialVolume = 10;
            final byte initialTemperature = 1;
            final byte finalTemperature = 60;
            // when
            final double changeInVolume = PhysicsCalc.Thermodynamics.thermalVolumetricExpansionChangeInVolume(
                copperVolumetricExpansionCoeff, initialVolume, initialTemperature, finalTemperature);
            // then
            assertEquals(0.029382, changeInVolume, DELTA6);
        }

        @Test
        void testThermalVolumetricExpansionFinalLength() {
            // given
            final byte initialVolume = 10;
            final double changeInVolume = 0.029382;
            // when
            final double finalVolume = PhysicsCalc.Thermodynamics
                .thermalVolumetricExpansionFinalVolume(initialVolume, changeInVolume);
            // then
            assertEquals(10.02938, finalVolume, DELTA5);
        }

        @Test
        void testThermalResistanceOfPlate() {
            // given
            final short copperThermalConductivity = 401;
            final double thickness = 0.05;
            final double crossSectionalArea = 0.0025;
            // when
            final double finalVolume = PhysicsCalc.Thermodynamics
                .thermalResistanceOfPlate(copperThermalConductivity, thickness, crossSectionalArea);
            // then
            assertEquals(0.04988, finalVolume, DELTA5);
        }

        @Test
        void testThermalResistanceOfHollowCylinder() {
            // given
            final short copperThermalConductivity = 401;
            final double length = MathCalc.ONE_HALF;
            final double innerRadius = 0.1;
            final double outerRadius = 0.2;
            // when
            final double resistance = PhysicsCalc.Thermodynamics
                .thermalResistanceOfHollowCylinder(copperThermalConductivity, length, innerRadius, outerRadius);
            // then
            assertEquals(0.0005502, resistance, DELTA7);
        }

        @Test
        void testThermalResistanceOfHollowSphere() {
            // given
            final short copperThermalConductivity = 401;
            final double innerRadius = 0.1;
            final double outerRadius = 0.2;
            // when
            final double resistance = PhysicsCalc.Thermodynamics
                .thermalResistanceOfHollowSphere(copperThermalConductivity, innerRadius, outerRadius);
            // then
            assertEquals(0.0009922, resistance, DELTA7);
        }

        @Test
        void testThermalResistanceOfHollowCylinderCriticalRadius() {
            // given
            final short copperThermalConductivity = 401;
            final double heatTransferCoeff = 0.6;
            // when
            final double criticalRadius = PhysicsCalc.Thermodynamics
                .thermalResistanceOfHollowCylinderCriticalRadius(copperThermalConductivity, heatTransferCoeff);
            // then
            assertEquals(1336.6, criticalRadius, DELTA1);
        }

        @Test
        void testThermalResistanceOfHollowSphereCriticalRadius() {
            // given
            final short copperThermalConductivity = 401;
            final double heatTransferCoeff = 0.6;
            // when
            final double criticalRadius = PhysicsCalc.Thermodynamics
                .thermalResistanceOfHollowSphereCriticalRadius(copperThermalConductivity, heatTransferCoeff);
            // then
            assertEquals(668.3, criticalRadius, DELTA1);
        }

        @Test
        void testSpecificHeat() {
            // given
            final short energy = 20_500;
            final byte iceBlockMassKg = 1;
            final byte changeInTemperature = 10;
            // when
            final double totalEnergy = PhysicsCalc.Thermodynamics
                .specificHeat(energy, iceBlockMassKg, changeInTemperature);
            // then
            assertEquals(2050, totalEnergy, DELTA1);
        }

        @Test
        void testWaterHeating() {
            // given
            final byte iceBlockMassKg = 1;
            final short initialTempCelsius = -10;
            final byte finalTempCelsius = 0;
            final short specificHeat = 2108;
            // when
            final double totalEnergy = PhysicsCalc.Thermodynamics
                .waterHeating(iceBlockMassKg, initialTempCelsius, finalTempCelsius, specificHeat);
            // then
            assertEquals(21_080, totalEnergy, DELTA1);
        }

        @Test
        void testWaterHeatingTime() {
            // given
            final int totalEnergy = 757_320;
            final short heatingPower = 1800;
            final double efficiency = 0.9; // 90%
            // when
            final double timeSeconds = PhysicsCalc.Thermodynamics
                .waterHeatingTime(totalEnergy, heatingPower, efficiency);
            // then
            assertEquals(467.48, timeSeconds, DELTA2);
        }
    }

    @Nested
    class Atmospheric {
        @Test
        void testDryAirDensity() {
            // given
            final double airPressure = PressureUnit.hpaToPa(1013.25);
            final double airTemperature = TemperatureUnit.celsiusToKelvin(15);
            // when
            final double airDensity = PhysicsCalc.Atmospheric.dryAirDensity(airPressure, airTemperature);
            // then
            assertEquals(1.225, airDensity, DELTA3);
        }

        @Test
        void testMoistAirDensity() {
            // given
            final double airPressure = PressureUnit.hpaToPa(1013.25);
            final double airTemperature = TemperatureUnit.celsiusToKelvin(15);
            final byte relativeHumidity = 70;
            // when
            final double airDensity = PhysicsCalc.Atmospheric
                .moistAirDensity(airPressure, airTemperature, relativeHumidity);
            // then
            assertEquals(1.21955, airDensity, DELTA5);
        }

        @Test
        void testMoistAirDensityDewPoint() {
            // given
            final double airTemperature = 15;
            final byte relativeHumidity = 70;
            // when
            final double dewPoint = PhysicsCalc.Atmospheric.moistAirDensityDewPoint(airTemperature, relativeHumidity);
            // then
            assertEquals(9.57, dewPoint, DELTA2);
        }

        @Test
        void testWaterVaporPressure() {
            // given
            final double airTemperature = 15;
            final byte relativeHumidity = 70;
            // when
            final double pressure = PhysicsCalc.Atmospheric.waterVaporPressure(airTemperature, relativeHumidity);
            // then
            assertEquals(11.923, pressure, DELTA1);
        }
    }

    @Nested
    class Astrophysics {
        @Test
        void testFinalBlackHoleMass() {
            // given
            final byte blackHoleMass = 5; // Suns
            final byte fallingObjectMass = 3; // Suns
            // when
            final double finalMass = PhysicsCalc.Astrophysics.finalBlackHoleMass(blackHoleMass, fallingObjectMass);
            // then
            assertEquals(7.79, finalMass, DELTA2);
        }

        @Test
        void testFinalBlackHoleEventHorizonRadius() {
            // given
            final double eventHorizonRadius = 14.77;
            final double eventHorizonGrowth = 0.558; // 55.8%
            // when
            final double finalRadius = PhysicsCalc.Astrophysics
                .finalBlackHoleEventHorizonRadius(eventHorizonRadius, eventHorizonGrowth);
            // then
            assertEquals(23.01, finalRadius, DELTA2);
        }

        @Test
        void testBlackHoleEnergyRelease() {
            // given
            final double fallingObjectMass = 3 * 0.07; // 7% of the mass
            // when
            final double energy = PhysicsCalc.Astrophysics.blackHoleEnergyRelease(fallingObjectMass);
            // then
            assertEquals(375.4, energy, DELTA1);
        }

        @Test
        void testBlackHoleGravitationalField() {
            // given
            final double blackHoleMass = MassUnit.sunsToKg(5);
            final double eventHorizonRadius = LengthUnit.kilometersToMeters(14.77);
            // when
            final double gravitationalField = PhysicsCalc.Astrophysics
                .blackHoleGravitationalField(blackHoleMass, eventHorizonRadius);
            // then
            assertEquals(3.0426326443517363e12, gravitationalField, 1e12);
        }

        @Test
        void testBlackHoleGravitationalFieldAfterMerge() {
            // given
            final double finalBlackHoleMass = MassUnit.sunsToKg(7.79);
            final double eventHorizonRadius = LengthUnit.kilometersToMeters(23.01);
            // when
            final double gravitationalField = PhysicsCalc.Astrophysics
                .blackHoleGravitationalField(finalBlackHoleMass, eventHorizonRadius);
            // then
            assertEquals(1_952_806_317_948.0, gravitationalField, 1e12);
        }

        @Test
        void testGravitationalPotentialEnergy() {
            // given
            final double blackHoleMass = MassUnit.sunsToKg(5);
            final double fallingObjectMass = MassUnit.sunsToKg(3);
            final short distanceToBlackHoleMeters = 8000;
            // when
            final double potentialEnergyJoules = PhysicsCalc.Astrophysics.gravitationalPotentialEnergy(blackHoleMass,
                fallingObjectMass, distanceToBlackHoleMeters);
            // then
            assertEquals(-4.95065025819e+47, potentialEnergyJoules, 1e47);
        }

        @Test
        void testBlackHoleTemperature() {
            // given
            final double mass = MassUnit.sunsToKg(5);
            // when
            final double temperature = PhysicsCalc.Astrophysics.blackHoleTemperature(mass);
            // then
            assertEquals(1.2344e-8, temperature, 1e-8);
        }
    }
}
