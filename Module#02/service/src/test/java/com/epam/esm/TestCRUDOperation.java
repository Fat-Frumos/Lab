package com.epam.esm;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.service.DefaultCertificateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static com.epam.esm.mapper.CertificateMapper.mapper;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestCRUDOperation {

    @Mock
    private CertificateDao dao;

    @InjectMocks
    private DefaultCertificateService service;

    private static final Long id = 1L;

    private Certificate certificate;

    @BeforeEach
    public void setUp() {
        CertificateDto certificateDto =
                CertificateDto.builder()
                .id(id)
                .name("Gift")
                .description("Test certificate description")
                .price(BigDecimal.TEN)
                .duration(30)
                .build();

        certificate = Certificate.builder()
                .id(id)
                .name(certificateDto.getName())
                .description(certificateDto.getDescription())
                .price(certificateDto.getPrice())
                .duration(certificateDto.getDuration())
                .build();
    }


    @DisplayName("Test Certificate save")
    @ParameterizedTest(name = "Run {index}: certificateDto = {0}, expected = {1}")
    @CsvSource({
            "10, Gift, Certificate, 1, 510",
            "11, Gift2, Certificate2, 1, 520",
            "12, Gift3, Certificate3, 1, 530"
    })
    void testSaveCertificate(long id, String name, String description, BigDecimal price, int duration) {
        certificate = Certificate.builder()
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .id(id)
                .build();
        when(service.save(mapper.toDto(certificate))).thenReturn(true);
        assertTrue(service.save(mapper.toDto(certificate)));
    }

    @Test
    @DisplayName("Test Certificate Deletion")
    void testDeleteCertificate() {
        when(dao.delete(id)).thenReturn(true);
        assertTrue(service.delete(id));
        verify(dao, times(1)).delete(id);
    }

    @Test
    @DisplayName("Test Certificate Deletion Twice")
    void testDeleteCertificateTwice() {
        when(dao.delete(id)).thenReturn(true).thenReturn(false);
        assertTrue(service.delete(id));
        assertFalse(service.delete(id));
        verify(dao, times(2)).delete(id);
    }
}
