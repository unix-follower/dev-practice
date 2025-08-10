package org.example.db.stockmarket

import jakarta.persistence
import jakarta.persistence.EntityManager
import org.hibernate.Session
import org.hibernate.metamodel.model.domain.SingularPersistentAttribute
import org.hibernate.query.TupleTransformer
import org.hibernate.query.criteria.{HibernateCriteriaBuilder, JpaExpression}

import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util
import scala.jdk.CollectionConverters.*


trait StockRepository {
    def findByTicker(ticker: String, limit: Int, offset: Int): List[persistence.Tuple]

    /**
     * @return sum the volume column over time to see how much stock has been traded cumulatively.
     */
    def cumulativeVolume(params: StockSearchParams): List[StockAggregateProjection]

    /**
     * @return sum the dividends column over time to calculate the total dividends paid.
     */
    def cumulativeDividends(ticker: String, limit: Int, offset: Int): List[StockCumulativeDividendsProjection]

    /**
     * @return sum the capital_gains column to track total gains over time.
     */
    def cumulativeCapitalGains(ticker: String, limit: Int, offset: Int): List[StockCumulativeCapitalGainsProjection]

    /**
     * @return calculate a moving average for the close column over a window of days (e.g., 5-day, 10-day, 30-day).
     */
    def closingPriceMovingAverage(ticker: String, limit: Int, offset: Int, days: Int): List[StockAggregateProjection]

    /**
     * @return calculate a moving average for the volume column to identify trading patterns.
     */
    def volumeMovingAverage(ticker: String, limit: Int, offset: Int, days: Int): List[StockAggregateProjection]

    /**
     * @return calculate the percentage change in close from the previous day to measure daily price fluctuations.
     */
    def closingPricePercentageChange(ticker: String, limit: Int, offset: Int): List[StockAggregateProjection]

    /**
     * @return calculate the percentage change in volume to measure trading activity fluctuations.
     */
    def volumePercentageChange(ticker: String, limit: Int, offset: Int): List[StockAggregateProjection]

    /**
     * @return rank the stocks for a given day based on their close price.
     */
    def rankByClosingPrice(dateAt: OffsetDateTime, limit: Int, offset: Int): List[StockAggregateProjection]

    /**
     * @return calculate the percentile of volume to understand how a stock's trading activity compares to others.
     */
    def volumePercentile(dateAt: OffsetDateTime, limit: Int, offset: Int): List[StockAggregateProjection]

    /**
     * @return calculate the standard deviation of close prices to measure volatility.
     */
    def stdClosingPrice(ticker: String, limit: Int, offset: Int): List[StockAggregateProjection]

    /**
     * @return divide the standard deviation of close by the mean to normalize volatility.
     */
    def coefficientVariation(ticker: String, limit: Int, offset: Int): List[StockAggregateProjection]

    /**
     * @return determine whether higher trading activity (volume) correlates with changes in the close price.
     */
    def correlationBetweenVolumeAndClosingPrice(ticker: String, limit: Int, offset: Int): List[StockAggregateProjection]

    /**
     * @return Analyze whether dividend payouts influence stock close prices.
     */
    def correlationBetweenDividendsAndClosingPrice(ticker: String, limit: Int, offset: Int): List[StockAggregateProjection]

    /**
     * @return calculate the average close price for each month.
     */
    def avgMonthlyClosingPrice(ticker: String, limit: Int, offset: Int): List[StockAggregateProjection]

    /**
     * @return sum the dividends column grouped by year.
     */
    def totalDividendsPaidPerYear(ticker: String, limit: Int, offset: Int): List[StockAggregateProjection]

    /**
     * @return calculate the difference between high and low prices for each day.
     */
    def dailyHighLowSpread(ticker: String, limit: Int, offset: Int): List[StockAggregateProjection]

    /**
     * @return calculate the dividend yield as dividends / close for each day.
     */
    def dividendYield(ticker: String, limit: Int, offset: Int): List[StockDividendYieldProjection]

