package org.example.assistantonsbservlet.api.chemistry.model;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public record ElectronDto(
    String id,
    Integer electronCount,
    boolean[] flags,
    Map<Object, Object> properties
) {
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ElectronDto that)) {
            return false;
        }

        return Objects.equals(id, that.id)
            && Objects.deepEquals(flags, that.flags)
            && Objects.equals(electronCount, that.electronCount)
            && Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, electronCount, Arrays.hashCode(flags), properties);
    }
}
