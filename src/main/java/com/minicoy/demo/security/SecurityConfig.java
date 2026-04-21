package com.minicoy.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors
                        .configurationSource(
                                corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // PUBLIC endpoints
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/products").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/products/**").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/categories").permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/api/orders/place").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/*.html").permitAll()
                        .requestMatchers("/").permitAll()

                        // ADMIN only endpoints
                        .requestMatchers(HttpMethod.POST,
                                "/api/products").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,
                                "/api/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,
                                "/api/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,
                                "/api/orders").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,
                                "/api/orders/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,
                                "/api/orders/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,
                                "/api/orders/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}