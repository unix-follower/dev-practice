package org.example.assistantonsbservlet.math;

import org.example.assistantonsbservlet.api.math.model.CalculateRightTriangleReq;
import org.example.assistantonsbservlet.api.model.resp.CalculatorResponse;

public interface RightTriangleCalculator extends TriangleCalculator {
    CalculatorResponse calculate(CalculateRightTriangleReq request);
}
