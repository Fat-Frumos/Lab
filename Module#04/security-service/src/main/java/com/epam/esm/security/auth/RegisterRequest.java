package com.epam.esm.security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The RegisterRequest class represents the request payload for user registration.
 * It contains the user's username, email, and password.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    /**
     * The username for registration.
     */
    private String username;

    /**
     * The email for registration.
     */
    private String email;

    /**
     * The password for registration.
     */
    private String password;
}
