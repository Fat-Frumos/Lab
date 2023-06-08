package com.epam.esm.security.auth;

import com.epam.esm.entity.Role;
import com.epam.esm.entity.Token;
import com.epam.esm.entity.User;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.security.service.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * Service class that handles user authentication and registration.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class AuthenticationService {
    private final PasswordEncoder encoder;
    private final AuthenticationManager manager;
    private final UserRepository userRepository;
    private final JwtTokenProvider provider;

    /**
     * Registers a new user with the provided registration request.
     * <p>
     * Generate a token for the saved user and create a Token object
     *
     * @param request The registration request containing user details.
     * @return The authentication response containing the generated token.
     */
    public AuthenticationResponse register(
            final RegisterRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(encoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        User saved = userRepository.save(user);
        log.info(saved.toString());
        Token token = Token.builder()
                .user(saved)
                .token(provider.buildToken(saved))
                .accessTokenTTL(provider.getExpiration())
                .revoked(false)
                .expired(false)
                .build();
        Token save = provider.save(token);
        log.info(save.toString());
        return AuthenticationResponse.builder()
                .token(token.getToken())
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
    public AuthenticationResponse authenticate(
            final AuthenticationRequest request) {
        manager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()));
        User user = userRepository.findByUsername(
                        request.getUsername())
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User not found: %s",
                                request.getUsername())));
        log.info(user.toString());
        return AuthenticationResponse.builder()
                .token(provider.buildToken(user))
                .build();
    }
}
