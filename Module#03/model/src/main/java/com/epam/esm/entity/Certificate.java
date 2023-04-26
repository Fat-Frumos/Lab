package com.epam.esm.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Data
@Builder
public class Certificate implements Serializable {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Instant createDate;
    private Instant lastUpdateDate;
    private Integer duration;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Tag> tags;
}
