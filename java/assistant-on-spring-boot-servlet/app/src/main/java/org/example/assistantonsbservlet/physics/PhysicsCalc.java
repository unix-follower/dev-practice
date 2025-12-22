package org.example.assistantonsbservlet.physics;

import org.example.assistantonsbservlet.math.Constants;
import org.example.assistantonsbservlet.math.MathCalc;
import org.example.assistantonsbservlet.math.MathCalc.Algebra;
import org.example.assistantonsbservlet.math.MathCalc.Trigonometry;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.DoubleUnaryOperator;

public final class PhysicsCalc {
    public static final double GRAVITATIONAL_ACCELERATION_IN_M_PER_S2 = 9.80665;
    public static final double AVOGADRO_NUMBER = 6.02214082e23;
    /**
     * ~3.00 * 10⁸ m/s. Also, 300 * 10^⁶ m/s
     */
    public static final double SPEED_OF_LIGHT_IN_M_PER_SEC = 2.99792458e8;
    public static final double ELECTRON_CHARGE_IN_COULOMBS = 1.6021766208e-19;
    public static final double BLINK_OF_AN_EYE_SEC = 0.350; // 350e-3
    /**
     * (6.02214 * 10^23 electrons/mole) / (6.24151 * 10^18 electrons/coulomb) = 96485 coulombs/mole
     * {@link #AVOGADRO_NUMBER} {@link #ONE_COULOMB}
     */
    public static final double FARADAY_CONSTANT = 96_485;
    /**
     * The number of electron charges
     */
    public static final double ONE_COULOMB = 6.241509343e18;
    public static final short HORSEPOWER = 746;
    public static final double BOLTZMANN_CONSTANT = 1.380649e-23; // J/K
    public static final double REF_VOLTAGE_FOR_0_DBU = 0.77459667;

    private PhysicsCalc() {
    }

    /**
     * @return v = 1/C ∫ᵀ_(-∞) i * dt
     */
    public static double integrateCapacitorVoltage(
        DoubleUnaryOperator voltageFn, double current, double capacitance, int numberOfIntervals) {
        double sum = 0;
        for (int i = 0; i < numberOfIntervals; i++) {
            sum += current * voltageFn.applyAsDouble(current + i);
        }
        return 1 / capacitance * sum;
    }

    /**
     * @return v = 1/C ∫ᵀ_(-∞) i * dt + v₀
     */
    public static double integrateCapacitorVoltage(DoubleUnaryOperator voltageFn, double current, double capacitance,
                                                   int numberOfIntervals, double knownVoltageAtPoint) {
        double sum = 0;
        for (int i = 0; i < numberOfIntervals; i++) {
            sum += current * voltageFn.applyAsDouble(current + i) + knownVoltageAtPoint;
        }
        return 1 / capacitance * sum;
    }

    /**
     * U = ∫ p*dt = ∫ vC(dv/dt)dt = C ∫ v * dv
     *
     * @return U = 1/2 * Cv² assuming 0V at the beginning
     */
    public static double capacitorEnergy(double capacitance, double voltage) {
        return MathCalc.ONE_HALF * capacitance * (voltage * voltage);
    }

    /**
     * U = ∫ p*dt = ∫ iL(di/dt)dt = L ∫ i * di
     *
     * @return U = 1/2 * Li²
     */
    public static double inductorEnergy(double inductance, double current) {
        return MathCalc.ONE_HALF * inductance * (current * current);
    }

    /**
     * The constant of proportionality L is the called the inductance.
     *
     * @return v = L * di/dt. The units are henry
     */
    public static double inductorVoltage(double inductance, double changeInCurrent, double changeInTime) {
        return inductance * (changeInCurrent / changeInTime);
    }

    /**
     * @return i = 1/L ∫ᵀ_(-∞) v * dt
     */
    public static double integrateInductorCurrent(
        DoubleUnaryOperator currentFn, double voltage, double inductance, int numberOfIntervals) {
        double sum = 0;
        for (int i = 0; i < numberOfIntervals; i++) {
            sum += voltage * currentFn.applyAsDouble(voltage + i);
        }
        return 1 / inductance * sum;
    }

    public static final class DragCoefficient {
        public static final double SPHERE = 0.47;
        public static final double HEMISPHERE = 0.42;
        public static final double CONE = 0.5;
        public static final double CUBE = 1.05;
        public static final double ANGLED_CUBE = 0.8;
        public static final double LONG_CYLINDER = 0.82;
        public static final double SHORT_CYLINDER = 1.15;
        public static final double STREAMLINED_BODY = 0.04;
        public static final double STREAMLINED_HALF_BODY = 0.09;
        public static final double GOLF_BALL = 0.389;
        public static final double BASEBALL = 0.3275;

        private DragCoefficient() {
        }
    }

    public static final class Kinematics {
        private Kinematics() {
        }

        /**
         * @return velocity = distance / time
         */
        public static double velocity(double distance, long time) {
            checkTimeInput(time);
            return distance / time;
        }

        /**
         * @return u = v − a * t
         */
        public static double initialVelocity(double finalVelocity, double acceleration, long time) {
            return finalVelocity - acceleration * time;
        }

        /**
         * @return u = 2 * (s / t) - v
         */
        public static double initialVelocityFromDisplacement(double displacement, double finalVelocity, long time) {
            checkTimeInput(time);
            return 2 * (displacement / time) - finalVelocity;
        }

