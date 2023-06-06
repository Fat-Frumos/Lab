package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.Set;

/**
 * Data access interface for managing tags.
 */
public interface TagDao extends Dao<Tag> {

    /**
     * Retrieves a tag by its ID.
     *
     * @param id the ID of the tag
     * @return the tag with the specified ID, or null if not found
     */
    Tag findById(Long id);

    /**
     * Saves a set of tags.
     *
     * @param tags the set of tags to save
     * @return the saved set of tags
     */
    Set<Tag> saveAll(Set<Tag> tags);
}
