package org.example.assistantonsbservlet.math;

import org.apache.commons.math.linear.MatrixUtils;
import org.example.assistantonsbservlet.api.math.model.CalculateMatrixAddReq;
import org.example.assistantonsbservlet.api.model.resp.CalculatorMatrixResponse;

public final class MatrixCalc implements MatrixCalculator {
    @Override
    public CalculatorMatrixResponse calculate(CalculateMatrixAddReq request) {
        switch (request.solveFor()) {
            case Constants.MATRIX_PLUS_MATRIX -> {
                return matrixPlusMatrix(request);
            }
            case Constants.MATRIX_A_PLUS_SCALAR -> {
                return matrixPlusScalar(request);
            }
            default -> throw new UnsupportedOperationException();
        }
    }

    private CalculatorMatrixResponse matrixPlusMatrix(CalculateMatrixAddReq request) {
        final var leftMatrix = MatrixUtils.createRealMatrix(request.a());
        final var rightMatrix = MatrixUtils.createRealMatrix(request.b());
        final var resultMatrix = leftMatrix.add(rightMatrix);
        return new CalculatorMatrixResponse(resultMatrix.getData());
    }

    private CalculatorMatrixResponse matrixPlusScalar(CalculateMatrixAddReq request) {
        final var matrix = MatrixUtils.createRealMatrix(request.a());
        final var resultMatrix = matrix.scalarAdd(request.scalar());
        return new CalculatorMatrixResponse(resultMatrix.getData());
    }
}
