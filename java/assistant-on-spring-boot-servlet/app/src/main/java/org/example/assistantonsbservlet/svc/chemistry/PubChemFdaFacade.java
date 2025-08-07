package org.example.assistantonsbservlet.svc.chemistry;

import org.example.assistantonsbservlet.api.chemistry.organic.dbs.pubchem.fda.model.FoodAdditiveSubstanceResponseDto;
import org.example.db.pubchem.fda.repo.FoodAdditiveSubstanceRepository;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class PubChemFdaFacade implements PubChemFdaApiFacade {
    private final FoodAdditiveSubstanceRepository repository;
    private final ConversionService conversionService;

    public PubChemFdaFacade(
        final FoodAdditiveSubstanceRepository repository,
        final ConversionService conversionService
    ) {
        this.repository = repository;
        this.conversionService = conversionService;
    }

    @Override
    public List<FoodAdditiveSubstanceResponseDto> getAll(final int page, final int size) {
        return repository.findAll(page, size).stream()
            .map(foodAdditiveSubstance ->
                conversionService.convert(foodAdditiveSubstance, FoodAdditiveSubstanceResponseDto.class)
            )
            .toList();
    }
}
