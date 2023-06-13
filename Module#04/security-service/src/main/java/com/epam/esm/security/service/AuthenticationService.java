package com.epam.esm.security.service;

import com.epam.esm.entity.Role;
import com.epam.esm.entity.Token;
import com.epam.esm.entity.User;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.security.auth.AuthenticationRequest;
import com.epam.esm.security.auth.AuthenticationResponse;
import com.epam.esm.security.auth.RegisterRequest;
import com.epam.esm.security.exception.InvalidJwtAuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Optional;

import static com.epam.esm.entity.RoleType.USER;
import static java.lang.String.format;
import static java.time.Instant.now;

/**
 * Service class that handles user authentication and registration.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class AuthenticationService {
    private final PasswordEncoder encoder;
    private final JwtTokenProvider provider;
    private final AuthenticationManager manager;
    private final UserRepository userRepository;

    /**
     * Registers a new user with the provided registration request.
     * <p>
     * Generate a token for the saved user and create a Token object
     *
     * @param request The registration request containing user details.
     * @return The authentication response containing the generated token.
     */
    @Transactional
    public AuthenticationResponse register(
            final RegisterRequest request) {
        User user = findByUsername(request.getUsername())
                .orElseGet(() -> saveUserWithRole(request));
        String jwtToken = provider.generateToken(user);
        Token token = provider.updateUserTokens(user, jwtToken);
        return getResponse(user, jwtToken, token.getAccessTokenTTL());
    }

    /**
     * Authenticates a user with the provided credentials.
     * <p>
     * Find the user in the database based on the username
     *
     * @param request The authentication request containing the username and password.
     * @return An AuthenticationResponse object containing the generated token.
     * @throws UserNotFoundException If the user is not found in the database.
     */
    @Transactional
    public AuthenticationResponse authenticate(
            final AuthenticationRequest request) {
        setAuthenticationToken(request);
        User user = findUser(request.getUsername());
        String jwtToken = provider.generateToken(user);
        provider.revokeAllUserTokens(user);
        provider.updateUserTokens(user, jwtToken);
        return getResponse(user, jwtToken,
                provider.getExpiration());
    }

    /**
     * Refreshes the access token for the current user.
     *
     * @param request  The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @return AuthenticationResponse containing the refreshed access token.
     */
    @Transactional
    public AuthenticationResponse refresh(
            final HttpServletRequest request,
            final HttpServletResponse response) {
        if (provider.isBearerToken(request)) {
            String refreshToken = request
                    .getHeader(HttpHeaders.AUTHORIZATION)
                    .substring(7);
            String username = provider.getUsername(refreshToken);
            if (username != null) {
                User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UserNotFoundException(
                                format("User not found :%s", username)));
                if (provider.isTokenValid(refreshToken, user)) {
                    String accessToken = provider.generateToken(user);
                    Token token = provider.updateUserTokens(user, accessToken);
                    return AuthenticationResponse.builder()
                            .username(username)
                            .accessToken(token.getAccessToken())
                            .refreshToken(refreshToken)
                            .expiresAt(new Timestamp(token.getAccessTokenTTL()))
                            .build();
                }
            }
        }
        throw new InvalidJwtAuthenticationException("Invalid Jwt Authentication");
    }

    /**
     * Logs out the current user by invalidating the access token and clearing any associated session data.
     *
     * @param request  The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     */
    public void logout(
            final HttpServletRequest request,
            final HttpServletResponse response) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            Token token = provider.findByToken(jwt).orElse(null);
            if (token != null) {
                token.setExpired(true);
                token.setRevoked(true);
                provider.save(token);
            }
        }
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            response.getWriter().write("Logout successful");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Sets the authentication token for the provided username and password.
     *
     * @param request The AuthenticationRequest containing the username and password.
     */
    private void setAuthenticationToken(
            final AuthenticationRequest request) {
        Authentication authenticate = manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));
        SecurityContextHolder.getContext()
                .setAuthentication(authenticate);
    }

    /**
     * Retrieves the user with the given username.
     *
     * @param username The username of the user to find.
     * @return The User object.
     * @throws UserNotFoundException if the user is not found.
     */
    @Transactional
    public User findUser(final String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(
                        format("User not found: %s", username)));
    }

    /**
     * Saves a new user with the provided registration details and default role.
     *
     * @param request The RegisterRequest containing the user registration details.
     * @return The saved User object.
     */
    @Transactional
    public User saveUserWithRole(
            final RegisterRequest request) {
        User user = User.builder()
                .password(encoder.encode(request.getPassword()))
                .username(request.getUsername())
                .email(request.getEmail())
                .role(getRole())
                .build();
        return userRepository.save(user);
    }

    /**
     * Finds the user with the given username.
     *
     * @param username The username of the user to find.
     * @return Optional containing the User object, or empty if not found.
     */
    @Transactional
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Returns the Role object representing the default role for a user.
     *
     * @return The default Role object.
     */
    private static Role getRole() {
        return Role.builder()
                .permission(USER)
                .build();
    }

    /**
     * Constructs the AuthenticationResponse object with the user details, JWT token, and access token expiration.
     *
     * @param user        The User object.
     * @param jwtToken    The JWT token.
     * @param accessToken The access token expiration time.
     * @return The AuthenticationResponse object.
     */
    private AuthenticationResponse getResponse(
            User user, String jwtToken, Long accessToken) {
        return AuthenticationResponse.builder()
                .username(user.getUsername())
                .expiresAt(Timestamp.from(now()
                        .plusMillis(accessToken)))
                .refreshToken(provider.generateRefreshToken(user))
                .accessToken(jwtToken)
                .build();
    }
}
