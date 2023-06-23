package com.epam.esm.security.service;

import com.epam.esm.entity.User;
import com.epam.esm.security.auth.AuthenticationRequest;
import com.epam.esm.security.auth.AuthenticationResponse;
import com.epam.esm.security.auth.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AuthService {
    @Transactional
    AuthenticationResponse signup(
            RegisterRequest request);

    @Transactional
    AuthenticationResponse authenticate(
            AuthenticationRequest request);

    @Transactional
    AuthenticationResponse refresh(
            String authorizationHeader,
            HttpServletResponse response);

    void logout(
            HttpServletRequest request,
            HttpServletResponse response);

    @Transactional
    User findUser(String username);

    @Transactional
    User saveUserWithRole(
            RegisterRequest request);

    @Transactional
    Optional<User> findByUsername(
            String username);
}
