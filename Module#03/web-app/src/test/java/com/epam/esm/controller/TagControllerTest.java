package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TagController.class)
@SpringJUnitConfig(classes = TestConfig.class)
class TagControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService tagService;

    long id = 1L;
    String tagName = "Summer";
    long id2 = 2L;
    String tagName2 = "Winter";
    TagDto tagDto = TagDto.builder().id(id).name(tagName).build();

    @Test
    @DisplayName("Test getById - Retrieves a tag by ID")
    void getByIdTest() throws Exception {

        TagDto tagDto = TagDto.builder()
                .id(id)
                .name(tagName)
                .build();
        given(tagService.getById(id)).willReturn(tagDto);

        mockMvc.perform(get("/tags/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(tagName));
    }

    @Test
    @DisplayName("Test getAll - Retrieves all tags with pagination")
    void getAllTest() throws Exception {

        TagDto tagDto1 = TagDto.builder()
                .id(id)
                .name(tagName)
                .build();
        TagDto tagDto2 = TagDto.builder()
                .id(id2)
                .name(tagName2)
                .build();
        List<TagDto> tagDtoList = Arrays.asList(tagDto1, tagDto2);
        given(tagService.getAll(any())).willReturn(tagDtoList);

        mockMvc.perform(get("/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.tagDtoList[0].id").value(id))
                .andExpect(jsonPath("$._embedded.tagDtoList[0].name").value(tagName))
                .andExpect(jsonPath("$._embedded.tagDtoList[1].id").value(id2))
                .andExpect(jsonPath("$._embedded.tagDtoList[1].name").value(tagName2));
    }

    @Test
    @DisplayName("Create Test - Verify successful creation of a tag")
    void createTest() throws Exception {

        given(tagService.save(tagDto)).willReturn(tagDto);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(tagName));
    }

    @Test
    @DisplayName("Test delete - Deletes a tag by ID")
    void deleteTest() throws Exception {
        mockMvc.perform(delete("/tags/{id}", id))
                .andExpect(status().isNoContent());
        verify(tagService, times(1)).delete(id);
    }
}