package org.example.assistantonsbservlet.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.autoconfigure.LogbookProperties;
import org.zalando.logbook.core.Conditions;
import org.zalando.logbook.core.DefaultHttpLogFormatter;
import org.zalando.logbook.core.DefaultHttpLogWriter;
import org.zalando.logbook.core.DefaultSink;

import java.util.List;

@Configuration
@EnableJpaRepositories
@EnableTransactionManagement
@EnableConfigurationProperties({AppProperties.class})
class ApplicationConfig {
    @Bean
    ConversionService conversionService(List<Converter<?, ?>> converters) {
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