    /**
     * @return identify days when stock_splits is greater than 1 and analyze price changes before and after the split.
     */
    def trackStockSplits(stockSplits: Int, limit: Int, offset: Int): List[Stock]

    /**
     * @return sum the capital_gains column over time to calculate total gains.
     */
    def totalCapitalGains(ticker: String, limit: Int, offset: Int): List[StockCapitalGainsAggregateProjection]

    /**
     * @return calculate the ratio of capital_gains to close to measure the impact of gains on stock price.
     */
    def relativeContributionOfCapitalGains(ticker: String, limit: Int, offset: Int): List[StockCapitalGainsAggregateProjection]

    /**
     * @return calculate the average volume for each month to identify seasonal trading patterns.
     */
    def avgMonthlyVolume(ticker: String, limit: Int, offset: Int): List[StockAggregateProjection]

    /**
     * @return analyze close prices grouped by month to detect seasonal trends.
     */
    def seasonalPriceTrends(ticker: String, limit: Int, offset: Int): List[StockAggregateProjection]

    /**
     * @return identify rows where close is significantly higher or lower than the average.
     */
    def findOutliersInClosingPrice(ticker: String, limit: Int, offset: Int): List[StockAggregateProjection]

    /**
     * @return identify days with unusually high or low trading activity.
     */
    def findOutliersInVolume(ticker: String, limit: Int, offset: Int): List[StockAggregateProjection]
}

private class CumulativeVolumeRow(
                                   var totalCount: java.lang.Long = null,
                                   var ticker: String = null,
                                   var dateAt: OffsetDateTime = null,
                                   var volume: java.lang.Long = null,
                                   var cumulativeVolume: java.lang.Long = null
                                 ) extends Serializable {
    def this() = {
        this(null, null, null, null)
    }

    def setTicker(ticker: String): Unit = this.ticker = ticker

    def setDateAt(dateAt: OffsetDateTime): Unit = this.dateAt = dateAt

    def setVolume(volume: Long): Unit = this.volume = volume

    def setCumulativeVolume(cumulativeVolume: Long): Unit = this.volume = volume
}

class StockRepositoryImpl(em: EntityManager) extends StockRepository {
    def findByTicker(ticker: String, limit: Int, offset: Int): List[persistence.Tuple] = {
        val sql =
            """
              |SELECT count(s.id) OVER () AS totalCount, s
              |FROM stock s
              |WHERE s.id.ticker = :ticker
              |ORDER BY s.id.dateAt DESC
              |""".stripMargin
        searchForTickerWithTotalCount(sql = sql, ticker = ticker, limit = limit, offset = offset)
    }

    private def searchForTickerWithTotalCount[T](sql: String, ticker: String, limit: Int, offset: Int): List[persistence.Tuple] = {
        val query = em.createQuery(sql, classOf[persistence.Tuple])
          .setParameter("ticker", ticker)
          .setFirstResult(offset)
          .setMaxResults(limit)
        query.getResultList.asScala.toList
    }

    private def searchForTicker[T](sql: String, resultClass: Class[T], ticker: String, limit: Int, offset: Int): List[T] = {
        val query = em.createQuery(sql, resultClass)
          .setParameter("ticker", ticker)
          .setFirstResult(offset)
          .setMaxResults(limit)
        query.getResultList.asScala.toList
    }

