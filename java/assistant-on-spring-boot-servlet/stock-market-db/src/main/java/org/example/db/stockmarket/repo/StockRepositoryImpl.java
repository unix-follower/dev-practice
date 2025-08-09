package org.example.db.stockmarket.repo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import org.example.db.stockmarket.model.Stock;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Lazy
public class StockRepositoryImpl implements StockRepository {
    private final EntityManager em;

    public StockRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Tuple> findByTicker(String ticker, int offset, int limit) {
        return searchForTickerWithTotalCount(Stock.FIND_BY_TICKER_NQ, ticker, offset, limit);
    }

    private List<Tuple> searchForTickerWithTotalCount(String namedQuery, String ticker, int offset, int limit) {
        final var query = em.createNamedQuery(namedQuery, Tuple.class)
            .setParameter("ticker", ticker)
            .setFirstResult(offset)
            .setMaxResults(limit);
        return query.getResultList();
    }
}
