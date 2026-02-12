## Checklist: Computing an Integral in Java (Physics – Velocity Problems)

### ✅ Steps to Follow

1. **Define the Velocity Function**
   - Use a Java functional interface (e.g., `DoubleUnaryOperator`) to represent the velocity as a function of time.

2. **Choose the Integration Method**
   - For most cases, use numerical methods like the Trapezoidal Rule or Simpson’s Rule.
   - For simple problems, Trapezoidal Rule is easy to implement and understand.

3. **Set Integration Bounds and Step Size**
   - Define the start and end time (e.g., `startTime`, `endTime`).
   - Choose a reasonable step size (e.g., `timeStep`) for accuracy.

4. **Implement the Numerical Integration**
   - Loop over the interval, summing the area under the curve using the chosen method.

5. **Test with Known Functions**
   - Validate your implementation with functions where the integral is known (e.g., constant velocity).

6. **Handle Edge Cases**
   - Check for negative step sizes, zero intervals, or invalid bounds.

7. **Document and Use Descriptive Variable Names**
   - Use names like `velocityFunction`, `startTime`, `endTime`, `timeStep`, `displacement`, etc.

---

### ⚠️ Things to Avoid

- **Hardcoding Values**
  - Parameterize your function, bounds, and step size.

- **Ignoring Step Size Impact**
  - Too large a step size reduces accuracy; too small increases computation time.

- **Not Handling Exceptions**
  - Handle invalid input and potential arithmetic errors.

- **Skipping Validation**
  - Always test with simple cases to ensure correctness.

---

### 👀 Watch Out For

- **Floating Point Precision**
  - Be aware of rounding errors in numerical integration.

- **Performance for Large Intervals**
  - Optimize step size and consider parallelization for large datasets.

- **Function Discontinuities**
  - If the velocity function is discontinuous, numerical methods may be inaccurate.

---

## Code Samples

### Good Example

```java
import java.util.function.DoubleUnaryOperator;

public class PhysicsIntegration {
    // Numerical integration using the Trapezoidal Rule
    public static double computeDisplacement(
            DoubleUnaryOperator velocityFunction,
            double startTime,
            double endTime,
            double timeStep) {
        if (timeStep <= 0 || endTime <= startTime) {
            throw new IllegalArgumentException("Invalid time interval or step size.");
        }
        double displacement = 0.0;
        double currentTime = startTime;
        while (currentTime < endTime) {
            double nextTime = Math.min(currentTime + timeStep, endTime);
            double averageVelocity = (velocityFunction.applyAsDouble(currentTime) +
                                      velocityFunction.applyAsDouble(nextTime)) / 2.0;
            displacement += averageVelocity * (nextTime - currentTime);
            currentTime = nextTime;
        }
        return displacement;
    }

    public static void main(String[] args) {
        DoubleUnaryOperator velocityFunction = time -> 10.0 * time; // Example: v(t) = 10t
        double startTime = 0.0;
        double endTime = 5.0;
        double timeStep = 0.01;
        double displacement = computeDisplacement(velocityFunction, startTime, endTime, timeStep);
        System.out.println("Displacement: " + displacement);
    }
}
```

### Bad Example

```java
public class BadIntegration {
    // Poor variable names, no validation, hardcoded values
    public static double integrate(DoubleUnaryOperator f) {
        double s = 0;
        for (double t = 0; t < 5; t += 1) {
            s += f.applyAsDouble(t);
        }
        return s;
    }

    public static void main(String[] args) {
        DoubleUnaryOperator v = t -> 10 * t;
        System.out.println(integrate(v));
    }
}
```
**Problems:**
- Uses single-letter variable names (`s`, `t`, `v`).
- Hardcoded bounds and step size.
- No validation or averaging for Trapezoidal Rule.
- Low accuracy due to large step size.

---

## References

1. Oracle Java Documentation:  
   - Functional Interfaces: [https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/function/DoubleUnaryOperator.html](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/function/DoubleUnaryOperator.html)