    override def cumulativeVolume(params: StockSearchParams): List[StockAggregateProjection] = {
        val defaultExecStrategy: () => List[StockAggregateProjection] = () => {
            val session = em.unwrap(classOf[Session])

            val rowTransformer = new TupleTransformer[StockAggregateProjection] {
                override def transformTuple(tuple: Array[AnyRef], aliases: Array[String]): StockAggregateProjection = {
                    StockAggregateProjection(
                        tuple(0).asInstanceOf[Long],
                        tuple(1).asInstanceOf[String],
                        tuple(2).asInstanceOf[OffsetDateTime],
                        BigDecimal.valueOf(tuple(3).asInstanceOf[Long]),
                        BigDecimal.valueOf(tuple(4).asInstanceOf[Long]),
                    )
                }
            }

            val orderBy = if (params.isSortAsc) s"${params.sortColumn} ASC" else s"${params.sortColumn} DESC"

            val query = session.createNamedQuery(Stock.CUMULATIVE_VOLUME_NQ, classOf[StockAggregateProjection])
              .setParameter("ticker", params.ticker)
              .setParameter("orderBy", orderBy)
              .setFirstResult(params.offset)
              .setMaxResults(params.limit)
              .setTupleTransformer(rowTransformer)

            query.getResultList.asScala.toList
        }

        val sortColumn = if (params.sortColumn.equals("dateAt")) "date_at" else params.sortColumn
        val orderBy = if (params.isSortAsc) s"$sortColumn ASC" else s"$sortColumn DESC"

        val nativeQueryExecStrategy: () => List[StockAggregateProjection] = () => {
            val query = em.createNamedQuery(Stock.CUMULATIVE_VOLUME_NNQ)
              .setParameter("ticker", params.ticker)
              .setParameter("orderBy", orderBy)
              .setFirstResult(params.offset)
              .setMaxResults(params.limit)

            val list = query.getResultList
            list.asScala.toList.map(_.asInstanceOf[StockAggregateProjection])
        }

        val cbExecStrategy: () => List[StockAggregateProjection] = () => {
            val cb = em.getCriteriaBuilder.asInstanceOf[HibernateCriteriaBuilder]
            val criteriaQuery = cb.createQuery(classOf[CumulativeVolumeRow])
            val stockRoot = criteriaQuery.from(classOf[Stock])

            val stockIdPath = stockRoot.get("id")
            val tickerPath = stockIdPath.get("ticker").as(classOf[String])
            val dateAtPath = stockIdPath.get("dateAt").as(classOf[OffsetDateTime])
            val volumeAttr = stockRoot.getModel.findSingularAttribute("volume")
            val volumePath = stockRoot.get(volumeAttr).as(classOf[java.lang.Long]).asInstanceOf[JpaExpression[Number]]

            val window = cb.createWindow()
              .partitionBy(tickerPath)
              .orderBy(cb.asc(dateAtPath))

            val volumeSum = cb.sum(volumePath, window).as(classOf[java.lang.Long])

            val selection = cb.construct(
                classOf[CumulativeVolumeRow],
                util.List.of(
                    cb.count(stockIdPath, cb.createWindow()),
                    tickerPath,
                    dateAtPath,
                    volumePath,
                    volumeSum
                )
            )
            criteriaQuery.select(selection)
            criteriaQuery.where(
                util.List.of(cb.equal(tickerPath, params.ticker))
            )

            val sortColumn = () => {
                params.sortColumn match {
                    case "dateAt" => dateAtPath
                    case "volume" => volumePath
                    case "cumulativeVolume" => volumeSum
                }
            }

            if (params.isSortAsc) {
                criteriaQuery.orderBy(cb.asc(sortColumn()))
            } else {
                criteriaQuery.orderBy(cb.desc(sortColumn()))
            }

            val session = em.unwrap(classOf[Session])

            val rowTransformer = new TupleTransformer[StockAggregateProjection] {
                override def transformTuple(tuple: Array[AnyRef], aliases: Array[String]): StockAggregateProjection = {
                    val row = tuple(0).asInstanceOf[CumulativeVolumeRow]
                    StockAggregateProjection(
                        row.totalCount,
                        row.ticker,
                        row.dateAt,
                        BigDecimal.valueOf(row.volume),
                        BigDecimal.valueOf(row.cumulativeVolume),
                    )
                }
            }

            val query = session.createQuery(criteriaQuery)
              .setFirstResult(params.offset)
              .setMaxResults(params.limit)
              .setTupleTransformer(rowTransformer)

            query.getResultList.asScala.toList
        }

        params.execStrategy match {
            case "nativeQuery" => nativeQueryExecStrategy()
            case "criteriaQuery" => cbExecStrategy()
            case _ => defaultExecStrategy()
        }
    }

