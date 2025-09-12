package org.example.assistantonsbservlet.api.model.resp;

import java.util.Arrays;
import java.util.Objects;

public record CalculatorMatrixResponse(
    double[][] result
) {
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CalculatorMatrixResponse that)) {
            return false;
        }
        return Objects.deepEquals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(result);
    }
}
