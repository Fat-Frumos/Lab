//package com.epam.esm.controller.auth;
//
//import com.epam.esm.entity.Role;
//import com.epam.esm.entity.RoleType;
//import com.epam.esm.entity.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mock.web.MockHttpSession;
//import org.springframework.security.authentication.TestingAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
//import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
//import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//
//@AutoConfigureMockMvc
//@ActiveProfiles(value = "dev")
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class MaximumSessionsTests {
//    @Autowired
//    private MockMvc mvc;
//    MvcResult mvcResult;
//    private final String admin = "ROLE_ADMIN";
//
//    @BeforeEach
//    void setup() throws Exception {
//        mvcResult = this.mvc.perform(formLogin())
//                .andExpect(authenticated())
//                .andReturn();
//    }
//    @Test
//    void testWithMockAuthentication() {
//        Authentication authentication = new TestingAuthenticationToken("alice", "alice", "ROLE_ADMIN");
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        SecurityContextHolder.clearContext();
//    }
//
//    @Test
//    void loginOnSecondLoginThenFirstSessionTerminated() throws Exception {
//        MockHttpSession firstLoginSession = (MockHttpSession) mvcResult.getRequest().getSession();
//        this.mvc.perform(get("/login").session(firstLoginSession)
//                        .with(jwt().authorities(new SimpleGrantedAuthority(admin))))
//                .andExpect(authenticated());
//        this.mvc.perform(formLogin()).andExpect(authenticated());
//        this.mvc.perform(get("/login").session(firstLoginSession))
//                .andExpect(unauthenticated());
//    }
//
//    @Test
//    void loginOnSecondLoginThenPreventLogin() throws Exception {
//        MockHttpSession firstLoginSession = (MockHttpSession) mvcResult.getRequest().getSession();
//        this.mvc.perform(get("/login").session(firstLoginSession))
//                .andExpect(authenticated());
//        this.mvc.perform(formLogin()).andExpect(unauthenticated());
//        this.mvc.perform(get("/login").session(firstLoginSession))
//                .andExpect(authenticated());
//    }
//}
