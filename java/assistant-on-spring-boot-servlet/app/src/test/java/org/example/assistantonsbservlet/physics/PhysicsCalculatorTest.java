package org.example.assistantonsbservlet.physics;

import org.example.assistantonsbservlet.math.ConversionCalculator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PhysicsCalculatorTest {
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
            final double result = PhysicsCalculator.Kinematics.arrowSpeed(ibo, drawLengthOfBow, peakDrawWeight,
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
            final double result = PhysicsCalculator.Kinematics.ballisticCoefficient(
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
            final double result = PhysicsCalculator.Kinematics.momentum(mass, velocity);
            // then
            assertEquals(130, result, 0.1);
        }

        @Test
        void calculateVelocityMagnitude2d() {
            // given
            final double velocityXDirection = 2;
            final double velocityYDirection = 3;
            final double[] velocity2d = new double[] {velocityXDirection, velocityYDirection};
            // when
            final double magnitude = PhysicsCalculator.Kinematics.velocityMagnitude(velocity2d);
            // then
            assertEquals(3.6056, magnitude, 0.1);
        }

        @Test
        void calculateMomentumMagnitude2d() {
            // given
            final double mass = 65; // kg
            final double velocityXDirection = 2;
            final double velocityYDirection = 3;
            final double[] velocity2d = new double[] {velocityXDirection, velocityYDirection};
            // when
            final double magnitude = PhysicsCalculator.Kinematics.momentumMagnitude(mass, velocity2d);
            // then
            assertEquals(234.364, magnitude, 0.1);
        }

        @Test
        void calculateVelocityMagnitude3d() {
            // given
            final double velocityXDirection = 2;
            final double velocityYDirection = 3;
            final double velocityZDirection = 1;
            final double[] velocity3d = new double[] {velocityXDirection, velocityYDirection, velocityZDirection};
            // when
            final double magnitude = PhysicsCalculator.Kinematics.velocityMagnitude(velocity3d);
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
            final double[] velocity3d = new double[] {velocityXDirection, velocityYDirection, velocityZDirection};
            // when
            final double magnitude = PhysicsCalculator.Kinematics.momentumMagnitude(mass, velocity3d);
            // then
            assertEquals(243.23, magnitude, 0.1);
        }

        @Test
        void calculateVelocityOfDesiredMomentum() {
            // given
            final double momentum = 195;
            final double mass = 65; // kg
            // when
            final double velocity = PhysicsCalculator.Kinematics.velocityOfDesiredMomentum(momentum, mass);
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
            final double obj2FinalVelocity = PhysicsCalculator.Kinematics.conservationOfMomentum(
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
            final double[] finalVelocities = PhysicsCalculator.Kinematics.conservationOfMomentum(
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
            final double[] finalVelocities = PhysicsCalculator.Kinematics.conservationOfMomentum(
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
            final double displacement = PhysicsCalculator.Kinematics.displacement(averageVelocity, time);
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
            final double displacementInMeters = PhysicsCalculator.Kinematics.displacement(
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
            final double displacementInMeters = PhysicsCalculator.Kinematics.displacementOfVelocities(
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
            final double velocity = PhysicsCalculator.Kinematics.freeFallVelocity(initialVelocity, timeInSec);
            // then
            assertEquals(78.45, velocity, 0.01); // m/s
        }

        @Test
        void calculateFreeFallDistance() {
            // given
            final int timeInSec = 8;
            // when
            final double distanceInM = PhysicsCalculator.Kinematics.freeFallDistance(timeInSec);
            // then
            assertEquals(313.81, distanceInM, 0.01);
        }

        @Test
        void calculateFreeFallDistanceWithAirResistance() {
            // given
            final double airResistanceCoef = 0.24;
            final double terminalVelocity = 55.4;
            // when
            final double dragForce = PhysicsCalculator.Kinematics.freeFallDistanceWithAirResistance(
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
            final double terminalVelocity = PhysicsCalculator.Kinematics.terminalVelocity(
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
            final double dragCoef = PhysicsCalculator.DragCoefficient.BASEBALL;
            // when
            final double terminalVelocity = PhysicsCalculator.Kinematics.terminalVelocity(
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
            final double dragCoef = PhysicsCalculator.DragCoefficient.GOLF_BALL;
            // when
            final double terminalVelocity = PhysicsCalculator.Kinematics.terminalVelocity(
                massInKg, gravitationalAcceleration, densityOfFluid, dragCoef, crossSectionalArea);
            // then
            assertEquals(32.734, terminalVelocity, 0.001);
        }

        @Test
        void calculateWeightOfFreeFallingBody() {
            // given
            final double massInKg = 60;
            // when
            final double weight = PhysicsCalculator.Kinematics.weightOfFreeFallingBody(massInKg);
            // then
            assertEquals(588.399, weight, 0.01);
        }

        @Test
        void calculateFriction() {
            // given
            final double frictionCoef = 0.13;
            final double normalForceInNewtons = 250;
            // when
            final double friction = PhysicsCalculator.Kinematics.friction(frictionCoef, normalForceInNewtons);
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
            final double friction = PhysicsCalculator.Kinematics.energyLostToFriction(
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
            final double windCorrectionAngle = PhysicsCalculator.Kinematics.windCorrectionAngle(
                trueAirspeedInKnots, windSpeedInKnots, courseInRadians, windDirectionInRadians);
            // when
            final double headingInRadians = PhysicsCalculator.Kinematics.aircraftHeading(
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
            final double acceleration = PhysicsCalculator.Dynamics.acceleration(
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
            final double acceleration = PhysicsCalculator.Dynamics.acceleration(massInKg, netForceInNewtons);
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
            final double acceleration = PhysicsCalculator.Dynamics.accelerationWithDeltaDistance(
                initialVelocity, distanceInM, timeInSec);
            // then
            assertEquals(-22.22, acceleration, 0.01);
        }
    }
}
