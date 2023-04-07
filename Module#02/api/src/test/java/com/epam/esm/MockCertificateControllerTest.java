package com.epam.esm;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificateWithoutTagDto;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.handler.ErrorHandlerController;
import com.epam.esm.service.DefaultCertificateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MockCertificateControllerTest {

    @InjectMocks
    public CertificateController controller;
    @Mock
    private DefaultCertificateService service;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private CertificateDto certificateDto;
    List<CertificateWithoutTagDto> certificateDtoList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new ErrorHandlerController())
                .build();

        certificateDto = CertificateDto.builder()
                .id(1L)
                .name("Gift")
                .description("Certificate")
                .price(new BigDecimal(100))
                .build();

        CertificateWithoutTagDto certificateDto2 = CertificateWithoutTagDto.builder()
                .id(2L)
                .name("Gift 2")
                .description("Certificate 2")
                .price(new BigDecimal(200))
                .build();
        CertificateWithoutTagDto certificateDto3 = CertificateWithoutTagDto.builder()
                .id(3L)
                .name("Gift 3")
                .description("Certificate 3")
                .price(new BigDecimal(300))
                .build();

        certificateDtoList.add(certificateDto3);
        certificateDtoList.add(certificateDto2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldReturnStatus() throws Exception {

        given(service.getById(1L)).willReturn(certificateDto);
        given(service.getById(22L)).willThrow(new CertificateNotFoundException("Certificate not found"));

        mockMvc.perform(get("/certificates/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        mockMvc.perform(get("/certificates/22").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
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
    void shouldReturnListOfCertificate(long id, String name, String description, BigDecimal price) throws Exception {
        List<CertificateWithoutTagDto> certificateDtoList = Arrays.asList(
                CertificateWithoutTagDto.builder().id(id).name(name).description(description).price(price).build(),
                CertificateWithoutTagDto.builder().id(id).name(name).description(description).price(price).build()
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

    @Test
    @DisplayName("Should return a certificate with the specified values")
    void shouldReturnCertificateById() throws Exception {
        given(service.getById(1L)).willReturn(certificateDto);
        mockMvc.perform(get("/certificates/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Gift"))
                .andExpect(jsonPath("$.description").value("Certificate"))
                .andExpect(jsonPath("$.price").value(100));
    }
    @ParameterizedTest
    @CsvSource({
            "1, Gift 1, Certificate 1, 100",
            "2, Gift 2, Certificate 2, 200"
    })
    @DisplayName("Should return a certificate with the specified values")
    void shouldReturnCertificateById(long id, String name, String description, BigDecimal price) throws Exception {
        CertificateDto certificateDto = CertificateDto.builder().id(id).name(name)
                .description(description).price(price).createDate(Instant.now())
                .lastUpdateDate(Instant.now()).build();
        given(service.getById(1L)).willReturn(certificateDto);
        mockMvc.perform(get("/certificates/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.description").value(description))
                .andExpect(jsonPath("$.price").value(price.intValue()));
    }
}
