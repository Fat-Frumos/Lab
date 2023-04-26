package com.epam.esm;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificateWithoutTagDto;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.handler.ErrorHandlerController;
import com.epam.esm.service.CertificateService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CertificateControllerTest {
    @InjectMocks
    public CertificateController controller;
    @Mock
    private CertificateService service;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private CertificateDto certificateDto;
    private CertificateDto createdDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ErrorHandlerController())
                .build();
        certificateDto = CertificateDto.builder()
                .id(1L)
                .name("Gift").duration(30)
                .description("Certificate")
                .price(BigDecimal.valueOf(100))
                .build();
        createdDto = CertificateDto.builder()
                .id(1L).name("Test Certificate").duration(30)
                .description("This is a test certificate")
                .price(BigDecimal.valueOf(10))
                .build();
    }

    @Test
    void shouldReturnStatus() throws Exception {
        given(service.getById(1L)).willReturn(certificateDto);
        given(service.getById(22L)).willThrow(
                new CertificateNotFoundException("Certificate not found"));
        mockMvc.perform(get("/certificates/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/certificates/22")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnCertificate() throws Exception {
        mockMvc.perform(post("/certificates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(certificateDto)));
        mockMvc.perform(post("/certificates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(certificateDto)));
        mockMvc.perform(MockMvcRequestBuilders.get("/certificates/"))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @ParameterizedTest(name = "Certificate {index} - {0}")
    @CsvSource({
            "1, Gift 1, Certificate 1, 100",
            "2, Gift 2, Certificate 2, 200"
    })
    void shouldReturnListOfCertificate(
            long id, String name, String description, BigDecimal price)
            throws Exception {
        List<CertificateWithoutTagDto> certificateDtoList = Arrays.asList(
                CertificateWithoutTagDto.builder().id(id).name(name)
                        .description(description).price(price).build(),
                CertificateWithoutTagDto.builder().id(id).name(name)
                        .description(description).price(price).build()
        );
        given(service.getAllWithoutTags()).willReturn(certificateDtoList);
        mockMvc.perform(MockMvcRequestBuilders.get("/certificates")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[" + (id - 1) + "].id").value(id))
                .andExpect(jsonPath("$[" + (id - 1) + "].name").value(name))
                .andExpect(jsonPath("$[" + (id - 1) + "].description").value(description))
                .andExpect(jsonPath("$[" + (id - 1) + "].price").value(price));
    }

    @ParameterizedTest
    @CsvSource({
            "1, Gift 1, Certificate 1, 100",
            "2, Gift 2, Certificate 2, 200"
    })
    @DisplayName("Should return a certificate with the specified values")
    void shouldReturnCertificateById(
            long id, String name, String description, BigDecimal price)
            throws Exception {
        CertificateDto certificateDto = CertificateDto.builder()
                .id(id).name(name)
                .description(description).price(price)
                .createDate(Instant.now())
                .lastUpdateDate(Instant.now())
                .build();
        given(service.getById(1L)).willReturn(certificateDto);
        mockMvc.perform(get("/certificates/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.description").value(description))
                .andExpect(jsonPath("$.price").value(price.intValue()));
    }

    @Test
    @DisplayName("Should Return True When Certificate Is Updated")
    void testUpdateShouldReturnTrueWhenCertificateIsUpdated() throws Exception {
        CertificateDto certificateDto = CertificateDto.builder()
                .id(1L).name("Test Certificate")
                .description("This is a test certificate")
                .duration(30).price(BigDecimal.valueOf(10))
                .build();
        when(service.update(certificateDto)).thenReturn(certificateDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(certificateDto);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(patch("/certificates")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdDto.getId()))
                .andExpect(jsonPath("$.name").value(createdDto.getName()))
                .andExpect(jsonPath("$.description").value(createdDto.getDescription()))
                .andExpect(jsonPath("$.price").value(createdDto.getPrice()))
                .andExpect(jsonPath("$.duration").value(createdDto.getDuration()));
        verify(service, times(1)).update(certificateDto);
    }

    @Test
    @DisplayName("Should delete certificate when it exists")
    void testDeleteShouldDeleteCertificateWhenItExists() {
        Long certificateId = 1L;
        doNothing().when(service).delete(certificateId);
        controller.delete(certificateId);
        verify(service, times(1)).delete(certificateId);
    }

    @Test
    @DisplayName("Should create certificate and return created certificate")
    void testCreateShouldCreateCertificate() throws Exception {
        when(service.save(certificateDto)).thenReturn(createdDto);
        mockMvc.perform(post("/certificates")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(certificateDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(createdDto.getId()))
                .andExpect(jsonPath("$.name").value(createdDto.getName()))
                .andExpect(jsonPath("$.description").value(createdDto.getDescription()))
                .andExpect(jsonPath("$.price").value(createdDto.getPrice()))
                .andExpect(jsonPath("$.duration").value(createdDto.getDuration()));
        verify(service, times(1)).save(certificateDto);
    }
}
