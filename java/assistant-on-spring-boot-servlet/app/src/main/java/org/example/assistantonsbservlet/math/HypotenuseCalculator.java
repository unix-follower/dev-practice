package org.example.assistantonsbservlet.math;

import org.example.assistantonsbservlet.api.math.model.CalculateHypotenuseReq;
import org.example.assistantonsbservlet.api.model.resp.CalculatorResponse;

public sealed interface HypotenuseCalculator extends TriangleCalculator permits HypotenuseCalc {
    CalculatorResponse calculate(CalculateHypotenuseReq request);
}
