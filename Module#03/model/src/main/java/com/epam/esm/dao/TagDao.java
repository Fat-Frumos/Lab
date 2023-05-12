package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.Set;

public interface TagDao extends Dao<Tag> {
    Tag findById(Long id);

    Set<Tag> saveAll(Set<Tag> tags);
}
