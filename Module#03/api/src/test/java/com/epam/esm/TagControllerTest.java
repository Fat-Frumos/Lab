package com.epam.esm;

import com.epam.esm.controller.TagController;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TagControllerTest {
    @InjectMocks
    public TagController controller;

    private MockMvc mockMvc;
    @Mock
    private TagService service;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
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
                .tagId(id)
                .name(name)
                .build();

        when(service.getById(id)).thenReturn(tagDto);

        mockMvc.perform(get("/api/tags/" + id)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.tagId").value(Objects.requireNonNull(tagDto.getTagId())))
                .andExpect(jsonPath("$.name").value(Objects.requireNonNull(tagDto.getName())));
        verify(service, times(1)).getById(id);
    }

    @Test
    @DisplayName("Should return all tags")
    void testGetAllShouldReturnAllTags() throws Exception {
        List<TagDto> tagDtos = Arrays.asList(
                TagDto.builder().tagId(1L).name("Tag 1").build(),
                TagDto.builder().tagId(2L).name("Tag 2").build(),
                TagDto.builder().tagId(3L).name("Tag 3").build()
        );

        when(service.getAll()).thenReturn(tagDtos);

        mockMvc.perform(get("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].tagId").value(tagDtos.get(0).getTagId().intValue()))
                .andExpect(jsonPath("$[0].name").value(tagDtos.get(0).getName()))
                .andExpect(jsonPath("$[1].tagId").value(tagDtos.get(1).getTagId().intValue()))
                .andExpect(jsonPath("$[1].name").value(tagDtos.get(1).getName()))
                .andExpect(jsonPath("$[2].tagId").value(tagDtos.get(2).getTagId().intValue()))
                .andExpect(jsonPath("$[2].name").value(tagDtos.get(2).getName()));

        verify(service, times(1)).getAll();
    }
}
