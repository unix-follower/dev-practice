package org.example.assistantonsbservlet.api.chemistry;

import org.example.assistantonsbservlet.api.ChemistryGraphResponse;
import org.example.assistantonsbservlet.api.chemistry.model.CompoundSDFDataResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/v1/chemistry/compound")
public interface CompoundApi {
    @GetMapping(value = "/graph", params = "name")
    ResponseEntity<ChemistryGraphResponse> getCompoundGraphDataByName(
        @RequestParam String name,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int pageSize
    );

    @GetMapping("/graph")
    ResponseEntity<ChemistryGraphResponse> getAllGraphs(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int pageSize
    );

    @GetMapping("/{cid}")
    ResponseEntity<CompoundSDFDataResponse> getCompoundSDFDataByCid(@PathVariable long cid);
}
