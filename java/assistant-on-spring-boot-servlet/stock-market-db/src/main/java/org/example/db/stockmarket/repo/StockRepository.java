package org.example.db.stockmarket.repo;

import jakarta.persistence.Tuple;

import java.util.List;

public interface StockRepository {
    List<Tuple> findByTicker(String ticker, int offset, int limit);
}
