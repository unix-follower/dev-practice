package org.example.assistantonsbservlet.convert;

import org.example.assistantonsbservlet.api.chemistry.model.FoodAdditiveSubstanceResponseDto;
import org.example.db.pubchem.fda.model.FoodAdditiveSubstance;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class FoodAdditiveSubstanceToDtoListConverter implements
    Converter<FoodAdditiveSubstance, FoodAdditiveSubstanceResponseDto> {
    @Override
    public FoodAdditiveSubstanceResponseDto convert(FoodAdditiveSubstance source) {
        return new FoodAdditiveSubstanceResponseDto(
            source.getCompoundCid(),
            source.getName(),
            source.getSynonyms(),
            source.getMolecularWeight(),
            source.getMolecularFormula(),
            source.getPolarArea(),
            source.getComplexity(),
            source.getXlogp(),
            source.getHeavyAtomCount(),
            source.getHBondDonorCount(),
            source.getHBondAcceptorCount(),
            source.getRotatableBondCount(),
            source.getInchi(),
            source.getSmiles(),
            source.getInchiKey(),
            source.getIupacName(),
            source.getExactMass(),
            source.getMonoisotopicMass(),
            source.getCharge(),
            source.getCovalentUnitCount(),
            source.getIsotopicAtomCount(),
            source.getTotalAtomStereoCount(),
            source.getDefinedAtomStereoCount(),
            source.getUndefinedAtomStereoCount(),
            source.getTotalBondStereoCount(),
            source.getDefinedBondStereoCount(),
            source.getUndefinedBondStereoCount(),
            source.getLinkedPubChemLiteratureCount(),
            source.getLinkedPubChemPatentCount(),
            source.getLinkedPubChemPatentFamilyCount(),
            source.getMeshHeadings(),
            source.getAnnotationContent(),
            source.getAnnotationTypeCount(),
            source.getLinkedBioAssays(),
            source.getCreateDate(),
            source.getDataSource(),
            source.getDataSourceCategory(),
            source.getTaggedByPubChem()
        );
    }
}
