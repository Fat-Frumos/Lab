package com.epam.esm.controller;

import com.epam.esm.assembler.TagAssembler;
import com.epam.esm.assembler.UserAssembler;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.service.TagService;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest(UserController.class)
//@ExtendWith(MockitoExtension.class)
//class UserControllerTest {
//
//    @Mock
//    private UserService service;
////    @Mock
////    private UserAssembler assembler;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    void getUserTest() throws Exception {
//        UserDto userDto = UserDto.builder().build();
//        User user = User.builder().build();
//        when(service.findById(1L)).thenReturn(user);
////        when(assembler.toModel(userDto)).thenReturn(EntityModel.of(userDto));
//
//        mockMvc.perform(get("/users/{id}", 1L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value(userDto.getUsername()))
//                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
//    }
//}
