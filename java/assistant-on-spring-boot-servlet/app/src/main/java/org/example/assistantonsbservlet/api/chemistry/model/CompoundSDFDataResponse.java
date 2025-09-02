package org.example.assistantonsbservlet.api.chemistry.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record CompoundSDFDataResponse(
    String id,
    String title,
    List<AtomDto> atoms,
    List<BondDto> bonds,
    List<ElectronDto> singleElectrons,
    List<ElectronDto> electrons,
    boolean[] flags,
    Map<Object, Object> properties
) {
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CompoundSDFDataResponse that)) {
            return false;
        }

        return Objects.equals(id, that.id)
            && Objects.equals(title, that.title)
            && Objects.deepEquals(flags, that.flags)
            && Objects.equals(atoms, that.atoms)
            && Objects.equals(bonds, that.bonds)
            && Objects.equals(electrons, that.electrons)
            && Objects.equals(properties, that.properties)
            && Objects.equals(singleElectrons, that.singleElectrons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            title,
            atoms,
            bonds,
            singleElectrons,
            electrons,
            Arrays.hashCode(flags),
            properties
        );
    }
}
