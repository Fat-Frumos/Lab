package com.epam.esm.controller.auth;

import com.epam.esm.entity.Role;
import com.epam.esm.entity.RoleType;
import com.epam.esm.entity.User;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static com.epam.esm.controller.TestConfig.getCrudHttpRequests;
import static com.epam.esm.controller.TestConfig.withAuthorities;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@DirtiesContext
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "dev")
class AuthenticationControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    private final Long id = 1100L;
    private final String username = "bob";
    private final String email = "user@i.ua";
    private final String password = "bob";
    private final String userRead = "ROLE_USER";
    private final String userContent = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
    private final String unauthorized = "{\"statusCode\":\"UNAUTHORIZED\",\"errorMessage\":\"Unauthorized\"}";
    User userDetails;

    @BeforeEach
    void logoutBeforeTest() {
        SecurityContextHolder.clearContext();
        userDetails = User.builder().id(id).role(Role.builder().permission(RoleType.USER)
                .build()).email(email).password(password).username(username).build();
    }

    @Test
    @DisplayName("Given user, when accessing Http CUD certificates, then return 302 Redirect")
    void anonymousWhenAccessCertificatesThenRedirection() throws Exception {
        for (String host : Arrays.asList("/api/certificates", "/api/certificates/1")) { //todo 2 sub level certificates/1/order, certificates/1/user CRUD etc
            List<MockHttpServletRequestBuilder> httpRequests = getCrudHttpRequests(host);
            for (int i = 1; i < httpRequests.size(); i++) {
                MockHttpServletRequestBuilder mock = httpRequests.get(i);
                mockMvc.perform(mock.content(userContent))
                        .andExpect(status().isUnauthorized())
                        .andExpect(content().json(unauthorized));
            }
        }
    }

    @Test
    @DisplayName("Given user should be redirected from accessing Http CUD tags, then return 401 Unauthorized")
    void anonymousWhenAccessTagsThenUnauthorized() throws Exception {
        for (String host : Arrays.asList("/api/tags", "/api/tags/1")) {
            @NotNull List<MockHttpServletRequestBuilder> httpRequests = getCrudHttpRequests(host);
            for (int i = 1; i < httpRequests.size(); i++) {
                MockHttpServletRequestBuilder mock = httpRequests.get(i);
                mockMvc.perform(mock.content(userContent))
                        .andExpect(status().isUnauthorized())
                        .andExpect(content().json(unauthorized));
            }
        }
    }

    @Test
    @DisplayName("Given user, when accessing Http CRUD users, then return 302 Redirect")
    void anonymousWhenAccessOrderThenRedirection() throws Exception {
        for (String host : Arrays.asList("/api/users", "/api/users/1")) {
            for (MockHttpServletRequestBuilder mock : getCrudHttpRequests(host)) {
                mockMvc.perform(mock.content(userContent))
                        .andExpect(status().isUnauthorized())
                        .andExpect(content().json(unauthorized));
            }
        }
    }

    @Test
    @DisplayName("Anonymous user should be redirected from Http CRUD accessing orders")
    void anonymousWhenAccessUsersThenForbidden() throws Exception {  //todo 2 sub level order
        List<String> hosts = Arrays.asList("/api/orders", "/api/orders/1", "/api/orders/users/1", "/api/orders/1/users/1", "/api/orders/users/1/most");
        for (String host : hosts) {
            for (MockHttpServletRequestBuilder mock : getCrudHttpRequests(host)) {
                mockMvc.perform(mock.content(userContent))
                        .andExpect(status().isUnauthorized())
                        .andExpect(content().json(unauthorized));
            }
        }
    }

    @Test
    @DisplayName("User with read scope should be authorized, when accessing /api/users, then return HTTP 403 forbidden to access specific user and all users")
    void userWhenReadScopeUserThenForbidden() throws Exception {
        for (String host : Arrays.asList("/api/users", "/api/users/1")) {
            for (MockHttpServletRequestBuilder mock : getCrudHttpRequests(host)) {
                mockMvc.perform(withAuthorities(mock, userRead))
                        .andExpect(status().isForbidden())
                        .andReturn();
            }
        }
    }

    @Test
    @DisplayName("Given user with USER_READ permission, when accessing /api/orders, then return HTTP 403 Forbidden")
    void userWhenHasUserReadPermissionThenForbidden() throws Exception {
        for (String host : Arrays.asList("/api/orders", "/api/orders/2")) {
            for (MockHttpServletRequestBuilder mock : getCrudHttpRequests(host)) {
                mockMvc.perform(withAuthorities(mock, userRead))
                        .andExpect(status().isForbidden());
            }
        }
    }

//    @Test
//    @DisplayName("Given user with USER_READ permission, when accessing /api/certificates, then return HTTP 200 OK")
//    void userWhenHasUserReadPermissionThenCanAccessTag() throws Exception {
//        mockMvc.perform(withAuthorities(get("/api/certificates"), userRead))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("Given user with USER_READ permission, when accessing /api/tags, then return HTTP 200 OK")
//    void userWhenHasUserReadPermissionThenCanAccessCertificates() throws Exception {
//        for (String host : Arrays.asList("/api/tags", "/api/tags/1")) {
//            mockMvc.perform(withAuthorities(get(host), userRead))
//                    .andExpect(status().isOk());
//        }
//    }
//
//    @Test
//    @DisplayName("Given user with USER_READ permission, when accessing /api/orders, then return HTTP 200 OK")
//    void userWhenHasUserReadPermissionThenCanAccessOrders() throws Exception {
//        mockMvc.perform(withAuthorities(get("/api/orders"), userRead)) //TODO users orders
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
//        MvcResult result = mockMvc.perform(post("/api/signup")
//                        .with(jwt().authorities(new SimpleGrantedAuthority(userRead)))
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
//    @DisplayName("User should be able to authenticate")
//    void userWhenAuthenticateThenAllowed() throws Exception {
//        mockMvc.perform(withAuthorities(post("/api/token/authenticate")
//                        .content(userContent)
//                        .contentType(MediaType.APPLICATION_JSON), userRead))
//                .andExpect(status().isOk());
//    }
}
