package org.example.db.stockmarket

import jakarta.persistence.{Column, ColumnResult, ConstructorResult, Embeddable, EmbeddedId, Entity, EntityResult, FieldResult, NamedNativeQueries, NamedNativeQuery, NamedQueries, NamedQuery, SqlResultSetMapping, SqlResultSetMappings, Table}

import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.Objects

case class StockWithRunningTotal(stock: Stock, runningTotal: BigDecimal)

case class StockAggregateProjection(
                                     totalCount: Long,
                                     ticker: String,
                                     dateAt: OffsetDateTime,
                                     aggregate: BigDecimal,
                                     aggregated: BigDecimal
                                   )

case class StockDividendYieldProjection(stockId: StockId, dividends: BigDecimal, close: BigDecimal, dividendYield: BigDecimal)

case class StockCapitalGainsAggregateProjection(stockId: StockId, capitalGains: BigDecimal, close: BigDecimal, capitalGainsContribution: BigDecimal)

case class StockCumulativeDividendsProjection(stockId: StockId, dividends: BigDecimal, cumulativeDividends: BigDecimal)

case class StockCumulativeCapitalGainsProjection(stockId: StockId, capitalGains: BigDecimal, cumulativeCapitalGains: BigDecimal)

case class StockStatisticsProjection(
                                      stock: Stock,
                                      openSum: BigDecimal,
                                      openAvg: BigDecimal,
                                      openMin: BigDecimal,
                                      openMax: BigDecimal,
                                      openRank: Long
                                    )

case class StockSearchParams(
                              ticker: String = null,
                              limit: Int = 10,
                              offset: Int = 0,
                              sortColumn: String = "dateAt",
                              isSortAsc: Boolean = false,
                              execStrategy: String = "default"
                            )

@Embeddable
class StockId(
               private var ticker: String = null,
               private var dateAt: OffsetDateTime = null
             ) extends Serializable {
    def this() = {
        this(null, null)
    }

    @Column(nullable = false)
    def getTicker: String = ticker

    def setTicker(ticker: String): Unit = this.ticker = ticker

    @Column(name = "date_at", nullable = false)
    def getDateAt: OffsetDateTime = dateAt

    def setDateAt(dateAt: OffsetDateTime): Unit = this.dateAt = dateAt

    override def equals(obj: Any): Boolean = {
        if (this == obj) return true
        if (obj == null || getClass != obj.getClass) return false
        val other = obj.asInstanceOf[StockId]
        Objects.equals(ticker, other.ticker) && Objects.equals(dateAt, other.dateAt)
    }

    override def hashCode(): Int = Objects.hash(ticker, dateAt)
}

object Stock {
    final val CUMULATIVE_VOLUME_NQ = "cumulativeVolumeNQ"
    final val CUMULATIVE_VOLUME_NNQ = "cumulativeVolumeNNQ"
    final val CUMULATIVE_VOLUME_MAPPING = "cumulativeVolumeMapping"
}

@SqlResultSetMappings(Array(
    new SqlResultSetMapping(
        name = Stock.CUMULATIVE_VOLUME_MAPPING,
        classes = Array(
            new ConstructorResult(
                targetClass = classOf[StockAggregateProjection],
                columns = Array(
                    new ColumnResult(name = "totalCount"),
                    new ColumnResult(name = "ticker"),
                    new ColumnResult(name = "dateAt", classOf[OffsetDateTime]),
                    new ColumnResult(name = "volume", classOf[BigDecimal]),
                    new ColumnResult(name = "cumulativeVolume"),
                )
            )
        ),
    )
))
@NamedQueries(Array(
    new NamedQuery(
        name = Stock.CUMULATIVE_VOLUME_NQ,
        query =
            """
            SELECT COUNT(s.id) OVER () AS totalCount, s.id.ticker, s.id.dateAt, s.volume, SUM(s.volume) OVER (PARTITION BY s.id.ticker ORDER BY s.id.dateAt) AS cumulativeVolume
            FROM stock s
            WHERE s.id.ticker = :ticker
            ORDER BY :orderBy
            """
    )
))
@NamedNativeQueries(Array(
    new NamedNativeQuery(
        name = Stock.CUMULATIVE_VOLUME_NNQ,
        query =
            """
            SELECT COUNT(ROW(ticker, date_at)) OVER () AS totalCount, ticker, date_at AS dateAt, volume, SUM(volume) OVER (PARTITION BY ticker ORDER BY date_at) AS cumulativeVolume
            FROM stock
            WHERE ticker = :ticker
            ORDER BY :orderBy
            """,
        resultSetMapping = Stock.CUMULATIVE_VOLUME_MAPPING
    )
))
@Entity(name = "stock")
@Table(name = "stock")
class Stock(
             private var id: StockId = null,
             private var open: BigDecimal = BigDecimal.ZERO,
             private var high: BigDecimal = BigDecimal.ZERO,
             private var low: BigDecimal = BigDecimal.ZERO,
             private var close: BigDecimal = BigDecimal.ZERO,
             private var adjustedClose: BigDecimal = BigDecimal.ZERO,
             private var volume: Long = 0,
             private var dividends: BigDecimal = BigDecimal.ZERO,
             private var stockSplits: BigDecimal = BigDecimal.ZERO,
             private var capitalGains: BigDecimal = BigDecimal.ZERO
           ) extends Serializable {
    def this() = {
        this(null)
    }

    @EmbeddedId
    def getId: StockId = id

    def setId(id: StockId): Unit = this.id = id

    def getOpen: BigDecimal = open

    def setOpen(open: BigDecimal): Unit = this.open = open

    def getHigh: BigDecimal = high

    def setHigh(high: BigDecimal): Unit = this.high = high

    def getLow: BigDecimal = low

    def setLow(low: BigDecimal): Unit = this.low = low

    def getClose: BigDecimal = close

    def setClose(close: BigDecimal): Unit = this.close = close

    @Column(name = "adjusted_close")
    def getAdjustedClose: BigDecimal = adjustedClose

    def setAdjustedClose(adjustedClose: BigDecimal): Unit = this.adjustedClose = adjustedClose

    def getVolume: Long = volume

    def setVolume(volume: Long): Unit = this.volume = volume

    def getDividends: BigDecimal = dividends

    def setDividends(dividends: BigDecimal): Unit = this.dividends = dividends

    @Column(name = "stock_splits")
    def getStockSplits: BigDecimal = stockSplits

    def setStockSplits(stockSplits: BigDecimal): Unit = this.stockSplits = stockSplits

    @Column(name = "capital_gains")
    def getCapitalGains: BigDecimal = capitalGains

    def setCapitalGains(capitalGains: BigDecimal): Unit = this.capitalGains = capitalGains

    override def equals(obj: Any): Boolean = {
        if (this == obj) return true
        if (obj == null || getClass != obj.getClass) return false
        val other = obj.asInstanceOf[Stock]
        Objects.equals(id, other.id) &&
          Objects.equals(open, other.open) &&
          Objects.equals(high, other.high) &&
          Objects.equals(low, other.low) &&
          Objects.equals(close, other.close) &&
          Objects.equals(adjustedClose, other.adjustedClose) &&
          volume == other.volume &&
          Objects.equals(dividends, other.dividends) &&
          Objects.equals(stockSplits, other.stockSplits) &&
          Objects.equals(capitalGains, other.capitalGains)
    }

    override def hashCode(): Int = Objects.hash(id, open, high, low, close, adjustedClose, volume, dividends, stockSplits, capitalGains)
}
