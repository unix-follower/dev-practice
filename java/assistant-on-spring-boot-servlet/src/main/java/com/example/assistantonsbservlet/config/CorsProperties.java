package com.example.assistantonsbservlet.config;

import java.time.Duration;
import java.util.List;

public record CorsProperties(
    List<String> accessControlAllowOriginPatterns,
    Duration accessControlMaxAge,
    List<String> accessControlAllowHeaders,
    List<String> accessControlAllowMethods
) {
}
