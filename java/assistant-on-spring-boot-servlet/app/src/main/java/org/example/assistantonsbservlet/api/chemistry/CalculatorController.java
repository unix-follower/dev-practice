package org.example.assistantonsbservlet.api.chemistry;

import org.example.assistantonsbservlet.api.ErrorCode;
import org.example.assistantonsbservlet.api.chemistry.model.req.ChemCalculatorFormulaInput;
import org.example.assistantonsbservlet.api.chemistry.model.req.ChemCalculateMolarMassReq;
import org.example.assistantonsbservlet.api.chemistry.model.req.ChemCalculateMoleReq;
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
    public ResponseEntity<ChemistryCalculatorResp> calculateMolarMass(ChemCalculateMolarMassReq body) {
        if (isUnknownSubstance(body)) {
            throw new ChemistryApiException(ErrorCode.INVALID_INPUT);
        }

        final var response = facade.calculateMolarMass(body);
        return ResponseEntity.ok(response);
    }

    private static boolean isUnknownSubstance(ChemCalculatorFormulaInput body) {
        return body.formula() == null && body.smiles() == null;
    }

    @Override
    public ResponseEntity<ChemistryCalculatorResp> calculateMole(ChemCalculateMoleReq body) {
        if (isUnknownSubstance(body) && body.mass() == null && body.molecularWeight() == null) {
            throw new ChemistryApiException(ErrorCode.INVALID_INPUT);
        }

        final var response = facade.calculateMole(body);
        return ResponseEntity.ok(response);
    }
}
