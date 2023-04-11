package com.epam.esm.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@Builder
public class Tag implements Serializable {

    private Long id;
    private String name;
    private Set<Certificate> certificates;
}
