package org.example.assistantonsbservlet.physics;

import java.util.Arrays;

public final class PhysicsCalculator {
    private PhysicsCalculator() {
    }

    public static final class Kinematics {
        private Kinematics() {
        }

        /**
         * @return velocity = distance / time
         */
        static double velocity(double distance, long time) {
            checkTimeInput(time);
            return distance / time;
        }

        /**
         * @return u = v − a * t
         */
        static double initialVelocity(double finalVelocity, double acceleration, long time) {
            return finalVelocity - acceleration * time;
        }

        /**
         * @return u = 2 * (s / t) - v
         */
        static double initialVelocityFromDisplacement(double displacement, double finalVelocity, long time) {
            checkTimeInput(time);
            return 2 * (displacement / time) - finalVelocity;
        }

        /**
         * @return u = √(v² − 2as)
         */
        static double initialVelocityFromDisplacement(
            double displacement, double finalVelocity, double acceleration) {
            final double value = finalVelocity * finalVelocity - 2 * acceleration * displacement;
            if (value < 0) {
                throw new IllegalArgumentException("Invalid input: Resulting value inside square root is negative.");
            }
            return Math.sqrt(value);
        }

        /**
         * @return u = (s / t) − (at / 2)
         */
        static double initialVelocityFromDisplacementAndAcceleration(
            double displacement, double acceleration, long time) {
            checkTimeInput(time);
            return (displacement / time) - (acceleration * time / 2.0);
        }

        private static void checkTimeInput(long time) {
            if (time == 0) {
                throw new IllegalArgumentException("Time cannot be zero.");
            }
        }

        /**
         * @return v = u + at
         */
        static double finalVelocity(double initialVelocity, double acceleration, long time) {
            return initialVelocity + acceleration * time;
        }

        /**
         * @return v = 2 * (s / t) − u
         */
        static double finalVelocityFromDisplacement(double displacement, double initialVelocity, long time) {
            checkTimeInput(time);
            return 2 * (displacement / time) - initialVelocity;
        }

        /**
         * @return v = (s / t) + (at / 2)
         */
        static double finalVelocityFromDisplacementAndAcceleration(
            double displacement, double acceleration, long time) {
            checkTimeInput(time);
            return (displacement / time) + (acceleration * time / 2.0);
        }

        /**
         * Calculate the actual arrow speed (v) in ft/s.
         *
         * @param ibo              Arrow speed according to the IBO specification in ft/s.
         * @param drawLengthOfBow  Draw length in inches.
         * @param additionalWeight Additional weight on the bowstring in grains.
         * @param arrowWeight      Arrow weight in grains.
         * @param drawWeight       Draw weight in pounds.
         * @return v = IBO + (L − 30) * 10 − W/3 + min(0, −(A−5D)/3)
         */
        static double arrowSpeed(
            double ibo, double drawLengthOfBow, double drawWeight, double additionalWeight, double arrowWeight) {
            // Calculate the actual arrow speed using the formula
            return ibo + (drawLengthOfBow - 30) * 10 - additionalWeight / 3.0
                + Math.min(0, -(arrowWeight - 5 * drawWeight) / 3.0);
        }

        /**
         * @return B = m / (C × A). The unit is lb/in²
         */
        static double ballisticCoefficient(
            double projectileMass, double dragCoefficient, double crossSectionArea) {
            if (dragCoefficient == 0 || crossSectionArea == 0) {
                throw new IllegalArgumentException("Drag Coefficient and Cross Section Area cannot be zero.");
            }
            return projectileMass / (dragCoefficient * crossSectionArea);
        }

        /**
         * @return k = ρ × A × C / 2
         */
        static double airResistanceCoefficient(
            double mediumDensity, double crossSectionalArea, double dimensionlessDragCoef) {
            return mediumDensity * crossSectionalArea * dimensionlessDragCoef / 2.0;
        }

        /**
         * @return F_d = −b * v²
         */
        static double dragForce(double airResistanceCoeff, double carVelocity) {
            return -airResistanceCoeff * carVelocity * carVelocity;
        }

        /**
         * @return p = mv. The unit is kg*m/s
         */
        static double momentum(double mass, double velocity) {
            return mass * velocity;
        }

