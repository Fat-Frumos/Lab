package com.epam.esm.service;

import com.epam.esm.dao.CertificateDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ServiceExceptionTest.class})
class ServiceExceptionTest {

    @Mock
    private CertificateDao certificateDao;
    @InjectMocks
    private DefaultCertificateService service;
    private static Exception cause;

    @BeforeEach
    void setUp() {
        cause = new Exception("Cause Exception");
        certificateDao = mock(CertificateDao.class);
        service = new DefaultCertificateService(certificateDao);
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
