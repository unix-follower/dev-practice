package org.example.assistantonsbservlet.api.chemistry.graph;

import org.example.assistantonsbservlet.api.ChemistryGraphResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/v1/chemistry/graph/compound")
public interface CompoundGraphApi {
    @GetMapping(params = "name")
    ResponseEntity<ChemistryGraphResponse> getCompoundDataByName(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int pageSize,
        @RequestParam String name
    );

    @GetMapping
    ResponseEntity<ChemistryGraphResponse> getAll(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int pageSize
    );
}
