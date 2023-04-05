package com.epam.esm.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Data
@Builder
public class Certificate implements BaseEntity {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Instant createDate;
    private Instant lastUpdateDate;
    private Integer duration;
    private Set<Tag> tags;

    @Override
    public String toString() {
        return String.format(
                "{id=%d, name='%s', description='%s', price=%s, duration=%d, tags=%s}",
                id, name, description, price, duration, tags);
    }
}
