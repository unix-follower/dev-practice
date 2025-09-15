package org.example.assistantonsbservlet.math;

import org.example.assistantonsbservlet.api.math.model.CalculateCosineReq;
import org.example.assistantonsbservlet.api.math.model.CalculateHypotenuseReq;
import org.example.assistantonsbservlet.api.math.model.CalculateMatrixAddReq;
import org.example.assistantonsbservlet.api.math.model.CalculateRightTriangleReq;
import org.example.assistantonsbservlet.api.math.model.CalculateSineReq;
import org.example.assistantonsbservlet.api.math.model.CalculateTanReq;
import org.example.assistantonsbservlet.api.model.resp.CalculatorMatrixResponse;
import org.example.assistantonsbservlet.api.model.resp.CalculatorScalarResponse;
import org.springframework.stereotype.Component;

@Component
public final class MathCalculatorFacade implements MathCalculatorApiFacade {
    @Override
    public CalculatorScalarResponse calculate(CalculateRightTriangleReq body) {
        final var calc = new RightTriangleCalc();
        return calc.calculate(body);
    }

    @Override
    public CalculatorScalarResponse calculate(CalculateHypotenuseReq body) {
        final var calc = new HypotenuseCalc();
        return calc.calculate(body);
    }

    @Override
    public CalculatorMatrixResponse calculate(CalculateMatrixAddReq body) {
        final var calc = new MatrixCalc();
        return calc.calculate(body);
    }

    @Override
    public CalculatorScalarResponse calculate(CalculateCosineReq body) {
        final var calc = new TrigCalc();
        return calc.calculate(body);
    }

    @Override
    public CalculatorScalarResponse calculate(CalculateSineReq body) {
        final var calc = new TrigCalc();
        return calc.calculate(body);
    }

    @Override
    public CalculatorScalarResponse calculate(CalculateTanReq body) {
        final var calc = new TrigCalc();
        return calc.calculate(body);
    }
}
