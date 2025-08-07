package org.example.assistantonsbservlet.api.chemistry.organic.dbs.pubchem.fda;

import org.example.assistantonsbservlet.api.chemistry.organic.dbs.pubchem.fda.model.FoodAdditiveSubstanceResponseDto;
import org.example.assistantonsbservlet.svc.chemistry.PubChemFdaApiFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FoodAdditiveSubstanceController implements FoodAdditiveSubstanceApi {
    private final PubChemFdaApiFacade facade;

    public FoodAdditiveSubstanceController(PubChemFdaApiFacade facade) {
        this.facade = facade;
    }

    @Override
    public ResponseEntity<List<FoodAdditiveSubstanceResponseDto>> getAll(int page, int size) {
        final var substanceDtoList = facade.getAll(page, size);
        return ResponseEntity.ok(substanceDtoList);
    }
}
