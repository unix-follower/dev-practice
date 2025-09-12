package org.example.assistantonsbservlet.api.math.model;

import java.util.Arrays;
import java.util.Objects;

public record CalculateMatrixAddReq(
    String solveFor,
    double[][] a,
    double[][] b,
    Double scalar
) {
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CalculateMatrixAddReq that)) {
            return false;
        }
        return Objects.deepEquals(a, that.a)
            && Objects.deepEquals(b, that.b)
            && Objects.equals(scalar, that.scalar)
            && Objects.equals(solveFor, that.solveFor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(solveFor, Arrays.deepHashCode(a), Arrays.deepHashCode(b), scalar);
    }
}
