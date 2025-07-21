package com.example.assistantonsbservlet.api.pubchem;

import com.example.assistantonsbservlet.api.pubchem.model.FoodAdditiveSubstanceResponseDto;
import com.example.assistantonsbservlet.svc.FoodAdditiveSubstanceApiFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FoodAdditiveSubstanceController implements FoodAdditiveSubstanceApi {
    private final FoodAdditiveSubstanceApiFacade facade;

    public FoodAdditiveSubstanceController(FoodAdditiveSubstanceApiFacade facade) {
        this.facade = facade;
    }

    @Override
    public ResponseEntity<List<FoodAdditiveSubstanceResponseDto>> getAll(int page, int size) {
        final var substanceDtoList = facade.getAll(page, size);
        return ResponseEntity.ok(substanceDtoList);
    }
}
