package com.epam.esm.service;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.CertificateDaoImpl;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.mapper.CertificateMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
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
    List<CertificateDto> certificateDtoList = new ArrayList<>();
    Certificate certificate;
    CertificateDto certificateDto;
    Pageable pageable;

    @BeforeEach
    public void setUp() {

        pageable = PageRequest.of(0, 25, Sort.by("name").ascending());
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

        certificateDtoList.add(CertificateDto.builder().id(1L).name("Gift1")
                .description("Certificate1").duration(10)
                .price(BigDecimal.valueOf(100)).build());
        certificateDtoList.add(CertificateDto.builder().id(2L).name("Gift2")
                .description("Certificate2").duration(10)
                .price(BigDecimal.valueOf(100)).build());
        certificateDtoList.add(CertificateDto.builder().id(3L).name("Gift3")
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
        when(certificateMapper.toDto(certificate)).thenReturn(certificateDto);
        service.delete(id);
        verify(certificateDao).delete(id);
    }

    @ParameterizedTest
    @CsvSource({"1, Winter, 1, 25", "2, Summer, 2, 50", "3, Spring, 3, 75", "4, Autumn, 4, 100"})
    @DisplayName("Get all certificates without tags")
    void getAllWithoutTags(int id, String tagName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, tagName));
        when(certificateDao.getAll(pageable)).thenReturn(certificates);
        when(certificateMapper.toDtoList(certificates)).thenReturn(certificateDtos);
        Page<CertificateDto> result = service.getAllWithoutTags(pageable);
        when(certificateDao.getAll(pageable)).thenReturn(certificates);
        when(certificateMapper.toDtoList(certificates)).thenReturn(certificateDtos);
        verify(certificateDao).getAll(pageable);
        verify(certificateMapper).toDtoList(certificates);
        assertEquals(certificateDtos, result.getContent());
    }

    @ParameterizedTest
    @CsvSource({"ID,1,2", "PAGE,1,2", "SIZE,25,50", "ORDER,0,1"})
    @DisplayName("Get all certificates without tags")
    void getAllWithoutTag(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, name));
        when(certificateDao.getAll(pageable)).thenReturn(certificates);
        when(certificateMapper.toDtoList(certificates)).thenReturn(certificateDtos);
        Page<CertificateDto> result = service.getAllWithoutTags(pageable);
        assertEquals(certificateDtos, result.getContent());
        verify(certificateDao).getAll(pageable);
        verify(certificateMapper).toDtoList(certificates);
    }

    @Test
    @DisplayName("Should update certificate when certificate exists")
    void testUpdateShouldUpdateCertificate() {
        when(certificateDao.getById(certificateDtos.get(0).getId())).thenReturn(Optional.of(certificates.get(0)));
        when(certificateMapper.toEntity(certificateDtos.get(0))).thenReturn(certificates.get(0));
        when(certificateDao.update(certificates.get(0))).thenReturn(certificate);
        assertNotNull(certificateDao.update(certificateMapper.toEntity(certificateDtos.get(0))));
        verify(certificateMapper, times(1)).toEntity(certificateDtos.get(0));
        verify(certificateDao, times(1)).update(certificates.get(0));
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

    @DisplayName("Test Get Certificate By Name")
    @ParameterizedTest(name = "Run {index}: name = {0}")
    @CsvSource({
            "1, Java, description, 10, 30",
            "2, SQL, description, 10, 30",
            "3, Programming, description, 10, 30",
            "4, PorsgreSQL, description, 10, 30",
            "5, Spring, description, 10, 30"
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

    @ParameterizedTest
    @CsvSource({
            "1, Java, description, 10, 30",
            "2, SQL, description, 10, 30",
            "3, Programming, description, 10, 30",
            "4, PorsgreSQL, description, 10, 30",
            "5, Spring, description, 10, 30"
    })
    void testFindCertificatesByTags(long id, String name, String description, BigDecimal price, int duration) {
        List<String> tagNames = Arrays.asList("Tag1", "Tag2", "Tag3");

        Certificate certificate = Certificate.builder()
                .id(id).description(description)
                .duration(duration).price(price).name(name).build();
        CertificateDto certificateDto = CertificateDto.builder()
                .id(id).name(name).description(description)
                .duration(duration).price(price).build();

        List<Certificate> certificates = Collections.singletonList(certificate);
        List<CertificateDto> expectedDtos = Collections.singletonList(certificateDto);
        when(certificateDao.findByTagNames(tagNames)).thenReturn(certificates);
        when(certificateMapper.toDtoList(certificates)).thenReturn(expectedDtos);

        Page<CertificateDto> result = service.findCertificatesByTags(tagNames);

        assertEquals(expectedDtos.size(), result.getContent().size());
    }

    @ParameterizedTest
    @CsvSource({
            "1, Java, description, 10, 30",
            "2, SQL, description, 10, 30",
            "3, Programming, description, 10, 30",
            "4, PorsgreSQL, description, 10, 30",
            "5, Spring, description, 10, 30"
    })
    void getCertificatesByUserIdTest(
            long id, String name, String description, BigDecimal price, int duration) {

        Certificate certificate = Certificate.builder()
                .id(id).description(description)
                .duration(duration).price(price).name(name).build();
        CertificateDto certificateDto = CertificateDto.builder()
                .id(id).name(name).description(description)
                .duration(duration).price(price).build();

        List<Certificate> certificates = Collections.singletonList(certificate);
        List<CertificateDto> expectedDtos = Collections.singletonList(certificateDto);
        when(certificateDao.getCertificatesByUserId(anyLong())).thenReturn(certificates);
        when(certificateMapper.toDtoList(Collections.singletonList(certificate))).thenReturn(expectedDtos);

        Page<CertificateDto> actualCertificates = service.getCertificatesByUserId(id);
        assertEquals(expectedDtos, actualCertificates.getContent());
    }

    @ParameterizedTest(name = "Save {index}: name = {0}")
    @CsvSource({
            "1, Java, description, 10, 30",
            "2, SQL, description, 10, 30",
            "3, Programming, description, 10, 30",
            "4, PorsgreSQL, description, 10, 30",
            "5, Spring, description, 10, 30"
    })
    void testSaveCertificate(Long id, String name, String description, BigDecimal price, int duration) {

        Certificate certificate = Certificate.builder()
                .id(id).description(description)
                .duration(duration).price(price).name(name).build();
        CertificateDto certificateDto = CertificateDto.builder()
                .id(id).name(name).description(description)
                .duration(duration).price(price).build();

        when(certificateDao.save(certificate)).thenReturn(certificate);
        when(certificateMapper.toEntity(certificateDto)).thenReturn(certificate);
        when(certificateMapper.toDto(certificate)).thenReturn(certificateDto);
        CertificateDto actual = service.save(certificateDto);
        verify(certificateDao).save(certificate);
        verify(certificateMapper).toEntity(certificateDto);
        verify(certificateMapper).toDto(certificate);
        assertEquals(certificateDto, actual);
    }

    @ParameterizedTest(name = "Update {index}: name = {1}")
    @CsvSource({
            "1, Java, description, 10, 30",
            "2, SQL, description, 10, 30",
            "3, Programming, description, 10, 30",
            "4, PorsgreSQL, description, 10, 30",
            "5, Spring, description, 10, 30"
    })
    void testUpdateCertificates(Long id, String name, String description, BigDecimal price, int duration) {
        Certificate certificate = Certificate.builder()
                .id(id).description(description)
                .duration(duration).price(price).name(name).build();
        CertificateDto certificateDto = CertificateDto.builder()
                .id(id).name(name).description(description)
                .duration(duration).price(price).build();
        when(certificateDao.update(certificate)).thenReturn(certificate);
        when(certificateMapper.toEntity(certificateDto)).thenReturn(certificate);
        when(certificateMapper.toDto(certificate)).thenReturn(certificateDto);

        CertificateDto result = service.update(certificateDto);

        verify(certificateDao).update(certificate);
        verify(certificateMapper).toEntity(certificateDto);
        verify(certificateMapper).toDto(certificate);
        assertEquals(certificateDto, result);
    }

    @ParameterizedTest
    @CsvSource({
            "1, Java, description, 10, 30",
            "2, SQL, description, 10, 30",
            "3, Programming, description, 10, 30",
            "4, PorsgreSQL, description, 10, 30",
            "5, Spring, description, 10, 30"
    })
    void getByIdsTest(long id, String name, String description, BigDecimal price, int duration) {

        Certificate certificate = Certificate.builder()
                .id(id).description(description)
                .duration(duration).price(price).name(name).build();
        CertificateDto certificateDto = CertificateDto.builder()
                .id(id).name(name).description(description)
                .duration(duration).price(price).build();
        Set<Long> ids = new HashSet<>();
        ids.add(1L);
        ids.add(2L);
        List<Certificate> certificates = Collections.singletonList(certificate);
        List<CertificateDto> expectedDtos = Collections.singletonList(certificateDto);

        when(certificateDao.findAllByIds(ids)).thenReturn(new HashSet<>(certificates));
        when(certificateMapper.toDtoList(certificates)).thenReturn(expectedDtos);

        Page<CertificateDto> actualCertificateDtos = service.getByIds(ids);

        assertEquals(new HashSet<>(expectedDtos), new HashSet<>(actualCertificateDtos.getContent()));
        verify(certificateDao).findAllByIds(ids);
        verify(certificateMapper).toDtoList(certificates);
    }
}
