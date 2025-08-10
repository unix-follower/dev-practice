package org.example.assistantonsbservlet.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.stock-market")
public record StockMarketProperties(
    @ConfigurationProperties
    DataSourceProperties ds,
    @ConfigurationProperties
    HikariProperties hikari
) {
}
