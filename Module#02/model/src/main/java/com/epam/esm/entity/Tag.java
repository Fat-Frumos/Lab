package com.epam.esm.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * A class representing a tag.
 */
@Data
@Builder
public class Tag implements Serializable {

    /**
     * The unique identifier of the tag.
     */
    private Long id;
    /**
     * The name of the tag.
     */
    private String name;
    /**
     * The certificates associated with the tag.
     */
    private Set<Certificate> certificates;
}
