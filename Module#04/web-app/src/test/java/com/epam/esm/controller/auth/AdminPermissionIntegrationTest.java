package com.epam.esm.controller.auth;

import com.epam.esm.entity.Role;
import com.epam.esm.entity.RoleType;
import com.epam.esm.entity.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.security.auth.AuthenticationRequest;
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

import static com.epam.esm.controller.auth.AuthenticationControllerIntegrationTest.withAuthorities;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@DirtiesContext
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "dev")
class AdminPermissionIntegrationTest {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Long id = 1100L;
    private final String username = "Admin";
    private final String email = "admin@i.ua";
    private final String password = "1";
    private final String admin = "ROLE_ADMIN";
    private final String adminRead = "ADMIN_READ";
    private final String adminContent = "{\"username\":\"admin\",\"password\":\"password\"}";
    User userDetails;

    @BeforeEach
    void clearDatabase() {
        userDetails = User.builder().id(id).role(Role.builder().permission(RoleType.USER).build()).email(email).password(password).username(username).build();
        entityManager.createQuery("DELETE FROM Order").executeUpdate();
        entityManager.createQuery("DELETE FROM User").executeUpdate();
    }
//
//    @Test
//    @DisplayName("Administrator should be able to access /api/certificates")
//    void administratorWhenAccessCertificatesThenAllowed() throws Exception {
//        mockMvc.perform(get("/api/certificates")
//                        .with(jwt().authorities(new SimpleGrantedAuthority(adminRead))))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("Administrator should be able to access /api/tags")
//    void administratorWhenAccessTagsThenAllowed() throws Exception {
//        mockMvc.perform(get("/api/tags")
//                        .with(jwt().authorities(new SimpleGrantedAuthority(adminRead))))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("Administrator should be able to access /api/orders")
//    void administratorWhenAccessOrdersThenAllowed() throws Exception {
//        mockMvc.perform(get("/api/orders")
//                        .with(jwt().authorities(new SimpleGrantedAuthority(adminRead))))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("Given administrator with ADMIN_READ permission, when accessing /api/certificates, then return HTTP 200 OK")
//    void administratorWhenHasAdminReadPermissionThenCanAccessCertificates() throws Exception {
//        mockMvc.perform(withAuthorities(get("/api/certificates"), "ADMIN_READ"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("Given administrator with ADMIN_READ permission, when accessing /api/orders, then return HTTP 200 OK")
//    void administratorWhenHasAdminReadPermissionThenCanAccessOrders() throws Exception {
//        mockMvc.perform(withAuthorities(get("/api/orders"), "ADMIN_READ"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("Administrator should be able to access /api/users")
//    void administratorWhenAccessUsersThenAllowed() throws Exception {
//        mockMvc.perform(get("/api/users")
//                        .with(jwt().authorities(new SimpleGrantedAuthority(adminRead))))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("Given valid registration request, when registering a new user as administrator, then return HTTP 200 OK")
//    void administratorWhenRegisterThenAllowed() throws Exception {
//        mockMvc.perform(withAuthorities(post("/api/token/register")
//                        .content(adminContent)
//                        .contentType(MediaType.APPLICATION_JSON), adminRead))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("Administrator should be able to authenticate")
//    void administratorWhenAuthenticateThenAllowed() throws Exception {
//        mockMvc.perform(withAuthorities(post("/api/token/authenticate")
//                        .content(adminContent)
//                        .contentType(MediaType.APPLICATION_JSON), adminRead))
//                .andExpect(status().isOk());
//    }

    @Test
    @DisplayName("Given admin user, when accessing /api/users, then should be able to get all users")
    void givenAdminUserWhenGetAllByThenAllUsersReturned() throws Exception {
        mockMvc.perform(get("/api/users")
                        .with(jwt().authorities(new SimpleGrantedAuthority(admin))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Administrator should be able to perform all operations")
    void administratorWhenPerformAllOperationsThenAllowed() throws Exception {
        mockMvc.perform(post("/api/certificates/" + id)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("User with read scope and admin role should be authorized to access specific user")
    void userWhenReadScopeAdminThenAuthorized() throws Exception {
        mockMvc.perform(get("/api/users/1")
                        .with(jwt().authorities(new SimpleGrantedAuthority(admin))))
                .andExpect(status().isOk()); //todo tokens
    }

    @Test
    @DisplayName("Given valid authentication request, when authenticating a user, then return HTTP 200 OK with token")
    void givenValidAuthenticationRequestWhenAuthenticatingUserThenReturnsToken() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest(username, password);

        MvcResult result = mockMvc.perform(post("/api/token/authenticate")
                        .with(jwt().authorities(new SimpleGrantedAuthority(admin)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        assertNotNull(responseString);
    }
}
