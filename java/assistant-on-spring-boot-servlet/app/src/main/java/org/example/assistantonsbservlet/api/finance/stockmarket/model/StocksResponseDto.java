package org.example.assistantonsbservlet.api.finance.stockmarket.model;

import java.util.List;

public record StocksResponseDto(
    long total,
    List<StockDto> stocks
) {
}
