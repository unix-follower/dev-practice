package org.example.assistantonsbservlet.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties("app")
public record AppProperties(
    @NestedConfigurationProperty CorsProperties cors,
    @NestedConfigurationProperty PubChemProperties pubChem,
    @NestedConfigurationProperty StockMarketProperties stockMarket
) {
}
