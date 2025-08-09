package org.example.assistantonsbservlet.convert;

import jakarta.persistence.Tuple;
import org.example.assistantonsbservlet.api.finance.stockmarket.model.StockDto;
import org.example.assistantonsbservlet.api.finance.stockmarket.model.StocksResponseDto;
import org.example.db.stockmarket.model.Stock;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@WritingConverter
public class StockTuplesToDtoConverter implements Converter<List<Tuple>, StocksResponseDto> {
    private static final byte STOCK_TUPLE_INDEX = 1;

    @Override
    public StocksResponseDto convert(List<Tuple> source) {
        final var stockDtoList = source.stream()
            .map(tuple -> {
                final var stock = tuple.get(STOCK_TUPLE_INDEX, Stock.class);
                return new StockDto(
                    stock.getId().getTicker(),
                    stock.getId().getDateAt(),
                    stock.getOpen().doubleValue(),
                    stock.getHigh().doubleValue(),
                    stock.getLow().doubleValue(),
                    stock.getClose().doubleValue(),
                    stock.getAdjustedClose().doubleValue(),
                    stock.getVolume(),
                    stock.getDividends().doubleValue(),
                    stock.getStockSplits().doubleValue(),
                    stock.getCapitalGains().doubleValue()
                );
            })
            .toList();
        final long totalCount;
        if (!source.isEmpty()) {
            totalCount = source.getFirst().get("totalCount", Long.class);
        } else {
            totalCount = 0;
        }

        return new StocksResponseDto(totalCount, stockDtoList);
    }
}
