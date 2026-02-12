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
import static org.junit.jupiter.api.Assertions.assertFalse;
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

        static List<Arguments> resultantVelocityArgs() {
            return List.of(
                Arguments.of(new double[]{15, 7}, new double[]{0, AngleUnit.degToRadians(90)},
                    16.55, DELTA2)
            );
        }

        @ParameterizedTest
        @MethodSource("resultantVelocityArgs")
        void testResultantVelocity(double[] velocities, double[] angles, double expectedResult, double delta) {
            // when
            final double resultantVelocity = PhysicsCalc.Kinematics.resultantVelocity(velocities, angles);
            // then
            assertEquals(expectedResult, resultantVelocity, delta);
        }

        @Test
        void testResultantVelocityAngle() {
            // given
            final double vxResultant = 15;
            final double vyResultant = 7;
            // when
            final double angle = PhysicsCalc.Kinematics.resultantVelocityAngle(vxResultant, vyResultant);
            // then
            assertEquals(AngleUnit.degToRadians(25), angle, DELTA2);
        }

        @Test
        void testWeightOfFreeFallingBody() {
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

        @Test
        void testAccelerationMagnitudeComponents() {
            // given
            final double[] accelerationVector = new double[]{-3, 4, 2};
            // when
            final double magnitudeOfAcceleration = PhysicsCalc.Kinematics.accelerationMagnitude(accelerationVector);
            // then
            assertEquals(5.385, magnitudeOfAcceleration, DELTA3);
        }

        @Test
        void testAccelerationMagnitudeGivenVelocityVectorsDifference() {
            // given
            final double[] initialVelocityVector = new double[]{-3, 4};
            final double[] finalVelocityVector = new double[]{3, 2};
            final byte timeDifference = 5;
            // when
            final double magnitudeOfAcceleration = PhysicsCalc.Kinematics
                .accelerationMagnitude(initialVelocityVector, finalVelocityVector, timeDifference);
            // then
            assertEquals(1.265, magnitudeOfAcceleration, DELTA3);
        }

        @Test
        @Disabled
        void testGroundSpeed() {
            // given
            final double trueAirspeed = 10.5;
            final byte windSpeed = 12;
            final double courseRad = 0.52;
            final double windDirectionRad = 0.79;
            // when
            final double groundSpeed = PhysicsCalc.Kinematics
                .groundSpeed(trueAirspeed, windSpeed, courseRad, windDirectionRad);
            // then
            assertEquals(1.56, groundSpeed, DELTA2);
        }

        @Test
        void testAngularFrequency() {
            // given
            final byte angularDisplacement = 15;
            final byte timeTakenSeconds = 5;
            // when
            final double frequency = PhysicsCalc.Kinematics.angularFrequency(angularDisplacement, timeTakenSeconds);
            // then
            assertEquals(3, frequency, DELTA1);
        }

        @Test
        void testAngularDisplacementFromAngularFrequency() {
            // given
            final byte angularFrequency = 3;
            final byte timeTakenSeconds = 5;
            // when
            final double displacement = PhysicsCalc.Kinematics
                .angularDisplacementFromAngularFrequency(angularFrequency, timeTakenSeconds);
            // then
            assertEquals(15, displacement, DELTA1);
        }

        @Test
        void testTimeTakenFromAngularFrequency() {
            // given
            final byte angularFrequency = 3;
            final byte angularDisplacement = 15;
            // when
            final double timeTaken = PhysicsCalc.Kinematics
                .timeTakenFromAngularFrequency(angularFrequency, angularDisplacement);
            // then
            assertEquals(5, timeTaken, DELTA1);
        }

        @Test
        void testAngularFrequencyOfOscillatingObject() {
            // given
            final byte timePeriodSeconds = 5;
            // when
            final double angularFrequency = PhysicsCalc.Kinematics
                .angularFrequencyOfOscillatingObject(timePeriodSeconds);
            // then
            assertEquals(1.25664, angularFrequency, DELTA5);
        }

        @Test
        void testFrequencyFromAngularFrequencyOfOscillatingObject() {
            // given
            final double angularFrequency = 1.25664;
            // when
            final double frequency = PhysicsCalc.Kinematics
                .frequencyFromAngularFrequencyOfOscillatingObject(angularFrequency);
            // then
            assertEquals(0.2, frequency, DELTA1);
        }

        @Test
        void testTorque() {
            // given
            final double distance = 0.5;
            final byte force = 120;
            final double angle = 1.570796;
            // when
            final double torque = PhysicsCalc.Kinematics.torque(distance, force, angle);
            // then
            assertEquals(60, torque, DELTA1);
        }

        @Test
        void testAngularVelocityForConstantAngularAcceleration() {
            // given
            final double initialAngularVelocity = 27.5;
            final byte angularAcceleration = -10;
            final byte time = 2;
            // when
            final double angularVelocity = PhysicsCalc.Kinematics
                .angularVelocityForConstantAngularAcceleration(initialAngularVelocity, angularAcceleration, time);
            // then
            assertEquals(7.5, angularVelocity, DELTA1);
        }

        @Test
        void testAngularAcceleration() {
            // given
            final byte initialAngularVelocity = 0;
            final byte finalAngularVelocity = 24;
            final byte time = 8;
            // when
            final double angularAcceleration = PhysicsCalc.Kinematics
                .angularAcceleration(initialAngularVelocity, finalAngularVelocity, time);
            // then
            assertEquals(3, angularAcceleration, DELTA1);
        }

        @Test
        void testAngularAccelerationFromTangentialAcceleration() {
            // given
            final double radius = 2.5;
            final double tangentialAcceleration = 7.5;
            // when
            final double angularAcceleration = PhysicsCalc.Kinematics
                .angularAcceleration(tangentialAcceleration, radius);
            // then
            assertEquals(3, angularAcceleration, DELTA1);
        }

        @Test
        void testCentrifugalForce() {
            // given
            final short massKg = 1000;
            final short radius = 150;
            final byte tangentialVelocity = 50;
            // when
            final double force = PhysicsCalc.Kinematics.centrifugalForce(massKg, radius, tangentialVelocity);
            // then
            assertEquals(16_666.6, force, DELTA1);
        }

        @Test
        void testCentrifugalAcceleration() {
            // given
            final double centrifugalForce = 16_666.6;
            final short massKg = 1000;
            // when
            final double acceleration = PhysicsCalc.Kinematics.centrifugalAcceleration(massKg, centrifugalForce);
            // then
            assertEquals(16.667, acceleration, DELTA3);
        }

        @Test
        void testCentripetalForce() {
            // given
            final short massKg = 2000;
            final byte radius = 10;
            final double tangentialVelocity = 12.5;
            // when
            final double centripetalForce = PhysicsCalc.Kinematics.centripetalForce(massKg, radius, tangentialVelocity);
            // then
            assertEquals(31_250, centripetalForce, DELTA1);
        }

        @Test
        void testCentripetalAcceleration() {
            // given
            final byte radius = 10;
            final double tangentialVelocity = 12.5;
            // when
            final double acceleration = PhysicsCalc.Kinematics.centripetalAcceleration(radius, tangentialVelocity);
            // then
            assertEquals(15.625, acceleration, DELTA3);
        }

        @Test
        void testAngularDisplacement() {
            // given
            final short distanceTraveled = 185;
            final byte radius = 9;
            // when
            final double angularDisplacement = PhysicsCalc.Kinematics.angularDisplacement(distanceTraveled, radius);
            // then
            assertEquals(20.556, angularDisplacement, DELTA3);
        }

        @Test
        void testAngularDisplacementFromAngularVelocity() {
            // given
            final byte angularVelocity = 24;
            final byte time = 8;
            // when
            final double angularDisplacement = PhysicsCalc.Kinematics
                .angularDisplacementFromAngularVelocity(angularVelocity, time);
            // then
            assertEquals(192, angularDisplacement, DELTA1);
        }

        @Test
        void testAngularDisplacementFromAngularAcceleration() {
            // given
            final byte angularVelocity = 24;
            final byte time = 8;
            final byte angularAcceleration = 3;
            // when
            final double angularDisplacement = PhysicsCalc.Kinematics
                .angularDisplacementFromAngularAcceleration(angularVelocity, time, angularAcceleration);
            // then
            assertEquals(288, angularDisplacement, DELTA1);
        }

        @Test
        void testAngularMomentumAroundOwnAxis() {
            // given
            final byte momentOfInertia = 2;
            final byte angularVelocity = 1;
            // when
            final double angularMomentum = PhysicsCalc.Kinematics
                .angularMomentumAroundOwnAxis(momentOfInertia, angularVelocity);
            // then
            assertEquals(2, angularMomentum, DELTA1);
        }

        @Test
        void testAngularMomentumAroundCentralPoint() {
            // given
            final byte massKg = 3;
            final byte velocity = 2;
            final double radius = 0.1;
            // when
            final double angularMomentum = PhysicsCalc.Kinematics
                .angularMomentumAroundCentralPoint(massKg, velocity, radius);
            // then
            assertEquals(0.6, angularMomentum, DELTA1);
        }

        @Test
        void testPolarMomentOfInertiaOfSolidCircle() {
            // given
            final double diameter = 0.05;
            // when
            final double momentOfInertia = PhysicsCalc.Kinematics.polarMomentOfInertiaOfSolidCircle(diameter);
            // then
            assertEquals(6.136e-7, momentOfInertia, DELTA1);
        }

        @Test
        void testPolarMomentOfInertiaOfHollowCircle() {
            // given
            final double innerDiameter = 0.02;
            final double outerDiameter = 0.05;
            // when
            final double momentOfInertia = PhysicsCalc.Kinematics
                .polarMomentOfInertiaOfHollowCircle(innerDiameter, outerDiameter);
            // then
            assertEquals(5.979e-7, momentOfInertia, DELTA1);
        }

        @Test
        void testMassMomentOfInertiaOfBall() {
            // given
            final double massKg = 1.5;
            final double radius = 0.3;
            // when
            final double momentOfInertia = PhysicsCalc.Kinematics.massMomentOfInertiaOfBall(massKg, radius);
            // then
            assertEquals(0.054, momentOfInertia, DELTA3);
        }

        @Test
        void testMassMomentOfInertiaOfCircularHoop() {
            // given
            final double massKg = 1.5;
            final double radius = 0.3;
            // when
            final double[] moments = PhysicsCalc.Kinematics.massMomentOfInertiaOfCircularHoop(massKg, radius);
            // then
            assertArrayEquals(new double[]{0.0675, 0.135}, moments, DELTA4);
        }

        @Test
        void testMassMomentOfInertiaOfCuboid() {
            // given
            final byte massKg = 5;
            final byte length = 1;
            final byte width = 2;
            final byte height = 2;
            // when
            final double[] moments = PhysicsCalc.Kinematics.massMomentOfInertiaOfCuboid(massKg, length, width, height);
            // then
            assertArrayEquals(new double[]{3.3333, 2.0833, 2.0833, 2.2222}, moments, DELTA4);
        }

        @Test
        void testMassMomentOfInertiaOfCylinder() {
            // given
            final double massKg = 1.5;
            final double radius = 0.3;
            final double height = 0.9;
            // when
            final double[] moments = PhysicsCalc.Kinematics.massMomentOfInertiaOfCylinder(massKg, radius, height);
            // then
            assertArrayEquals(new double[]{0.135, 0.0675}, moments, DELTA4);
        }

        @Test
        void testMassMomentOfInertiaOfCylindricalTube() {
            // given
            final double massKg = 1.5;
            final double innerRadius = 0.01;
            final double outerRadius = 0.3;
            final double height = 0.9;
            // when
            final double[] moments = PhysicsCalc.Kinematics
                .massMomentOfInertiaOfCylinderTube(massKg, innerRadius, outerRadius, height);
            // then
            assertArrayEquals(new double[]{0.13504, 0.06758}, moments, DELTA5);
        }

        @Test
        void testMassMomentOfInertiaOfCylindricalShell() {
            // given
            final double massKg = 1.5;
            final double radius = 0.3;
            // when
            final double momentOfInertia = PhysicsCalc.Kinematics.massMomentOfInertiaOfCylinderShell(massKg, radius);
            // then
            assertEquals(0.135, momentOfInertia, DELTA3);
        }

        @Test
        void testMassMomentOfInertiaOfDisc() {
            // given
            final double massKg = 1.5;
            final double radius = 0.3;
            // when
            final double[] moments = PhysicsCalc.Kinematics.massMomentOfInertiaOfDisc(massKg, radius);
            // then
            assertArrayEquals(new double[]{0.03375, 0.0675}, moments, DELTA5);
        }

        @Test
        void testMassMomentOfInertiaOfDodecahedron() {
            // given
            final double massKg = 1.5;
            final double side = 0.3;
            // when
            final double[] moments = PhysicsCalc.Kinematics.massMomentOfInertiaOfDodecahedron(massKg, side);
            // then
            assertArrayEquals(new double[]{0.082, 0.13665}, moments, DELTA5);
        }

        @Test
        void testMassMomentOfEllipsoid() {
            // given
            final double massKg = 1.5;
            final double semiaxisA = 0.3;
            final double semiaxisB = 0.6;
            final double semiaxisC = 0.9;
            // when
            final double[] moments = PhysicsCalc.Kinematics
                .massMomentOfInertiaOfEllipsoid(massKg, semiaxisA, semiaxisB, semiaxisC);
            // then
            assertArrayEquals(new double[]{0.351, 0.27, 0.135}, moments, DELTA3);
        }

        @Test
        void testMassMomentOfInertiaOfIcosahedron() {
            // given
            final double massKg = 1.5;
            final double side = 0.3;
            // when
            final double[] moments = PhysicsCalc.Kinematics.massMomentOfInertiaOfIcosahedron(massKg, side);
            // then
            assertArrayEquals(new double[]{0.03534, 0.0589}, moments, DELTA5);
        }

        @Test
        void testMassMomentOfInertiaOfIsoscelesTriangle() {
            // given
            final double commonSideLength = 0.3;
            final double massKg = 1.5;
            final double angle = 0.5236;
            // when
            final double moment = PhysicsCalc.Kinematics
                .massMomentOfInertiaOfIsoscelesTriangle(commonSideLength, massKg, angle);
            // then
            assertEquals(0.05625, moment, DELTA5);
        }

        @Test
        void testMassMomentOfInertiaOfOctahedron() {
            // given
            final double massKg = 1.5;
            final double side = 0.3;
            // when
            final double[] moments = PhysicsCalc.Kinematics.massMomentOfInertiaOfOctahedron(massKg, side);
            // then
            assertArrayEquals(new double[]{0.0135, 0.0225}, moments, DELTA4);
        }

        @Test
        void testMassMomentOfPointMass() {
            // given
            final double massKg = 1.5;
            final double distance = 0.3;
            // when
            final double moment = PhysicsCalc.Kinematics.massMomentOfPointMass(massKg, distance);
            // then
            assertEquals(0.135, moment, DELTA3);
        }

        @Test
        void testMassMomentOfRectangularPlate() {
            // given
            final double massKg = 1.5;
            final double width = 0.5;
            final double length = 1;
            // when
            final double moment = PhysicsCalc.Kinematics.massMomentOfRectangularPlate(massKg, width, length);
            // then
            assertEquals(0.15625, moment, DELTA5);
        }

        @Test
        void testMassMomentOfRectangularPolygon() {
            // given
            final double massKg = 1.5;
            final double radius = 0.3;
            final byte numberOfVertices = 4;
            // when
            final double moment = PhysicsCalc.Kinematics.massMomentOfRegularPolygon(massKg, radius, numberOfVertices);
            // then
            assertEquals(0.045, moment, DELTA3);
        }

        @Test
        void testMassMomentOfHollowRightCircularCone() {
            // given
            final double massKg = 1.5;
            final double radius = 0.3;
            final double height = 1.7;
            // when
            final double[] moments = PhysicsCalc.Kinematics.massMomentOfHollowRightCircularCone(massKg, radius, height);
            // then
            assertArrayEquals(new double[]{2.2013, 0.0675}, moments, DELTA4);
        }

        @Test
        void testMassMomentOfSolidRightCircularCone() {
            // given
            final double massKg = 1.5;
            final double radius = 0.3;
            final double height = 1.7;
            // when
            final double[] moments = PhysicsCalc.Kinematics.massMomentOfSolidRightCircularCone(massKg, radius, height);
            // then
            assertArrayEquals(new double[]{2.6212, 0.0405}, moments, DELTA4);
        }

        @Test
        void testMassMomentOfRod() {
            // given
            final double massKg = 1.5;
            final double length = 0.3;
            // when
            final double[] moments = PhysicsCalc.Kinematics.massMomentOfRod(massKg, length);
            // then
            assertArrayEquals(new double[]{0.01125, 0.045}, moments, DELTA5);
        }

        @Test
        void testMassMomentOfInertiaOfSphere() {
            // given
            final byte massKg = 9;
            final double radius = 0.7;
            // when
            final double momentOfInertia = PhysicsCalc.Kinematics.massMomentOfInertiaOfSphere(massKg, radius);
            // then
            assertEquals(2.94, momentOfInertia, DELTA2);
        }

        @Test
        void testMassMomentOfSphericalShell() {
            // given
            final double massKg = 1.5;
            final double innerRadius = 0.03;
            final double outerRadius = 0.3;
            // when
            final double moment = PhysicsCalc.Kinematics.massMomentOfSphericalShell(massKg, innerRadius, outerRadius);
            // then
            assertEquals(0.05405, moment, DELTA5);
        }

        @Test
        void testMassMomentOfTetrahedron() {
            // given
            final double massKg = 1.5;
            final double side = 0.3;
            // when
            final double[] moments = PhysicsCalc.Kinematics.massMomentOfTetrahedron(massKg, side);
            // then
            assertArrayEquals(new double[]{0.00675, 0.01125}, moments, DELTA5);
        }

        @Test
        void testMassMomentOfTorus() {
            // given
            final double massKg = 1.5;
            final double minorRadius = 0.03;
            final double majorRadius = 0.3;
            // when
            final double[] moments = PhysicsCalc.Kinematics.massMomentOfTorus(massKg, minorRadius, majorRadius);
            // then
            assertArrayEquals(new double[]{0.13601, 0.06834}, moments, DELTA5);
        }

        @Test
        void testMassMomentOfTwoPointMasses() {
            // given
            final double massKg1 = 1.5;
            final double massKg2 = 0.5;
            final double distance = 0.8;
            // when
            final double moment = PhysicsCalc.Kinematics.massMomentOfTwoPointMasses(massKg1, massKg2, distance);
            // then
            assertEquals(0.24, moment, DELTA2);
        }

        static List<Arguments> projectileMotionMaximumHeightArgs() {
            return List.of(
                Arguments.of(1, 9.144, 0, 1, DELTA1),
                Arguments.of(0, 9.144, AngleUnit.degToRadians(45), 2.13, DELTA2),
                Arguments.of(0, 9.144, AngleUnit.degToRadians(70), 3.764, DELTA3),
                Arguments.of(0, 14.7, AngleUnit.degToRadians(90), 11.02, DELTA2)
            );
        }

        @ParameterizedTest
        @MethodSource("projectileMotionMaximumHeightArgs")
        void testProjectileMotionMaximumHeight(double initialHeight, double initialVelocity, double angleOfLaunch,
                                               double expectedResult, double delta) {
            // when
            final double maxHeight = PhysicsCalc.Kinematics
                .projectileMotionMaximumHeight(initialHeight, initialVelocity, angleOfLaunch);
            // then
            assertEquals(expectedResult, maxHeight, delta);
        }

        static List<Arguments> projectileMotionTimeOfFlightArgs() {
            return List.of(
                // The deepest point of the Grand Canyon
                Arguments.of(1828.8, 4.8768, AngleUnit.degToRadians(20), 19.5, DELTA1),
                Arguments.of(0, 4.8768, AngleUnit.degToRadians(20), 0.34, DELTA2)
            );
        }

        @ParameterizedTest
        @MethodSource("projectileMotionTimeOfFlightArgs")
        void testProjectileMotionTimeOfFlight(double initialHeight, double initialVelocity, double angleOfLaunch,
                                              double expectedResult, double delta) {
            // when
            final double maxHeight = PhysicsCalc.Kinematics
                .projectileMotionTimeOfFlight(initialHeight, initialVelocity, angleOfLaunch);
            // then
            assertEquals(expectedResult, maxHeight, delta);
        }

        @Test
        void testProjectileMotionLaunchingFromGroundTimeOfFlight() {
            // given
            final double initialVelocity = 15;
            // when
            final double timeOfFlight = PhysicsCalc.Kinematics
                .projectileMotionLaunchingFromGroundTimeOfFlight(initialVelocity);
            // then
            assertEquals(3.06, timeOfFlight, DELTA2);
        }

        @Test
        void testProjectileMotionLaunchingFromElevationTimeOfFlight() {
            // given
            final byte initialHeight = 5;
            final double initialVelocity = 14.7;
            // when
            final double timeOfFlight = PhysicsCalc.Kinematics
                .projectileMotionLaunchingFromElevationTimeOfFlight(initialHeight, initialVelocity);
            // then
            assertEquals(3.306, timeOfFlight, DELTA3);
        }

        @Test
        void testProjectileMotionHorizontalVelocityComponent() {
            // given
            // Initial vertical velocity (Vᵧ) = 5
            final double initialVelocity = 5.7735;
            final double angleOfLaunch = 1.047198;
            // when
            final double horizontalVelocity = PhysicsCalc.Kinematics
                .projectileMotionHorizontalVelocityComponent(initialVelocity, angleOfLaunch);
            // then
            assertEquals(2.887, horizontalVelocity, DELTA1);
        }

        @Test
        void testProjectileMotionVerticalVelocityComponent() {
            // given
            // Initial horizontal velocity (Vₓ) = 2.887
            // Initial vertical velocity (Vᵧ) = 5
            final double initialVelocity = 5.7735;
            final double angleOfLaunch = 1.047198;
            final byte timeOfFlight = 0;
            // when
            final double verticalVelocity = PhysicsCalc.Kinematics
                .projectileMotionVerticalVelocityComponent(initialVelocity, angleOfLaunch, timeOfFlight);
            // then
            assertEquals(5, verticalVelocity, DELTA1);
        }

        @Test
        void testProjectileMotionHorizontalDistance() {
            // given
            final short eiffelTowerHeight = 324;
            final byte initialVelocity = 7;
            // when
            final double distance = PhysicsCalc.Kinematics
                .projectileMotionHorizontalDistance(eiffelTowerHeight, initialVelocity);
            // then
            assertEquals(56.9017, distance, DELTA4);
        }

        static List<Arguments> projectileMotionRangeArgs() {
            return List.of(
                Arguments.of(30, 0.436332, 100, 162.87, DELTA2),
                Arguments.of(22, 0.610865, 0, 46.38, DELTA2)
            );
        }

        @ParameterizedTest
        @MethodSource("projectileMotionRangeArgs")
        void testProjectileMotionRange(double initialVelocity, double angleOfLaunch, double initialHeight,
                                       double expectedResult, double delta) {
            // when
            final double range = PhysicsCalc.Kinematics
                .projectileMotionRange(initialVelocity, angleOfLaunch, initialHeight);
            // then
            assertEquals(expectedResult, range, delta);
        }

        @Test
        void testProjectileMotionTrajectory() {
            // given
            final double initialVelocity = 1.524;
            final double angleOfLaunch = AngleUnit.degToRadians(60);
            final double initialHeight = 0.127;
            // when
            final double distance = PhysicsCalc.Kinematics
                .projectileMotionTrajectory(initialHeight, initialVelocity, angleOfLaunch);
            // then
            assertEquals(3.22e-15, distance, DELTA1);
        }

        @Test
        void testProjectileMotionTrajectoryWithoutAngle() {
            // given
            final double initialVelocity = 1.524;
            final double initialHeight = 0.127;
            // when
            final double distance = PhysicsCalc.Kinematics
                .projectileMotionTrajectory(initialHeight, initialVelocity);
            // then
            assertEquals(3.22e-15, distance, DELTA1);
        }

        @Test
        void testInclinedPlaneCubicBlockSlidingTime() {
            // given
            final byte mass = 2;
            final double initialVelocity = 0;
            final double angle = AngleUnit.degToRadians(40);
            final double frictionCoeff = 0.2;
            final double height = 5;
            // when
            final double time = PhysicsCalc.Kinematics
                .inclinedPlaneCubicBlockSlidingTime(mass, initialVelocity, angle, frictionCoeff, height);
            // then
            assertEquals(1.8, time, DELTA1);
        }

        @Test
        void testInclinedPlaneCubicBlockEnergyLoss() {
            // given
            final double mass = 2;
            final double acceleration = 4.801;
            final double time = 1.8;
            final double finalVelocity = PhysicsCalc.Kinematics.finalVelocity(0, acceleration, time);
            final double height = 5;
            // when
            final double loss = PhysicsCalc.Kinematics.inclinedPlaneCubicBlockEnergyLoss(mass, finalVelocity, height);
            // then
            assertEquals(23.38, loss, DELTA2);
        }

        @Test
        void testInclinedPlaneRollingObjSlidingTime() {
            // given
            final double initialVelocity = 2;
            final double angle = AngleUnit.degToRadians(30);
            final double height = 5;
            final double constantFactor = 2. / 5;
            // when
            final double time = PhysicsCalc.Kinematics
                .inclinedPlaneRollingObjSlidingTime(initialVelocity, angle, height, constantFactor);
            // then
            assertEquals(1.886, time, DELTA3);
        }

        @Test
        void testCentrifugeSpeedRCFtoRPM() {
            // given
            final byte rotorRadius = 5;
            final short rcf = 10_956;
            // when
            final double rpm = PhysicsCalc.Kinematics.centrifugeSpeedRCFtoRPM(rotorRadius, rcf);
            // then
            assertEquals(13_999.7, rpm, DELTA1);
        }

        @Test
        void testCentrifugeSpeedRPMtoRCF() {
            // given
            final byte rotorRadius = 5;
            final short rpm = 14_000;
            // when
            final double rcf = PhysicsCalc.Kinematics.centrifugeSpeedRPMtoRCF(rotorRadius, rpm);
            // then
            assertEquals(10_956.4, rcf, DELTA1);
        }

        @Test
        void testSledSlidingDown() {
            // given
            final byte hillLength = 10;
            final double frictionCoeff = 0.474;
            // when
            final double[] results = PhysicsCalc.Kinematics.sledSlidingDown(hillLength, frictionCoeff);
            // then
            assertArrayEquals(new double[]{4.649, 9.642, 2.0742}, results, DELTA3);
        }

        @Test
        void testSimpleHarmonicMotion() {
            // given
            final double amplitude = 0.015;
            final double time = 1.4;
            final byte frequency = 1;
            // when
            final double[] results = PhysicsCalc.Kinematics.simpleHarmonicMotion(amplitude, time, frequency);
            // then
            assertArrayEquals(new double[]{0.008817, -0.076248, -0.348072}, results, DELTA6);
        }

        @Test
        void testCoriolisEffect() {
            // given
            final double velocity = 138.9;
            final double angularVelocity = SpeedUnit.EARTH_ANGULAR_VELOCITY;
            final double latitude = 0.9146; // Northern hemisphere
            // when
            final double acceleration = PhysicsCalc.Kinematics.coriolisEffect(velocity, angularVelocity, latitude);
            // then
            assertEquals(0.016, acceleration, DELTA3);
        }

        @Test
        void testCoriolisForce() {
            // given
            final int mass = 50_000;
            final double velocity = 138.9;
            final double angularVelocity = SpeedUnit.EARTH_ANGULAR_VELOCITY;
            final double latitude = 0.9146; // Northern hemisphere
            // when
            final double force = PhysicsCalc.Kinematics.coriolisForce(mass, velocity, angularVelocity, latitude);
            // then
            assertEquals(800, force, DELTA1);
        }

        @Test
        void testSimplePendulum() {
            // given
            final byte length = 2;
            // when
            final double frequency = PhysicsCalc.Kinematics.simplePendulum(length);
            // then
            assertEquals(0.3524, frequency, DELTA4);
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
        void testShearStrainUsingShearStressAndModulus() {
            // given
            final int shearStress = 8_000_000;
            final long brassShearModulus = 35_000_000_000L;
            // when
            final double shearStrain = PhysicsCalc.Mechanics
                .shearStrainUsingShearStressAndModulus(shearStress, brassShearModulus);
            // then
            assertEquals(0.00022857, shearStrain, DELTA8);
        }

        @Test
        void testShearStrainForShaftUnderTorsion() {
            // given
            final double distanceFromShaftAxis = 0.2;
            final double angleOfTwist = 0.2618;
            final double shaftLength = 0.8;
            // when
            final double shearStrain = PhysicsCalc.Mechanics
                .shearStrainForShaftUnderTorsion(distanceFromShaftAxis, angleOfTwist, shaftLength);
            // then
            assertEquals(0.06545, shearStrain, DELTA5);
        }

        @Test
        void testMaxShearStrainForShaftUnderTorsion() {
            // given
            final double shaftRadius = 0.3;
            final double angleOfTwist = 0.2618;
            final double shaftLength = 0.8;
            // when
            final double shearStrain = PhysicsCalc.Mechanics
                .maxShearStrainForShaftUnderTorsion(angleOfTwist, shaftRadius, shaftLength);
            // then
            assertEquals(0.09817, shearStrain, DELTA5);
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

        @Test
        void testAngleOfTwist() {
            // given
            final int internalTorque = 279_973;
            final double shaftLength = 0.25;
            final long shearModulus = 68_000_000_000L;
            // when
            final double angle = PhysicsCalc.Mechanics
                .angleOfTwist(internalTorque, shaftLength, shearModulus);
            // then
            assertEquals(0.002684, angle, DELTA6);
        }

        @Test
        void testAngleOfRepose() {
            // given
            final double heapHeight = 0.35;
            final double heapRadius = 0.96;
            // when
            final double angle = PhysicsCalc.Mechanics.angleOfRepose(heapHeight, heapRadius);
            // then
            assertEquals(0.3496, angle, DELTA4);
        }

        @Test
        void testBulkModulusChangeInVolume() {
            // given
            final double pressure = 21e6;
            final double initialVolume = 0.001155;
            final double bulkModulus = 5e9;
            // when
            final double changeInVolume = PhysicsCalc.Mechanics
                .bulkModulusChangeInVolume(pressure, initialVolume, bulkModulus);
            // then
            assertEquals(-0.000004851, changeInVolume, DELTA9);
        }

        @Test
        void testBulkStrain() {
            // given
            final double initialVolume = 0.001155;
            final double changeInVolume = -0.000004851;
            // when
            final double bulkStrain = PhysicsCalc.Mechanics.bulkStrain(initialVolume, changeInVolume);
            // then
            assertEquals(-0.0042, bulkStrain, DELTA4);
        }

        @Test
        void testShaftSizeForTwistingMomentOnly() {
            // given
            final double powerTransmitted = PowerUnit.kilowattsToWatts(20);
            final double shaftRotationSpeed = 200;
            // when
            final double torque = PhysicsCalc.Mechanics
                .shaftSizeForTwistingMomentOnly(powerTransmitted, shaftRotationSpeed);
            // then
            assertEquals(955, torque, DELTA1);
        }

        @Test
        void testDiameterOfSolidShaftForTwistingMomentOnly() {
            // given
            final double torque = 955;
            final int allowableShearStress = 42_000_000;
            // when
            final double diameter = PhysicsCalc.Mechanics
                .diameterOfSolidShaftForTwistingMomentOnly(torque, allowableShearStress);
            // then
            assertEquals(0.04874, diameter, DELTA5);
        }

        @Test
        void testShaftSizeDiametersForTwistingOrBendingMoment() {
            // given
            final double diameterOfSolidShaft = 0.06141;
            final double diameterRatio = 0.5;
            // when
            final double[] diameters = PhysicsCalc.Mechanics
                .shaftSizeDiametersForTwistingOrBendingMoment(diameterOfSolidShaft, diameterRatio);
            // then
            assertArrayEquals(new double[]{0.062745, 0.031372}, diameters, DELTA6);
        }

        @Test
        void testShaftSizeForBendingMomentOnly() {
            // given
            final double bendingMoment = 955;
            final int allowableBendingStress = 42_000_000;
            // when
            final double diameter = PhysicsCalc.Mechanics
                .shaftSizeForBendingMomentOnly(bendingMoment, allowableBendingStress);
            // then
            assertEquals(0.06141, diameter, DELTA5);
        }

        @Test
        void testEquivalentTwistingMoment() {
            // given
            final double torque = 955;
            final double bendingMoment = 955;
            // when
            final double equivalentTwistingMoment = PhysicsCalc.Mechanics
                .equivalentTwistingMoment(bendingMoment, torque);
            // then
            assertEquals(1350.5, equivalentTwistingMoment, DELTA1);
        }

        @Test
        void testEquivalentBendingMoment() {
            // given
            final double torque = 955;
            final double bendingMoment = 955;
            // when
            final double equivalentBendingMoment = PhysicsCalc.Mechanics.equivalentBendingMoment(bendingMoment, torque);
            // then
            assertEquals(1152.8, equivalentBendingMoment, DELTA1);
        }

        @Test
        void testFluctuatingEquivalentTwistingMoment() {
            // given
            final double torque = 955;
            final double bendingMoment = 955;
            final double bendingFactor = 1.0001;
            final double torsionFactor = 745.6;
            // when
            final double equivalentTwistingMoment = PhysicsCalc.Mechanics
                .fluctuatingEquivalentTwistingMoment(torque, bendingMoment, bendingFactor, torsionFactor);
            // then
            assertEquals(712_048.6, equivalentTwistingMoment, DELTA1);
        }

        @Test
        void testFluctuatingEquivalentBendingMoment() {
            // given
            final double torque = 955;
            final double bendingMoment = 955;
            final double bendingFactor = 1.0001;
            final double torsionFactor = 745.6;
            // when
            final double equivalentBendingMoment = PhysicsCalc.Mechanics
                .fluctuatingEquivalentBendingMoment(torque, bendingMoment, bendingFactor, torsionFactor);
            // then
            assertEquals(356_501.8, equivalentBendingMoment, DELTA1);
        }

        @Test
        void testShaftSizeDiameterForTorsionalRigidity() {
            // given
            final double torque = 955;
            final short rigidityModulus = 2400;
            final double angle = 0.2618;
            final double length = 1.5;
            // when
            final double solidShaftDiameter = PhysicsCalc.Mechanics
                .shaftSizeDiameterForTorsionalRigidity(torque, rigidityModulus, angle, length);
            // then
            assertEquals(2.1952, solidShaftDiameter, DELTA4);
        }

        @Test
        void testStress() {
            // given
            final double area = 1e-4;
            final short force = 30_000;
            // when
            final double stress = PhysicsCalc.Mechanics.stress(area, force);
            // then
            assertEquals(300_000_000, stress, DELTA1);
        }

        @Test
        void testStrain() {
            // given
            final byte initialLength = 2;
            final double changeInLength = 0.003;
            // when
            final double stress = PhysicsCalc.Mechanics.strain(initialLength, changeInLength);
            // then
            assertEquals(0.0015, stress, DELTA4);
        }

        @Test
        void testYoungsModulus() {
            // given
            final int stress = 300_000_000;
            final double strain = 0.0015;
            // when
            final double modulus = PhysicsCalc.Mechanics.youngsModulus(stress, strain);
            // then
            assertEquals(200_000_000_000L, modulus, DELTA1);
        }

        @Test
        void testShearStressTransverseForArbitraryCrossSection() {
            // given
            final byte shearForceMagnitude = 120;
            final double width = 0.1;
            final byte firstMomentOfArea = 10;
            final double momentOfInertia = 15.708;
            // when
            final double shearStressMagnitude = PhysicsCalc.Mechanics.shearStressTransverseForArbitraryCrossSection(
                shearForceMagnitude, width, firstMomentOfArea, momentOfInertia);
            // then
            assertEquals(764, shearStressMagnitude, DELTA1);
        }

        @Test
        void testShearStressTransverseForRectangular() {
            // given
            final byte shearForceMagnitude = 120;
            final byte baseWidth = 4;
            final byte squareHeight = 2;
            final double distanceToNeutralAxis = 0.8;
            // when
            final double shearStressMagnitudeAtDistance = PhysicsCalc.Mechanics.shearStressTransverseForRectangular(
                shearForceMagnitude, baseWidth, squareHeight, distanceToNeutralAxis);
            // then
            assertEquals(8.1, shearStressMagnitudeAtDistance, DELTA1);
        }

        @Test
        void testShearStressTransverseForHollowCircular() {
            // given
            final byte shearForceMagnitude = 120;
            final double outerRadius = 0.3;
            final double innerRadius = 0.03;
            // when
            final double maxShearStressMagnitude = PhysicsCalc.Mechanics
                .shearStressTransverseForHollowCircular(shearForceMagnitude, outerRadius, innerRadius);
            // then
            assertEquals(628.2, maxShearStressMagnitude, DELTA1);
        }

        @Test
        void testShearStressTransverseForIBeam() {
            // given
            final byte shearForceMagnitude = 120;
            final byte baseWidth = 4;
            final double webThickness = 0.2;
            final double webLength = 0.6;
            // when
            final double maxShearStressMagnitude = PhysicsCalc.Mechanics
                .shearStressTransverseForIBeam(shearForceMagnitude, baseWidth, webThickness, webLength);
            // then
            assertEquals(745.1, maxShearStressMagnitude, DELTA1);
        }

        @Test
        void testMinShearStressTransverseForIBeam() {
            // given
            final byte shearForceMagnitude = 120;
            final byte baseWidth = 4;
            final double webThickness = 0.2;
            final double webLength = 0.6;
            // when
            final double maxShearStressMagnitude = PhysicsCalc.Mechanics
                .minShearStressTransverseForIBeam(shearForceMagnitude, baseWidth, webThickness, webLength);
            // then
            assertEquals(724.7, maxShearStressMagnitude, DELTA1);
        }

        @Test
        void testShearStressTorsionalForSolidCircle() {
            // given
            final double appliedTorque = 0.9;
            final double outerRadius = 0.3;
            // when
            final double maxShearStressMagnitude = PhysicsCalc.Mechanics
                .shearStressTorsionalForSolidCircle(appliedTorque, outerRadius);
            // then
            assertEquals(21.22, maxShearStressMagnitude, DELTA2);
        }

        @Test
        void testShearStressTorsionalForHollowCircle() {
            // given
            final double appliedTorque = 0.9;
            final double outerRadius = 0.3;
            final double innerRadius = 0.03;
            // when
            final double maxShearStressMagnitude = PhysicsCalc.Mechanics
                .shearStressTorsionalForHollowCircle(appliedTorque, outerRadius, innerRadius);
            // then
            assertEquals(21.223, maxShearStressMagnitude, DELTA3);
        }

        @Test
        void testThermalStress() {
            // given
            final double copperThermalExpansionCoeff = 0.000017;
            final long youngsModulus = 110_000_000_000L;
            final byte initialTemperature = 20;
            final byte finalTemperature = 50;
            // when
            final double thermalStress = PhysicsCalc.Mechanics
                .thermalStress(copperThermalExpansionCoeff, youngsModulus, initialTemperature, finalTemperature);
            // then
            assertEquals(56_100_000, thermalStress, DELTA1);
        }

        @Test
        void testElongation() {
            // given
            final double originalLength = 0.01;
            final double finalLength = 0.015;
            // when
            final double elongation = PhysicsCalc.Mechanics.elongation(originalLength, finalLength);
            // then
            assertEquals(50, elongation, DELTA1);
        }

        @Test
        void testTrueStrain() {
            // given
            final double engineeringStrain = 0.1;
            // when
            final double trueStrain = PhysicsCalc.Mechanics.trueStrain(engineeringStrain);
            // then
            assertEquals(0.09531, trueStrain, DELTA5);
        }

        @Test
        void testTrueStress() {
            // given
            final double engineeringStrain = 0.1;
            final int engineeringStress = 8_000_000;
            // when
            final double trueStress = PhysicsCalc.Mechanics.trueStress(engineeringStrain, engineeringStress);
            // then
            assertEquals(8_800_000, trueStress, DELTA1);
        }

        @Test
        void testBeltLength() {
            // given
            final double largePulleyDiameter = 0.3;
            final double smallPulleyDiameter = 0.15;
            final double pulleyCenterDistance = 1.5;
            // when
            final double length = PhysicsCalc.Mechanics
                .beltLength(largePulleyDiameter, smallPulleyDiameter, pulleyCenterDistance);
            // then
            assertEquals(3.7106, length, DELTA4);
        }

        @Test
        void testApproximateBeltLength() {
            // given
            final double largePulleyDiameter = 0.3;
            final double smallPulleyDiameter = 0.15;
            final double pulleyCenterDistance = 1.5;
            // when
            final double length = PhysicsCalc.Mechanics
                .approximateBeltLength(largePulleyDiameter, smallPulleyDiameter, pulleyCenterDistance);
            assertEquals(3.7152, length, DELTA4);
        }

        @Test
        void testPulley() {
            // given
            final short transmittingPower = 1500;
            final short pulleyCenterDistance = 1;
            final double driverPulleyDiameter = 0.4;
            final short driverPulleyAngularVelocity = 1000;
            final double drivenPulleyDiameter = 0.1;
            // when
            final double[] pulleyResults = PhysicsCalc.Mechanics.pulley(transmittingPower, pulleyCenterDistance,
                driverPulleyDiameter, driverPulleyAngularVelocity, drivenPulleyDiameter);
            // then
            assertArrayEquals(new double[]{14.324, 4000, 3.581, 2.808, 20.944, 71.62}, pulleyResults, DELTA3);
        }

        @Test
        void testTransmissionSpeed() {
            // given
            final short tireDiameter = 382;
            final short engineRPM = 5000;
            final byte transmissionGearRatio = 1;
            final byte differentialGearRatio = 3;
            // when
            final double speed = PhysicsCalc.Mechanics
                .transmissionSpeed(tireDiameter, engineRPM, transmissionGearRatio, differentialGearRatio);
            assertEquals(120, speed, DELTA1);
        }

        @Test
        void testEngineDisplacement() {
            // given
            final byte numOfCylinders = 4;
            final byte boreDiameter = 50;
            final short strokeLength = 250;
            // when
            final double volume = PhysicsCalc.Mechanics.engineDisplacement(numOfCylinders, boreDiameter, strokeLength);
            assertEquals(1_963_495.4, volume, DELTA1);
        }

        @Test
        void testEngineBoreDiameter() {
            // given
            final byte numOfCylinders = 2;
            final short strokeLength = 150;
            final double engineDisplacement = VolumeUnit.cm3ToMm3(200);
            // when
            final double boreDiameter = PhysicsCalc.Mechanics
                .engineBoreDiameter(numOfCylinders, strokeLength, engineDisplacement);
            assertEquals(29.135, boreDiameter, DELTA3);
        }

        static List<Arguments> densityArgs() {
            return List.of(
                Arguments.of(298, 78.6, 3.7913486, DELTA7), // g/cm³
                Arguments.of(MassUnit.EARTH_KG, VolumeUnit.EARTH_VOLUME, DensityUnit.EARTH_DENSITY, DELTA1)
            );
        }

        @ParameterizedTest
        @MethodSource("densityArgs")
        void testDensity(double mass, double volume, double expectedResult, double delta) {
            // when
            final double density = PhysicsCalc.Mechanics.density(mass, volume);
            // then
            assertEquals(expectedResult, density, delta);
        }

        @Test
        void testMassFromDensity() {
            // when
            final double mass = PhysicsCalc.Mechanics
                .massFromDensity(DensityUnit.EARTH_DENSITY, VolumeUnit.EARTH_VOLUME);
            // then
            assertEquals(MassUnit.EARTH_KG, mass, 1e21);
        }

        @Test
        void testVolumeFromDensity() {
            // when
            final double volume = PhysicsCalc.Mechanics
                .volumeFromDensity(MassUnit.EARTH_KG, DensityUnit.EARTH_DENSITY);
            // then
            assertEquals(VolumeUnit.EARTH_VOLUME, volume, 1e17);
        }

        @Test
        void testSpecificGravity() {
            // given
            final double densityOfMercury = PhysicsCalc.Mechanics.density(135.34, 10);
            final double refDensity = DensityUnit.WATER_DENSITY;
            // when
            final double specificGravity = PhysicsCalc.Mechanics.specificGravity(densityOfMercury, refDensity);
            assertEquals(13.535, specificGravity, DELTA3);
        }

        @Test
        void testNumberDensity() {
            // given copper
            final double density = 8960; // kg/m³
            final double molarMass = MassUnit.gramsToKg(63.55); // kg/mol
            final byte numOfFreeElectrons = 1;
            // when
            final double number = PhysicsCalc.Mechanics.numberDensity(density, molarMass, numOfFreeElectrons);
            assertEquals(8.491e28, number, 1e25);
        }
    }

    @Nested
    class QuantumMechanics {
        @Test
        void testPhotonEnergy() {
            // given
            final double wavelength = LengthUnit.nanometersToMeters(450);
            // when
            final double energy = PhysicsCalc.QuantumMechanics.photonEnergy(wavelength);
            // then
            assertEquals(4.414342e-19, energy, DELTA1);
        }

        @Test
        void testBohrModelPhotonFrequency() {
            // given
            final double initialEnergy = EnergyUnit.electronVoltsToJoules(-13.6);
            final double finalEnergy = EnergyUnit.electronVoltsToJoules(-3.4);
            // when
            final double frequency = PhysicsCalc.QuantumMechanics.bohrModelPhotonFrequency(initialEnergy, finalEnergy);
            // then f = 2466.3 THz ≈ 2.5 × 10¹⁵ Hz
            assertEquals(-2_466_349_082_759_464L, frequency, 1e15);
        }

        @Test
        void testDeBroglieWavelength() {
            // given
            final double restMass = 9.10938356e-31;
            final double velocity = 2_997_924.58;
            // when
            final double wavelength = PhysicsCalc.QuantumMechanics.deBroglieWavelength(restMass, velocity);
            // then
            assertEquals(2.42632e-10, wavelength, DELTA1);
        }

        @Test
        void testHydrogenLikeLevelEnergy() {
            // given
            final byte energyLevel = 1;
            final byte atomicNumber = 1;
            // when
            final double energy = PhysicsCalc.QuantumMechanics.hydrogenLikeLevelEnergy(energyLevel, atomicNumber);
            // then
            assertEquals(-13.6, energy, DELTA1);
        }

        @Test
        void testRadiatedPower() {
            // given
            final double sunArea = MathCalc.Geometry.sphereArea(LengthUnit.SUN_RADIUS);
            final short temperature = 5776;
            final byte perfectEmissivity = 1;
            // when
            final double power = PhysicsCalc.QuantumMechanics.radiatedPower(sunArea, temperature, perfectEmissivity);
            // then
            assertEquals(3.845e26, power, 1e23);
        }

        @Test
        void testRadiatedPowerGivenArea() {
            // given
            final double distanceFromEarthToSun = LengthUnit.kilometersToMeters(149_600_000);
            final double area = MathCalc.Geometry.sphereArea(distanceFromEarthToSun);
            // when
            final double power = PhysicsCalc.QuantumMechanics.radiatedPower(area);
            // then
            assertEquals(3.845e26, power, 1e23);
        }

        @Test
        void testRadiatedPowerTemperature() {
            // given
            final double sunArea = MathCalc.Geometry.sphereArea(LengthUnit.SUN_RADIUS);
            final double power = 3.845e26;
            final byte perfectEmissivity = 1;
            // when
            final double temperature = PhysicsCalc.QuantumMechanics
                .radiatedPowerTemperature(sunArea, power, perfectEmissivity);
            // then
            assertEquals(5775.7, temperature, DELTA1);
        }

        @Test
        void testComptonWavelength() {
            // given
            final double mass = MassUnit.meToKg(1);
            // when
            final double wavelength = PhysicsCalc.QuantumMechanics.comptonWavelength(mass);
            // then
            assertEquals(2.4263e-12, wavelength, DELTA1);
        }

        @Test
        void testComptonScattering() {
            // given
            final double mass = 9.109e-31;
            final double scatteringAngle = AngleUnit.degToRadians(80);
            // when
            final double wavelengthExtension = PhysicsCalc.QuantumMechanics.comptonScattering(mass, scatteringAngle);
            // then
            assertEquals(2.005e-12, wavelengthExtension, DELTA1);
        }

        @Test
        void testCurieConstant() {
            // given
            final byte numberOfAtoms = 4;
            final double latticeConstant = 0.2;
            final double magneticMoment = 9.274e-24;
            // when
            final double curieConstant = PhysicsCalc.QuantumMechanics
                .curieConstant(numberOfAtoms, latticeConstant, magneticMoment);
            // then
            assertEquals(1.1221e-46, curieConstant, DELTA1);
        }

        @Test
        void testAngularMomentumMagnitude() {
            // given
            final byte quantumNumber = 1;
            // when
            final double magnitude = PhysicsCalc.QuantumMechanics.angularMomentumMagnitude(quantumNumber);
            // then
            assertEquals(1.4913897670116408e-34, magnitude, DELTA1);
        }

        @Test
        void testSpinMagnitude() {
            // given
            final double quantumNumber = MathCalc.ONE_HALF;
            // when
            final double magnitude = PhysicsCalc.QuantumMechanics.spinMagnitude(quantumNumber);
            // then
            assertEquals(9.13285984196702e-35, magnitude, DELTA1);
        }

        @Test
        void testMagneticMoment() {
            // given
            final double spin = 0.5;
            final byte orbital = 1;
            // when
            final double magneticMoment = PhysicsCalc.QuantumMechanics.magneticMoment(spin, orbital);
            // then
            assertEquals(2.582, Math.abs(magneticMoment), DELTA3);
        }

        @Test
        void testWiensLawTemperature() {
            // given an A-type star
            final double peakWavelength = LengthUnit.nanometersToMeters(340);
            // when
            final double temperature = PhysicsCalc.QuantumMechanics.wiensLawTemperature(peakWavelength);
            // then
            assertEquals(8522.8, temperature, DELTA1);
        }

        @Test
        void testWiensLawPeakWavelength() {
            // given
            final double temperature = 8522.8;
            // when
            final double peakWavelength = PhysicsCalc.QuantumMechanics.wiensLawPeakWavelength(temperature);
            // then
            assertEquals(LengthUnit.nanometersToMeters(340), peakWavelength, DELTA1);
        }

        @Test
        void testWiensLawPeakFrequency() {
            // given
            final double temperature = 8522.8;
            // when
            final double peakFrequency = PhysicsCalc.QuantumMechanics.wiensLawPeakFrequency(temperature);
            // then
            assertEquals(501_048_866_489_600L, peakFrequency, DELTA1);
        }

        @Test
        void testPhotoelectricEffectEjectedElectron() {
            // given
            final double frequency = FrequencyUnit.thzToHz(501);
            final double thresholdFrequency = FrequencyUnit.thzToHz(490);
            // when
            final double maxKE = PhysicsCalc.QuantumMechanics
                .photoelectricEffectEjectedElectron(frequency, thresholdFrequency);
            // then
            assertEquals(7.321e-21, maxKE, DELTA1);
        }

        @Test
        void testPhotoelectricEffectWorkFunction() {
            // given
            final double thresholdFrequency = FrequencyUnit.thzToHz(490);
            // when
            final double energy = PhysicsCalc.QuantumMechanics.photoelectricEffectWorkFunction(thresholdFrequency);
            // then
            assertEquals(3.247e-19, energy, DELTA1);
        }

        @Test
        void testRydbergEquation() {
            // given
            final byte atomicNumber = 1;
            final byte initialState = 4;
            final byte finalState = 2;
            // when
            final double wavelength = PhysicsCalc.QuantumMechanics
                .rydbergEquation(atomicNumber, initialState, finalState);
            // then
            assertEquals(4.86e-7, wavelength, DELTA1);
        }

        @Test
        void testHeisenbergMomentumUncertainty() {
            // given
            final double momentum = PhysicsCalc.Kinematics.momentum(9.11e-31, 2e6);
            final double percentUncertainty = 0.5;
            // when
            final double momentumUncertainty = PhysicsCalc.QuantumMechanics
                .heisenbergMomentumUncertainty(momentum, percentUncertainty);
            // then
            assertEquals(9.1e-27, momentumUncertainty, DELTA1);
        }

        @Test
        void testHeisenbergPositionUncertainty() {
            // given
            final double momentumUncertainty = 9.1e-27;
            // when
            final double positionUncertainty = PhysicsCalc.QuantumMechanics
                .heisenbergPositionUncertainty(momentumUncertainty);
            // then
            assertEquals(LengthUnit.nanometersToMeters(5.8), positionUncertainty, DELTA1);
        }

        @Test
        void testHeisenbergVelocityUncertainty() {
            // given
            final double mass = 0.149;
            final double positionUncertainty = 5.8e-9;
            // when
            final double velocityUncertainty = PhysicsCalc.QuantumMechanics
                .heisenbergVelocityUncertainty(mass, positionUncertainty);
            // then
            assertEquals(6.101e-17, velocityUncertainty, DELTA1);
        }

        @Test
        void testFermiWaveNumber() {
            // given
            final double copperNumberDensityOfElectrons = 8.47e28;
            // when
            final double waveNumber = PhysicsCalc.QuantumMechanics.fermiWaveNumber(copperNumberDensityOfElectrons);
            // then
            assertEquals(13_586_308_450L, waveNumber, 1);
        }

        @Test
        void testFermiEnergy() {
            // given
            final double copperWaveNumber = 13_586_308_450L;
            // when
            final double energy = PhysicsCalc.QuantumMechanics.fermiEnergy(copperWaveNumber);
            // then
            assertEquals(EnergyUnit.electronVoltsToJoules(7.033), energy, DELTA1);
        }

        @Test
        void testFermiTemperature() {
            // given
            final double copperEnergy = EnergyUnit.electronVoltsToJoules(7.033);
            // when
            final double temperature = PhysicsCalc.QuantumMechanics.fermiTemperature(copperEnergy);
            // then
            assertEquals(81_614.7, temperature, DELTA1);
        }

        @Test
        void testFermiVelocity() {
            // given
            final double copperWaveNumber = 13_586_308_450L;
            // when
            final double velocity = PhysicsCalc.QuantumMechanics.fermiVelocity(copperWaveNumber);
            // then
            assertEquals(1_572_854.8, velocity, DELTA1);
        }

        @Test
        void testFermiDiracDistribution() {
            // given
            final double energy = 1.1268e-17;
            final double fermiEnergyJoules = 1.1268e-18;
            final double temperature = 81_612;
            // when
            final double fermiFunction = PhysicsCalc.QuantumMechanics
                .fermiDiracDistribution(energy, fermiEnergyJoules, temperature);
            // then
            assertEquals(0.000123, fermiFunction, DELTA1);
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

        @Test
        void testHydraulicGradient() {
            // given
            final byte headAtPoint1 = 5;
            final byte headAtPoint2 = 14;
            final byte distance = 9;
            // when
            final double gradient = PhysicsCalc.FluidMechanics.hydraulicGradient(headAtPoint1, headAtPoint2, distance);
            // then
            assertEquals(-1, gradient, DELTA1);
        }

        @Test
        void testStokesLaw() {
            // given
            final double accelerationOfGravity = AccelerationUnit.GRAVITATIONAL_ACCELERATION_ON_EARTH;
            final double mediumViscosity = 0.38;
            final short mediumDensity = 850;
            final short particleDensity = 2710;
            final double particleDiameter = 0.01;
            // when
            final double terminalVelocity = PhysicsCalc.FluidMechanics
                .stokesLaw(accelerationOfGravity, mediumViscosity, mediumDensity, particleDensity, particleDiameter);
            // then
            assertEquals(0.266672, terminalVelocity, DELTA6);
        }

        @Test
        void testHydraulicPressure() {
            // given
            final double pistonForce = 980.7;
            final double pistonArea = 0.0007069;
            // when
            final double pressure = PhysicsCalc.FluidMechanics.hydraulicPressure(pistonForce, pistonArea);
            // then
            assertEquals(1387325, pressure, DELTA1);
        }

        @Test
        void testHydraulicPressurePistonForce() {
            // given
            final double pistonArea = 0.0007069;
            final double pistonArea2 = 0.07069;
            final double pistonForce2 = 98066.5;
            // when
            final double pistonForce = PhysicsCalc.FluidMechanics
                .hydraulicPressurePistonForce(pistonArea, pistonArea2, pistonForce2);
            // then
            assertEquals(980.7, pistonForce, DELTA1);
        }

        @Test
        void testHydraulicPressureSecondPistonForce() {
            // given
            final double pistonArea = 0.0007069;
            final double pistonArea2 = 0.07069;
            final double pistonForce = 980.7;
            // when
            final double pistonForce2 = PhysicsCalc.FluidMechanics
                .hydraulicPressureSecondPistonForce(pistonArea, pistonArea2, pistonForce);
            // then
            assertEquals(98070, pistonForce2, DELTA1);
        }

        @Test
        void testHydraulicLiftingDistanceWorkDone() {
            // given
            final double pistonForce = 980.7;
            final double liftingDistanceOfPiston = 1.4;
            // when
            final double workDone = PhysicsCalc.FluidMechanics
                .hydraulicPressureLiftingDistanceWorkDone(pistonForce, liftingDistanceOfPiston);
            // then
            assertEquals(1373, workDone, DELTA1);
        }

        @Test
        void testWaterDensity() {
            // given
            final byte temperature = 20;
            final byte salinity = 35;
            // when
            final double density = PhysicsCalc.FluidMechanics.waterDensity(temperature, salinity);
            // then
            assertEquals(1024.7, density, DELTA1);
        }

        @Test
        void testMassOfWater() {
            // given
            final byte massOfSalt = 1;
            // when
            final double massOfWater = PhysicsCalc.FluidMechanics.massOfWater(massOfSalt);
            // then
            assertEquals(27.57, massOfWater, DELTA2);
        }

        @Test
        void testMassOfSalt() {
            // given
            final double massOfWater = 27.57;
            // when
            final double massOfSalt = PhysicsCalc.FluidMechanics.massOfSalt(massOfWater);
            // then
            assertEquals(1, massOfSalt, DELTA1);
        }

        @Test
        void testSinkInWater() {
            // given
            final double appleDensity = 0.808;
            // when
            final boolean result = PhysicsCalc.FluidMechanics.sinkInWater(DensityUnit.WATER_DENSITY, appleDensity);
            // then
            assertFalse(result);
        }

        @Test
        void testApiGravityDegree() {
            // given kerosene
            final double crudeLiquidDensity = 0.775; // g/m³, 775 kg/m³
            // when
            final double degree = PhysicsCalc.FluidMechanics.apiGravityDegree(crudeLiquidDensity);
            // then
            assertEquals(51, degree, DELTA1);
        }

        @Test
        void testArchimedesPrinciple() {
            // given aluminum block wholly immersed in water
            final double objectTrueMass = 1e6; // kg
            final double objectDensity = 2.7e3; // kg/m³
            // when
            final double buoyancyForce = PhysicsCalc.FluidMechanics.archimedesPrinciple(objectTrueMass, objectDensity);
            // then
            assertEquals(3_631_620.4, buoyancyForce, DELTA1);
        }

        @Test
        void testArchimedesPrincipleObjApparentMass() {
            // given aluminum block wholly immersed in water
            final double objectTrueMass = 1e6; // kg
            final double displacedFluidMassKg = 370_370;
            // when
            final double mass = PhysicsCalc.FluidMechanics
                .archimedesPrincipleObjApparentMass(objectTrueMass, displacedFluidMassKg);
            // then
            assertEquals(629_630, mass, DELTA1);
        }

        @Test
        void testArchimedesPrincipleObjApparentWeight() {
            // given aluminum block wholly immersed in water
            final double objectTrueMass = 1e6; // kg
            final double buoyancyForce = 3_632_089;
            // when
            final double weight = PhysicsCalc.FluidMechanics
                .archimedesPrincipleObjApparentWeight(objectTrueMass, buoyancyForce);
            // then
            assertEquals(6_174_561, weight, DELTA1);
        }

        @Test
        void testVolumetricFlowRateInCircularPipe() {
            // given
            final double diameter = 0.0762;
            final double velocity = 3.048;
            final double emptyPipe = 0;
            // when
            final double flowRate = PhysicsCalc.FluidMechanics
                .volumetricFlowRateInCircularPipe(diameter, velocity, emptyPipe);
            // then
            assertEquals(0.0139, flowRate, DELTA4);
        }

        @Test
        void testVolumetricFlowRateInCircularPipeBernoulliEq() {
            // given
            final double pipeDiameter = 0.2;
            final byte fluidSpeed = 2;
            // when
            final double flowRate = PhysicsCalc.FluidMechanics
                .volumetricFlowRateInCircularPipeBernoulliEq(pipeDiameter, fluidSpeed);
            // then
            assertEquals(0.06283, flowRate, DELTA5);
        }

        @Test
        void testVolumetricFlowRateInCircularPipePartiallyFilled() {
            // given
            final double diameter = 0.0762;
            final double velocity = 3.048;
            final double filled = 0.07;
            // when
            final double flowRate = PhysicsCalc.FluidMechanics
                .volumetricFlowRateInCircularPipe(diameter, velocity, filled);
            // then
            assertEquals(0.013366, flowRate, DELTA6);
        }

        @Test
        void testMassFlowRateInCircularPipe() {
            // given
            final double diameter = 0.0762;
            final double velocity = 3.048;
            final double emptyPipe = 0;
            final double density = VolumeUnit.gcm3ToKgm3(DensityUnit.WATER_DENSITY);
            // when
            final double massFlowRate = PhysicsCalc.FluidMechanics
                .massFlowRateInCircularPipe(diameter, velocity, density, emptyPipe);
            // then
            assertEquals(13.898, massFlowRate, DELTA3);
        }

        @Test
        void testMassFlowRateInCircularPipeBernoulliEq() {
            // given
            final double volumetricFlowRate = 0.06283;
            final short density = 1000;
            // when
            final double massFlowRate = PhysicsCalc.FluidMechanics
                .massFlowRateInCircularPipeBernoulliEq(volumetricFlowRate, density);
            // then
            assertEquals(62.83, massFlowRate, DELTA2);
        }

        @Test
        void testMassFlowRateInCircularPipePartiallyFilled() {
            // given
            final double diameter = 0.0762;
            final double velocity = 3.048;
            final double filled = 0.07;
            final double density = VolumeUnit.gcm3ToKgm3(DensityUnit.WATER_DENSITY);
            // when
            final double massFlowRate = PhysicsCalc.FluidMechanics
                .massFlowRateInCircularPipe(diameter, velocity, density, filled);
            // then
            assertEquals(13.364, massFlowRate, DELTA3);
        }

        @Test
        void testVolumetricFlowRateInRectangle() {
            // given
            final double width = 0.06;
            final double height = 0.03;
            final double velocity = 3.048;
            // when
            final double flowRate = PhysicsCalc.FluidMechanics.volumetricFlowRateInRectangle(width, height, velocity);
            // then
            assertEquals(0.005486, flowRate, DELTA6);
        }

        @Test
        void testMassFlowRateInRectangle() {
            // given
            final double width = 0.06;
            final double height = 0.03;
            final double velocity = 3.048;
            final double density = VolumeUnit.gcm3ToKgm3(DensityUnit.WATER_DENSITY);
            // when
            final double massFlowRate = PhysicsCalc.FluidMechanics
                .massFlowRateInRectangle(width, height, velocity, density);
            // then
            assertEquals(5.486, massFlowRate, DELTA3);
        }

        @Test
        void testKinematicViscosity() {
            // given water at 78 °C
            final double dynamicViscosity = 0.36336; // mPa⋅s
            final double density = 0.973; // kg/m³
            // when
            final double kinematicViscosity = PhysicsCalc.FluidMechanics.kinematicViscosity(dynamicViscosity, density);
            // then
            assertEquals(0.37344, kinematicViscosity, DELTA5); // mm²/s
        }

        @Test
        void testMachNumber() {
            // given
            final double objectSpeed = 416.67;
            // when
            final double number = PhysicsCalc.FluidMechanics.machNumber(objectSpeed, SpeedUnit.SOUND_SPEED);
            // then
            assertEquals(1.214, number, DELTA3);
        }

        @Test
        void testObjectSpeedFromMachNumber() {
            // given
            final byte machNumber = 3;
            // when
            final double objectSpeed = PhysicsCalc.FluidMechanics
                .objectSpeedFromMachNumber(SpeedUnit.SOUND_SPEED, machNumber);
            // then
            assertEquals(1029.6, objectSpeed, DELTA1);
        }

        @Test
        void testObliqueShockDownstreamMachNumber() {
            // given
            final double specificHeatRatio = 1.4;
            final byte upstreamMachNumber = 5;
            final double waveAngle = AngleUnit.degToRadians(20);
            // when
            final double turnAngle = PhysicsCalc.FluidMechanics
                .obliqueShockDownstreamMachNumber(specificHeatRatio, upstreamMachNumber, waveAngle);
            // then
            assertEquals(3.933, turnAngle, DELTA3);
        }

        @Test
        void testNormalShockUpstreamMachNumber() {
            // given
            final byte upstreamMachNumber = 5;
            final double waveAngle = AngleUnit.degToRadians(20);
            // when
            final double normalShock = PhysicsCalc.FluidMechanics
                .normalShockUpstreamMachNumber(upstreamMachNumber, waveAngle);
            // then
            assertEquals(1.71, normalShock, DELTA2);
        }

        @Test
        void testNormalShockDownstreamMachNumber() {
            // given
            final double downstreamMachNumber = 3.933;
            final double waveAngle = AngleUnit.degToRadians(20);
            final double turnAngle = 0.18615;
            // when
            final double normalShock = PhysicsCalc.FluidMechanics
                .normalShockDownstreamMachNumber(downstreamMachNumber, waveAngle, turnAngle);
            // then
            assertEquals(0.638, normalShock, DELTA3);
        }

        @Test
        void testObliqueShockPressureRatio() {
            // given
            final double specificHeatRatio = 1.4;
            final byte upstreamMachNumber = 5;
            final double waveAngle = AngleUnit.degToRadians(20);
            // when
            final double ratio = PhysicsCalc.FluidMechanics
                .obliqueShockPressureRatio(specificHeatRatio, upstreamMachNumber, waveAngle);
            // then
            assertEquals(3.245, ratio, DELTA3);
        }

        @Test
        void testObliqueShockStagnationPressureRatio() {
            // given
            final double specificHeatRatio = 1.4;
            final byte upstreamMachNumber = 5;
            final double waveAngle = AngleUnit.degToRadians(20);
            // when
            final double ratio = PhysicsCalc.FluidMechanics
                .obliqueShockStagnationPressureRatio(specificHeatRatio, upstreamMachNumber, waveAngle);
            // then
            assertEquals(0.8515, ratio, DELTA4);
        }

        @Test
        void testObliqueShockTemperatureRatio() {
            // given
            final double specificHeatRatio = 1.4;
            final byte upstreamMachNumber = 5;
            final double waveAngle = AngleUnit.degToRadians(20);
            // when
            final double ratio = PhysicsCalc.FluidMechanics
                .obliqueShockTemperatureRatio(specificHeatRatio, upstreamMachNumber, waveAngle);
            // then
            assertEquals(1.4656, ratio, DELTA4);
        }

        @Test
        void testObliqueShockDensityRatio() {
            // given
            final double specificHeatRatio = 1.4;
            final byte upstreamMachNumber = 5;
            final double waveAngle = AngleUnit.degToRadians(20);
            // when
            final double ratio = PhysicsCalc.FluidMechanics
                .obliqueShockDensityRatio(specificHeatRatio, upstreamMachNumber, waveAngle);
            // then
            assertEquals(2.2142, ratio, DELTA4);
        }

        @Test
        void testHydraulicMeanDepth() {
            // given
            final byte areaOfCrossSection = 1;
            final double channelWidth = 0.5;
            // when
            final double depth = PhysicsCalc.FluidMechanics.hydraulicMeanDepth(areaOfCrossSection, channelWidth);
            // then
            assertEquals(2, depth, DELTA1);
        }

        @Test
        void testHydraulicMeanDepthFromFroudeNumber() {
            // given
            final byte flowVelocity = 2;
            final double froudeNumber = 0.4;
            // when
            final double depth = PhysicsCalc.FluidMechanics
                .hydraulicMeanDepthFromFroudeNumber(flowVelocity, froudeNumber);
            // then
            assertEquals(2.5493, depth, DELTA4);
        }

        @Test
        void testChannelWidthFromHydraulicMeanDepth() {
            // given
            final double areaOfCrossSection = 1.5;
            final double depth = 2.5493;
            // when
            final double width = PhysicsCalc.FluidMechanics
                .channelWidthFromHydraulicMeanDepth(areaOfCrossSection, depth);
            // then
            assertEquals(0.5884, width, DELTA4);
        }

        @Test
        void testFroudeNumber() {
            // given
            final byte flowVelocity = 1;
            final byte hydraulicMeanDepth = 2;
            // when
            final double number = PhysicsCalc.FluidMechanics.froudeNumber(flowVelocity, hydraulicMeanDepth);
            // then
            assertEquals(0.2258, number, DELTA4);
        }

        @Test
        void testHydraulicRadiusOfPipe() {
            // given
            final double radius = 0.6;
            // when
            final double hydraulicRadius = PhysicsCalc.FluidMechanics.hydraulicRadiusOfPipe(radius);
            // then
            assertEquals(0.3, hydraulicRadius, DELTA1);
        }

        @Test
        void testHydraulicRadiusOfPartiallyFilledPipe() {
            // given
            final double radius = 0.6;
            final double filledHeight = 0.2;
            // when
            final double hydraulicRadius = PhysicsCalc.FluidMechanics
                .hydraulicRadiusOfPartiallyFilledPipe(radius, filledHeight);
            // then
            assertEquals(0.12276, hydraulicRadius, DELTA5);
        }

        @Test
        void testHydraulicRadiusOfRectangularChannel() {
            // given
            final byte height = 2;
            final byte width = 6;
            // when
            final double radius = PhysicsCalc.FluidMechanics.hydraulicRadiusOfRectangularChannel(width, height);
            // then
            assertEquals(1.2, radius, DELTA1);
        }

        @Test
        void testHydraulicRadiusOfTrapezoidalChannel() {
            // given
            final byte height = 2;
            final double topWidth = 2.5;
            final byte bottomWidth = 1;
            // when
            final double radius = PhysicsCalc.FluidMechanics
                .hydraulicRadiusOfTrapezoidalChannel(topWidth, bottomWidth, height);
            // then
            assertEquals(0.6639, radius, DELTA4);
        }

        @Test
        void testHydraulicRadiusOfTriangularChannel() {
            // given
            final byte width = 5;
            final byte height = 2;
            // when
            final double radius = PhysicsCalc.FluidMechanics.hydraulicRadiusOfTriangularChannel(width, height);
            // then
            assertEquals(0.7809, radius, DELTA4);
        }

        @Test
        void testHydrostaticPressure() {
            // given pure water and the Mariana Trench in the Pacific Ocean
            final short fluidDensity = 1000;
            final short depth = 11_000;
            final int externalPressure = PressureUnit.ATMOSPHERIC_PRESSURE;
            // when
            final double pressure = PhysicsCalc.FluidMechanics
                .hydrostaticPressure(fluidDensity, depth, externalPressure);
            // then
            assertEquals(107_974_475, pressure, DELTA1);
        }

        @Test
        void testKnudsenNumber() {
            // given
            final double meanFreePath = 0.08;
            final double characteristicLinearDimension = 0.05;
            // when
            final double number = PhysicsCalc.FluidMechanics.knudsenNumber(meanFreePath, characteristicLinearDimension);
            // then
            assertEquals(1.6, number, DELTA1);
        }

        @Test
        void testReynoldsNumber() {
            // given
            final byte fluidVelocity = 1;
            final double characteristicLinearDimension = 0.25;
            final double fluidDensity = 999.7;
            final double dynamicViscosity = 0.001308;
            // when
            final double number = PhysicsCalc.FluidMechanics
                .reynoldsNumber(fluidVelocity, characteristicLinearDimension, fluidDensity, dynamicViscosity);
            // then
            assertEquals(191_074.1, number, DELTA1);
        }

        @Test
        void testReynoldsNumberFromKinematicViscosity() {
            // given
            final byte fluidVelocity = 1;
            final double characteristicLinearDimension = 0.25;
            final double kinematicViscosity = 0.0000013084;
            // when
            final double number = PhysicsCalc.FluidMechanics
                .reynoldsNumberFromKinematicViscosity(fluidVelocity, characteristicLinearDimension, kinematicViscosity);
            // then
            assertEquals(191_073, number, DELTA1);
        }

        @Test
        void testBernoulliEquationSolveForSpeed() {
            // given
            final short fluidDensity = 1000;
            final short pressurePosition1 = 1000;
            final double height = 3;
            final double fluidSpeed = 2;
            final short pressurePosition2 = 1200;
            // when
            final double speed = PhysicsCalc.FluidMechanics.bernoulliEquationSolveForSpeed(fluidDensity,
                pressurePosition1, height, fluidSpeed, pressurePosition2, height);
            // then
            assertEquals(1.8974, speed, DELTA4);
        }

        @Test
        void testMagnusForce() {
            // given
            final double radius = 0.5;
            final byte length = 10;
            final byte angularVelocity = 20;
            final byte freeStreamVelocity = 10;
            // when
            final double force = PhysicsCalc.FluidMechanics
                .magnusForce(radius, length, angularVelocity, DensityUnit.AIR_DENSITY, freeStreamVelocity);
            // then
            assertEquals(3848.451, force, DELTA3);
        }

        @Test
        void testLiftCoefficient() {
            // given air
            final short liftForce = 800;
            final byte flowSpeed = 100;
            final byte surfaceArea = 1;
            // when
            final double coeff = PhysicsCalc.FluidMechanics
                .liftCoefficient(liftForce, flowSpeed, surfaceArea, DensityUnit.AIR_DENSITY);
            // then
            assertEquals(0.1306, coeff, DELTA4);
        }

        @Test
        void testLiftForce() {
            // given air
            final double liftCoeff = 0.52;
            final short flowSpeed = 150;
            final byte surfaceArea = 2;
            // when
            final double force = PhysicsCalc.FluidMechanics
                .liftForce(liftCoeff, flowSpeed, surfaceArea, DensityUnit.AIR_DENSITY);
            // then
            assertEquals(14_332.5, force, DELTA1);
        }

        @Test
        void testDarcysLawHydraulicGradient() {
            // given
            final byte pressureIn = 120;
            final byte pressureOut = 20;
            final byte pressureDifference = pressureIn - pressureOut;
            final byte viscosity = 14;
            final double distance = 38.095;
            // when
            final double hydraulicGradient = PhysicsCalc.FluidMechanics
                .darcysLawHydraulicGradient(pressureDifference, viscosity, distance);
            // then
            assertEquals(0.1875, hydraulicGradient, DELTA4);
        }

        @Test
        void testDarcysLawFlowRate() {
            // given
            final byte area = 2;
            final double permeability = 0.8;
            final double hydraulicGradient = 0.1875;
            // when
            final double flowRate = PhysicsCalc.FluidMechanics
                .darcysLawFlowRate(area, permeability, hydraulicGradient);
            // then
            assertEquals(0.3, flowRate, DELTA1);
        }

        @Test
        void testDarcysLawFlowRateFromVolumeAndTime() {
            // given
            final byte volume = 3;
            final double time = 10;
            // when
            final double flowRate = PhysicsCalc.FluidMechanics.darcysLawFlowRate(volume, time);
            // then
            assertEquals(0.3, flowRate, DELTA1);
        }

        @Test
        void testDarcysLawPermeability() {
            // given
            final double flowRate = 0.3;
            final byte area = 2;
            final double hydraulicGradient = 0.1875;
            // when
            final double permeability = PhysicsCalc.FluidMechanics
                .darcysLawPermeability(flowRate, area, hydraulicGradient);
            // then
            assertEquals(0.8, permeability, DELTA1);
        }

        @Test
        void testDarcysLawPermeabilityFromVolumeAndTime() {
            // given
            final double volume = 3;
            final double time = 10;
            final byte area = 2;
            final double hydraulicGradient = 0.1875;
            // when
            final double permeability = PhysicsCalc.FluidMechanics
                .darcysLawPermeability(volume, time, area, hydraulicGradient);
            // then
            assertEquals(0.8, permeability, DELTA1);
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
        void testForceGivenVelocitiesAndDeltaTime() {
            // given
            final byte massKg = 10;
            final byte initialVelocity = 100;
            final byte finalVelocity = 120;
            final byte changeInTime = 6;
            // when
            final double force = PhysicsCalc.Dynamics.force(massKg, initialVelocity, finalVelocity, changeInTime);
            // then
            assertEquals(33.33, force, DELTA2);
        }

        @Test
        void testNewtons3rdLawReactionForce() {
            // given
            final byte massKg = 20;
            final byte acceleration = 70;
            // when
            final double force = PhysicsCalc.Dynamics.newtons3rdLawReactionForce(massKg, acceleration);
            // then
            assertEquals(-1400, force, DELTA1);
        }

        @Test
        void testNewtons3rdLawAcceleration() {
            // given
            final byte massKg1 = 90;
            final byte acceleration1 = 20;
            final double actionForce = PhysicsCalc.Dynamics.force(massKg1, acceleration1);
            final byte massKg2 = 30;
            // when
            final double acceleration2 = PhysicsCalc.Dynamics.newtons3rdLawAcceleration(actionForce, massKg2);
            // then
            assertEquals(-60, acceleration2, DELTA1);
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

        @Test
        void testStoppingDistance() {
            // given
            final double vehicleSpeed = 120;
            final double perceptionReactionTime = 1.5;
            final double roadGrade = 0; // flat surface
            final double wetRoadFrictionCoeff = 0.27;
            // when
            final double distance = PhysicsCalc.Dynamics
                .stoppingDistance(vehicleSpeed, perceptionReactionTime, roadGrade, wetRoadFrictionCoeff);
            // then
            assertEquals(260, distance, DELTA1);
        }

        @Test
        void testBrakingTime() {
            // given
            final double initialSpeed = LengthUnit.kmphToMetersPerSecond(120);
            final double deceleration = 43.7;
            // when
            final double time = PhysicsCalc.Dynamics.brakingTime(initialSpeed, deceleration);
            // then
            assertEquals(0.7628, time, DELTA4);
        }

        @Test
        void testHookesLawForce() {
            // given
            final double springDisplacement = 0.15;
            // initialSpringLength = 2 m
            // finalSpringLength = 2 m + 0.15 m = 2.15 m
            final byte springForceConstant = 80;
            // when
            final double force = PhysicsCalc.Dynamics.hookesLawForce(springDisplacement, springForceConstant);
            // then
            assertEquals(-12, force, DELTA1);
        }

        static List<Arguments> tensionHangingObjectOnRopesArgs() {
            final double angle30 = AngleUnit.degToRadians(30);
            final double angle60 = AngleUnit.degToRadians(60);
            final double weight = PhysicsCalc.Statics.massToWeight(10);
            return List.of(
                Arguments.of(weight, angle60, angle60, new double[]{56.61, 56.61}, DELTA2),
                Arguments.of(weight, angle60, angle30, new double[]{84.92, 49.03}, DELTA2)
            );
        }

        @ParameterizedTest
        @MethodSource("tensionHangingObjectOnRopesArgs")
        void testTensionHangingObjectOnRopes(double weightN, double angleAlpha, double angleBeta,
                                             double[] expectedResult, double delta) {
            // when
            final double[] tensions = PhysicsCalc.Dynamics.tensionHangingObjectOnRopes(weightN, angleAlpha, angleBeta);
            // then
            assertArrayEquals(expectedResult, tensions, delta);
        }

        static List<Arguments> tensionPullingOnFrictionlessSurfaceArgs() {
            final double angle30 = AngleUnit.degToRadians(30);
            final double angle45 = AngleUnit.degToRadians(45);
            final double angle60 = AngleUnit.degToRadians(60);
            return List.of(
                Arguments.of(new double[]{3}, 20, angle30, new double[]{20}, DELTA1),
                Arguments.of(new double[]{3, 2}, 24, angle60, new double[]{24, 4.8}, DELTA1),
                Arguments.of(new double[]{3, 2, 5}, 32, angle45, new double[]{32, 15.84, 11.314}, DELTA3)
            );
        }

        @ParameterizedTest
        @MethodSource("tensionPullingOnFrictionlessSurfaceArgs")
        void testTensionPullingOnFrictionlessSurface(double[] masses, double pullingForceN, double angleTheta,
                                                     double[] expectedResult, double delta) {
            // when
            final double[] tensions = PhysicsCalc.Dynamics
                .tensionPullingOnFrictionlessSurface(masses, pullingForceN, angleTheta);
            // then
            assertArrayEquals(expectedResult, tensions, delta);
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

        @Test
        void testDipoleMoment() {
            // given
            final double distanceBetweenCharges = 0.2;
            final double charge = 0.5;
            // when
            final double electricDipoleMoment = PhysicsCalc.Electromagnetism
                .dipoleMoment(distanceBetweenCharges, charge);
            // then
            assertEquals(0.1, electricDipoleMoment, DELTA1);
        }

        @Test
        void testDipoleMomentSystemOfCharges() {
            // given
            final double[] referencePoint = {-1.5, 2.5, 2};
            final double[] charges = new double[]{0.25, -0.14, 0.17};
            final double[][] chargeCoordinates = new double[][]{
                {2, 3, 3},
                {1, -1, 1.5},
                {-1, -0.5, 2}
            };
            // when
            final double[] dipoleMoment = PhysicsCalc.Electromagnetism
                .dipoleMomentSystemOfCharges(referencePoint, charges, chargeCoordinates);
            // then
            assertArrayEquals(new double[]{0.61, 0.105, 0.32}, dipoleMoment, DELTA3);
        }

        @Test
        void testGaussLaw() {
            // given
            final double charge = 1e-8;
            // when
            final double electricFlux = PhysicsCalc.Electromagnetism.gaussLaw(charge);
            // then
            assertEquals(1129.4, electricFlux, DELTA1);
        }

        @Test
        void testGaussLawCharge() {
            // given
            final double electricFlux = 1129.4;
            // when
            final double charge = PhysicsCalc.Electromagnetism.gaussLawCharge(electricFlux);
            // then
            assertEquals(1e-8, charge, DELTA1);
        }

        @Test
        void testMagneticPermeability() {
            // given iron (99.95% pure)
            final int relativePermeability = 200_000;
            // when
            final double permeability = PhysicsCalc.Electromagnetism.magneticPermeability(relativePermeability);
            // then
            assertEquals(0.25133, permeability, DELTA5);
        }

        @Test
        void testMagneticRelativePermeability() {
            // given iron (99.95% pure)
            final int susceptibility = 199_999;
            // when
            final double relativePerm = PhysicsCalc.Electromagnetism.magneticRelativePermeability(susceptibility);
            // then
            assertEquals(200_000, relativePerm, DELTA1);
        }

        @Test
        void testMagneticSusceptibility() {
            // given iron (99.95% pure)
            final int relativePermeability = 200_000;
            // when
            final double susceptibility = PhysicsCalc.Electromagnetism.magneticSusceptibility(relativePermeability);
            // then
            assertEquals(199_999, susceptibility, DELTA1);
        }

        @Test
        void testHallCoefficient() {
            // given
            final double voltage = 0.00005;
            final double thickness = 0.00002;
            final byte current = 10;
            final double magneticField = 0.7519;
            // when
            final double hallCoefficient = PhysicsCalc.Electromagnetism
                .hallCoefficient(voltage, thickness, current, magneticField);
            // then
            assertEquals(LengthUnit.millimetersToMeters(0.133), hallCoefficient, DELTA3);
        }

        @Test
        void testDriftVelocity() {
            // given
            final byte current = 10;
            final double area = 0.000001;
            final double numberDensity = 8.94e28;
            final double charge = 1.6022e-19;
            // when
            final double velocity = PhysicsCalc.Electromagnetism.driftVelocity(current, area, numberDensity, charge);
            // then
            assertEquals(0.0006982, velocity, DELTA7);
        }

        @Test
        void testMagneticDipoleMoment() {
            // given
            final byte current = 2;
            final byte loopLength = 2;
            // when
            final double moment = PhysicsCalc.Electromagnetism.magneticDipoleMoment(current, loopLength);
            // then
            assertEquals(0.6366, moment, DELTA4);
        }

        @Test
        void testSolenoidMagneticDipoleMoment() {
            // given
            final double current = 0.127;
            final double solenoidRadius = 0.5;
            final short turns = 150;
            // when
            final double moment = PhysicsCalc.Electromagnetism
                .solenoidMagneticDipoleMoment(current, solenoidRadius, turns);
            // then
            assertEquals(14.962, moment, DELTA3);
        }

        @Test
        void testCurrentFromMagneticDipoleMoment() {
            // given
            final double moment = 14.962;
            final double area = 0.7854;
            final short turns = 150;
            // when
            final double current = PhysicsCalc.Electromagnetism.currentFromMagneticDipoleMoment(moment, area, turns);
            // then
            assertEquals(0.127, current, DELTA3);
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
        void testEquivalentResistanceInSeries() {
            // given
            final double[] resistors = new double[]{1500, 300, 700};
            // when
            final double resistance = PhysicsCalc.Electronics.equivalentResistanceInSeries(resistors);
            // then
            assertEquals(2500, resistance, DELTA1);
        }

        @Test
        void testMissingResistorInSeries() {
            // given
            final double desiredTotalResistance = 2500;
            final double[] resistors = new double[]{1500, 300};
            // when
            final double resistor = PhysicsCalc.Electronics.missingResistorInSeries(resistors, desiredTotalResistance);
            // then
            assertEquals(700, resistor, DELTA1);
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

        @Test
        void testSolenoidMagneticField() {
            // given
            final double current = 0.1;
            final double length = 0.1;
            final byte turns = 100;
            // when
            final double magneticField = PhysicsCalc.Electronics.solenoidMagneticField(current, length, turns);
            // then
            assertEquals(0.00012566, magneticField, DELTA8);
        }

        @Test
        void testRlcCircuitQFactor() {
            // given
            final double capacitance = CapacitanceUnit.microFaradsToFarads(50);
            final double inductance = InductanceUnit.milliHenriesToHenries(25);
            final byte resistance = 30;
            // when
            final double qFactor = PhysicsCalc.Electronics.rlcCircuitQFactor(capacitance, inductance, resistance);
            // then
            assertEquals(0.7454, qFactor, DELTA4);
        }

        @Test
        void testRlcCircuitFrequency() {
            // given
            final double capacitance = CapacitanceUnit.microFaradsToFarads(50);
            final double inductance = InductanceUnit.milliHenriesToHenries(25);
            // when
            final double frequency = PhysicsCalc.Electronics.rlcCircuitFrequency(capacitance, inductance);
            // then
            assertEquals(142.35, frequency, DELTA2);
        }

        @Test
        void testResonantFrequencyLC() {
            // given
            final double capacitance = 2.2e-10;
            final double inductance = InductanceUnit.milliHenriesToHenries(1);
            // when
            final double frequency = PhysicsCalc.Electronics.resonantFrequencyLC(capacitance, inductance);
            // then
            assertEquals(339_319.4, frequency, DELTA1);
        }

        @Test
        void testResonantFrequencyLCCapacitiveReactance() {
            // given
            final double frequency = 339_319.4;
            final double capacitance = 2.2e-10;
            // when
            final double capacitiveReactance = PhysicsCalc.Electronics
                .resonantFrequencyLCCapacitiveReactance(frequency, capacitance);
            // then
            assertEquals(2132, capacitiveReactance, DELTA1);
        }

        @Test
        void testResonantFrequencyLCInductiveReactance() {
            // given
            final double frequency = 339_319.4;
            final double inductance = InductanceUnit.milliHenriesToHenries(1);
            // when
            final double inductiveReactance = PhysicsCalc.Electronics
                .resonantFrequencyLCInductiveReactance(frequency, inductance);
            // then
            assertEquals(2132, inductiveReactance, DELTA1);
        }

        @Test
        void testRCCircuitFrequency() {
            // given
            final double capacitance = 0.00005;
            final byte resistance = 30;
            // when
            final double frequency = PhysicsCalc.Electronics.rcCircuitFrequency(capacitance, resistance);
            // then
            assertEquals(106.1, frequency, DELTA1);
        }

        @Test
        void testRCCircuitChargingTime() {
            // given
            final double capacitance = 0.00005;
            final byte resistance = 30;
            // when
            final double time = PhysicsCalc.Electronics.rcCircuitChargingTime(capacitance, resistance);
            // then
            assertEquals(0.0015, time, DELTA4);
        }

        @Test
        void testMagneticForceOnCurrentCarryingWire() {
            // given
            final double magneticField = 0.00005;
            final double current = 2.5;
            final double length = 0.01;
            final double angleRad = 1.5708;
            // when
            final double force = PhysicsCalc.Electronics
                .magneticForceOnCurrentCarryingWire(magneticField, current, length, angleRad);
            // then
            assertEquals(0.00000125, force, DELTA8);
        }

        @Test
        void testMagneticForceBetweenWires() {
            // given
            final double current1 = 1000;
            final double current2 = 500;
            final double distance = 0.5;
            // when
            final double force = PhysicsCalc.Electronics.magneticForceBetweenWires(current1, current2, distance);
            // then
            assertEquals(0.2, force, DELTA1);
        }

        @Test
        void testLedSeriesResistance() {
            // given
            final byte numberOfLEDs = 2;
            final short supplyVoltage = 5;
            final double current = ElectricCurrentUnit.milliAmperesToAmperes(20);
            final byte ledRedColor = 2;
            // when
            final double resistance = PhysicsCalc.Electronics
                .ledSeriesResistance(numberOfLEDs, supplyVoltage, current, ledRedColor);
            // then
            assertEquals(50, resistance, DELTA1);
        }

        @Test
        void testLedDissipatedPowerInSingleLED() {
            // given
            final double current = ElectricCurrentUnit.milliAmperesToAmperes(20);
            final double voltageDropAcrossLED = 2; // LED red color
            // when
            final double power = PhysicsCalc.Electronics.ledDissipatedPowerInSingleLED(current, voltageDropAcrossLED);
            // then
            assertEquals(0.04, power, DELTA2);
        }

        @Test
        void testLedsTotalDissipatedPower() {
            // given
            final double current = ElectricCurrentUnit.milliAmperesToAmperes(20);
            final byte numberOfLEDs = 2;
            final double voltageDropAcrossLED = 2; // LED red color
            // when
            final double totalDissipatedPower = PhysicsCalc.Electronics
                .ledsTotalDissipatedPower(numberOfLEDs, current, voltageDropAcrossLED);
            // then
            assertEquals(0.08, totalDissipatedPower, DELTA2);
        }

        @Test
        void testLedSeriesDissipatedPowerInResistor() {
            // given
            final byte numberOfLEDs = 2;
            final short supplyVoltage = 5;
            final double current = ElectricCurrentUnit.milliAmperesToAmperes(20);
            final byte ledRedColor = 2;
            final double resistance = PhysicsCalc.Electronics
                .ledSeriesResistance(numberOfLEDs, supplyVoltage, current, ledRedColor);
            // when
            final double resistor = PhysicsCalc.Electronics.ledSeriesDissipatedPowerInResistor(current, resistance);
            // then
            assertEquals(0.02, resistor, DELTA2);
        }

        @Test
        void testLedParallelResistance() {
            // given
            final byte numberOfLEDs = 2;
            final short supplyVoltage = 5;
            final double current = ElectricCurrentUnit.milliAmperesToAmperes(20);
            final byte ledRedColor = 2;
            // when
            final double resistance = PhysicsCalc.Electronics
                .ledParallelResistance(numberOfLEDs, supplyVoltage, current, ledRedColor);
            // then
            assertEquals(75, resistance, DELTA1);
        }

        @Test
        void testLedParallelDissipatedPowerInResistor() {
            // given
            final byte numberOfLEDs = 2;
            final short supplyVoltage = 5;
            final double current = ElectricCurrentUnit.milliAmperesToAmperes(20);
            final byte ledRedColor = 2;
            final double resistance = PhysicsCalc.Electronics
                .ledParallelResistance(numberOfLEDs, supplyVoltage, current, ledRedColor);
            // when
            final double resistor = PhysicsCalc.Electronics
                .ledParallelDissipatedPowerInResistor(numberOfLEDs, current, resistance);
            // then
            assertEquals(0.12, resistor, DELTA2);
        }

        @Test
        void testShockleyDiode() {
            // given
            final byte emissionCoefficient = 1;
            final short saturationCurrent = 150;
            final short thermalVoltage = 220;
            final byte voltageDrop = 20;
            // when
            final double current = PhysicsCalc.Electronics
                .shockleyDiode(emissionCoefficient, saturationCurrent, thermalVoltage, voltageDrop);
            // then
            assertEquals(14.275, current, DELTA3);
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
            assertEquals(342.993, speedSound, DELTA1);
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
            final short frequency = 210;
            // when
            final double wavelength = PhysicsCalc.Acoustics.soundWavelength(SpeedUnit.SOUND_SPEED, frequency);
            // then
            assertEquals(1.6342, wavelength, DELTA4);
        }

        @Test
        void testSoundFrequency() {
            // given
            final double wavelength = 1.6333;
            // when
            final double frequency = PhysicsCalc.Acoustics.soundFrequency(SpeedUnit.SOUND_SPEED, wavelength);
            // then
            assertEquals(210.1, frequency, DELTA1);
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

        @Test
        void testEfficiency() {
            // given
            final short energyInput = 240;
            final short energyOutput = 210;
            // when
            final double efficiency = PhysicsCalc.Thermodynamics.efficiency(energyInput, energyOutput);
            // then
            assertEquals(87.5, efficiency, DELTA1);
        }

        @Test
        void testThermalEfficiency() {
            // given
            final short hotReservoirTemperature = 909;
            final short coldReservoirTemperature = 500;
            // when
            final double thermalEfficiency = PhysicsCalc.Thermodynamics
                .thermalEfficiency(hotReservoirTemperature, coldReservoirTemperature);
            // then
            assertEquals(0.4499, thermalEfficiency, DELTA4);
        }

        @Test
        void testThermalEfficiencyHotReservoirTemp() {
            // given
            final short coldReservoirTemperature = 500;
            final double thermalEfficiency = 44.99; // %
            // when
            final double hotReservoirTemperature = PhysicsCalc.Thermodynamics
                .thermalEfficiencyHotReservoirTemp(coldReservoirTemperature, thermalEfficiency);
            // then
            assertEquals(909, hotReservoirTemperature, DELTA1);
        }

        @Test
        void testIrreversibleThermalEfficiencyHotReservoirTemp() {
            // given
            final int heatReceived = 1_000_000_000;
            final byte thermalEfficiency = 45; // %
            // when
            final double netWorkOutput = PhysicsCalc.Thermodynamics
                .irreversibleThermalEfficiencyNetWorkOutput(heatReceived, thermalEfficiency);
            // then
            assertEquals(450_000_000, netWorkOutput, DELTA1);
        }

        @Test
        void testIrreversibleThermalEfficiencyHeatRejected() {
            // given
            final int heatReceived = 1_000_000_000;
            final int netWorkOutput = 450_000_000;
            // when
            final double heatRejected = PhysicsCalc.Thermodynamics
                .irreversibleThermalEfficiencyHeatRejected(heatReceived, netWorkOutput);
            // then
            assertEquals(550_000_000, heatRejected, DELTA1);
        }

        @Test
        void testHeatCapacity() {
            // given
            final short mass = 236;
            final short specificHeat = 4184;
            // when
            final double heatRejected = PhysicsCalc.Thermodynamics.heatCapacity(mass, specificHeat);
            // then
            assertEquals(987_424, heatRejected, DELTA1);
        }

        @Test
        void testBasicHeatTransfer() {
            // given
            final byte mass = 3;
            final byte specificHeat = 10;
            final byte initialTemperature = 12;
            final byte finalTemperature = 17;
            // when
            final double heatTransfer = PhysicsCalc.Thermodynamics
                .basicHeatTransfer(mass, specificHeat, initialTemperature, finalTemperature);
            // then
            assertEquals(150, heatTransfer, DELTA1);
        }

        @Test
        void testConductionHeatTransfer() {
            // given
            final byte thermalConductivity = 37;
            final byte crossSectionalArea = 49;
            final byte coldTemperature = 2;
            final byte hotTemperature = 32;
            final byte timeTaken = 58;
            final double thicknessOfMaterial = 0.98;
            // when
            final double heatTransfer = PhysicsCalc.Thermodynamics.conductionHeatTransfer(thermalConductivity,
                crossSectionalArea, coldTemperature, hotTemperature, timeTaken, thicknessOfMaterial);
            // then
            assertEquals(3_219_000, heatTransfer, DELTA1);
        }

        @Test
        void testHeatTransferThroughConvection() {
            // given
            final short convectiveHeatTransferCoeff = 2000;
            final byte surfaceArea = 1;
            final byte bulkTemperature = 20;
            final byte surfaceTemperature = 50;
            // when
            final double heatTransferredPerUnitTime = PhysicsCalc.Thermodynamics.heatTransferThroughConvection(
                convectiveHeatTransferCoeff, surfaceArea, bulkTemperature, surfaceTemperature);
            // then
            assertEquals(60_000, heatTransferredPerUnitTime, DELTA1);
        }

        @Test
        void testHeatTransferByRadiation() {
            // given
            final short areaOfHotObject = 1;
            final double emissivity = 0.67;
            final double objectTemperature = 373.15;
            final double envTemperature = 273.15;
            // when
            final double heatTransferredPerUnitTime = PhysicsCalc.Thermodynamics.heatTransferByRadiation(
                areaOfHotObject, emissivity, objectTemperature, envTemperature);
            // then
            assertEquals(-525.09, heatTransferredPerUnitTime, DELTA2);
        }

        @Test
        void testIdealGasLawPressure() {
            // given
            final double amountOfSubstance = 0.1;
            final double temperature = 323.15;
            // when
            final double pressure = PhysicsCalc.Thermodynamics
                .idealGasLawPressure(amountOfSubstance, temperature);
            // then
            assertEquals(268.68, pressure, DELTA2);
        }

        @Test
        void testIdealGasLawTemperature() {
            // given
            final double pressure = 268.68;
            final byte volume = 1;
            final double amountOfSubstance = 0.1;
            // when
            final double temperature = PhysicsCalc.Thermodynamics
                .idealGasLawTemperature(pressure, volume, amountOfSubstance);
            // then
            assertEquals(323.15, temperature, DELTA2);
        }

        @Test
        void testBoylesLawFinalVolume() {
            // given
            final int initialPressure = 101_325;
            final double initialVolume = 0.001;
            final int finalPressure = 81_060;
            // when
            final double finalVolume = PhysicsCalc.Thermodynamics
                .boylesLawFinalVolume(initialPressure, initialVolume, finalPressure);
            // then
            assertEquals(0.00125, finalVolume, DELTA5);
        }

        @Test
        void testBoylesLawFinalPressure() {
            // given
            final int initialPressure = 101_325;
            final double initialVolume = 0.001;
            final double finalVolume = 0.00125;
            // when
            final double finalPressure = PhysicsCalc.Thermodynamics
                .boylesLawFinalPressure(initialPressure, initialVolume, finalVolume);
            // then
            assertEquals(81_060, finalPressure, DELTA1);
        }

        @Test
        void testBoylesLawFinalPressureFromVolumeRatio() {
            // given
            final int initialPressure = 101_325;
            final double initialVolume = 0.001;
            final double finalVolume = 0.00125;
            final double volumeRatio = initialVolume / finalVolume;
            // when
            final double finalPressure = PhysicsCalc.Thermodynamics
                .boylesLawFinalPressure(initialPressure, volumeRatio);
            // then
            assertEquals(81_060, finalPressure, DELTA1);
        }

        @Test
        void testCharlesLawInitialVolume() {
            // given
            final double initialTemperature = 543.2;
            final double finalTemperature = 615.2;
            final double finalVolume = 0.00075;
            // when
            final double initialVolume = PhysicsCalc.Thermodynamics
                .charlesLawInitialVolume(initialTemperature, finalTemperature, finalVolume);
            // then
            assertEquals(0.0006622, initialVolume, DELTA7);
        }

        @Test
        void testCharlesLawFinalVolume() {
            // given
            final double initialVolume = 0.002;
            final double initialTemperature = 308.15;
            final double finalTemperature = 288.15;
            // when
            final double finalVolume = PhysicsCalc.Thermodynamics
                .charlesLawFinalVolume(initialVolume, initialTemperature, finalTemperature);
            // then
            assertEquals(0.0018702, finalVolume, DELTA7);
        }

        @Test
        void testCharlesLawFinalTemperature() {
            // given
            final double initialVolume = 0.0008495;
            final double finalVolume = 0.0017556;
            final short initialTemperature = 295;
            // when
            final double finalTemperature = PhysicsCalc.Thermodynamics
                .charlesLawFinalTemperature(initialVolume, finalVolume, initialTemperature);
            // then
            assertEquals(609.7, finalTemperature, DELTA1);
        }

        @Test
        void testGayLussacsLawFinalPressure() {
            // given
            final int initialPressure = 100_000;
            final double initialTemperature = 293.15;
            final double finalTemperature = 673.15;
            // when
            final double finalPressure = PhysicsCalc.Thermodynamics
                .gayLussacsLawFinalPressure(initialPressure, initialTemperature, finalTemperature);
            // then
            assertEquals(229_626.4, finalPressure, DELTA1);
        }

        @Test
        void testGayLussacsLawFinalTemperature() {
            // given
            final int initialPressure = 162_120;
            final double finalPressure = 101_325;
            final short initialTemperature = 460;
            // when
            final double finalTemperature = PhysicsCalc.Thermodynamics
                .gayLussacsLawFinalTemperature(initialPressure, finalPressure, initialTemperature);
            // then
            assertEquals(287.5, finalTemperature, DELTA1);
        }

        @Test
        void testAmountOfGas() {
            // given
            final int initialPressure = 100_000;
            final double initialTemperature = 293.15;
            final double volume = 0.0003;
            // when
            final double finalPressure = PhysicsCalc.Thermodynamics
                .amountOfGas(initialPressure, initialTemperature, volume);
            // then
            assertEquals(0.012308, finalPressure, DELTA6);
        }

        @Test
        void testJouleHeating() {
            // given
            final short current = 150;
            final byte resistance = 20;
            final byte timeSeconds = 45;
            // when
            final double generatedHeat = PhysicsCalc.Thermodynamics.jouleHeating(current, resistance, timeSeconds);
            // then
            assertEquals(20_250_000, generatedHeat, DELTA1);
        }

        @Test
        void testEvaporationRate() {
            // given
            final double surfaceAreaOfWater = 1.86;
            final double airSpeed = 2.222;
            final byte relativeHumidity = 70;
            final double airTemperature = 20.14;
            // when
            final double evaporationRate = PhysicsCalc.Thermodynamics
                .evaporationRate(surfaceAreaOfWater, airSpeed, airTemperature, relativeHumidity);
            // then
            assertEquals(0.55, evaporationRate, DELTA2);
        }

        @Test
        void testCarnotEfficiency() {
            // given
            final double coldReservoirTemperature = TemperatureUnit.celsiusToKelvin(25);
            final double hotReservoirTemperature = TemperatureUnit.celsiusToKelvin(135);
            // when
            final double carnotEfficiency = PhysicsCalc.Thermodynamics
                .carnotEfficiency(coldReservoirTemperature, hotReservoirTemperature);
            // then
            assertEquals(26.95, carnotEfficiency, DELTA2);
        }

        @Test
        void testCarnotReversibleRefrigeratorCOP() {
            // given
            final double hotMediumTemperature = 408.15;
            final double coldMediumTemperature = 298.15;
            // when
            final double refrigeratorCOP = PhysicsCalc.Thermodynamics
                .carnotReversibleRefrigeratorCOP(hotMediumTemperature, coldMediumTemperature);
            // then
            assertEquals(2.7105, refrigeratorCOP, DELTA4);
        }

        @Test
        void testCarnotReversibleHeatPumpCOP() {
            // given
            final double hotMediumTemperature = 408.15;
            final double coldMediumTemperature = 298.15;
            // when
            final double heatPumpCOP = PhysicsCalc.Thermodynamics
                .carnotReversibleHeatPumpCOP(hotMediumTemperature, coldMediumTemperature);
            // then
            assertEquals(3.7105, heatPumpCOP, DELTA4);
        }

        @Test
        void testRefrigeratorCOP() {
            // given
            final byte hotMediumRejected = 20;
            final byte coldMediumTaken = 12;
            // when
            final double cop = PhysicsCalc.Thermodynamics.refrigeratorCOP(hotMediumRejected, coldMediumTaken);
            // then
            assertEquals(1.5, cop, DELTA1);
        }

        @Test
        void testHeatPumpCOP() {
            // given
            final byte hotMediumRejected = 20;
            final byte coldMediumTaken = 12;
            // when
            final double cop = PhysicsCalc.Thermodynamics.heatPumpCOP(hotMediumRejected, coldMediumTaken);
            // then
            assertEquals(2.5, cop, DELTA1);
        }

        @Test
        void testWorkDoneOnRefrigeratorOrPump() {
            // given
            final byte hotMediumRejected = 20;
            final byte coldMediumTaken = 12;
            // when
            final double workDone = PhysicsCalc.Thermodynamics
                .workDoneOnRefrigeratorOrPump(hotMediumRejected, coldMediumTaken);
            // then
            assertEquals(8, workDone, DELTA1);
        }

        @Test
        void testSpecificGasConstant() {
            // given
            final double molarMass = 31.99;
            // when
            final double constant = PhysicsCalc.Thermodynamics.specificGasConstant(molarMass);
            // then
            assertEquals(0.2599, constant, DELTA4);
        }

        @Test
        void testSpecificGasConstantWithSpecificHeatCapacity() {
            // given
            final double constantPressure = 0.461;
            final double specificGasConstant = 0.2599;
            // when
            final double constant = PhysicsCalc.Thermodynamics
                .specificGasConstantWithSpecificHeatCapacity(constantPressure, specificGasConstant);
            // then
            assertEquals(0.2011, constant, DELTA4);
        }

        @Test
        void testLatentHeat() {
            // given
            final byte mass = 20;
            final double waterVaporizationSpecificLatentHeat = 2264.7;
            // when
            final double latentHeat = PhysicsCalc.Thermodynamics.latentHeat(mass, waterVaporizationSpecificLatentHeat);
            // then
            assertEquals(45_294, latentHeat, DELTA1);
        }

        @Test
        void testCuriesLaw() {
            // given
            final double curieConstant = 1.3;
            final byte magneticField = 1;
            final double temperature = 293.15;
            // when
            final double magnetization = PhysicsCalc.Thermodynamics
                .curiesLaw(curieConstant, magneticField, temperature);
            // then
            assertEquals(0.004435, magnetization, DELTA6);
        }

        @Test
        void testSensibleHeat() {
            // given
            final double mass = 0.075;
            final double airSpecificHeat = 1006.1;
            final byte initialTemperature = 25;
            final byte finalTemperature = -5;
            // when
            final double sensibleHeat = PhysicsCalc.Thermodynamics
                .sensibleHeat(mass, airSpecificHeat, initialTemperature, finalTemperature);
            // then
            assertEquals(-2263.7, sensibleHeat, DELTA1);
        }

        @Test
        void testThermalDiffusivity() {
            // given
            final double thermalConductivity = 0.607;
            final short density = 997;
            final short specificHeatCapacity = 4182;
            // when
            final double thermalDiffusivity = PhysicsCalc.Thermodynamics
                .thermalDiffusivity(thermalConductivity, density, specificHeatCapacity);
            // then
            assertEquals(1.4558e-7, thermalDiffusivity, DELTA5);
        }

        @Test
        void testIdealGasDensity() {
            // given
            final short specificGasConstant = 287;
            final int absolutePressure = 101_325;
            final double temperature = 288.15;
            // when
            final double density = PhysicsCalc.Thermodynamics
                .idealGasDensity(specificGasConstant, absolutePressure, temperature);
            // then
            assertEquals(1.2252, density, DELTA4);
        }

        @Test
        void testThermalEquilibrium() {
            // given
            final double massObj1 = 0.1;
            final short specificHeatCapacityObj1 = 4181;
            final double initialTemperatureObj1 = 13.3;

            final double massObj2 = 0.1;
            final short specificHeatCapacityObj2 = 4181;
            final double initialTemperatureObj2 = 20;
            // when
            final double finalTemperature = PhysicsCalc.Thermodynamics.thermalEquilibrium(massObj1,
                specificHeatCapacityObj1, initialTemperatureObj1, massObj2, specificHeatCapacityObj2,
                initialTemperatureObj2);
            // then
            assertEquals(16.65, finalTemperature, DELTA2);
        }

        @Test
        void testBoltzmannFactor() {
            // given
            final double energy1 = 1.6022e-20;
            final double energy2 = 3.2044e-20;
            final double temperature = 273.15;
            // when
            final double factor = PhysicsCalc.Thermodynamics.boltzmannFactor(energy1, energy2, temperature);
            // then
            assertEquals(70, factor, DELTA1);
        }

        @Test
        void testCompressibility() {
            // given
            final int pressure = 100_000;
            final byte volume = 1;
            final double numberOfMoles = 44.6;
            final short temperature = 293;
            // when
            final double factor = PhysicsCalc.Thermodynamics
                .compressibility(pressure, volume, numberOfMoles, temperature);
            // then
            assertEquals(0.9204, factor, DELTA4);
        }

        @Test
        void testAvgParticleVelocity() {
            // given
            final double carbonAtomMass = MassUnit.amuToKg(12);
            final short temperature = 300;
            // when
            final double velocity = PhysicsCalc.Thermodynamics.avgParticleVelocity(carbonAtomMass, temperature);
            // then
            assertEquals(727.5, velocity, DELTA1);
        }

        @Test
        void testRmsVelocity() {
            // given
            final double temperature = 300.15;
            final double molarMass = 0.032;
            // when
            final double velocity = PhysicsCalc.Thermodynamics.rmsVelocity(temperature, molarMass);
            // then
            assertEquals(483.69, velocity, DELTA2);
        }

        @Test
        void testMedianVelocity() {
            // given
            final double temperature = 300.15;
            final double molarMass = 0.032;
            // when
            final double velocity = PhysicsCalc.Thermodynamics.medianVelocity(temperature, molarMass);
            // then
            assertEquals(394.935, velocity, DELTA3);
        }

        @Test
        void testNewtonsLawOfCooling() {
            // given
            final byte ambientTemperature = 22;
            final byte initialTemperature = 100;
            final double coolingCoefficient = 0.015;
            final byte temperatureAfterSeconds = 120;
            // when
            final double finalTemperature = PhysicsCalc.Thermodynamics.newtonsLawOfCooling(ambientTemperature,
                initialTemperature, coolingCoefficient, temperatureAfterSeconds);
            // then
            assertEquals(34.89, finalTemperature, DELTA2);
        }

        @Test
        void testNewtonsLawOfCoolingCoeff() {
            // given
            final byte area = 20;
            final byte heatCapacity = 19;
            final double heatTransferCoefficient = 0.01425;
            // when
            final double coeff = PhysicsCalc.Thermodynamics
                .newtonsLawOfCoolingCoeff(area, heatCapacity, heatTransferCoefficient);
            // then
            assertEquals(0.015, coeff, DELTA3);
        }

        @Test
        void testBiotNumber() {
            // given
            final byte surfaceArea = 20;
            final byte volume = 4;
            final double heatTransferCoefficient = 1.6;
            final byte thermalConductivity = 3;
            // when
            final double number = PhysicsCalc.Thermodynamics
                .biotNumber(surfaceArea, volume, heatTransferCoefficient, thermalConductivity);
            // then
            assertEquals(0.10667, number, DELTA5);
        }

        @Test
        void testVanDerWaalsEquation() {
            // given
            final byte amountOfSubstance = 1;
            final double volume = 0.00009;
            final double pressure = 21_850_000;
            final double constantA = 0.547;
            final double constantB = 0.00003052;
            // when
            final double temperature = PhysicsCalc.Thermodynamics
                .vanDerWaalsEquation(amountOfSubstance, volume, pressure, constantA, constantB);
            // then
            assertEquals(639.4, temperature, DELTA1);
        }

        @Test
        void testHeatTransferCoeffConductionOnly() {
            // given
            final double area = 1.2;
            final double[][] layers = new double[][]{{0.78, 0.002}, {0.026, 0.005}, {0.78, 0.002}};
            // when
            final double coeff = PhysicsCalc.Thermodynamics.heatTransferCoeffConductionOnly(area, layers);
            // then
            assertEquals(6.0779, coeff, DELTA4);
        }

        @Test
        void testHeatTransferCoeffWithConductionAndConvectionOnBothSides() {
            // given
            final double area = 1.2;
            final double convectionCoeffInner = 10;
            final double convectionCoeffOuter = 40;
            final double[][] layers = new double[][]{{0.78, 0.002}, {0.026, 0.005}, {0.78, 0.002}};
            // when
            final double coeff = PhysicsCalc.Thermodynamics.heatTransferCoeffWithConductionAndConvectionOnBothSides(
                area, convectionCoeffInner, convectionCoeffOuter, layers);
            // then
            assertEquals(3.7217, coeff, DELTA4);
        }

        @Test
        void testNusseltNumber() {
            // given
            final double characteristicLength = 1.2;
            final byte convectionCoeff = 12;
            final byte fluidThermalConductivity = 4;
            // when
            final double number = PhysicsCalc.Thermodynamics
                .nusseltNumber(characteristicLength, convectionCoeff, fluidThermalConductivity);
            // then
            assertEquals(3.6, number, DELTA1);
        }

        @Test
        void testNusseltNumberEmpiricalNaturalConvection() {
            // given
            final byte naturalConvectionCoeff = 3;
            final double rayleighNumber = 1.9;
            final double rayleighlCoeff = 2.1;
            // when
            final double number = PhysicsCalc.Thermodynamics
                .nusseltNumberEmpiricalNaturalConvection(naturalConvectionCoeff, rayleighNumber, rayleighlCoeff);
            // then
            assertEquals(11.55, number, DELTA2);
        }

        @Test
        void testNusseltNumberEmpiricalForcedConvection() {
            // given
            final double forcedConvectionCoeff = 0.037;
            final double reynoldsNumber = 1.9;
            final double reynoldsExponent = 0.8;
            final int prandtlNumber = 438_984;
            final double prandtlExponent = 0.33;
            // when
            final double number = PhysicsCalc.Thermodynamics.nusseltNumberEmpiricalForcedConvection(
                forcedConvectionCoeff, reynoldsNumber, reynoldsExponent, prandtlNumber, prandtlExponent);
            // then
            assertEquals(4.5, number, DELTA1);
        }
    }

    @Nested
    class Atmospheric {
        @Test
        void testAirDensity() {
            // given
            final double airPressure = PressureUnit.hpaToPa(1013.25);
            final double airTemperature = TemperatureUnit.celsiusToKelvin(15);
            // when
            final double airDensity = PhysicsCalc.Atmospheric.airDensity(airPressure, airTemperature);
            // then
            assertEquals(1.225, airDensity, DELTA3);
        }

        @Test
        void testMoistAirDensity() {
            // given
            final double airPressure = PressureUnit.hpaToPa(1013.25);
            final double airTemperature = TemperatureUnit.celsiusToKelvin(15);
            final byte relativeHumidity = 70; // %
            // when
            final double airDensity = PhysicsCalc.Atmospheric
                .moistAirDensity(airPressure, airTemperature, relativeHumidity);
            // then
            assertEquals(1.21955, airDensity, DELTA5);
        }

        @Test
        void testDewPoint() {
            // given
            final double airTemperature = 15;
            final byte relativeHumidity = 70; // %
            // when
            final double dewPoint = PhysicsCalc.Atmospheric.dewPoint(airTemperature, relativeHumidity);
            // then
            assertEquals(9.57, dewPoint, DELTA2);
        }

        @Test
        void testWaterVaporPressure() {
            // given
            final double airTemperature = 15;
            final byte relativeHumidity = 70; // %
            // when
            final double pressure = PhysicsCalc.Atmospheric.waterVaporPressure(airTemperature, relativeHumidity);
            // then
            assertEquals(11.923, pressure, DELTA1);
        }

        @Test
        void testRelativeHumidity() {
            // given
            final byte temperature = 35;
            final double dewPoint = 21.1;
            // when
            final double relativeHumidity = PhysicsCalc.Atmospheric.relativeHumidity(temperature, dewPoint);
            // then
            assertEquals(44.45, relativeHumidity, DELTA2);
        }

        @Test
        void testAbsoluteHumidity() {
            // given
            final byte relativeHumidity = 60; // %
            final double airTemperature = TemperatureUnit.celsiusToKelvin(32);
            final short vaporPressure = 2856;
            // when
            final double absHumidity = PhysicsCalc.Atmospheric
                .absoluteHumidity(relativeHumidity, airTemperature, vaporPressure);
            // then
            assertEquals(0.02028, absHumidity, DELTA2);
        }

        @Test
        void testAirDensityWithAltimeter() {
            // given
            final byte airTemperature = 33;
            final byte relativeHumidity = 40; // %
            final short altimeterSetting = 990;
            final short stationElevation = 1500;
            // when
            final double airDensity = PhysicsCalc.Atmospheric.airDensity(airTemperature, relativeHumidity,
                altimeterSetting, stationElevation);
            // then
            assertEquals(0.9304, airDensity, DELTA3);
        }

        @Test
        void testDensityAltitude() {
            // given
            final double airDensity = 0.9304;
            // when
            final double densityAltitude = PhysicsCalc.Atmospheric.densityAltitude(airDensity);
            // then
            assertEquals(2.7744, densityAltitude, DELTA3);
        }

        @Test
        void testAirPressureAtAltitude() {
            // given
            final byte pressureAtSeaLevel = 1;
            final short summitOfMountEverest = 8949;
            final double temperature = TemperatureUnit.celsiusToKelvin(-30);
            // when
            final double pressure = PhysicsCalc.Atmospheric
                .airPressureAtAltitude(pressureAtSeaLevel, summitOfMountEverest, temperature);
            // then
            assertEquals(0.2844107, pressure, DELTA7);
        }

        @Test
        void testTemperatureAtAltitude() {
            // given
            final byte tempAtSeaLevelCelsius = 15;
            final short altitude = 5000;
            // when
            final double temp = PhysicsCalc.Atmospheric.temperatureAtAltitude(tempAtSeaLevelCelsius, altitude);
            // then
            assertEquals(-17.5, temp, DELTA1);
        }

        @Test
        void testTrueAirSpeedOATCorrection() {
            // given
            final short meanSeaLevelAltitude = 3000;
            final byte indicatedAirSpeed = 24;
            final byte oatEstimationCorrection = 2;
            // when
            final double trueAirSpeed = PhysicsCalc.Atmospheric
                .trueAirSpeedOATCorrection(meanSeaLevelAltitude, indicatedAirSpeed, oatEstimationCorrection);
            // then
            assertEquals(28.724, trueAirSpeed, 3.5);
        }

        @Test
        void testTrueAirSpeedFromWindAndGroundSpeed() {
            // given
            final short groundSpeed = 320;
            final byte windSpeed = 24;
            final double windAngleRad = 0.2618;
            // when
            final double trueAirSpeed = PhysicsCalc.Atmospheric
                .trueAirSpeedFromWindAndGroundSpeed(groundSpeed, windSpeed, windAngleRad);
            // then
            assertEquals(306.44, trueAirSpeed, 10);
        }

        @Test
        void testHeatIndex() {
            // given
            final byte temperature = 95;
            final byte relativeHumidity = 75;
            // when
            final double index = PhysicsCalc.Atmospheric.heatIndex(temperature, relativeHumidity);
            // then
            assertEquals(128, index, DELTA1);
        }

        @Test
        void testAirDynamicViscosity() {
            // given
            final double temperature = 323.15;
            // when
            final double dynamicViscosity = PhysicsCalc.Atmospheric.airDynamicViscosity(temperature);
            // then
            assertEquals(0.0195355, dynamicViscosity, DELTA7);
        }

        @Test
        void testAirKinematicViscosity() {
            // given
            final short pressure = 10_000;
            final double temperature = 323.15;
            // when
            final double kinematicViscosity = PhysicsCalc.Atmospheric.airKinematicViscosity(pressure, temperature);
            // then
            assertEquals(0.18121, kinematicViscosity, DELTA5);
        }

        @Test
        void testCloudBaseAltitude() {
            // given
            final byte temperature = 9;
            final byte dewPoint = 6;
            final byte elevation = 0;
            // when
            final double altitude = PhysicsCalc.Atmospheric.cloudBaseAltitude(temperature, dewPoint, elevation);
            // then
            assertEquals(374.1, altitude, DELTA1);
        }

        @Test
        void testCloudBase() {
            // given
            final byte temperature = 9;
            final double cloudAltitude = 374.1;
            final byte elevation = 0;
            // when
            final double cloudTemperature = PhysicsCalc.Atmospheric.cloudBase(temperature, cloudAltitude, elevation);
            // then
            assertEquals(5.318, cloudTemperature, DELTA3);
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

        @Test
        void testSchwarzschildRadius() {
            // given
            final double mass = MassUnit.sunsToKg(10);
            // when
            final double radius = PhysicsCalc.Astrophysics.schwarzschildRadius(mass);
            // then
            assertEquals(29_541.2, radius, DELTA1);
        }

        @Test
        void testRedshift() {
            // given
            final double emittedLightWavelength = LengthUnit.nanometersToMeters(620);
            final double observedLightWavelength = LengthUnit.nanometersToMeters(610);
            // when
            final double z = PhysicsCalc.Astrophysics.redshift(emittedLightWavelength, observedLightWavelength);
            // then
            assertEquals(-0.01613, z, DELTA5);
        }

        @Test
        void testRedshiftFromFrequency() {
            // given
            final double emittedLight = FrequencyUnit.thzToHz(483.5);
            final double observedLight = FrequencyUnit.thzToHz(491.5);
            // when
            final double z = PhysicsCalc.Astrophysics.redshiftFromFrequency(emittedLight, observedLight);
            // then
            assertEquals(-0.01613, z, DELTA3);
        }

        @Test
        void testParallaxDistance() {
            // given
            final double proximaCentauriAngle = AngleUnit.marcsecToArcsec(771.6);
            // when
            final double distance = PhysicsCalc.Astrophysics.parallaxDistance(proximaCentauriAngle);
            // then
            assertEquals(1.296, distance, DELTA3);
        }

        @Test
        void testRadiationPressureInsideStar() {
            // given
            final double temperature = 78_000;
            // when
            final double pressure = PhysicsCalc.Astrophysics.radiationPressureInsideStar(temperature);
            // then
            assertEquals(9334.8, pressure, DELTA1);
        }

        @Test
        void testRadiationPressureOutsideOpaqueStar() {
            // given
            final double angle = AngleUnit.degToRadians(15);
            final double luminosity = 0.9;
            final double distance = LengthUnit.astronomicalUnitsToMeters(2);
            // when
            final double pressure = PhysicsCalc.Astrophysics
                .radiationPressureOutsideOpaqueStar(angle, luminosity, distance);
            // then
            assertEquals(9.53e-7, pressure, DELTA1);
        }

        @Test
        void testRadiationPressureOutsideReflectiveStar() {
            // given
            final double angle = AngleUnit.degToRadians(15);
            final double luminosity = 0.9;
            final double distance = LengthUnit.astronomicalUnitsToMeters(2);
            // when
            final double pressure = PhysicsCalc.Astrophysics
                .radiationPressureOutsideReflectiveStar(angle, luminosity, distance);
            // then
            assertEquals(0.000001906, pressure, DELTA1);
        }

        @Test
        void testLuminosity() {
            // given Vega star
            final double starRadius = LengthUnit.sunRadiusToMeters(2.5);
            final short temperature = 9602;
            // when
            final double luminosity = PhysicsCalc.Astrophysics.luminosity(starRadius, temperature);
            // then
            assertEquals(47.67, luminosity, DELTA2);
        }

        @Test
        void testLuminosityUsingBoltzmann() {
            // given Vega star
            final double starRadius = LengthUnit.sunRadiusToMeters(2.5);
            final short temperature = 9602;
            // when
            final double luminosity = PhysicsCalc.Astrophysics.luminosityUsingBoltzmann(starRadius, temperature);
            // then
            assertEquals(PowerUnit.solarLuminosityToWatts(47.67), luminosity, 1e26);
        }

        @Test
        void testAbsoluteMagnitude() {
            // given Vega star
            final double luminosity = PowerUnit.solarLuminosityToWatts(47.67);
            // when
            final double absMagnitude = PhysicsCalc.Astrophysics.absoluteMagnitude(luminosity);
            // then
            assertEquals(0.544, absMagnitude, DELTA3);
        }

        @Test
        void testApparentMagnitude() {
            // given Vega star
            final double absMagnitude = 0.544;
            final double distance = 7.68;
            // when
            final double apparentMagnitude = PhysicsCalc.Astrophysics.apparentMagnitude(absMagnitude, distance);
            // then
            assertEquals(-0.02866, apparentMagnitude, DELTA3);
        }
    }

    @Nested
    class Relativity {
        @Test
        void testEmc2() {
            // given A Uranium-235 fission reaction yield
            final double massKg = MassUnit.amuToKg(235);
            // when
            final double energy = PhysicsCalc.Relativity.emc2(massKg);
            // then
            assertEquals(3.50718e-8, energy, DELTA1);
        }

        @Test
        void testElectronSpeed() {
            // given
            final double acceleratingPotential = VoltageUnit.kilovoltsToVolts(11);
            // when
            final double relativisticVelocity = PhysicsCalc.Relativity.electronSpeed(acceleratingPotential);
            final double classicalVelocity = PhysicsCalc.Relativity
                .electronSpeedClassicalVelocity(acceleratingPotential);
            final double velocityDifference = relativisticVelocity - classicalVelocity;
            // then
            assertEquals(61_220_537, relativisticVelocity, 1);
            assertEquals(62_204_534, classicalVelocity, 1);
            assertEquals(-983_997, velocityDifference, 1);
        }

        @Test
        void testRelativisticKE() {
            // given
            final double massKg = 9.11e-31;
            final double velocity = SpeedUnit.lightSpeedToMetersPerSecond(0.99);
            // when
            final double relativisticKE = PhysicsCalc.Relativity.relativisticKE(massKg, velocity);
            // then
            assertEquals(4.985e-13, relativisticKE, DELTA1);
        }

        @Test
        void testTimeDilation() {
            // given
            final byte timeInterval = 3;
            final int velocity = 200_000_000;
            // when
            final double relativeTime = PhysicsCalc.Relativity.timeDilation(timeInterval, velocity);
            // then
            assertEquals(4.027154, relativeTime, DELTA6);
        }

        @Test
        void testGravitationalTimeDilationTimeDiff() {
            // given supermassive black hole
            final double bhMass = MassUnit.sunsToKg(4.3e9);
            final double bhRadius = LengthUnit.kilometersToMeters(18_921_401_000L);
            final short bhTimeInterval = 300; // coordinate time
            // when
            final double blackHoleTimeDiff = PhysicsCalc.Relativity
                .gravitationalTimeDilationTimeDiff(bhMass, bhRadius, bhTimeInterval);
            // then
            assertEquals(223.2986377633, blackHoleTimeDiff, DELTA6);
        }

        @Test
        void testGravitationalTimeDilationInOtherFrameRef() {
            // given the 1st frame of reference (Earth) and the 2nd (black hole)
            final double bhMass = MassUnit.sunsToKg(4.3e9);
            final double bhRadius = LengthUnit.kilometersToMeters(18_921_401_000L);
            final short bhTimeInterval = 300; // coordinate time
            // when
            final double earthFrameRefTime = PhysicsCalc.Relativity
                .gravitationalTimeDilationInOtherFrameRef(bhMass, bhRadius, bhTimeInterval);
            // then
            final double equivalentTimeOnEarth = 523.2986377633;
            assertEquals(equivalentTimeOnEarth, earthFrameRefTime, DELTA6);
        }

        @Test
        void testLengthContraction() {
            // given
            final double properLength = 2;
            final int relativeVelocity = 19_980_000;
            // when
            final double observedLength = PhysicsCalc.Relativity.lengthContraction(properLength, relativeVelocity);
            // then
            assertEquals(1.9956, observedLength, DELTA4);
        }
    }

    @Nested
    class Astronomy {
        @Test
        void testFirstCosmicVelocity() {
            // given
            final double massKg = MassUnit.earthsToKg(1);
            final double radius = LengthUnit.avgEarthRadiiToMeters(1);
            // when
            final double velocity = PhysicsCalc.Astronomy.firstCosmicVelocity(massKg, radius);
            // then
            assertEquals(7909.8, velocity, DELTA1);
        }

        @Test
        void testEscapeVelocity() {
            // given
            final double massKg = MassUnit.earthsToKg(1);
            final double radius = LengthUnit.avgEarthRadiiToMeters(1);
            // when
            final double velocity = PhysicsCalc.Astronomy.escapeVelocity(massKg, radius);
            // then
            assertEquals(11_186.1, velocity, DELTA1);
        }

        @Test
        void testEarthOrbitalSpeed() {
            // given International Space Station (ISS)
            final double height = LengthUnit.kilometersToMeters(400);
            // when
            final double speed = PhysicsCalc.Astronomy.earthOrbitalSpeed(height);
            // then
            assertEquals(7672.6, speed, DELTA1);
        }

        @Test
        void testEarthOrbitalPeriod() {
            // given International Space Station (ISS)
            final double height = LengthUnit.kilometersToMeters(400);
            // when
            final double period = PhysicsCalc.Astronomy.earthOrbitalPeriod(height);
            // then
            assertEquals(PhysicsTimeUnit.hoursToSeconds(1.5403), period, 1);
        }

        @Test
        void testOrbitalVelocityEccentricity() {
            // given Earth
            final byte semiMajorAxis = 1;
            final double semiMinorAxis = 0.99986;
            // when
            final double eccentricity = PhysicsCalc.Astronomy.orbitalVelocityEccentricity(semiMajorAxis, semiMinorAxis);
            // then
            assertEquals(0.016733, eccentricity, DELTA6);
        }

        @Test
        void testApoapsisAndPeriapsisDistance() {
            // given Earth
            final byte semiMajorAxis = 1;
            final double semiMinorAxis = 0.99986;
            // when
            final double[] distances = PhysicsCalc.Astronomy.apoapsisAndPeriapsisDistance(semiMajorAxis, semiMinorAxis);
            // then
            assertArrayEquals(new double[]{1.0167, 0.9833}, distances, DELTA4);
        }

        @Test
        void testOrbitalEnergy() {
            // given Earth
            final double starMass = MassUnit.SOLAR_KG;
            final double satelliteMass = MassUnit.EARTH_KG;
            final double earthSemiMajorAxis = LengthUnit.astronomicalUnitsToMeters(1);
            // when
            final double energy = PhysicsCalc.Astronomy.orbitalEnergy(starMass, satelliteMass, earthSemiMajorAxis);
            // then
            assertEquals(-2.65e33, energy, 1e30);
        }

        @Test
        void testVelocityAtApoapsisAndPeriapsis() {
            // given Earth
            final double distanceAtApoapsis = LengthUnit.astronomicalUnitsToMeters(1.0167);
            final double distanceAtPeriapsis = LengthUnit.astronomicalUnitsToMeters(0.9833);
            // when
            final double[] velocities = PhysicsCalc.Astronomy
                .velocityAtApoapsisAndPeriapsis(distanceAtApoapsis, distanceAtPeriapsis);
            // then
            assertArrayEquals(new double[]{29_295.5, 30_290.5}, velocities, DELTA1);
        }

        @Test
        void testOrbitalVelocity() {
            // given Earth
            final double distanceAtApoapsis = LengthUnit.astronomicalUnitsToMeters(1.0167);
            final double distanceAtPeriapsis = LengthUnit.astronomicalUnitsToMeters(0.9833);
            final double semiMajorAxis = PhysicsCalc.Astronomy.semiMajorAxis(distanceAtApoapsis, distanceAtPeriapsis);
            final double distance = LengthUnit.astronomicalUnitsToMeters(1);
            final double gm = PhysicsCalc.GRAVITATIONAL_CONSTANT * MassUnit.SOLAR_KG;
            // when
            final double velocity = PhysicsCalc.Astronomy.orbitalVelocity(distance, semiMajorAxis, gm);
            // then
            assertEquals(29_788.8, velocity, DELTA1);
        }

        @Test
        void testOrbitalPeriod() {
            // given Earth
            final double distanceAtApoapsis = LengthUnit.astronomicalUnitsToMeters(1.0167);
            final double distanceAtPeriapsis = LengthUnit.astronomicalUnitsToMeters(0.9833);
            final double semiMajorAxis = PhysicsCalc.Astronomy.semiMajorAxis(distanceAtApoapsis, distanceAtPeriapsis);
            final double gm = PhysicsCalc.GRAVITATIONAL_CONSTANT * MassUnit.SOLAR_KG;
            // when
            final double period = PhysicsCalc.Astronomy.orbitalPeriod(semiMajorAxis, gm);
            // then
            assertEquals(31553987, period, 1e3);
        }

        @Test
        void testSatelliteAroundCentralSphereOrbitalPeriod() {
            // given Earth density
            final double density = 5510;
            // when
            final double period = PhysicsCalc.Astronomy.satelliteAroundCentralSphereOrbitalPeriod(density);
            // then
            assertEquals(5062.4, period, DELTA1);
        }

        @Test
        void testBinarySystemOrbitalPeriod() {
            // given
            final double semiMajorAxis = LengthUnit.astronomicalUnitsToMeters(1);
            final double sunMass = MassUnit.SOLAR_KG;
            final double earthMass = MassUnit.EARTH_KG;
            // when
            final double period = PhysicsCalc.Astronomy.binarySystemOrbitalPeriod(semiMajorAxis, sunMass, earthMass);
            // then
            assertEquals(31_554_527, period, 1e3);
        }

        @Test
        void testKepler3RdLawPlanetPeriod() {
            // given Mars semi-major axis
            final double starMass = MassUnit.SOLAR_KG;
            final double semiMajorAxis = LengthUnit.astronomicalUnitsToMeters(1.524);
            // when
            final double planetPeriod = PhysicsCalc.Astronomy.kepler3rdLawPlanetPeriod(starMass, semiMajorAxis);
            // then
            assertEquals(1.882, PhysicsTimeUnit.secondsToYears(planetPeriod), DELTA3);
        }

        @Test
        void testKepler3RdLawStarMass() {
            // given Mars data
            final double semiMajorAxis = LengthUnit.astronomicalUnitsToMeters(1.524);
            final double planetPeriod = PhysicsTimeUnit.yearsToSeconds(1.881);
            // when
            final double mass = PhysicsCalc.Astronomy.kepler3rdLawStarMass(semiMajorAxis, planetPeriod);
            // then
            assertEquals(MassUnit.SOLAR_KG, mass, 1e28); // ≈0.13% diff
        }

        @Test
        void testSynodicPeriod() {
            // given
            final double refPlanetSiderealPeriod = 1; // Earth
            final double planetSiderealPeriod = 11.9; // Jupyter
            // when
            final double period = PhysicsCalc.Astronomy.synodicPeriod(refPlanetSiderealPeriod, planetSiderealPeriod);
            // then
            assertEquals(1.092, period, DELTA3);
        }

        @Test
        void testExoplanetRadialVelocityDetection() {
            // given Proxima Centauri b
            final double starOrbitalSpeed = 1.511;
            final double readshift = PhysicsCalc.Astrophysics.redshift(starOrbitalSpeed);
            final double originalWavelength = 6.1e-7;
            // when
            final double readshiftedWavelength = PhysicsCalc.Astronomy
                .exoplanetRadialVelocityDetection(readshift, originalWavelength);
            // then
            assertEquals(0.000001, readshiftedWavelength, DELTA6);
        }

        @Test
        void testExoplanetTransitDetection() {
            // given Proxima Centauri b
            final double starArea = 36154522229066854D;
            final double planetArea = 183623209887524D;
            // when
            final double starAppearance = PhysicsCalc.Astronomy.exoplanetTransitDetection(starArea, planetArea);
            // then
            assertEquals(0.5079, starAppearance, DELTA4);
        }

        @Test
        void testExoplanetAstrometryDetection() {
            // given Proxima Centauri b
            final byte amplitudeFactor = 2;
            final double starMass = 0.1221;
            final int starDistance = 265_611;
            final double planetMass = 0.000003903;
            final double semiMajorAxis = 0.0485;
            // when
            final double angularDisplacement = PhysicsCalc.Astronomy
                .exoplanetAstrometryDetection(amplitudeFactor, starMass, starDistance, planetMass, semiMajorAxis);
            // then
            assertEquals(0.000002408, angularDisplacement, DELTA9);
        }

        @Test
        void testIdealRocketEquation() {
            // given
            final double effectiveExhaustVelocity = 450.5;
            final short initialMass = 5000;
            final short finalMass = 4500;
            // when
            final double velocityChange = PhysicsCalc.Astronomy
                .idealRocketEquation(effectiveExhaustVelocity, initialMass, finalMass);
            // then
            assertEquals(47.465, velocityChange, DELTA3);
        }

        @Test
        void testRocketThrust() {
            // given Falcon 9
            final short effectiveExhaustVelocity = 3000;
            final double flowAreaAtNozzle = 1.227185;
            // totalFuelMass = 44320; timeElapsed = 162;
            final double massLossRate = 273.6; // ≈ totalFuelMass / timeElapsed
            final double ambientPressure = 101_325;
            final double pressureAtNozzle = 84_424;
            // when
            final double thrust = PhysicsCalc.Astronomy.rocketThrust(effectiveExhaustVelocity, flowAreaAtNozzle,
                massLossRate, ambientPressure, pressureAtNozzle);
            // then
            assertEquals(800_059.3, thrust, DELTA1);
        }

        @Test
        void testSpecificImpulse() {
            // given Kouznetsov NK-33
            final double exhaustVelocity = 3250;
            // when
            final double impulse = PhysicsCalc.Astronomy.specificImpulse(exhaustVelocity);
            // then
            assertEquals(331.4, impulse, DELTA1);
        }

        @Test
        void testTsfc() {
            // given Kouznetsov NK-33
            final int massFlowRate = 52_194;
            final int thrust = 169_632;
            // when
            final double tsfc = PhysicsCalc.Astronomy.tsfc(massFlowRate, thrust);
            // then
            assertEquals(0.3077, tsfc, DELTA4);
        }

        @Test
        void testSpecificThrust() {
            // given Kouznetsov NK-33
            final int thrust = 169_632;
            final int massFlowRate = 52_194;
            // when
            final double tsfcInverse = PhysicsCalc.Astronomy.specificThrust(thrust, massFlowRate);
            // then
            assertEquals(3.25, tsfcInverse, DELTA2);
        }

        @Test
        void testThrustWeightRatio() {
            // given Boeing 747 (Aircraft)
            final int thrust = 1_184_000;
            final double weight = PhysicsCalc.Statics.massToWeight(447_700);
            // when
            final double ratio = PhysicsCalc.Astronomy.thrustWeightRatio(thrust, weight);
            // then
            assertEquals(0.2697, ratio, DELTA4);
        }
    }
}
