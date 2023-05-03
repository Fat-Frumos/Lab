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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TagAssemblerTest {
    @MockBean
    private TagService tagService;
    @Mock
    private TagController tagController = mock(TagController.class);
    private final TagAssembler tagAssembler = new TagAssembler();
    private MockMvc mockMvc;

    @ParameterizedTest
    @DisplayName("Test toModel method of TagAssembler")
    @CsvSource({
            "1, Winter",
            "2, Summer",
            "3, Spring",
            "4, Autumn"
    })
    void testToModel(Long id, String name) {
        TagDto tagDto = TagDto.builder().tagId(id).name(name).build();

        EntityModel<TagDto> expectedModel = EntityModel.of(tagDto,
                linkTo(methodOn(TagController.class).getAll()).withRel("tags"),
                linkTo(methodOn(TagController.class).getById(tagDto.getTagId())).withSelfRel(),
                linkTo(methodOn(TagController.class).delete(tagDto.getTagId())).withRel("delete"));

        EntityModel<Linkable> actualModel = tagAssembler.toModel(tagDto);

        assertEquals(expectedModel.getContent(), actualModel.getContent());
        assertEquals(expectedModel.getLinks(), actualModel.getLinks());

        when(tagController.getById(tagDto.getTagId())).thenReturn(actualModel);
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
                TagDto.builder().tagId(id).build(),
                TagDto.builder().name(name).build(),
                TagDto.builder().tagId(id).name(name).build()
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

    @ParameterizedTest(name = "Tag id={0} name={1}")
    @CsvSource({
            "1, Winter",
            "2, Summer",
            "3, Spring",
            "4, Autumn"
    })
    @DisplayName("Should create correct links for Tag with given id")
    void shouldCreateCorrectLinksForTag(Long id, String name) {
        TagDto tagDto = TagDto.builder().tagId(id).name(name).build();
        Mockito.when(tagService.getById(id)).thenReturn(tagDto);
        TagAssembler assembler = new TagAssembler();
        TagController tagController = new TagController(tagService, assembler);
        EntityModel<Linkable> model = tagController.getById(id);
        Link selfLink = linkTo(methodOn(TagController.class).getById(id)).withSelfRel();
        Link deleteLink = linkTo(methodOn(TagController.class).delete(id)).withRel("delete");
        EntityModel<Linkable> actualModel = tagAssembler.toModel(tagDto);
        CollectionModel<EntityModel<Linkable>> entityModels = tagController.getAll();

        assertNotNull(model);
        assertNotNull(entityModels);
        assertEquals(model, actualModel);
        assertNotNull(model.getLink("self"));
        assertNotNull(model.getLink("all-tags"));
        assertEquals(selfLink, model.getLink("self").get());
        assertEquals(deleteLink, model.getLink("delete").get());
        verify(tagService, times(1)).getById(id);
    }
}
