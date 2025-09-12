package org.example.assistantonsbservlet.math;

import org.example.assistantonsbservlet.api.math.model.CalculateRightTriangleReq;
import org.example.assistantonsbservlet.api.model.resp.CalculatorScalarResponse;

public sealed interface RightTriangleCalculator extends TriangleCalculator permits RightTriangleCalc {
    CalculatorScalarResponse calculate(CalculateRightTriangleReq request);
}
