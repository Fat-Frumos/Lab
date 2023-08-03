package com.epam.esm.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

import static java.sql.Timestamp.valueOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CertificateSlimDtoTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();
    Long id;
    String name;
    String description;
    BigDecimal price;
    int duration;
    Timestamp createDate;
    Timestamp lastUpdateDate;

    @BeforeEach
    public void setup() {
        id = 1L;
        name = "Name";
        description = "Description";
        price = BigDecimal.valueOf(9999.99);
        duration = 365;
        createDate = valueOf(LocalDateTime.now());
        lastUpdateDate = valueOf(LocalDateTime.now());
    }

    @Test
    @DisplayName("Test CertificateSlimDto validation")
    void testCertificateSlimDtoValidation() {
        CertificateSlimDto certificate =
                getDto(id, name, description, price, duration, createDate, lastUpdateDate);
        Set<ConstraintViolation<CertificateSlimDto>> violations =
                validator.validate(certificate);
        assertTrue(violations.isEmpty());
        certificate.setName("");
        violations = validator.validate(certificate);
        assertEquals(1, violations.size());
        assertEquals("size must be between 1 and 512",
                violations.iterator().next().getMessage());

        certificate.setName("Name");
        certificate.setPrice(BigDecimal.valueOf(10000));
        violations = validator.validate(certificate);
        assertEquals(1, violations.size());
        assertEquals("Price must be less than 10000.00",
                violations.iterator().next().getMessage());

        certificate.setPrice(BigDecimal.valueOf(9999.99));
        certificate.setDuration(366);
        violations = validator.validate(certificate);
        assertEquals(1, violations.size());
        assertEquals("Duration must be less than or equal to 365.",
                violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Given a valid name, when CertificateSlimDto is validated, then no validation errors are generated")
    void testValidName() {
        CertificateSlimDto slimDto =
                getDto(id, name, description, price, duration, createDate, lastUpdateDate);
        Set<ConstraintViolation<CertificateSlimDto>> violations =
                validator.validate(slimDto);
        assertTrue(violations.isEmpty());
        assertEquals(slimDto.getId(), id);
        assertEquals(slimDto.getName(), name);
        assertEquals(slimDto.getDescription(), description);
        assertEquals(slimDto.getPrice(), price);
        assertEquals(slimDto.getDuration(), duration);
        assertEquals(slimDto.getCreateDate(), createDate);
        assertEquals(slimDto.getLastUpdateDate(), lastUpdateDate);
    }

    @Test
    @DisplayName("Given an invalid name, when CertificateSlimDto is validated, then a validation error is generated with the correct message")
    void testInvalidName() {
        CertificateSlimDto certificate = getDto(id, "", description, price, duration, createDate, lastUpdateDate);
        Set<ConstraintViolation<CertificateSlimDto>> violations =
                validator.validate(certificate);
        assertEquals("size must be between 1 and 512",
                violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Given an invalid name with length 513, when CertificateSlimDto is validated, then a validation error is generated with the correct message")
    void testInvalidNameMax() {
        String invalidName = "a".repeat(513);
        CertificateSlimDto certificate = getDto(id, invalidName, description, price, duration, createDate, lastUpdateDate);
        Set<ConstraintViolation<CertificateSlimDto>> violations =
                validator.validate(certificate);
        assertEquals("size must be between 1 and 512",
                violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Given a valid description, when CertificateSlimDto is validated, then no validation errors are generated")
    void testValidDescription() {
        CertificateSlimDto certificate = getDto(id, name, description, price, duration, createDate, lastUpdateDate);
        Set<ConstraintViolation<CertificateSlimDto>> violations =
                validator.validate(certificate);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Given an invalid description, when CertificateSlimDto is validated, then a validation error is generated with the correct message")
    void testInvalidDescription() {
        String invalidDescription = "a".repeat(1025);
        CertificateSlimDto certificate =
                getDto(id, name, invalidDescription, price, duration, createDate, lastUpdateDate);
        Set<ConstraintViolation<CertificateSlimDto>> violations =
                validator.validate(certificate);
        assertEquals("size must be between 1 and 1024",
                violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Given an invalid price, when CertificateSlimDto is validated, then a validation error is generated with the correct message")
    void testInvalidPrice() {
        BigDecimal price = BigDecimal.valueOf(10000.0);
        CertificateSlimDto certificate =
                getDto(id, name, description, price, duration, createDate, lastUpdateDate);
        Set<ConstraintViolation<CertificateSlimDto>> violations =
                validator.validate(certificate);
        assertEquals("Price must be less than 10000.00",
                violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Given an invalid duration, when CertificateSlimDto is validated, then a validation error is generated with the correct message")
    void testInvalidDuration() {
        CertificateSlimDto certificate =
                getDto(id, name, description, price, 366, createDate, lastUpdateDate);
        Set<ConstraintViolation<CertificateSlimDto>> violations =
                validator.validate(certificate);
        assertEquals("Duration must be less than or equal to 365.",
                violations.iterator().next().getMessage());
    }

    private static CertificateSlimDto getDto(
            long id, String name, String description,
            BigDecimal price, int duration, Timestamp createDate,
            Timestamp lastUpdateDate) {
        return CertificateSlimDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .createDate(createDate)
                .lastUpdateDate(lastUpdateDate)
                .build();
    }
}