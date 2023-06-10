package com.epam.esm.controller.auth;

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
import org.springframework.transaction.annotation.Transactional;

import static com.epam.esm.controller.auth.AuthenticationControllerIntegrationTest.withAuthorities;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@DirtiesContext
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "dev")
class GuestPermissionIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    private final Long id = 1100L;
    private final String userRead = "USER_READ";
    private final String guestRead = "GUEST_READ";
    private final String guestContent = "{\"username\":\"user\",\"password\":\"password\"}";

    @Test
    @DisplayName("User with wrong scope should be forbidden")
    void userWhenWrongScopeThenForbidden() throws Exception {
        mockMvc.perform(get("/users")
                        .with(jwt().jwt(t -> t.claim("user_id", this.id.toString())
                                        .claim("scope", userRead))
                                .authorities(new SimpleGrantedAuthority(guestRead))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Given guest user with GUEST_READ permission, when accessing /api/orders, then return HTTP 403 Forbidden")
    void guestWhenHasGuestReadPermissionThenForbidden() throws Exception {
        mockMvc.perform(withAuthorities(get("/api/orders"), guestRead))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Guest user should be forbidden from accessing /api/orders")
    void guestWhenAccessOrdersThenForbidden() throws Exception {
        mockMvc.perform(get("/api/orders")
                        .with(jwt().authorities(new SimpleGrantedAuthority(guestRead))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Guest user should be forbidden from accessing /api/certificates")
    void guestWhenAccessCertificatesThenForbidden() throws Exception {
        mockMvc.perform(get("/api/certificates")
                        .with(jwt().authorities(new SimpleGrantedAuthority(guestRead))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Guest user should be forbidden from accessing /api/tags")
    void guestWhenAccessTagsThenForbidden() throws Exception {
        mockMvc.perform(get("/api/tags")
                        .with(jwt().authorities(new SimpleGrantedAuthority(guestRead))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Guest user should be forbidden from accessing /api/users")
    void guestWhenAccessUsersThenForbidden() throws Exception {
        mockMvc.perform(get("/api/users")
                        .with(jwt().authorities(new SimpleGrantedAuthority(guestRead))))
                .andExpect(status().isForbidden());
    }

//    @Test
//    @DisplayName("Given guest user with GUEST_READ permission, when accessing /api/certificates, then return HTTP 200 OK")
//    void guestWhenHasGuestReadPermissionThenCanAccessCertificates() throws Exception {
//        mockMvc.perform(withAuthorities(get("/api/certificates"), guestRead))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("Guest user should be able to authenticate")
//    void guestWhenAuthenticateThenAllowed() throws Exception {
//        mockMvc.perform(withAuthorities(post("/api/token/authenticate")
//                        .content(guestContent)
//                        .contentType(MediaType.APPLICATION_JSON), guestRead))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("Given valid registration request, when registering a new user as guest user, then return HTTP 200 OK")
//    void guestWhenRegisterThenAllowed() throws Exception {
//        mockMvc.perform(withAuthorities(post("/api/token/register")
//                        .content(guestContent)
//                        .contentType(MediaType.APPLICATION_JSON), guestRead))
//                .andExpect(status().isOk());
//    }
}
