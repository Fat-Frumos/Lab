package com.epam.esm.security.service;

import com.epam.esm.entity.SecurityUser;
import com.epam.esm.entity.User;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.security.auth.AuthenticationResponse;
import com.epam.esm.security.auth.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @InjectMocks
    private AuthenticationService service;
    @Mock
    private UserRepository userRepository;

    RegisterRequest mockRequest;
    User mockUser;
    AuthenticationResponse mockResponse;
    SecurityUser securityUser;

    @BeforeEach
    public void setUp() {
        mockRequest = mock(RegisterRequest.class);
        mockUser = User.builder().username("test").build();
        mockResponse = mock(AuthenticationResponse.class);
        securityUser = SecurityUser.builder().user(mockUser).build();
    }

    @Test
    @DisplayName("Given a valid username, when testFindUser is called, it should return an User")
    void testFindUser() {
        String username = "test";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(securityUser.getUser()));
        SecurityUser securityUser = service.findUser(username);
        assertEquals(securityUser.getUser().getUsername(), username);
    }

    @Test
    void testFindUserNotFound() {
        String username = "nonExistentUser";
        assertThrows(UserNotFoundException.class,
                () -> service.findUser(username));
    }
}
