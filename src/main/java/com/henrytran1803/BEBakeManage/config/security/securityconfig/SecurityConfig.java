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
                        .requestMatchers(HttpMethod.POST, "/api/user/bills").permitAll()  // Rule cụ thể cho API status
                        .requestMatchers(HttpMethod.GET, "/api/user/bills/{billId}").permitAll()
                        .requestMatchers("/api/user/bills/*/status").permitAll()  // Rule cụ thể cho API status
                        .requestMatchers( "/api/nofications/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers( "/websocket/**", "/ws/**").permitAll()
                        .requestMatchers("/api/user/bills/*/status").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/categories").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/products/search/active").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products/{id}").permitAll()
                        .requestMatchers("/api/payment/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/price/{id}/history").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/products/cart").permitAll()
                        .requestMatchers("/api/uploads/**").permitAll()

                        // Các rules yêu cầu authentication
                        .requestMatchers(HttpMethod.POST,"/api/auth/register").hasRole("MANAGE")

                        .requestMatchers("/api/user/bills/**").hasAnyRole("USER","MANAGE")
                        .requestMatchers("/api/user/**").hasRole("USER")
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
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "http://127.0.0.1:3000",
                "ws://localhost:3000"
                ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
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