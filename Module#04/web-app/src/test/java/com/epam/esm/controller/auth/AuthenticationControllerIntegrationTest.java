package com.epam.esm.controller.auth;

import com.epam.esm.entity.Role;
import com.epam.esm.entity.RoleType;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.stream.Collectors;

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

    private final Long id = 1100L;
    private final String username = "User";
    private final String email = "user@i.ua";
    private final String password = "1";
    private final String userRead = "USER_READ";
    private final String userContent = "{\"username\":\"user\",\"password\":\"password\"}";
    User userDetails;

    @BeforeEach
    void clearDatabase() {
        userDetails = User.builder().id(id).role(Role.builder().permission(RoleType.USER).build()).email(email).password(password).username(username).build();
        entityManager.createQuery("DELETE FROM Order").executeUpdate();
        entityManager.createQuery("DELETE FROM User").executeUpdate();
    }

    public static MockHttpServletRequestBuilder withAuthorities(MockHttpServletRequestBuilder builder, String... authorities) {
        return builder.with(jwt().authorities(Arrays.stream(authorities)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList())));
    }

//    @Test
//    @DisplayName("Given anonymous user, when accessing /api/certificates, then return HTTP 403 Forbidden")
//    void anonymousWhenAccessCertificatesThenForbidden() throws Exception {
//        mockMvc.perform(get("/api/certificates"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @DisplayName("Given anonymous user, when accessing /api/orders, then return HTTP 403 Forbidden")
//    void anonymousWhenAccessOrdersThenForbidden() throws Exception {
//        mockMvc.perform(get("/api/orders"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @DisplayName("Anonymous user should be forbidden from accessing /api/tags")
//    void anonymousWhenAccessTagsThenForbidden() throws Exception {
//        mockMvc.perform(get("/api/tags"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @DisplayName("Anonymous user should be forbidden from accessing /api/users")
//    void anonymousWhenAccessUsersThenForbidden() throws Exception {
//        mockMvc.perform(get("/api/users"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @DisplayName("Given user with USER_READ permission, when accessing /api/certificates, then return HTTP 200 OK")
//    void userWhenHasUserReadPermissionThenCanAccessCertificates() throws Exception {
//        mockMvc.perform(withAuthorities(get("/api/certificates"), "USER_READ"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("Given user with USER_READ permission, when accessing /api/orders, then return HTTP 200 OK")
//    void userWhenHasUserReadPermissionThenCanAccessOrders() throws Exception {
//        mockMvc.perform(withAuthorities(get("/api/orders"), userRead))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("User with USER_CREATE permission should be able to create an order")
//    void userWhenHasUserCreatePermissionThenCanCreateOrder() throws Exception {
//        mockMvc.perform(post("/api/orders/{userId}", this.id)
//                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("User should be able to make an order on main entity")
//    void userWhenMakeOrderOnMainEntityThenAllowed() throws Exception {
//        mockMvc.perform(post("/api/certificates/1/orders") //TODO
//                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("User should be able to read all")
//    void userWhenReadAllThenAllowed() throws Exception {
//        mockMvc.perform(get("/api/certificates")
//                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("User with USER_READ permission should be able to access /api/tags")
//    void userWhenHasUserReadPermissionThenCanAccessTags() throws Exception {
//        mockMvc.perform(get("/api/tags")
//                        .with(jwt().authorities(new SimpleGrantedAuthority(userRead))))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("Given valid registration request, when registering a new user, then return HTTP 200 Created")
//    void givenValidRegistrationRequestWhenRegisteringNewUserThenReturnsCreated() throws Exception {
//        RegisterRequest request = new RegisterRequest(username, email, password);
//        MvcResult result = mockMvc.perform(post("/api/token/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.access_token").exists())
//                .andExpect(jsonPath("$.refresh_token").exists())
//                .andExpect(jsonPath("$.username").value(username))
//                .andExpect(jsonPath("$.expires_at").exists())
//                .andReturn();
//        String responseString = result.getResponse().getContentAsString();
//        AuthenticationResponse response = objectMapper.readValue(responseString, AuthenticationResponse.class);
//        String token = response.getAccessToken();
//        String subject = jwtTokenProvider.getUsername(token);
//        assertEquals(username, subject);
//    }
//
//    @Test
//    @DisplayName("Anonymous user should be able to authenticate")
//    void anonymousWhenAuthenticateThenAllowed() throws Exception {
//        mockMvc.perform(withAuthorities(post("/api/token/authenticate")
//                        .content(userContent)
//                        .contentType(MediaType.APPLICATION_JSON)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("User should be able to authenticate")
//    void userWhenAuthenticateThenAllowed() throws Exception {
//        mockMvc.perform(withAuthorities(post("/api/token/authenticate")
//                        .content(userContent)
//                        .contentType(MediaType.APPLICATION_JSON), userRead))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("Given valid registration request, when registering a new user as anonymous user, then return HTTP 200 OK")
//    void anonymousWhenRegisterThenAllowed() throws Exception {
//        mockMvc.perform(post("/api/token/register")
//                        .content(userContent)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("Given valid registration request, when registering a new user as user, then return HTTP 200 OK")
//    void userWhenRegisterThenAllowed() throws Exception {
//        mockMvc.perform(withAuthorities(post("/api/token/register")
//                        .content(userContent)
//                        .contentType(MediaType.APPLICATION_JSON), userRead))
//                .andExpect(status().isOk());
//    }

    @Test
    @DisplayName("User without scopes should be forbidden")
    void userWhenNoScopesThenForbidden() throws Exception {
        mockMvc.perform(get("/users")
                        .with(jwt()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Given user without USER_READ permission, when accessing /api/certificates, then return HTTP 403 Forbidden")
    void userWhenNoUserReadPermissionThenForbidden() throws Exception {
        mockMvc.perform(withAuthorities(get("/api/certificates")))
                .andExpect(status().isForbidden());
    }


    @Test
    @DisplayName("User with read scope should be authorized")
    void userWhenReadScopeThenAuthorized() throws Exception {
        mockMvc.perform(get("/api/users")
                        .with(jwt().authorities(new SimpleGrantedAuthority(userRead))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("User with read scope should be forbidden to access specific user")
    void userWhenReadScopeUserThenForbidden() throws Exception {
        mockMvc.perform(get("/api/users/1")
                        .with(jwt().authorities(new SimpleGrantedAuthority(userRead))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("User with USER_READ permission should not be able to access /api/users")
    void userWhenHasUserReadPermissionThenCanAccessUsers() throws Exception {
        mockMvc.perform(get("/api/users")
                        .with(jwt().authorities(new SimpleGrantedAuthority(userRead))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Given user with USER_READ permission, when accessing /api/orders, then return HTTP 403 Forbidden")
    void userWhenHasUserReadPermissionThenForbidden() throws Exception {
        mockMvc.perform(withAuthorities(get("/api/orders"), "USER_READ"))
                .andExpect(status().isForbidden());
    }
}
