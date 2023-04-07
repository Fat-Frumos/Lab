package com.epam.esm.service.mapper;

import com.epam.esm.domain.Certificate;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.mapper.CertificateMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("CertificateMapper tests")
class CertificateMapperTest {
    private final CertificateMapper mapper = CertificateMapper.getInstance();

    @DisplayName("toEntity should map DTO to entity")
    @ParameterizedTest(name = "Map {1} to entity")
    @CsvSource({
            "1, cert1, desc1, 10.0, 30, 2022-05-01T00:00:00Z, 2022-05-01T01:00:00Z",
            "2, cert2, desc2, 20.0, 60, 2022-06-01T00:00:00Z, 2022-06-01T01:00:00Z",
            "3, cert3, desc3, 30.0, 90, 2022-07-01T00:00:00Z, 2022-07-01T01:00:00Z"
    })
    void toEntity_shouldMapDtoToEntity(Long id, String name, String description, BigDecimal price,
                                       Integer duration, Instant createDate, Instant lastUpdateDate) {
        CertificateDto dto = CertificateDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .createDate(createDate)
                .lastUpdateDate(lastUpdateDate)
                .build();

        Certificate entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals(name, entity.getName());
        assertEquals(description, entity.getDescription());
        assertEquals(price, entity.getPrice());
        assertEquals(duration, entity.getDuration());
        assertEquals(createDate, entity.getCreateDate());
        assertEquals(lastUpdateDate, entity.getLastUpdateDate());
    }

    @DisplayName("Should map CertificateDto to Certificate entity")
    @ParameterizedTest(name = "{index} => dto={0}")
    @CsvSource({
            "1, Test Certificate, This is a test certificate., 100.0, 30",
            "2, Another Test Certificate, This is another test certificate., 50.0, 15"
    })
    void shouldMapCertificateDtoToCertificate(String id, String name, String description, BigDecimal price, Integer duration) {
        CertificateDto dto = CertificateDto.builder()
                .id(Long.valueOf(id))
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .build();

        Certificate certificate = mapper.toEntity(dto);
        assertEquals(dto.getId(), certificate.getId());
        assertEquals(dto.getName(), certificate.getName());
        assertEquals(dto.getDescription(), certificate.getDescription());
        assertEquals(dto.getPrice(), certificate.getPrice());
        assertEquals(dto.getDuration(), certificate.getDuration());
    }

    @DisplayName("Should map Certificate entity to CertificateDto")
    @ParameterizedTest(name = "{index} => entity={0}")
    @CsvSource({
            "1, Test Certificate, This is a test certificate., 100.0, 30",
            "2, Another Test Certificate, This is another test certificate., 50.0, 15"
    })
    void shouldMapCertificateToCertificateDto(String id, String name, String description, BigDecimal price, Integer duration) {
        Certificate certificate = Certificate.builder()
                .id(Long.valueOf(id))
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .build();
        CertificateDto dto = mapper.toDto(certificate);

        assertEquals(certificate.getId(), dto.getId());
        assertEquals(certificate.getName(), dto.getName());
        assertEquals(certificate.getDescription(), dto.getDescription());
        assertEquals(certificate.getPrice(), dto.getPrice());
        assertEquals(certificate.getDuration(), dto.getDuration());
    }

    @Test
    @DisplayName("Should throw NullPointerException when passed null argument")
    void shouldThrowNullPointerExceptionWhenPassedNullToEntity() {
        assertThrows(RuntimeException.class, () -> mapper.toEntity(null));
    }

    @Test
    @DisplayName("Should throw NullPointerException when passed null argument")
    void shouldThrowNullPointerExceptionWhenPassedNullToDto() {
        assertThrows(RuntimeException.class, () -> mapper.toDto(null));
    }
}