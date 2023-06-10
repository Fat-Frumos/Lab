package com.epam.esm.service;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.CertificateDaoImpl;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificateSlimDto;
import com.epam.esm.dto.PatchCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.mapper.CertificateMapper;
import com.epam.esm.mapper.TagMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    @Mock
    private TagMapper tagMapper = mock(TagMapper.class);
    @InjectMocks
    private CertificateService service;
    public List<Certificate> certificates;
    Page<CertificateDto> certificateDtos;
    List<CertificateDto> certificateDtoList = new ArrayList<>();
    List<CertificateSlimDto> slimDtos = new ArrayList<>();
    Page<CertificateSlimDto> slim;
    Certificate certificate;
    CertificateDto certificateDto;
    Pageable pageable;
    List<Tag> tags = Arrays.asList(
            Tag.builder().name("tag1").build(),
            Tag.builder().name("tag2").build());

    List<String> tagNames = Arrays.asList("tag1", "tag2");
    List<Certificate> expectedCertificates = Arrays.asList(
            Certificate.builder().id(1L).name("certificate1").build(),
            Certificate.builder().id(2L).name("certificate2").build());
    List<CertificateDto> expectedCertificateDtos = Arrays.asList(
            CertificateDto.builder().id(1L).name("certificate1").build(),
            CertificateDto.builder().id(2L).name("certificate2").build());

    @BeforeEach
    public void setUp() {

        pageable = PageRequest.of(0, 25, Sort.by("name").ascending());
        service = new CertificateServiceImpl(certificateDao, certificateMapper, tagMapper);

        certificate = Certificate.builder().id(1L).name("Gift").name("Certificate").build();
        certificateDto = CertificateDto.builder()
                .id(1L).name("Test Certificate")
                .description("Test description")
                .duration(10)
                .price(BigDecimal.valueOf(100))
                .createDate(Timestamp.from(Instant.now()))
                .lastUpdateDate(Timestamp.from(Instant.now()))
                .build();

        CertificateSlimDto slimDto = CertificateSlimDto.builder()
                .id(1L).name("Gift")
                .description("Certificate")
                .duration(10)
                .price(BigDecimal.valueOf(100))
                .createDate(Timestamp.from(Instant.now()))
                .lastUpdateDate(Timestamp.from(Instant.now()))
                .build();
        slimDtos.add(slimDto);

        certificateDtoList.add(CertificateDto.builder().id(1L).name("Gift1")
                .description("Certificate1").duration(10)
                .price(BigDecimal.valueOf(100)).build());
        certificateDtoList.add(CertificateDto.builder().id(2L).name("Gift2")
                .description("Certificate2").duration(10)
                .price(BigDecimal.valueOf(100)).build());
        certificateDtoList.add(CertificateDto.builder().id(3L).name("Gift3")
                .description("Certificate3").duration(10)
                .price(BigDecimal.valueOf(100)).build());

        List<CertificateDto> certificateDtoList = Arrays.asList(
                CertificateDto.builder().id(1L).name("Winter").description("Certificate1").build(),
                CertificateDto.builder().id(2L).name("Summer").description("Certificate2").build(),
                CertificateDto.builder().id(3L).name("Spring").description("Certificate3").build(),
                CertificateDto.builder().id(3L).name("Autumn").description("Certificate3").build()
        );
        List<CertificateSlimDto> certificateSlimDtos = Arrays.asList(
                CertificateSlimDto.builder().id(1L).name("Winter").description("Certificate1").build(),
                CertificateSlimDto.builder().id(2L).name("Summer").description("Certificate2").build(),
                CertificateSlimDto.builder().id(3L).name("Spring").description("Certificate3").build(),
                CertificateSlimDto.builder().id(3L).name("Autumn").description("Certificate3").build()
        );

        slim = new PageImpl<>(certificateSlimDtos);
        certificateDtos = new PageImpl<>(certificateDtoList);
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

//    @ParameterizedTest
//    @CsvSource({
//            "1, Java, Winter, 10, 30",
//            "2, SQL,  Summer, 10, 30",
//            "4, PorsgreSQL, Autumn, 10, 30",
//            "5, Spring, Boot, 10, 30"
//    })
//    void testGetCertificatesByTags(long id, String tag1, String tag2) {
//        List<String> tagNames = Arrays.asList(tag1, tag2);
//        when(certificateDao.findByTagNames(tagNames)).thenReturn(certificates);
//        when(certificateMapper.toDtoList(certificates)).thenReturn(certificateDtoList);
//
//        List<CertificateDto> result = service.getCertificatesByTags(tagNames);
//
//        assertEquals(certificateDtoList, result);
//        assertEquals(certificateDtoList.size(), result.size());
//        verify(certificateDao).findByTagNames(tagNames);
//        verify(certificateMapper).toDtoList(certificates);
//    }

    @ParameterizedTest
    @CsvSource({
            "1, Java, description, 10, 30",
            "2, SQL, description, 10, 30",
            "3, Programming, description, 10, 30",
            "4, PorsgreSQL, description, 10, 30",
            "5, Spring, description, 10, 30"
    })
    void testGetByOrderId(long id, String name, String description, BigDecimal price, int duration) {
        Certificate postDto = Certificate.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .build();
        when(certificateDao.findAllByOrderId(id))
                .thenReturn(new HashSet<>(Collections.singleton(postDto)));
        List<CertificateDto> actualCertificates =
                service.getByOrderId(id);
        assertEquals(certificateMapper
                .toDtoList(expectedCertificates), actualCertificates);
    }

    @ParameterizedTest
    @CsvSource({
            "1, Java, description, 10, 30",
            "2, SQL, description, 10, 30",
            "3, Programming, description, 10, 30",
            "4, PorsgreSQL, description, 10, 30",
            "5, Spring, description, 10, 30"
    })
    void testSave(long id, String name, String description, BigDecimal price, int duration) {
        CertificateDto postDto = CertificateDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .build();
        Certificate expectedCertificate = certificateMapper.toEntity(postDto);
        when(certificateDao.save(expectedCertificate)).thenReturn(expectedCertificate);
        CertificateDto actualCertificate = service.save(postDto);
        assertEquals(certificateMapper.toDto(expectedCertificate), actualCertificate);
    }
//
//    @ParameterizedTest
//    @CsvSource({"1, Winter", "2, Summer", "3, Spring", "4, Autumn"})
//    @DisplayName("Get certificates")
//    void testGetAll(int page, String name) {
//        Pageable pageable = PageRequest.of(page, 10);
//        when(certificateDao.getAllBy(pageable)).thenReturn(expectedCertificates);
//        List<CertificateDto> actualCertificates = service.getCertificates(pageable);
//        assertEquals(certificateMapper.toDtoList(expectedCertificates), actualCertificates);
//        verify(certificateDao).getAllBy(pageable);
//    }

//    @Test
//    @DisplayName("Test Get Certificates By Tag")
//    void testGetCertificatesByTag() {
//        when(certificateDao.findByTagNames(tagNames)).thenReturn(expectedCertificates);
//        when(certificateMapper.toDtoList(expectedCertificates)).thenReturn(expectedCertificateDtos);
//        List<CertificateDto> actualPage = service.findAllByTags(tagNames);
//        assertEquals(expectedCertificateDtos, actualPage);
//        verify(certificateDao).findByTagNames(tagNames);
//        verify(certificateMapper).toDtoList(expectedCertificates);
//    }

    @DisplayName("Test find tags by certificate ID")
    @ParameterizedTest(name = "Test #{index} - Certificate ID: {0}")
    @CsvSource({
            "1",
            "2",
            "3",
            "4"})
    void testFindTagsByCertificate(Long certificateId) {

        Set<Tag> expectedTags = new HashSet<>(tags);
        Set<TagDto> expectedTagDtos = new HashSet<>(Arrays.asList(
                TagDto.builder().name("tag1").build(),
                TagDto.builder().name("tag2").build()));

        when(certificateDao.findTagsByCertificateId(certificateId)).thenReturn(tags);
        when(tagMapper.toDtoSet(expectedTags)).thenReturn(expectedTagDtos);


        Set<TagDto> actualTagDtos = service.findTagsByCertificateId(certificateId);

        assertEquals(expectedTagDtos, actualTagDtos);
        verify(certificateDao).findTagsByCertificateId(certificateId);
        verify(tagMapper).toDtoSet(expectedTags);
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
        when(certificateDao.findByUsername(name)).thenReturn(certificate);
        when(certificateMapper.toDto(certificate.get())).thenReturn(certificateDto);
        CertificateDto result = service.getByName(name);
        assertEquals(certificateDto, result);
        verify(certificateDao).findByUsername(name);
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

//    @ParameterizedTest
//    @CsvSource({"0, Winter, 1, 25", "1, Summer, 2, 50", "2, Spring, 3, 75", "3, Autumn, 4, 100"})
//    @DisplayName("Get all certificates without tags")
//    void getAllWithoutTags(int i, String tagName, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, tagName));
//        when(certificateDao.getAllBy(pageable)).thenReturn(certificates);
//        when(certificateMapper.toCertificateSlimDto(certificates)).thenReturn(slimDtos);
//        List<CertificateDto> result = service.getCertificates(pageable);
//        when(certificateDao.getAllBy(pageable)).thenReturn(certificates);
//        when(certificateMapper.toCertificateSlimDto(certificates)).thenReturn(slimDtos);
//        assertNotNull(result);
//    }

    @Test
    @DisplayName("Should update certificate when certificate exists")
    void testUpdateShouldUpdateCertificate() {
        when(certificateDao.getById(certificateDtos.getContent().get(0).getId())).thenReturn(Optional.of(certificates.get(0)));
        when(certificateMapper.toEntity(certificateDtos.getContent().get(0))).thenReturn(certificates.get(0));
        when(certificateDao.update(certificates.get(0))).thenReturn(certificate);
        assertNotNull(certificateDao.update(certificateMapper.toEntity(certificateDtos.getContent().get(0))));
        verify(certificateMapper, times(1)).toEntity(certificateDtos.getContent().get(0));
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
        when(certificateDao.findByUsername(name)).thenReturn(Optional.of(certificate));
        when(certificateMapper.toDto(certificate)).thenReturn(certificateDto);
        CertificateDto result = service.getByName(name);

        verify(certificateDao, times(1)).findByUsername(name);
        verify(certificateMapper, times(1)).toDto(certificate);
        assertEquals(certificateDto, result);
    }

    @Test
    @DisplayName("Get certificate by non-existent name")
    void getCertificateByNonExistentName() {
        when(certificateDao.findByUsername("NonExistentCertificate"))
                .thenReturn(Optional.empty());
        assertThrows(CertificateNotFoundException.class, () ->
                service.getByName("NonExistentCertificate"));
        verify(certificateDao, times(1))
                .findByUsername("NonExistentCertificate");
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
        when(certificateDao.findByUsername(name)).thenReturn(Optional.of(certificate));
        when(certificateMapper.toDto(certificate)).thenReturn(certificateDto);
        CertificateDto dto = service.getByName(name);

        assertNotNull(certificateDto);
        assertEquals(name, certificateDto.getName());
        assertEquals("description", certificateDto.getDescription());
        assertEquals(new BigDecimal("10"), certificateDto.getPrice());
        assertEquals(30, certificateDto.getDuration());
        assertEquals(certificateDto, dto);
        verify(certificateDao, times(1)).findByUsername(name);
        verify(certificateMapper, times(1)).toDto(certificate);
    }

//    @ParameterizedTest
//    @CsvSource({
//            "1, Java, description, 10, 30",
//            "2, SQL, description, 10, 30",
//            "3, Programming, description, 10, 30",
//            "4, PorsgreSQL, description, 10, 30",
//            "5, Spring, description, 10, 30"
//    })
//    void testFindCertificatesByTags(long id, String name, String description, BigDecimal price, int duration) {
//        List<String> tagNames = Arrays.asList("Tag1", "Tag2", "Tag3");
//
//        Certificate certificate = Certificate.builder()
//                .id(id).description(description)
//                .duration(duration).price(price).name(name).build();
//        CertificateDto certificateDto = CertificateDto.builder()
//                .id(id).name(name).description(description)
//                .duration(duration).price(price).build();
//
//        List<Certificate> certificates = Collections.singletonList(certificate);
//        List<CertificateDto> expectedDtos = Collections.singletonList(certificateDto);
//        when(certificateDao.findByTagNames(tagNames)).thenReturn(certificates);
//        when(certificateMapper.toDtoList(certificates)).thenReturn(expectedDtos);
//
//        List<CertificateDto> result = service.findAllByTags(tagNames);
//
//        assertEquals(expectedDtos.size(), result.size());
//    }

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

    @ParameterizedTest(name = "Update {index}: name = {1}")
    @CsvSource({
            "1, Java, description, 10, 30",
            "2, SQL, description, 10, 30",
            "3, Programming, description, 10, 30",
            "4, PorsgreSQL, description, 10, 30",
            "5, Spring, description, 10, 30"
    })
    void testUpdateCertificates(Long id, String name,
                                String description, BigDecimal price, int duration) {
        Certificate certificate = Certificate.builder()
                .id(id).description(description)
                .duration(duration).price(price).name(name).build();
        CertificateDto dto = CertificateDto.builder()
                .id(id).duration(duration).price(price).build();
        PatchCertificateDto patchDto = PatchCertificateDto.builder()
                .id(id).duration(duration).price(price).build();
        when(certificateDao.update(certificate)).thenReturn(certificate);
        when(certificateMapper.toEntity(patchDto)).thenReturn(certificate);
        when(certificateMapper.toDto(certificate)).thenReturn(dto);

        CertificateDto result = service.update(patchDto);

        verify(certificateDao).update(certificate);
        verify(certificateMapper).toEntity(patchDto);
        verify(certificateMapper).toDto(certificate);
        assertEquals(dto, result);
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

        when(certificateDao.findAllByIds(ids)).thenReturn(certificates);
        when(certificateMapper.toDtoList(certificates)).thenReturn(expectedDtos);

        List<CertificateDto> actualCertificateDtos = service.getByIds(ids);

        assertEquals(new HashSet<>(expectedDtos), new HashSet<>(actualCertificateDtos));
        verify(certificateDao).findAllByIds(ids);
        verify(certificateMapper).toDtoList(certificates);
    }
}
