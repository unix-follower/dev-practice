package org.example.assistantonsbservlet.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.pubchem")
public record PubChemProperties(
    @ConfigurationProperties
    DataSourceProperties ds,
    @ConfigurationProperties
    HikariProperties hikari,
    @ConfigurationProperties
    DataSourceProperties pgAgeGraph,
    @ConfigurationProperties
    HikariProperties hikariPgAgeGraph
) {
}
