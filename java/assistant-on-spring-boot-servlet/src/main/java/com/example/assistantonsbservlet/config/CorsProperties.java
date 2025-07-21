package com.example.assistantonsbservlet.config;

import java.time.Duration;
import java.util.List;

public record CorsProperties(
    String accessControlAllowOrigin,
    Duration accessControlMaxAge,
    List<String> accessControlAllowHeaders,
    List<String> accessControlAllowMethods
) {
}
