package org.example.assistantonsbservlet.api.chemistry.model;

import org.example.assistantonsbservlet.api.model.Point2dDto;
import org.example.assistantonsbservlet.api.model.Point3dDto;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public record BondDto(
    String id,
    int index,
    Integer fromAtomIndex,
    Integer toAtomIndex,
    int atomCount,
    boolean isAromatic,
    boolean isInRing,
    Integer electronCount,
    String bondOrder,
    String stereo,
    Point2dDto bond2DCenter,
    Point3dDto bond3DCenter,
    boolean[] flags,
    Map<Object, Object> properties
) {
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BondDto bondDto)) {
            return false;
        }

        return index == bondDto.index
            && atomCount == bondDto.atomCount
            && isInRing == bondDto.isInRing
            && isAromatic == bondDto.isAromatic
            && Objects.equals(id, bondDto.id)
            && Objects.equals(stereo, bondDto.stereo)
            && Objects.deepEquals(flags, bondDto.flags)
            && Objects.equals(bondOrder, bondDto.bondOrder)
            && Objects.equals(toAtomIndex, bondDto.toAtomIndex)
            && Objects.equals(fromAtomIndex, bondDto.fromAtomIndex)
            && Objects.equals(electronCount, bondDto.electronCount)
            && Objects.equals(bond2DCenter, bondDto.bond2DCenter)
            && Objects.equals(bond3DCenter, bondDto.bond3DCenter)
            && Objects.equals(properties, bondDto.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            index,
            fromAtomIndex,
            toAtomIndex,
            atomCount,
            isAromatic,
            isInRing,
            electronCount,
            bondOrder,
            stereo,
            bond2DCenter,
            bond3DCenter,
            Arrays.hashCode(flags),
            properties
        );
    }
}
