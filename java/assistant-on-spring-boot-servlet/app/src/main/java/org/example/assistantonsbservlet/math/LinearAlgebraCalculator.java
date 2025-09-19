package org.example.assistantonsbservlet.math;

import org.apache.commons.math.linear.EigenDecompositionImpl;
import org.apache.commons.math.linear.LUDecompositionImpl;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.SingularValueDecompositionImpl;

public interface LinearAlgebraCalculator {
    static double determinant(double[][] matrix) {
        final var realMatrix = MatrixUtils.createRealMatrix(matrix);
        return new LUDecompositionImpl(realMatrix).getDeterminant();
    }

    static double l2norm(double[][] matrix) {
        final var realMatrix = MatrixUtils.createRealMatrix(matrix);
        return new SingularValueDecompositionImpl(realMatrix).getNorm();
    }

    static double l2norm(double[] vector) {
        final var realVector  = MatrixUtils.createRealVector(vector);
        return realVector.getNorm();
    }

    static double rank(double[][] matrix) {
        final var realMatrix = MatrixUtils.createRealMatrix(matrix);
        return new SingularValueDecompositionImpl(realMatrix).getRank();
    }

    static double[] eigenvalues(double[][] matrix) {
        final var realMatrix = MatrixUtils.createRealMatrix(matrix);
        return new EigenDecompositionImpl(realMatrix, 0).getRealEigenvalues();
    }
}
