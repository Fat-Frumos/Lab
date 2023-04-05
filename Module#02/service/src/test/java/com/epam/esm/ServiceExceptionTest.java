package com.epam.esm;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.mapper.CertificateMapper;
import com.epam.esm.service.DefaultCertificateService;
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
    @Mock
    private CertificateMapper mapper = CertificateMapper.mapper;
    @InjectMocks
    private DefaultCertificateService service;
    private static final String message = "Service Exception";
    private static Exception cause;

    @BeforeEach
    void setUp() {
        cause = new Exception("Cause Exception");
        certificateDao = mock(CertificateDao.class);
        service = new DefaultCertificateService(certificateDao, mapper);
    }

    @Test
    @DisplayName("Creating Runtime Exception with message when invoke Service getByName with null")
    void createRuntimeExceptionWithMessage() {
        assertThrows(RuntimeException.class, () ->
                service.getByName(null), message);
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
