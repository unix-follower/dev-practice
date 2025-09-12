package org.example.assistantonsbservlet.api.math;

import org.example.assistantonsbservlet.api.math.model.CalculateHypotenuseReq;
import org.example.assistantonsbservlet.api.math.model.CalculateRightTriangleReq;
import org.example.assistantonsbservlet.api.model.resp.CalculatorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/math/calculator")
public interface CalculatorApi {
    @PostMapping(value = "/right-triangle")
    ResponseEntity<CalculatorResponse> calculate(@RequestBody CalculateRightTriangleReq body);

    @PostMapping(value = "/hypotenuse")
    ResponseEntity<CalculatorResponse> calculate(@RequestBody CalculateHypotenuseReq body);
}
