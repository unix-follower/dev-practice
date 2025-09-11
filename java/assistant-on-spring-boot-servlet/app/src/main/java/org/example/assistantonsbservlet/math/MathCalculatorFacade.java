package org.example.assistantonsbservlet.math;

import org.example.assistantonsbservlet.api.math.model.CalculateRightTriangleReq;
import org.example.assistantonsbservlet.api.model.resp.CalculatorResponse;
import org.springframework.stereotype.Component;

@Component
public final class MathCalculatorFacade implements MathCalculatorApiFacade {
    @Override
    public CalculatorResponse calculate(CalculateRightTriangleReq body) {
        final var calc = new RightTriangleCalc();
        return calc.calculate(body);
    }
}
