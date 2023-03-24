package com.epam.esm.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Certificate implements BaseEntity {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Instant createDate;
    private Instant lastUpdateDate;
    private Integer duration;
    private Set<Tag> tags = new HashSet<>();
}