    override def cumulativeDividends(ticker: String, limit: Int, offset: Int): List[StockCumulativeDividendsProjection] = {
        val sql =
            """
              |SELECT s.id.ticker, s.id.dateAt, s.dividends, SUM(s.dividends) OVER (PARTITION BY s.id.ticker ORDER BY s.id.dateAt) AS cumulativeDividends
              |FROM stock s
              |WHERE s.id.ticker = :ticker
              |ORDER BY s.id.dateAt DESC
              |""".stripMargin
        searchForTicker(sql = sql, resultClass = classOf[StockCumulativeDividendsProjection], ticker = ticker, limit = limit, offset = offset)
    }

    override def cumulativeCapitalGains(ticker: String, limit: Int, offset: Int): List[StockCumulativeCapitalGainsProjection] = {
        val sql =
            """
              |SELECT s.id.ticker, s.id.dateAt, s.capitalGains, SUM(s.capitalGains) OVER (PARTITION BY s.id.ticker ORDER BY s.id.dateAt) AS cumulativeCapitalGains
              |FROM stock s
              |WHERE s.id.ticker = :ticker
              |ORDER BY s.id.dateAt DESC
              |""".stripMargin
        searchForTicker(sql = sql, resultClass = classOf[StockCumulativeCapitalGainsProjection], ticker = ticker, limit = limit, offset = offset)
    }

    private def movingAverage[T](sql: String, resultClass: Class[T], ticker: String, limit: Int, offset: Int, days: Int): List[T] = {
        val query = em.createQuery(sql, resultClass)
          .setParameter("ticker", ticker)
          .setParameter("days", days)
          .setFirstResult(offset)
          .setMaxResults(limit)
        query.getResultList.asScala.toList
    }

    override def closingPriceMovingAverage(ticker: String, limit: Int, offset: Int, days: Int): List[StockAggregateProjection] = {
        val sql =
            """
              |SELECT s.id.ticker, s.id.dateAt, s.close, AVG(s.close) OVER (
              |PARTITION BY s.id.ticker ORDER BY s.id.dateAt ROWS BETWEEN :days PRECEDING AND CURRENT ROW
              |) AS movingAvgClose
              |FROM stock s
              |WHERE s.id.ticker = :ticker
              |ORDER BY s.id.dateAt DESC
              |""".stripMargin
        movingAverage(sql = sql, resultClass = classOf[StockAggregateProjection], ticker = ticker, limit = limit, offset = offset, days = days)
    }

    override def volumeMovingAverage(ticker: String, limit: Int, offset: Int, days: Int): List[StockAggregateProjection] = {
        val sql =
            """
              |SELECT s.id.ticker, s.id.dateAt, s.volume, AVG(s.volume) OVER (
              |PARTITION BY s.id.ticker ORDER BY s.id.dateAt ROWS BETWEEN :days PRECEDING AND CURRENT ROW
              |) AS movingAvgVolume
              |FROM stock s
              |WHERE s.id.ticker = :ticker
              |ORDER BY s.id.dateAt DESC
              |""".stripMargin
        movingAverage(sql = sql, resultClass = classOf[StockAggregateProjection], ticker = ticker, limit = limit, offset = offset, days = days)
    }

    override def closingPricePercentageChange(ticker: String, limit: Int, offset: Int): List[StockAggregateProjection] = {
        val sql =
            """
              |SELECT s.id.ticker, s.id.dateAt, s.close, (s.close - LAG(s.close) OVER (
              |PARTITION BY s.id.ticker ORDER BY s.id.dateAt)
              |) / LAG(s.close) OVER (PARTITION BY s.id.ticker ORDER BY s.id.dateAt) * 100 AS dailyPercentageChange
              |FROM stock s
              |WHERE s.id.ticker = :ticker
              |ORDER BY s.id.dateAt DESC
              |""".stripMargin
        searchForTicker(sql = sql, resultClass = classOf[StockAggregateProjection], ticker = ticker, limit = limit, offset = offset)
    }

