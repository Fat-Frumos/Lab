package com.epam.esm;

import com.epam.esm.controller.TagController;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TagControllerTest {
    @InjectMocks
    public TagController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    @Mock
    private TagService service;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @ParameterizedTest
    @CsvSource({
            "1, 'Tag 1'",
            "2, 'Tag 2'",
            "3, 'Tag 3'"
    })
    @DisplayName("Should return tag by id")
    void testGetByIdShouldReturnTagById(Long id, String name) throws Exception {
        TagDto tagDto = TagDto.builder()
                .id(id)
                .name(name)
                .build();

        when(service.getById(id)).thenReturn(tagDto);

        mockMvc.perform(get("/tags/" + id)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(Objects.requireNonNull(tagDto.getId())))
                .andExpect(jsonPath("$.name").value(Objects.requireNonNull(tagDto.getName())));
        verify(service, times(1)).getById(id);
    }

    @Test
    @DisplayName("Should return all tags")
    void testGetAllShouldReturnAllTags() throws Exception {
        List<TagDto> tagDtos = Arrays.asList(
                TagDto.builder().id(1L).name("Tag 1").build(),
                TagDto.builder().id(2L).name("Tag 2").build(),
                TagDto.builder().id(3L).name("Tag 3").build()
        );

        when(service.getAll()).thenReturn(tagDtos);

        mockMvc.perform(get("/tags")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].id").value(tagDtos.get(0).getId().intValue()))
                .andExpect(jsonPath("$[0].name").value(tagDtos.get(0).getName()))
                .andExpect(jsonPath("$[1].id").value(tagDtos.get(1).getId().intValue()))
                .andExpect(jsonPath("$[1].name").value(tagDtos.get(1).getName()))
                .andExpect(jsonPath("$[2].id").value(tagDtos.get(2).getId().intValue()))
                .andExpect(jsonPath("$[2].name").value(tagDtos.get(2).getName()));

        verify(service, times(1)).getAll();
    }

    @Test
    @DisplayName("Should save tag")
    void testSaveShouldSaveTag() throws Exception {
        TagDto tagDto = new TagDto(null, "Tag 1");
        TagDto expectedTagDto = new TagDto(1L, "Tag 1");
        when(service.save(tagDto)).thenReturn(expectedTagDto);
        String requestBody = objectMapper.writeValueAsString(tagDto);

        mockMvc.perform(post("/tags").contentType(MediaType.APPLICATION_JSON_VALUE).content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Tag 1"));
        verify(service, times(1)).save(tagDto);
    }
}
