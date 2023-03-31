package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static java.lang.String.format;

@Setter
@Getter
@Builder
@Data
public class CertificateDto {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    @JsonFormat(timezone = "GMT+03:00")
    private Instant createDate;
    @JsonFormat(timezone = "GMT+03:00")
    private Instant lastUpdateDate;
    private int duration;
    private List<TagDto> tags;

    @Override
    public String toString() {
        return format(
                "{id=%d, name='%s', description='%s', price=%s, duration=%d, tags=%s}",
                id, name, description, price, duration, tags);
    }
}
