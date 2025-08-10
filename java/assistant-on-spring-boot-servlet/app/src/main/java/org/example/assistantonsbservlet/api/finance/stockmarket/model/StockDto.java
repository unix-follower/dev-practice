package org.example.assistantonsbservlet.api.finance.stockmarket.model;

import java.time.OffsetDateTime;

public record StockDto(
    String ticker,
    OffsetDateTime dateAt,
    double open,
    double high,
    double low,
    double close,
    double adjustedClose,
    long volume,
    double dividends,
    double stockSplits,
    double capitalGains
) {
}
