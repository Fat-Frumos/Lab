package com.epam.esm.controller;

import com.epam.esm.security.auth.AuthenticationRequest;
import com.epam.esm.security.auth.AuthenticationResponse;
import com.epam.esm.security.auth.AuthenticationService;
import com.epam.esm.security.auth.RegisterRequest;
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
}
