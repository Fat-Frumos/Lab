package com.epam.esm;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.DefaultCertificateDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.mapper.CertificateMapper;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.DefaultCertificateService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DefaultCertificateServiceTest {
    private final CertificateMapper mapper = CertificateMapper.getInstance();
    @Mock
    private CertificateDao dao = mock(DefaultCertificateDao.class);
    @Mock
    private CertificateService service = new DefaultCertificateService(dao, mapper);
    private static List<Certificate> list;
    private static final String message = "An error occurred";
    private static final Long id = 1L;

    @BeforeEach
    public void setUp() {

        list = List.of(
                Certificate.builder()
                        .id(1L)
                        .name("Gift2")
                        .duration(10)
                        .description("Certificate")
                        .price(new BigDecimal(200))
                        .build(),

                Certificate.builder()
                        .id(2L)
                        .name("Gift2")
                        .duration(20)
                        .description("Certificate2")
                        .price(new BigDecimal(200))
                        .build(),

                Certificate.builder()
                        .id(3L)
                        .name("Gift2")
                        .description("Certificate2")
                        .duration(30)
                        .price(new BigDecimal(200))
                        .build()
        );

    }

    @Test
    void testGetAllSizeList() {
        when(dao.getAll()).thenReturn(list);
        List<CertificateDto> actual = service.getAll();
        IntStream.range(0, list.size())
                .forEach(i -> assertEquals(list.get(i).toString(),
                        actual.get(i).toString()));
        assertEquals(list.size(), actual.size());
    }

    @Test
    @DisplayName("Test getAll with error")
    void testGetAllWithError() throws RuntimeException {
        when(dao.getAll()).thenThrow(new RuntimeException("Error message"));

        assertThrows(RuntimeException.class, () -> service.getAll());

        verify(dao, times(1)).getAll();
    }

    @Test
    @DisplayName("Test getById throws Service Exception when certificate does not exist")
    void testGetByIdThrowsRuntimeException() {
        when(dao.getById(id)).thenReturn(null);
        assertThrows(RuntimeException.class, () -> service.getById(id));
    }

    @Test
    @DisplayName("Test getById with invalid id")
    void testGetByIdWithInvalidId() {
        assertThrows(RuntimeException.class,
                () -> service.getById(id));
    }

    @Test
    @DisplayName("Test getById with null id")
    void testGetByIdWithNullIdExpectedRuntimeException() {
        assertThrows(RuntimeException.class,
                () -> service.getById(null));
    }

    @Test
    void testGetById() throws RuntimeException {
        when(dao.getById(2L)).thenReturn(list.get(1));
        assertEquals(service.getById(2L).toString(),
                list.get(1).toString());
    }

    @Test
    void testRuntimeException() {
        String expectedMessage = "Expected error message";
        try {
            throw new RuntimeException(expectedMessage);
        } catch (RuntimeException e) {
            Assertions.assertEquals(expectedMessage,
                    e.getMessage());
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
}
