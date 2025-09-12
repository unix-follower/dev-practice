package org.example.assistantonsbservlet.math;

import org.example.assistantonsbservlet.api.math.model.CalculateRightTriangleReq;
import org.example.assistantonsbservlet.api.model.resp.CalculatorResponse;

public sealed interface RightTriangleCalculator extends TriangleCalculator permits RightTriangleCalc {
    CalculatorResponse calculate(CalculateRightTriangleReq request);
}
