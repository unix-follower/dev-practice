package org.example.assistantonsbservlet.api.finance.stockmarket;

import org.example.assistantonsbservlet.api.finance.stockmarket.model.StocksResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/v1/finance/stock-market")
public interface FinanceStockMarketApi {
    @GetMapping
    ResponseEntity<StocksResponseDto> getByTicker(
        @RequestParam String ticker,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int pageSize
    );
}
