package org.example.assistantonsbservlet.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.JsonNodeFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.autoconfigure.LogbookProperties;
import org.zalando.logbook.core.Conditions;
import org.zalando.logbook.core.DefaultHttpLogFormatter;
import org.zalando.logbook.core.DefaultHttpLogWriter;
import org.zalando.logbook.core.DefaultSink;

import java.util.List;

@Configuration
@ComponentScan(basePackages = {
    "org.example.assistantonsbservlet",
    "org.example.db",
})
@EnableConfigurationProperties({AppProperties.class})
class ApplicationConfig {
    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
            .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
            .configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true)
            .configure(JsonNodeFeature.STRIP_TRAILING_BIGDECIMAL_ZEROES, true);
    }

    @Bean
    ConversionService appConversionService(List<Converter<?, ?>> converters) {
        final var service = new DefaultConversionService();
        converters.forEach(service::addConverter);
        return service;
    }

    @Bean
    Logbook logbook(LogbookProperties logbookProperties) {
        final var excludePredicates = logbookProperties.getExclude().stream()
            .map(Conditions::requestTo)
            .toList();

        return Logbook.builder()
            .condition(
                Conditions.exclude(excludePredicates)
            )
            .sink(
                new DefaultSink(
                    new DefaultHttpLogFormatter(),
                    new DefaultHttpLogWriter()
                )
            )
            .build();
    }
}
