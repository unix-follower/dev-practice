package org.example.assistantonsbservlet.convert;

import org.example.assistantonsbservlet.api.chemistry.model.AtomDto;
import org.example.assistantonsbservlet.api.chemistry.model.BondDto;
import org.example.assistantonsbservlet.api.chemistry.model.CompoundSDFDataResponse;
import org.example.assistantonsbservlet.api.chemistry.model.ElectronDto;
import org.example.assistantonsbservlet.api.model.Point2dDto;
import org.example.assistantonsbservlet.api.model.Point3dDto;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IElectronContainer;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import javax.vecmath.Point3d;
import java.util.ArrayList;
import java.util.List;

@Component
@WritingConverter
public class CompoundSDFDataToDtoConverter implements Converter<IAtomContainer, CompoundSDFDataResponse> {
    @Override
    public CompoundSDFDataResponse convert(IAtomContainer source) {
        final var atomDtoList = mapAtoms(source);
        final var bondDtoList = mapBonds(source);

        final var singleElectronDtoList = mapElectrons(source.singleElectrons());
        final var electronDtoList = mapElectrons(source.electronContainers());

        return new CompoundSDFDataResponse(
            source.getID(),
            source.getTitle(),
            atomDtoList,
            bondDtoList,
            singleElectronDtoList,
            electronDtoList,
            source.getFlags(),
            source.getProperties()
        );
    }

    private static List<AtomDto> mapAtoms(IAtomContainer source) {
        final var atomDtoList = new ArrayList<AtomDto>();
        for (final var atom : source.atoms()) {
            final var point2dDto = mapPoint2dDto(atom);
            final var point3dDto = mapPoint3dDto(atom.getPoint3d());
            final var fractionalPoint3dDto = mapPoint3dDto(atom.getFractionalPoint3d());

            final String hybridization = mapHybridization(atom);
            final String maxBondOrder = mapMaxBondOrder(atom);

            final var atomDto = new AtomDto(
                atom.getID(),
                atom.getIndex(),
                atom.getBondCount(),
                atom.getCharge(),
                atom.getImplicitHydrogenCount(),
                atom.getTotalHydrogenCount(),
                atom.getMapIdx(),
                atom.getAtomicNumber(),
                atom.getAtomTypeName(),
                atom.getBondOrderSum(),
                atom.getCovalentRadius(),
                atom.getExactMass(),
                atom.getFormalCharge(),
                atom.getFormalNeighbourCount(),
                atom.getMassNumber(),
                atom.getNaturalAbundance(),
                atom.getSymbol(),
                atom.getValency(),
                hybridization,
                maxBondOrder,
                point2dDto,
                point3dDto,
                fractionalPoint3dDto,
                atom.getFlags(),
                atom.getProperties()
            );

            atomDtoList.add(atomDto);
        }
        return atomDtoList;
    }

    private static Point2dDto mapPoint2dDto(IAtom atom) {
        final var point2d = atom.getPoint2d();
        Point2dDto point2dDto = null;
        if (point2d != null) {
            double x = point2d.getX();
            double y = point2d.getY();
            point2dDto = new Point2dDto(x, y);
        }
        return point2dDto;
    }

    private static Point3dDto mapPoint3dDto(Point3d fractionalPoint3d) {
        Point3dDto fractionalPoint3dDto = null;
        if (fractionalPoint3d != null) {
            double x = fractionalPoint3d.getX();
            double y = fractionalPoint3d.getY();
            double z = fractionalPoint3d.getZ();
            fractionalPoint3dDto = new Point3dDto(x, y, z);
        }
        return fractionalPoint3dDto;
    }

    private static String mapHybridization(IAtom atom) {
        String hybridization = null;
        final var atomHybridization = atom.getHybridization();
        if (atomHybridization != null) {
            hybridization = atomHybridization.name();
        }
        return hybridization;
    }

    private static String mapMaxBondOrder(IAtom atom) {
        String maxBondOrder = null;
        final var maxBondOrderEnum = atom.getMaxBondOrder();
        if (maxBondOrderEnum != null) {
            maxBondOrder = maxBondOrderEnum.name();
        }
        return maxBondOrder;
    }

    private static List<BondDto> mapBonds(IAtomContainer source) {
        final var bondDtoList = new ArrayList<BondDto>();
        for (final var bond : source.bonds()) {
            final var fromAtom = bond.getBegin();
            final var fromIndex = (fromAtom != null) ? fromAtom.getIndex() : null;

            final var toAtom   = bond.getEnd();
            final var toIndex   = (toAtom != null) ? toAtom.getIndex() : null;

            final var bond2DCenterDto = mapPoint2dDto(bond);
            final var bond3DCenterDto = mapPoint3dDto(bond);
            final String bondOrder = mapBondOrder(bond);
            final String stereo = mapStereo(bond);

            final var bondDto = new BondDto(
                bond.getID(),
                bond.getIndex(),
                fromIndex,
                toIndex,
                bond.getAtomCount(),
                bond.isAromatic(),
                bond.isInRing(),
                bond.getElectronCount(),
                bondOrder,
                stereo,
                bond2DCenterDto,
                bond3DCenterDto,
                bond.getFlags(),
                bond.getProperties()
            );
            bondDtoList.add(bondDto);
        }
        return bondDtoList;
    }

    private static Point2dDto mapPoint2dDto(IBond bond) {
        Point2dDto bond2DCenterDto = null;
        try {
            final var bond2DCenter = bond.get2DCenter();
            bond2DCenterDto = new Point2dDto(bond2DCenter.getX(), bond2DCenter.getY());
        } catch (NullPointerException _) {
            // bond.get2DCenter() will throw NPE if the 2d point is not set
        }
        return bond2DCenterDto;
    }

    private static Point3dDto mapPoint3dDto(IBond bond) {
        Point3dDto bond3DCenterDto = null;
        try {
            final var bond3DCenter = bond.get3DCenter();
            bond3DCenterDto = new Point3dDto(bond3DCenter.getX(), bond3DCenter.getY(), bond3DCenter.getZ());
        } catch (NullPointerException _) {
            // bond.get3DCenter() will throw NPE if the 3d point is not set
        }
        return bond3DCenterDto;
    }

    private static String mapBondOrder(IBond bond) {
        final var bondOrderEnum = bond.getOrder();
        String bondOrder = null;
        if (bondOrderEnum != null) {
            bondOrder = bondOrderEnum.name();
        }
        return bondOrder;
    }

    private static String mapStereo(IBond bond) {
        final var stereoEnum = bond.getStereo();
        String stereo = null;
        if (stereoEnum != null) {
            stereo = stereoEnum.name();
        }
        return stereo;
    }

    private static List<ElectronDto> mapElectrons(Iterable<? extends IElectronContainer> source) {
        final var electronDtoList = new ArrayList<ElectronDto>();
        for (final var electron : source) {
            final var electronDto = new ElectronDto(
                electron.getID(),
                electron.getElectronCount(),
                electron.getFlags(),
                electron.getProperties()
            );
            electronDtoList.add(electronDto);
        }
        return electronDtoList;
    }
}
