package org.example.assistantonsbservlet.api.math;

import org.example.assistantonsbservlet.api.math.model.CalculateHypotenuseReq;
import org.example.assistantonsbservlet.api.math.model.CalculateMatrixAddReq;
import org.example.assistantonsbservlet.api.math.model.CalculateRightTriangleReq;
import org.example.assistantonsbservlet.api.model.resp.CalculatorMatrixResponse;
import org.example.assistantonsbservlet.api.model.resp.CalculatorScalarResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/math/calculator")
public interface CalculatorApi {
    @PostMapping(value = "/right-triangle")
    ResponseEntity<CalculatorScalarResponse> calculate(@RequestBody CalculateRightTriangleReq body);

    @PostMapping(value = "/hypotenuse")
    ResponseEntity<CalculatorScalarResponse> calculate(@RequestBody CalculateHypotenuseReq body);

    @PostMapping(value = "/linear-algebra/matrix-addition")
    ResponseEntity<CalculatorMatrixResponse> calculate(@RequestBody CalculateMatrixAddReq body);
}
