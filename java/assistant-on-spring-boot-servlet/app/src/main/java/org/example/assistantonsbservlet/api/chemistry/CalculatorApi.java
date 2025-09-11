package org.example.assistantonsbservlet.api.chemistry;

import org.example.assistantonsbservlet.api.chemistry.model.req.ChemCalculateMolarMassReq;
import org.example.assistantonsbservlet.api.chemistry.model.req.ChemCalculateMoleReq;
import org.example.assistantonsbservlet.api.model.resp.CalculatorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/chemistry/calculator")
public interface CalculatorApi {
    @PostMapping(value = "/molar-mass")
    ResponseEntity<CalculatorResponse> calculate(@RequestBody ChemCalculateMolarMassReq body);

    @PostMapping(value = "/mole")
    ResponseEntity<CalculatorResponse> calculate(@RequestBody ChemCalculateMoleReq body);
}
