package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/tags")
public class TagController {
    private final TagService tagService;

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public final TagDto getById(
            @PathVariable final Long id) {
        return tagService.getById(id);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public final List<TagDto> getAll() {
        return tagService.getAll();
    }

    @ResponseStatus(CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public final TagDto create(
            @RequestBody final TagDto tagDto) {
        return tagService.save(tagDto);
    }

    @ResponseStatus(OK)
    @DeleteMapping(value = "/{id}")
    public final void delete(
            @PathVariable("id") final Long id) {
        tagService.delete(id);
    }
}