        /**
         * @return v = p/m. The unit is kg*m/s
         */
        static double velocityOfDesiredMomentum(double momentum, double mass) {
            return momentum / mass;
        }

        /**
         * @return ||p|| = m * √(vₓ² + vᵧ² + v_z²) ⇒ ||p|| = m * ||v||
         */
        static double velocityMagnitude(double[] velocityVector) {
            return Math.sqrt(Arrays.stream(velocityVector).map(m -> m * m).sum());
        }

        /**
         * @return ||p|| = m * ||v||
         */
        static double momentumMagnitude(double mass, double[] velocityVector) {
            return mass * velocityMagnitude(velocityVector);
        }

        /**
         * Calculate the object2 final velocity in m/s when the collision type is unknown or partially elastic.
         * @return m₁u₁ + m₂u₂ = m₁v₁ + m₂v₂
         */
        static double conservationOfMomentum(
            double obj1Mass, double obj1InitialVelocity, double obj1FinalVelocity,
            double obj2Mass, double obj2InitialVelocity) {
            final double initialMomentum = obj1Mass * obj1InitialVelocity + obj2Mass * obj2InitialVelocity;
            final double obj1FinalMomentum = obj1Mass * obj1FinalVelocity;
            final double obj2FinalMomentum = initialMomentum - obj1FinalMomentum;
            return obj2FinalMomentum / obj2Mass;
        }

        /**
         * Calculate the conservation of momentum for perfectly elastic/inelastic collision type.
         * For perfectly elastic collision:
         * v₁ = ((m₁ - m₂) / m₁ + m₂) * u₁ + ((2m₂) / m₁ + m₂) * u₂
         * v₂ = ((2m₂) / m₁ + m₂) * u₁ + ((m₂ - m₁) / m₁ + m₂) * u₂
         * For perfectly inelastic collision:
         * m₁u₁ + m₂u₂ = (m₁ + m₂)V
         * V = (m₁u₁ + m₂u₂) / (m₁ + m₂)
         * @return final velocity vector in m/s.
         */
        static double[] conservationOfMomentum(
            double obj1Mass, double obj1InitialVelocity,
            double obj2Mass, double obj2InitialVelocity, CollisionType type) {
            switch (type) {
                case PERFECTLY_ELASTIC -> {
                    final double massSum = obj1Mass + obj2Mass;
                    final double obj1FinalVelocity = ((obj1Mass - obj2Mass) / massSum) * obj1InitialVelocity
                        + ((2 * obj2Mass) / massSum) * obj2InitialVelocity;
                    final double obj2FinalVelocity = ((2 * obj1Mass) / massSum) * obj1InitialVelocity
                        + ((obj2Mass - obj1Mass) / massSum) * obj2InitialVelocity;
                    return new double[] {obj1FinalVelocity, obj2FinalVelocity};
                }
                case PERFECTLY_INELASTIC -> {
                    final double finalVelocity = (obj1Mass * obj1InitialVelocity + obj2Mass * obj2InitialVelocity)
                        / (obj1Mass + obj2Mass);
                    return new double[]{finalVelocity, finalVelocity};
                }
                default -> throw new IllegalArgumentException();
            }
        }

        /**
         * @return d = v * t
         */
        static double displacement(double averageVelocity, long totalTime) {
            return averageVelocity * totalTime;
        }

        /**
         * @return d = (1 / 2) * a * t² + v₀ * t
         */
        static double displacement(double acceleration, double initialVelocity, long totalTime) {
            return 0.5 * acceleration * totalTime * totalTime + initialVelocity * totalTime;
        }

        /**
         * @return v = v₀ + g * t
         */
        static double freeFall(double initialVelocity, long fallTime) {
            return initialVelocity + Constants.GRAVITATIONAL_ACCELERATION_IN_M_PER_S2 * fallTime;
        }

        /**
         * @return s = (1 / 2) * g * t²
         */
        static double freeFallDistance(long fallTime) {
            return 0.5 * Constants.GRAVITATIONAL_ACCELERATION_IN_M_PER_S2 * fallTime * fallTime;
        }

