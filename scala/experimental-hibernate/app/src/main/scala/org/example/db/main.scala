package org.example.db

import jakarta.persistence.Persistence
import org.example.db.stockmarket.{Stock, StockJpaRepository, StockJpaRepositoryImpl, StockSearchParams}

import java.util
import scala.util.Using

@main
def main(methodToExec: String, execStrategy: String, other: String*): Unit =
    val dbUser = System.getenv("STOCK_MARKET_DB_USERNAME")
    val dbPassword = System.getenv("STOCK_MARKET_DB_PASSWORD")

    val properties = new util.HashMap[String, String]()
    properties.put("javax.persistence.jdbc.user", dbUser)
    properties.put("javax.persistence.jdbc.password", dbPassword)

    val methodToExecMap = Map[String, StockJpaRepository => Any](
        "findByTicker" -> (repo => {
            // CLI args example:
            // findByTicker _ KO 10 0
            val stockTuples = repo.findByTicker(
                ticker = other.apply(0),
                limit = other.apply(1).toInt,
                offset = other.apply(2).toInt,
            )
            println(s"KO stocks: ${stockTuples.head.get("totalCount")}")
            stockTuples.foreach(stockTuple => {
                val stock = stockTuple.get(1).asInstanceOf[Stock]
                println(s"KO stock on: ${stock.getId.getDateAt}, High: ${stock.getHigh}")
            })
        }),
        "cumulativeVolume" -> (repo => {
            // CLI args examples:
            // cumulativeVolume _ KO 10 0 dateAt false
            // cumulativeVolume nativeQuery KO 10 0 dateAt false
            // cumulativeVolume criteriaQuery KO 10 0 cumulativeVolume false
            val stocks = repo.cumulativeVolume(StockSearchParams(
                execStrategy = execStrategy,
                ticker = other.apply(0),
                limit = other.apply(1).toInt,
                offset = other.apply(2).toInt,
                sortColumn = other.apply(3),
                isSortAsc = other.apply(4).toBoolean,
            ))
            println(s"KO stocks: ${stocks.head.totalCount}")
            stocks.foreach(stock => println(s"KO stock on: ${stock.dateAt}, volume: ${stock.aggregate}, cumulativeVolume: ${stock.aggregated}"))
        }),
    )

    val emFactory = Persistence.createEntityManagerFactory("stock-market", properties)
    Using(emFactory) { emf =>
        Using(emf.createEntityManager()) { em =>
            val tx = em.getTransaction
            try {
                tx.begin()
                val stockRepository: StockJpaRepository = StockJpaRepositoryImpl(em)
                
                methodToExecMap.get(methodToExec) match {
                    case Some(method) => method(stockRepository)
                    case None => throw IllegalArgumentException(s"Method '$methodToExec' is not found.")
                }

                tx.commit()
            } catch {
                case e: Throwable =>
                    tx.rollback()
                    e.printStackTrace()
            }
        }
    }
