 package com.epam.esm;

 import com.epam.esm.controller.CertificateController;
 import com.epam.esm.dto.CertificateDto;
 import com.epam.esm.handler.ErrorHandlerController;
 import com.epam.esm.service.CertificateService;
 import com.fasterxml.jackson.databind.ObjectMapper;
 import org.junit.jupiter.api.BeforeEach;
 import org.junit.jupiter.api.DisplayName;
 import org.junit.jupiter.api.Test;
 import org.junit.jupiter.api.extension.ExtendWith;
 import org.mockito.InjectMocks;
 import org.mockito.Mock;
 import org.mockito.junit.jupiter.MockitoExtension;
 import org.springframework.http.MediaType;
 import org.springframework.test.web.servlet.MockMvc;
 import org.springframework.test.web.servlet.setup.MockMvcBuilders;

 import java.math.BigDecimal;

 import static org.mockito.Mockito.doNothing;
 import static org.mockito.Mockito.times;
 import static org.mockito.Mockito.verify;
 import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

 @ExtendWith(MockitoExtension.class)
 class CertificateControllerTest {
     @InjectMocks
     public CertificateController controller;
     @Mock
     private CertificateService service;
     private MockMvc mockMvc;
     private ObjectMapper objectMapper;
     private CertificateDto certificateDto;

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
     }

     @Test
     void shouldReturnCertificate() throws Exception {
         mockMvc.perform(post("/api/certificates")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(objectMapper.writeValueAsString(certificateDto)));
         mockMvc.perform(post("/api/certificates")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(objectMapper.writeValueAsString(certificateDto)));
     }

     @Test
     @DisplayName("Should delete certificate when it exists")
     void testDeleteShouldDeleteCertificateWhenItExists() {
         Long certificateId = 1L;
         doNothing().when(service).delete(certificateId);
         controller.delete(certificateId);
         verify(service, times(1)).delete(certificateId);
     }
 }
