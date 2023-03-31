package com.epam.esm;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.DefaultCertificateService;
import com.epam.esm.mapper.CertificateMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

@ContextConfiguration(classes = {RuntimeExceptionTest.class})
@ExtendWith(SpringExtension.class)
class RuntimeExceptionTest {
    private final CertificateMapper mapper = CertificateMapper.getInstance();
    private static  CertificateDao certificateDao;
    private static CertificateService certificateService;
    private static final String message = "Service Exception";
    private static Exception cause;

    @BeforeEach
    void setUp() {
        cause = new Exception("Cause Exception");
        certificateDao = mock(CertificateDao.class);
        certificateService = new DefaultCertificateService(certificateDao, mapper);
    }

    @Test
    @DisplayName("Creating RuntimeException with message")
    void createRuntimeExceptionWithMessage() {
        RuntimeException exception = new RuntimeException(message);
        assertThrows(RuntimeException.class, () -> {
            throw exception;
        }, message);
    }

    @Test
    @DisplayName("Creating RuntimeException with message and cause")
    void createRuntimeExceptionWithMessageAndCause() {
        RuntimeException exception = new RuntimeException(message, cause);
        assertThrows(RuntimeException.class, () -> {
            throw exception;
        }, message);
    }

    @Test
    @DisplayName("Creating RuntimeException with cause")
    void createRuntimeExceptionWithCause() {
        Exception cause = new Exception(message);
        RuntimeException exception = new RuntimeException(cause);
        assertThrows(RuntimeException.class, () -> {
            throw exception;
        }, cause.getMessage());
    }
}
