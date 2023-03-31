package com.epam.esm.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@Builder
@Data
public class Tag implements BaseEntity {

    private Long id;
    private String name;
    private Set<Certificate> certificates;
}
