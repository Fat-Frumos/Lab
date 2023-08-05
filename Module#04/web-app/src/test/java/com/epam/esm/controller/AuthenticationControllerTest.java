//package com.epam.esm.controller;
//
//import com.epam.esm.repository.UserRepository;
//import com.epam.esm.security.auth.AuthenticationRequest;
//import com.epam.esm.security.auth.AuthenticationResponse;
//import com.epam.esm.security.auth.RegisterRequest;
//import com.epam.esm.security.service.AuthenticationService;
//import com.epam.esm.security.service.JwtTokenProvider;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.sql.Timestamp;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class AuthenticationControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private AuthenticationService service;
//
//    @MockBean
//    private JwtTokenProvider jwtTokenProvider;
//
//    @MockBean
//    private UserRepository userRepository;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @InjectMocks
//    private AuthenticationController authenticationController;
//    RegisterRequest mockRequest;
//    AuthenticationResponse mockResponse;
//
//    @BeforeEach
//    public void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
//        mockRequest = new RegisterRequest(
//                "username", "password", "email");
//        mockResponse = new AuthenticationResponse(
//                "username", "access_token", "refresh_token",
//                new Timestamp(System.currentTimeMillis()));
//    }
//
//    @Test
//    void testSignups() throws Exception {
//        when(service.signup(mockRequest)).thenReturn(mockResponse);
//        mockMvc.perform(post("/signup")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(mockRequest)))
//                .andExpect(status().is4xxClientError());
//    }
//
//    @Test
//    void testSignup() throws Exception {
//
//        when(service.signup(mockRequest)).thenReturn(mockResponse);
//
//        mockMvc.perform(post("/signup")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(mockRequest)))
//                .andExpect(status().is4xxClientError());
////                .andExpect(jsonPath("$.accessToken").value(mockResponse.getAccessToken()));
//    }
//
//    @Test
//    void testLogin() throws Exception {
//        AuthenticationRequest mockRequest = new AuthenticationRequest("testUser", "testPassword");
//        when(service.login(mockRequest)).thenReturn(mockResponse);
//        mockMvc.perform(post("/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(mockRequest)))
//                .andExpect(status().is4xxClientError());
////                .andExpect(jsonPath("$.accessToken").value(mockResponse.getAccessToken()));
//    }
//
//    private static String asJsonString(Object obj) {
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            return objectMapper.writeValueAsString(obj);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
