package com.epam.esm.criteria;

import com.epam.esm.entity.Tag;
import lombok.Builder;
import lombok.Data;
import org.hibernate.query.sqm.SortOrder;

import java.util.Set;

@Data
@Builder
public class Criteria {
    private Long page;
    private Long size;
    private Long offset;
    private String sortBy;
    private FilterParams field;
    private SortOrder sortOrder;
    private Set<Tag> tags;
}
