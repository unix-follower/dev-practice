## Checklist: Computing Derivatives in Java (Physics – Velocity Problems)

### ✅ Steps to Follow

1. **Define the Function to Differentiate**
   - Use a Java functional interface (e.g., `DoubleUnaryOperator`) to represent the function (e.g., position as a function of time).

2. **Choose the Differentiation Method**
   - For most cases, use numerical methods like finite differences (forward, backward, central).
   - Central difference is generally more accurate for smooth functions.

3. **Set Differentiation Point and Step Size**
   - Define the point at which to compute the derivative (e.g., `timePoint`).
   - Choose a reasonable step size (e.g., `timeStep`) for accuracy.

4. **Implement the Numerical Differentiation**
   - For first derivative (velocity):  
     - Central difference: `(f(x + h) - f(x - h)) / (2 * h)`
   - For second derivative (acceleration):  
     - Central difference: `(f(x + h) - 2 * f(x) + f(x - h)) / (h * h)`
   - For higher-order derivatives, extend the finite difference formulas.

5. **Test with Known Functions**
   - Validate your implementation with functions where the derivative is known (e.g., position = `10 * time^2` → velocity = `20 * time`).

6. **Handle Edge Cases**
   - Check for negative or zero step sizes, invalid points, or discontinuities.

7. **Document and Use Descriptive Variable Names**
   - Use names like `positionFunction`, `velocityFunction`, `timePoint`, `timeStep`, `acceleration`, etc.

---

### ⚠️ Things to Avoid

- **Hardcoding Values**
  - Parameterize your function, point, and step size.

- **Ignoring Step Size Impact**
  - Too large a step size reduces accuracy; too small increases rounding errors.

- **Not Handling Exceptions**
  - Handle invalid input and potential arithmetic errors.

- **Skipping Validation**
  - Always test with simple cases to ensure correctness.

---

### 👀 Watch Out For

- **Floating Point Precision**
  - Be aware of rounding errors in numerical differentiation.

- **Performance for Large Datasets**
  - Optimize step size and consider parallelization for large datasets.

- **Function Discontinuities**
  - If the function is discontinuous, numerical methods may be inaccurate.

---

## Code Samples

### Good Example

```java
import java.util.function.DoubleUnaryOperator;

public class PhysicsDifferentiation {
    // First derivative (velocity) using central difference
    public static double computeFirstDerivative(
            DoubleUnaryOperator positionFunction,
            double timePoint,
            double timeStep) {
        if (timeStep <= 0) {
            throw new IllegalArgumentException("Step size must be positive.");
        }
        double forwardValue = positionFunction.applyAsDouble(timePoint + timeStep);
        double backwardValue = positionFunction.applyAsDouble(timePoint - timeStep);
        return (forwardValue - backwardValue) / (2 * timeStep);
    }

    // Second derivative (acceleration) using central difference
    public static double computeSecondDerivative(
            DoubleUnaryOperator positionFunction,
            double timePoint,
            double timeStep) {
        if (timeStep <= 0) {
            throw new IllegalArgumentException("Step size must be positive.");
        }
        double forwardValue = positionFunction.applyAsDouble(timePoint + timeStep);
        double currentValue = positionFunction.applyAsDouble(timePoint);
        double backwardValue = positionFunction.applyAsDouble(timePoint - timeStep);
        return (forwardValue - 2 * currentValue + backwardValue) / (timeStep * timeStep);
    }

    public static void main(String[] args) {
        DoubleUnaryOperator positionFunction = time -> 10.0 * time * time; // Example: s(t) = 10t^2
        double timePoint = 5.0;
        double timeStep = 0.01;
        double velocity = computeFirstDerivative(positionFunction, timePoint, timeStep);
        double acceleration = computeSecondDerivative(positionFunction, timePoint, timeStep);
        System.out.println("Velocity at t=5: " + velocity);
        System.out.println("Acceleration at t=5: " + acceleration);
    }
}
```

### Bad Example

```java
public class BadDifferentiation {
    // Poor variable names, no validation, hardcoded values
    public static double diff(DoubleUnaryOperator f) {
        double h = 1;
        return (f.applyAsDouble(5 + h) - f.applyAsDouble(5 - h)) / (2 * h);
    }

    public static void main(String[] args) {
        DoubleUnaryOperator s = t -> 10 * t * t;
        System.out.println(diff(s));
    }
}
```
**Problems:**
- Uses single-letter variable names (`h`, `f`, `s`).
- Hardcoded point and step size.
- No validation or error handling.
- Low accuracy due to large step size.

---

## References

1. Oracle Java Documentation:  
   - Functional Interfaces: [https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/function/DoubleUnaryOperator.html](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/function/DoubleUnaryOperator.html)
