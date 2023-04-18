package com.epam.esm.service;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.CertificateDaoImpl;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.exception.CertificateAlreadyExistsException;
import com.epam.esm.mapper.CertificateMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CertificateServiceExceptionTest.class})
class CertificateServiceExceptionTest {

    private final static Exception cause = new Exception("Cause Exception");
    @Mock
    private final CertificateDao dao = mock(CertificateDaoImpl.class);
    @Mock
    private CertificateMapper mapper = mock(CertificateMapper.class);
    @InjectMocks
    private CertificateService service = new CertificateServiceImpl(dao, mapper);
    private static final String message = "An error occurred";

    @Test
    @DisplayName("Test getAll with error")
    void testGetAllWithError() throws RuntimeException {
        when(dao.getAll()).thenThrow(new RuntimeException("Error message"));
        assertThrows(RuntimeException.class, () -> service.getAll());
        verify(dao, times(1)).getAll();
    }

    @Test
    void testRuntimeException() {
        try {
            throw new RuntimeException(message);
        } catch (RuntimeException e) {
            Assertions.assertEquals(message, e.getMessage());
        }
    }

    @Test
    void testRuntimeExceptionWithMessageAndCause() {
        Throwable cause = new IllegalArgumentException(message);
        RuntimeException e = new RuntimeException(message, cause);
        assertEquals(message, e.getMessage());
        assertEquals(cause, e.getCause());
    }

    @Test
    void testRuntimeExceptionThrownWithMessage() {
        try {
            throw new RuntimeException(message);
        } catch (RuntimeException ex) {
            assertEquals(message, ex.getMessage());
        }
    }


    @Test
    void testCertificateIsExistsException() {
        CertificateAlreadyExistsException exception = new CertificateAlreadyExistsException("Test Certificate");
        assertTrue(exception.getMessage().contains("Test Certificate"));
    }

    @Test
    @DisplayName("Should throw CertificateIsExistsException when a certificate with the same name already exists")
    void shouldThrowCertificateIsExistsException() {

        CertificateDto certificateDto = CertificateDto.builder()
                .id(1L)
                .name("Existing Certificate")
                .description("An existing certificate")
                .duration(5)
                .price(BigDecimal.valueOf(10.0))
                .build();

        Certificate existingCertificate = Certificate.builder().build();

        when(mapper.toEntity(certificateDto)).thenReturn(existingCertificate);
        when(dao.getByName("Existing Certificate")).thenReturn(Optional.of(existingCertificate));

        assertThrows(CertificateAlreadyExistsException.class, () -> service.save(certificateDto));
        verify(dao, never()).save(existingCertificate);
    }

    @Test
    @DisplayName("Creating RuntimeException with cause")
    void createRuntimeExceptionWithCause() {
        RuntimeException exception = new RuntimeException(cause);
        assertThrows(RuntimeException.class, () -> {
            throw exception;
        }, cause.getMessage());
    }
}
