package com.epam.esm.criteria;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum FilterParams {
    TAGS("tags", 0),
    PAGE("page", 1),
    SIZE("size", 25),
    DATE("date", 0),
    NAME("name", 0),
    ORDER("ASC", 0),
    PRICE("price", 0),
    SORT("sort", 0),
    DURATION("duration", 1),
    DESCRIPTION("description", 0),
    UPDATED_DATE("updated_date", 0),
    ID("id", 0);

    private final String key;
    private final int value;

    public static FilterParams value(final String name) {
        return Arrays.stream(FilterParams.values())
                .filter(param -> param.key.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
