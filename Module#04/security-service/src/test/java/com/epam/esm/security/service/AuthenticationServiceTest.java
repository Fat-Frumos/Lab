package com.epam.esm.security.service;

import com.epam.esm.entity.Role;
import com.epam.esm.entity.RoleType;
import com.epam.esm.entity.Token;
import com.epam.esm.entity.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.security.auth.AuthenticationRequest;
import com.epam.esm.security.auth.AuthenticationResponse;
import com.epam.esm.security.auth.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthenticationServiceTest {
    HttpServletRequest request;
    @Mock
    PasswordEncoder encoder;
    @Mock
    AuthenticationManager manager;
    @Mock
    UserRepository userRepository;
    private final String username = "Bob";
    private User user;
    String email = "bob@i.ua";
    String password = "password";
    private final String token = "testRefreshToken";
    private String jwtToken;
    Long expired = 1000L;

    @BeforeEach
    void init() {
        request = new MockHttpServletRequest();
        user = User.builder()
                .username(username)
                .email(email)
                .password(password)
                .role(Role.builder().permission(RoleType.USER).build())
                .build();
    }

    @Test
    @DisplayName("Given a user, jwtToken, and accessToken, when getResponse is called, then return an AuthenticationResponse with the expected values")
    void testGetResponse() throws Exception {
        JwtTokenProvider provider = mock(JwtTokenProvider.class);
        when(provider.getExpiration()).thenReturn(expired);
        when(provider.generateRefreshToken(user)).thenReturn(token);
        AuthenticationService authService = new AuthenticationService(encoder, provider, manager, userRepository);
        Method getResponseMethod = AuthenticationService.class.getDeclaredMethod("getResponse", User.class, String.class, Long.class);
        getResponseMethod.setAccessible(true);
        AuthenticationResponse response = (AuthenticationResponse) getResponseMethod.invoke(authService, user, jwtToken, expired);
        assertEquals("Bob", response.getUsername());
        assertEquals(jwtToken, response.getAccessToken());
        assertEquals(token, response.getRefreshToken());
        assertNotNull(response.getExpiresAt());
    }

    @DisplayName("Given a RegisterRequest, when saveUserWithRole is called, then return a User with the expected values")
    @Test
    void testSaveUserWithRole() {
        RegisterRequest request = RegisterRequest.builder()
                .username(username)
                .password(password)
                .email(email)
                .build();
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        when(encoder.encode(password)).thenReturn("encodedPassword");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.save(argThat(user ->
                username.equals(user.getUsername()) &&
                        "encodedPassword".equals(user.getPassword()) &&
                        email.equals(user.getEmail()))))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AuthenticationService authService = new AuthenticationService(encoder, null, null, userRepository);
        User user = authService.saveUserWithRole(request);
        assertEquals(username, user.getUsername());
        assertEquals("encodedPassword", user.getPassword());
        assertEquals(email, user.getEmail());
    }

    @Test
    @DisplayName("Given an AuthenticationRequest, when authenticate is called, then return an AuthenticationResponse")
    void testAuthenticate() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername(username);
        request.setPassword(password);
        JwtTokenProvider provider = mock(JwtTokenProvider.class);
        when(provider.generateToken(any(User.class))).thenReturn(token);
        when(provider.getExpiration()).thenReturn(1000L);
        AuthenticationManager manager = mock(AuthenticationManager.class);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        AuthenticationService authService = new AuthenticationService(encoder, provider, manager, userRepository);
        AuthenticationResponse response = authService.authenticate(request);
        assertEquals(username, response.getUsername());
        assertEquals(token, response.getAccessToken());
    }

    @Test
    void testRegister() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername(username);
        request.setPassword("testPassword");
        request.setEmail("test@example.com");
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        when(encoder.encode("testPassword")).thenReturn("encodedPassword");
        JwtTokenProvider provider = mock(JwtTokenProvider.class);
        when(provider.generateToken(any(User.class))).thenReturn(token);
        Token tokens = new Token();
        tokens.setAccessTokenTTL(1000L);
        when(provider.updateUserTokens(any(User.class), eq(token))).thenReturn(tokens);
        AuthenticationManager manager = mock(AuthenticationManager.class);

        UserRepository userRepository = mock(UserRepository.class);
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        AuthenticationService authService = new AuthenticationService(encoder, provider, manager, userRepository);
        AuthenticationResponse response = authService.register(request);
        assertEquals(username, response.getUsername());
        assertEquals(token, response.getAccessToken());
    }

//    @Test
//    void testRefresh() {
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer testRefreshToken");
//        HttpServletResponse response = mock(HttpServletResponse.class);
//        JwtTokenProvider provider = mock(JwtTokenProvider.class);
//        when(provider.isBearerToken(request)).thenReturn(true);
//        when(provider.getUsername("testRefreshToken")).thenReturn("testUser");
//        when(provider.generateToken(any(User.class))).thenReturn("testAccessToken");
//        Token token = new Token();
//        token.setAccessToken("testAccessToken");
//        when(provider.updateUserTokens(any(User.class), eq("testAccessToken"))).thenReturn(token);
//        User user = new User();
//        user.setUsername("testUser");
//        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
//
//        AuthenticationService authService = new AuthenticationService(null, provider, null, userRepository);
//
//        AuthenticationResponse authResponse = authService.refresh(request, response);
//
//        assertEquals("testUser", authResponse.getUsername());
//        assertEquals("testAccessToken", authResponse.getAccessToken());
//        assertEquals("testRefreshToken", authResponse.getRefreshToken());
//    }

}