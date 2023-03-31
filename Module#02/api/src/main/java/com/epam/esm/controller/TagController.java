package com.epam.esm.controller;

import com.epam.esm.handler.ErrorResponse;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/tags")
public class TagController {
    private final TagService tagService;

    @ResponseBody
    @GetMapping(value = "/{id}")
    public final ResponseEntity<Object> getTag(
            @PathVariable("id") final Long id) {
        try {
            return new ResponseEntity<>(
                    tagService.getById(id),
                    new HttpHeaders(),
                    HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(
                    new ErrorResponse(e.getMessage(), 40401),
                    HttpStatus.NOT_FOUND);
        }
    }

    @ResponseBody
    @GetMapping
    public final ResponseEntity<Object> getAllTag() {
        try {
            return new ResponseEntity<>(
                    tagService.getAll(),
                    new HttpHeaders(),
                    HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(
                    new ErrorResponse(e.getMessage(), 40401),
                    HttpStatus.NOT_FOUND);
        }
    }
}
