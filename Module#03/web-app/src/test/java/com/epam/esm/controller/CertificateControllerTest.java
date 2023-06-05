package com.epam.esm.controller;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Criteria;
import com.epam.esm.service.CertificateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CertificateController.class)
@SpringJUnitConfig(classes = {TestConfig.class})
class CertificateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CertificateService service;
    CertificateDto dto = CertificateDto.builder()
            .id(1L)
            .name("Test Certificate")
            .description("Test Description")
            .price(new BigDecimal("9.99"))
            .duration(5)
            .build();

    @Test
    @DisplayName("Test getById - Retrieves a certificate by ID")
    void testGetCertificates() throws Exception {
        when(service.getById(1L)).thenReturn(dto);
        mockMvc.perform(get("/certificates/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"Test Certificate\"}"));
    }

    @Test
    @DisplayName("Given certificate ID, when getById, then return the corresponding certificate")
    void testGetCertificateById() throws Exception {
        when(service.getById(1L)).thenReturn(dto);
        mockMvc.perform(get("/certificates/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
        verify(service).getById(1L);
    }

    @ParameterizedTest
    @CsvSource({
            "1, Java, description, 10, 30, Spring, Season",
            "2, SQL, description, 10, 30, Summer, Season",
            "3, Programming, description, 10, 30, Winter, Season",
            "4, PostgreSQL, description, 10, 30, Autumn, Season",
            "5, Spring, description, 10, 30, Years, Season"
    })
    @DisplayName("Given pageable information, when getAll, then return all certificates with pagination")
    void getAllTest(long id, String name, String description, BigDecimal price, long id2, String name2, String desc) throws Exception {
        CertificateDto certificateDto = CertificateDto.builder()
                .id(id)
                .price(price)
                .name(name)
                .description(description)
                .build();
        CertificateDto certificateDto2 = CertificateDto.builder()
                .id(id2)
                .price(price)
                .name(name2)
                .description(desc)
                .build();

        List<CertificateDto> certificateDtoList = Arrays.asList(certificateDto, certificateDto2);
        given(service.getCertificates(any(Pageable.class))).willReturn(certificateDtoList);

        mockMvc.perform(get("/certificates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.certificateDtoList").isArray())
                .andExpect(jsonPath("$._embedded.certificateDtoList", hasSize(2)))
                .andReturn();
    }

    @ParameterizedTest
    @CsvSource({
            "1, Java, description, 10, 30, Spring, Season",
            "2, SQL, description, 10, 30, Summer, Season",
            "3, Programming, description, 10, 30, Winter, Season",
            "4, PostgreSQL, description, 10, 30, Autumn, Season",
            "5, Spring, description, 10, 30, Years, Season"
    })
    @DisplayName("Given search criteria and pageable information, when search, then return matching certificates with pagination")
    void searchTests(long id, String name, String description, BigDecimal price, int duration, String tag1, String tag2) throws Exception {
        List<CertificateDto> dtoList = Collections.singletonList(
                CertificateDto.builder()
                        .id(id)
                        .name(name)
                        .description(description)
                        .price(price)
                        .duration(duration)
                        .build());
        given(service.findAllBy(any(Criteria.class), any(Pageable.class))).willReturn(dtoList);
        mockMvc.perform(get("/certificates/search")
                        .param("name", name)
                        .param("description", description)
                        .param("tagNames", tag1, tag2)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.certificateDtoList").isArray())
                .andExpect(jsonPath("$._embedded.certificateDtoList", hasSize(dtoList.size())))
                .andReturn();
    }

    @Test
    @DisplayName("Given certificate ID, when delete, then delete the corresponding certificate")
    void deleteTest() throws Exception {
        Long certificateId = 1L;
        mockMvc.perform(delete("/certificates/{id}", certificateId))
                .andExpect(status().isNoContent());
        verify(service, times(1)).delete(certificateId);
    }

    @ParameterizedTest
    @CsvSource({
            "1, Java, description, 1, 1",
            "2, SQL, description, 2, 2",
            "3, Programming, description, 3, 3",
            "4, PostgreSQL, description, 4, 4",
            "5, Spring, description, 5, 5"
    })
    @DisplayName("Given user ID, when getUserCertificates, then return all certificates associated with the user")
    void getUserCertificatesTest(long id, String name, String description, long userId, int certificateId) throws Exception {
        CertificateDto certificateDto = CertificateDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .build();
        List<CertificateDto> certificateDtoList = Collections.singletonList(certificateDto);
        given(service.getCertificatesByUserId(userId)).willReturn(new PageImpl<>(certificateDtoList));

        mockMvc.perform(get("/certificates/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.certificateDtoList[0].id").value(id))
                .andExpect(jsonPath("$._embedded.certificateDtoList[0].name").value(name))
                .andExpect(jsonPath("$._embedded.certificateDtoList[0].description").value(description));
    }

    @ParameterizedTest
    @CsvSource({
            "1, Java, description, 1, 1",
            "2, SQL, description, 2, 2",
            "3, Programming, description, 3, 3",
            "4, PostgreSQL, description, 4, 4",
            "5, Spring, description, 5, 5"
    })
    @DisplayName("Given order ID, when getAllByOrderId, then return all certificates associated with the order")
    void getAllByOrderIdTest(long id, String name, String description, long orderId, int certificateId) throws Exception {
        CertificateDto certificateDto = CertificateDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .build();
        List<CertificateDto> certificateDtoList = Collections.singletonList(certificateDto);
        given(service.getByOrderId(orderId)).willReturn(certificateDtoList);
        mockMvc.perform(get("/certificates/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.certificateDtoList[0].id").value(id))
                .andExpect(jsonPath("$._embedded.certificateDtoList[0].name").value(name))
                .andExpect(jsonPath("$._embedded.certificateDtoList[0].description").value(description));
    }

    @ParameterizedTest
    @CsvSource({
            "1, Java, description, 1, Tag1",
            "2, SQL, description, 2, Tag2",
            "3, Programming, description, 3, Tag3",
            "4, PostgreSQL, description, 4, Tag4",
            "5, Spring, description, 5, Tag5"
    })
    @DisplayName("Given certificate ID, when getTagsByCertificateId, then return all tags associated with the certificate")
    void getTagsByCertificateIdTest(long id, String name, String description, long certificateId, String tagName) throws Exception {
        TagDto tagDto = TagDto.builder()
                .id(id)
                .name(tagName)
                .build();
        Set<TagDto> tagDtoSet = Collections.singleton(tagDto);
        given(service.findTagsByCertificateId(certificateId)).willReturn(tagDtoSet);

        mockMvc.perform(get("/certificates/{id}/tags", certificateId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.tagDtoList[0].id").value(certificateId))
                .andExpect(jsonPath("$._embedded.tagDtoList[0].name").value(tagName));
    }
}
