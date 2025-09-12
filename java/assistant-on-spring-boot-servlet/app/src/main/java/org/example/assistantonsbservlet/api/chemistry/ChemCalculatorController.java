package org.example.assistantonsbservlet.api.chemistry;

import org.example.assistantonsbservlet.api.ErrorCode;
import org.example.assistantonsbservlet.api.chemistry.model.req.ChemCalculatorFormulaInput;
import org.example.assistantonsbservlet.api.chemistry.model.req.ChemCalculateMolarMassReq;
import org.example.assistantonsbservlet.api.chemistry.model.req.ChemCalculateMoleReq;
import org.example.assistantonsbservlet.api.model.resp.CalculatorScalarResponse;
import org.example.assistantonsbservlet.chemistry.ChemCalculatorApiFacade;
import org.example.assistantonsbservlet.exception.ChemistryApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChemCalculatorController implements CalculatorApi {
    private final ChemCalculatorApiFacade facade;

    public ChemCalculatorController(ChemCalculatorApiFacade facade) {
        this.facade = facade;
    }

    @Override
    public ResponseEntity<CalculatorScalarResponse> calculate(ChemCalculateMolarMassReq body) {
        if (isUnknownSubstance(body)) {
            throw new ChemistryApiException(ErrorCode.INVALID_INPUT);
        }

        final var response = facade.calculate(body);
        return ResponseEntity.ok(response);
    }

    private static boolean isUnknownSubstance(ChemCalculatorFormulaInput body) {
        return body.formula() == null && body.smiles() == null;
    }

    @Override
    public ResponseEntity<CalculatorScalarResponse> calculate(ChemCalculateMoleReq body) {
        if (isUnknownSubstance(body) && body.mass() == null && body.molecularWeight() == null) {
            throw new ChemistryApiException(ErrorCode.INVALID_INPUT);
        }

        final var response = facade.calculate(body);
        return ResponseEntity.ok(response);
    }
}
