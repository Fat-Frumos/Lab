package com.epam.esm.controller;

import com.epam.esm.dao.UserDaoImpl;
import com.epam.esm.dto.RoleDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserSlimDto;
import com.epam.esm.entity.RoleType;
import com.epam.esm.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@DirtiesContext
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    String admin = "ROLE_ADMIN";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserDaoImpl userDao;
    @MockBean
    private UserService userService;
    Long userId = 1L;
    String username = "alice";
    UserDto userDto = UserDto.builder().id(userId).username(username).email("Admin@i.ua").role(RoleDto.builder().permission(RoleType.USER).build()).build();
    UserSlimDto postDto = UserSlimDto.builder().id(userId).password("admin").email("Admin@i.ua").username(username).role(RoleDto.builder().permission(RoleType.USER).build()).build();

//    @Test
//    @DisplayName("Given existing user ID, when getUser, then return the user")
//    void getUser() throws Exception {
//        UserDto userDto = UserDto.builder().id(userId).username(username).build();
//        when(userService.getById(userId)).thenReturn(userDto);
//        mockMvc.perform(get("/users/{id}", userId).with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"))))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(userId))
//                .andExpect(jsonPath("$.username").value(username));
//    }

    @Test
    @DisplayName("Given pageable information, when getUsers, then return the collection of users")
    void getUsers() throws Exception {
        Page<UserDto> page = new PageImpl<>(Collections.singletonList(userDto));
        when(userService.getAll(any(Pageable.class))).thenReturn(page);
        mockMvc.perform(get("/users").with(user(username))
                        .with(jwt().authorities(new SimpleGrantedAuthority(admin))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.userDtoList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.userDtoList[0].username").value(username));
    }

    @Test
    @DisplayName("Create Order: Given valid user ID and certificate IDs, when create order, then return HTTP status 201")
    void testCreateOrderShouldReturnHttpStatusCreated() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postDto))
                        .with(jwt().authorities(new SimpleGrantedAuthority(admin))))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Create Order: Given invalid DTO, when create order, then return HTTP status 400")
    void testCreateOrderShouldReturnHttpStatusBadRequest() throws Exception {
        UserDto postDto = UserDto.builder().build();
        mockMvc.perform(post("/users")
                        .with(jwt().authorities(new SimpleGrantedAuthority(admin)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Delete User: Given valid user ID, when delete user, then return HTTP status 204")
    void testDeleteUserShouldReturnHttpStatusNoContent() throws Exception {
        mockMvc.perform(delete("/users/{id}", postDto.getId())
                        .with(jwt().authorities(new SimpleGrantedAuthority(admin))))
                .andExpect(status().isNoContent());
    }
}
