package com.epam.esm;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.DefaultCertificateDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.mapper.CertificateMapper;
import com.epam.esm.service.DefaultCertificateService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
class DefaultCertificateServiceTest {
    private final CertificateMapper mapper = CertificateMapper.getInstance();
    private final CertificateDao dao = mock(DefaultCertificateDao.class);
    @InjectMocks
    private DefaultCertificateService service = new DefaultCertificateService(dao, mapper);
    @Mock
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
                        .tags(new HashSet<>())
                        .build(),

                Certificate.builder()
                        .id(2L)
                        .name("Gift2")
                        .duration(20)
                        .description("Certificate2")
                        .price(new BigDecimal(200))
                        .tags(new HashSet<>())
                        .build(),

                Certificate.builder()
                        .id(3L)
                        .name("Gift2")
                        .description("Certificate2")
                        .duration(30)
                        .price(new BigDecimal(200))
                        .tags(new HashSet<>())
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
        Long invalidId = Long.MAX_VALUE;
        assertThrows(RuntimeException.class,
                () -> service.getById(invalidId),
                "Expected getById to throw a RuntimeException with an invalid id");
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
    @DisplayName("Test getById with invalid id")
    void testGetByIdWithInvalidIds() {
        Long invalidId = 100L;
        assertThrows(RuntimeException.class,
                () -> service.getById(invalidId));
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

    @DisplayName("Test Certificate Update")
    @ParameterizedTest(name = "Run {index}: certificateDto = {0}, expected = {1}")
    @CsvSource({
            "1,updated name,updated description,10,50, true",
            "2,updated name 2,updated description 2,20,30, true"
    })
    void testUpdateCertificate(long id, String name, String description, BigDecimal price, int duration, boolean expected) {
        CertificateDto certificateDto = CertificateDto.builder()
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .id(id)
                .build();
        when(service.update(certificateDto)).thenReturn(true);
        boolean update = service.update(certificateDto);
        assertEquals(expected, update);
    }
    @DisplayName("Test Get Certificate By Name")
    @ParameterizedTest(name = "Run {index}: name = {0}")
    @CsvSource({
            "Gift",
            "Certificate",
            "Java",
            "SQL",
            "Programming",
            "Spring"
    })
    void testGetCertificateByName(String name) {
        Certificate certificate = Certificate.builder()
                .name(name)
                .description("Certificate description")
                .price(new BigDecimal("10"))
                .duration(30)
                .id(1L)
                .build();
        when(dao.getByName(name)).thenReturn(certificate);

        CertificateDto certificateDto = service.getByName(name);

        assertNotNull(certificateDto);
        assertEquals(name, certificateDto.getName());
        assertEquals("Certificate description", certificateDto.getDescription());
        assertEquals(new BigDecimal("10"), certificateDto.getPrice());
        assertEquals(30, certificateDto.getDuration());
    }
}