    override def volumePercentageChange(ticker: String, limit: Int, offset: Int): List[StockAggregateProjection] = {
        val sql =
            """
              |SELECT s.id.ticker, s.id.dateAt, s.volume, (s.volume - LAG(s.volume) OVER (
              |PARTITION BY s.id.ticker ORDER BY s.id.dateAt)
              |) / LAG(s.volume) OVER (PARTITION BY s.id.ticker ORDER BY s.id.dateAt) * 100 AS dailyPercentageChange
              |FROM stock s
              |WHERE s.id.ticker = :ticker
              |ORDER BY s.id.dateAt DESC
              |""".stripMargin
        searchForTicker(sql = sql, resultClass = classOf[StockAggregateProjection], ticker = ticker, limit = limit, offset = offset)
    }

    private def searchOnDate[T](sql: String, resultClass: Class[T], dateAt: OffsetDateTime, limit: Int, offset: Int): List[T] = {
        val query = em.createQuery(sql, resultClass)
          .setParameter("dateAt", dateAt)
          .setFirstResult(offset)
          .setMaxResults(limit)
        query.getResultList.asScala.toList
    }

    override def rankByClosingPrice(dateAt: OffsetDateTime, limit: Int, offset: Int): List[StockAggregateProjection] = {
        val sql =
            """
              |SELECT s.id.ticker, s.id.dateAt, s.close, RANK() OVER (ORDER BY s.close DESC) AS rankClose
              |FROM stock s
              |WHERE s.id.dateAt = :dateAt
              |""".stripMargin
        searchOnDate(sql = sql, resultClass = classOf[StockAggregateProjection], dateAt = dateAt, limit = limit, offset = offset)
    }

    override def volumePercentile(dateAt: OffsetDateTime, limit: Int, offset: Int): List[StockAggregateProjection] = {
        val sql =
            """
              |SELECT s.id.ticker, s.id.dateAt, s.close, PERCENT_RANK() OVER (ORDER BY s.close DESC) AS closePercentileRank
              |FROM stock s
              |WHERE s1.id.ticker = :ticker
              |ORDER BY s.id.dateAt DESC
              |""".stripMargin
        searchOnDate(sql = sql, resultClass = classOf[StockAggregateProjection], dateAt = dateAt, limit = limit, offset = offset)
    }

    override def stdClosingPrice(ticker: String, limit: Int, offset: Int): List[StockAggregateProjection] = {
        val sql =
            """
              |SELECT s.id.ticker, s.id.dateAt, STDDEV(s.close) AS priceVolatility
              |FROM stock s
              |WHERE s.id.ticker = :ticker
              |GROUP BY s.id.ticker, s.id.dateAt
              |ORDER BY s.id.dateAt DESC
              |""".stripMargin
        searchForTicker(sql = sql, resultClass = classOf[StockAggregateProjection], ticker = ticker, limit = limit, offset = offset)
    }

    override def coefficientVariation(ticker: String, limit: Int, offset: Int): List[StockAggregateProjection] = {
        val sql =
            """
              |SELECT s.id.ticker, s.id.dateAt, STDDEV(s.close) / AVG(s.close)
              |FROM stock s
              |WHERE s.id.ticker = :ticker
              |ORDER BY s.id.dateAt DESC
              |""".stripMargin
        searchForTicker(sql = sql, resultClass = classOf[StockAggregateProjection], ticker = ticker, limit = limit, offset = offset)
    }

