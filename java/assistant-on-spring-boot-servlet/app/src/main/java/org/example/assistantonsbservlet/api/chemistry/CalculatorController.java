package org.example.assistantonsbservlet.api.chemistry;

import org.example.assistantonsbservlet.api.ErrorCode;
import org.example.assistantonsbservlet.api.chemistry.model.req.ChemistryCalculatorReq;
import org.example.assistantonsbservlet.api.chemistry.model.resp.ChemistryCalculatorResp;
import org.example.assistantonsbservlet.exception.ChemistryApiException;
import org.example.assistantonsbservlet.chemistry.CalculatorApiFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculatorController implements CalculatorApi {
    private final CalculatorApiFacade facade;

    public CalculatorController(CalculatorApiFacade facade) {
        this.facade = facade;
    }

    @Override
    public ResponseEntity<ChemistryCalculatorResp> calculateMolarMass(ChemistryCalculatorReq body) {
        if (body.formula() == null && body.smiles() == null) {
            throw new ChemistryApiException(ErrorCode.INVALID_INPUT);
        }
        final var response = facade.calculateMolarMass(body);
        return ResponseEntity.ok(response);
    }
}
