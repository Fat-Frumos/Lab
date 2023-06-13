package com.epam.esm.controller;

import com.epam.esm.security.auth.AuthenticationRequest;
import com.epam.esm.security.auth.AuthenticationResponse;
import com.epam.esm.security.auth.RegisterRequest;
import com.epam.esm.security.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for handling authentication-related requests.
 * <p>
 * This class is responsible for handling registration and authentication requests.
 */

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/token")
public class AuthenticationController {
    private final AuthenticationService service;

    /**
     * Endpoint for user registration.
     *
     * @param request The registration request containing user details.
     * @return ResponseEntity containing the authentication response.
     */
    @PostMapping("/register")
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
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            final @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    /**
     * Refreshes the access token and returns a new AuthenticationResponse.
     *
     * @param request  The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @return ResponseEntity containing the refreshed AuthenticationResponse.
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshTokens(
            final HttpServletRequest request,
            final HttpServletResponse response) {
        return ResponseEntity.ok(service.refresh(request, response));
    }

    /**
     * Logs out the current user by invalidating the access token.
     *
     * @param request  The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @return ResponseEntity indicating a successful logout.
     */
    @PostMapping("/logout")
    public ResponseEntity<Object> logout(
            final HttpServletRequest request,
            final HttpServletResponse response) {
        service.logout(request, response);
        return ResponseEntity.ok().build();
    }
}
