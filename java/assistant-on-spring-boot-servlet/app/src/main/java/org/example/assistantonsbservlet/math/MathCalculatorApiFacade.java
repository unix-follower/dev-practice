package org.example.assistantonsbservlet.math;

import org.example.assistantonsbservlet.api.math.model.CalculateCosineReq;
import org.example.assistantonsbservlet.api.math.model.CalculateHypotenuseReq;
import org.example.assistantonsbservlet.api.math.model.CalculateMatrixAddReq;
import org.example.assistantonsbservlet.api.math.model.CalculateRightTriangleReq;
import org.example.assistantonsbservlet.api.model.resp.CalculatorMatrixResponse;
import org.example.assistantonsbservlet.api.model.resp.CalculatorScalarResponse;

public sealed interface MathCalculatorApiFacade permits MathCalculatorFacade {
    CalculatorScalarResponse calculate(CalculateRightTriangleReq body);

    CalculatorScalarResponse calculate(CalculateHypotenuseReq body);

    CalculatorMatrixResponse calculate(CalculateMatrixAddReq body);

    CalculatorScalarResponse calculate(CalculateCosineReq body);
}
