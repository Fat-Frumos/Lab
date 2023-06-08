package com.epam.esm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a token used for authentication and authorization.
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    @Id
    @GeneratedValue
    private Integer id;
    /**
     * The type of the token (e.g., Bearer).
     */
    @Enumerated(EnumType.STRING)
    private TokenType tokenType = TokenType.BEARER;
    /**
     * The actual token value.
     */
    @Column(unique = true)
    private String token;
    /**
     * The time-to-live (TTL) in milliseconds for the access token.
     */
    @Column(name = "access_token_ttl")
    private long accessTokenTTL;
    /**
     * Flag indicating if the token has been revoked.
     */
    private boolean revoked;
    /**
     * Flag indicating if the token has expired.
     */
    private boolean expired;
    /**
     * The user associated with the token.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
