package org.example.assistantonsbservlet.finance;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.example.assistantonsbservlet.api.ErrorCode;
import org.example.assistantonsbservlet.api.finance.stockmarket.model.StocksResponseDto;
import org.example.assistantonsbservlet.exception.AppException;
import org.example.db.stockmarket.repo.StockRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnBooleanProperty("app.stock-market.ds.enabled")
public final class StockMarketFacade implements StockMarketApiFacade {
    private final ApplicationContext appContext;
    private final ConversionService appConversionService;
    private EntityManagerFactory emf;

    public StockMarketFacade(ApplicationContext appContext) {
        this.appContext = appContext;
        this.appConversionService = appContext.getBean("appConversionService", ConversionService.class);
    }

    @PersistenceUnit(unitName = "stock-market-unit")
    public void setEmf(@Qualifier("stockMarketEntityManagerFactory") EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public StocksResponseDto findByTicker(String ticker, int page, int pageSize) {
        final var em = emf.createEntityManager();
        final var tx = em.getTransaction();
        try (em) {
            tx.begin();
            final var repository = appContext.getBean(StockRepository.class, em);
            final int offset = (page - 1) * pageSize;
            final var tuples = repository.findByTicker(ticker, offset, pageSize);
            final var stocks = appConversionService.convert(tuples, StocksResponseDto.class);
            tx.commit();
            return stocks;
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

@Component
@ConditionalOnBooleanProperty(value = "app.stock-market.ds.enabled", havingValue = false)
final class StockMarketNoopFacade implements StockMarketApiFacade {
    @Override
    public StocksResponseDto findByTicker(String ticker, int page, int pageSize) {
        throw new UnsupportedOperationException();
    }
}
