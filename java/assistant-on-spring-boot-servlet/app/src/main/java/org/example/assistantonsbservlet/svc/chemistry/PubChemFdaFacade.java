package org.example.assistantonsbservlet.svc.chemistry;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.example.assistantonsbservlet.api.ErrorCode;
import org.example.assistantonsbservlet.api.chemistry.organic.dbs.pubchem.fda.model.FoodAdditiveSubstanceResponseDto;
import org.example.assistantonsbservlet.exception.AppException;
import org.example.db.pubchem.fda.repo.FoodAdditiveSubstanceRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PubChemFdaFacade implements PubChemFdaApiFacade {
    private final ApplicationContext appContext;
    private final ConversionService appConversionService;
    private EntityManagerFactory emf;

    public PubChemFdaFacade(ApplicationContext appContext) {
        this.appContext = appContext;
        this.appConversionService = appContext.getBean("appConversionService", ConversionService.class);
    }

    @PersistenceUnit(unitName = "pubchem-unit")
    public void setEmf(@Qualifier("pubChemEntityManagerFactory") EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public List<FoodAdditiveSubstanceResponseDto> getAll(final int page, final int pageSize) {
        final var em = emf.createEntityManager();
        final var tx = em.getTransaction();
        try (em) {
            tx.begin();
            final var repository = appContext.getBean(FoodAdditiveSubstanceRepository.class, em);
            final int offset = (page - 1) * pageSize;
            final var responseDtoList = repository.findAll(offset, pageSize).stream()
                .map(foodAdditiveSubstance ->
                    appConversionService.convert(foodAdditiveSubstance, FoodAdditiveSubstanceResponseDto.class)
                )
                .toList();
            tx.commit();
            return responseDtoList;
            // CHECKSTYLE:OFF: IllegalCatch
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new AppException(e, ErrorCode.UNKNOWN);
        }
        // CHECKSTYLE:ON: IllegalCatch
    }
}
