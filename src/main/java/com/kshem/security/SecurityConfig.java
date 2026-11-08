package com.kshem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomUserDetailsService userDetailsService,
            JwtAuthenticationEntryPoint authenticationEntryPoint,
            CustomAccessDeniedHandler accessDeniedHandler) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    // 1. Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Authentication Provider
    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider =
                new DaoAuthenticationProvider(userDetailsService);

        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    // 3. Authentication Manager
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();
    }

    // 4. Security Filter Chain
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            CorsConfigurationSource corsConfigurationSource)
            throws Exception {

        http

            // Enable CORS
            .cors(cors ->
                cors.configurationSource(corsConfigurationSource))

            // Disable CSRF for REST APIs
            .csrf(csrf -> csrf.disable())

            // Stateless session management
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS))

            // Custom exception handling
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
            )

            // Role-based authorization
            .authorizeHttpRequests(auth -> auth

                // PUBLIC APIs
                .requestMatchers(
                    "/api/auth/**",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html"
                ).permitAll()

                // ADMIN APIs
                .requestMatchers("/api/admin/**")
                    .hasRole("ADMIN")

                // STUDENT APIs
                .requestMatchers(HttpMethod.POST, "/api/students")
                    .hasRole("STUDENT")

                .requestMatchers(HttpMethod.PUT, "/api/students/**")
                    .hasRole("STUDENT")

                .requestMatchers(HttpMethod.DELETE, "/api/students/**")
                    .hasRole("ADMIN")

                .requestMatchers(HttpMethod.GET, "/api/students/eligible")
                    .hasAnyRole("ADMIN", "PLACEMENT_OFFICER")

                .requestMatchers(HttpMethod.GET, "/api/students")
                    .hasAnyRole("ADMIN", "PLACEMENT_OFFICER")

                .requestMatchers(HttpMethod.GET, "/api/students/**")
                    .hasAnyRole("ADMIN", "PLACEMENT_OFFICER", "STUDENT")

                // COMPANY APIs
                .requestMatchers(HttpMethod.POST, "/api/companies")
                    .hasRole("COMPANY")

                .requestMatchers(HttpMethod.PUT, "/api/companies/**")
                    .hasRole("COMPANY")

                .requestMatchers(HttpMethod.DELETE, "/api/companies/**")
                    .hasRole("ADMIN")

                .requestMatchers(HttpMethod.GET, "/api/companies")
                    .hasAnyRole("ADMIN", "PLACEMENT_OFFICER")

                .requestMatchers(HttpMethod.GET, "/api/companies/**")
                    .authenticated()

                // JOB DRIVE APIs
                .requestMatchers(HttpMethod.POST, "/api/job-drives")
                    .hasRole("COMPANY")

                .requestMatchers(HttpMethod.PUT, "/api/job-drives/**")
                    .hasAnyRole("ADMIN", "PLACEMENT_OFFICER", "COMPANY")

                .requestMatchers(HttpMethod.PATCH, "/api/job-drives/*/close")
                    .hasAnyRole("ADMIN", "PLACEMENT_OFFICER")

                .requestMatchers(HttpMethod.GET, "/api/job-drives/**")
                    .authenticated()

                // APPLICATION APIs
                .requestMatchers(HttpMethod.POST, "/api/applications")
                    .hasRole("STUDENT")

                .requestMatchers(HttpMethod.PATCH, "/api/applications/*/withdraw")
                    .hasRole("STUDENT")

                .requestMatchers(HttpMethod.PATCH, "/api/applications/*/status")
                    .hasAnyRole("ADMIN", "PLACEMENT_OFFICER")

                .requestMatchers(HttpMethod.GET, "/api/applications")
                    .hasAnyRole("ADMIN", "PLACEMENT_OFFICER")

                .requestMatchers(HttpMethod.GET, "/api/applications/student/**")
                    .hasAnyRole("ADMIN", "PLACEMENT_OFFICER", "STUDENT")

                .requestMatchers(HttpMethod.GET, "/api/applications/drive/**")
                    .hasAnyRole("ADMIN", "PLACEMENT_OFFICER", "COMPANY")

                .requestMatchers(HttpMethod.GET, "/api/applications/**")
                    .hasAnyRole(
                        "ADMIN", "PLACEMENT_OFFICER", "COMPANY", "STUDENT")

                // INTERVIEW APIs
                .requestMatchers(HttpMethod.POST, "/api/interviews")
                    .hasAnyRole("ADMIN", "PLACEMENT_OFFICER")

                .requestMatchers(HttpMethod.PUT, "/api/interviews/**")
                    .hasAnyRole("ADMIN", "PLACEMENT_OFFICER")

                .requestMatchers(HttpMethod.PATCH, "/api/interviews/**")
                    .hasAnyRole("ADMIN", "PLACEMENT_OFFICER")

                .requestMatchers(HttpMethod.GET, "/api/interviews/**")
                    .hasAnyRole("ADMIN", "PLACEMENT_OFFICER", "STUDENT")

                // OFFER APIs
                .requestMatchers(HttpMethod.POST, "/api/offers/**")
                    .hasAnyRole("ADMIN", "PLACEMENT_OFFICER")

                .requestMatchers(HttpMethod.PATCH, "/api/offers/*/decision")
                    .hasRole("STUDENT")

                .requestMatchers(HttpMethod.PATCH, "/api/offers/*/expire")
                    .hasAnyRole("ADMIN", "PLACEMENT_OFFICER")

                .requestMatchers(HttpMethod.GET, "/api/offers")
                    .hasAnyRole("ADMIN", "PLACEMENT_OFFICER")

                .requestMatchers(HttpMethod.GET, "/api/offers/student/**")
                    .hasAnyRole("ADMIN", "PLACEMENT_OFFICER", "STUDENT")

                .requestMatchers(HttpMethod.GET, "/api/offers/**")
                    .hasAnyRole("ADMIN", "PLACEMENT_OFFICER", "STUDENT")

                // Remaining APIs require authentication
                .anyRequest().authenticated()
            )

            .authenticationProvider(authenticationProvider())

            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
