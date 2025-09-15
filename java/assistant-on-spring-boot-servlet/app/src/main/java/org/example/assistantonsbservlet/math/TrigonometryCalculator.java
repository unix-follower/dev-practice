package org.example.assistantonsbservlet.math;

import org.example.assistantonsbservlet.api.math.model.CalculateCosineReq;
import org.example.assistantonsbservlet.api.math.model.CalculateSineReq;
import org.example.assistantonsbservlet.api.model.resp.CalculatorScalarResponse;

public sealed interface TrigonometryCalculator permits TrigCalc {
    CalculatorScalarResponse calculate(CalculateCosineReq request);

    CalculatorScalarResponse calculate(CalculateSineReq request);
}
