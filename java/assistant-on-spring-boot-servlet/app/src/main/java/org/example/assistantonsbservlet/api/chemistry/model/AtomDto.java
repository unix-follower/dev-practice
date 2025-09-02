package org.example.assistantonsbservlet.api.chemistry.model;

import org.example.assistantonsbservlet.api.model.Point2dDto;
import org.example.assistantonsbservlet.api.model.Point3dDto;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public record AtomDto(
    String id,
    int index,
    int bondCount,
    Double charge,
    Integer implicitHydrogenCount,
    Integer totalHydrogenCount,
    int mapIdx,
    Integer atomicNumber,
    String atomTypeName,
    Double bondOrderSum,
    Double covalentRadius,
    Double exactMass,
    Integer formalCharge,
    Integer formalNeighbourCount,
    Integer massNumber,
    Double naturalAbundance,
    String symbol,
    Integer valency,
    String hybridization,
    String maxBondOrder,
    Point2dDto point2d,
    Point3dDto point3d,
    Point3dDto fractionalPoint3d,
    boolean[] flags,
    Map<Object, Object> properties
) {
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AtomDto atomDto)) {
            return false;
        }

        return index == atomDto.index
            && mapIdx == atomDto.mapIdx
            && bondCount == atomDto.bondCount
            && Objects.equals(id, atomDto.id)
            && Objects.equals(charge, atomDto.charge)
            && Objects.equals(symbol, atomDto.symbol)
            && Objects.equals(valency, atomDto.valency)
            && Objects.deepEquals(flags, atomDto.flags)
            && Objects.equals(exactMass, atomDto.exactMass)
            && Objects.equals(massNumber, atomDto.massNumber)
            && Objects.equals(point2d, atomDto.point2d)
            && Objects.equals(point3d, atomDto.point3d)
            && Objects.equals(atomTypeName, atomDto.atomTypeName)
            && Objects.equals(bondOrderSum, atomDto.bondOrderSum)
            && Objects.equals(maxBondOrder, atomDto.maxBondOrder)
            && Objects.equals(atomicNumber, atomDto.atomicNumber)
            && Objects.equals(formalCharge, atomDto.formalCharge)
            && Objects.equals(hybridization, atomDto.hybridization)
            && Objects.equals(covalentRadius, atomDto.covalentRadius)
            && Objects.equals(naturalAbundance, atomDto.naturalAbundance)
            && Objects.equals(totalHydrogenCount, atomDto.totalHydrogenCount)
            && Objects.equals(formalNeighbourCount, atomDto.formalNeighbourCount)
            && Objects.equals(fractionalPoint3d, atomDto.fractionalPoint3d)
            && Objects.equals(implicitHydrogenCount, atomDto.implicitHydrogenCount)
            && Objects.equals(properties, atomDto.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            index,
            bondCount,
            charge,
            implicitHydrogenCount,
            totalHydrogenCount,
            mapIdx,
            atomicNumber,
            atomTypeName,
            bondOrderSum,
            covalentRadius,
            exactMass,
            formalCharge,
            formalNeighbourCount,
            massNumber,
            naturalAbundance,
            symbol,
            valency,
            hybridization,
            maxBondOrder,
            point2d,
            point3d,
            fractionalPoint3d,
            Arrays.hashCode(flags),
            properties
        );
    }
}
