package org.example.assistantonsbservlet.finance;

import org.example.assistantonsbservlet.api.finance.stockmarket.model.StocksResponseDto;

public sealed interface StockMarketApiFacade permits StockMarketFacade, StockMarketNoopFacade {
    /**
     * @param page 1-based page
     */
    StocksResponseDto findByTicker(String ticker, int page, int pageSize);
}
