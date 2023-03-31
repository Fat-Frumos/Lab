package com.epam.esm;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.handler.ErrorHandlerController;
import com.epam.esm.service.DefaultCertificateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MockCertificateControllerTest {

    @InjectMocks
    public CertificateController controller;
    @Mock
    private DefaultCertificateService service;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private CertificateDto certificateDto;
    private CertificateDto certificateDto2;
    List<CertificateDto> certificateDtoList = new ArrayList<>();


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
        certificateDto2 = CertificateDto.builder()
                .id(2L)
                .name("Gift 2")
                .description("Certificate 2")
                .price(new BigDecimal(200))
                .build();

        certificateDtoList.add(certificateDto);
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
        mockMvc.perform(MockMvcRequestBuilders.post("/certificates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(certificateDto)));
        mockMvc.perform(MockMvcRequestBuilders.post("/certificates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(certificateDto)));
        mockMvc.perform(MockMvcRequestBuilders.get("/certificates/"))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    void shouldReturnListOfCertificate() throws Exception {
        given(service.getAll()).willReturn(certificateDtoList);
        mockMvc.perform(MockMvcRequestBuilders.get("/certificates")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Gift"))
                .andExpect(jsonPath("$[0].description").value("Certificate"))
                .andExpect(jsonPath("$[0].price").value(100))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Gift 2"))
                .andExpect(jsonPath("$[1].description").value("Certificate 2"))
                .andExpect(jsonPath("$[1].price").value(200));
    }

    @Test
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
}