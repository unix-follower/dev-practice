package org.example.assistantonsbservlet.math;

import org.example.assistantonsbservlet.api.math.model.CalculateRightTriangleReq;
import org.example.assistantonsbservlet.api.model.resp.CalculatorResponse;

public sealed interface MathCalculatorApiFacade permits MathCalculatorFacade {
    CalculatorResponse calculate(CalculateRightTriangleReq body);
}
