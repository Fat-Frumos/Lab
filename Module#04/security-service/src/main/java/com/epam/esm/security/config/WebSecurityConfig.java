package com.epam.esm.security.config;

import com.epam.esm.security.filter.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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
@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtAuthorizationFilter authorizationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    /**
     * Configures the security filter chain for HTTP requests.
     *
     * @param http The HttpSecurity object to configure.
     * @return The security filter chain.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(
            final HttpSecurity http) throws Exception {

        return http
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and().headers().frameOptions().sameOrigin()
                .and().authorizeHttpRequests(authorize ->
                        authorize.requestMatchers(PathRequest.toH2Console()).permitAll()
                                .requestMatchers(POST, "/signup", "/logout", "/login", "/token/**").permitAll()
                                .requestMatchers(GET, "/certificates/**").permitAll()
                                .requestMatchers("/tags/**", "/orders/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                                .requestMatchers(POST, "/orders/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
//                                .requestMatchers( "/orders/**","/tags/**").hasAnyRole(USER.name(), ADMIN.name())
//                                .requestMatchers(GET, "/orders/**","/tags/**").hasAnyRole(USER.name())
                                .requestMatchers("/**").hasAuthority("ROLE_ADMIN")
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
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:8080/api"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