        /**
         * @return F = μ * N
         */
        static double friction(double frictionCoefficient, double normalForce) {
            return frictionCoefficient * normalForce;
        }

        /**
         * @return v_g = √(v_a² + v_w² - (2 * v_a * v_w * cos(δ) - w + α))
         */
        static double groundSpeed(
            double trueAirspeed, double windSpeed, double course,
            double windDirection, double windCorrectionAngle) {
            return Math.sqrt(
                trueAirspeed * trueAirspeed + windSpeed * windSpeed
                    - (2 * trueAirspeed * windSpeed * Math.cos(course) - windDirection + windCorrectionAngle)
            );
        }

        /**
         * @return J = Δp = p₂ - p₁
         */
        static double impulse(double initialMomentum, double finalMomentum) {
            return initialMomentum - finalMomentum;
        }

        /**
         * @return J = Δp = p₂ − p₁ = m * V₂ - m * V₁ = m * ΔV
         */
        static double impulse(double mass, double initialVelocity, double finalVelocity) {
            return mass * initialVelocity - mass * finalVelocity;
        }

        /**
         * @return a = √(a₁² + a₂² + a₃²)
         */
        static double magnitudeOfAcceleration(double[] accelerationComponents) {
            return Math.sqrt(Arrays.stream(accelerationComponents).sum());
        }

        /**
         * @return |a| = |v₁ - v₂| / Δt
         */
        static double[] magnitudeOfAcceleration(
            long timeDifference,
            double initialVelocityX, double initialVelocityY,
            double finalVelocityX, double finalVelocityY) {
            if (timeDifference == 0) {
                throw new IllegalArgumentException("Time difference cannot be zero.");
            }
            final double vDiffX = finalVelocityX - initialVelocityX;
            final double vDiffY = finalVelocityY - initialVelocityY;
            return new double[]{vDiffX / timeDifference, vDiffY / timeDifference};
        }

        /**
         * @return J = π/2 * R⁴
         */
        static double polarMomentOfSolidCircle(double radius) {
            return Math.PI / 2 * Math.pow(radius, 4);
        }

        /**
         * @return J = π/32 * D⁴
         */
        static double polarMomentOfSolidCircleFromDiameter(double diameter) {
            return Math.PI / 32 * Math.pow(diameter, 4);
        }

        /**
         * @return J = π/2 * (R⁴ - Rᵢ⁴)
         */
        static double polarMomentOfHollowCylinder(double innerRadius, double outerRadius) {
            return Math.PI / 2 * (Math.pow(outerRadius, 4) - Math.pow(innerRadius, 4));
        }

        /**
         * @return J = π/32 * (D⁴ - d⁴)
         */
        static double polarMomentOfHollowCylinderFromDiameters(double innerDiameter, double outerDiameter) {
            return Math.PI / 32 * (Math.pow(outerDiameter, 4) - Math.pow(innerDiameter, 4));
        }

        static double[] calculateQuarterMile(double etConstant, double fsConstant, double weight, double power) {
            final double elapsedTime = etConstant * Math.pow((weight / power), (double) 1 / 3);
            final double finalSpeed = fsConstant * Math.pow((power / weight), (double) 1 / 3);
            return new double[]{elapsedTime, finalSpeed};
        }

        /**
         * ET = 6.290 * (Weight / Power)^⅓
         * <p/>
         * Final speed = 224 * (Power / Weight)^⅓
         */
        static double[] huntingtonQuarterMile(double weight, double power) {
            return calculateQuarterMile(6.290, 224, weight, power);
        }

        /**
         * ET = 6.269 * (Weight / Power)^⅓
         * <p/>
         * Final speed = 230 * (Power / Weight)^⅓
         */
        static double[] foxQuarterMile(double weight, double power) {
            return calculateQuarterMile(6.269, 230, weight, power);
        }

        /**
         * ET = 5.825 * (Weight / Power)^⅓
         * <p/>
         * Final speed = 234 * (Power / Weight)^⅓
         */
        static double[] haleQuarterMile(double weight, double power) {
            return calculateQuarterMile(5.825, 234, weight, power);
        }
    }
}
