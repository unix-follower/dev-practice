package com.example.assistantonsbservlet.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
class SecurityConfig {
    private final AppProperties appProperties;

    SecurityConfig(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationEventPublisher.class)
    DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher(ApplicationEventPublisher delegate) {
        return new DefaultAuthenticationEventPublisher(delegate);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final var source = new UrlBasedCorsConfigurationSource();

        final var corsConfiguration = new CorsConfiguration();
        final var cors = appProperties.cors();
        corsConfiguration.addAllowedOrigin(cors.accessControlAllowOrigin());
        corsConfiguration.setMaxAge(cors.accessControlMaxAge());
        corsConfiguration.setAllowedHeaders(cors.accessControlAllowHeaders());
        corsConfiguration.setAllowedMethods(cors.accessControlAllowMethods());

        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(Customizer.withDefaults())
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/").anonymous().anyRequest().permitAll()
            );

        return http.build();
    }
}
