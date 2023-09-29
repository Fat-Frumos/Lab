package com.epam.esm.controller;

import com.epam.esm.entity.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "dev")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserPermissionTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    private MockMvc mockMvc;
    User entity;
    private final String giftFilePath = "gift.json";
    private final String userFilePath = "user.json";

    String[] arrayFields = {
            "_links.self.href",
            "_embedded.certificateDtoList[0].id",
            "_embedded.certificateDtoList[0].name",
            "_embedded.certificateDtoList[0].price",
            "_embedded.certificateDtoList[0].description"
    };
    String[] arrayUserFields = {
            "_embedded.userDtoList[0].id",
            "_embedded.userDtoList[0].username",
            "_embedded.userDtoList[0].email",
            "_embedded.userDtoList[0].orderDtos[0].id",
            "_embedded.userDtoList[0].orderDtos[0].user.id",
            "_embedded.userDtoList[0].orderDtos[0].user.username",
            "_embedded.userDtoList[0].orderDtos[0].user.email",
            "_embedded.userDtoList[0].orderDtos[0].user.password",
            "_embedded.userDtoList[0].orderDtos[0].user.role.id",
            "_embedded.userDtoList[0].orderDtos[0].user.role.permission",
            "_embedded.userDtoList[0]._links.self.href",
            "_embedded.userDtoList[0]._links.orders.href",
            "_embedded.userDtoList[0]._links.certificates.href"
    };

    @BeforeAll
    static void beforeAll() {
        System.out.println("before All " + TransactionSynchronizationManager.isActualTransactionActive());
    }

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        entity = User.builder().email("email@i.ua").password("password").username("User").build();
        System.out.println("before each " + TransactionSynchronizationManager.isActualTransactionActive());
    }

    @Test
    void returnCertificatesDetails() throws Exception {
        String url = "/certificates?size=1";
        String json = readFile(giftFilePath);
        Map<String, Object> fieldMap = new HashMap<>();
        Stream.of(arrayFields).forEach(field -> fieldMap.put(field, JsonPath.parse(json).read(field)));
        fieldMap.forEach((field, value) -> {
            try {
                this.mockMvc.perform(get(url))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath(field).value(String.valueOf(value)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void returnUsersDetails() throws Exception {
        String url = "/users?size=1";
        String json = readFile(userFilePath);
        Map<String, Object> fieldMap = new HashMap<>();
        Stream.of(arrayUserFields).forEach(field -> fieldMap.put(field, JsonPath.parse(json).read(field)));
        fieldMap.forEach((field, value) -> {
            try {
                this.mockMvc.perform(get(url).with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath(field).value(String.valueOf(value)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

//    @Test
//    void returnUserDetails() throws Exception {
//        String url = "/users/1";
//        mockMvc.perform(get(url)
//                        .with(user("alice"))
//                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse().getContentAsString();
//    }

    @RepeatedTest(2)
    void saveRetrieveUserAndFindByUsername() {
        userRepository.save(entity);
        Optional<User> user = userRepository.findByUsername("User");
        assertAll(
                () -> assertEquals(1100, user.get().getId()),
                () -> assertEquals("User", user.get().getUsername())
        );
    }

    private String readFile(String filePath) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(filePath)) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private void print(String url) throws Exception {
        String jsonResponse = this.mockMvc
                .perform(get(url)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andReturn()
                .getResponse().getContentAsString();
        System.out.println(jsonResponse);
    }
//    @RepeatedTest(2)
//    void saveAllRetrieveUserAndFindAll() {
//        userRepository.saveAll(Collections.singleton(entity));
//
//        List<User> user = userRepository.findAll();
//        UserDto save = userService.update(UserSlimDto.builder().build());
//
////        assertAll(
////                () -> assertEquals(1100, user.get().getId()),
////                () -> assertEquals("User", user.get().getUsername())
////        );
//    }
}
