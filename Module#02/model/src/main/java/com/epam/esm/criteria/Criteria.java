package com.epam.esm.criteria;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

/**
 * Criteria for filtering and sorting certificates.
 * <p>
 * Criteria can be used to filter certificates based on various attributes
 * such as name, description, tag name, and date, specified field and order.
 */
@Data
@Builder
public class Criteria {

    /**
     * The sort order to use when sorting certificates.
     */
    private SortOrder sortOrder;
    /**
     * The field to use when sorting certificates.
     */
    private SortField sortField;
    /**
     * The name of the certificate to filter by.
     */
    private String name;
    /**
     * The description of the certificate to filter by.
     */
    private String description;
    /**
     * The name of the tag associated with the certificate to filter by.
     */
    private String tagName;
    /**
     * The date the certificate was created on to filter by.
     */
    private Instant date;
}
