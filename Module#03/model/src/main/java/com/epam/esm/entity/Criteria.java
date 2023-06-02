package com.epam.esm.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Criteria {
    private String name;
    private String description;
    private List<String> tagNames;
}
