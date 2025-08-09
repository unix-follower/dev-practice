package org.example.assistantonsbservlet.config;

import java.time.Duration;

public record HikariProperties(
    String poolName,
    int maximumPoolSize,
    int minimumIdle,
    Duration connectionTimeout,
    Duration validationTimeout,
    Duration idleTimeout,
    Duration maxLifetime,
    Duration keepAliveTime
) {
}
