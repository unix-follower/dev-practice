package org.example.assistantonsbservlet.api.chemistry.organic.dbs.pubchem.fda;

import org.example.assistantonsbservlet.api.chemistry.organic.dbs.pubchem.fda.model.FoodAdditiveSubstanceResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/api/v1/chemistry/food-additives")
public interface FoodAdditiveSubstanceApi {
    @GetMapping
    ResponseEntity<List<FoodAdditiveSubstanceResponseDto>> getAll(@RequestParam int page, @RequestParam int size);
}
