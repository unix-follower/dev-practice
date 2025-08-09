package org.example.assistantonsbservlet.svc.finance;

import org.example.assistantonsbservlet.api.finance.stockmarket.model.StocksResponseDto;

public interface StockMarketApiFacade {
    /**
     * @param page 1-based page
     */
    StocksResponseDto findByTicker(String ticker, int page, int pageSize);
}
