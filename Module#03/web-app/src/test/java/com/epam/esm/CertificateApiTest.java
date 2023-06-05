//package com.epam.esm;
//
//import com.epam.esm.dao.CertificateDao;
//import com.epam.esm.dto.CertificateDto;
//import com.epam.esm.service.CertificateService;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.SneakyThrows;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.web.client.RestTemplate;
//
//import java.math.BigDecimal;
//import java.util.Collections;
//import java.util.List;
//import java.util.Objects;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
//import static org.springframework.http.HttpStatus.OK;
//import static org.springframework.http.MediaType.APPLICATION_JSON;
//
//@ActiveProfiles("dev")
//@SpringBootTest(webEnvironment = RANDOM_PORT)
//public class CertificateApiTest {
//
//    @LocalServerPort
//    private int port;
//    @Autowired
//    private TestRestTemplate restTemplate;
//    @Autowired
//    private CertificateDao dao;
//    @Autowired
//    private CertificateService service;
//
//    private static HttpHeaders headers;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @BeforeAll
//    public static void init() {
//        headers = new HttpHeaders();
//        headers.setContentType(APPLICATION_JSON);
//    }
//
//    private String createURLWithPort() {
//        return String.format("http://localhost:%d/api/certificates/", port);
//    }
//
//    @Test
//    @DisplayName("Test ResponseEntity<CertificateResponse> for retrieving certificates")
//    void testResponseEntityCertificates() {
//        HttpEntity<String> entity = new HttpEntity<>(null, headers);
//        ResponseEntity<CertificateResponse> response = restTemplate.exchange(createURLWithPort(), HttpMethod.GET, entity, CertificateResponse.class);
//        List<CertificateDto> orderList = Objects.requireNonNull(response.getBody()).get_embedded().getCertificateDtoList();
//        assertNotNull(orderList);
//        assertEquals(OK, response.getStatusCode());
//        assertEquals(orderList.size(), service.getCertificates(PageRequest.of(0, 25, Sort.by(Sort.Direction.ASC, "id"))).size());
//        assertEquals(orderList.size(), dao.getAllBy(PageRequest.of(0, 25, Sort.by(Sort.Direction.ASC, "id"))).size());
//    }
//
//    @Test
//    @SneakyThrows
//    @DisplayName("Test deserialization of CertificateResponse from String")
//    void testCertificateList() {
//        ResponseEntity<String> response = restTemplate.getForEntity(createURLWithPort(), String.class);
//        assertEquals(OK, response.getStatusCode());
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        List<CertificateDto> certificates = objectMapper.readValue(response.getBody(), CertificateResponse.class).get_embedded().getCertificateDtoList();
//        assertNotNull(certificates);
//    }
//
//    @Test
//    @DisplayName("Test deserialization of CertificateResponse using RestTemplate")
//    void testDeserialization() {
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Collections.singletonList(APPLICATION_JSON));
//        HttpEntity<String> entity = new HttpEntity<>(null, headers);
//
//        ResponseEntity<CertificateResponse> response = restTemplate
//                .exchange(createURLWithPort(), HttpMethod.GET, entity, CertificateResponse.class);
//
//        CertificateResponse certificateResponse = response.getBody();
//        assertNotNull(certificateResponse);
//        List<CertificateDto> certificates = certificateResponse.get_embedded().getCertificateDtoList();
//        assertNotNull(certificates);
//    }
//
//    @ParameterizedTest
//    @SneakyThrows
//    @DisplayName("Test saving CertificateDto using deserialized CertificateResponse")
//    @CsvSource({
//            "1, Olivia, Noah, Olivia-Noah@gmail.com, 10, Java, certificate description, 10, 30",
//            "2, Emma, Liam, Emma-Liam@gmail.com, 20, Certificate, certificate description, 20, 45",
//            "3, Charlotte, Oliver, Charlotte-Oliver@gmail.com, 30, Spring, certificate description, 30, 60",
//            "4, Amelia, Elijah, Amelia-Elijah@gmail.com, 40, SQL, certificate description, 40, 75",
//            "5, Ava, Leo, Ava-Leo@gmail.com, 50, Programming, certificate description, 50, 90"
//    })
//    void testCertificateLists(long id,
//                              String firstName,
//                              String lastName,
//                              String email,
//                              long certificateId,
//                              String certificateName,
//                              String certificateDescription,
//                              BigDecimal price,
//                              int duration) {
//        CertificateDto original = CertificateDto.builder()
////                .name(certificateName)
////                .description(certificateDescription)
//                .price(price)
//                .duration(duration)
//                .build();
//        ResponseEntity<String> response = restTemplate.getForEntity(createURLWithPort(), String.class);
//        assertEquals(OK, response.getStatusCode());
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        CertificateResponse certificateResponse = objectMapper.readValue(response.getBody(), CertificateResponse.class);
//        assertNotNull(certificateResponse);
//        List<CertificateDto> certificates = certificateResponse.get_embedded().getCertificateDtoList();
//        certificates.forEach(Assertions::assertNotNull);
//        CertificateDto certificateDto = service.save(original);
//        System.out.println(certificateDto);
////        assertThat(certificateDto).usingRecursiveComparison().isEqualTo(original);
//    }
//
//}
////    @Sql(scripts = {"/db/test/test.sql"}, executionPhase = BEFORE_TEST_METHOD)
////    @Sql(statements = "DELETE FROM orders WHERE id='2'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
////        ClassPathResource resource = new ClassPathResource("/mapping/certificate.json");
////        String expectedJson = new String(Files.readAllBytes(resource.getFile().toPath()));
////        assertEquals(expectedJson, response.getBody());
////ArgumentCaptor<CertificateDto> certificateArgumentCaptor = ArgumentCaptor.forClass(CertificateDto.class);
////    CertificateDto capturedCertificateDto = certificateArgumentCaptor.getValue();
////    assertThat(capturedCertificateDto).usingRecursiveComparison().isEqualTo(original);
