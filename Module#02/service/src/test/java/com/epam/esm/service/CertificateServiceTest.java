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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    private final CertificateService certificateService = new CertificateServiceImpl(certificateDao, certificateMapper);
    List<Certificate> certificates;
    List<CertificateDto> certificateDtos;
    List<CertificateWithoutTagDto> certificateWithoutTagDtos;

    @BeforeEach
    public void setUp() {
        certificateWithoutTagDtos = List.of(
                CertificateWithoutTagDto.builder().id(1L).name("Gift1").description("Certificate1").build(),
                CertificateWithoutTagDto.builder().id(2L).name("Gift2").description("Certificate2").build(),
                CertificateWithoutTagDto.builder().id(3L).name("Gift3").description("Certificate3").build()
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
    void getById(Long id, String name) {
        CertificateDto certificateDto = CertificateDto.builder().id(id).name(name).build();
        Certificate certificate = Certificate.builder().id(id).name(name).build();
        when(certificateDao.getById(id)).thenReturn(certificate);
        when(certificateMapper.toDto(certificate)).thenReturn(certificateDto);
        CertificateDto result = certificateService.getById(id);
        assertEquals(certificateDto, result);
    }

    @Test
    @DisplayName("Get all certificates")
    void getAll() {
        when(certificateDao.getAll()).thenReturn(certificates);
        when(certificateMapper.toDtoList(certificates)).thenReturn(certificateDtos);
        assertEquals(certificateDtos, certificateService.getAll());
        verify(certificateDao).getAll();
        verify(certificateMapper).toDtoList(certificates);
    }

    @ParameterizedTest
    @CsvSource({"1, Winter", "2, Summer", "3, Spring", "4, Autumn"})
    @DisplayName("Get certificate by name")
    void getByName(Long id, String name) {
        CertificateDto certificateDto = CertificateDto.builder().id(id).name(name).build();
        Certificate certificate = Certificate.builder().id(id).name(name).build();
        when(certificateDao.getByName(name)).thenReturn(certificate);
        when(certificateMapper.toDto(certificate)).thenReturn(certificateDto);
        CertificateDto result = certificateService.getByName(name);
        assertEquals(certificateDto, result);
        verify(certificateDao).getByName(name);
        verify(certificateMapper).toDto(certificate);
    }

    @ParameterizedTest
    @CsvSource({"1", "2", "3"})
    @DisplayName("Delete certificate by id when found")
    void deleteFound(Long id) {
        CertificateDto certificateDto = CertificateDto.builder().id(id).build();
        Certificate certificate = Certificate.builder().id(id).build();
        when(certificateDao.getById(id)).thenReturn(certificate);
        when(certificateMapper.toDto(certificate)).thenReturn(certificateDto);
        certificateService.delete(id);
        verify(certificateDao).delete(id);
    }

    @ParameterizedTest
    @CsvSource({"1, Winter", "2, Summer", "3, Spring", "4, Autumn"})
    @DisplayName("Delete certificate by id when not found")
    void deleteNotFound(Long id) {
        when(certificateService.getById(id)).thenReturn(null);
        assertThrows(CertificateNotFoundException.class, () ->
                        certificateService.delete(id),
                "CertificateNotFoundException"
        );
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
        assertEquals(certificateDtos, certificateService.getAllBy(criteria));
        verify(certificateDao).getAllBy(criteria);
        verify(certificateMapper).toDtoList(certificates);
    }

    @ParameterizedTest
    @CsvSource({"5, Winter", "6, Summer", "7, Spring", "8, Autumn"})
    @DisplayName("Update certificate when not found")
    void updateNotFound(Long id, String name) {
        CertificateDto certificateDto = CertificateDto.builder().id(id).name(name).build();
        when(certificateDao.getById(id)).thenReturn(null);
        assertThrows(CertificateNotFoundException.class,
                () -> certificateService.update(certificateDto),
                "CertificateNotFoundException"
        );
    }

    @ParameterizedTest
    @CsvSource({"5, Winter", "6, Summer", "7, Spring", "8, Autumn"})
    @DisplayName("Save certificate when not exists")
    void saveNotExists(Long id, String name) {
        CertificateDto certificateDto = CertificateDto.builder().id(id).name(name).build();
        Certificate certificate = Certificate.builder().id(id).name(name).build();
        when(certificateDao.getByName(name)).thenReturn(null);
        when(certificateService.getById(id)).thenReturn(certificateDto);
        when(certificateMapper.toEntity(certificateDto)).thenReturn(certificate);
        when(certificateDao.save(certificate)).thenReturn(id);
        assertEquals(certificateDto, certificateService.save(certificateDto));
        verify(certificateDao).getByName(name);
        verify(certificateMapper).toEntity(certificateDto);
        verify(certificateDao).save(certificate);
    }

    @ParameterizedTest
    @CsvSource({"1, Winter", "2, Summer", "3, Spring", "4, Autumn"})
    @DisplayName("Save certificate when already exists")
    void saveAlreadyExists(Long id, String name) {
        CertificateDto certificateDto = CertificateDto.builder().id(id).name(name).build();
        Certificate certificate = Certificate.builder().id(id).name(name).build();
        when(certificateDao.getByName(name)).thenReturn(certificate);
        assertThrows(CertificateAlreadyExistsException.class,
                () -> certificateService.save(certificateDto),
                "CertificateAlreadyExistsException"
        );
    }

    @Test
    @DisplayName("Get all certificates without tags")
    void getAllWithoutTags() {
        when(certificateDao.getAll()).thenReturn(certificates);
        when(certificateMapper.toDtoWithoutTagsList(certificates)).thenReturn(certificateWithoutTagDtos);
        List<CertificateWithoutTagDto> result = certificateService.getAllWithoutTags();
        assertEquals(certificateWithoutTagDtos, result);
        verify(certificateDao).getAll();
        verify(certificateMapper).toDtoWithoutTagsList(certificates);
    }

    @Test
    @DisplayName("Should update certificate when certificate exists")
    void testUpdateShouldUpdateCertificate() {
        when(certificateDao.getById(certificateDtos.get(0).getId())).thenReturn(certificates.get(0));
        when(certificateMapper.toEntity(certificateDtos.get(0))).thenReturn(certificates.get(0));
        when(certificateDao.update(certificates.get(0))).thenReturn(true);
        assertTrue(certificateDao.update(certificateMapper.toEntity(certificateDtos.get(0))));
        verify(certificateMapper, times(1)).toEntity(certificateDtos.get(0));
        verify(certificateDao, times(1)).update(certificates.get(0));
    }

    @Test
    @DisplayName("Should throw CertificateNotFoundException when certificate doesn't exist")
    void testUpdateShouldThrowCertificateNotFoundException() {
        when(certificateDao.getById(certificateDtos.get(0).getId())).thenReturn(null);
        assertThrows(CertificateNotFoundException.class, () -> certificateService.update(certificateDtos.get(0)));
        verify(certificateDao, times(1)).getById(certificateDtos.get(0).getId());
        verify(certificateMapper, never()).toEntity(certificateDtos.get(0));
        verify(certificateDao, never()).update(any(Certificate.class));
    }


    @Test
    void testCertificateDtoBuilder() {
        CertificateDto certificateDto = CertificateDto.builder()
                .id(1L).name("Test Certificate")
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
}
