package com.epam.esm.criteria;

import lombok.Getter;

/**
 * Enum representing the possible fields that can be used for sorting in a query.
 * <p>
 * Each field is associated with the corresponding database column name.
 */
@Getter
public enum SortField {
    /**
     * Field representing the certificate's last update date
     */
    DATE("c.last_update_date"),
    /**
     * Field representing the certificate's name
     */
    NAME("c.name");
    /**
     * The database column name associated with the field
     */
    private final String field;

    /**
     * Constructor for the SortField enum.
     *
     * @param field The database column name associated with the field
     */
    SortField(final String field) {
        this.field = field;
    }
}
