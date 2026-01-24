package org.example.assistantonsbservlet.physics;

import org.example.assistantonsbservlet.math.Constants;
import org.example.assistantonsbservlet.math.MathCalc;
import org.example.assistantonsbservlet.math.MathCalc.Algebra;
import org.example.assistantonsbservlet.math.MathCalc.Arithmetic;
import org.example.assistantonsbservlet.math.MathCalc.Geometry;
import org.example.assistantonsbservlet.math.MathCalc.LinearAlgebra;
import org.example.assistantonsbservlet.math.MathCalc.Trigonometry;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.DoubleUnaryOperator;

import static org.example.assistantonsbservlet.math.MathCalc.Algebra.ln;
import static org.example.assistantonsbservlet.math.MathCalc.Algebra.log;
import static org.example.assistantonsbservlet.math.MathCalc.Algebra.squareRoot;
import static org.example.assistantonsbservlet.math.MathCalc.Arithmetic.reciprocal;
import static org.example.assistantonsbservlet.math.MathCalc.Geometry.crossSectionalAreaOfCircularWire;
import static org.example.assistantonsbservlet.math.NumberUtils.checkGreater0;
import static org.example.assistantonsbservlet.physics.AccelerationUnit.GRAVITATIONAL_ACCELERATION_ON_EARTH;

public final class PhysicsCalc {
    public static final double AVOGADRO_NUMBER = 6.02214076e23;
    /**
     * ~3.00 * 10⁸ m/s. Also, 300 * 10⁶ m/s
     */
    public static final int SPEED_OF_LIGHT = 299_792_458;
    public static final double ELECTRON_CHARGE_IN_COULOMBS = 1.6021766208e-19;
    public static final double BLINK_OF_AN_EYE_SEC = 0.350; // 350e-3
    /**
     * (6.02214 * 10²³ electrons/mole) / (6.24151 * 10¹⁸ electrons/coulomb) = 96485 coulombs/mole
     * {@link #AVOGADRO_NUMBER} {@link #ONE_COULOMB}
     */
    public static final double FARADAY_CONSTANT = 96_485;
    /**
     * The number of electron charges
     */
    public static final double ONE_COULOMB = 6.241509343e18;
    public static final double COULOMB_CONSTANT = 8.98755e9; // (N⋅m²)/C²

    public static final short HORSEPOWER = 746;
    public static final double BOLTZMANN_CONSTANT = 1.380649e-23; // J/K
    public static final double STEFAN_BOLTZMANN_CONSTANT = 5.67037442e-8; // 5.67037442×10⁻⁸ J/(s⋅m²⋅k⁴)
    public static final double REF_VOLTAGE_FOR_0_DBU = 0.77459667;
    public static final double VACUUM_PERMITTIVITY = 8.854187818814e-12; // ε₀≈8.8541×10⁻¹² F/m
    /**
     * Permeability of free space
     * μ₀≈4π×10⁻⁷ H/m; 1.25664×10⁻⁶ T⋅m/A
     */
    public static final double VACUUM_PERMEABILITY = Trigonometry.PI4 * 1e-7;
    public static final short SOUND_SPEED = 343; // Normal room temperature at 20°C; m/s or 1130 ft/s
    public static final double SOUND_SPEED_IN_DRY_AIR = 331.3; // at 0°C; m/s
    public static final double SOUND_SPEED_IN_AIR_KELVIN_REF_POINT = 273.15; // at 0°C
    public static final int FPE_CONSTANT = 450_240; // foot-pounds of energy
    public static final double GRAVITATIONAL_CONSTANT = 6.6743e-11; // 6.6743 × 10⁻¹¹ m³ kg⁻¹ s⁻²
    public static final byte MONOATOMIC_GAS_DEGREES_OF_FREEDOM = 3;
    public static final double SUN_POTENTIAL_ENERGY_ERGS = 1.788e54;
    public static final double REDUCED_PLANCK_CONSTANT = 1.0545718001e-34; // h/2π; 1.0545718001×10⁻³⁴ J·s
    public static final double UNIVERSAL_GAS_CONSTANT = 8.31446261815324; // J/(mol·K)

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
         * @return velocity = distance / time. The units are m/s
         */
        public static double velocity(double distanceMeters, long timeSeconds) {
            checkGreater0(distanceMeters);
            checkTimeInput(timeSeconds);
            return distanceMeters / timeSeconds;
        }

