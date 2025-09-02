package org.example.assistantonsbservlet.svc.chemistry;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.example.assistantonsbservlet.api.ChemistryGraphResponse;
import org.example.assistantonsbservlet.api.ErrorCode;
import org.example.assistantonsbservlet.exception.AppException;
import org.example.db.pubchem.graph.CompoundRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
public class PubChemGraphServiceImpl implements PubChemGraphService {
    private final ApplicationContext appContext;
    private final ConversionService appConversionService;
    private EntityManagerFactory emf;

    public PubChemGraphServiceImpl(ApplicationContext appContext) {
        this.appContext = appContext;
        this.appConversionService = appContext.getBean("appConversionService", ConversionService.class);
    }

    @PersistenceUnit(unitName = "pubchem-graph-unit")
    public void setEmf(@Qualifier("pubChemGraphEntityManagerFactory") EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    @WithSpan(value = "PubChemGraphServiceImpl.getAllGraphs")
    public ChemistryGraphResponse getAllGraphs(int page, int pageSize) {
        final int offset = (page - 1) * pageSize;

        final var em = emf.createEntityManager();
        final var tx = em.getTransaction();
        try (em) {
            tx.begin();
            final var repository = appContext.getBean(CompoundRepository.class, em);
            final var compoundList = repository.findAll(offset, pageSize);
            final var response = appConversionService.convert(compoundList, ChemistryGraphResponse.class);
            tx.commit();
            return response;
            // CHECKSTYLE:OFF: IllegalCatch
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new AppException(e, ErrorCode.UNKNOWN);
        }
        // CHECKSTYLE:ON: IllegalCatch
    }

    @Override
    public ChemistryGraphResponse getCompoundDataByName(String compoundName, int page, int pageSize) {
        final var em = emf.createEntityManager();
        final var tx = em.getTransaction();
        try (em) {
            tx.begin();
            final var repository = appContext.getBean(CompoundRepository.class, em);
            final var compound = repository.findCompoundDataByName(page, pageSize, compoundName);
            final var response = appConversionService.convert(compound, ChemistryGraphResponse.class);
            tx.commit();
            return response;
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
