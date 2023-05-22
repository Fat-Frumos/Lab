package com.epam.esm.dto;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CertificateDtoTest {

    @ParameterizedTest(name = "Test #{index} - ID: {0}, Name: {1}, Description: {2}")
    @CsvSource({
            "1, Winter, Season 1, 10.0, 30",
            "2, Summer, Season 2, 20.0, 45",
            "3, Spring, Season 3, 30.0, 60",
            "4, Autumn, Season 4, 40.0, 75"})
    void certificateDtoValidation(Long id, String name, String description, BigDecimal price, int duration) {
        CertificateDto certificateDto = CertificateDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .createDate(Timestamp.valueOf(LocalDateTime.now()))
                .lastUpdateDate(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        assertDoesNotThrow(() -> validateCertificateDto(certificateDto));
    }

    @ParameterizedTest(name = "Test #{index} - ID: {0}, Name: {1}, Description: {2}")
    @CsvSource({
            "1, Winter, Season 1, 10.0, 30",
            "2, Summer, Season 2, 20.0, 45",
            "3, Spring, Season 3, 30.0, 60",
            "4, Autumn, Season 4, 40.0, 75",
            "5, , , 20.0, 30",
            "6, Summer, , , 40",
            "7, , Season 3, , 50",
            "8, Autumn, Season 4, -10.0, 60",
            "9, Winter, Season 1, 10.0, -5"})
    void certificateDtoValidationsNeg(Long id, String name, String description, BigDecimal price, int duration) {
        CertificateDto certificateDto = CertificateDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .createDate(Timestamp.valueOf(LocalDateTime.now()))
                .lastUpdateDate(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        if (id != null && price != null && name != null && duration > 0) {
            assertDoesNotThrow(() -> validateCertificateDto(certificateDto));
        } else {
            assertThrows(ConstraintViolationException.class, () -> validateCertificateDto(certificateDto));
        }
    }

    private void validateCertificateDto(CertificateDto certificateDto) {

        if (certificateDto.getId() == null) {
            throw new ConstraintViolationException("Id cannot be null", null);
        }
        if (certificateDto.getName() == null || certificateDto.getName().isEmpty()) {
            throw new ConstraintViolationException("Name cannot be blank", null);
        }
        if (certificateDto.getDescription() == null || certificateDto.getDescription().isEmpty()) {
            throw new ConstraintViolationException("Description cannot be blank", null);
        }
        if (certificateDto.getPrice() == null) {
            throw new ConstraintViolationException("Price cannot be null", null);
        }
        if (certificateDto.getDuration() <= 0) {
            throw new ConstraintViolationException("Duration must be a positive value", null);
        }
    }
}

