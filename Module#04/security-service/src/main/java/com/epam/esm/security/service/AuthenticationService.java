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
    private static final String FORMAT = "%s%n%s";


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
                .orElseGet(() -> saveUser(request));
        String jwtToken = provider.generateToken(user);
        Token token = provider.updateUserTokens(user, jwtToken);
        log.info(format(FORMAT, token, jwtToken));
        return AuthenticationResponse.builder()
                .username(user.getUsername())
                .expiresAt(Timestamp.from(now().plusMillis(token.getAccessTokenTTL())))
                .refreshToken(provider.generateRefreshToken(user))
                .accessToken(jwtToken)
                .build();
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
        User user = findUser(request);
        String jwtToken = provider.generateToken(user);
        log.info(format(FORMAT, user, jwtToken));
        provider.updateUserTokens(user, jwtToken);
        return AuthenticationResponse.builder()
                .expiresAt(Timestamp.from(now().plusMillis(
                        provider.getExpiration())))
                .username(user.getUsername())
                .refreshToken(provider.generateRefreshToken(user))
                .accessToken(jwtToken)
                .build();
    }

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
                    log.info(format(FORMAT, user, token));
                    CookieUtils.addCookie(response, "access_token", accessToken, (int) provider.getExpiration());
                    CookieUtils.addCookie(response, "refresh_token", refreshToken, (int) provider.getExpiration());
                    return AuthenticationResponse.builder()
                            .username(username)
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build();
                }
            }
        }
        throw new InvalidJwtAuthenticationException("Invalid Jwt Authentication");
    }

    @Transactional
    public User getUser(
            String tokenValue) {
        return findByUsername(provider
                .getUsername(tokenValue)).orElseThrow(() ->
                new UserNotFoundException("User not found"));
    }

    private void setAuthenticationToken(
            AuthenticationRequest request) {
        Authentication authenticate = manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));
        SecurityContextHolder.getContext()
                .setAuthentication(authenticate);
    }

    @Transactional
    public User findUser(AuthenticationRequest request) {
        return userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException(
                        format("User not found: %s",
                                request.getUsername())));
    }

    @Transactional
    public User saveUser(RegisterRequest request) {
        return userRepository.save(User.builder()
                .password(encoder.encode(request.getPassword()))
                .username(request.getUsername())
                .email(request.getEmail())
                .role(Role.builder().permission(USER).build())
                .build());
    }

    @Transactional
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
