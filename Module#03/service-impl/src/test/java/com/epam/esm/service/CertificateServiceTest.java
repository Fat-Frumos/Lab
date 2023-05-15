package com.epam.esm.service;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.criteria.FilterParams;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.CertificateDaoImpl;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificateWithoutTagDto;
import com.epam.esm.dto.TagDto;
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

import javax.swing.SortOrder;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
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
    Criteria criteria;

    @BeforeEach
    public void setUp() {
        criteria = Criteria.builder().page(0).size(25).build();
        service = new CertificateServiceImpl(certificateDao, certificateMapper);

        certificate = Certificate.builder().id(1L).name("testCertificate").build();
        certificateDto = CertificateDto.builder()
                .id(1L).name("Test Certificate")
                .description("Test description")
                .duration(10)
                .price(BigDecimal.valueOf(100))
                .createDate(Timestamp.from(Instant.now()))
                .lastUpdateDate(Timestamp.from(Instant.now()))
                .build();

        certificateWithoutTagDtos = new ArrayList<>();
        certificateWithoutTagDtos.add(CertificateWithoutTagDto.builder().id(1L).name("Gift1")
                .description("Certificate1").duration(10)
                .price(BigDecimal.valueOf(100)).build());
        certificateWithoutTagDtos.add(CertificateWithoutTagDto.builder().id(2L).name("Gift2")
                .description("Certificate2").duration(10)
                .price(BigDecimal.valueOf(100)).build());
        certificateWithoutTagDtos.add(CertificateWithoutTagDto.builder().id(3L).name("Gift3")
                .description("Certificate3").duration(10)
                .price(BigDecimal.valueOf(100)).build());

        certificateDtos = new ArrayList<>();
        certificateDtos.add(CertificateDto.builder().id(1L).name("Gift1").description("Certificate1").build());
        certificateDtos.add(CertificateDto.builder().id(2L).name("Gift2").description("Certificate2").build());
        certificateDtos.add(CertificateDto.builder().id(3L).name("Gift3").description("Certificate3").build());

        certificates = new ArrayList<>();
        certificates.add(Certificate.builder().id(1L).name("Gift1").duration(10)
                .description("Certificate1").price(new BigDecimal(100))
                .tags(new HashSet<>()).build());
        certificates.add(Certificate.builder().id(2L).name("Gift2").duration(20)
                .description("Certificate2").price(new BigDecimal(200))
                .tags(new HashSet<>()).build());
        certificates.add(Certificate.builder().id(3L).name("Gift3")
                .description("Certificate3").duration(30).price(new BigDecimal(300))
                .tags(new HashSet<>()).build());
    }

    @Test
    @DisplayName("Throw CertificateNotFoundException when updating non-existent certificate")
    void testUpdateNonExistentCertificate() {
        Long id = 1L;
        CertificateDto dto = CertificateDto.builder()
                .id(id)
                .name("Certificate 1")
                .description("Certificate 1 description")
                .duration(30)
                .price(BigDecimal.valueOf(100.00))
                .build();
        when(certificateDao.getById(id)).thenReturn(Optional.empty());
        assertThrows(CertificateNotFoundException.class, () -> service.update(dto, id));
        verify(certificateDao).getById(id);
    }


    @ParameterizedTest
    @CsvSource({"1", "2", "3"})
    @DisplayName("Find tags by certificate id")
    void testFindTagsByCertificateId(Long id) {
        List<TagDto> expected = new ArrayList<>();
        expected.add(TagDto.builder().id(id).build());
        when(certificateDao.findTagsByCertificateId(id)).thenReturn(expected);
        List<TagDto> result = service.findTagsByCertificateId(id);

        assertEquals(expected, result);

        verify(certificateDao).findTagsByCertificateId(id);
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

    @ParameterizedTest
    @CsvSource({"5, Winter", "6, Summer", "7, Spring", "8, Autumn"})
    @DisplayName("Update certificate when not found")
    void updateNotFound(Long id, String name) {
        CertificateDto certificateDto = CertificateDto.builder().id(id).name(name).build();
        when(certificateDao.getById(id)).thenReturn(Optional.empty());
        assertThrows(CertificateNotFoundException.class,
                () -> service.update(certificateDto, id),
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

    @ParameterizedTest
    @CsvSource({"1, Winter, 1, 25", "2, Summer, 2, 50", "3, Spring, 3, 75", "4, Autumn, 4, 100"})
    @DisplayName("Get all certificates without tags")
    void getAllWithoutTags(int id, String tagName, int page, int size) {
        Criteria criteria = Criteria.builder()
                .filterParams(FilterParams.ID)
                .build();
        criteria.addParam(FilterParams.ID, id);
        criteria.addParam(FilterParams.PAGE, page);
        criteria.addParam(FilterParams.TAGS, tagName);
        criteria.addParam(FilterParams.SIZE, size);

        when(certificateDao.getAll(criteria)).thenReturn(certificates);
        when(certificateMapper.toDtoWithoutTagsList(certificates)).thenReturn(certificateWithoutTagDtos);
        List<CertificateWithoutTagDto> result = service.getAllWithoutTags(criteria);
        when(certificateDao.getAll()).thenReturn(certificates);
        when(certificateMapper.toDtoWithoutTagsList(certificates)).thenReturn(certificateWithoutTagDtos);
        verify(certificateDao).getAll(criteria);
        verify(certificateMapper).toDtoWithoutTagsList(certificates);
        assertEquals(certificateWithoutTagDtos, result);
    }

    @ParameterizedTest
    @CsvSource({"ID,1,2", "PAGE,1,2", "SIZE,25,50", "ORDER,0,1"})
    @DisplayName("Get all certificates without tags")
    void getAllWithoutTag(String name, int a, int b) {
        Criteria criteria = Criteria.builder()
                .page(a)
                .size(b)
                .sortOrder(SortOrder.ASCENDING)
                .filterParams(FilterParams.valueOf(name))
                .build();
        when(certificateDao.getAll(criteria)).thenReturn(certificates);
        when(certificateMapper.toDtoWithoutTagsList(certificates)).thenReturn(certificateWithoutTagDtos);
        List<CertificateWithoutTagDto> result = service.getAllWithoutTags(criteria);
        assertEquals(certificateWithoutTagDtos, result);
        verify(certificateDao).getAll(criteria);
        verify(certificateMapper).toDtoWithoutTagsList(certificates);
        assertEquals(FilterParams.valueOf(name), criteria.getFilterParams());
    }


    @Test
    @DisplayName("Should update certificate when certificate exists")
    void testUpdateShouldUpdateCertificate() {
        when(certificateDao.getById(certificateDtos.get(0).getId())).thenReturn(Optional.of(certificates.get(0)));
        when(certificateMapper.toEntity(certificateDtos.get(0))).thenReturn(certificates.get(0));
        when(certificateDao.update(certificates.get(0), 1L)).thenReturn(certificate);
        assertNotNull(certificateDao.update(certificateMapper.toEntity(certificateDtos.get(0)), 1L));
        verify(certificateMapper, times(1)).toEntity(certificateDtos.get(0));
        verify(certificateDao, times(1)).update(certificates.get(0), 1L);
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
        when(service.getAllWithoutTags(criteria))
                .thenReturn(certificateWithoutTagDtos);
        List<CertificateWithoutTagDto> dtos = service.getAllWithoutTags(criteria);
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
