package com.epam.esm.controller;

import com.epam.esm.assembler.CertificateAssembler;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.PatchCertificateDto;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CertificateControllerTest {
    @InjectMocks
    public CertificateController controller;
    @Mock
    private CertificateService service;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    @Mock
    private CertificateAssembler assembler;
    private CertificateDto dto;
    Long id = 1L;
    int[] ids = new int[]{1, 3, 2, 4, 5};

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ErrorHandlerController())
                .build();
        dto = CertificateDto.builder()
                .id(id)
                .name("Gift").duration(30)
                .description("Certificate")
                .price(BigDecimal.valueOf(100))
                .build();
    }

//    @Test
//    void testUpdateCertificateDuration() throws Exception {
//        Long certificateId = 3L;
//        int newDuration = 60;
//
//        CertificateDto updatedCertificate = CertificateDto.builder()
//                .id(certificateId)
//                .duration(newDuration)
//                .build();
//
//        CertificateDto existingCertificate = CertificateDto.builder()
//                .id(certificateId)
//                .build();
//        mockMvc.perform(patch("/api/certificates/" + certificateId)
//                        .contentType(APPLICATION_JSON)
//                        .content("{\"duration\": 60}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(certificateId))
//                .andExpect(jsonPath("$.duration").value(newDuration));
//
//        controller.update(id, existingCertificate);
//        verify(service).update(updatedCertificate);
//    }

    @ParameterizedTest
    @DisplayName("Test update certificate should return certificates")
    @CsvSource({
            "1, Olivia, Noah, Olivia-Noah@gmail.com, 10, Java, description, 10, 30",
            "2, Emma, Liam, Emma-Liam@gmail.com, 20, Certificate, description, 20, 45",
            "3, Charlotte, Oliver, Charlotte-Oliver@gmail.com, 30, Spring, description, 30, 60",
            "4, Amelia, Elijah, Amelia-Elijah@gmail.com, 40, SQL, description, 40, 75",
            "5, Ava, Leo, Ava-Leo@gmail.com, 50, Programming, description, 50, 90"
    })
    void testCreateAndUpdateCertificateShouldReturnCertificates(
            long id,
            String firstName,
            String lastName,
            String email,
            long certificateId,
            String certificateName,
            String certificateDescription,
            BigDecimal price,
            int duration) throws Exception {
        List<String> tagNames = new ArrayList<>();
        tagNames.add("Java");
        tagNames.add("SQL");
        PatchCertificateDto expectedCertificate = PatchCertificateDto.builder()
                .id(certificateId)
                .price(price)
                .duration(duration)
                .build();
        mockMvc.perform(post("/certificates")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
        mockMvc.perform(patch("/certificates")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));
        controller.update(id, expectedCertificate);
        verify(service).save(dto);
    }

    @Test
    @DisplayName("Certificate controller")
    void testDeleteShouldDeleteCertificateWhenItExists() throws Exception {
        doNothing().when(service).delete(id);
        controller.delete(id);
        mockMvc.perform(delete("/certificates/{id}", id))
                .andExpect(status().isNoContent());
        verify(service, times(2)).delete(id);
    }
}
