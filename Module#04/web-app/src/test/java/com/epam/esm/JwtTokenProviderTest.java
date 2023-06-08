package com.epam.esm;

import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.exception.TokenNotFoundException;
import com.epam.esm.security.service.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest()
@Import(JwtTokenProvider.class)
class JwtTokenProviderTest {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    private final String username = "Bob";
    private UserDetails userDetails;
    private final String invalidToken = "invalidToken";
    private String token;

    @BeforeEach
    void init() {
        userDetails = User.builder()
                .username(username)
                .password("password")
                .role(Role.USER)
                .build();
        token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)),
                        SignatureAlgorithm.HS256)
                .compact();
    }

    @Test
    @DisplayName("Given a valid UserDetails object with a scope, when buildToken is called, then return a valid JWT token with the scope")
    void givenValidUserDetailsWithScopeWhenBuildTokenThenReturnValidTokenWithScope() {
        String jwt = jwtTokenProvider.buildToken(userDetails);
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                .build()
                .parseClaimsJws(jwt);
        Assertions.assertEquals(username, claims.getBody().getSubject());
        Assertions.assertTrue(claims.getBody().getExpiration().after(new Date()));
    }

    @Test
    @DisplayName("Given a valid JWT token, when getUsername is called, then return the username from the token")
    void givenValidJwtTokenWhenGetUsernameThenReturnUsernameFromToken() {
        String result = jwtTokenProvider.getUsername(token);
        assertEquals(username, result);
    }

    @Test
    @DisplayName("Given a valid JWT token and a valid claim function, when getClaim is called, then return the claim from the token")
    void givenValidJwtTokenAndValidClaimFunctionWhenGetClaimThenReturnClaimFromToken() {
        String result = jwtTokenProvider.getClaim(token, Claims::getSubject);
        assertEquals(username, result);
    }

    @Test
    @DisplayName("Given a valid UserDetails object, when buildRefreshToken is called, then return a valid refresh JWT token")
    void givenValidUserWhenBuildRefreshTokenThenReturnValidRefreshToken() {
        String token = jwtTokenProvider.buildRefreshToken(userDetails);
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                .build()
                .parseClaimsJws(token);
        Assertions.assertEquals(username, claims.getBody().getSubject());
        Assertions.assertTrue(claims.getBody().getExpiration().after(new Date()));
    }

    @Test
    @DisplayName("Given a valid UserDetails object, when buildToken is called, then return a valid JWT token")
    void givenValidUserWhenBuildTokenThenReturnValidToken() {
        String jwt = jwtTokenProvider.buildToken(userDetails);
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                .build()
                .parseClaimsJws(jwt);
        Assertions.assertEquals(username, claims.getBody().getSubject());
        Assertions.assertTrue(claims.getBody().getExpiration().after(new Date()));
    }

    @Test
    @DisplayName("Given a valid UserDetails object and valid claims, when buildToken is called with claims and userDetails arguments, then return a valid JWT token with the claims")
    void givenValidUserAndValidClaimsWhenBuildTokenWithClaimsAndUserThenReturnValidTokenWithClaims() {
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("key", "value");
        String jwt = jwtTokenProvider.buildToken(claimsMap, userDetails);
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                .build()
                .parseClaimsJws(jwt);
        Assertions.assertEquals(username, claims.getBody().getSubject());
        Assertions.assertTrue(claims.getBody().getExpiration().after(new Date()));
        Assertions.assertEquals("value", claims.getBody().get("key"));
    }

    @Test
    @DisplayName("Given a valid JWT token and a valid UserDetails object, when validateToken is called, then return true")
    void validatedJwtTokenAndValidUserWhenValidateTokenThenReturnTrue() {
        boolean result = jwtTokenProvider.validatedToken(token, userDetails);
        assertTrue(result);
    }

    @Test
    @DisplayName("Given an invalid JWT token, when getUsername is called, then throw TokenNotFoundException")
    void validatedUsernameWithInvalidToken() {
        assertThrows(TokenNotFoundException.class, () ->
                jwtTokenProvider.getUsername(invalidToken));
    }

    @Test
    @DisplayName("Given an invalid JWT token and a valid claim function, when getClaim is called, then throw TokenNotFoundException")
    void validatedClaimWithInvalidToken() {
        assertThrows(TokenNotFoundException.class, () ->
                jwtTokenProvider.getClaim(invalidToken, Claims::getSubject));
    }

    @Test
    @DisplayName("Given an expired JWT token and a valid UserDetails object, when validatedToken is called, then return false")
    void validatedTokenWithExpiredToken() {
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() - jwtExpiration))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)),
                        SignatureAlgorithm.HS256)
                .compact();
        boolean result = jwtTokenProvider.validatedToken(token, userDetails);
        assertFalse(result);
    }

    @Test
    @DisplayName("Given a null UserDetails object, when buildToken is called, then throw IllegalArgumentException")
    void buildTokenWithNullUserDetails() {
        assertThrows(IllegalArgumentException.class, () -> jwtTokenProvider.buildToken(null));
    }

    @Test
    @DisplayName("Given a null UserDetails object and valid claims, when buildToken is called with claims and userDetails arguments, then throw IllegalArgumentException")
    void buildTokenWithNullUserDetailsAndValidClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("key", "value");
        assertThrows(IllegalArgumentException.class, () -> jwtTokenProvider.buildToken(claims, null));
    }

    @Test
    @DisplayName("Given a valid JWT token and a null UserDetails object, when validatedToken is called, then return false")
    void validatedTokenWithNullUserDetails() {
        assertFalse(jwtTokenProvider.validatedToken(token, null));
    }
    @Test
    @DisplayName("Given a valid UserDetails object and invalid claims, when buildToken is called with claims and userDetails arguments, then throw IllegalArgumentException")
    void buildTokenWithInvalidClaims() {
        assertThrows(IllegalArgumentException.class, () -> jwtTokenProvider.buildToken(null, userDetails));
    }
}
