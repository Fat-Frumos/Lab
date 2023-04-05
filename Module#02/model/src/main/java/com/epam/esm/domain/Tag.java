package com.epam.esm.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class Tag implements BaseEntity {

    private Long id;
    private String name;
    private Set<Certificate> certificates;
}
