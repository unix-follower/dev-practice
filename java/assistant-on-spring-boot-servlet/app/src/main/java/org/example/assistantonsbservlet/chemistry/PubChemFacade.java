package org.example.assistantonsbservlet.chemistry;

import org.example.assistantonsbservlet.api.ChemistryGraphResponse;
import org.example.assistantonsbservlet.api.chemistry.model.CompoundSDFDataResponse;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.io.iterator.IteratingSDFReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.io.StringReader;

@Component
public final class PubChemFacade implements PubChemApiFacade {
    private static final Logger LOGGER = LoggerFactory.getLogger(PubChemFacade.class);

    private final ConversionService appConversionService;
    private final PubChemGraphService pubChemGraphService;

    public PubChemFacade(ApplicationContext appContext) {
        this.appConversionService = appContext.getBean("appConversionService", ConversionService.class);
        this.pubChemGraphService = appContext.getBean(PubChemGraphService.class);
    }

    @Override
    public ChemistryGraphResponse getAllGraphs(int page, int pageSize) {
        return pubChemGraphService.getAllGraphs(page, pageSize);
    }

    @Override
    public ChemistryGraphResponse getCompoundDataByName(String compoundName, int page, int pageSize) {
        return pubChemGraphService.getCompoundDataByName(compoundName, page, pageSize);
    }

    @Override
    public CompoundSDFDataResponse getCompoundSDFDataByCid(long cid) {
        final var iterator = new IteratingSDFReader(
            new StringReader(getData()),
            DefaultChemObjectBuilder.getInstance()
        );
        CompoundSDFDataResponse compoundDto = null;
        if (iterator.hasNext()) {
            final var mol = iterator.next();
            LOGGER.debug("{}", mol);
            compoundDto = appConversionService.convert(mol, CompoundSDFDataResponse.class);

        }
        return compoundDto;
    }

    private String getData() {
        return """
            4
              -OEChem-07252511403D
            
             14 13  0     1  0  0  0  0  0999 V2000
                1.5903   -0.8258    0.0378 O   0  0  0  0  0  0  0  0  0  0  0  0
               -1.9942    0.1028   -0.1015 N   0  0  0  0  0  0  0  0  0  0  0  0
                0.4693   -0.0273   -0.3404 C   0  0  1  0  0  0  0  0  0  0  0  0
               -0.7967   -0.6430    0.2606 C   0  0  0  0  0  0  0  0  0  0  0  0
                0.7313    1.3933    0.1435 C   0  0  0  0  0  0  0  0  0  0  0  0
                0.4149   -0.0317   -1.4353 H   0  0  0  0  0  0  0  0  0  0  0  0
               -0.7090   -0.6871    1.3526 H   0  0  0  0  0  0  0  0  0  0  0  0
               -0.8967   -1.6812   -0.0775 H   0  0  0  0  0  0  0  0  0  0  0  0
                1.6841    1.7603   -0.2541 H   0  0  0  0  0  0  0  0  0  0  0  0
                0.8156    1.4252    1.2355 H   0  0  0  0  0  0  0  0  0  0  0  0
               -0.0587    2.0827   -0.1681 H   0  0  0  0  0  0  0  0  0  0  0  0
               -2.8156   -0.3770    0.2637 H   0  0  0  0  0  0  0  0  0  0  0  0
               -2.0968    0.1155   -1.1153 H   0  0  0  0  0  0  0  0  0  0  0  0
                1.4396   -1.7233   -0.3047 H   0  0  0  0  0  0  0  0  0  0  0  0
              1  3  1  0  0  0  0
              1 14  1  0  0  0  0
              2  4  1  0  0  0  0
              2 12  1  0  0  0  0
              2 13  1  0  0  0  0
              3  4  1  0  0  0  0
              3  5  1  0  0  0  0
              3  6  1  0  0  0  0
              4  7  1  0  0  0  0
              4  8  1  0  0  0  0
              5  9  1  0  0  0  0
              5 10  1  0  0  0  0
              5 11  1  0  0  0  0
            M  END
            > <PUBCHEM_COMPOUND_CID>
            4
            
            > <PUBCHEM_CONFORMER_RMSD>
            0.4
            
            > <PUBCHEM_CONFORMER_DIVERSEORDER>
            1
            5
            6
            4
            3
            2
            
            > <PUBCHEM_MMFF94_PARTIAL_CHARGES>
            7
            1 -0.68
            12 0.36
            13 0.36
            14 0.4
            2 -0.99
            3 0.28
            4 0.27
            
            > <PUBCHEM_EFFECTIVE_ROTOR_COUNT>
            1
            
            > <PUBCHEM_PHARMACOPHORE_FEATURES>
            4
            1 1 acceptor
            1 1 donor
            1 2 cation
            1 2 donor
            
            > <PUBCHEM_HEAVY_ATOM_COUNT>
            5
            
            > <PUBCHEM_ATOM_DEF_STEREO_COUNT>
            0
            
            > <PUBCHEM_ATOM_UDEF_STEREO_COUNT>
            1
            
            > <PUBCHEM_BOND_DEF_STEREO_COUNT>
            0
            
            > <PUBCHEM_BOND_UDEF_STEREO_COUNT>
            0
            
            > <PUBCHEM_ISOTOPIC_ATOM_COUNT>
            0
            
            > <PUBCHEM_COMPONENT_COUNT>
            1
            
            > <PUBCHEM_CACTVS_TAUTO_COUNT>
            1
            
            > <PUBCHEM_CONFORMER_ID>
            0000000400000001
            
            > <PUBCHEM_MMFF94_ENERGY>
            -0.1086
            
            > <PUBCHEM_FEATURE_SELFOVERLAP>
            20.297
            
            > <PUBCHEM_SHAPE_FINGERPRINT>
            139733 1 9294993661768824926
            20096714 4 18197503037103913545
            21015797 1 8716345989422343382
            24536 1 17242171464452986119
            29004967 10 18410296882578243426
            5943 1 11085239728363356890
            
            > <PUBCHEM_SHAPE_MULTIPOLES>
            92.05
            1.98
            1.2
            0.63
            0.77
            0.25
            0
            -0.04
            0.01
            -0.48
            -0.08
            0
            0.01
            0.07
            
            > <PUBCHEM_SHAPE_SELFOVERLAP>
            155.457
            
            > <PUBCHEM_SHAPE_VOLUME>
            63.3
            
            > <PUBCHEM_COORDINATE_TYPE>
            2
            5
            10
            
            $$$$
            """;
    }
}
