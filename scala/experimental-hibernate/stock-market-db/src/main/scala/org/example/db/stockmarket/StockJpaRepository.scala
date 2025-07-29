package org.example.db.stockmarket

import jakarta.persistence.EntityManager

import java.util

trait StockJpaRepository {
    def findByTicker(ticker: String, limit: Int, offset: Int): util.List[Stock]
}

class StockJpaRepositoryImpl(em: EntityManager) extends StockJpaRepository {
    def findByTicker(ticker: String, limit: Int, offset: Int): util.List[Stock] = {
        val query = em.createQuery("SELECT s FROM stock s WHERE s.id.ticker = :ticker ORDER BY s.id.dateAt DESC", classOf[Stock])
          .setParameter("ticker", ticker)
          .setFirstResult(offset)
          .setMaxResults(limit)
        query.getResultList
    }
}
