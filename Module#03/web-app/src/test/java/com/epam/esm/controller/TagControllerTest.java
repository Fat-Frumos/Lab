package com.epam.esm.controller;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.handler.ErrorHandlerController;
import com.epam.esm.service.TagService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest(webEnvironment = RANDOM_PORT, classes = {TagDaoImpl.class})
@ExtendWith(MockitoExtension.class)
class TagControllerTest {

    @InjectMocks
    public TagController controller;
    @Mock
    private TagService service;
    @Mock
    private TagDao dao;

    private ObjectMapper objectMapper;
    private MockMvc mockMvc;
    private TagDto dto;
    final long id = 1;
    final String name = "Mazda";

    @BeforeEach
    public void init() {
        dto = TagDto.builder().id(id).name(name).build();
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ErrorHandlerController())
                .build();
    }

//    @Test
//    void readTests() throws Exception {
//        mockMvc.perform(get("/tags/" + id))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(jsonPath("$.id").value(id))
//                .andExpect(jsonPath("$.name").value(name));
//    }
//
//    @Test
//    void getByIdTest() throws Exception {
//        mockMvc.perform(get("/tags/{id}", id))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(jsonPath("$.id").value(id))
//                .andExpect(jsonPath("$.name").value(name));
//    }


//    @Test
//    void createTest() throws Exception {
//        when(service.save(dto)).thenReturn(dto);
//
//        mockMvc.perform(post("/certificates")
//                        .contentType(APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(jsonPath("$.id").value(id))
//                .andExpect(jsonPath("$.name").value(name));
//    }


//    @Test
//    void readTest() throws Exception {
//
//        when(service.getById(id)).thenReturn(dto);
//
//        mockMvc.perform(get("/tags/" + id))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(jsonPath("$.id").value(id))
//                .andExpect(jsonPath("$.name").value(name));
//    }

    @Test
    void deleteTest() throws Exception {
        final long id = 1;
        mockMvc.perform(delete("/tags/" + id))
                .andExpect(status().isNoContent());
    }

    //
//    @Test
//    void testCreateTag() {
//        Tag tag = Tag.builder().build();
//        Tag savedTag = dao.save(tag);
//        assertNotNull(savedTag.getId());
//    }

//    @Test
//    void createTagTest() throws Exception {
//        String filename = "certificate.json";
//        String json = FileReader.read(filename);
//        mockMvc.perform(post("/tags")
//                        .content(json)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().is2xxSuccessful());
//    }
}
