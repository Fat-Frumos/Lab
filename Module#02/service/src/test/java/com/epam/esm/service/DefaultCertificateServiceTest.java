package com.epam.esm.service;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.criteria.SortOrder;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.DefaultCertificateDao;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificateWithoutTagDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.exception.CertificateIsExistsException;
import com.epam.esm.mapper.CertificateMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
class DefaultCertificateServiceTest {
    @Mock
    private final CertificateDao dao = mock(DefaultCertificateDao.class);
    @InjectMocks
    private DefaultCertificateService service = new DefaultCertificateService(dao);
    @Mock
    private CertificateMapper mapper = mock(CertificateMapper.class);
    @Mock
    private static List<Certificate> certificateList;
    private static final String message = "An error occurred";
    private static final Long id = 1L;

    @BeforeEach
    public void setUp() {
        certificateList = List.of(
                Certificate.builder()
                        .id(id)
                        .name("Gift1")
                        .duration(10)
                        .description("Certificate1")
                        .price(new BigDecimal(100))
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
                        .name("Gift3")
                        .description("Certificate3")
                        .duration(30)
                        .price(new BigDecimal(300))
                        .tags(new HashSet<>())
                        .build()
        );
    }

    @ParameterizedTest
    @CsvSource({
            "1, Gift1, 10, Certificate1, 100",
            "2, Gift2, 20, Certificate2, 200",
            "3, Gift3, 30, Certificate3, 300"
    })
    void testGetAllWithoutTag(long id, String name, int duration, String description, BigDecimal price) {

        List<Certificate> certificateList = List.of(
                Certificate.builder()
                        .id(id)
                        .name(name)
                        .duration(duration)
                        .description(description)
                        .price(price)
                        .tags(new HashSet<>())
                        .build()
        );
        Mockito.when(dao.getAll()).thenReturn(certificateList);

        List<CertificateWithoutTagDto> expectedList = List.of(
                CertificateWithoutTagDto.builder()
                        .id(id)
                        .name(name)
                        .duration(duration)
                        .description(description)
                        .price(price)
                        .build()
        );
        List<CertificateWithoutTagDto> actualList = service.getAllWithoutTags();
        Assertions.assertEquals(expectedList, actualList);
    }

    @Test
    void testGetAllWithoutTags() {
        Mockito.when(dao.getAll()).thenReturn(certificateList);
        List<CertificateWithoutTagDto> dtos = service.getAllWithoutTags();
        assertEquals(certificateList.size(), dtos.size());
        for (int i = 0; i < dtos.size(); i++) {
            CertificateWithoutTagDto dto = dtos.get(i);
            Certificate certificate = certificateList.get(i);
            assertEquals(certificate.getId(), dto.getId());
            assertEquals(certificate.getName(), dto.getName());
            assertEquals(certificate.getDuration(), dto.getDuration());
            assertEquals(certificate.getDescription(), dto.getDescription());
            assertEquals(certificate.getPrice(), dto.getPrice());
        }
    }

    @DisplayName("Test getAllWithoutTags method with different input certificates")
    @ParameterizedTest(name = "{index} => expectedCertificateWithoutTag=''{1}''")
    @CsvSource({
            "1, Gift1, 10, Certificate1, 100",
            "2, Gift2, 20, Certificate2, 200",
            "3, Gift3, 30, Certificate3, 300"
    })
    void testGetAllWithoutTags(long id, String name, int duration, String description, BigDecimal price) {

        List<CertificateWithoutTagDto> expectedCertificates = new ArrayList<>();
        CertificateWithoutTagDto certificateWithoutTagDto = CertificateWithoutTagDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .build();
        expectedCertificates.add(certificateWithoutTagDto);

        when(dao.getAll()).thenReturn(certificateList);
        when(mapper.toDtoWithoutTagsList(certificateList)).thenReturn(expectedCertificates);
        List<CertificateWithoutTagDto> actualCertificates = service.getAllWithoutTags();
        assertEquals(expectedCertificates.get(0), actualCertificates.get(Math.toIntExact(id - 1)));
        verify(dao, times(1)).getAll();
    }


    @Test
    void testGetAllSizeList() {
        when(dao.getAll()).thenReturn(certificateList);
        List<CertificateDto> actual = service.getAll();
        IntStream.range(0, certificateList.size())
                .forEach(i -> assertEquals(certificateList.get(i).toString(),
                        actual.get(i).toString()));
        assertEquals(certificateList.size(), actual.size());
    }

    @Test
    @DisplayName("Test getAll with error")
    void testGetAllWithError() throws RuntimeException {
        when(dao.getAll()).thenThrow(new RuntimeException("Error message"));

        assertThrows(RuntimeException.class, () -> service.getAll());

        verify(dao, times(1)).getAll();
    }

    @Test
    void testGetById() throws RuntimeException {
        when(dao.getById(2L)).thenReturn(certificateList.get(1));
        assertEquals(service.getById(2L).toString(),
                certificateList.get(1).toString());
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
        verify(dao, times(1)).getByName(name);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 'Gift1', 'Certificate1', 100, 10",
            "2, 'Gift2', 'Certificate2', 200, 20"
    })
    void getAllBy_shouldReturnCertificates(long id, String name, String description, BigDecimal price, int duration) {
        Criteria criteria = Criteria.builder()
                .name("name")
                .sortOrder(SortOrder.ASC)
                .build();

        List<CertificateDto> expected = Collections.singletonList(
                CertificateDto.builder()
                        .id(id)
                        .name(name)
                        .description(description)
                        .price(price)
                        .duration(duration)
                        .tags(new HashSet<>())
                        .build()
        );

        when(dao.getAllBy(criteria)).thenReturn(certificateList);
        when(mapper.toDtoList(certificateList)).thenReturn(expected);
        List<CertificateDto> actualCertificates = service.getAllBy(criteria);
        assertEquals(expected.get(0), actualCertificates.get(Math.toIntExact(id - 1)));
        verify(dao, times(1)).getAllBy(criteria);
    }

    @Test
    void testCertificateDtoBuilder() {
        CertificateDto certificateDto = CertificateDto.builder()
                .id(1L)
                .name("Test Certificate")
                .description("Test description")
                .duration(5)
                .price(BigDecimal.valueOf(10.0))
                .createDate(Instant.now())
                .lastUpdateDate(Instant.now())
                .build();

        assertEquals(Long.valueOf(1L), certificateDto.getId());
        assertEquals("Test Certificate", certificateDto.getName());
        assertEquals("Test description", certificateDto.getDescription());
        assertEquals(Integer.valueOf(5), certificateDto.getDuration());
        assertEquals(BigDecimal.valueOf(10.0), certificateDto.getPrice());
        assertNotNull(certificateDto.getCreateDate());
        assertNotNull(certificateDto.getLastUpdateDate());
    }

    @Test
    void testCertificateIsExistsException() {
        CertificateIsExistsException exception = new CertificateIsExistsException("Test Certificate");
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
        when(dao.getByName("Existing Certificate")).thenReturn(existingCertificate);

        assertThrows(CertificateIsExistsException.class, () -> service.save(certificateDto));
        verify(dao, never()).save(existingCertificate);
    }

}