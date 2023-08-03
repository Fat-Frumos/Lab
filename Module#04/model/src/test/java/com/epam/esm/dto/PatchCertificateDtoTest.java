package com.epam.esm.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

class PatchCertificateDtoTest {
    @Test
    void testPatchCertificateDto() {
        Set<TagDto> tags = new HashSet<>();
        tags.add(new TagDto(1L, "tag1"));
        tags.add(new TagDto(2L, "tag2"));

        PatchCertificateDto certificate = new PatchCertificateDto(
                1L,
                new BigDecimal("99.99"),
                30,
                tags);

        assertEquals(1L, certificate.getId());
        assertEquals(new BigDecimal("99.99"), certificate.getPrice());
        assertEquals(30, certificate.getDuration());
        assertEquals(tags, certificate.getTags());
    }

    @Test
    void testHandleUnknown() {
        PatchCertificateDto certificate = PatchCertificateDto.builder().build();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            certificate.handleUnknown("unknownField", "unknownValue");});
        String expectedMessage = "Field unknownField is not allowed for update: unknownValue";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