        /**
         * @return u = √(v² − 2as)
         */
        public static double initialVelocityFromDisplacement(
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
        public static double initialVelocityFromDisplacementAndAcceleration(
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
        public static double finalVelocity(double initialVelocity, double acceleration, long time) {
            return initialVelocity + acceleration * time;
        }

        /**
         * @return v = 2 * (s / t) − u
         */
        public static double finalVelocityFromDisplacement(double displacement, double initialVelocity, long time) {
            checkTimeInput(time);
            return 2 * (displacement / time) - initialVelocity;
        }

        /**
         * @return v_t = √((2mg)/(pC_dA)). The units are m/s
         */
        public static double terminalVelocity(
            double massInKg, double gravitationalAcceleration, double fluidDensity,
            double dragCoef, double crossSectionalArea) {
            return Math.sqrt(
                (2 * massInKg * gravitationalAcceleration)
                    / (fluidDensity * crossSectionalArea * dragCoef)
            );
        }

        /**
         * @return t = (v_t/g) * tanh^(-1)(h/v_t)
         */
        public static double timeOfFall(double terminalVelocity, double gravitationalAcceleration, double altitudeInM) {
            if (terminalVelocity <= 0) {
                throw new IllegalArgumentException("Terminal velocity must be greater than 0");
            }

            final double altitudeRatio = altitudeInM / terminalVelocity;

            if (altitudeRatio >= 1 || altitudeRatio <= -1) {
                throw new IllegalArgumentException(
                    "Altitude and terminal velocity result in invalid range for tanh^-1 calculation");
            }

            final double atanh = 0.5 * Math.log((1 + altitudeRatio) / (1 - altitudeRatio));
            return (terminalVelocity / gravitationalAcceleration) * atanh;
        }

        /**
         * @return v(t) = v_t(1 - e^(-(gt)/v_t)
         */
        public static double instantaneousSpeed(
            double terminalVelocity, double gravitationalAcceleration, double timeElapsedInSec) {
            return terminalVelocity * (1 - Math.exp(
                -(gravitationalAcceleration * timeElapsedInSec) / terminalVelocity)
            );
        }

        /**
         * @return v = (s / t) + (at / 2)
         */
        public static double finalVelocityFromDisplacementAndAcceleration(
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
        public static double arrowSpeed(
            double ibo, double drawLengthOfBow, double drawWeight, double additionalWeight, double arrowWeight) {
            // Calculate the actual arrow speed using the formula
            return ibo + (drawLengthOfBow - 30) * 10 - additionalWeight / 3.0
                + Math.min(0, -(arrowWeight - 5 * drawWeight) / 3.0);
        }

        /**
         * @return B = m / (C * A). The units are lb/in²
         */
        public static double ballisticCoefficient(
            double projectileMass, double dragCoefficient, double crossSectionArea) {
            if (dragCoefficient == 0 || crossSectionArea == 0) {
                throw new IllegalArgumentException("Drag Coefficient and Cross Section Area cannot be zero.");
            }
            return projectileMass / (dragCoefficient * crossSectionArea);
        }

        /**
         * @return k = ρ * A * C / 2
         */
        public static double airResistanceCoefficient(
            double mediumDensity, double crossSectionalArea, double dimensionlessDragCoef) {
            return mediumDensity * crossSectionalArea * dimensionlessDragCoef / 2.0;
        }

        /**
         * F_d = (1/2) * ρ * v² * A * C
         * where:
         * ρ: Air density
         * v: Instantaneous velocity
         * A: Cross-sectional area
         * C: Drag coefficient
         */
        public static double dragForce(
            double airDensity, double instantaneousSpeed, double crossSectionalArea, double dragCoef) {
            return 0.5 * airDensity * Math.pow(instantaneousSpeed, 2) * crossSectionalArea * dragCoef;
        }

        /**
         * @return p = mv. The units are kg*m/s
         */
        public static double momentum(double mass, double velocity) {
            return mass * velocity;
        }

        /**
         * @return v = p/m. The units are kg*m/s
         */
        public static double velocityOfDesiredMomentum(double momentum, double mass) {
            return momentum / mass;
        }

        /**
         * @return ||p|| = m * √(vₓ² + vᵧ² + v_z²) ⇒ ||p|| = m * ||v||
         */
        public static double velocityMagnitude(double[] velocityVector) {
            return MathCalc.LinearAlgebra.vectorMagnitude(velocityVector);
        }

        /**
         * @return ||p|| = m * ||v||
         */
        public static double momentumMagnitude(double mass, double[] velocityVector) {
            return mass * velocityMagnitude(velocityVector);
        }

        /**
         * Calculate the object2 final velocity in m/s when the collision type is unknown or partially elastic.
         *
         * @return m₁u₁ + m₂u₂ = m₁v₁ + m₂v₂
         */
        public static double conservationOfMomentum(
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
         *
         * @return final velocity vector in m/s.
         */
        public static double[] conservationOfMomentum(
            double obj1Mass, double obj1InitialVelocity,
            double obj2Mass, double obj2InitialVelocity, CollisionType type) {
            switch (type) {
                case PERFECTLY_ELASTIC -> {
                    final double massSum = obj1Mass + obj2Mass;
                    final double obj1FinalVelocity = ((obj1Mass - obj2Mass) / massSum) * obj1InitialVelocity
                        + ((2 * obj2Mass) / massSum) * obj2InitialVelocity;
                    final double obj2FinalVelocity = ((2 * obj1Mass) / massSum) * obj1InitialVelocity
                        + ((obj2Mass - obj1Mass) / massSum) * obj2InitialVelocity;
                    return new double[]{obj1FinalVelocity, obj2FinalVelocity};
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
         * Calculate displacement using constant velocity
         *
         * @return d = v * t. The units are meters
         */
        public static double displacement(double averageVelocity, long timeInSeconds) {
            return averageVelocity * timeInSeconds;
        }

        /**
         * @return d = (1 / 2) * a * t² + v₀ * t. The units are meters
         */
        public static double displacement(double acceleration, double initialVelocity, long timeInSeconds) {
            return 0.5 * acceleration * timeInSeconds * timeInSeconds + initialVelocity * timeInSeconds;
        }

        /**
         * @return d = (1 / 2) * (v₁ + v₀) * t. The units are meters
         */
        public static double displacementOfVelocities(
            double initialVelocity, double finalVelocity, long timeInSeconds) {
            return 0.5 * (finalVelocity + initialVelocity) * timeInSeconds;
        }

        /**
         * @return v = v₀ + g * t. The units are m/s
         */
        public static double freeFallVelocity(double initialVelocity, long fallTime) {
            return initialVelocity + GRAVITATIONAL_ACCELERATION_IN_M_PER_S2 * fallTime;
        }

        /**
         * @return s = (1 / 2) * g * t². The units are meters
         */
        public static double freeFallDistance(long fallTimeInSec) {
            return 0.5 * GRAVITATIONAL_ACCELERATION_IN_M_PER_S2 * fallTimeInSec * fallTimeInSec;
        }

        /**
         * F = k * v²
         * where v is the instantaneous speed, and k is the air resistance coefficient, measured in kilograms per meter.
         * The units are Newtons
         */
        public static double freeFallDistanceWithAirResistance(double airResistanceCoef, double terminalVelocity) {
            return airResistanceCoef * terminalVelocity * terminalVelocity;
        }

        /**
         * @return W = mg. The units are Newtons
         */
        public static double weightOfFreeFallingBody(double mass) {
            return mass * GRAVITATIONAL_ACCELERATION_IN_M_PER_S2;
        }

        /**
         * @return F = μ * N
         */
        public static double friction(double frictionCoefficient, double normalForce) {
            return frictionCoefficient * normalForce;
        }

        /**
         * @return E = μ * (m * g * cos(θ)) * d
         */
        public static double energyLostToFriction(double frictionCoef, double distanceTraveled, double massInKg,
                                                  double gravitationalAcceleration, double theta) {
            return frictionCoef * (massInKg * gravitationalAcceleration * Math.cos(theta) * distanceTraveled);
        }

        /**
         * @return ѱ = δ + ⍺. The units are radians
         */
        public static double aircraftHeading(double course, double windCorrectionAngle) {
            return course + windCorrectionAngle;
        }

        /**
         * @return α = sin^(-1)[(v_w / v_a) * sin(ω - δ)]. The units are radians
         */
        public static double windCorrectionAngle(
            double trueAirspeed, double windSpeed, double course, double windDirection) {
            return Math.asin((windSpeed / trueAirspeed) * Math.sin(windDirection - course));
        }

        /**
         * @return v_g = √(v_a² + v_w² - (2 * v_a * v_w * cos(δ) - ω + α)). The units are knots (kn)
         */
        public static double groundSpeed(double trueAirspeed, double windSpeed, double course, double windDirection) {
            final double windCorAngle = windCorrectionAngle(trueAirspeed, windSpeed, course, windDirection);
            return Math.sqrt(
                trueAirspeed * trueAirspeed + windSpeed * windSpeed
                    - (2 * trueAirspeed * windSpeed * Math.cos(course) - windDirection + windCorAngle)
            );
        }

        /**
         * @return J = Δp = p₂ - p₁
         */
        public static double impulse(double initialMomentum, double finalMomentum) {
            return initialMomentum - finalMomentum;
        }

        /**
         * @return J = Δp = p₂ − p₁ = m * V₂ - m * V₁ = m * ΔV
         */
        public static double impulse(double mass, double initialVelocity, double finalVelocity) {
            return mass * initialVelocity - mass * finalVelocity;
        }

        /**
         * @return a = √(a₁² + a₂² + a₃²)
         */
        public static double magnitudeOfAcceleration(double[] accelerationComponents) {
            return Math.sqrt(Arrays.stream(accelerationComponents).sum());
        }

        /**
         * @return |a| = |v₁ - v₂| / Δt
         */
        public static double[] magnitudeOfAcceleration(
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
        public static double polarMomentOfSolidCircle(double radius) {
            return Math.PI / 2 * Math.pow(radius, 4);
        }

        /**
         * @return J = π/32 * D⁴
         */
        public static double polarMomentOfSolidCircleFromDiameter(double diameter) {
            return Math.PI / 32 * Math.pow(diameter, 4);
        }

        /**
         * @return J = π/2 * (R⁴ - Rᵢ⁴)
         */
        public static double polarMomentOfHollowCylinder(double innerRadius, double outerRadius) {
            return Math.PI / 2 * (Math.pow(outerRadius, 4) - Math.pow(innerRadius, 4));
        }

        /**
         * @return J = π/32 * (D⁴ - d⁴)
         */
        public static double polarMomentOfHollowCylinderFromDiameters(double innerDiameter, double outerDiameter) {
            return Math.PI / 32 * (Math.pow(outerDiameter, 4) - Math.pow(innerDiameter, 4));
        }

        public static double[] calculateQuarterMile(double etConstant, double fsConstant, double weight, double power) {
            final double elapsedTime = etConstant * Math.pow((weight / power), (double) 1 / 3);
            final double finalSpeed = fsConstant * Math.pow((power / weight), (double) 1 / 3);
            return new double[]{elapsedTime, finalSpeed};
        }

        /**
         * ET = 6.290 * (Weight / Power)^⅓
         * <p/>
         * Final speed = 224 * (Power / Weight)^⅓
         */
        public static double[] huntingtonQuarterMile(double weight, double power) {
            return calculateQuarterMile(6.290, 224, weight, power);
        }

        /**
         * ET = 6.269 * (Weight / Power)^⅓
         * <p/>
         * Final speed = 230 * (Power / Weight)^⅓
         */
        public static double[] foxQuarterMile(double weight, double power) {
            return calculateQuarterMile(6.269, 230, weight, power);
        }

        /**
         * ET = 5.825 * (Weight / Power)^⅓
         * <p/>
         * Final speed = 234 * (Power / Weight)^⅓
         */
        public static double[] haleQuarterMile(double weight, double power) {
            return calculateQuarterMile(5.825, 234, weight, power);
        }
    }

    public static final class Dynamics {
        private Dynamics() {
        }

        /**
         * @return a = (vf − vi) / Δt. The units are m/s²
         */
        public static double acceleration(double initialVelocity, double finalVelocity, long changeInTime) {
            return (finalVelocity - initialVelocity) / changeInTime;
        }

        /**
         * @return a = 2 * (Δd − vi * Δt) / Δt². The units are m/s²
         */
        public static double accelerationWithDeltaDistance(
            double initialVelocity, double distanceTraveled, long changeInTime) {
            return 2 * (distanceTraveled - initialVelocity * changeInTime) / (changeInTime * changeInTime);
        }

        /**
         * @return a = F / m. The units are m/s²
         */
        public static double acceleration(double mass, double netForce) {
            return netForce / mass;
        }

        /**
         * @return Fₙ = m ⋅ g + F ⋅ sin(x). The units are Newtons
         */
        public static double normalForceWithHorizontalSurfaceAndDownwardExternalForce(
            double massInKg, double outsideForce, double outsideForceAngleRad) {
            return massInKg * GRAVITATIONAL_ACCELERATION_IN_M_PER_S2 + outsideForce
                * Trigonometry.sin(outsideForceAngleRad);
        }

        /**
         * @return Fₙ = m ⋅ g − F ⋅ sin(x). The units are Newtons
         */
        public static double normalForceWithHorizontalSurfaceAndUpwardExternalForce(
            double massInKg, double outsideForce, double outsideForceAngleRad) {
            return massInKg * GRAVITATIONAL_ACCELERATION_IN_M_PER_S2 - outsideForce
                * Trigonometry.sin(outsideForceAngleRad);
        }

        /**
         * @return Fₙ = m ⋅ g. The units are Newtons
         */
        public static double normalForceWithHorizontalSurface(double massInKg) {
            return massInKg * GRAVITATIONAL_ACCELERATION_IN_M_PER_S2;
        }

        /**
         * @return Fₙ = m ⋅ g ⋅ cos(α). The units are Newtons
         */
        public static double normalForceWithInclinedSurface(double massInKg, double inclinationAngleRad) {
            return massInKg * GRAVITATIONAL_ACCELERATION_IN_M_PER_S2 * Trigonometry.cos(inclinationAngleRad);
        }

        /**
         * →   →    →    →          →
         * F = F₁ + F₂ + F₃ + ... + Fₙ
         * →      →
         * F = ∑∞ Fᵢ
         * F₁ₓ = F₁ cos θ₁
         * Fₓ = F₁ₓ + F₂ₓ
         * F₁ᵧ = F₁ sin θ₁
         * Fᵧ = F₁ᵧ + F₂ᵧ
         * F = √(F²ₓ + F²ᵧ)
         * θ = tan⁻¹(Fᵧ / Fₓ)
         * The units are Newtons
         */
        public static double[] netForce(double[][] forces) {
            final double[] resultantForce = new double[4];
            final byte horizontalComponentIdx = Constants.ARR_1ST_INDEX;
            final byte verticalComponentIdx = Constants.ARR_2ND_INDEX;
            for (var forceComponents : forces) {
                final double force = forceComponents[Constants.ARR_1ST_INDEX];
                final double angle = forceComponents[Constants.ARR_2ND_INDEX];
                resultantForce[horizontalComponentIdx] += force * Trigonometry.cos(angle);
                resultantForce[verticalComponentIdx] += force * Trigonometry.sin(angle);
            }
            final double fx = resultantForce[horizontalComponentIdx];
            final double fy = resultantForce[verticalComponentIdx];
            // Magnitude (F)
            resultantForce[Constants.ARR_3RD_INDEX] = Algebra.squareRoot(fx * fx + fy * fy);
            // Direction (θ)
            resultantForce[Constants.ARR_4TH_INDEX] = Trigonometry.multivaluedTanInverse(fy, fx);
            return resultantForce;
        }
    }

    public static final class Electronics {
        public static final double THREE_PHASE_GENERATOR = 1.732; // √3

        private Electronics() {
        }

        /**
         * C = Q / V
         *
         * @return Q = C * V. The units are μC
         */
        public static double electricalChargeInCapacitor(double capacitanceMicroFarads, double voltageVolts) {
            return capacitanceMicroFarads * voltageVolts;
        }

        /**
         * Alternatives: E = ½ × Q² / C or E = ½ × Q × V.
         *
         * @return E = ½ × C × V². The units are Joules
         */
        public static double energyStoredInCapacitor(double capacityFarads, double voltageVolts) {
            return MathCalc.ONE_HALF * capacityFarads * voltageVolts * voltageVolts;
        }

        /**
         * @return E = ½ × L × I². The units are Joules
         */
        public static double inductorEnergy(double inductanceHenries, double currentAmperes) {
            return MathCalc.ONE_HALF * inductanceHenries * currentAmperes * currentAmperes;
        }

        /**
         *
         * @return C = 2(E/V²). The units are MicroFarads μF
         */
        public static double capacitorSize(double startupEnergyMicroJoules, double voltageVolts) {
            final double voltageVoltsSquared = voltageVolts * voltageVolts;
            return 2 * (startupEnergyMicroJoules / voltageVoltsSquared);
        }

        /**
         * I = I is current
         * R is a constant of proportionality, representing the resistance.
         *
         * @return V = I*R
         */
        public static double ohmsLawVoltage(double current, double resistance) {
            return current * resistance;
        }

        /**
         * @return V = P/I
         */
        public static double ohmsLawVoltageGivenPower(double current, double power) {
            return power / current;
        }

        /**
         * @return V = √(P*R)
         */
        public static double ohmsLawVoltageGivenPowerAndResistance(double power, double resistance) {
            return Algebra.squareRoot(power * resistance);
        }

        /**
         * Equivalents:
         * <br/>P = dU/dt = dU/dq * dq/dt
         * P = dU/dt. The units are joules/second (aka watts)
         *
         * @return P = I × V. The units are Watts W
         */
        public static double ohmsLawPower(double currentAmperes, double voltageVolts) {
            return currentAmperes * voltageVolts;
        }

        /**
         * Equivalent: P = I*V
         *
         * @return P = I*L * dI/dt. The units are Watts W
         */
        public static double ohmsLawPowerInInductor(
            double current, double inductance, double changeInCurrent, double changeInTime) {
            return current * inductance * (changeInCurrent / changeInTime);
        }

        /**
         * @return P = V²/R. The units are Watts W
         */
        public static double ohmsLawPowerGivenVoltageAndResistance(double voltage, double resistance) {
            return (voltage * voltage) / resistance;
        }

        /**
         * @return P = R*I². The units are Watts W
         */
        public static double ohmsLawPowerGivenResistanceAndCurrent(double resistance, double current) {
            return resistance * (current * current);
        }

        /**
         * Alternatives:
         * <br/>I = dq/dt
         * <br/>I = C * (dv/dt)
         *
         * @return I = V/R. The units are Amperes
         */
        public static double ohmsLawCurrent(double voltage, double resistance) {
            return voltage / resistance;
        }

        /**
         * @return I = P/V. The units are Amperes
         */
        public static double ohmsLawCurrentGivenPowerAndVoltage(double power, double voltage) {
            return power / voltage;
        }

        /**
         * @return I = √(P/V). The units are Amperes
         */
        public static double ohmsLawCurrentGivenPowerAndResistance(double power, double resistance) {
            return Algebra.squareRoot(power / resistance);
        }

        /**
         * @return R = V/I. The units are Ohms
         */
        public static double ohmsLawResistance(double voltage, double current) {
            return voltage / current;
        }

        /**
         * @return R = P/I². The units are Ohms
         */
        public static double ohmsLawResistanceGivenPowerAndCurrent(double power, double current) {
            return power / (current * current);
        }

        /**
         * @return R = V²/P. The units are Ohms
         */
        public static double ohmsLawResistanceGivenVoltageAndPower(double voltage, double power) {
            return (voltage * voltage) / power;
        }

        /**
         * <ul>
         *     <li>ρ - Specific resistance of the conductive material</li>
         *     <li>E - Electric field vector</li>
         *     <li>J - Current density vector</li>
         * </ul>
         *
         * @return ρ = E/J. The units are Ohms
         */
        public static double ohmsLawForAnisotropicMaterial() {
            throw new UnsupportedOperationException();
        }

        /**
         * V = V₁ + V₂ + … → Q / C = Q / C₁ + Q / C₂ + …
         *
         * @return 1 / C = 1 / C₁ + 1 / C₂ + …. The units are μF
         */
        public static double capacitorInSeries(double[] capacitorsInMicroFarads) {
            final double inverseOfCapacitanceSum = Arrays.stream(capacitorsInMicroFarads)
                .map(capacitor -> 1 / capacitor)
                .sum();
            return 1 / inverseOfCapacitanceSum;
        }

        /**
         * @return C = C₁ + C₂ + …. The units are F
         */
        public static double capacitorInParallel(double[] capacitorsInFarads) {
            return Arrays.stream(capacitorsInFarads).sum();
        }

        /**
         * R = band₃×(10×band₁+band₂)±band₄
         * <br/>Rₘᵢₙ = R−(band₄×R)
         * <br/>Rₘₐₓ = R+(band₄×R)
         *
         * @return {R, Rₘᵢₙ, Rₘₐₓ}
         */
        public static double[] resistorBand4Value(
            ResistorColorCode band1,
            ResistorColorCode band2,
            ResistorColorCode.MultiplierBand multiplierBand,
            ResistorColorCode.Tolerance tolerance) {
            final double resistorValue = multiplierBand.getMultiplier() * (10 * band1.ordinal() + band2.ordinal());
            final double min = resistorValue - (tolerance.getTolerancePercent() * resistorValue);
            final double max = resistorValue + (tolerance.getTolerancePercent() * resistorValue);
            return new double[]{resistorValue, min, max};
        }

        /**
         * R = band₄*(100×band₁+10×band₂+band₃)±band₅
         * <br/>Rₘᵢₙ = R−(band₅×R)
         * <br/>Rₘₐₓ = R+(band₅×R)
         *
         * @return {R, Rₘᵢₙ, Rₘₐₓ}
         */
        public static double[] resistorBand5Value(
            ResistorColorCode band1,
            ResistorColorCode band2,
            ResistorColorCode band3,
            ResistorColorCode.MultiplierBand multiplierBand,
            ResistorColorCode.Tolerance tolerance) {
            final double resistorValue = multiplierBand.getMultiplier()
                * (100 * band1.ordinal() + 10 * band2.ordinal() + band3.ordinal());
            final double min = resistorValue - (tolerance.getTolerancePercent() * resistorValue);
            final double max = resistorValue + (tolerance.getTolerancePercent() * resistorValue);
            return new double[]{resistorValue, min, max};
        }

        /**
         * R = R₀×(1+TCR×(T−T₀))
         *
         * @return {R, Rₘᵢₙ, Rₘₐₓ}
         */
        public static double[] resistorBand6Value(
            ResistorColorCode[] bands,
            ResistorColorCode.MultiplierBand multiplierBand,
            ResistorColorCode.Tolerance tolerance,
            ResistorColorCode.TCR tcr,
            double temperatureStart, double temperatureEnd
        ) {
            final var band1 = bands[Constants.ARR_1ST_INDEX];
            final var band2 = bands[Constants.ARR_2ND_INDEX];
            final var band3 = bands[Constants.ARR_3RD_INDEX];
            final double[] r0values = resistorBand5Value(band1, band2, band3, multiplierBand, tolerance);
            final double r0 = r0values[Constants.ARR_1ST_INDEX];
            final double resistorValue = r0 * (1 + tcr.getTempCoeff() * (temperatureEnd - temperatureStart));
            return new double[]{resistorValue, r0values[Constants.ARR_2ND_INDEX], r0values[Constants.ARR_3RD_INDEX]};
        }

        /**
         * @return V⋅I/1000. The units are kVA
         */
        public static double apparentPowerACSinglePhase(double currentAmpres, double voltageVolts) {
            return apparentPowerAC(1, currentAmpres, voltageVolts);
        }

        /**
         * @return V⋅I⋅P_F/1000. The units are kW
         */
        public static double acPowerSinglePhase(double currentAmpres, double voltageVolts, double powerFactor) {
            return acPower(1, currentAmpres, voltageVolts, powerFactor);
        }

        /**
         * @return V⋅I⋅P_F⋅η/746. The units are hp
         */
        public static double motorOutputHorsepowerACSinglePhase(
            double currentAmpres, double voltageVolts, double powerFactor, double efficiency) {
            return motorOutputHorsepowerAC(1, currentAmpres, voltageVolts, powerFactor, efficiency);
        }

        /**
         * @return V⋅I/1000. The units are kVA
         */
        public static double apparentPowerACThreePhase(double currentAmpres, double voltageVolts) {
            return apparentPowerAC(THREE_PHASE_GENERATOR, currentAmpres, voltageVolts);
        }

        /**
         * @return V⋅I⋅P_F/1000. The units are kW
         */
        public static double acPowerThreePhase(double currentAmpres, double voltageVolts, double powerFactor) {
            return acPower(THREE_PHASE_GENERATOR, currentAmpres, voltageVolts, powerFactor);
        }

        /**
         * @return V⋅I⋅P_F⋅η/746. The units are hp
         */
        public static double motorOutputHorsepowerACThreePhase(
            double currentAmpres, double voltageVolts, double powerFactor, double efficiency) {
            return motorOutputHorsepowerAC(THREE_PHASE_GENERATOR, currentAmpres, voltageVolts, powerFactor, efficiency);
        }

        public static double apparentPowerAC(double phase, double currentAmpres, double voltageVolts) {
            return phase * voltageVolts * currentAmpres / 1000;
        }

        /**
         * AC - Alternating Current
         */
        public static double acPower(double phase, double currentAmpres, double voltageVolts, double powerFactor) {
            return phase * voltageVolts * currentAmpres * powerFactor / 1000;
        }

        public static double motorOutputHorsepowerAC(
            double phase, double currentAmpres, double voltageVolts, double powerFactorPercent, double efficiency) {
            return phase * voltageVolts * currentAmpres * powerFactorPercent * efficiency / HORSEPOWER;
        }

        /**
         * @return V⋅I⋅P_F/1000. The units are kW
         */
        public static double powerDirectCurrent(double currentAmpres, double voltageVolts) {
            return currentAmpres * voltageVolts / 1000;
        }

        public static double motorOutputHorsepowerDirectCurrent(
            double currentAmpres, double voltageVolts, double efficiency) {
            return voltageVolts * currentAmpres * efficiency / HORSEPOWER;
        }

        /**
         * @param inductors in Henries (H)
         * @return L = 1/(1/L₁ + 1/L₂ + ... + 1/Lₙ). The units are H
         */
        public static double equivalentInductanceInParallel(double[] inductors) {
            return 1 / Arrays.stream(inductors).map(inductor -> 1 / inductor).sum();
        }

        public static double missingInductorInParallel(double[] inductors, double desiredTotalInductance) {
            final double reciprocalSum = Arrays.stream(inductors).map(inductor -> 1 / inductor).sum();
            final double reciprocalMissing = (1 / desiredTotalInductance) - reciprocalSum;
            return 1 / reciprocalMissing;
        }

        /**
         * @param inductors in Henries (H)
         * @return L = L₁ + L₂ + ... + Lₙ. The units are H
         */
        public static double equivalentInductanceInSeries(double[] inductors) {
            return Arrays.stream(inductors).sum();
        }

        public static double missingInductorInSeries(double[] inductors, double desiredTotalInductance) {
            return desiredTotalInductance - equivalentInductanceInSeries(inductors);
        }

        /**
         * @param resistors in Ohms (Ω)
         * @return R = R₁ + R₂ + ... + Rₙ. The units are Ω
         */
        public static double equivalentResistanceInParallel(double[] resistors) {
            return 1 / Arrays.stream(resistors).map(resistor -> 1 / resistor).sum();
        }

        public static double missingResistorInParallel(double[] resistors, double desiredTotalResistance) {
            final double reciprocalSum = Arrays.stream(resistors).map(resistor -> 1 / resistor).sum();
            final double reciprocalMissing = (1 / desiredTotalResistance) - reciprocalSum;
            return 1 / reciprocalMissing;
        }

        /**
         * @return P = I²⋅R = V²/R. The units are watts (W)
         */
        public static double[] resistorDissipatedPower(double resistanceOhms, double voltageVolts) {
            return new double[]{
                ohmsLawPowerGivenVoltageAndResistance(voltageVolts, resistanceOhms),
                ohmsLawCurrent(voltageVolts, resistanceOhms)
            };
        }

        public static double[][] resistorWattageInParallel(double[] resistors, double constantVoltage) {
            final int rows = resistors.length;
            final int columns = 4;
            final double[][] resistorWattages = new double[rows][columns];
            for (int i = 0; i < rows; i++) {
                final double resistor = resistors[i];
                double[] dissipatedPower = resistorDissipatedPower(resistor, constantVoltage);
                final double[] row = new double[columns];
                row[Constants.ARR_1ST_INDEX] = resistor;
                row[Constants.ARR_2ND_INDEX] = dissipatedPower[Constants.ARR_2ND_INDEX];
                row[Constants.ARR_3RD_INDEX] = 0;
                row[Constants.ARR_4TH_INDEX] = dissipatedPower[Constants.ARR_1ST_INDEX];
                resistorWattages[i] = row;
            }
            return resistorWattages;
        }

        /**
         * RR - Resistor + Resistor
         *
         * @param resistors in ohms (Ω)
         * @return V₂ = R₂ / (R₁+R₂)V₁. The units are volts (V)
         */
        public static double voltageDividerRR(double[] resistors, double inputVoltageVolts) {
            Objects.requireNonNull(resistors);

            final double lastResistor = resistors[resistors.length - 1];
            final double sum = Arrays.stream(resistors).sum();
            return lastResistor / sum * inputVoltageVolts;
        }

        /**
         * CC - Capacitor + Capacitor
         *
         * @param capacitors in farads (F)
         * @return V₂ = C₁ / (C₁+C₂)V₁. The units are volts (V)
         */
        public static double voltageDividerCC(double[] capacitors, double inputVoltageVolts) {
            Objects.requireNonNull(capacitors);

            final double fistCapacitor = capacitors[Constants.ARR_1ST_INDEX];
            final double sum = Arrays.stream(capacitors).sum();
            return fistCapacitor / sum * inputVoltageVolts;
        }

        /**
         * LL - Inductor + Inductor
         *
         * @param inductors in henries (H)
         * @return V₂ = L₂ / (L₁+L₂)V₁. The units are volts (V)
         */
        public static double voltageDividerLL(double[] inductors, double inputVoltageVolts) {
            Objects.requireNonNull(inductors);

            final double lastInductor = inductors[inductors.length - 1];
            final double sum = Arrays.stream(inductors).sum();
            return lastInductor / sum * inputVoltageVolts;
        }

        /**
         * E = √(4⋅R⋅k⋅T⋅ΔF). The units are volts (V).
         * Lᵤ = 20⋅log₁₀(V/V₀) where V₀ is the reference voltage for noise level Lᵤ.
         * The units are decibels unloaded (dBu).
         * Lᵥ = 20⋅log₁₀(V/V₀) where V₀ = 1 V. The units are decibel Volt (dBV).
         *
         * @return [E, Lᵤ, Lᵥ]
         */
        public static double[] resistorNoise(double resistanceOhms, double temperatureKelvins, double bandwidthHz) {
            final double resistorNoise = Algebra
                .squareRoot(4 * resistanceOhms * BOLTZMANN_CONSTANT * temperatureKelvins * bandwidthHz);
            final double noiseLevelLu = 20 * Algebra.log(resistorNoise / REF_VOLTAGE_FOR_0_DBU);
            final double noiseLevelLv = 20 * Algebra.log(resistorNoise);
            return new double[]{resistorNoise, noiseLevelLu, noiseLevelLv};
        }

        /**
         * Peak voltage (Vₚ)
         *
         * @return Vᵣₘₛ = Vₚ/√2
         */
        public static double rmsVoltageSineWaveVp(double voltageVolts) {
            return voltageVolts / Algebra.squareRoot(2);
        }

        /**
         * Peak-to-peak voltage (Vₚₚ)
         *
         * @return Vᵣₘₛ = Vₚₚ/(2√2)
         */
        public static double rmsVoltageSineWaveVpp(double voltageVolts) {
            return voltageVolts / (2 * Algebra.squareRoot(2));
        }

        /**
         * Average voltage (Vₐᵥ₉)
         *
         * @return Vᵣₘₛ = πVₐᵥ₉/(2√2)
         */
        public static double rmsVoltageSineWaveVavg(double voltageVolts) {
            return Math.PI * voltageVolts / (2 * Algebra.squareRoot(2));
        }

        /**
         * @return Vᵣₘₛ = Vₚ
         */
        public static double rmsVoltageSquareWaveVp(double voltageVolts) {
            return voltageVolts;
        }

        /**
         * @return Vᵣₘₛ = Vₚₚ/2
         */
        public static double rmsVoltageSquareWaveVpp(double voltageVolts) {
            return voltageVolts / 2;
        }

        /**
         * @return Vᵣₘₛ = Vₐᵥ₉
         */
        public static double rmsVoltageSquareWaveVavg(double voltageVolts) {
            return voltageVolts;
        }

        /**
         * @return Vᵣₘₛ = Vₚ/√3
         */
        public static double rmsVoltageTriangleWaveVp(double voltageVolts) {
            return voltageVolts / Algebra.squareRoot(3);
        }

        /**
         * @return Vᵣₘₛ = Vₚₚ/(2√3)
         */
        public static double rmsVoltageTriangleWaveVpp(double voltageVolts) {
            return voltageVolts / (2 * Algebra.squareRoot(3));
        }

        /**
         * @return Vᵣₘₛ = πVₐᵥ₉/(2√3)
         */
        public static double rmsVoltageTriangleWaveVavg(double voltageVolts) {
            return (Math.PI * voltageVolts) / (2 * Algebra.squareRoot(3));
        }

        /**
         * @return Vᵣₘₛ = Vₚ/√3
         */
        public static double rmsVoltageSawtoothWaveVp(double voltageVolts) {
            return voltageVolts / Algebra.squareRoot(3);
        }

        /**
         * @return Vᵣₘₛ = Vₚₚ/(2√3)
         */
        public static double rmsVoltageSawtoothWaveVpp(double voltageVolts) {
            return voltageVolts / (2 * Algebra.squareRoot(3));
        }

        /**
         * @return Vᵣₘₛ = πVₐᵥ₉/(2√3)
         */
        public static double rmsVoltageSawtoothWaveVavg(double voltageVolts) {
            return (Math.PI * voltageVolts) / (2 * Algebra.squareRoot(3));
        }

        /**
         * @return Vᵣₘₛ = Vₚ/2
         */
        public static double rmsVoltageHalfWaveRectifiedSineWaveVp(double voltageVolts) {
            return voltageVolts / 2;
        }

        /**
         * @return Vᵣₘₛ = Vₚₚ/4
         */
        public static double rmsVoltageHalfWaveRectifiedSineWaveVpp(double voltageVolts) {
            return voltageVolts / 4;
        }

        /**
         * @return Vᵣₘₛ = πVₐᵥ₉/2
         */
        public static double rmsVoltageHalfWaveRectifiedSineWaveVavg(double voltageVolts) {
            return Math.PI * voltageVolts / 2;
        }

        /**
         * @return Vᵣₘₛ = Vₚ/√2
         */
        public static double rmsVoltageFullWaveRectifiedSineWaveVp(double voltageVolts) {
            return voltageVolts / Algebra.squareRoot(2);
        }

        /**
         * @return Vᵣₘₛ = Vₚₚ/(2√2)
         */
        public static double rmsVoltageFullWaveRectifiedSineWaveVpp(double voltageVolts) {
            return voltageVolts / (2 * Algebra.squareRoot(2));
        }

        /**
         * @return Vᵣₘₛ = πVₐᵥ₉/(2√2)
         */
        public static double rmsVoltageFullWaveRectifiedSineWaveVavg(double voltageVolts) {
            return Math.PI * voltageVolts / (2 * Algebra.squareRoot(2));
        }
    }
}
