package com.epam.esm.assembler;

import com.epam.esm.controller.TagController;
import com.epam.esm.dto.Linkable;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@ExtendWith(MockitoExtension.class)
class TagAssemblerTest {

    @Mock
    private TagController tagController = mock(TagController.class);
    private final TagAssembler tagAssembler = new TagAssembler();

    @ParameterizedTest
    @DisplayName("Test toModel method of TagAssembler")
    @CsvSource({
            "1, Winter",
            "2, Summer",
            "3, Spring",
            "4, Autumn"
    })
    void testToModel(Long id, String name) {
        TagDto tagDto = TagDto.builder().id(id).name(name).build();

        EntityModel<TagDto> expectedModel = EntityModel.of(tagDto,
                linkTo(methodOn(TagController.class).getAll()).withRel("tags"),
                linkTo(methodOn(TagController.class).getById(tagDto.getId())).withSelfRel(),
                linkTo(methodOn(TagController.class).delete(tagDto.getId())).withRel("delete"));

        EntityModel<Linkable> actualModel = tagAssembler.toModel(tagDto);

        assertEquals(expectedModel.getContent(), actualModel.getContent());
        assertEquals(expectedModel.getLinks(), actualModel.getLinks());

        when(tagController.getById(tagDto.getId())).thenReturn(actualModel);
        EntityModel<Linkable> model = tagController.getById(id);
    }

    @ParameterizedTest
    @DisplayName("Test toCollectionModel method of TagAssembler")
    @CsvSource({
            "1, Winter",
            "2, Summer",
            "3, Spring",
            "4, Autumn"
    })
    void testToCollectionModel(Long id, String name) {
        List<TagDto> tagDtos = Arrays.asList(
                TagDto.builder().id(id).build(),
                TagDto.builder().name(name).build(),
                TagDto.builder().id(id).name(name).build()
        );

        List<EntityModel<TagDto>> expectedModels = tagDtos.stream()
                .map(tagDto -> EntityModel.of(tagDto,
                        linkTo(methodOn(TagController.class).getById(tagDto.getId())).withSelfRel(),
                        linkTo(methodOn(TagController.class).getAll()).withRel("tags"),
                        linkTo(methodOn(TagController.class).delete(tagDto.getId())).withRel("delete")))
                .collect(toList());

        CollectionModel<EntityModel<Linkable>> actualCollectionModel = tagAssembler.toCollectionModel(tagDtos);

        assertEquals(expectedModels.size(), actualCollectionModel.getContent().size());

        List<EntityModel<Linkable>> actualModels = new ArrayList<>(actualCollectionModel.getContent());

        assertEquals(expectedModels.get(0).getContent(), actualModels.get(0).getContent());
        assertEquals(expectedModels.get(1).getContent(), actualModels.get(1).getContent());
        assertEquals(expectedModels.get(2).getContent(), actualModels.get(2).getContent());
    }
}
