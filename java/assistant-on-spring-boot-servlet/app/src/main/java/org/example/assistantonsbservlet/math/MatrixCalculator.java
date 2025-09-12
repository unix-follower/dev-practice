package org.example.assistantonsbservlet.math;

import org.example.assistantonsbservlet.api.math.model.CalculateMatrixAddReq;
import org.example.assistantonsbservlet.api.model.resp.CalculatorMatrixResponse;

public sealed interface MatrixCalculator permits MatrixCalc {
    CalculatorMatrixResponse calculate(CalculateMatrixAddReq request);
}
