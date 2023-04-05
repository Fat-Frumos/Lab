package com.epam.esm.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@Table("gift_certificates")
public class Criteria {

    @Column("id")
    Long id;
    @Column("sortDirection")
    SortDirection sortDirection;
    @Column("name")
    String name;
    @Column("price")
    BigDecimal price;
    @Column("description")
    String description;
    @Column("tagName")
    String tagName;
    @Column("date")
    Instant date;
    @Column("duration")
    Integer duration;
}
