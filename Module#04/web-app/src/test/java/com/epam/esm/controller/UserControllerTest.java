package com.epam.esm.controller;

import com.epam.esm.dto.UserDto;
import com.epam.esm.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    @MockBean
    private UserService userService;

    Long userId = 1L;
    UserDto userDto = UserDto.builder().id(userId).username("Admin").build();

    @Test
    @DisplayName("Given existing user ID, when getUser, then return the user")
    void getUser() throws Exception {

        when(userService.getById(1L)).thenReturn(userDto);
        mockMvc.perform(get("/users/1")
                        .with(jwt().authorities(new SimpleGrantedAuthority(admin))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("Admin"));
    }

    @Test
    @DisplayName("Given pageable information, when getUsers, then return the collection of users")
    void getUsers() throws Exception {
        Page<UserDto> page = new PageImpl<>(Collections.singletonList(userDto));
        when(userService.getAll(any(Pageable.class))).thenReturn(page);
        mockMvc.perform(get("/users")
                .with(jwt().authorities(new SimpleGrantedAuthority(admin))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.userDtoList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.userDtoList[0].username").value("Admin"));
    }
}
