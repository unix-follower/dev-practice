package com.example.assistantonsbservlet.svc;

import com.example.assistantonsbservlet.api.pubchem.model.FoodAdditiveSubstanceResponseDto;
import com.example.assistantonsbservlet.db.pubchem.repo.FoodAdditiveSubstanceRepository;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class FoodAdditiveSubstanceFacade implements FoodAdditiveSubstanceApiFacade {
    private final FoodAdditiveSubstanceRepository repository;
    private final ConversionService conversionService;

    public FoodAdditiveSubstanceFacade(
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
