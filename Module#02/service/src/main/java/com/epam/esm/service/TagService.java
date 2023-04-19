package com.epam.esm.service;

import com.epam.esm.dto.TagDto;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * The TagService interface provides
 * operations to manage tags.
 */
@Validated
public interface TagService {
    /**
     * Retrieves a tag by ID.
     *
     * @param id the ID of the tag to retrieve
     * @return the tag DTO
     */
    TagDto getById(Long id);

    /**
     * Retrieves a tag by name.
     *
     * @param name the name of the tag to retrieve
     * @return the tag DTO
     */
    TagDto getByName(String name);

    /**
     * Retrieves all tags.
     *
     * @return a list of tag DTOs
     */
    List<TagDto> getAll();

    /**
     * Saves a tag.
     *
     * @param tag the tag DTO to save
     * @return the saved tag DTO
     */
    TagDto save(TagDto tag);

    /**
     * Deletes a tag by ID.
     *
     * @param id the ID of the tag to delete
     */
    void delete(Long id);
}