        /**
         * @param finalVelocity in m/s
         * @param acceleration  in m/s²
         * @return u = v − a * t. The units are m/s
         */
        public static double initialVelocity(double finalVelocity, double acceleration, long timeSeconds) {
            return finalVelocity - velocityChange(acceleration, timeSeconds);
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
            return squareRoot(value);
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
         * @param initialVelocity in m/s
         * @param acceleration    in m/s²
         * @return v = u + at. The units are m/s
         */
        public static double finalVelocity(double initialVelocity, double acceleration, long timeSeconds) {
            return initialVelocity + velocityChange(acceleration, timeSeconds);
        }

        /**
         * @return average velocity = (velocity₁ × time₁ + velocity₂ × time₂ + …) / total time. The units are m/s
         */
        public static double avgVelocity(double[][] velocities) {
            final double[][] timeVelocityArray = Arrays.stream(velocities)
                .map(velocityData -> new double[]{
                    velocityData[Constants.ARR_2ND_INDEX], velocityData[Constants.ARR_1ST_INDEX]
                })
                .toList()
                .toArray(new double[0][]);
            return Arithmetic.weightedAverage(timeVelocityArray);
        }

        /**
         * @return The units are m/s
         */
        public static double velocityChange(double acceleration, long timeSeconds) {
            return acceleration * timeSeconds;
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
         * @return t = (v_t/g) * tanh⁻¹(h/v_t)
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
         * @param velocity in m/s
         * @return p = mv. The units are kg*m/s
         */
        public static double momentum(double massKg, double velocity) {
            return massKg * velocity;
        }

        /**
         * @return v = p/m. The units are kg*m/s
         */
        public static double velocityOfDesiredMomentum(double momentum, double mass) {
            return momentum / mass;
        }

        /**
         * @return ||p|| = m * √(vₓ² + vᵧ² + v_z²) ⇒ ||p|| = m * ||v||. The units are m/s
         */
        public static double velocityMagnitude(double[] velocityVector) {
            return LinearAlgebra.vectorMagnitude(velocityVector);
        }

        /**
         * <ul>
         *     <li>p = [pₓ, pᵧ, p_z] = mv = m[vₓ, vᵧ, v_z]</li>
         *     <li>pₓ = mvₓ</li>
         *     <li>pᵧ = mvᵧ</li>
         *     <li>p_z = mv_z</li>
         *     <li>∥p∥ = √(pₓ²+pᵧ²+p_z²) = m√(vₓ²+vᵧ²+v_z²) ⟹ ∥p∥ = m∥v∥</li>
         * </ul>
         *
         * @return ||p|| = m * ||v||. The units are kg*m/s
         */
        public static double momentumMagnitude(double massKg, double[] velocityVector) {
            return massKg * velocityMagnitude(velocityVector);
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
            return MathCalc.ONE_HALF * acceleration * timeInSeconds * timeInSeconds + initialVelocity * timeInSeconds;
        }

        /**
         * @return d = (1 / 2) * (v₁ + v₀) * t. The units are meters
         */
        public static double displacementOfVelocities(
            double initialVelocity, double finalVelocity, long timeInSeconds) {
            return MathCalc.ONE_HALF * (finalVelocity + initialVelocity) * timeInSeconds;
        }

        /**
         * @return v = v₀ + g * t. The units are m/s
         */
        public static double freeFallVelocity(double initialVelocity, long fallTime) {
            return initialVelocity + GRAVITATIONAL_ACCELERATION_ON_EARTH * fallTime;
        }

        /**
         * @return s = (1 / 2) * g * t². The units are meters
         */
        public static double freeFallDistance(long fallTimeInSec) {
            return MathCalc.ONE_HALF * GRAVITATIONAL_ACCELERATION_ON_EARTH * fallTimeInSec * fallTimeInSec;
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
            return mass * GRAVITATIONAL_ACCELERATION_ON_EARTH;
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
         * @return α = sin⁻¹[(v_w / v_a) * sin(ω - δ)]. The units are radians
         */
        public static double windCorrectionAngle(
            double trueAirspeed, double windSpeed, double course, double windDirection) {
            return Math.asin((windSpeed / trueAirspeed) * Math.sin(windDirection - course));
        }

        /**
         * @param trueAirspeed in m/s
         * @param windSpeed    in m/s
         * @return v_g = √(vₐ² + v_w² - (2 * vₐ * v_w * cos(δ) - ω + α)). The units are m/s
         */
        public static double groundSpeed(
            double trueAirspeed, double windSpeed, double courseRadians, double windDirectionRadians) {
            final double windCorAngle = windCorrectionAngle(
                trueAirspeed, windSpeed, courseRadians, windDirectionRadians);
            final double heading = courseRadians + windDirectionRadians;
            return squareRoot(trueAirspeed * trueAirspeed + windSpeed * windSpeed
                - (2 * trueAirspeed * windSpeed * Trigonometry.cos(heading)) - windDirectionRadians + windCorAngle
            );
        }

        /**
         * @return J = Δp = p₂ − p₁ = m * V₂ - m * V₁ = m * ΔV. The units are N·s
         */
        public static double impulse(double massKg, double initialVelocity, double finalVelocity) {
            return massKg * (finalVelocity - initialVelocity);
        }

        /**
         * @param impulse in N·s
         * @return J = F⋅t. The units are N
         */
        public static double forceFromImpulse(double impulse, double timeIntervalSeconds) {
            return impulse / timeIntervalSeconds;
        }

        /**
         * @param impulse in N·s
         * @return J = F⋅t. The units are seconds
         */
        public static double timeIntervalOfImpulse(double impulse, double forceNewtons) {
            return impulse / forceNewtons;
        }

        /**
         * @param impulse       in N·s
         * @param finalMomentum in N·s
         * @return p₁ = p₂ - J. The units are N·s
         */
        public static double initialMomentumFromImpulse(double impulse, double finalMomentum) {
            return finalMomentum - impulse;
        }

        /**
         * @param impulse         in N·s
         * @param initialMomentum in N·s
         * @return p₂ = J + p₁. The units are N·s
         */
        public static double finalMomentumFromImpulse(double impulse, double initialMomentum) {
            return impulse + initialMomentum;
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

        /**
         * For rotating objects
         *
         * @return ω = Δθ/Δt. The units are rad/s
         */
        public static double angularFrequency(double angularDisplacementRad, double timeTakenSeconds) {
            return angularDisplacementRad / timeTakenSeconds;
        }

        /**
         * @return Δθ = ωΔt. The units are rad/s
         */
        public static double angularDisplacementFromAngularFrequency(double angularFrequency, double timeTakenSeconds) {
            return angularFrequency * timeTakenSeconds;
        }

        /**
         * @return Δt = Δθ/ω. The units are sec
         */
        public static double timeTakenFromAngularFrequency(double angularFrequency, double angularDisplacement) {
            return angularDisplacement / angularFrequency;
        }

        /**
         * @return ω = 2πf = 2π/T. The units are rad/s
         */
        public static double angularFrequencyOfOscillatingObject(double timePeriodSeconds) {
            return Trigonometry.PI2 / timePeriodSeconds;
        }

        /**
         * @param angularFrequency in rad/s
         * @return f = ω/2π. The units are Hz
         */
        public static double frequencyFromAngularFrequencyOfOscillatingObject(double angularFrequency) {
            return angularFrequency / Trigonometry.PI2;
        }

        /**
         * @return τ = r F sin(θ). The units are N⋅m
         */
        public static double torque(double distanceMeters, double forceNewtons, double angleRad) {
            return distanceMeters * forceNewtons * Trigonometry.sin(angleRad);
        }

        /**
         * Angular frequency is a scalar, whereas angular velocity is a (pseudo)vector.
         *
         * @param initialAngularVelocity in rad
         * @param angularAcceleration    in rad
         * @return ω = ω₀ + αt. The units are rad/s
         */
        public static double angularVelocityForConstantAngularAcceleration(
            double initialAngularVelocity, double angularAcceleration, double timeSeconds) {
            return initialAngularVelocity + angularAcceleration * timeSeconds;
        }

        /**
         * Angular frequency is a scalar, whereas angular velocity is a (pseudo)vector.
         * Angular velocity difference.
         *
         * @param initialAngle in rad
         * @param finalAngle   in rad
         * @return α = (ω₂−ω₁)/t. The units are rad/s
         */
        public static double angularVelocity(double initialAngle, double finalAngle, double timeSeconds) {
            return (finalAngle - initialAngle) / timeSeconds;
        }

        /**
         * Angular acceleration — Describes how the angular velocity changes with time.
         *
         * @param initialAngularVelocity in rad/s
         * @param finalAngularVelocity   in rad/s
         * @return α = (ω₂−ω₁)/t. The units are rad/s²
         */
        public static double angularAcceleration(
            double initialAngularVelocity, double finalAngularVelocity, double timeSeconds) {
            return angularVelocity(initialAngularVelocity, finalAngularVelocity, timeSeconds);
        }

        /**
         * Radial velocity
         *
         * @param tangentialAcceleration in m/s²
         * @return α = a/R. The units are rad/s²
         */
        public static double angularAcceleration(double tangentialAcceleration, double radiusMeters) {
            return tangentialAcceleration / radiusMeters;
        }

        /**
         * @param tangentialVelocity in m/s
         * @return F = mv²/r. The units are N
         */
        public static double centrifugalForce(double massKg, double radiusMeters, double tangentialVelocity) {
            return (massKg * tangentialVelocity * tangentialVelocity) / radiusMeters;
        }

        /**
         * @return a = F / m. The units are m/s²
         */
        public static double centrifugalAcceleration(double massKg, double centrifugalForceNewtons) {
            return centrifugalForceNewtons / massKg;
        }

        /**
         * The centripetal force makes an object move along a curved trajectory, and it points
         * to the rotation's center. The centrifugal force is an apparent force felt by the body
         * which moves along a curved path, and it points outside the curvature.
         *
         * @param tangentialVelocity in m/s
         * @return F = mv²/r. The units are N
         */
        public static double centripetalForce(double massKg, double radiusMeters, double tangentialVelocity) {
            return (massKg * tangentialVelocity * tangentialVelocity) / radiusMeters;
        }

        /**
         * @param tangentialVelocity in m/s
         * @return a = v²/r. The units are m/s²
         */
        public static double centripetalAcceleration(double radiusMeters, double tangentialVelocity) {
            return (tangentialVelocity * tangentialVelocity) / radiusMeters;
        }

        /**
         * @return θ = s/r. The units are rad
         */
        public static double angularDisplacement(double distanceMeters, double radiusMeters) {
            return distanceMeters / radiusMeters;
        }

        /**
         * @param angularVelocity in rad/s
         * @return θ = ω × t. The units are rad
         */
        public static double angularDisplacementFromAngularVelocity(double angularVelocity, double timeSeconds) {
            return angularVelocity * timeSeconds;
        }

        /**
         * @param angularVelocity     in rad/s
         * @param angularAcceleration in rad/s²
         * @return θ = (ω × t) + (1 / 2 × ɑ × t²). The units are rad
         */
        public static double angularDisplacementFromAngularAcceleration(
            double angularVelocity, double timeSeconds, double angularAcceleration) {
            return (angularVelocity * timeSeconds) + (MathCalc.ONE_HALF * angularAcceleration
                * timeSeconds * timeSeconds);
        }

        /**
         * @param momentOfInertia in kg⋅m²
         * @param angularVelocity in rad/s
         * @return L = Iω. The units are kg⋅m²/s
         */
        public static double angularMomentumAroundOwnAxis(double momentOfInertia, double angularVelocity) {
            return momentOfInertia * angularVelocity;
        }

        /**
         * @param velocity in m/s
         * @return L = mvr. The units are kg⋅m²/s
         */
        public static double angularMomentumAroundCentralPoint(double massKg, double velocity, double radiusMeters) {
            return massKg * velocity * radiusMeters;
        }

        /**
         * J = ∫_A ρ²dA
         * J = I_z = Iₓ + Iᵧ
         * <br/>
         * For ellipse: K = πa³b³/(a² + b²)
         * where:
         * K – Torsion constant of the ellipse;
         * a – Distance between the center and any ellipse vertices;
         * b – Distance between the center and the other ellipse's vertices.
         *
         * @return J = π/32 * D⁴. The units are m⁴
         */
        public static double polarMomentOfInertiaOfSolidCircle(double diameterMeters) {
            final double quartic = diameterMeters * diameterMeters * diameterMeters * diameterMeters;
            return Trigonometry.PI_OVER_32 * quartic;
        }

        /**
         * @return J = π/2(R⁴ - Rᵢ⁴). The units are m⁴
         */
        public static double polarMomentOfInertiaOfHollowCircle(double innerDiameterMeters,
                                                                double outerDiameterMeters) {
            final double innerRadius = Geometry.circleRadius(innerDiameterMeters);
            final double outerRadius = Geometry.circleRadius(outerDiameterMeters);
            final double quarticDiff = Math.pow(outerRadius, 4) - Math.pow(innerRadius, 4);
            return Trigonometry.PI_OVER_2 * quarticDiff;
        }

        /**
         * @return I = 2/5 * mr². The units are kg⋅m²
         */
        public static double massMomentOfInertiaOfBall(double massKg, double radiusMeters) {
            return MathCalc.TWO_FIFTH * massKg * radiusMeters * radiusMeters;
        }

        /**
         * Iₓ = Iᵧ = 1/2 * mr²
         * I_z = m*r²
         *
         * @return [Iₓ, I_z]. The units are kg⋅m²
         */
        public static double[] massMomentOfInertiaOfCircularHoop(double massKg, double radiusMeters) {
            final double inertiaZ = massKg * radiusMeters * radiusMeters;
            return new double[]{MathCalc.ONE_HALF * inertiaZ, inertiaZ};
        }

        /**
         * Iₗ = 1/12 * m(w²+h²)
         * I_w = 1/12 * m(l²+h²)
         * Iₕ = 1/12 * m(l²+w²)
         * I_d = 1/6 * m((w²h²+l²h²+l²w²) / (l²+w²+h²))
         *
         * @return The moment of inertia about an axis passing through the [length, width, height, longest diagonal].
         * The units are kg⋅m²
         */
        public static double[] massMomentOfInertiaOfCuboid(
            double massKg, double lengthMeters, double widthMeters, double heightMeters) {
            final double wSquared = widthMeters * widthMeters;
            final double hSquared = heightMeters * heightMeters;
            final double lengthInertia = MathCalc.ONE_TWELFTH * massKg * (wSquared + hSquared);
            final double lSquared = lengthMeters * lengthMeters;
            final double widthInertia = MathCalc.ONE_TWELFTH * massKg * (lSquared + hSquared);
            final double heightInertia = MathCalc.ONE_TWELFTH * massKg * (lSquared + wSquared);
            final double diagonalInertia = MathCalc.ONE_SIXTH * massKg * ((wSquared * hSquared + lSquared * hSquared
                + lSquared * wSquared) / (lSquared + wSquared + hSquared));
            return new double[]{lengthInertia, widthInertia, heightInertia, diagonalInertia};
        }

        /**
         * Iₓ = Iᵧ = 1/12 * m(3r²+h²)
         * I_z = 1/2 * m*r²
         *
         * @return [Iₓ, I_z]. The units are kg⋅m²
         */
        public static double[] massMomentOfInertiaOfCylinder(double massKg, double radiusMeters, double heightMeters) {
            final double rSquared = radiusMeters * radiusMeters;
            return new double[]{
                MathCalc.ONE_TWELFTH * massKg * (3 * rSquared + heightMeters * heightMeters),
                MathCalc.ONE_HALF * massKg * rSquared
            };
        }

        /**
         * Iₓ = Iᵧ = 1/12 * m(3(r²₂+r²₁)+h²)
         * I_z = 1/2 * m(r²₂+r²₁)
         *
         * @return [Iₓ, I_z]. The units are kg⋅m²
         */
        public static double[] massMomentOfInertiaOfCylinderTube(
            double massKg, double innerRadiusMeters, double outerRadiusMeters, double heightMeters) {
            final double radiusSqSum = outerRadiusMeters * outerRadiusMeters + innerRadiusMeters * innerRadiusMeters;
            return new double[]{
                MathCalc.ONE_TWELFTH * massKg * (3 * radiusSqSum + heightMeters * heightMeters),
                MathCalc.ONE_HALF * massKg * radiusSqSum
            };
        }

        /**
         * @return I = mr². The units are kg⋅m²
         */
        public static double massMomentOfInertiaOfCylinderShell(double massKg, double radiusMeters) {
            return massKg * radiusMeters * radiusMeters;
        }

        /**
         * Iₓ = Iᵧ = 1/4 * mr²
         * I_z = 1/2 * mr²
         *
         * @return [Iₓ, I_z]. The units are kg⋅m²
         */
        public static double[] massMomentOfInertiaOfDisc(double massKg, double radiusMeters) {
            final double radiusSq = radiusMeters * radiusMeters;
            return new double[]{MathCalc.ONE_FOURTH * massKg * radiusSq, MathCalc.ONE_HALF * massKg * radiusSq};
        }

        /**
         * φ = (1 + √5)/2
         * Solid: Iₓ = Iᵧ = I_z = (39φ + 28)/150 * ms²
         * Hollow: Iₓ = Iᵧ = I_z = (39φ + 28)/90 * ms²
         *
         * @return [solid, hollow]. The units are kg⋅m²
         */
        public static double[] massMomentOfInertiaOfDodecahedron(double massKg, double sideMeters) {
            final double phi = (1 + squareRoot(5)) / 2;
            final double numerator = 39 * phi + 28;
            final double prod = massKg * sideMeters * sideMeters;
            return new double[]{numerator / 150 * prod, numerator / 90 * prod};
        }

        /**
         * Iₐ = 1/5 * m(b² + c²)
         * I_b = 1/5 * m(a² + c²)
         * I꜀ = 1/5 * m(a² + b²)
         *
         * @return [a, b, c]. The units are kg⋅m²
         */
        public static double[] massMomentOfInertiaOfEllipsoid(
            double massKg, double semiaxisA, double semiaxisB, double semiaxisC) {
            final double aSq = semiaxisA * semiaxisA;
            final double bSq = semiaxisB * semiaxisB;
            final double cSq = semiaxisC * semiaxisC;
            final double mass = MathCalc.ONE_FIFTH * massKg;
            return new double[]{mass * (bSq + cSq), mass * (aSq + cSq), mass * (aSq + bSq)};
        }

        /**
         * φ = (1 + √5)/2
         * Solid: Iₓ = Iᵧ = I_z = φ²/10 * ms²
         * Hollow: Iₓ = Iᵧ = I_z = φ²/6 * ms²
         *
         * @return [solid, hollow]. The units are kg⋅m²
         */
        public static double[] massMomentOfInertiaOfIcosahedron(double massKg, double sideMeters) {
            final double phi = (1 + squareRoot(5)) / 2;
            final double phiSq = phi * phi;
            final double sSq = sideMeters * sideMeters;
            return new double[]{(phiSq / 10) * massKg * sSq, (phiSq / 6) * massKg * sSq};
        }

        /**
         * @return I = 1/2 * mL²(1 - 2/3 sin²(β)). The units are kg⋅m²
         */
        public static double massMomentOfInertiaOfIsoscelesTriangle(
            double commonSideLengthMeters, double massKg, double angleRad) {
            final double lSq = commonSideLengthMeters * commonSideLengthMeters;
            final double sinBeta = Trigonometry.sin(angleRad);
            return MathCalc.ONE_HALF * massKg * lSq * (1 - MathCalc.TWO_THIRDS * sinBeta * sinBeta);
        }

        /**
         * Solid: Iₓ = Iᵧ = I_z = 1/10 * ms²
         * Hollow: Iₓ = Iᵧ = I_z = 1/6 * ms²
         *
         * @return [solid, hollow]. The units are kg⋅m²
         */
        public static double[] massMomentOfInertiaOfOctahedron(double massKg, double sideMeters) {
            final double sSq = sideMeters * sideMeters;
            return new double[]{reciprocal(10) * massKg * sSq, reciprocal(6) * massKg * sSq};
        }

        /**
         * @return I = mr². The units are kg⋅m²
         */
        public static double massMomentOfPointMass(double massKg, double distanceMeters) {
            return massKg * distanceMeters * distanceMeters;
        }

        /**
         * @return I = 1/12 m(w²+l²). The units are kg⋅m²
         */
        public static double massMomentOfRectangularPlate(double massKg, double widthMeters, double lengthMeters) {
            return MathCalc.ONE_TWELFTH * massKg * (widthMeters * widthMeters + lengthMeters * lengthMeters);
        }

        /**
         * R = s/(2 sin(π/n))
         *
         * @param radiusMeters Radius of circumscribed circle.
         * @return I = 1/2 mR²(1-2/3 sin²(π/n)). The units are kg⋅m²
         */
        public static double massMomentOfRegularPolygon(double massKg, double radiusMeters, long numberOfVertices) {
            final double sine = Trigonometry.sin(Math.PI / numberOfVertices);
            return MathCalc.ONE_HALF * massKg * radiusMeters * radiusMeters * (1 - MathCalc.TWO_THIRDS * sine * sine);
        }

        /**
         * Iₓ = Iᵧ = 1/4 * m(r² + 2h²)
         * I_z = 1/2 * mr²
         *
         * @return [Iₓ, I_z]. The units are kg⋅m²
         */
        public static double[] massMomentOfHollowRightCircularCone(
            double massKg, double radiusMeters, double heightMeters) {
            final double rSq = radiusMeters * radiusMeters;
            return new double[]{
                MathCalc.ONE_FOURTH * massKg * (rSq + 2 * heightMeters * heightMeters),
                MathCalc.ONE_HALF * massKg * rSq
            };
        }

        /**
         * Iₓ = Iᵧ = 3/20 * m(r² + 4h²)
         * I_z = 3/10 * mr²
         *
         * @return [Iₓ, I_z]. The units are kg⋅m²
         */
        public static double[] massMomentOfSolidRightCircularCone(
            double massKg, double radiusMeters, double heightMeters) {
            final double rSq = radiusMeters * radiusMeters;
            return new double[]{
                0.15 * massKg * (rSq + 4 * heightMeters * heightMeters),
                0.3 * massKg * rSq
            };
        }

        /**
         * I_center = 1/12 * mL²
         * I_end = 1/3 * mL²
         *
         * @return [I_center, I_end]. The units are kg⋅m²
         */
        public static double[] massMomentOfRod(double massKg, double lengthMeters) {
            final double lSq = lengthMeters * lengthMeters;
            return new double[]{MathCalc.ONE_TWELFTH * massKg * lSq, MathCalc.ONE_THIRD * massKg * lSq};
        }

        /**
         * @return I = 2/3 * mr². The units are kg⋅m²
         */
        public static double massMomentOfInertiaOfSphere(double massKg, double radiusMeters) {
            return MathCalc.TWO_THIRDS * massKg * radiusMeters * radiusMeters;
        }

        /**
         * @return I = 2/5 * m((r⁵₂-r⁵₁)/(r³₂-r³₁)). The units are kg⋅m²
         */
        public static double massMomentOfSphericalShell(
            double massKg, double innerRadiusMeters, double outerRadiusMeters) {
            final double numerator = Math.pow(outerRadiusMeters, 5) - Math.pow(innerRadiusMeters, 5);
            final double denominator = Math.pow(outerRadiusMeters, 3) - Math.pow(innerRadiusMeters, 3);
            return MathCalc.TWO_FIFTH * massKg * (numerator / denominator);
        }

        /**
         * I_solid = 1/20 * ms²
         * I_hollow = 1/12 * ms²
         *
         * @return [I_solid, I_hollow]. The units are kg⋅m²
         */
        public static double[] massMomentOfTetrahedron(double massKg, double sideMeters) {
            final double sSq = sideMeters * sideMeters;
            return new double[]{MathCalc.ONE_TWENTIETH * massKg * sSq, MathCalc.ONE_TWELFTH * massKg * sSq};
        }

        /**
         * I⟂diameter = 1/4 * m(3a²+4b²)
         * I∥diameter = 1/8 * m(5a²+4b²)
         *
         * @return [center, diameter]. The units are kg⋅m²
         */
        public static double[] massMomentOfTorus(double massKg, double minorRadiusMeters, double majorRadiusMeters) {
            final double aSq = minorRadiusMeters * minorRadiusMeters;
            final double bSq = majorRadiusMeters * majorRadiusMeters;
            return new double[]{
                MathCalc.ONE_FOURTH * massKg * (3 * aSq + 4 * bSq),
                MathCalc.ONE_EIGHTH * massKg * (5 * aSq + 4 * bSq)
            };
        }

        /**
         * @return I = m₁m₂/m₁+m₂ * r² = μr²). The units are kg⋅m²
         */
        public static double massMomentOfTwoPointMasses(double massKg1, double massKg2, double distanceMeters) {
            return (massKg1 * massKg2) / (massKg1 + massKg2) * distanceMeters * distanceMeters;
        }
    }

    public static final class Mechanics {
        private Mechanics() {
        }

        /**
         * @return PE grav. = m×h×g. The units are joules
         */
        public static double potentialEnergy(double massKg, double heightMeters, double gravitationalAcceleration) {
            return massKg * heightMeters * gravitationalAcceleration;
        }

        /**
         * Elastic potential energy per unit volume:
         * u = (1/2) × (F/A) × (Δx/x)
         * (F/A) is stress, and (Δx/x) is the strain.
         * u = (1/2) × stress × strain
         *
         * @param springForceConstant in N/m
         * @param springStretchLength Δx in meters
         * @return U = 1/2 kΔx². The units are joules
         */
        public static double elasticPotentialEnergy(double springForceConstant, double springStretchLength) {
            return MathCalc.ONE_HALF * springForceConstant * springStretchLength * springStretchLength;
        }

        /**
         * @param springForceConstant   in N/m
         * @param springPotentialEnergy Δx in J
         * @return Δx = √(2 × U / k). The units are meters
         */
        public static double elongationOfString(double springForceConstant, double springPotentialEnergy) {
            return squareRoot(2 * springPotentialEnergy / springForceConstant);
        }

        /**
         * W = ΔKE = KE₂ – KE₁
         *
         * @param velocity in m/s
         * @return KE = 0.5 × m × v². The units are J
         */
        public static double kineticEnergy(double massKg, double velocity) {
            return MathCalc.ONE_HALF * massKg * velocity * velocity;
        }

        /**
         * Fₐ = 1/(2d) * mv²
         * Fₘₐₓ = mv²/d
         * E = 1/2 * mv²
         * <br/>
         * Impact loads:
         * <table>
         *     <tr><th>Category</th><th>Velocity</th></tr>
         *     <tr><th>Low-velocity impact</th><th>&lt;10 m/s</th></tr>
         *     <tr><th>Intermediate velocity impact</th><th>10-50 m/s</th></tr>
         *     <tr><th>High-velocity impact</th><th>50-1000 m/s</th></tr>
         *     <tr><th>Hypervelocity impact</th><th>&gt;2.5 km/s</th></tr>
         * </table>
         *
         * @param velocity in m/s
         */
        public static double[] impactEnergyDistance(double massKg, double velocity, double collisionDistanceMeters) {
            final double mvSquared = massKg * velocity * velocity;
            final double avgForceN = reciprocal(2 * collisionDistanceMeters) * mvSquared;
            final double maximumForceN = mvSquared / collisionDistanceMeters;
            final double energyJoules = MathCalc.ONE_HALF * mvSquared;
            return new double[]{avgForceN, maximumForceN, energyJoules};
        }

        /**
         * Fₐ = (mv)/t
         * Fₘₐₓ = 2Fₐ
         * E = 1/2 * mv²
         *
         * @param velocity in m/s
         */
        public static double[] impactEnergyTime(double massKg, double velocity, double collisionTimeSeconds) {
            final double avgForceN = (massKg * velocity) / collisionTimeSeconds;
            final double maximumForceN = 2 * avgForceN;
            final double energyJoules = MathCalc.ONE_HALF * massKg * velocity * velocity;
            return new double[]{avgForceN, maximumForceN, energyJoules};
        }

        /**
         * V_f = (M_b × V_b + M꜀ × V꜀) / (1000*M_f)
         * Eᵣ = 0.5 * M_f × V_f²
         * Iᵣ = M_f × V_f
         * Velocities are in m/s.
         */
        public static double[] recoilEnergy(double bulletMassGrams, double bulletVelocity, double powderChargeMassGrams,
                                            double velocityOfCharge, double firearmMassKg) {
            final double firearmVelocity = (bulletMassGrams * bulletVelocity + powderChargeMassGrams * velocityOfCharge)
                / (1000 * firearmMassKg);
            final double recoilEnergyJoules = MathCalc.ONE_HALF * firearmMassKg * firearmVelocity * firearmVelocity;
            final double recoilImpulse = firearmMassKg * firearmVelocity; // N⋅s
            return new double[]{firearmVelocity, recoilEnergyJoules, recoilImpulse};
        }

        /**
         * FPE = (w×v²)/450240
         *
         * @param velocity     projectile speed in ft/s
         * @param weightGrains projectile mass
         */
        public static double footPoundsOfEnergy(double velocity, double weightGrains) {
            return (weightGrains * velocity * velocity) / FPE_CONSTANT;
        }

        /**
         * @return power-to-weight ratio = power/weight
         */
        public static double pwr(double power, double weight) {
            return power / weight;
        }

        /**
         * Signal to Noise Ratio
         * <table>
         *     <tr><th>SNR values</th><th>Requirements</th></tr>
         *     <tr><td>5-10 dB</td><td>Cannot establish a connection</td></tr>
         *     <tr><td>10-15 dB</td><td>Can establish an unreliable connection</td></tr>
         *     <tr><td>15-25 dB</td><td>Acceptable level to establish a poor connection</td></tr>
         *     <tr><td>25-40 dB</td><td>Considered a good connection</td></tr>
         *     <tr><td>41+ dB</td><td>Considered to be an excellent connection</td></tr>
         * </table>
         *
         * @return SNR = signal / noise
         */
        public static double snr(double signal, double noise) {
            return signal / noise;
        }

        /**
         * @param signal in dB(s)
         * @param noise  in dB(s)
         * @return SNR(dB) = signal − noise. The units are dB(s)
         */
        public static double snrDifference(double signal, double noise) {
            return signal - noise;
        }

        /**
         * @return pSNR = 10×log(signal/noise). The units are dB(s)
         */
        public static double powerSNR(double signalWatts, double noiseWatts) {
            return 10 * log(signalWatts / noiseWatts);
        }

        /**
         * @return vSNR = 20×log(signal/noise). The units are dB(s)
         */
        public static double voltageSNR(double signalVolts, double noiseVolts) {
            return 20 * log(signalVolts / noiseVolts);
        }

        /**
         * SNR = μ/σ
         * SNR = μ²/σ²
         *
         * @param signalMean μ
         * @param noiseStd   noise's standard deviation (σ)
         */
        public static double[] snrFromCoefficientOfVariation(double signalMean, double noiseStd) {
            return new double[]{signalMean / noiseStd, (signalMean * signalMean) / (noiseStd * noiseStd)};
        }

        /**
         * 2,4,6-trinitrotoluene. One kg of TNT releases 4.184 MJ of energy upon detonation.
         * <br/>
         * TNT factor = Hₑₓₚ/Hₜₙₜ
         * W_eq = Wₑₓₚ * (Hₑₓₚ/Hₜₙₜ)
         */
        public static double[] tntEquivalent(
            double explosiveDetonationHeat, double tntDetonationHeat, double explosiveWeight) {
            final double tntFactor = explosiveDetonationHeat / tntDetonationHeat;
            final double equivalentWeight = explosiveWeight * tntFactor;
            return new double[]{tntFactor, equivalentWeight};
        }

        /**
         * @return W = F⋅cos(θ)⋅s. The units are joules
         */
        public static double work(double forceNewtons, double angleOfForceRad, double displacementMeters) {
            return forceNewtons * Trigonometry.cos(angleOfForceRad) * displacementMeters;
        }

        /**
         * W = m×a×d
         * <br/>
         * d = t × ((v₀+v₁)/2)
         * W = F×d =(m × ((v₁-v₀)/t)) × (t × ((v₀+v₁)/2)) = m/2 × (v²₁-v²₀)
         *
         * @return W = 1/2 m(v²₁-v²₀). The units are joules
         */
        public static double workFromVelocityChange(double massKg, double initialSpeed, double finalSpeed) {
            return MathCalc.ONE_HALF * massKg * (finalSpeed * finalSpeed - initialSpeed * initialSpeed);
        }

        /**
         * @return P = W/t = (F⋅s)/t. The units are watts
         */
        public static double power(double workJoules, double timeSeconds) {
            return workJoules / timeSeconds;
        }

        /**
         * EIRP - Effective Isotropic Radiated Power
         * EIRP = Tₓ−L꜀+Gₐ
         * where:
         * <ul>
         *     <li>Tₓ — Output power of the transmitter (dBmW);</li>
         *     <li>L꜀ — Sum of cable and connectors losses (if present) (dB);</li>
         *     <li>Gₐ — Antenna gain (dBi).</li>
         * </ul>
         *
         * @return EIRP = Tx power(dBmW) − Cable loss(dB) − Connectors loss(dB) + Antenna gain(dBi). The units are dBmW
         */
        public static double eirpWithKnownTotalCableLoss(double totalCableLoss, double transmitterOutputPower,
                                                         double antennaGain, double numberOfConnectors,
                                                         double connectorLoss) {
            return transmitterOutputPower - totalCableLoss - numberOfConnectors * connectorLoss + antennaGain;
        }

        public static double eirpWithKnownCableLossPerUnitOfLength(
            double cableLoss, double cableLength, double transmitterOutputPower,
            double antennaGain, double numberOfConnectors, double connectorLoss) {
            final double totalCableLoss = cableLoss * cableLength;
            return eirpWithKnownTotalCableLoss(totalCableLoss, transmitterOutputPower, antennaGain, numberOfConnectors,
                connectorLoss);
        }

        /**
         * where:
         * v — Poisson's ratio (dimensionless);
         * ε_trans — Transverse (lateral) strain - the relative change in the dimension
         * perpendicular to the direction of force;
         * ε_axial — Axial strain - the relative change in a dimension parallel to the direction of the force.
         *
         * @return v = ε_trans/ε_axial
         */
        public static double poissonsRatio(double transverseStrain, double axialStrain) {
            return transverseStrain / axialStrain;
        }

        /**
         * @param area Area over which the force acts in m²
         * @return 𝜏 = F/A. The units are pascals
         */
        public static double shearStress(double forceNewtons, double area) {
            return forceNewtons / area;
        }

        /**
         * @return γ ≈ Δx/L
         */
        public static double shearStrain(double displacementMeters, double transverseLengthMeters) {
            return displacementMeters / transverseLengthMeters;
        }

        /**
         * @param area Area over which the force acts in m²
         * @return G = (FL)/(AΔx). The units are pascals
         */
        public static double shearModulus(
            double forceNewtons, double area, double displacementMeters, double transverseLengthMeters) {
            return (forceNewtons * transverseLengthMeters) / (area * displacementMeters);
        }

        /**
         * τ = Gγ
         *
         * @return G = τ/γ. The units are pascals
         */
        public static double shearModulusFromShearStressAndStrain(double shearStressPascals, double shearStrain) {
            return shearStressPascals / shearStrain;
        }

        /**
         * @return G = E / (2(1 + ν)). The units are pascals
         */
        public static double shearModulusFromYoungsModulus(double youngsModulus, double poissonsRatio) {
            return youngsModulus / (2 * (1 + poissonsRatio));
        }

        /**
         * where:
         * E — Young's modulus, in gigapascals (GPa);
         * G — Shear modulus, in GPa;
         * v — Poisson's ratio.
         *
         * @return E = 2×G(1+v)
         */
        public static double youngsModulus() {
            throw new UnsupportedOperationException();
        }

        /**
         * @param internalTorque N·m
         * @return ϕ = ∑(TL)/(JG). The units are rad
         */
        public static double angleOfTwist(double internalTorque, double shaftLengthMeters, double shearModulusPa) {
            final double momentOfInertia = Kinematics.polarMomentOfInertiaOfSolidCircle(shaftLengthMeters);
            return (internalTorque * shaftLengthMeters) / (momentOfInertia * shearModulusPa);
        }

        /**
         * θᵣ = arctan(μₛ)
         * θᵣ — Angle of repose;
         * μₛ — Static friction coefficient.
         *
         * @return θ = arctan(h/r). The units are rad
         */
        public static double angleOfRepose(double heapHeightMeters, double heapRadiusMeters) {
            final double staticFrictionCoeff = heapHeightMeters / heapRadiusMeters;
            return Trigonometry.tanInverse(staticFrictionCoeff);
        }

        /**
         * @return B = E / 3(1 - 2ν). The units are Pa
         */
        public static double bulkModulusFromYoungsModulus(double youngsModulus, double poissonsRatio) {
            return youngsModulus / (3 * (1 - 2 * poissonsRatio));
        }

        /**
         * @param pressurePa    Pressure applied on object (ΔP). Don't include the ambient pressure
         * @param initialVolume V₀ in m³
         * @return ΔV = −(ΔPV₀)/B. The units are m³
         */
        public static double bulkModulusChangeInVolume(double pressurePa, double initialVolume, double bulkModulusPa) {
            return -(pressurePa * initialVolume) / bulkModulusPa;
        }

        /**
         * @param initialVolume  V₀ in m³
         * @param changeInVolume in m³
         * @return ΔV/V₀
         */
        public static double bulkStrain(double initialVolume, double changeInVolume) {
            return changeInVolume / initialVolume;
        }

        /**
         * @return T = 60⋅P/(2π⋅n). The units are N⋅m
         */
        public static double shaftSizeForTwistingMomentOnly(double transmittedPowerWatts,
                                                            double shaftRotationSpeedRPM) {
            return 60 * transmittedPowerWatts / (Trigonometry.PI2 * shaftRotationSpeedRPM);
        }

        /**
         * T = π⋅τ⋅d³/16
         *
         * @param torque in N⋅m
         * @return d = ∛((16T)/(πτ)). The units are m
         */
        public static double diameterOfSolidShaftForTwistingMomentOnly(double torque, double allowableShearStressPa) {
            return Algebra.cubeRoot((16 * torque) / (Math.PI * allowableShearStressPa));
        }

        /**
         * M = π/32 × σ_b ⋅ d³₀(1−k)
         * dₒ = d/∛(1-k⁴)
         * dᵢ = d/dₒ
         *
         * @return The units are m
         */
        public static double[] shaftSizeDiametersForTwistingOrBendingMoment(
            double diameterOfSolidShaftMeters, double diameterRatio) {
            final double outer = diameterOfSolidShaftMeters / Algebra.cubeRoot(1 - Math.pow(diameterRatio, 4));
            final double inner = diameterRatio * outer;
            return new double[]{outer, inner};
        }

        /**
         * M = π/32 × σ_b ⋅ d³₀(1−k⁴)
         *
         * @param bendingMoment in N⋅m
         * @return d = ∛((M*32)/(π*σ_b)). The units are m
         */
        public static double shaftSizeForBendingMomentOnly(double bendingMoment, double allowableBendingStressPa) {
            return Algebra.cubeRoot((32 * bendingMoment) / (Math.PI * allowableBendingStressPa));
        }

        /**
         * @param bendingMoment in N⋅m
         * @param torque        in N⋅m
         * @return Tₑ = √(M²+T²) = π/16 × τₘₐₓ ⋅ d³₀(1−k⁴). The units are N⋅m
         */
        public static double equivalentTwistingMoment(double bendingMoment, double torque) {
            return squareRoot(bendingMoment * bendingMoment + torque * torque);
        }

        /**
         * @param bendingMoment in N⋅m
         * @param torque        in N⋅m
         * @return Tₑ = 1/2(M+√(M²+T²)) = π/32 × σ_b(ₘₐₓ) ⋅ d³₀(1−k⁴). The units are N⋅m
         */
        public static double equivalentBendingMoment(double bendingMoment, double torque) {
            return MathCalc.ONE_HALF * (bendingMoment + squareRoot(bendingMoment * bendingMoment + torque * torque));
        }

        /**
         * @param bendingMoment in N⋅m
         * @param torque        in N⋅m
         * @return Tₑ = √((Kₘ⋅M)² + (Kₜ⋅T)²). The units are N⋅m
         */
        public static double fluctuatingEquivalentTwistingMoment(
            double torque, double bendingMoment, double bendingFactor, double torsionFactor) {
            return squareRoot(Math.pow(bendingFactor * bendingMoment, 2) + Math.pow(torsionFactor * torque, 2));
        }

        /**
         * @param bendingMoment in N⋅m
         * @param torque        in N⋅m
         * @return Mₑ = 1/2(Kₘ⋅M+√((Kₘ⋅M)²+(Kₜ⋅T)²)). The units are N⋅m
         */
        public static double fluctuatingEquivalentBendingMoment(
            double torque, double bendingMoment, double bendingFactor, double torsionFactor) {
            return MathCalc.ONE_HALF * (bendingFactor * bendingMoment
                + squareRoot(Math.pow(bendingFactor * bendingMoment, 2) + Math.pow(torsionFactor * torque, 2)));
        }

        /**
         * For a solid circular shaft: T = (G⋅θ)/L × π/32×d⁴.
         * For a hollow circular shaft: T = (G⋅θ)/L × π/32 × (d⁴ₒ−d⁴ᵢ).
         *
         * @param angleRad Torsional deflection or angle of twist (θ)
         * @return d = ⁴√((32⋅T⋅L)/(G⋅π⋅θ)). The units are m
         */
        public static double shaftSizeDiameterForTorsionalRigidity(
            double torque, double rigidityModulusPa, double angleRad, double lengthMeters) {
            final double numerator = 32 * torque * lengthMeters;
            final double denominator = rigidityModulusPa * Math.PI * angleRad;
            return Algebra.nthRoot(numerator / denominator, 4);
        }
    }

    public static final class FluidMechanics {
        public static final double STD_SEAWATER_SALINITY = 0.035; // 35‰

        private FluidMechanics() {
        }

        /**
         * @return p = ρ × v² / 2. The units are pascals
         */
        public static double dynamicPressure(double forceNewtons, double areaSquareMeters) {
            return forceNewtons / areaSquareMeters;
        }

        /**
         * @return CFM = (P_hp × efficiency * 6356) / ΔP. The units are cu ft/min
         */
        public static double fanMassAirflowInCFM(double powerOutputHp, double pressureInH2O, double efficiency) {
            return (powerOutputHp * efficiency * 6356) / pressureInH2O;
        }

        /**
         * @return h_g = (h₁-h₂)/L = Δh/L
         */
        public static double hydraulicGradient(
            double headAtPoint1Meters, double headAtPoint2Meters, double distanceMeters) {
            return (headAtPoint1Meters - headAtPoint2Meters) / distanceMeters;
        }

        /**
         * where:
         * v — The terminal velocity;
         * g — The acceleration due to gravity;
         * d — The diameter of the sphere;
         * μ — The dynamic viscosity of the fluid;
         * ρp and ρm — Respectively the particle and the medium density.
         *
         * @return v = g × d² × (ρp - ρm)/(18 × μ). The units are m/s
         */
        public static double stokesLaw(
            double accelerationOfGravity, double mediumViscosity, double mediumDensity,
            double particleDensity, double particleDiameter) {
            return accelerationOfGravity * (particleDiameter * particleDiameter)
                * (particleDensity - mediumDensity) / (18 * mediumViscosity);
        }

        /**
         * @return F₂ = (A₂/A₁)*F₁. The units are Pa
         */
        public static double hydraulicPressure(double pistonForceNewtons, double pistonAreaMeters) {
            return pistonForceNewtons / pistonAreaMeters;
        }

        /**
         * @return F₁ = (A₁/A₂)*F₂. The units are N
         */
        public static double hydraulicPressurePistonForce(
            double pistonAreaMeters, double pistonArea2Meters, double secondPistonForceNewtons) {
            return (pistonAreaMeters / pistonArea2Meters) * secondPistonForceNewtons;
        }

        /**
         * @return F₂ = (A₂/A₁)*F₁. The units are N
         */
        public static double hydraulicPressureSecondPistonForce(
            double pistonAreaMeters, double pistonArea2Meters, double pistonForceNewtons) {
            return (pistonArea2Meters / pistonAreaMeters) * pistonForceNewtons;
        }

        /**
         * d₁ = F₂/(F₁d₂)
         * d₁ = A₂/(A₁d₂)
         *
         * @return W = F₁d₁ = F₂d₂. The units are J
         */
        public static double hydraulicPressureLiftingDistanceWorkDone(
            double pistonForceNewtons, double liftingDistanceMeters) {
            return pistonForceNewtons * liftingDistanceMeters;
        }

        /**
         * @return The units are kg/m³
         */
        public static double waterDensity(double tempCelsius, double salinityPerMille) {
            // Pure water density at t (kg/m³)
            final double t = tempCelsius;
            final double s = salinityPerMille;
            final double tSquared = t * t;
            final double tCubed = t * t * t;
            final double tQuadruple = t * t * t * t;
            final double rhoW = 999.842594 + 6.793952e-2 * t - 9.095290e-3 * tSquared
                + 1.001685e-4 * tCubed - 1.120083e-6 * tQuadruple + 6.536332e-9 * tQuadruple * t;
            // Seawater density at atmospheric pressure (UNESCO 1983)
            return rhoW + (0.824493 - 0.0040899 * t + 0.000076438 * tSquared
                - 0.00000082467 * tCubed + 0.0000000053875 * tQuadruple) * s
                + (-0.00572466 + 0.00010227 * t - 0.0000016546 * tSquared) * Math.pow(s, 1.5)
                + 0.00048314 * s * s;
        }

        /**
         * S = m₁ / (m₁ + m₀)
         * where m₀ is the mass of pure water and m₁ is the mass of salt.
         *
         * @return The units are kg
         */
        public static double massOfWater(double massOfSaltKg) {
            return (massOfSaltKg / STD_SEAWATER_SALINITY) - massOfSaltKg;
        }

        /**
         * @return The units are kg
         */
        public static double massOfSalt(double massOfWaterKg) {
            return massOfWaterKg * STD_SEAWATER_SALINITY;
        }

        public static boolean sinkInWater(double waterDensity, double objectDensity) {
            return objectDensity > waterDensity;
        }

        /**
         * @return Pr = Momentum transport/Thermal (or heat) transport = ν/α
         */
        public static double prandtlNumber() {
            throw new UnsupportedOperationException();
        }
    }

    public static final class Statics {
        private Statics() {
        }

        /**
         * @return p = F / A. The units are pascals
         */
        public static double pressure(double forceNewtons, double areaSquareMeters) {
            return forceNewtons / areaSquareMeters;
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
            return massInKg * GRAVITATIONAL_ACCELERATION_ON_EARTH + outsideForce
                * Trigonometry.sin(outsideForceAngleRad);
        }

        /**
         * @return Fₙ = m ⋅ g − F ⋅ sin(x). The units are Newtons
         */
        public static double normalForceWithHorizontalSurfaceAndUpwardExternalForce(
            double massInKg, double outsideForce, double outsideForceAngleRad) {
            return massInKg * GRAVITATIONAL_ACCELERATION_ON_EARTH - outsideForce
                * Trigonometry.sin(outsideForceAngleRad);
        }

        /**
         * @return Fₙ = m ⋅ g. The units are Newtons
         */
        public static double normalForceWithHorizontalSurface(double massInKg) {
            return massInKg * GRAVITATIONAL_ACCELERATION_ON_EARTH;
        }

        /**
         * @return Fₙ = m ⋅ g ⋅ cos(α). The units are Newtons
         */
        public static double normalForceWithInclinedSurface(double massInKg, double inclinationAngleRad) {
            return massInKg * GRAVITATIONAL_ACCELERATION_ON_EARTH * Trigonometry.cos(inclinationAngleRad);
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
            resultantForce[Constants.ARR_3RD_INDEX] = squareRoot(fx * fx + fy * fy);
            // Direction (θ)
            resultantForce[Constants.ARR_4TH_INDEX] = Trigonometry.multivaluedTanInverse(fy, fx);
            return resultantForce;
        }

        /**
         * where:
         * <ul>
         *     <li>E — Bullet's kinetic energy;</li>
         *     <li>m — Mass of the bullet;</li>
         *     <li>v — Velocity of the bullet.</li>
         * </ul>
         *
         * @param bulletVelocity in m/s
         * @return E = 1/2⋅m⋅v². The units are joules
         */
        public static double bulletEnergy(double bulletMassKg, double bulletVelocity) {
            return MathCalc.ONE_HALF * bulletMassKg * bulletVelocity * bulletVelocity;
        }

        /**
         * F = m×(v₁−v₀)/t
         *
         * @param acceleration in m/s²
         * @return F = m⋅a or F = m⋅g at a constant speed
         */
        public static double force(double massInKg, double acceleration) {
            return massInKg * acceleration;
        }

        /**
         * where:
         * <ul>
         *     <li>F — Gravitational force, measured in newtons (N) (our force converter can convert it
         *     to other units). It is always positive, which means that two objects of a certain mass always
         *     attract (and never repel) each other;</li>
         *     <li>M and m — Masses of two objects in question, in kilograms (kg);</li>
         *     <li>R — Distance between the centers of these two objects, in meters (m);</li>
         *     <li>G — Gravitational constant. It is equal to 6.674×10⁻¹¹ N·m²/kg².</li>
         * </ul>
         *
         * @return F = GMm/R². The units are newtons
         */
        public static double gravitationalForce(double massKg1, double massKg2, double distanceMeters) {
            return (GRAVITATIONAL_CONSTANT * massKg1 * massKg2) / (distanceMeters * distanceMeters);
        }
    }

    public static final class Electromagnetism {
        public static final Map<String, Double> CONDUCTIVITY_MAP; // in S/m
        public static final Map<String, Double> RESISTIVITY_MAP; // in S/m

        static {
            CONDUCTIVITY_MAP = Map.ofEntries(
                Map.entry("Ag", 62_893_082.0), // Silver
                Map.entry("Annealed Cu", 58_479_532.0), // Annealed copper
                Map.entry("Au", 40_983_607.0), // Gold
                Map.entry("Al", 37_735_849.0), // Aluminum
                Map.entry("W", 17_857_143.0), // Tungsten
                Map.entry("Li", 10_775_862.0), // Lithium
                Map.entry("Fe", 10_298_661.0), // Iron
                Map.entry("Pt", 9_433_962.0), // Platinum
                Map.entry("Hg", 1_020_408.0), // Mercury
                Map.entry("C", 1_538.5), // Carbon
                Map.entry("Si", 0.0015625), // Silicon
                Map.entry("SiO2", 1e-13), // Glass
                Map.entry("C2F4", 1e-24), // Teflon
                Map.entry("Cu", 59_523_810.0) // Copper
            );

            RESISTIVITY_MAP = Map.ofEntries(
                Map.entry("Ag", 1.59e-8),
                Map.entry("Annealed Cu", 1.71e-8),
                Map.entry("Au", 2.44e-8),
                Map.entry("Al", 2.65e-8),
                Map.entry("W", 5.6e-8),
                Map.entry("Li", 9.28e-8),
                Map.entry("Fe", 9.71e-8),
                Map.entry("Pt", 1.06e-7),
                Map.entry("Hg", 9.8e-7),
                Map.entry("C", 0.00065),
                Map.entry("Si", 640.0),
                Map.entry("SiO2", 10_000_000_000_000d),
                Map.entry("C2F4", 1e+24),
                Map.entry("Cu", 1.68e-8)
            );
        }

        private Electromagnetism() {
        }

        public static double conductivityOf(String chemicalSymbol) {
            return CONDUCTIVITY_MAP.get(chemicalSymbol);
        }

        public static double resistivityOf(String chemicalSymbol) {
            return RESISTIVITY_MAP.get(chemicalSymbol);
        }

        /**
         * ρ = 1/σ
         *
         * @param conductivity in S/m
         * @return resistivity in Ω*m
         */
        public static double conductivityToResistivity(double conductivity) {
            return reciprocal(conductivity);
        }

        /**
         * σ = 1/ρ
         *
         * @param resistivity in Ω*m
         * @return conductivity in S/m
         */
        public static double resistivityToConductivity(double resistivity) {
            return reciprocal(resistivity);
        }

        /**
         * P = I × V
         * In AC circuits: P = I × V × pf, where pf is the power factor.
         *
         * @return power. The units are watts
         */
        public static double electricalPower(double voltageVolts, double currentAmperes, double powerFactor) {
            return currentAmperes * voltageVolts * powerFactor;
        }

        /**
         * @param electricFieldStrength in N/C
         * @return u = ε₀/2 * E² + 1/(2μ₀) * B². The units are J/m³
         */
        public static double energyDensityOfFields(double electricFieldStrength, double magneticFieldTesla) {
            checkGreater0(electricFieldStrength);
            checkGreater0(magneticFieldTesla);

            final double eSquared = electricFieldStrength * electricFieldStrength;
            final double bSquared = magneticFieldTesla * magneticFieldTesla;
            return (VACUUM_PERMITTIVITY / 2) * eSquared + reciprocal(2 * VACUUM_PERMEABILITY) * bSquared;
        }

        /**
         * @param energyDensity in J/m³
         * @return The units are N/C
         */
        public static double electricFieldStrength(double energyDensity, double magneticFieldTesla) {
            checkGreater0(energyDensity);
            checkGreater0(magneticFieldTesla);

            final double magneticFieldSquared = magneticFieldTesla * magneticFieldTesla;
            final double term = energyDensity - reciprocal(2 * VACUUM_PERMEABILITY) * magneticFieldSquared;
            return squareRoot(2 * term / VACUUM_PERMITTIVITY);
        }

        /**
         * B = μ₀I/2πd
         * where:
         * <ul>
         *     <li>I - Current flowing through the wire;</li>
         *     <li>d - Distance from the wire;</li>
         *     <li>B - Strength of the magnetic field produced at distance d</li>
         *     <li>μ₀ is the permeability of free space</li>
         * </ul>
         *
         * @return The units are tesla
         */
        public static double magneticField(double currentAmperes, double distanceMeters) {
            return (VACUUM_PERMEABILITY * currentAmperes) / (Trigonometry.PI2 * distanceMeters);
        }

        /**
         * @return I = (2πdB)/μ₀. The units are tesla
         */
        public static double magneticFieldInStraightWire(double distanceMeters, double magneticFieldTesla) {
            return (Trigonometry.PI2 * distanceMeters * magneticFieldTesla) / VACUUM_PERMEABILITY;
        }

        /**
         * where:
         * <ul>
         *     <li>ε — Dielectric permittivity (a measure of resistance) in farads per meter;</li>
         *     <li>A — Area where the plates overlap;</li>
         *     <li>s — Separation distance between the plates.</li>
         * </ul>
         *
         * @return C = (εA)/s. The units are farads
         */
        public static double capacitance(double area, double permittivity, double separationDistance) {
            return (permittivity * area) / separationDistance;
        }

        /**
         * where:
         * <ul>
         *     <li>ε — absolute permittivity;</li>
         *     <li>A — area of the plates which have identical sizes;</li>
         *     <li>d — distance between the plates.</li>
         * </ul>
         *
         * @return C = ε · A/d. The units are farads
         */
        public static double capacitanceInParallelPlateCapacitor(double area, double permittivity, double distance) {
            return permittivity * (area / distance);
        }

        /**
         * where:
         * <ul>
         *     <li>a – Acceleration of the particle;</li>
         *     <li>q – Charge of the particle;</li>
         *     <li>m – Mass of the particle;</li>
         *     <li>E – Electric field.</li>
         * </ul>
         *
         * @param charge        in coulombs
         * @param electricField in N/C
         * @return a = (qE)/m. The units are m/s²
         */
        public static double accelerationInElectricField(double massGrams, double charge, double electricField) {
            return (charge * electricField) / massGrams;
        }

        /**
         * Z = R ± j × X
         * where:
         * <ul>
         *     <li>X - the reactance</li>
         *     <li>R - the resistance</li>
         *     <li>Z - an impedance</li>
         *     <li>j = √-1</li>
         * </ul>
         * <p/>
         * X = 1 / (2 × π × f × C)
         * ω = 2 × π × f
         *
         * @return X = 1 / (ω × C). The units are ohms
         */
        public static double capacitiveReactance(double capacitanceFarads, double frequencyHz) {
            final double angularFrequency = Trigonometry.PI2 * frequencyHz;
            return reciprocal(angularFrequency * capacitanceFarads);
        }

        /**
         * where:
         * <ul>
         *     <li>F is the electrostatic force between charges (in Newtons)</li>
         *     <li>q₁ is the magnitude of the first charge (in Coulombs)</li>
         *     <li>q₂ is the magnitude of the second charge (in Coulombs)</li>
         *     <li>r is the shortest distance between the charges (in m)</li>
         *     <li>kₑ is the Coulomb's constant</li>
         * </ul>
         *
         * @return F = (kₑq₁q₂)/r². The units are newtons
         */
        public static double coulombsLaw(double charge1, double charge2, double distanceMeters) {
            return (COULOMB_CONSTANT * charge1 * charge2) / (distanceMeters * distanceMeters);
        }

        /**
         * Gain (dB) = 20 log₁₀(Output voltage/Input voltage)
         * Gain (dB) = 20 log₁₀(1) = 0
         * 1√2 = 0.707
         * Gain (dB) = 20 log₁₀(0.707) = -3 dB
         *
         * @return f꜀ = 1/(2πRC). The units are Hz
         */
        public static double cutoffFrequencyRCFilter(double resistanceOhms, double capacitanceFarads) {
            return reciprocal(Trigonometry.PI2 * resistanceOhms * capacitanceFarads);
        }

        /**
         * @return f꜀ = R/(2πL). The units are Hz
         */
        public static double cutoffFrequencyRLFilter(double resistanceOhms, double inductanceHenries) {
            return resistanceOhms / (Trigonometry.PI2 * inductanceHenries);
        }

        /**
         * where:
         * <ul>
         *     <li>f – Cyclotron frequency;</li>
         *     <li>q – Charge of the particle;</li>
         *     <li>B – Strength of the magnetic field;</li>
         *     <li>m – Mass of the particle.</li>
         * </ul>
         *
         * @return f = (qB)/(2m). The units are Hz
         */
        public static double cyclotronFrequency(
            double chargeCoulombs, double magneticFieldStrengthTesla, double massKg) {
            return (chargeCoulombs * magneticFieldStrengthTesla) / (Trigonometry.PI2 * massKg);
        }

        /**
         * @param velocity in m/s
         * @return F = qvBsin(α). The units are newtons
         */
        public static double lorentzForce(
            double magneticFieldTesla, double chargeCoulombs, double velocity, double angleRad) {
            return chargeCoulombs * velocity * magneticFieldTesla * Trigonometry.sin(angleRad);
        }

        /**
         * @return P = PF × I × U. The units are watts
         */
        public static double acWattageSinglePhase(double voltageVolts, double currentAmp, double powerFactor) {
            return powerFactor * currentAmp * voltageVolts;
        }

        /**
         * @return P = √3 × PF × I × V. The units are watts
         */
        public static double acWattage3PhaseL2L(double voltageVolts, double currentAmp, double powerFactor) {
            return squareRoot(3) * acWattageSinglePhase(voltageVolts, currentAmp, powerFactor);
        }

        /**
         * @return P = 3 × PF × I × V. The units are watts
         */
        public static double acWattage3PhaseL2N(double voltageVolts, double currentAmp, double powerFactor) {
            return 3 * acWattageSinglePhase(voltageVolts, currentAmp, powerFactor);
        }

        /**
         * @return E = (kQ)/r². The units are newton/coulomb
         */
        public static double electricField(double chargeCoulombs, double distanceMeters) {
            return (COULOMB_CONSTANT * chargeCoulombs) / (distanceMeters * distanceMeters);
        }

        /**
         * {@link #COULOMB_CONSTANT}
         *
         * @return 1/(4πε₀)
         */
        private static double permittivity(double relativePermittivity) {
            return reciprocal(Trigonometry.PI4 * VACUUM_PERMITTIVITY * relativePermittivity);
        }

        /**
         * @return E = (kQ)/r². The units are newton/coulomb
         */
        public static double electricField(
            double chargeCoulombs, double distanceMeters, double relativePermittivity) {
            return (permittivity(relativePermittivity) * chargeCoulombs) / (distanceMeters * distanceMeters);
        }

        /**
         * @return V = k * q/r. The units are volts
         */
        public static double electricPotential(double chargeCoulombs, double distanceMeters) {
            return COULOMB_CONSTANT * chargeCoulombs / distanceMeters;
        }

        public static double electricPotential(
            double chargeCoulombs, double distanceMeters, double relativePermittivity) {
            return permittivity(relativePermittivity) * chargeCoulombs / distanceMeters;
        }

        /**
         * where:
         * W_AB - two arbitrary points, A and B, then the work done;
         * ΔU - the change in the potential energy when the charge q moves from A to B.
         *
         * @return W_AB = ΔU = (V_A − V_B)q. The units are volts
         */
        public static double electricPotentialDifference(double chargeCoulombs, double electricPotentialEnergyJoules) {
            return electricPotentialEnergyJoules / chargeCoulombs;
        }

        /**
         * @param crossSectionalArea in m²
         * @return Φ = B * A * cos(θ). The units are weber
         */
        public static double faradayLawMagneticFlux(
            double crossSectionalArea, double turns, double magneticFieldTesla) {
            return magneticFieldTesla * crossSectionalArea * Trigonometry.cos(AngleUnit.turnsToRadians(turns));
        }

        /**
         * @return ε = −N * dΦ/dt. The units are volts
         */
        public static double faradayLawInducedVoltage(double magneticFluxWeber, double turns, double timeSeconds) {
            return -turns * (magneticFluxWeber / timeSeconds);
        }

        /**
         * f₀ = √(fᵤ⋅fₗ)
         * where:
         * fᵤ — Upper cutoff frequency;
         * fₗ — Lower cutoff frequency.
         * <br/>
         * f_BW = fᵤ-fₗ
         *
         * @return f_BW = f₀/Q. The units are Hz
         */
        public static double frequencyBandwidth(double centerFrequencyHz, double qualityFactor) {
            return centerFrequencyHz / qualityFactor;
        }

        /**
         * @return fₗ = f₀(√(1 + 1/(4Q²)) − 1/(2Q)). The units are Hz
         */
        public static double lowerCutoffFrequency(double centerFrequencyHz, double qualityFactor) {
            return centerFrequencyHz * (squareRoot(1 + reciprocal(4 * qualityFactor * qualityFactor))
                - reciprocal(2 * qualityFactor));
        }

        /**
         * @return fᵤ = f₀(√(1 + 1/(4Q²)) + 1/(2Q)). The units are Hz
         */
        public static double upperCutoffFrequency(double centerFrequencyHz, double qualityFactor) {
            return centerFrequencyHz * (squareRoot(1 + reciprocal(4 * qualityFactor * qualityFactor))
                + reciprocal(2 * qualityFactor));
        }

        /**
         * @return P = I²R₁ + I²R₂ + ... + I²Rₙ. The units are watts
         */
        public static double powerDissipationInSeries(double voltageVolts, double[] resistors) {
            final double totalResistance = Electronics.equivalentResistanceInSeries(resistors);
            final double totalCurrent = Electronics.ohmsLawCurrent(voltageVolts, totalResistance);
            return Electronics.ohmsLawPowerGivenResistanceAndCurrent(totalResistance, totalCurrent);
        }

        /**
         * R_eq = 1/R₁ + 1/R₂ + ... + 1/Rₙ
         *
         * @return P = V²R_eq. The units are watts
         */
        public static double powerDissipationInParallel(double voltageVolts, double[] resistors) {
            final double totalResistance = Electronics.equivalentResistanceInParallel(resistors);
            return Electronics.ohmsLawPowerGivenVoltageAndResistance(voltageVolts, totalResistance);
        }

        /**
         * @return p = q × d. The units are C·m
         */
        public static double dipoleMoment(double distanceBetweenCharges, double chargeCoulombs) {
            return chargeCoulombs * distanceBetweenCharges;
        }

        /**
         * @return p(r) = ∑ⁿᵢ₌₁ qᵢ(rᵢ−r) = q₁(r₁−r) + q₂(r₂−r) + q₃(r₃−r)
         */
        public static double[] dipoleMomentSystemOfCharges(
            double[] referencePoint, double[] charges, double[][] chargeCoordinates) {
            final double[] results = new double[charges.length];
            for (int i = 0; i < charges.length; i++) {
                final double[] refDiff = LinearAlgebra.vectorSubtract(chargeCoordinates[i], referencePoint);
                for (int j = 0; j < charges.length; j++) {
                    results[j] += charges[i] * refDiff[j];
                }
            }
            return results;
        }

        /**
         * @return ϕ = Q/ε₀. The units are V·m
         */
        public static double gaussLaw(double electricChargeCoulombs) {
            return electricChargeCoulombs / VACUUM_PERMITTIVITY;
        }

        /**
         * @param electricFlux in V·m
         * @return Q = ϕ⋅ε₀. The units are C
         */
        public static double gaussLawCharge(double electricFlux) {
            return electricFlux * VACUUM_PERMITTIVITY;
        }

        /**
         * χ = μ/μ₀ − 1 = μᵣ−1
         * μ₀ = 4π10⁻⁷ H/m is the magnetic permeability of free space.
         *
         * @return The units are H/m
         */
        public static double magneticPermeability(double relativePermeability) {
            return VACUUM_PERMEABILITY * relativePermeability;
        }

        public static double magneticRelativePermeability(double susceptibility) {
            return 1 + susceptibility;
        }

        public static double magneticSusceptibility(double relativePermeability) {
            return relativePermeability - 1;
        }

        /**
         * RH = - 1/(n × q)
         * where:
         * n [1/m³] — Concentration of the carriers;
         * q [C] — Charge of a single carrier.
         *
         * @return RH = V × t / (I × B). The units are m³/C
         */
        public static double hallCoefficient(
            double voltageVolts, double thicknessMeters, double currentAmps, double magneticFieldTesla) {
            return voltageVolts * thicknessMeters / (currentAmps * magneticFieldTesla);
        }

        /**
         * @param area          Cross-sectional area (A) of a wire in m²
         * @param numberDensity n – Charge carrier number density in x10²⁸ carriers/m³
         * @return u = I/(nAq). The units are m/s
         */
        public static double driftVelocity(
            double currentAmps, double area, double numberDensity, double chargeCoulombs) {
            return currentAmps / (numberDensity * area * chargeCoulombs);
        }

        /**
         * τ = μ×B
         *
         * @return μ = I⋅A. The units are A⋅m²
         */
        public static double magneticDipoleMoment(double currentAmps, double loopLengthMeters) {
            final double area = Geometry.circleAreaOfCircumference(loopLengthMeters);
            return currentAmps * area;
        }

        /**
         * @return μ_solenoid = N⋅I⋅A. The units are A⋅m²
         */
        public static double solenoidMagneticDipoleMoment(
            double currentAmps, double solenoidRadiusMeters, double numberOfTurns) {
            final double interiorArea = Geometry.circleArea(solenoidRadiusMeters);
            return numberOfTurns * currentAmps * interiorArea;
        }

        /**
         * @return I = |μ|/(N⋅A). The units are A
         */
        public static double currentFromMagneticDipoleMoment(double moment, double areaMeters, double numberOfTurns) {
            return Math.abs(moment) / (numberOfTurns * areaMeters);
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
            return squareRoot(power * resistance);
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
         * @return P = V²/R. The units are watts
         */
        public static double ohmsLawPowerGivenVoltageAndResistance(double voltage, double resistance) {
            return (voltage * voltage) / resistance;
        }

        /**
         * @return P = R*I². The units are watts
         */
        public static double ohmsLawPowerGivenResistanceAndCurrent(double resistance, double current) {
            return resistance * (current * current);
        }

        /**
         * Alternatives:
         * <br/>I = dq/dt
         * <br/>I = C * (dv/dt)
         *
         * @return I = V/R. The units are amperes
         */
        public static double ohmsLawCurrent(double voltage, double resistance) {
            return voltage / resistance;
        }

        /**
         * @return I = P/V. The units are amperes
         */
        public static double ohmsLawCurrentGivenPowerAndVoltage(double power, double voltage) {
            return power / voltage;
        }

        /**
         * @return I = √(P/V). The units are amperes
         */
        public static double ohmsLawCurrentGivenPowerAndResistance(double power, double resistance) {
            return squareRoot(power / resistance);
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
        public static double apparentPowerACSinglePhase(double currentAmperes, double voltageVolts) {
            return apparentPowerAC(1, currentAmperes, voltageVolts);
        }

        /**
         * @return V⋅I⋅P_F/1000. The units are kW
         */
        public static double acPowerSinglePhase(double currentAmperes, double voltageVolts, double powerFactor) {
            return acPower(1, currentAmperes, voltageVolts, powerFactor);
        }

        /**
         * @return V⋅I⋅P_F⋅η/746. The units are hp
         */
        public static double motorOutputHorsepowerACSinglePhase(
            double currentAmperes, double voltageVolts, double powerFactor, double efficiency) {
            return motorOutputHorsepowerAC(1, currentAmperes, voltageVolts, powerFactor, efficiency);
        }

        /**
         * @return V⋅I/1000. The units are kVA
         */
        public static double apparentPowerACThreePhase(double currentAmperes, double voltageVolts) {
            return apparentPowerAC(THREE_PHASE_GENERATOR, currentAmperes, voltageVolts);
        }

        /**
         * @return V⋅I⋅P_F/1000. The units are kW
         */
        public static double acPowerThreePhase(double currentAmperes, double voltageVolts, double powerFactor) {
            return acPower(THREE_PHASE_GENERATOR, currentAmperes, voltageVolts, powerFactor);
        }

        /**
         * @return V⋅I⋅P_F⋅η/746. The units are hp
         */
        public static double motorOutputHorsepowerACThreePhase(
            double currentAmperes, double voltageVolts, double powerFactor, double efficiency) {
            return motorOutputHorsepowerAC(
                THREE_PHASE_GENERATOR, currentAmperes, voltageVolts, powerFactor, efficiency);
        }

        public static double apparentPowerAC(double phase, double currentAmperes, double voltageVolts) {
            return phase * voltageVolts * currentAmperes / 1000;
        }

        /**
         * AC - Alternating Current
         */
        public static double acPower(double phase, double currentAmperes, double voltageVolts, double powerFactor) {
            return phase * Electromagnetism.electricalPower(voltageVolts, currentAmperes, powerFactor) / 1000;
        }

        public static double motorOutputHorsepowerAC(
            double phase, double currentAmperes, double voltageVolts, double powerFactorPercent, double efficiency) {
            return phase * voltageVolts * currentAmperes * powerFactorPercent * efficiency / HORSEPOWER;
        }

        /**
         * @return V⋅I⋅P_F/1000. The units are kW
         */
        public static double powerDirectCurrent(double currentAmperes, double voltageVolts) {
            return currentAmperes * voltageVolts / 1000;
        }

        public static double motorOutputHorsepowerDirectCurrent(
            double currentAmperes, double voltageVolts, double efficiency) {
            return voltageVolts * currentAmperes * efficiency / HORSEPOWER;
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
         * @return R_eq = R₁ + R₂ + ... + Rₙ. The units are Ω
         */
        public static double equivalentResistanceInSeries(double[] resistors) {
            return Arrays.stream(resistors).sum();
        }

        public static double missingResistorInSeries(double[] resistors, double desiredTotalResistance) {
            return desiredTotalResistance - equivalentResistanceInSeries(resistors);
        }

        /**
         * @param resistors in Ohms (Ω)
         * @return R = 1/R₁ + 1/R₂ + ... + 1/Rₙ. The units are Ω
         */
        public static double equivalentResistanceInParallel(double[] resistors) {
            return reciprocal(Arrays.stream(resistors).map(Arithmetic::reciprocal).sum());
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
            final double resistorNoise = squareRoot(
                4 * resistanceOhms * BOLTZMANN_CONSTANT * temperatureKelvins * bandwidthHz);
            final double noiseLevelLu = 20 * log(resistorNoise / REF_VOLTAGE_FOR_0_DBU);
            final double noiseLevelLv = 20 * log(resistorNoise);
            return new double[]{resistorNoise, noiseLevelLu, noiseLevelLv};
        }

        /**
         * Peak voltage (Vₚ)
         *
         * @return Vᵣₘₛ = Vₚ/√2
         */
        public static double rmsVoltageSineWaveVp(double voltageVolts) {
            return voltageVolts / squareRoot(2);
        }

        /**
         * Peak-to-peak voltage (Vₚₚ)
         *
         * @return Vᵣₘₛ = Vₚₚ/(2√2)
         */
        public static double rmsVoltageSineWaveVpp(double voltageVolts) {
            return voltageVolts / (2 * squareRoot(2));
        }

        /**
         * Average voltage (Vₐᵥ₉)
         *
         * @return Vᵣₘₛ = πVₐᵥ₉/(2√2)
         */
        public static double rmsVoltageSineWaveVavg(double voltageVolts) {
            return Math.PI * voltageVolts / (2 * squareRoot(2));
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
            return voltageVolts / squareRoot(3);
        }

        /**
         * @return Vᵣₘₛ = Vₚₚ/(2√3)
         */
        public static double rmsVoltageTriangleWaveVpp(double voltageVolts) {
            return voltageVolts / (2 * squareRoot(3));
        }

        /**
         * @return Vᵣₘₛ = πVₐᵥ₉/(2√3)
         */
        public static double rmsVoltageTriangleWaveVavg(double voltageVolts) {
            return (Math.PI * voltageVolts) / (2 * squareRoot(3));
        }

        /**
         * @return Vᵣₘₛ = Vₚ/√3
         */
        public static double rmsVoltageSawtoothWaveVp(double voltageVolts) {
            return voltageVolts / squareRoot(3);
        }

        /**
         * @return Vᵣₘₛ = Vₚₚ/(2√3)
         */
        public static double rmsVoltageSawtoothWaveVpp(double voltageVolts) {
            return voltageVolts / (2 * squareRoot(3));
        }

        /**
         * @return Vᵣₘₛ = πVₐᵥ₉/(2√3)
         */
        public static double rmsVoltageSawtoothWaveVavg(double voltageVolts) {
            return (Math.PI * voltageVolts) / (2 * squareRoot(3));
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
            return voltageVolts / squareRoot(2);
        }

        /**
         * @return Vᵣₘₛ = Vₚₚ/(2√2)
         */
        public static double rmsVoltageFullWaveRectifiedSineWaveVpp(double voltageVolts) {
            return voltageVolts / (2 * squareRoot(2));
        }

        /**
         * @return Vᵣₘₛ = πVₐᵥ₉/(2√2)
         */
        public static double rmsVoltageFullWaveRectifiedSineWaveVavg(double voltageVolts) {
            return Math.PI * voltageVolts / (2 * squareRoot(2));
        }

        /**
         * @return R = ρ × L / A. The units are ohms
         */
        public static double wireResistance(
            double lengthMeters, double diameterMeters, double electricalResistivity) {
            final double crossSectionalArea = crossSectionalAreaOfCircularWire(diameterMeters);
            return electricalResistivity * lengthMeters / crossSectionalArea;
        }

        /**
         * @return R = 1 / G. The units are ohms
         */
        public static double wireResistance(double conductanceSiemens) {
            return reciprocal(conductanceSiemens);
        }

        /**
         * @param electricalConductivity in S/m
         * @param crossSectionalArea     in m²
         * @return G = σ × A / L. The units are siemens
         */
        public static double wireConductance(
            double electricalConductivity, double crossSectionalArea, double lengthMeters) {
            return electricalConductivity * crossSectionalArea / lengthMeters;
        }

        /**
         * @return τ = R×C. The units are seconds
         */
        public static double capacitorChargeTimeConstant(double resistanceOhms, double capacitanceFarads) {
            return resistanceOhms * capacitanceFarads;
        }

        /**
         * <table>
         *     <tr><th>Time</th><th>Charged in percentage (%)</th></tr>
         *     <tr><td>1τ</td><td>63.2</td></tr>
         *     <tr><td>2τ</td><td>86.5</td></tr>
         *     <tr><td>3τ</td><td>95.0</td></tr>
         *     <tr><td>4τ</td><td>98.2</td></tr>
         *     <tr><td>5τ</td><td>99.3</td></tr>
         * </table>
         * <h6>Discharge<h6/>
         * <table>
         *     <tr><th>Time</th><th>Charged in percentage (%)</th></tr>
         *     <tr><td>1τ</td><td>36.8</td></tr>
         *     <tr><td>2τ</td><td>13.5</td></tr>
         *     <tr><td>3τ</td><td>5</td></tr>
         *     <tr><td>4τ</td><td>1.8</td></tr>
         *     <tr><td>5τ</td><td>0.7</td></tr>
         * </table>
         * <ul>
         *     <li>Percentage = 1−e^(−T/τ)</li>
         *     <li>1−e^(-5τ/τ) = 1−e⁻⁵ ≈ 99.3%</li>
         *     <li>Percentage = 1−e−ᴹᵀᶜ</li>
         * </ul>
         * T = 5×τ = 5×R×C
         * where:
         * T — Charge time (seconds);
         * τ — Time constant (seconds);
         * R — Resistance (ohms);
         * C — Capacitance (farads).
         *
         * @return τ = R×C. The units are seconds
         */
        public static double capacitorChargeTime(double multipleTimeConstant, double timeConstant) {
            checkGreater0(multipleTimeConstant);
            checkGreater0(timeConstant);

            final double chargingTime = multipleTimeConstant * timeConstant;
            final double percentage = 1 - Math.exp(-chargingTime / timeConstant);
            checkCapacitorOvercharge(percentage);
            return chargingTime;
        }

        private static void checkCapacitorOvercharge(double percentage) {
            if (percentage >= 1) {
                throw new IllegalArgumentException("You can never charge a capacitor to 100% or more");
            }
        }

        public static double[] capacitorChargeTimeGivenPercentage(double percentage, double timeConstant) {
            checkGreater0(percentage);
            checkGreater0(timeConstant);

            final double multipleTimeConstant = -Algebra.ln(percentage);
            final double chargingTime = multipleTimeConstant * timeConstant;
            checkCapacitorOvercharge(percentage);
            return new double[]{multipleTimeConstant, chargingTime};
        }

        /**
         * @return Vₚ = Vₛ ⋅ Nₚ/Nₛ. The units are volts
         */
        public static double idealTransformerPrimaryVoltage(
            double primaryCoilWindings, double secondaryCoilWindings, double secondaryVoltageVolts) {
            return secondaryVoltageVolts * primaryCoilWindings / secondaryCoilWindings;
        }

        /**
         * @return Vₛ = Vₚ ⋅ Nₛ/Nₚ. The units are volts
         */
        public static double idealTransformerSecondaryVoltage(
            double primaryCoilWindings, double secondaryCoilWindings, double primaryVoltageVolts) {
            return primaryVoltageVolts * secondaryCoilWindings / primaryCoilWindings;
        }

        /**
         * P = Iₚ ⋅ Vₚ = Iₛ ⋅ Vₛ
         *
         * @return Iₚ = Iₛ ⋅ Nₛ/Nₚ. The units are amperes
         */
        public static double idealTransformerPrimaryCurrent(
            double primaryCoilWindings, double secondaryCoilWindings, double secondaryCurrent) {
            return secondaryCurrent * secondaryCoilWindings / primaryCoilWindings;
        }

        /**
         * @return Iₛ = Iₚ ⋅ Nₚ/Nₛ. The units are amperes
         */
        public static double idealTransformerSecondaryCurrent(
            double primaryCoilWindings, double secondaryCoilWindings, double primaryCurrent) {
            return primaryCurrent * primaryCoilWindings / secondaryCoilWindings;
        }

        /**
         * @return L = µ₀ × N² × A/l. The units are henries
         */
        public static double solenoidInductance(double numberOfTurns, double radiusMeters, double lengthMeters) {
            final double crossSectionalArea = Geometry.circleArea(radiusMeters);
            return VACUUM_PERMEABILITY * numberOfTurns * numberOfTurns * crossSectionalArea / lengthMeters;
        }

        /**
         * @return B = (µ₀NI)/L. The units are T
         */
        public static double solenoidMagneticField(double currentAmps, double lengthMeters, double numberOfTurns) {
            return (VACUUM_PERMEABILITY * numberOfTurns * currentAmps) / lengthMeters;
        }

        /**
         * @return The units are meters
         */
        public static double solenoidInductanceSolveForRadius(
            double numberOfTurns, double lengthMeters, double inductanceHenries) {
            return squareRoot((inductanceHenries * lengthMeters)
                / (VACUUM_PERMEABILITY * numberOfTurns * numberOfTurns * Math.PI));
        }

        /**
         * @return The units are meters
         */
        public static double solenoidInductanceSolveForLength(
            double numberOfTurns, double radiusMeters, double inductanceHenries) {
            return (VACUUM_PERMEABILITY * numberOfTurns * numberOfTurns
                * Math.PI * radiusMeters * radiusMeters) / inductanceHenries;
        }

        /**
         * @param crossSectionalArea in m²
         * @return The units are meters
         */
        public static double solenoidInductanceSolveForRadiusGivenCrossSectionArea(double crossSectionalArea) {
            return Geometry.circleRadiusOfArea(crossSectionalArea);
        }

        /**
         * @param crossSectionalArea in m²
         * @return The units are meters
         */
        public static double solenoidInductanceSolveForLengthGivenCrossSectionArea(
            double numberOfTurns, double crossSectionalArea, double inductanceHenries) {
            return squareRoot((inductanceHenries * Math.PI)
                / (VACUUM_PERMEABILITY * numberOfTurns * numberOfTurns * crossSectionalArea));
        }

        /**
         * VR = (V no-load - V full-load) / V full-load
         * PC = VR×100
         */
        public static double[] stepUpVoltageRegulation(double noLoadVolts, double fullLoadVolts) {
            final double voltageRegulation = (noLoadVolts - fullLoadVolts) / fullLoadVolts;
            final double percentChange = voltageRegulation * 100;
            return new double[]{voltageRegulation, percentChange};
        }

        /**
         * VR = (V no-load - V full-load) / V no-load
         * PC = VR×100
         */
        public static double[] stepDownVoltageRegulation(double noLoadVolts, double fullLoadVolts) {
            final double voltageRegulation = (noLoadVolts - fullLoadVolts) / noLoadVolts;
            final double percentChange = voltageRegulation * 100;
            return new double[]{voltageRegulation, percentChange};
        }

        /**
         * @return PD = (V input − V output) × I output
         */
        public static double powerDissipationInVoltageRegulator(double inputVolts, double outputVolts, double current) {
            return (inputVolts - outputVolts) * current;
        }

        /**
         * @return f꜀ = 1/(2πRC). The units are Hz
         */
        public static double rcLowPassFilter(double resistanceOhms, double capacitanceFarads) {
            return reciprocal(Trigonometry.PI2 * resistanceOhms * capacitanceFarads);
        }

        /**
         * @return f꜀ = R/(2πL). The units are Hz
         */
        public static double rlLowPassFilter(double resistanceOhms, double inductanceHenries) {
            return resistanceOhms / (Trigonometry.PI2 * inductanceHenries);
        }

        /**
         * @return f꜀ = 1/(2πR_fC). The units are Hz
         */
        public static double invertingOpAmpLowPassFilter(double feedbackResistanceOhms, double capacitanceFarads) {
            return reciprocal(Trigonometry.PI2 * feedbackResistanceOhms * capacitanceFarads);
        }

        /**
         * @return G = -(R_f/Rᵢ)
         */
        public static double invertingOpAmpLowPassFilterGain(double inputResistanceOhms,
                                                             double feedbackResistanceOhms) {
            return -(feedbackResistanceOhms / inputResistanceOhms);
        }

        /**
         * @return f꜀ = 1/(2πRᵢC). The units are Hz
         */
        public static double nonInvertingOpAmpLowPassFilter(double inputResistanceOhms, double capacitanceFarads) {
            return reciprocal(Trigonometry.PI2 * inputResistanceOhms * capacitanceFarads);
        }

        /**
         * @return G = 1+(R_f/R_g)
         */
        public static double nonInvertingOpAmpLowPassFilterGain(double feedbackResistanceOhms,
                                                                double positiveToGroundResistance) {
            return 1 + feedbackResistanceOhms / positiveToGroundResistance;
        }

        /**
         * I = (1000 ⋅ kVA)/V
         * <br/>
         * For a single-phase transformer.
         *
         * @return kVA = I × V / 1000. The units are kVA
         */
        public static double transformerSize(double loadCurrentAmps, double loadVoltageVolts) {
            return (loadCurrentAmps * loadVoltageVolts) / 1000;
        }

        public static double transformerSize(
            double loadCurrentAmps, double loadVoltageVolts, double spareCapacityPercent) {
            final double minKVA = transformerSize(loadCurrentAmps, loadVoltageVolts);
            final double spareCapacity = minKVA * (spareCapacityPercent / 100);
            return minKVA + spareCapacity;
        }

        /**
         * @return kVA = I × V × √3 / 1000. The units are kVA
         */
        public static double threePhaseTransformerSize(double loadCurrentAmps, double loadVoltageVolts) {
            return loadCurrentAmps * loadVoltageVolts * squareRoot(3) / 1000;
        }

        public static double threePhaseTransformerSize(
            double loadCurrentAmps, double loadVoltageVolts, double spareCapacityPercent) {
            final double minKVA = threePhaseTransformerSize(loadCurrentAmps, loadVoltageVolts);
            final double spareCapacity = minKVA * (spareCapacityPercent / 100);
            return minKVA + spareCapacity;
        }

        /**
         * @return Q = 1/R × √(L/C)
         */
        public static double rlcCircuitQFactor(
            double capacitanceFarads, double inductanceHenries, double resistanceOhms) {
            return reciprocal(resistanceOhms) * squareRoot(inductanceHenries / capacitanceFarads);
        }

        public static double rlcCircuitFrequency(double capacitanceFarads, double inductanceHenries) {
            return resonantFrequencyLC(capacitanceFarads, inductanceHenries);
        }

        /**
         * @return f = 1 / (2π × √(L × C)). The units are Hz
         */
        public static double resonantFrequencyLC(double capacitanceFarads, double inductanceHenries) {
            return reciprocal(Trigonometry.PI2 * squareRoot(inductanceHenries * capacitanceFarads));
        }

        /**
         * @return xC = 1/(2π⋅f⋅C). The units are Ω
         */
        public static double resonantFrequencyLCCapacitiveReactance(double frequencyHz, double capacitanceFarads) {
            return reciprocal(Trigonometry.PI2 * frequencyHz * capacitanceFarads);
        }

        /**
         * @return xL = 2π⋅f⋅L. The units are Ω
         */
        public static double resonantFrequencyLCInductiveReactance(double frequencyHz, double inductanceHenries) {
            return Trigonometry.PI2 * frequencyHz * inductanceHenries;
        }

        /**
         * @return f = 1/(2πRC). The units are Hz
         */
        public static double rcCircuitFrequency(double capacitanceFarads, double resistanceOhms) {
            return reciprocal(Trigonometry.PI2 * resistanceOhms * capacitanceFarads);
        }

        /**
         * @return t = RC. The units are sec
         */
        public static double rcCircuitChargingTime(double capacitanceFarads, double resistanceOhms) {
            return resistanceOhms * capacitanceFarads;
        }

        /**
         * @return F = BIl * sin(α). The units are N
         */
        public static double magneticForceOnCurrentCarryingWire(
            double magneticFieldTesla, double currentAmps, double lengthMeters, double angleRad) {
            return magneticFieldTesla * currentAmps * lengthMeters * Trigonometry.sin(angleRad);
        }

        /**
         * @return F / L = μ₀ × Ia × Ib / (2π × d). The units are N
         */
        public static double magneticForceBetweenWires(double current1Amps, double current2Amps,
                                                       double distanceMeters) {
            return VACUUM_PERMEABILITY * current1Amps * current2Amps / (Trigonometry.PI2 * distanceMeters);
        }

        /**
         * <table>
         *     <tr><th>LED color</th><th>Voltage drop across LED</th></tr>
         *     <tr><td>Red</td><td>2</td></tr>
         *     <tr><td>Green</td><td>2.1</td></tr>
         *     <tr><td>Blue</td><td>3.6</td></tr>
         *     <tr><td>White</td><td>3.6</td></tr>
         *     <tr><td>Yellow</td><td>2.1</td></tr>
         *     <tr><td>Orange</td><td>2.2</td></tr>
         *     <tr><td>Amber</td><td>2.1</td></tr>
         *     <tr><td>Infrared</td><td>1.7</td></tr>
         * </table>
         *
         * @return R = (V - n × Vₒ) / Iₒ. The units are Ω
         */
        public static double ledSeriesResistance(
            long numberOfLEDs, double supplyVoltage, double currentAmps, double ledForwardVoltage) {
            final double voltageDropAcrossLED = numberOfLEDs * ledForwardVoltage;
            return (supplyVoltage - voltageDropAcrossLED) / currentAmps;
        }

        /**
         * For series or parallel LEDs.
         *
         * @return Pₒ = Vₒ × Iₒ. The units are W
         */
        public static double ledDissipatedPowerInSingleLED(double currentAmps, double voltageDropVolts) {
            return voltageDropVolts * currentAmps;
        }

        /**
         * For series or parallel LEDs.
         *
         * @return P = n × Vₒ × Iₒ. The units are W
         */
        public static double ledsTotalDissipatedPower(
            double numberOfLEDs, double currentAmps, double voltageDropVolts) {
            return numberOfLEDs * voltageDropVolts * currentAmps;
        }

        /**
         * @return Pr = (Iₒ)² × R. The units are W
         */
        public static double ledSeriesDissipatedPowerInResistor(double currentAmps, double resistanceOhms) {
            return currentAmps * currentAmps * resistanceOhms;
        }

        /**
         * @return R = (V - Vₒ) / (n × Iₒ). The units are Ω
         */
        public static double ledParallelResistance(
            long numberOfLEDs, double supplyVoltage, double currentAmps, double ledForwardVoltage) {
            return (supplyVoltage - ledForwardVoltage) / (numberOfLEDs * currentAmps);
        }

        /**
         * @return Pr = (n × Iₒ)² × R. The units are W
         */
        public static double ledParallelDissipatedPowerInResistor(
            long numberOfLEDs, double currentAmps, double resistanceOhms) {
            return Math.pow(numberOfLEDs * currentAmps, 2) * resistanceOhms;
        }

        /**
         * @param emissionCoeff aka quality factor. It accounts for imperfect junctions as observed
         *                      in real transistors. Its value typically ranges from 1 to 2.
         * @return I = Iₛ(e^(V_D/(nV_T)) − 1). The units are A
         */
        public static double shockleyDiode(
            double emissionCoeff, double saturationCurrentAmps, double thermalVoltageVolts, double voltageDropVolts) {
            return saturationCurrentAmps * (Math.exp(voltageDropVolts / (emissionCoeff * thermalVoltageVolts)) - 1);
        }
    }

    public static final class Acoustics {
        private Acoustics() {
        }

        /**
         * c = √((γRT)/M)
         * where:
         * <ul>
         *     <li>c — Speed of sound in an ideal gas;</li>
         *     <li>R — Molar gas constant, approximately 8.3145 J·mol⁻¹·K⁻¹;</li>
         *     <li>γ — Adiabatic index, approximately 1.4 for air;</li>
         *     <li>T — Absolute temperature (in kelvins);</li>
         *     <li>M — The molar mass of the gas. For dry air, it is about 0.0289645 kg/mol.</li>
         * </ul>
         *
         * @return c_air = 331.3 × √(1 + T/273.15). The units are m/s
         */
        public static double soundSpeed(double temperatureCelsius) {
            return SOUND_SPEED_IN_DRY_AIR * squareRoot(1 + temperatureCelsius / SOUND_SPEED_IN_AIR_KELVIN_REF_POINT);
        }

        /**
         * The formula for oceanography.
         *
         * @return 1404.3 + 4.7T - 0.04T². The units are m/s
         */
        public static double soundSpeedInWater(double temperature) {
            return 1404.3 + 4.7 * temperature - 0.04 * temperature * temperature;
        }

        /**
         * @return c = √(γ × p / ρ)
         */
        public static double soundSpeedInWater(double adiabaticIndex, double pressure, double airDensity) {
            return squareRoot(adiabaticIndex * pressure / airDensity);
        }

        /**
         * @return v = λf. The units are m/s
         */
        public static double soundSpeed(double wavelengthMeters, double frequencyHz) {
            return wavelengthMeters * frequencyHz;
        }

        /**
         * In fluids: v = √(B/ρ); B is bulk modulus.
         * In solids: v = √(E/ρ); E is Young's modulus.
         *
         * @return . The units are m/s
         */
        public static double soundSpeedInMedium(double modulus, double density) {
            return squareRoot(modulus / density);
        }

        /**
         * @param referencePressurePascals Pref — Reference value of sound pressure. Typically, it is assumed to be
         *                                 equal to 0.00002 Pa (human hearing threshold).
         * @return SPL = 20 × log(P/Pref). The units are dB
         */
        public static double soundPressureLevel(double referencePressurePascals, double soundWavePressurePascals) {
            return 20 * log(soundWavePressurePascals / referencePressurePascals);
        }

        /**
         * @param referenceIntensity Iref — Reference value if sound intensity. Typically, it is assumed to be
         *                           equal to 1×10⁻¹² W/m² (human hearing threshold).
         * @param soundIntensity     in W/m²
         * @return SIL = 10 × log(I/Iref). The units are dB
         */
        public static double soundIntensityLevel(double referenceIntensity, double soundIntensity) {
            return 10 * log(soundIntensity / referenceIntensity);
        }

        /**
         * where:
         * R — Radius of the sphere, i.e., the distance from the sound source.
         *
         * @return I = P/(4πR²). The units are W/m²
         */
        public static double soundIntensityAtDistance(double soundSourcePower, double distanceMeters) {
            return soundSourcePower / (Trigonometry.PI4 * distanceMeters * distanceMeters);
        }

        /**
         * @return f_b = ∣f₂−f₁∣. The units are Hz
         */
        public static double beatFrequency(double firstWaveFrequencyHz, double secondWaveFrequencyHz) {
            return Math.abs(secondWaveFrequencyHz - firstWaveFrequencyHz);
        }

        /**
         * @param speedOfSound in m/s
         * @return λ = v/f. The units are meters
         */
        public static double soundWavelength(double speedOfSound, double frequencyHz) {
            return speedOfSound / frequencyHz;
        }

        /**
         * @param speedOfSound in m/s
         * @return f = v/λ. The units are Hz
         */
        public static double soundFrequency(double speedOfSound, double wavelengthMeters) {
            return speedOfSound / wavelengthMeters;
        }

        /**
         * @param incidentSoundIntensity in W/m²
         * @param absorbedSoundIntensity in W/m²
         * @return α = Iₐ/Iᵢ
         */
        public static double soundAbsorptionCoefficient(double incidentSoundIntensity, double absorbedSoundIntensity) {
            return absorbedSoundIntensity / incidentSoundIntensity;
        }

        /**
         * @param surfaceAreas in m²
         * @return A = ∑Sᵢαᵢ. The units are m² sabins
         */
        public static double totalRoomSoundAbsorption(double[] surfaceAreas, double[] absorptionCoefficients) {
            return LinearAlgebra.dotProduct(surfaceAreas, absorptionCoefficients);
        }

        /**
         * @param absorptionOfRoom   in m² sabins
         * @param totalSurfaceInRoom in m²
         * @return αₘ = A/S. The units are m² sabins
         */
        public static double avgSoundAbsorptionCoefficient(double absorptionOfRoom, double totalSurfaceInRoom) {
            return absorptionOfRoom / totalSurfaceInRoom;
        }
    }

    public static final class Optics {
        public static final byte TELESCOPE_STD_FOV = 52; // in degrees

        private Optics() {
        }

        /**
         * @return distance = speed of light × time. The units are m
         */
        public static double lightSpeed(double timeSeconds) {
            return SPEED_OF_LIGHT * timeSeconds;
        }

        /**
         * 1.22 - a constant derived from the physics of diffraction, specifically the first zero of
         * the Bessel function for a circular aperture.
         *
         * @return θ = 1.22 × λ / d. The units are radians
         */
        public static double angularResolution(double wavelengthMeters, double apertureDiameterMeters) {
            return 1.22 * wavelengthMeters / apertureDiameterMeters;
        }

        /**
         * @return d = h × 1000 / Mil
         */
        public static double binocularsRange(double objectHeightMeters, double objectAngularHeightMRad) {
            return objectHeightMeters * 1000 / objectAngularHeightMRad;
        }

        /**
         * M = fₒ/fₑ
         *
         * @param objectiveFocalPoint the focal length of the telescope (fₒ)
         * @return eyepiece focal length (fₑ)
         */
        public static double telescopeEyepieceFocalLength(double objectiveFocalPoint, double magnification) {
            return objectiveFocalPoint / magnification;
        }

        /**
         * Most eyepieces come with an apparent field of view between 30° and 110°.
         * where:
         * fₜ - the focal length of the telescope.
         * fₑ - the eyepiece.
         * m - magnification
         *
         * @return fov꜀ = fovₐ/(fₜ/fₑ) = fovₐ/m. The units are degrees
         */
        public static double telescopeFOV(double apparentFOVDeg, double magnification) {
            final double fovDeg = apparentFOVDeg / magnification;
            return AngleUnit.degToArcseconds(fovDeg);
        }

        /**
         * @param fov field of view in arcsec
         * @return The units are deg²
         */
        public static double telescopeAreaFOV(double fov) {
            return 2 * Math.PI * (1 - Trigonometry.cos(fov / 2));
        }

        /**
         * fᵣ = fₒ / Dₒ
         *
         * @param objectiveDiameter in mm
         * @return telescope focal length. The units are mm
         */
        public static double telescopeObjectiveFocalPoint(double objectiveDiameter, double fRatio) {
            return fRatio * objectiveDiameter;
        }

        /**
         * @param telescopeFocalLength in mm
         * @param eyepieceFocalLength  in mm
         * @return M = fₒ/fₑ
         */
        public static double telescopeMagnification(double telescopeFocalLength, double eyepieceFocalLength) {
            return telescopeFocalLength / eyepieceFocalLength;
        }

        /**
         * @param objectiveDiameter in mm
         * @return Mₘᵢₙ = Dₒ / 7
         */
        public static double telescopeMinMagnification(double objectiveDiameter) {
            return objectiveDiameter / 7;
        }

        /**
         * @param objectiveDiameter in mm
         * @return Pᵣ = 115.8" / Dₒ. The units are arcsec
         */
        public static double telescopeResolvingPower(double objectiveDiameter) {
            return 115.8 / objectiveDiameter;
        }

        /**
         * @param objectiveDiameter in mm
         * @return Lₘ = 2 + 5×log(Dₒ)
         */
        public static double telescopeStarMagnitudeLimit(double objectiveDiameter) {
            return 2 + 5 * log(objectiveDiameter);
        }

        /**
         * @return FOVₛ = FOVₑ / M
         */
        public static double telescopeScopeFOV(double magnification, double eyepieceFOV) {
            return eyepieceFOV / magnification;
        }

        /**
         * @param objectiveDiameter in mm
         * @return Dₑₚ = Dₒ / M
         */
        public static double telescopeExitPupilDiameter(double objectiveDiameter, double magnification) {
            return objectiveDiameter / magnification;
        }

        /**
         * @return SB = 2 × Dₑₚ²
         */
        public static double telescopeSurfaceBrightness(double exitPupilDiameter) {
            return 2 * (exitPupilDiameter * exitPupilDiameter);
        }
    }

    public static final class Thermodynamics {
        private Thermodynamics() {
        }

        /**
         * @param tempDiffKelvins ΔT is the temperature difference across the object.
         * @param distanceMeters  Δx is the distance of heat transfer (the thickness of the object).
         * @return q = −λ(ΔT/Δx). The units are W/m²
         */
        public static double thermalConductivity(
            double materialThermalConductivity, double tempDiffKelvins, double distanceMeters) {
            return -materialThermalConductivity * (tempDiffKelvins / distanceMeters);
        }

        /**
         * where:
         * KE is the average kinetic energy of molecules,
         * v is the average velocity of molecules,
         * U is the total thermal energy of a gas,
         * f is the number of degrees of freedom,
         * T is the temperature,
         * M is the molar mass of the gas,
         * n is the number of moles),
         * k is the Boltzmann constant,
         * Na is the Avogadro constant.
         */
        public static double[] thermalEnergy(double degreesOfFreedom, double molarMassKg,
                                             double temperatureKelvins, double molesOfGas) {
            // KE = f × k × T / 2 in J
            final double avgKineticEnergy = degreesOfFreedom * BOLTZMANN_CONSTANT * temperatureKelvins / 2;
            // v = √(2 × KE × Na / M) in m/s
            final double avgSpeed = squareRoot(2 * avgKineticEnergy * AVOGADRO_NUMBER / molarMassKg);
            // U = n × Na × KE in J
            final double totalThermalEnergy = molesOfGas * AVOGADRO_NUMBER * avgKineticEnergy;
            return new double[]{avgKineticEnergy, avgSpeed, totalThermalEnergy};
        }

        /**
         * where:
         * T₁ – Initial temperature, and T₂ is the final temperature;
         * ΔL – Change in object's length;
         * L₁ – Initial length;
         * a – Linear expansion coefficient.
         *
         * @param linearExpansionCoeff in Kelvins
         * @param initialLength        in meters
         * @param initialTemperature   in Kelvins
         * @param finalTemperature     in Kelvins
         * @return ΔL = aL₁(T₂ - T₁). The units are m
         */
        public static double thermalLinearExpansionChangeInLength(
            double linearExpansionCoeff, double initialLength, double initialTemperature, double finalTemperature) {
            return linearExpansionCoeff * initialLength * (finalTemperature - initialTemperature);
        }

        public static double thermalLinearExpansionFinalLength(double initialLengthMeters, double changeInLength) {
            return initialLengthMeters + changeInLength;
        }

        /**
         * where:
         * T₁ – Initial temperature, and T₂ is the final temperature;
         * ΔV – Change in object's volume;
         * V₁ – Initial volume; and
         * b – Volumetric expansion coefficient.
         *
         * @param initialTemperature in Kelvins
         * @param finalTemperature   in Kelvins
         * @return ΔV = bV₁(T₂ − T₁). The units are m³
         */
        public static double thermalVolumetricExpansionChangeInVolume(
            double volumetricExpansionCoeff, double initialVolume, double initialTemperature, double finalTemperature) {
            return volumetricExpansionCoeff * initialVolume * (finalTemperature - initialTemperature);
        }

        public static double thermalVolumetricExpansionFinalVolume(double initialVolume, double changeInVolume) {
            return initialVolume + changeInVolume;
        }

        /**
         * R = (T₂−T₁)/Q₁−₂
         * <p>
         * where:
         * k — Thermal conductivity of the material W/m⋅K;
         * t — Length of the plate in m;
         * A — Cross-sectional area, A = l×w in m².
         *
         * @return R_plate = t/(kA). The units are K/W
         */
        public static double thermalResistanceOfPlate(
            double thermalConductivity, double thicknessMeters, double crossSectionalAreaMeters) {
            return thicknessMeters / (thermalConductivity * crossSectionalAreaMeters);
        }

        /**
         * @param thermalConductivity in W/(m⋅K)
         * @return R_cylinder = ln(r₂/r₁)/(2πLk). The units are K/W
         */
        public static double thermalResistanceOfHollowCylinder(
            double thermalConductivity, double lengthMeters, double innerRadiusMeters, double outerRadiusMeters) {
            return ln(outerRadiusMeters / innerRadiusMeters)
                / (Trigonometry.PI2 * lengthMeters * thermalConductivity);
        }

        /**
         * @param thermalConductivity in W/(m⋅K)
         * @return R_sphere = (r₂-r₁)/(4πr₁r₂k). The units are K/W
         */
        public static double thermalResistanceOfHollowSphere(
            double thermalConductivity, double innerRadiusMeters, double outerRadiusMeters) {
            return (outerRadiusMeters - innerRadiusMeters)
                / (Trigonometry.PI4 * innerRadiusMeters * outerRadiusMeters * thermalConductivity);
        }

        /**
         * @param heatTransferCoeff in W/(m²⋅K)
         * @return r_cr-cylinder = (2k)/h
         */
        public static double thermalResistanceOfHollowCylinderCriticalRadius(
            double thermalConductivity, double heatTransferCoeff) {
            return (2 * thermalConductivity) / heatTransferCoeff;
        }

        /**
         * @param heatTransferCoeff in W/(m²⋅K)
         * @return r_cr-sphere = k/h
         */
        public static double thermalResistanceOfHollowSphereCriticalRadius(
            double thermalConductivity, double heatTransferCoeff) {
            return thermalConductivity / heatTransferCoeff;
        }

        /**
         * @return c = Q/(m×ΔT). The units are J/(kg⋅K)
         */
        public static double specificHeat(double energyJoules, double massKg, double changeInTempCelsius) {
            return energyJoules / (massKg * changeInTempCelsius);
        }

        /**
         * where:
         * c is the specific heat capacity;
         * m is the mass;
         * T_f is the final temperature;
         * Tᵢ is the initial temperature.
         *
         * @return Qₜ = cm(T_f−Tᵢ). The units are J
         */
        public static double waterHeating(
            double massOrVolume, double initialTempCelsius, double finalTempCelsius, double specificHeat) {
            return specificHeat * massOrVolume * (finalTempCelsius - initialTempCelsius);
        }

        /**
         * @param efficiency in the percent scale 0-1
         * @return time = Q_total/(efficiency×power). The units are sec
         */
        public static double waterHeatingTime(double totalEnergyJoules, double heatingPowerWatts, double efficiency) {
            return totalEnergyJoules / (efficiency * heatingPowerWatts);
        }

        /**
         * @return η = Eₒᵤₜ/Eᵢₙ ⋅ 100%. The units are %
         */
        public static double efficiency(double energyInputJoules, double energyOutputJoules) {
            return energyOutputJoules / energyInputJoules * 100;
        }

        /**
         * @return ηₜₕ,ᵣₑᵥ = 1 − T꜀/Tₕ. The units are %
         */
        public static double thermalEfficiency(double hotReservoirTempKelvins, double coldReservoirTempKelvins) {
            return 1 - coldReservoirTempKelvins / hotReservoirTempKelvins;
        }

        /**
         * @return qₕ = q꜀/(1 − ηₜₕ). The units are K
         */
        public static double thermalEfficiencyHotReservoirTemp(
            double coldReservoirTempKelvins, double thermalEfficiencyPercent) {
            return coldReservoirTempKelvins / (1 - thermalEfficiencyPercent / 100);
        }

        /**
         * @return Ẇₙₑₜ,ₒᵤₜ = ηₜₕQ̇ᵢₙ. The units are J
         */
        public static double irreversibleThermalEfficiencyNetWorkOutput(
            double heatReceivedJoules, double thermalEfficiencyPercent) {
            return thermalEfficiencyPercent / 100 * heatReceivedJoules;
        }

        /**
         * @return Qₒᵤₜ = Q̇ᵢₙ-Ẇₙₑₜ,ₒᵤₜ. The units are J
         */
        public static double irreversibleThermalEfficiencyHeatRejected(double heatReceived, double netWorkOutput) {
            return heatReceived - netWorkOutput;
        }

        /**
         * ∆Q = c⋅m⋅∆T
         *
         * @param specificHeat in J/(kg⋅K)
         * @return S = ∆Q/∆T = c⋅m. The units are J/K
         */
        public static double heatCapacity(double massKg, double specificHeat) {
            return specificHeat * massKg;
        }

        /**
         * @param specificHeat       in J/(kg⋅K)
         * @param initialTempCelsius T₁
         * @param finalTempCelsius   T₂
         * @return Q = m⋅c⋅ΔT. The units are J
         */
        public static double basicHeatTransfer(double massKg, double specificHeat,
                                               double initialTempCelsius, double finalTempCelsius) {
            return massKg * specificHeat * (finalTempCelsius - initialTempCelsius);
        }

        /**
         * @param thermalConductivity in W/(m⋅K)
         * @param crossSectionalArea  in m²
         * @param coldTempCelsius     T꜀
         * @param hotTempCelsius      Tₕ
         * @return Q = (k⋅A⋅t⋅ΔT)/l. The units are J
         */
        public static double conductionHeatTransfer(
            double thermalConductivity, double crossSectionalArea, double coldTempCelsius, double hotTempCelsius,
            double timeTakenSeconds, double materialThicknessMeters) {
            return (thermalConductivity * crossSectionalArea * timeTakenSeconds * (hotTempCelsius - coldTempCelsius))
                / materialThicknessMeters;
        }

        /**
         * @param heatTransferCoeff  in W/m²⋅K
         * @param surfaceArea        in m²
         * @param bulkTempCelsius    T₁
         * @param surfaceTempCelsius T₂
         * @return Q = H꜀⋅A⋅ΔT. The units are W
         */
        public static double heatTransferThroughConvection(
            double heatTransferCoeff, double surfaceArea, double bulkTempCelsius, double surfaceTempCelsius) {
            return heatTransferCoeff * surfaceArea * (surfaceTempCelsius - bulkTempCelsius);
        }

        /**
         * @param hotObjectArea     in m²
         * @param emissivity        The emissivity depends on the type of material and the temperature of the surface.
         *                          It ranges from 0 (perfect reflector) to 1 (black body).
         * @param objectTempKelvins T₁
         * @param envTempKelvins    T₂
         * @return Q = σ⋅e⋅A⋅(T₂⁴−T₁⁴). The units are W
         */
        public static double heatTransferByRadiation(
            double hotObjectArea, double emissivity, double objectTempKelvins, double envTempKelvins) {
            return STEFAN_BOLTZMANN_CONSTANT * emissivity * hotObjectArea
                * (Math.pow(envTempKelvins, 4) - Math.pow(objectTempKelvins, 4));
        }

        /**
         * PV = nRT
         * <br/>
         * The isothermal transformation (Boyle's law): PV = k.
         * The isochoric transformation (Charles's law): P/T = k.
         * The isobaric transformation (Gay-Lussac's law): V/T = k.
         *
         * @return P = nRT. The units are Pa
         */
        public static double idealGasLawPressure(double amountOfSubstance, double tempKelvins) {
            return amountOfSubstance * UNIVERSAL_GAS_CONSTANT * tempKelvins;
        }

        /**
         * PV = nRT
         *
         * @param volume            in m³
         * @param amountOfSubstance in mol
         * @return T = PV/nR. The units are Kelvins
         */
        public static double idealGasLawTemperature(double pressurePascals, double volume, double amountOfSubstance) {
            return (pressurePascals * volume) / (amountOfSubstance * UNIVERSAL_GAS_CONSTANT);
        }

        /**
         * p₁ × V₁ = p₂ × V₂
         *
         * @param initialVolume in m³
         * @return Vf = Pᵢ · Vᵢ/Pf. The units are m³
         */
        public static double boylesLawFinalVolume(
            double initialPressurePa, double initialVolume, double finalPressurePa) {
            return initialPressurePa * initialVolume / finalPressurePa;
        }

        /**
         * @param initialVolume in m³
         * @param finalVolume   in m³
         * @return Pf = (Vᵢ · Pᵢ)/Vf. The units are Pa
         */
        public static double boylesLawFinalPressure(double initialPressurePa, double initialVolume,
                                                    double finalVolume) {
            return (initialVolume * initialPressurePa) / finalVolume;
        }

        /**
         * @return The units are Pa
         */
        public static double boylesLawFinalPressure(double initialPressurePa, double volumeRatio) {
            return initialPressurePa * volumeRatio;
        }

        /**
         * V₁ / T₁ = V₂ / T₂
         *
         * @param finalVolume m³
         * @return V₁ = (T₁×V₂)/T₂. The units are m³
         */
        public static double charlesLawInitialVolume(
            double initialTempKelvins, double finalTempKelvins, double finalVolume) {
            return (initialTempKelvins * finalVolume) / finalTempKelvins;
        }

        /**
         * @param initialVolume m³
         * @return V₂ = V₁ / T₁ × T₂. The units are m³
         */
        public static double charlesLawFinalVolume(
            double initialVolume, double initialTempKelvins, double finalTempKelvins) {
            return initialVolume / initialTempKelvins * finalTempKelvins;
        }

        /**
         * @param initialVolume m³
         * @return T₂ = T₁ / V₁ × V₂. The units are K
         */
        public static double charlesLawFinalTemperature(
            double initialVolume, double finalVolume, double initialTempKelvins) {
            return initialTempKelvins / initialVolume * finalVolume;
        }

        /**
         * @return p₂ = p₁ / T₁ × T₂. The units are Pa
         */
        public static double gayLussacsLawFinalPressure(
            double initialPressurePa, double initialTempKelvins, double finalTempKelvins) {
            return initialPressurePa / initialTempKelvins * finalTempKelvins;
        }

        /**
         * @return T₂ = T₁ × p₂ / p₁. The units are K
         */
        public static double gayLussacsLawFinalTemperature(
            double initialPressurePa, double finalPressurePa, double initialTempKelvins) {
            return initialTempKelvins * finalPressurePa / initialPressurePa;
        }

        /**
         * @param volume m³
         * @return n = p₁ × V / (R × T₁). The units are mol
         */
        public static double amountOfGas(double initialPressurePa, double initialTempKelvins, double volume) {
            return initialPressurePa * volume / (UNIVERSAL_GAS_CONSTANT * initialTempKelvins);
        }

        /**
         * @return Q = I² × R × t. The units are J
         */
        public static double jouleHeating(double currentAmps, double resistanceOhms, double timeSeconds) {
            return currentAmps * currentAmps * resistanceOhms * timeSeconds;
        }

        /**
         * Daily evaporation rate: g_d = 24×(25+19×v_d)×A×(X_sd − X_d).
         * <br/>
         * Xₛ = 3.733×10⁻³ + 3.2×10⁻⁴×T + 3×10⁻⁶×T² + 4×10⁻⁷×T³
         *
         * @param surfaceAreaOfWater in m²
         * @param airSpeed           in m/s
         * @return gₕ = (25+19×v)×A×(Xₛ−X). The units are kg/hr
         */
        public static double evaporationRate(
            double surfaceAreaOfWater, double airSpeed, double airTempCelsius, double relativeHumidityPercent) {
            final double maxHumidity = 0.003733 + 0.00032 * airTempCelsius
                + 0.000003 * airTempCelsius * airTempCelsius
                + 0.0000004 * airTempCelsius * airTempCelsius * airTempCelsius;
            final double actualHumidity = maxHumidity * (relativeHumidityPercent / 100);
            return (25 + 19 * airSpeed) * surfaceAreaOfWater * (maxHumidity - actualHumidity);
        }

        /**
         * @return η = (Tₕ−T꜀)/Tₕ⋅100%. The units are %
         */
        public static double carnotEfficiency(double coldReservoirTempKelvins, double hotReservoirTempKelvins) {
            return (hotReservoirTempKelvins - coldReservoirTempKelvins) / hotReservoirTempKelvins * 100;
        }

        /**
         * @return COPᵣ, ᵣₑᵥ = 1/(Tₕ/T꜀−1)
         */
        public static double carnotReversibleRefrigeratorCOP(
            double hotMediumTempKelvins, double coldMediumTempKelvins) {
            return reciprocal(hotMediumTempKelvins / coldMediumTempKelvins - 1);
        }

        /**
         * @return COPₕₚ, ᵣₑᵥ = 1/(1-T꜀/Tₕ)
         */
        public static double carnotReversibleHeatPumpCOP(double hotMediumTempKelvins, double coldMediumTempKelvins) {
            return reciprocal(1 - coldMediumTempKelvins / hotMediumTempKelvins);
        }

        /**
         * COPᵣ = Q꜀/W
         * Qₕ = Q꜀ + W
         *
         * @return COPᵣ = 1/(Qₕ/Q꜀−1)
         */
        public static double refrigeratorCOP(double hotMediumRejectedJoules, double coldMediumTakenJoules) {
            return reciprocal(hotMediumRejectedJoules / coldMediumTakenJoules - 1);
        }

        /**
         * @return COPₕₚ = 1/(1-Q꜀/Qₕ)
         */
        public static double heatPumpCOP(double hotMediumRejectedJoules, double coldMediumTakenJoules) {
            return reciprocal(1 - coldMediumTakenJoules / hotMediumRejectedJoules);
        }

        /**
         * @return W = Qₕ-Q꜀. The units are J
         */
        public static double workDoneOnRefrigeratorOrPump(
            double hotMediumRejectedJoules, double coldMediumTakenJoules) {
            return hotMediumRejectedJoules - coldMediumTakenJoules;
        }

        /**
         * @param molarMass in g/mol
         * @return Rₛ = R/M. The units are J/(g·K)
         */
        public static double specificGasConstant(double molarMass) {
            return UNIVERSAL_GAS_CONSTANT / molarMass;
        }

        /**
         * Rₛ = Cₚ - Cᵥ
         *
         * @param constantPressure    Cₚ in J/(g·K)
         * @param specificGasConstant in J/(g·K)
         * @return Cᵥ = Cₚ - Rₛ. The units are J/(g·K)
         */
        public static double specificGasConstantWithSpecificHeatCapacity(
            double constantPressure, double specificGasConstant) {
            return constantPressure - specificGasConstant;
        }

        /**
         * @param specificLatentHeat in J/g
         * @return Q = mL. The units are J
         */
        public static double latentHeat(double massGrams, double specificLatentHeat) {
            return massGrams * specificLatentHeat;
        }

        /**
         * @param curieConstant in K·A/(T·m)
         * @return M = C/T × B. The units are A/m
         */
        public static double curiesLaw(double curieConstant, double magneticFieldTesla, double tempKelvins) {
            return curieConstant / tempKelvins * magneticFieldTesla;
        }

        /**
         * @param specificHeat in J/(kg·K)
         * @return Q = mcₚ(T_f−Tᵢ). The units are J
         */
        public static double sensibleHeat(
            double massKg, double specificHeat, double initialTempCelsius, double finalTempCelsius) {
            return massKg * specificHeat * (finalTempCelsius - initialTempCelsius);
        }

        /**
         * @param thermalConductivity  in W/(m·K)
         * @param density              in kg/m³
         * @param specificHeatCapacity in J/(kg·K)
         * @return α = k/(ρCₚ). The units are m²/sec
         */
        public static double thermalDiffusivity(
            double thermalConductivity, double density, double specificHeatCapacity) {
            return thermalConductivity / (density * specificHeatCapacity);
        }

        /**
         * Pν = RT
         * where:
         * P — Absolute pressure of the gas;
         * ν — Specific volume of the gas;
         * R — Gas constant, different for every gas;
         * T — Absolute gas temperature, in kelvin (K).
         * <br/>
         * ν = 1/ρ
         * P/ρ = RT
         * ρ = Mp/RT
         * where:
         * M — Molar mass, in kg/mol or g/mol;
         * R — Universal gas constant.
         *
         * @param specificGasConstant in J/(kg·K)
         * @return ρ = P/RT. The units are kg/m³
         */
        public static double idealGasDensity(double specificGasConstant, double pressurePa, double tempKelvins) {
            return pressurePa / (specificGasConstant * tempKelvins);
        }

        /**
         * @param specificHeatCapacityObj1 in J/(kg·K)
         * @param specificHeatCapacityObj2 in J/(kg·K)
         * @return T_f = (m₁c₁t₁ᵢ + m₂c₂t₂ᵢ)/(m₁c₁+m₂c₂). The units are °C
         */
        public static double thermalEquilibrium(
            double massKgObj1, double specificHeatCapacityObj1, double initialTempKelvinsObj1,
            double massKgObj2, double specificHeatCapacityObj2, double initialTempKelvinsObj2) {
            final double obj1Prod = massKgObj1 * specificHeatCapacityObj1 * initialTempKelvinsObj1;
            final double obj2Prod = massKgObj2 * specificHeatCapacityObj2 * initialTempKelvinsObj2;
            return (obj1Prod + obj2Prod)
                / (massKgObj1 * specificHeatCapacityObj1 + massKgObj2 * specificHeatCapacityObj2);
        }

        /**
         * Boltzmann distribution (Gibbs distribution): P = 1/Z * e^(−E/k_B*T).
         * where:
         * Z – Normalization constant;
         * E – Energy of the state (in joules);
         * k_B - the Boltzmann constant;
         * T – Temperature (in kelvins);
         * P – Probability that this state occurs.
         *
         * @return P₂/P₁ = e^(E₂−E₁)/(k_B*T)
         */
        public static double boltzmannFactor(double energy1Joules, double energy2Joules, double tempKelvins) {
            return Math.exp((energy2Joules - energy1Joules) / (BOLTZMANN_CONSTANT * tempKelvins));
        }

        /**
         * @param volume m³
         * @return Z = P × V / n × R × T = V_actual/V_ideal
         */
        public static double compressibility(double pressurePa, double volume, double numOfMoles, double tempKelvins) {
            return (pressurePa * volume) / (numOfMoles * UNIVERSAL_GAS_CONSTANT * tempKelvins);
        }

        /**
         * @return f(v) = 4/√π * (m/(2kT))^3/2 * v²e^(−mv²/2kT). The units are m/s
         */
        public static double particlesVelocity(double particleMassKg, double tempKelvins, double velocity) {
            final double vSquared = velocity * velocity;
            return 4 / squareRoot(Math.PI) * Math.pow(particleMassKg / (2 * BOLTZMANN_CONSTANT * tempKelvins), 3. / 2)
                * vSquared * Math.exp(-particleMassKg * vSquared / 2 * BOLTZMANN_CONSTANT * tempKelvins);
        }

        /**
         * @return √(8RT/(πm)). The units are m/s
         */
        public static double avgParticleVelocity(double particleMassKg, double tempKelvins) {
            return squareRoot(8 * BOLTZMANN_CONSTANT * tempKelvins / (Math.PI * particleMassKg));
        }

        /**
         * @return vᵣₘₛ = √((3RT)/M). The units are m/s
         */
        public static double rmsVelocity(double tempKelvins, double molarMassKg) {
            return squareRoot(3 * UNIVERSAL_GAS_CONSTANT * tempKelvins / molarMassKg);
        }

        /**
         * @return vₘ = √((2RT)/M). The units are m/s
         */
        public static double medianVelocity(double tempKelvins, double molarMassKg) {
            return squareRoot(2 * UNIVERSAL_GAS_CONSTANT * tempKelvins / molarMassKg);
        }

        /**
         * @param coolingCoeff The larger the number, the faster the cooling.
         * @param timeSeconds  Time of the cooling. What's the temperature after x seconds?
         * @return T = T_amb + (T_initial - T_amb) × e⁻ᵏᵗ. The units are °C
         */
        public static double newtonsLawOfCooling(
            double ambientTempCelsius, double initialTempCelsius, double coolingCoeff, double timeSeconds) {
            return ambientTempCelsius + (initialTempCelsius - ambientTempCelsius)
                * Math.exp(-coolingCoeff * timeSeconds);
        }

        /**
         * @param area              in m²
         * @param heatCapacity      in J/K
         * @param heatTransferCoeff in W/(m²·K)
         * @return k = (hA)/C. The units are per second
         */
        public static double newtonsLawOfCoolingCoeff(double area, double heatCapacity, double heatTransferCoeff) {
            return (heatTransferCoeff * area) / heatCapacity;
        }

        /**
         * L꜀ = V/A
         *
         * @param surfaceArea         in m²
         * @param volume              in m³
         * @param heatTransferCoeff   in W/(m²·K)
         * @param thermalConductivity in W/(m·K)
         * @return Bi = h/k *L꜀. The units are
         */
        public static double biotNumber(
            double surfaceArea, double volume, double heatTransferCoeff, double thermalConductivity) {
            final double characteristicLength = volume / surfaceArea;
            return heatTransferCoeff / thermalConductivity * characteristicLength;
        }

        /**
         * 8P꜀V꜀ = 3RT꜀
         * where:
         * critical point: pressure P꜀, temperature T꜀, and the molar volume V꜀.
         * <br/>
         * (P+a*(n²/V²))(V−nb) = nRT
         * where:
         * P — Pressure of the gas;
         * V — Volume of the gas;
         * T — Temperature of the gas;
         * n — Number of moles of gas;
         * a and b — Van der Waals parameters.
         * a = 3P꜀V꜀² b = V꜀/3
         *
         * @param volume    in m³
         * @param constantB in m³
         * @return The units are K
         */
        public static double vanDerWaalsEquation(
            double amountOfSubstance, double volume, double pressurePa, double constantAPa, double constantB) {
            final double numerator = (pressurePa + constantAPa * Math.pow(amountOfSubstance, 2) / Math.pow(volume, 2)) *
                (volume - amountOfSubstance * constantB);
            final double denominator = amountOfSubstance * UNIVERSAL_GAS_CONSTANT;
            return numerator / denominator;
        }

        /**
         * @param area   m²
         * @param layers [Thermal conductivity (k) in W/(m⋅K), Thickness (L) in m]
         * @return R = 1/U = 1/A * (1/hᵢ + ∑ⁿᵢ₌₁ Lᵢ/kᵢ + 1/hₒ)
         */
        public static double heatTransferCoeffConductionOnly(double area, double[][] layers) {
            final double thermalResistance = reciprocal(area) * Arrays.stream(layers)
                .mapToDouble(layer -> layer[Constants.ARR_2ND_INDEX] / layer[Constants.ARR_1ST_INDEX])
                .sum();
            return reciprocal(thermalResistance);
        }

        /**
         * R = 1/A * (1/hᵢ + ∑ⁿᵢ₌₁ Lᵢ/kᵢ + 1/hₒ)
         *
         * @param area                     m²
         * @param convectionHeatCoeffInner W/(m²⋅K)
         * @param convectionHeatCoeffOuter W/(m²⋅K)
         * @param layers                   [Thermal conductivity (k) in W/(m⋅K), Thickness (L) in m]
         * @return U = 1/R
         */
        public static double heatTransferCoeffWithConductionAndConvectionOnBothSides(
            double area, double convectionHeatCoeffInner, double convectionHeatCoeffOuter, double[][] layers) {
            final double thermalResistance = reciprocal(area) * (reciprocal(convectionHeatCoeffInner)
                + Arrays.stream(layers)
                .mapToDouble(layer -> layer[Constants.ARR_2ND_INDEX] / layer[Constants.ARR_1ST_INDEX])
                .sum() + reciprocal(convectionHeatCoeffOuter));
            return reciprocal(thermalResistance);
        }

        /**
         * Nu = q_conv/q_cond
         * where:
         * Nu – Nussel number, dimensionless;
         * q_conv – Heat transfer due to convection;
         * q_cond – Heat transfer due to conduction.
         *
         * @param convectionCoeff          W/(m²⋅K)
         * @param fluidThermalConductivity W/(m⋅K)
         * @return Nu = (h꜀×L)/k_f
         */
        public static double nusseltNumber(
            double characteristicLengthMeters, double convectionCoeff, double fluidThermalConductivity) {
            return (convectionCoeff * characteristicLengthMeters) / fluidThermalConductivity;
        }

        /**
         * @return Nu = C×Raⁿ
         */
        public static double nusseltNumberEmpiricalNaturalConvection(
            double naturalConvectionCoeff, double rayleighNumber, double rayleighlCoeff) {
            return naturalConvectionCoeff * Math.pow(rayleighNumber, rayleighlCoeff);
        }

        /**
         * @return Nu = C×Reᵐ×Prⁿ
         */
        public static double nusseltNumberEmpiricalForcedConvection(
            double forcedConvectionCoeff, double reynoldsNumber, double reynoldsExponent,
            double prandtlNumber, double prandtlExponent) {
            return forcedConvectionCoeff * Math.pow(reynoldsNumber, reynoldsExponent)
                * Math.pow(prandtlNumber, prandtlExponent);
        }
    }

    public static final class Atmospheric {
        public static final double DRY_AIR_GAS_CONSTANT = 287.052874; // J/(kg·K)
        public static final double AIR_MOLAR_MASS = 0.0289644; // kg/mol
        public static final double WATER_VAPOR_GAS_CONSTANT = 461.495; // J/(kg·K)
        public static final double CRITICAL_WATER_PRESSURE = 22.064; // MPa
        public static final double CRITICAL_WATER_TEMPERATURE = 647.096; // K

        private Atmospheric() {
        }

        /**
         * @return ρ = P/(R×T). The units are kg/m³
         */
        public static double airDensity(double airPressurePascals, double airTempKelvins) {
            return airPressurePascals / (DRY_AIR_GAS_CONSTANT * airTempKelvins);
        }

        /**
         * <ul>
         *     <li>ρ = volume/mass of air</li>
         *     <li>p_total = p_N2 + p_O2 + p_Ar + p_H2O + ...</li>
         * </ul>
         * where:
         * p_d is the pressure of dry air in hPa or mb;
         * pᵥ is the water vapor pressure in hPa or mb;
         * T is the air temperature in Kelvins;
         * R_d is the specific gas constant for dry air equal to 287.058 J/(kg·K);
         * Rᵥ is the specific gas constant for water vapor equal to 461.495 J/(kg·K).
         *
         * @return ρ = (p_d/(R_dT)) + (pᵥ/(RᵥT)). The units are kg/m³
         */
        public static double moistAirDensity(double airPressurePascals, double airTempKelvins,
                                             double relativeHumidityPercent) {
            final double airTempCelsius = TemperatureUnit.kelvinToCelsius(airTempKelvins);
            final double vaporPressureHPa = waterVaporPressure(airTempCelsius, relativeHumidityPercent);
            final double vaporPressurePa = PressureUnit.hpaToPa(vaporPressureHPa);
            final double dryAirPressurePa = airPressurePascals - vaporPressurePa;
            return (dryAirPressurePa / (DRY_AIR_GAS_CONSTANT * airTempKelvins))
                + (vaporPressurePa / (WATER_VAPOR_GAS_CONSTANT * airTempKelvins));
        }

        /**
         * Magnus water vapor coefficients: β = 17.625 λ = 243.04°C or β = 17.62T λ = 243.12°C.
         *
         * @return Dₚ = (λ×(ln(RH/100) + (βT)/(λ+T))) / (β−(ln(RH/100) + (βT)/(λ+T)). The units are °C
         */
        public static double dewPoint(double airTemperatureCelsius, double relativeHumidityPercent) {
            final double beta = 17.625;
            final double lambda = 243.04;
            final double tmp = ln(relativeHumidityPercent / 100)
                + ((beta * airTemperatureCelsius) / (lambda + airTemperatureCelsius));
            return (lambda * tmp) / (beta - tmp);
        }

        /**
         * p₁ = 6.1078⋅10^(7.5T)/(T+237.3)
         *
         * @return pᵥ = p₁⋅RH. The units are hPa
         */
        public static double waterVaporPressure(double airTemperatureCelsius, double relativeHumidityPercent) {
            final double saturationVaporPressure = 6.1078 * Math.pow(10,
                (7.5 * airTemperatureCelsius) / (airTemperatureCelsius + 237.3));
            return saturationVaporPressure * (relativeHumidityPercent / 100);
        }

        /**
         * @return RH = 100 × (e^(17.625×T)/(243.04+T) / e^(17.625×Dₚ)/(243.04+Dₚ)). The units are %
         */
        public static double relativeHumidity(double tempCelsius, double dewPointCelsius) {
            return 100 * (Math.exp((17.625 * dewPointCelsius) / (243.04 + dewPointCelsius))
                / Math.exp((17.625 * tempCelsius) / (243.04 + tempCelsius)));
        }

        /**
         * @return RH = 100 × P_w / P_ws. The units are %
         */
        public static double relativeHumidityFromVaporPressure(double vaporPressure, double saturationVaporPressure) {
            return 100 * (vaporPressure / saturationVaporPressure);
        }

        /**
         * @return AH = (RH × P)/(R_w × T × 100). The units are kg/m³
         */
        public static double absoluteHumidity(
            double relativeHumidityPercent, double airTempKelvin, double saturationVaporPressurePa) {
            return (relativeHumidityPercent * saturationVaporPressurePa)
                / (WATER_VAPOR_GAS_CONSTANT * airTempKelvin * 100);
        }

        /**
         * where V – Volume of air and water vapor mixture.
         *
         * @return H = m/V. The units are g/m³
         */
        public static double absoluteHumidity(double massOfWaterVapor, double mixtureVolume) {
            return massOfWaterVapor / mixtureVolume;
        }

        /**
         * where:
         * a₁, a₂,..,a₆ – Empirical constants;
         * τ = 1 − T/T꜀.
         *
         * @return Pₛ = P꜀*e^((T꜀/T) * (a₁τ + a₂τ^1.5 + a₃τ³ + a₄τ^3.5 + a₅τ⁴ + a₆τ^7.5)). The units are Pa
         */
        public static double saturationVaporPressureOfWaterAtTemperature(double temperature) {
            final double tau = 1 - temperature / CRITICAL_WATER_TEMPERATURE;
            final double a1 = -7.85951783;
            final double a4 = 22.6807411;
            final double a2 = 1.84408259;
            final double a5 = -15.9618719;
            final double a3 = -11.7866497;
            final double a6 = 1.80122502;
            return CRITICAL_WATER_PRESSURE * Math.exp((CRITICAL_WATER_TEMPERATURE / temperature)
                * (a1 * tau + a2 * Math.pow(tau, 1.5) + a3 * Math.pow(tau, 3) + a4 * Math.pow(tau, 3.5)
                + a5 * Math.pow(tau, 4) + a6 * Math.pow(tau, 7.5)));
        }

        /**
         * P = (Aₛ^0.190263 − (8.417286×10⁻⁵×h))^1/0.190263
         * where:
         * P is the air pressure, in millibars (mbar);
         * Aₛ is the altimeter setting (mbar);
         * h is the weather station elevation (m).
         * <br/>
         * Pᵥ = R_H × 6.1078 × 10^(7.5×T)/(T+273.3)
         * P = P_d + Pᵥ
         *
         * @return ρ = P_d/(R_d×T) + Pᵥ/(Rᵥ×T). The units are kg/m³
         */
        public static double airDensity(double airTemperatureCelsius, double relativeHumidityPercent,
                                        double altimeterSetting, double stationElevation) {
            final double airPressure = Math.pow(
                Math.pow(altimeterSetting, 0.190263) - (8.417286 * 0.00001 * stationElevation),
                reciprocal(0.190263));
            final double waterVaporPressure = waterVaporPressure(airTemperatureCelsius, relativeHumidityPercent);
            final double dryAirPressure = airPressure - waterVaporPressure;
            final double airTempK = TemperatureUnit.celsiusToKelvin(airTemperatureCelsius);
            return PressureUnit.hpaToPa(dryAirPressure) / (DRY_AIR_GAS_CONSTANT * airTempK)
                + PressureUnit.hpaToPa(waterVaporPressure) / (WATER_VAPOR_GAS_CONSTANT * airTempK);
        }

        /**
         * @return H = 44.3308−42.2665×ρ^0.234969. The units are meters
         */
        public static double densityAltitude(double airDensity) {
            return 44.3308 - 42.2665 * Math.pow(airDensity, 0.234969);
        }

        /**
         * @return P = P₀ × e^(-g × M × (h - h₀)/(R × T). The units are Pa
         */
        public static double airPressureAtAltitude(
            double pressureAtSeaLevel, double altitudeMeters, double tempKelvins) {
            // The reference level is located at sea level, h₀ = 0.
            return pressureAtSeaLevel * Math.exp((-GRAVITATIONAL_ACCELERATION_ON_EARTH * AIR_MOLAR_MASS
                * altitudeMeters) / (UNIVERSAL_GAS_CONSTANT * tempKelvins));
        }

        /**
         * The lapse rate = 0.0065°C per meter. For imperial or US customary system: 0.00356.
         *
         * @return The units are °C
         */
        public static double temperatureAtAltitude(double tempAtSeaLevelCelsius, double altitudeMeters) {
            return tempAtSeaLevelCelsius - 0.0065 * altitudeMeters;
        }

        /**
         * where:
         * OAT - Outside Air Temperature correction term. We use it to take into account the temperature conditions
         * prevalent in the air the airplane is in;
         * A - Altitude of the airplane; and
         * TAS & IAS - True airspeed and indicated airspeed, respectively.
         *
         * @return TAS = (IAS * OAT * A / 1000) + IAS. The units are m/s
         */
        public static double trueAirSpeedOATCorrection(
            double meanSeaLevelAltitudeMeters, double indicatedAirSpeed, double oatEstimationCorrectionPercent) {
            return (indicatedAirSpeed * oatEstimationCorrectionPercent / 100 * meanSeaLevelAltitudeMeters / 1000)
                + indicatedAirSpeed;
        }

        /**
         * GS = TAS + W * cos θ
         * where:
         * GS - Ground speed;
         * W - Wind speed;
         * θ - Angle between the wind direction and aircraft's motion.
         *
         * @return The units are m/s
         */
        public static double trueAirSpeedFromWindAndGroundSpeed(
            double groundSpeedMeters, double windSpeedMeters, double windAngleRadians) {
            final double headwind = windSpeedMeters * Trigonometry.cos(windAngleRadians);
            final double crosswind = windSpeedMeters * Trigonometry.sin(windAngleRadians);
            return squareRoot(Math.pow(groundSpeedMeters - headwind, 2) + Math.pow(crosswind, 2));
        }

        /**
         * @return result according to Rothfusz regression formula. The units are °F
         */
        public static double heatIndex(double temperatureFahrenheit, double relativeHumidityPercent) {
            final double t = temperatureFahrenheit;
            final double rh = relativeHumidityPercent;
            final double tSquared = t * t;
            final double rhSquared = rh * rh;
            return -42.379
                + 2.04901523 * t
                + 10.14333127 * rh
                - 0.22475541 * t * rh
                - 0.00683783 * tSquared
                - 0.05481717 * rhSquared
                + 0.00122874 * tSquared * rh
                + 0.00085282 * t * rhSquared
                - 0.00000199 * tSquared * rhSquared;
        }

        /**
         * where:
         * 1.458×10⁻⁶ — Constant;
         * 110.4 — Another empirical constant.
         *
         * @return μ = (1.458×10⁻⁶ × T^3/2) / (T+110.4). The units are mPa·s
         */
        public static double airDynamicViscosity(double tempKelvins) {
            return (0.001458 * Math.pow(tempKelvins, 3. / 2)) / (tempKelvins + 110.4);
        }

        /**
         * @return ν = μ/ρ. The units are St
         */
        public static double airKinematicViscosity(double pressurePascals, double tempKelvins) {
            final double airDensity = airDensity(pressurePascals, tempKelvins);
            final double dynamicViscosity = airDynamicViscosity(tempKelvins);
            return dynamicViscosity / airDensity;
        }

        /**
         * For °F, cloud base = (temperature - dew point) / 4.4 × 1000 + elevation
         *
         * @return cloud base = (temperature - dew point) / 10 × 1247 + elevation. The units are °C
         */
        public static double cloudBaseAltitude(double tempCelsius, double dewPointCelsius, double elevationMeters) {
            return (tempCelsius - dewPointCelsius) / 10 * 1247 + elevationMeters;
        }

        /**
         * For °F, cloud temperature = temperature - 5.4 × (cloud base - elevation) / 1000
         *
         * @return cloud temperature = temperature - 0.984 × (cloud base - elevation) / 100. The units are °C
         */
        public static double cloudBase(double tempCelsius, double cloudBaseAltitude, double elevationMeters) {
            return tempCelsius - 0.984 * (cloudBaseAltitude - elevationMeters) / 100;
        }
    }

    public static final class Astrophysics {
        private Astrophysics() {
        }

        /**
         * The energy lost to the gravitational waves in merging ≈ 0.02625 2.625% (0.21/8)
         * 1 - energy lost = 0.97375
         *
         * @param blackHoleMass     in Suns
         * @param fallingObjectMass in Suns
         * @return The units are Suns
         */
        public static double finalBlackHoleMass(double blackHoleMass, double fallingObjectMass) {
            return (blackHoleMass + fallingObjectMass) * 0.97375;
        }

        public static double finalBlackHoleEventHorizonRadius(double eventHorizonRadiusKm, double eventHorizonGrowth) {
            return eventHorizonRadiusKm + (eventHorizonRadiusKm * eventHorizonGrowth);
        }

        /**
         * @param fallingObjectMass in Suns
         * @return The units are bethe (foe)
         */
        public static double blackHoleEnergyRelease(double fallingObjectMass) {
            return MassUnit.ergsToBethe(fallingObjectMass * SUN_POTENTIAL_ENERGY_ERGS);
        }

        /**
         * @param radiusMeters event horizon radius
         * @return (G*M)/r². The units are m/s²
         */
        public static double blackHoleGravitationalField(double massKg, double radiusMeters) {
            return (GRAVITATIONAL_CONSTANT * massKg) / (radiusMeters * radiusMeters);
        }

        /**
         * @return GPE = -G*M*m/r. The units are joules
         */
        public static double gravitationalPotentialEnergy(
            double largeObjectMassKg, double smallObjectMassKg, double distanceMeters) {
            return -GRAVITATIONAL_CONSTANT * largeObjectMassKg * smallObjectMassKg / distanceMeters;
        }

        /**
         * @return T = (ℏc³)/(8πGMk_B). The units are kelvins
         */
        public static double blackHoleTemperature(double massKg) {
            return (REDUCED_PLANCK_CONSTANT * Math.pow(SPEED_OF_LIGHT, 3))
                / (Trigonometry.PI8 * GRAVITATIONAL_CONSTANT * massKg * BOLTZMANN_CONSTANT);
        }
    }
}
