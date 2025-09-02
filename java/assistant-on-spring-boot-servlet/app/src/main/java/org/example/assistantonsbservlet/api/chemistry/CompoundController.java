package org.example.assistantonsbservlet.api.chemistry;

import org.example.assistantonsbservlet.api.ChemistryGraphResponse;
import org.example.assistantonsbservlet.api.chemistry.model.CompoundSDFDataResponse;
import org.example.assistantonsbservlet.svc.chemistry.PubChemFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompoundController implements CompoundApi {
    private final PubChemFacade facade;

    public CompoundController(PubChemFacade facade) {
        this.facade = facade;
    }

    @Override
    public ResponseEntity<ChemistryGraphResponse> getCompoundGraphDataByName(String name, int page, int pageSize) {
        final var graphResponse = facade.getCompoundDataByName(name, page, pageSize);
        return ResponseEntity.ok(graphResponse);
    }

    @Override
    public ResponseEntity<ChemistryGraphResponse> getAllGraphs(int page, int pageSize) {
        final var graphResponse = facade.getAllGraphs(page, pageSize);
        return ResponseEntity.ok(graphResponse);
    }

    @Override
    public ResponseEntity<CompoundSDFDataResponse> getCompoundSDFDataByCid(long cid) {
        final var response = facade.getCompoundSDFDataByCid(cid);
        return ResponseEntity.ok(response);
    }
}
