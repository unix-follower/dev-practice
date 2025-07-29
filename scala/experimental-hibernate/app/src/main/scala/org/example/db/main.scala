package org.example.db

import jakarta.persistence.Persistence
import org.example.db.stockmarket.{StockJpaRepository, StockJpaRepositoryImpl}

import java.util
import scala.util.Using

@main
def main(): Unit =
    val dbUser = System.getenv("STOCK_MARKET_DB_USERNAME")
    val dbPassword = System.getenv("STOCK_MARKET_DB_PASSWORD")

    val properties = new util.HashMap[String, String]()
    properties.put("javax.persistence.jdbc.user", dbUser)
    properties.put("javax.persistence.jdbc.password", dbPassword)

    val emFactory = Persistence.createEntityManagerFactory("stock-market", properties)
    Using(emFactory) { emf =>
        Using(emf.createEntityManager()) { em =>
            val tx = em.getTransaction
            try {
                tx.begin()
                val stockRepository: StockJpaRepository = StockJpaRepositoryImpl(em)
                val resultList = stockRepository.findByTicker("KO", 10, 0)
                println(resultList.size())
                tx.commit()
            } catch {
                case e: Throwable =>
                    tx.rollback()
                    println(e)
                    e.printStackTrace()
            }
        }
    }