    override def correlationBetweenVolumeAndClosingPrice(ticker: String, limit: Int, offset: Int): List[StockAggregateProjection] = {
        val sql =
            """
              |SELECT s.id.ticker, s.id.dateAt, CORR(s.volume, s.close) AS correlationVolumePrice
              |FROM stock s
              |WHERE s.id.ticker = :ticker
              |GROUP BY s.id.ticker, s.id.dateAt
              |ORDER BY s.id.dateAt DESC
              |""".stripMargin
        searchForTicker(sql = sql, resultClass = classOf[StockAggregateProjection], ticker = ticker, limit = limit, offset = offset)
    }

    override def correlationBetweenDividendsAndClosingPrice(ticker: String, limit: Int, offset: Int): List[StockAggregateProjection] = {
        val sql =
            """
              |SELECT s.id.ticker, s.id.dateAt, CORR(s.dividends, s.close) AS correlationDividendsPrice
              |FROM stock s
              |WHERE s.id.ticker = :ticker
              |GROUP BY s.id.ticker, s.id.dateAt
              |ORDER BY s.id.dateAt DESC
              |""".stripMargin
        searchForTicker(sql = sql, resultClass = classOf[StockAggregateProjection], ticker = ticker, limit = limit, offset = offset)
    }

    override def avgMonthlyClosingPrice(ticker: String, limit: Int, offset: Int): List[StockAggregateProjection] = {
        val sql =
            """
              |SELECT s.id.ticker, DATE_TRUNC('month', s.id.dateAt) AS month, AVG(s.close) AS avgMonthlyClose
              |FROM stock s
              |WHERE s.id.ticker = :ticker
              |GROUP BY s.id.ticker, DATE_TRUNC('month', s.id.dateAt)
              |ORDER BY month DESC
              |""".stripMargin
        searchForTicker(sql = sql, resultClass = classOf[StockAggregateProjection], ticker = ticker, limit = limit, offset = offset)
    }

    override def totalDividendsPaidPerYear(ticker: String, limit: Int, offset: Int): List[StockAggregateProjection] = {
        val sql =
            """
              |SELECT s.id.ticker, DATE_TRUNC('year', s.id.dateAt) AS year, SUM(s.dividends) AS dividendsPaidPerYear
              |FROM stock s
              |WHERE s.id.ticker = :ticker
              |GROUP BY s.id.ticker, DATE_TRUNC('year', s.id.dateAt)
              |ORDER BY year DESC
              |""".stripMargin
        searchForTicker(sql = sql, resultClass = classOf[StockAggregateProjection], ticker = ticker, limit = limit, offset = offset)
    }

    override def dailyHighLowSpread(ticker: String, limit: Int, offset: Int): List[StockAggregateProjection] = {
        val sql =
            """
              |SELECT s.id.ticker, s.id.dateAt, s.high, s.low, (s.high - s.low) AS dailySpread
              |FROM stock s
              |WHERE s.id.ticker = :ticker
              |ORDER BY s.id.dateAt DESC
              |""".stripMargin
        searchForTicker(sql = sql, resultClass = classOf[StockAggregateProjection], ticker = ticker, limit = limit, offset = offset)
    }

    override def dividendYield(ticker: String, limit: Int, offset: Int): List[StockDividendYieldProjection] = {
        val sql =
            """
              |SELECT s.id.ticker, s.id.dateAt, s.dividends, s.close, (s.dividends / s.close) * 100 AS dividendYield
              |FROM stock s
              |WHERE s.id.ticker = :ticker
              |ORDER BY s.id.dateAt DESC
              |""".stripMargin
        searchForTicker(sql = sql, resultClass = classOf[StockDividendYieldProjection], ticker = ticker, limit = limit, offset = offset)
    }

    override def trackStockSplits(stockSplits: Int, limit: Int, offset: Int): List[Stock] = {
        val sql =
            """
              |SELECT s
              |FROM stock s
              |WHERE s.stockSplits = :stockSplits
              |ORDER BY s.id.dateAt DESC
              |""".stripMargin
        val query = em.createQuery(sql, classOf[Stock])
          .setParameter("stockSplits", stockSplits)
          .setFirstResult(offset)
          .setMaxResults(limit)
        query.getResultList.asScala.toList
    }

