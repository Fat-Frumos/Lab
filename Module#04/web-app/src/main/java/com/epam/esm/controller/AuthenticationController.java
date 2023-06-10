package com.epam.esm.controller;

import com.epam.esm.entity.User;
import com.epam.esm.security.auth.AuthenticationRequest;
import com.epam.esm.security.auth.AuthenticationResponse;
import com.epam.esm.security.auth.RegisterRequest;
import com.epam.esm.security.service.AuthenticationService;
import com.epam.esm.security.service.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for handling authentication-related requests.
 * <p>
 * This class is responsible for handling registration and authentication requests.
 */

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    /**
     * Endpoint for user registration.
     *
     * @param request The registration request containing user details.
     * @return ResponseEntity containing the authentication response.
     */
    @PostMapping("/token/register")
    public ResponseEntity<AuthenticationResponse> register(
            final @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    /**
     * Endpoint for user authentication.
     *
     * @param request The authentication request containing user credentials.
     * @return ResponseEntity containing the authentication response.
     */
    @PostMapping("/token/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            final @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<AuthenticationResponse> refreshTokens(
            final HttpServletRequest request,
            final HttpServletResponse response) {
        return ResponseEntity.ok(service.refresh(request, response));
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(
            final HttpServletRequest request,
            final HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, "access_token");
        CookieUtils.deleteCookie(request, response, "refresh_token");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(
            final HttpServletRequest request) {
        return CookieUtils.getCookie(request, "access_token")
                .map(token -> ResponseEntity.ok(
                        service.getUser(token.getValue())))
                .orElse(ResponseEntity.status(
                        HttpStatus.UNAUTHORIZED).build());
    }
}
