package com.epam.esm.controller;

import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.security.auth.AuthenticationResponse;
import com.epam.esm.security.auth.RegisterRequest;
import com.epam.esm.security.service.JwtTokenProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@DirtiesContext
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "dev")
class AuthenticationControllerIntegrationTest {
    @PersistenceContext
    private EntityManager entityManager;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserRepository userRepository;
    Long id = 1101L;
    String username = "User";
    String email = "user@i.ua";
    String password = "1";
    String admin = "ROLE_ADMIN";
    String user = "ROLE_USER";
    User userDetails;

    @BeforeEach
    void clearDatabase() {
        userDetails = User.builder().id(id).role(Role.USER).email(email).password(password).username(username).build();
        entityManager.createQuery("DELETE FROM Order").executeUpdate();
        entityManager.createQuery("DELETE FROM User").executeUpdate();
    }

    @Test
    @DisplayName("User without scopes should be forbidden")
    void userWhenNoScopesThenForbidden() throws Exception {
        mockMvc.perform(get("/users")
                        .with(jwt()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("User with wrong scope should be forbidden")
    void userWhenWrongScopeThenForbidden() throws Exception {
        mockMvc.perform(get("/users")
                        .with(jwt().jwt(t -> t.claim("user_id", this.id.toString())
                                        .claim("scope", user))
                                .authorities(new SimpleGrantedAuthority("ROLE_MANAGER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("User with read scope should be authorized")
    void userWhenReadScopeThenAuthorized() throws Exception {
        mockMvc.perform(get("/users")
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority(user))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("User with read scope should be forbidden to access specific user")
    void userWhenReadScopeUserThenForbidden() throws Exception {
        mockMvc.perform(get("/users/1")
                        .with(jwt().authorities(new SimpleGrantedAuthority(user))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("User with read scope and admin role should be authorized to access specific user")
    void userWhenReadScopeAdminThenAuthorized() throws Exception {
        mockMvc.perform(get("/users/1")
                        .with(jwt().authorities(new SimpleGrantedAuthority(admin))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("Quartez@i.ua"))
                .andExpect(jsonPath("$.username").value("alice"));
    }

    @Test
    @DisplayName("Given admin user, when accessing /users, then should be able to get all users")
    void givenAdminUserWhenGetAllByThenAllUsersReturned() throws Exception {
        User saved = userRepository.save(userDetails);
        mockMvc.perform(get("/users")
                        .with(jwt().authorities(new SimpleGrantedAuthority(admin))))
                .andExpect(status().isOk());
        assertEquals(saved, userDetails);
    }

    @Test
    @DisplayName("Given valid registration request, when registering a new user, then return HTTP 200 Created")
    void givenValidRegistrationRequestWhenRegisteringNewUserThenReturnsCreated() throws Exception {
        RegisterRequest request = new RegisterRequest(username, email, password);
        MvcResult result = mockMvc.perform(post("/token/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();
        String responseString = result.getResponse().getContentAsString();
        AuthenticationResponse response = objectMapper.readValue(responseString, AuthenticationResponse.class);
        String token = response.getToken();
        String subject = jwtTokenProvider.getUsername(token);
        assertEquals(username, subject);
    }

//    @Test
//    @DisplayName("Given valid authentication request, when authenticating a user, then return HTTP 200 OK with token")
//    void givenValidAuthenticationRequestWhenAuthenticatingUserThenReturnsToken() throws Exception {
//        AuthenticationRequest request = new AuthenticationRequest(username, password);
//
//        MvcResult result = mockMvc.perform(post("/token/authenticate")
//                        .with(jwt().authorities(new SimpleGrantedAuthority(admin)))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String responseString = result.getResponse().getContentAsString();
//        AuthenticationResponse response = objectMapper.readValue(responseString, AuthenticationResponse.class);
//        String token = response.getToken();
//        String subject = jwtTokenProvider.getUsername(token);
//        assertEquals(username, subject);
//    }
}
