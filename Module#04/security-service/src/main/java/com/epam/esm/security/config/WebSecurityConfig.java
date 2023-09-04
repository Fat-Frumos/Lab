package com.epam.esm.security.config;

import com.epam.esm.security.filter.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

/**
 * Configuration class for web security.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private static final String USER = "ROLE_USER";
    private static final String ADMIN = "ROLE_ADMIN";
    /**
     * Handler for logging out the user.
     */
    private final LogoutHandler logoutHandler;

    /**
     * Handler for handling access denied situations.
     */
    private final AccessDeniedHandler accessDeniedHandler;

    /**
     * Filter for JWT authorization.
     */
    private final JwtAuthorizationFilter authorizationFilter;

    /**
     * Provider for authentication.
     */
    private final AuthenticationProvider authenticationProvider;

    /**
     * Entry point for authentication.
     */
    private final AuthenticationEntryPoint authenticationEntryPoint;

    /**
     * Configures the security filter chain for HTTP requests.
     *
     * @param http The HttpSecurity object to configure.
     * @return The security filter chain.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            final HttpSecurity http) throws Exception {

        return http
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and().headers().frameOptions().sameOrigin()
                .and().authorizeHttpRequests(authorize ->
                        authorize.requestMatchers(PathRequest.toH2Console()).permitAll()
                                .requestMatchers(POST, "/signup", "/logout", "/login").permitAll()
                                .requestMatchers(GET, "/certificates/**", "/tags/**").permitAll()
                                .requestMatchers(GET, "/orders/**", "/token/**").hasAnyAuthority(USER, ADMIN)
                                .requestMatchers(POST, "/orders/**").hasAnyAuthority(USER, ADMIN)
                                .requestMatchers(POST, "/users/**").hasAnyAuthority(ADMIN)
                                .requestMatchers("/**").hasAuthority(ADMIN)
                                .anyRequest()
                                .authenticated())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authenticationProvider(authenticationProvider)
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .logout().logoutUrl("/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) ->
                        SecurityContextHolder.clearContext())
                .and().build();
    }

    /**
     * Configures the CORS (Cross-Origin Resource Sharing) configuration source.
     *
     * @return The CORS configuration source.
     */
    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://192.168.31.177:5500", "http://127.0.0.1:5500", "http://127.0.0.1:8080", "http://127.0.0.1:4200", "https://gift-store-certificate.netlify.app"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
