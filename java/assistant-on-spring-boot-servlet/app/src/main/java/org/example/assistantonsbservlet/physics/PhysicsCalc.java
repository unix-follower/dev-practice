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
    public static final double COULOMB_CONSTANT = 8.98755e9; // (N⋅m²)/C²

    public static final short HORSEPOWER = 746;
    public static final double BOLTZMANN_CONSTANT = 1.380649e-23; // J/K
    public static final double REF_VOLTAGE_FOR_0_DBU = 0.77459667;
    public static final double VACUUM_PERMITTIVITY = 8.854187818814e-12; // ε₀≈8.8541×10⁻¹² F/m
    public static final double VACUUM_PERMEABILITY = Trigonometry.PI4 * 1e-7; // μ₀≈4π×10⁻⁷ H/m
    public static final short NORMAL_ROOM_TEMP_SOUND_SPEED = 343; // at 20°C; m/s or 1130 ft/s
    public static final double SOUND_SPEED_IN_DRY_AIR = 331.3; // at 0°C; m/s
    public static final double SOUND_SPEED_IN_AIR_KELVIN_REF_POINT = 273.15; // at 0°C
    public static final int FPE_CONSTANT = 450_240; // foot-pounds of energy
    public static final double GRAVITATIONAL_CONSTANT = 6.6743e-11; // 6.6743 × 10⁻¹¹ m³ kg⁻¹ s⁻²
    public static final int SPEED_OF_LIGHT = 29_9792_458; // m/s
    public static final byte MONOATOMIC_GAS_DEGREES_OF_FREEDOM = 3;

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
            return LinearAlgebra.vectorMagnitude(velocityVector);
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

        // Rotational and periodic motion

        /**
         * where:
         * <ul>
         *     <li>m — The mass of the particle;</li>
         *     <li>v — Cyclotron speed;</li>
         *     <li>r — The radius of the revolution.</li>
         * </ul>
         *
         * @return Fc = m × v²/r
         */
        public static double centripetalForce() {
            throw new UnsupportedOperationException();
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
    }

    public static final class FluidMechanics {
        private FluidMechanics() {
        }

        /**
         * @return p = ρ × v² / 2. The units are pascals
         */
        public static double dynamicPressure(double forceNewtons, double areaSquareMeters) {
            return forceNewtons / areaSquareMeters;
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
            final double crossSectionalArea = crossSectionalAreaOfCircularWire(Geometry.circleDiameter(radiusMeters));
            return VACUUM_PERMEABILITY * numberOfTurns * numberOfTurns * crossSectionalArea / lengthMeters;
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
    }

    public static final class Atmospheric {
        public static final double DRY_AIR_GAS_CONSTANT = 287.052874; // J/(kg·K)
        public static final double WATER_VAPOR_GAS_CONSTANT = 461.495; // J/(kg·K)

        private Atmospheric() {
        }

        /**
         * @return ρ = P/(R×T). The units are kg/m³
         */
        public static double dryAirDensity(double airPressurePascals, double airTempKelvins) {
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
         * α = ln(RH/100) + ((17.62T)/(243.12+T))
         *
         * @return DP = (243.12α)/(17.62−α). The units are Celsius
         */
        public static double moistAirDensityDewPoint(double airTemperatureCelsius, double relativeHumidityPercent) {
            final double waterVaporCoeff = 17.62;
            final double waterVaporCoeff2 = 243.12;
            final double alpha = Algebra.ln(relativeHumidityPercent / 100)
                + ((waterVaporCoeff * airTemperatureCelsius) / (waterVaporCoeff2 + airTemperatureCelsius));
            return (waterVaporCoeff2 * alpha) / (waterVaporCoeff - alpha);
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
    }
}
