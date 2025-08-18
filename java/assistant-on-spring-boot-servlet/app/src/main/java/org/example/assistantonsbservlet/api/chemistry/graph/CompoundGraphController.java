package org.example.assistantonsbservlet.api.chemistry.graph;

import org.example.assistantonsbservlet.api.ChemistryGraphResponse;
import org.example.assistantonsbservlet.svc.chemistry.PubChemGraphApiFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompoundGraphController implements CompoundGraphApi {
    private final PubChemGraphApiFacade facade;

    public CompoundGraphController(PubChemGraphApiFacade facade) {
        this.facade = facade;
    }

    @Override
    public ResponseEntity<ChemistryGraphResponse> getCompoundDataByName(int page, int pageSize, String name) {
        final var graphResponse = facade.getCompoundDataByName(page, pageSize, name);
        return ResponseEntity.ok(graphResponse);
    }

    @Override
    public ResponseEntity<ChemistryGraphResponse> getAll(int page, int pageSize) {
        final var graphResponse = facade.getAll(page, pageSize);
        return ResponseEntity.ok(graphResponse);
    }
}
