package org.example.db.stockmarket

import jakarta.persistence.{Column, Embeddable, EmbeddedId, Entity, Table}

import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.Objects

@Embeddable
class StockId extends Serializable {
    @Column(nullable = false)
    private var ticker: String = null

    @Column(name = "date_at", nullable = false)
    private var dateAt: OffsetDateTime = null

    def getTicker: String = ticker

    def setTicker(ticker: String): Unit = this.ticker = ticker

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

@Entity(name = "stock")
@Table(name = "stock")
class Stock extends Serializable {
    @EmbeddedId
    private var id: StockId = null

    private var open: BigDecimal = BigDecimal.ZERO
    private var high: BigDecimal = BigDecimal.ZERO
    private var low: BigDecimal = BigDecimal.ZERO
    private var close: BigDecimal = BigDecimal.ZERO

    @Column(name = "adjusted_close")
    private var adjustedClose: BigDecimal = BigDecimal.ZERO

    private var volume: Long = 0
    private var dividends: BigDecimal = BigDecimal.ZERO

    @Column(name = "stock_splits")
    private var stockSplits: BigDecimal = BigDecimal.ZERO

    @Column(name = "capital_gains")
    private var capitalGains: BigDecimal = BigDecimal.ZERO

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

    def getAdjustedClose: BigDecimal = adjustedClose

    def setAdjustedClose(adjustedClose: BigDecimal): Unit = this.adjustedClose = adjustedClose

    def getVolume: Long = volume

    def setVolume(volume: Long): Unit = this.volume = volume

    def getDividends: BigDecimal = dividends

    def setDividends(dividends: BigDecimal): Unit = this.dividends = dividends

    def getStockSplits: BigDecimal = stockSplits

    def setStockSplits(stockSplits: BigDecimal): Unit = this.stockSplits = stockSplits

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
