package com.epam.esm.assembler;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.Linkable;
import com.epam.esm.dto.TagDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

class CertificateAssemblerTest {

    @Mock
    private CertificateController certificateController;

    @InjectMocks
    private CertificateAssembler certificateAssembler;
    CertificateDto certificateDto;

    @BeforeEach
    void setUp() {
        certificateDto = CertificateDto.builder()
                .id(1L)
                .name("Certificate 1")
                .description("Certificate 1 Description")
                .price(BigDecimal.TEN)
                .createDate(new Timestamp(System.currentTimeMillis()))
                .lastUpdateDate(new Timestamp(System.currentTimeMillis()))
                .duration(10)
                .tags(new HashSet<>())
                .build();
        certificateController = mock(CertificateController.class);
        certificateAssembler = new CertificateAssembler();
    }

    @ParameterizedTest
    @DisplayName("Test toModel method with single entity")
    @CsvSource({
            "1, Certificate 1, Certificate 1 Description, 10, 10",
            "2, 'Certificate 2', 'Certificate 2 Description',20, 20"
    })
    void toModel_returnsEntityModels(long id, String name, String description, int price, int duration) {
        CertificateDto certificateDto = CertificateDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(BigDecimal.valueOf(price))
                .createDate(new Timestamp(System.currentTimeMillis()))
                .lastUpdateDate(new Timestamp(System.currentTimeMillis()))
                .duration(duration)
                .tags(new HashSet<>())
                .build();

        EntityModel<Linkable> entity = certificateAssembler.toModel(certificateDto);

        assertNotNull(entity);
        assertNotNull(entity.getContent());
        assertEquals(certificateDto, entity.getContent());
        assertNotNull(entity.getLink("self"));
        assertNotNull(entity.getLink("tags"));
        assertNotNull(entity.getLink("delete"));
    }


    @Test
    void toModelReturnsEntityModel() {

        EntityModel<Linkable> result = certificateAssembler.toModel(certificateDto);
        assertEquals(certificateDto, result.getContent());
        assertNotNull(result.getLink("self"));
        assertNotNull(result.getLink("tags"));
        assertNotNull(result.getLink("delete"));
    }

    @ParameterizedTest
    @CsvSource({
            "1, 'Certificate 1', 'Certificate 1 Description', 10",
            "2, 'Certificate 2', 'Certificate 2 Description', 20"
    })
    void toCollectionModel_returnsCollectionModel(long id, String name, String description, int duration) {
        CertificateDto certificateDto = CertificateDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(BigDecimal.TEN)
                .createDate(new Timestamp(System.currentTimeMillis()))
                .lastUpdateDate(new Timestamp(System.currentTimeMillis()))
                .duration(duration)
                .tags(new HashSet<>())
                .build();

        Iterable<CertificateDto> certificateDtos = () -> singletonList(certificateDto).iterator();
        when(certificateController.getAll()).thenReturn(CollectionModel.of(
                certificateAssembler.toCollectionModel(certificateDtos),
                linkTo(methodOn(CertificateController.class).getAll()).withSelfRel()
        ));

        CollectionModel<EntityModel<Linkable>> result = certificateController.getAll();

        assertEquals(1, result.getContent().size());
        assertNotNull(result.getLink("self"));
    }


    @Test
    void toCollectionModel_returnsCollectionModel() {

        CertificateDto certificateDto2 = CertificateDto.builder()
                .id(2L)
                .name("Certificate 2")
                .description("Certificate 2 Description")
                .price(BigDecimal.TEN)
                .createDate(new Timestamp(System.currentTimeMillis()))
                .lastUpdateDate(new Timestamp(System.currentTimeMillis()))
                .duration(10)
                .tags(new HashSet<>())
                .build();
        Iterable<CertificateDto> certificateDtos = Arrays.asList(certificateDto, certificateDto2);
        CollectionModel<EntityModel<Linkable>> result = certificateAssembler.toCollectionModel(certificateDtos);
        assertEquals(2, result.getContent().size());
        assertNotNull(result.getLink("self"));
    }

    @ParameterizedTest
    @CsvSource({
            "1, Certificate 1, Description 1, 10.0, 30, 2023-01-01 00:00:00, 2023-01-01 00:00:00,  1, Tag 1, 1, Tag 2",
            "2, Certificate 2, Description 2, 20.0, 60, 2023-02-01 00:00:00, 2023-02-01 00:00:00, 2, Tag 2, 2, Tag 3"
    })
    void toModel_ShouldReturnExpectedLinks(Long id, String name, String description, BigDecimal price,
                                           Integer duration, Timestamp createDate, Timestamp lastUpdateDate,
                                           Long tagId1, String tagName1, Long tagId2, String tagName2) {
        CertificateDto dto = CertificateDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .createDate(createDate)
                .lastUpdateDate(lastUpdateDate)
                .tags(new HashSet<>(Arrays.asList(
                        TagDto.builder().tagId(tagId1).name(tagName1).build(),
                        TagDto.builder().tagId(tagId2).name(tagName2).build()
                )))
                .build();

        EntityModel<Linkable> model = certificateAssembler.toModel(dto);

        assertEquals(dto.getId(), model.getContent().getId());
        assertEquals(dto.getName(), ((CertificateDto) model.getContent()).getName());
        assertEquals(dto.getDescription(), ((CertificateDto) model.getContent()).getDescription());
        assertEquals(dto.getPrice(), ((CertificateDto) model.getContent()).getPrice());
        assertEquals(dto.getDuration(), ((CertificateDto) model.getContent()).getDuration());
        assertEquals(dto.getCreateDate(), ((CertificateDto) model.getContent()).getCreateDate());
        assertEquals(dto.getLastUpdateDate(), ((CertificateDto) model.getContent()).getLastUpdateDate());
        assertEquals(dto.getTags().size(), ((CertificateDto) model.getContent()).getTags().size());
        Link selfLink = model.getLink("self").orElse(null);
        assertNotNull(selfLink);
        assertEquals("/certificates/" + dto.getId(), selfLink.getHref());
        Link tagsLink = model.getLink("tags").orElse(null);
        assertNotNull(tagsLink);
        assertEquals("/certificates/" + dto.getId() + "/tags", tagsLink.getHref());
//        Link deleteLink = model.getLink("delete").orElse(null);
//        assertEquals("/certificates/" + dto.getId(), deleteLink.getHref());
    }
}
