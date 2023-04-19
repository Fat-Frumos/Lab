package com.epam.esm.service;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.criteria.SortOrder;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.CertificateDaoImpl;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificateWithoutTagDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.exception.CertificateAlreadyExistsException;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.mapper.CertificateMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CertificateServiceTest {
    @Mock
    private CertificateDao certificateDao = mock(CertificateDaoImpl.class);
    @Mock
    private CertificateMapper certificateMapper = mock(CertificateMapper.class);
    @InjectMocks
    private CertificateService service;
    public List<Certificate> certificates;
    List<CertificateDto> certificateDtos;
    List<CertificateWithoutTagDto> certificateWithoutTagDtos;
    Certificate certificate;
    CertificateDto certificateDto;

    @BeforeEach
    public void setUp() {
        service = new CertificateServiceImpl(certificateDao, certificateMapper);

        certificate = Certificate.builder().id(1L).name("testCertificate").build();
        certificateDto = CertificateDto.builder()
                .id(1L).name("Test Certificate")
                .description("Test description")
                .duration(10)
                .price(BigDecimal.valueOf(100))
                .createDate(Instant.now())
                .lastUpdateDate(Instant.now())
                .build();

        certificateWithoutTagDtos = List.of(
                CertificateWithoutTagDto.builder().id(1L).name("Gift1")
                        .description("Certificate1").duration(10)
                        .price(BigDecimal.valueOf(100)).build(),
                CertificateWithoutTagDto.builder().id(2L).name("Gift2")
                        .description("Certificate2").duration(10)
                        .price(BigDecimal.valueOf(100)).build(),
                CertificateWithoutTagDto.builder().id(3L).name("Gift3")
                        .description("Certificate3").duration(10)
                        .price(BigDecimal.valueOf(100)).build()
        );

        certificateDtos = List.of(
                CertificateDto.builder().id(1L).name("Gift1").description("Certificate1").build(),
                CertificateDto.builder().id(2L).name("Gift2").description("Certificate1").build(),
                CertificateDto.builder().id(3L).name("Gift3").description("Certificate1").build()
        );

        certificates = List.of(
                Certificate.builder().id(1L).name("Gift1").duration(10)
                        .description("Certificate1").price(new BigDecimal(100))
                        .tags(new HashSet<>()).build(),

                Certificate.builder().id(2L).name("Gift2").duration(20)
                        .description("Certificate2").price(new BigDecimal(200))
                        .tags(new HashSet<>()).build(),

                Certificate.builder().id(3L).name("Gift3")
                        .description("Certificate3").duration(30).price(new BigDecimal(300))
                        .tags(new HashSet<>()).build()
        );
    }


    @ParameterizedTest
    @CsvSource({"1, Winter", "2, Summer", "3, Spring", "4, Autumn"})
    @DisplayName("Get certificate by id")
    void testGetById(Long id, String name) {
        CertificateDto certificateDto = CertificateDto.builder().id(id).name(name).build();
        Certificate certificate = Certificate.builder().id(id).name(name).build();
        when(certificateDao.getById(id)).thenReturn(Optional.of(certificate));
        when(certificateMapper.toDto(certificate)).thenReturn(certificateDto);
        CertificateDto result = service.getById(id);
        assertEquals(certificateDto, result);
    }

    @Test
    @DisplayName("Get all certificates")
    void getAll() {
        when(certificateDao.getAll()).thenReturn(certificates);
        when(certificateMapper.toDtoList(certificates)).thenReturn(certificateDtos);
        assertEquals(certificateDtos, service.getAll());
        verify(certificateDao).getAll();
        verify(certificateMapper).toDtoList(certificates);
    }

    @ParameterizedTest
    @CsvSource({"1, Winter", "2, Summer", "3, Spring", "4, Autumn"})
    @DisplayName("Get certificate by name")
    void getByName(Long id, String name) {
        CertificateDto certificateDto = CertificateDto.builder().id(id).name(name).build();
        Optional<Certificate> certificate = Optional.of(Certificate.builder().id(id).name(name).build());
        when(certificateDao.getByName(name)).thenReturn(certificate);
        when(certificateMapper.toDto(certificate.get())).thenReturn(certificateDto);
        CertificateDto result = service.getByName(name);
        assertEquals(certificateDto, result);
        verify(certificateDao).getByName(name);
        verify(certificateMapper).toDto(certificate.get());
    }

    @ParameterizedTest
    @CsvSource({"1", "2", "3"})
    @DisplayName("Delete certificate by id when found")
    void deleteFound(Long id) {
        CertificateDto certificateDto = CertificateDto.builder().id(id).build();
        Certificate certificate = Certificate.builder().id(id).build();
        when(certificateDao.getById(id)).thenReturn(Optional.of(certificate));
        when(certificateMapper.toDto(certificate)).thenReturn(certificateDto);
        service.delete(id);
        verify(certificateDao).delete(id);
    }


    @Test
    @DisplayName("Get all certificates by criteria")
    void getAllBy() {
        Criteria criteria = Criteria.builder()
                .name("Gift")
                .date(Instant.now())
                .tagName("Criteria")
                .description("Certificate")
                .sortOrder(SortOrder.ASC)
                .build();
        when(certificateDao.getAllBy(criteria)).thenReturn(certificates);
        when(certificateMapper.toDtoList(certificates)).thenReturn(certificateDtos);
        assertEquals(certificateDtos, service.getAllBy(criteria));
        verify(certificateDao).getAllBy(criteria);
        verify(certificateMapper).toDtoList(certificates);
    }

    @ParameterizedTest
    @CsvSource({"5, Winter", "6, Summer", "7, Spring", "8, Autumn"})
    @DisplayName("Update certificate when not found")
    void updateNotFound(Long id, String name) {
        CertificateDto certificateDto = CertificateDto.builder().id(id).name(name).build();
        when(certificateDao.getById(id)).thenReturn(Optional.empty());
        assertThrows(CertificateNotFoundException.class,
                () -> service.update(certificateDto),
                "CertificateNotFoundException"
        );
    }


    @ParameterizedTest
    @CsvSource({"1, Winter", "2, Summer", "3, Spring", "4, Autumn"})
    @DisplayName("Save certificate when already exists")
    void saveAlreadyExists(Long id, String name) {
        CertificateDto certificateDto = CertificateDto.builder().id(id).name(name).build();
        Certificate certificate = Certificate.builder().id(id).name(name).build();
        when(certificateDao.getByName(name)).thenReturn(Optional.of(certificate));
        assertThrows(CertificateAlreadyExistsException.class,
                () -> service.save(certificateDto),
                "CertificateAlreadyExistsException"
        );
    }

    @Test
    @DisplayName("Get all certificates without tags")
    void getAllWithoutTags() {
        when(certificateDao.getAll()).thenReturn(certificates);
        when(certificateMapper.toDtoWithoutTagsList(certificates)).thenReturn(certificateWithoutTagDtos);
        List<CertificateWithoutTagDto> result = service.getAllWithoutTags();
        assertEquals(certificateWithoutTagDtos, result);
        verify(certificateDao).getAll();
        verify(certificateMapper).toDtoWithoutTagsList(certificates);
    }

    @Test
    @DisplayName("Should update certificate when certificate exists")
    void testUpdateShouldUpdateCertificate() {
        when(certificateDao.getById(certificateDtos.get(0).getId())).thenReturn(Optional.of(certificates.get(0)));
        when(certificateMapper.toEntity(certificateDtos.get(0))).thenReturn(certificates.get(0));
        when(certificateDao.update(certificates.get(0))).thenReturn(true);
        assertTrue(certificateDao.update(certificateMapper.toEntity(certificateDtos.get(0))));
        verify(certificateMapper, times(1)).toEntity(certificateDtos.get(0));
        verify(certificateDao, times(1)).update(certificates.get(0));
    }

    @Test
    @DisplayName("Should throw CertificateNotFoundException when certificate doesn't exist")
    void testUpdateShouldThrowCertificateNotFoundException() {
        when(certificateDao.getById(certificateDtos.get(0).getId())).thenReturn(Optional.empty());
        assertThrows(CertificateNotFoundException.class, () -> service.update(certificateDtos.get(0)));
        verify(certificateDao, times(1)).getById(certificateDtos.get(0).getId());
        verify(certificateMapper, never()).toEntity(certificateDtos.get(0));
        verify(certificateDao, never()).update(any(Certificate.class));
    }

    @Test
    @DisplayName("Test Certificate Dto Builder")
    void testCertificateDtoBuilder() {
        assertEquals(Long.valueOf(1L), certificateDto.getId());
        assertEquals("Test Certificate", certificateDto.getName());
        assertEquals("Test description", certificateDto.getDescription());
        assertEquals(Integer.valueOf(10), certificateDto.getDuration());
        assertEquals(BigDecimal.valueOf(100), certificateDto.getPrice());
        assertNotNull(certificateDto.getCreateDate());
        assertNotNull(certificateDto.getLastUpdateDate());
    }

    @ParameterizedTest
    @CsvSource({"1, Winter", "2, Summer", "3, Spring", "4, Autumn"})
    @DisplayName("Get certificate by name")
    void getCertificateByName(Long id, String name) {

        Certificate certificate = Certificate.builder().id(id).name(name).build();
        CertificateDto certificateDto = CertificateDto.builder().id(id).name(name).build();
        when(certificateDao.getByName(name)).thenReturn(Optional.of(certificate));
        when(certificateMapper.toDto(certificate)).thenReturn(certificateDto);
        CertificateDto result = service.getByName(name);

        verify(certificateDao, times(1)).getByName(name);
        verify(certificateMapper, times(1)).toDto(certificate);
        assertEquals(certificateDto, result);
    }

    @Test
    @DisplayName("Get certificate by non-existent name")
    void getCertificateByNonExistentName() {
        when(certificateDao.getByName("NonExistentCertificate"))
                .thenReturn(Optional.empty());
        assertThrows(CertificateNotFoundException.class, () ->
                service.getByName("NonExistentCertificate"));
        verify(certificateDao, times(1))
                .getByName("NonExistentCertificate");
    }

    @ParameterizedTest
    @CsvSource({"1, Winter", "2, Summer", "3, Spring", "4, Autumn"})
    @DisplayName("Delete certificate with invalid id throws CertificateNotFoundException")
    void deleteCertificateWithInvalidId(Long id, String name) {
        when(certificateDao.getById(id))
                .thenReturn(Optional.empty());
        assertThrows(CertificateNotFoundException.class, () ->
                service.delete(id));
        verify(certificateDao).getById(id);
        verify(certificateDao, never()).delete(anyLong());

        when(certificateDao.getByName(name))
                .thenReturn(Optional.empty());
        assertThrows(CertificateNotFoundException.class, () ->
                service.delete(id));
        verify(certificateDao, never()).delete(anyLong());
    }

    @Test
    @DisplayName("getAllWithoutTags() method should return a list of certificates without tags")
    void testGetAllWithoutTags() {
        when(certificateDao.getAll()).thenReturn(certificates);
        when(service.getAllWithoutTags())
                .thenReturn(certificateWithoutTagDtos);
        List<CertificateWithoutTagDto> dtos = service.getAllWithoutTags();
        assertEquals(certificates.size(), dtos.size());
        for (int i = 0; i < dtos.size(); i++) {
            CertificateWithoutTagDto dto = dtos.get(i);
            Certificate certificate = certificates.get(i);
            assertEquals(certificate.getId(), dto.getId());
            assertEquals(certificate.getName(), dto.getName());
            assertEquals(certificate.getDuration(), dto.getDuration() * (i + 1));
            assertEquals(certificate.getDescription(), dto.getDescription());
            assertEquals(certificate.getPrice(), dto.getPrice().multiply(BigDecimal.valueOf((i + 1))));
        }
    }

    @DisplayName("Test Get Certificate By Name")
    @ParameterizedTest(name = "Run {index}: name = {0}")
    @CsvSource({
            "1, Gift, description, 10, 30",
            "2, Certificate, description, 10, 30",
            "3, Java, description, 10, 30",
            "4, SQL, description, 10, 30",
            "5, Programming, description, 10, 30",
            "6, Spring, description, 10, 30"
    })
    void getCertificateByNames(Long id, String name, String description, BigDecimal price, int duration) {
        Certificate certificate = Certificate.builder()
                .id(id).description(description)
                .duration(duration).price(price).name(name).build();
        CertificateDto certificateDto = CertificateDto.builder()
                .id(id).name(name).description(description)
                .duration(duration).price(price).build();
        when(certificateDao.getByName(name)).thenReturn(Optional.of(certificate));
        when(certificateMapper.toDto(certificate)).thenReturn(certificateDto);
        CertificateDto dto = service.getByName(name);

        assertNotNull(certificateDto);
        assertEquals(name, certificateDto.getName());
        assertEquals("description", certificateDto.getDescription());
        assertEquals(new BigDecimal("10"), certificateDto.getPrice());
        assertEquals(30, certificateDto.getDuration());
        assertEquals(certificateDto, dto);
        verify(certificateDao, times(1)).getByName(name);
        verify(certificateMapper, times(1)).toDto(certificate);
    }
}
