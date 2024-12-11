package com.henrytran1803.BEBakeManage.config.security.securityconfig;

import com.henrytran1803.BEBakeManage.config.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CorsConfigurationSource corsConfigurationSource = corsConfigurationSource();
        http.cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers( "/api/nofications/**").permitAll()
                        .requestMatchers( "/websocket/**", "/ws/**").permitAll()
                        .requestMatchers("/api/user/bills/*/status").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/categories").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/products/search/active").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products/{id}").permitAll()
                        .requestMatchers("/api/payment/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/price/{id}/history").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/products/cart").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/auth/register").permitAll()
                        .requestMatchers("/api/auth/**","/api/payment/**", "/uploads/**","/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/user/bills").permitAll()
                        .requestMatchers("/api/ingredients/**", "/api/supplier/**", "/api/units/**").hasRole("MANAGE")
                        .requestMatchers("/api/upload").hasRole("MANAGE")
                        .requestMatchers("/api/user/bills/**").hasAnyRole("USER","MANAGE")
                        .requestMatchers("/api/user/**").hasRole("USER")
                        .requestMatchers("/api/admin/tables").hasRole("MANAGE")
                        .requestMatchers("/api/admin/**", "/api/categories/**", "/api/categories/", "/api/recipes/**","/api/dashboard/**", "/api/products/**", "/api/promotions/**").hasRole("MANAGE")
                        .requestMatchers("/api/disposed/**",
                                "/api/user/bills/**",
                                "/api/admin/**",
                                "/api/dashboard/**",
                                "/api/discounts/**",
                                "/api/admin/**",
                                "/api/categories/**",
                                "/api/recipes/**",
                                "/api/products/**",
                                "/api/promotions/**",
                                "/api/productbatches/**",
                                "/api/price/**")
                        .hasRole("MANAGE")

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    configuration.setAllowedOriginPatterns(Collections.singletonList("*"));

    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

    configuration.setAllowedHeaders(Arrays.asList(
            "Origin",
            "Access-Control-Allow-Origin",
            "Content-Type",
            "Accept",
            "Authorization",
            "Origin, Accept",
            "X-Requested-With",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
    ));

    configuration.setExposedHeaders(Arrays.asList(
            "Origin",
            "Content-Type",
            "Accept",
            "Authorization",
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials"
    ));

    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L);


    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
}
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}