package com.epam.esm.dto;

import com.epam.esm.domain.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TagDto {

    private Long id;
    private String name;

    public TagDto(final Tag tag) {
        this.id = tag.getId();
        this.name = tag.getName();
    }
}