    override def totalCapitalGains(ticker: String, limit: Int, offset: Int): List[StockCapitalGainsAggregateProjection] = {
        val sql =
            """
              |SELECT s.id.ticker, s.id.dateAt, s.capitalGains, s.close,
              |SUM(s.capitalGains) OVER (PARTITION BY s.id.dateAt) AS totalCapitalGains
              |FROM stock s
              |WHERE s.id.ticker = :ticker
              |ORDER BY s.id.dateAt DESC
              |""".stripMargin
        searchForTicker(sql = sql, resultClass = classOf[StockCapitalGainsAggregateProjection], ticker = ticker, limit = limit, offset = offset)
    }

    override def relativeContributionOfCapitalGains(ticker: String, limit: Int, offset: Int): List[StockCapitalGainsAggregateProjection] = {
        val sql =
            """
              |SELECT s.id.ticker, s.id.dateAt, s.capitalGains, s.close, (s.capitalGains / s.close) * 100 AS capitalGainsContribution
              |FROM stock s
              |WHERE s.id.ticker = :ticker
              |ORDER BY s.id.dateAt DESC
              |""".stripMargin
        searchForTicker(sql = sql, resultClass = classOf[StockCapitalGainsAggregateProjection], ticker = ticker, limit = limit, offset = offset)
    }

    override def avgMonthlyVolume(ticker: String, limit: Int, offset: Int): List[StockAggregateProjection] = {
        val sql =
            """
              |SELECT s.id.ticker, EXTRACT(MONTH FROM s.id.dateAt) AS month, AVG(s.volume) * 100 AS avgVolume
              |FROM stock s
              |WHERE s.id.ticker = :ticker
              |GROUP BY s.id.ticker, EXTRACT(MONTH FROM s.id.dateAt)
              |ORDER BY month DESC
              |""".stripMargin
        searchForTicker(sql = sql, resultClass = classOf[StockAggregateProjection], ticker = ticker, limit = limit, offset = offset)
    }

    override def seasonalPriceTrends(ticker: String, limit: Int, offset: Int): List[StockAggregateProjection] = {
        val sql =
            """
              |SELECT s.id.ticker, EXTRACT(MONTH FROM s.id.dateAt) AS month, AVG(s.close) * 100 AS avgClose
              |FROM stock s
              |WHERE s.id.ticker = :ticker
              |GROUP BY s.id.ticker, EXTRACT(MONTH FROM s.id.dateAt)
              |ORDER BY month DESC
              |""".stripMargin
        searchForTicker(sql = sql, resultClass = classOf[StockAggregateProjection], ticker = ticker, limit = limit, offset = offset)
    }

    override def findOutliersInClosingPrice(ticker: String, limit: Int, offset: Int): List[StockAggregateProjection] = {
        val sql =
            """
              |SELECT s.id.ticker, s.id.dateAt, s.close
              |FROM stock s
              |WHERE s.close > (SELECT AVG(s1.close) + 2 * STDDEV(s1.close) FROM stock s1 WHERE s1.id.ticker = :ticker)
              |ORDER BY s.id.dateAt DESC
              |""".stripMargin
        searchForTicker(sql = sql, resultClass = classOf[StockAggregateProjection], ticker = ticker, limit = limit, offset = offset)
    }

    override def findOutliersInVolume(ticker: String, limit: Int, offset: Int): List[StockAggregateProjection] = {
        val sql =
            """
              |SELECT s.id.ticker, s.id.dateAt, s.volume
              |FROM stock s
              |WHERE s.volume > (SELECT AVG(s1.volume) + 2 * STDDEV(s1.volume) FROM stock s1 WHERE s1.id.ticker = :ticker)
              |ORDER BY s.id.dateAt DESC
              |""".stripMargin
        searchForTicker(sql = sql, resultClass = classOf[StockAggregateProjection], ticker = ticker, limit = limit, offset = offset)
    }
}
