package com.epam.esm.security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a response to an authentication request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
    /**
     * The username for authentication.
     */
    private String username;
    /**
     * The password for authentication.
     */
    private String password;
}
