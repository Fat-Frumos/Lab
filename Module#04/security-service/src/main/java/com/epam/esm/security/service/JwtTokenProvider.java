package com.epam.esm.security.service;

import com.epam.esm.entity.Token;
import com.epam.esm.exception.TokenNotFoundException;
import com.epam.esm.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Service class that provides various JWT token operations.
 */
@Service
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final TokenRepository tokenRepository;
    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private long jwtExpiration;
    @Value("${jwt.refresh-token.expiration}")
    private long refreshExpiration;

    /**
     * Retrieves the jwtExpiration time
     *
     * @return The expiration time
     */
    public long getExpiration() {
        return jwtExpiration;
    }

    /**
     * Retrieves the username from the provided token.
     *
     * @param token The JWT token.
     * @return The username extracted from the token.
     * @throws TokenNotFoundException If the token is malformed or not found.
     */
    public String getUsername(
            final String token) {
        try {
            return getClaim(token, Claims::getSubject);
        } catch (MalformedJwtException e) {
            throw new TokenNotFoundException(
                    String.format("Token Not Found: %s", token));
        }
    }

    /**
     * Retrieves a specific claim from the provided token using the given function.
     *
     * @param token    The JWT token.
     * @param function The function to extract the desired claim from the token.
     * @param <T>      The type of the desired claim.
     * @return The extracted claim.
     * @throws TokenNotFoundException If the token is malformed or not found.
     */
    public <T> T getClaim(
            final String token,
            final Function<Claims, T> function) {
        try {
            return function.apply(
                    getAllClaims(token));
        } catch (MalformedJwtException e) {
            throw new TokenNotFoundException(
                    String.format("Token Not Found: %s", token));
        }
    }

    /**
     * Builds a refresh token for the specified user.
     *
     * @param userDetails The UserDetails object.
     * @return The generated refresh token.
     */
    public String buildRefreshToken(
            final UserDetails userDetails) {
        return buildToken(
                new HashMap<>(),
                userDetails,
                refreshExpiration);
    }

    /**
     * Builds a token for the provided UserDetails.
     *
     * @param userDetails The UserDetails object.
     * @return The generated token.
     */
    public String buildToken(
            final UserDetails userDetails) {
        return buildToken(new HashMap<>(),
                userDetails);
    }

    /**
     * Builds a token for the provided UserDetails with additional claims.
     *
     * @param claims      Additional claims to include in the token.
     * @param userDetails The UserDetails object.
     * @return The generated token.
     */
    public String buildToken(
            final Map<String, Object> claims,
            final @NotNull UserDetails userDetails) {
        return buildToken(claims,
                userDetails,
                jwtExpiration);
    }

    /**
     * Builds a JWT token for the specified user with additional claims.
     *
     * @param claims      Additional claims to include in the token.
     * @param userDetails The user details.
     * @param expiration  The time expiration.
     * @return The generated JWT token.
     */
    private String buildToken(
            final Map<String, Object> claims,
            final UserDetails userDetails,
            final long expiration) {
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            throw new IllegalArgumentException("userDetails cannot be null");
        }
    }

    /**
     * Validates a JWT token against the specified user details.
     *
     * @param token       The JWT token to validate.
     * @param userDetails The user details to compare against.
     * @return {@code true} if the token is valid for the user,
     * {@code false} otherwise.
     */
    public boolean validatedToken(
            final String token,
            final UserDetails userDetails) {
        try {
            return getUsername(token)
                    .equals(userDetails.getUsername())
                    && !getClaim(token, Claims::getExpiration)
                    .before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Utility methods and repository operations related to JWT tokens.
     *
     * @param token The jwt token
     * @return The claims extracted from the token.
     */
    private Claims getAllClaims(
            final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Retrieves the signing key used for JWT token verification.
     *
     * @return The signing key.
     */
    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders
                .BASE64.decode(secretKey));
    }

    /**
     * Retrieves a token from the database based on the provided JWT.
     *
     * @param jwt The JWT token to search for.
     * @return An Optional containing the Token if found, or empty if not found.
     */
    @Transactional
    public Optional<Token> findByToken(
            final String jwt) {
        return tokenRepository.findByToken(jwt);
    }

    /**
     * Saves a token in the database.
     *
     * @param token The Token object to save.
     * @return The saved Token object.
     */
    @Transactional
    public Token save(Token token) {
        return tokenRepository.save(token);
    }
}
