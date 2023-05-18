package com.epam.esm.controller;

import com.epam.esm.assembler.TagAssembler;
import com.epam.esm.criteria.Criteria;
import com.epam.esm.criteria.FilterParams;
import com.epam.esm.dto.Linkable;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import javax.swing.SortOrder;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/tags")
public class TagController {
    private final TagService tagService;
    private final TagAssembler tagAssembler;

    @GetMapping(value = "/{id}",
            produces = APPLICATION_JSON_VALUE)
    public EntityModel<Linkable> getById(
            @PathVariable final Long id) {
        return tagAssembler.toModel(
                tagService.getById(id));
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public CollectionModel<EntityModel<Linkable>> getAll(
            @RequestParam(defaultValue = "UNSORTED") SortOrder sort,
            @RequestParam(defaultValue = "ID") FilterParams params,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        return tagAssembler.toCollectionModel(
                tagService.getAll(Criteria
                        .builder()
                        .filterParams(params)
                        .sortOrder(sort)
                        .page(page)
                        .size(size)
                        .build()));
    }

    @ResponseStatus(CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public TagDto create(
            @RequestBody final TagDto tagDto) {
        return tagService.save(tagDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> delete(
            @PathVariable final Long id) {
        tagService.delete(id);
        return new ResponseEntity<>(
                NO_CONTENT);
    }
}
