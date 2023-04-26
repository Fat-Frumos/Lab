package com.epam.esm.criteria;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class Criteria {

        private SortOrder sortOrder;
        private SortField sortField;
        private String name;
        private String description;
        private String tagName;
        private Instant date;
}
