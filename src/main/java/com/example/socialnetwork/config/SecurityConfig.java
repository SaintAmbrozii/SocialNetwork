package com.example.socialnetwork.config;


import com.example.socialnetwork.security.oauth.*;
import com.example.socialnetwork.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true,securedEnabled = true)
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final Oauth2AuthentificationSucessHandler oAuth2AuthenticationSuccessHandler;
    private final Oauth2AuthentificationFailureHandler oAuth2AuthenticationFailureHandler;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final LogoutService logoutService;

    public SecurityConfig(TokenProvider tokenProvider, CustomUserDetailsService customUserDetailsService,
                          Oauth2AuthentificationSucessHandler oAuth2AuthenticationSuccessHandler,
                          Oauth2AuthentificationFailureHandler oAuth2AuthenticationFailureHandler,
                          HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository, @Lazy LogoutService logoutService) {
        this.tokenProvider = tokenProvider;
        this.customUserDetailsService = customUserDetailsService;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
        this.logoutService = logoutService;
    }

    @Bean
    public TokenAuthentificationFilter tokenAuthenticationFilter() {
        TokenAuthentificationFilter tokenAuthenticationFilter = new TokenAuthentificationFilter();
        tokenAuthenticationFilter.setTokenProvider(tokenProvider);
        return tokenAuthenticationFilter;
    }



    @Bean
    public AuthenticationManager authenticationManager(
            final AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity httpSecurity)
            throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement
                                .sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS
                                )
                )
                .exceptionHandling(configurer ->
                        configurer.authenticationEntryPoint(
                                        (request, response, exception) -> {
                                            response.setStatus(
                                                    HttpStatus.UNAUTHORIZED
                                                            .value()
                                            );
                                            response.getWriter()
                                                    .write("Unauthorized.");
                                        })
                                .accessDeniedHandler(
                                        (request, response, exception) -> {
                                            response.setStatus(
                                                    HttpStatus.FORBIDDEN
                                                            .value()
                                            );
                                            response.getWriter()
                                                    .write("Unauthorized.");
                                        }))
                .authorizeHttpRequests(configurer ->
                        configurer.requestMatchers("/api/auth/**")
                                .permitAll()
                                .requestMatchers("/oauth2/**")
                                .permitAll()
                                .requestMatchers("/swagger-ui/**")
                                .permitAll()
                                .requestMatchers("/v3/api-docs/**")
                                .permitAll()
                                .requestMatchers("/ws/**")
                                .permitAll()
                                .requestMatchers("/api/users/**").hasAuthority("ROLE_USER")
                                .requestMatchers("/api/posts/**").hasAuthority("ROLE_USER")
                                .requestMatchers("/api/comments/**").hasAuthority("ROLE_USER")
                                .anyRequest().authenticated())
                .oauth2Login(httpSecurityOAuth2LoginConfigurer -> httpSecurityOAuth2LoginConfigurer.
                        authorizationEndpoint(authorizationEndpointConfig ->
                                authorizationEndpointConfig.baseUri("/oauth2/authorize")
                                        .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository))
                                .redirectionEndpoint(redirectionEndpointConfig -> redirectionEndpointConfig.baseUri("/oauth2/callback/*"))
                                .successHandler(oAuth2AuthenticationSuccessHandler)
                                .failureHandler(oAuth2AuthenticationFailureHandler)
                        )
                .logout(logout->logout.addLogoutHandler(logoutService)
                        .logoutUrl("api/auth/logout")
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()))
                .anonymous(AbstractHttpConfigurer::disable)
                .addFilterBefore(tokenAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
