package com.medialistmaker.gateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {

         http.cors((ServerHttpSecurity.CorsSpec::disable)).csrf(ServerHttpSecurity.CsrfSpec::disable);
         return http.build();
     }
}
