package com.epam.esm.criteria;

import lombok.Getter;

@Getter
public enum SortField {
        DATE("c.last_update_date"),
        NAME("c.name");
        private final String field;

        SortField(final String field) {
        this.field = field;
    }
}
