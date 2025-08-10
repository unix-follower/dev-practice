package org.example.db.stockmarket.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity(name = "stock")
@Table(name = "stock")
@NamedQuery(
    name = Stock.FIND_BY_TICKER_NQ,
    query = """
        SELECT COUNT(s.id) OVER () AS totalCount, s
        FROM stock s
        WHERE s.id.ticker = :ticker
        ORDER BY s.id.dateAt DESC
        """
)
public class Stock implements Serializable {
    public static final String FIND_BY_TICKER_NQ = "findByTickerNQ";

    @Embeddable
    public static class StockId implements Serializable {

        @Column(nullable = false)
        private String ticker;

        @Column(name = "date_at", nullable = false)
        private OffsetDateTime dateAt;

        public StockId() {
        }

        public StockId(String ticker, OffsetDateTime dateAt) {
            this.ticker = ticker;
            this.dateAt = dateAt;
        }

        public String getTicker() {
            return ticker;
        }

        public void setTicker(String ticker) {
            this.ticker = ticker;
        }

        public OffsetDateTime getDateAt() {
            return dateAt;
        }

        public void setDateAt(OffsetDateTime dateAt) {
            this.dateAt = dateAt;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            StockId stockId = (StockId) obj;
            return Objects.equals(ticker, stockId.ticker) &&
                Objects.equals(dateAt, stockId.dateAt);
        }

        @Override
        public int hashCode() {
            return Objects.hash(ticker, dateAt);
        }
    }

    @EmbeddedId
    private StockId id;

    private BigDecimal open = BigDecimal.ZERO;
    private BigDecimal high = BigDecimal.ZERO;
    private BigDecimal low = BigDecimal.ZERO;
    private BigDecimal close = BigDecimal.ZERO;

    @Column(name = "adjusted_close")
    private BigDecimal adjustedClose = BigDecimal.ZERO;

    private long volume = 0;

    private BigDecimal dividends = BigDecimal.ZERO;

    @Column(name = "stock_splits")
    private BigDecimal stockSplits = BigDecimal.ZERO;

    @Column(name = "capital_gains")
    private BigDecimal capitalGains = BigDecimal.ZERO;

    public StockId getId() {
        return id;
    }

    public void setId(StockId id) {
        this.id = id;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public BigDecimal getAdjustedClose() {
        return adjustedClose;
    }

    public void setAdjustedClose(BigDecimal adjustedClose) {
        this.adjustedClose = adjustedClose;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public BigDecimal getDividends() {
        return dividends;
    }

    public void setDividends(BigDecimal dividends) {
        this.dividends = dividends;
    }

    public BigDecimal getStockSplits() {
        return stockSplits;
    }

    public void setStockSplits(BigDecimal stockSplits) {
        this.stockSplits = stockSplits;
    }

    public BigDecimal getCapitalGains() {
        return capitalGains;
    }

    public void setCapitalGains(BigDecimal capitalGains) {
        this.capitalGains = capitalGains;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Stock stock)) {
            return false;
        }
        return volume == stock.volume &&
            Objects.equals(id, stock.id) &&
            Objects.equals(open, stock.open) &&
            Objects.equals(high, stock.high) &&
            Objects.equals(low, stock.low) &&
            Objects.equals(close, stock.close) &&
            Objects.equals(adjustedClose, stock.adjustedClose) &&
            Objects.equals(dividends, stock.dividends) &&
            Objects.equals(stockSplits, stock.stockSplits) &&
            Objects.equals(capitalGains, stock.capitalGains);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            open,
            high,
            low,
            close,
            adjustedClose,
            volume,
            dividends,
            stockSplits,
            capitalGains
        );
    }
}
