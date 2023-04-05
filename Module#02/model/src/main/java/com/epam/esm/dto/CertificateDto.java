package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

import static java.lang.String.format;

@Data
@Builder
@EqualsAndHashCode(callSuper=false)
public class CertificateDto extends BaseDto {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    @JsonFormat(timezone = "GMT+03:00")
    private Instant createDate;
    @JsonFormat(timezone = "GMT+03:00")
    private Instant lastUpdateDate;
    private int duration;
    private Set<TagDto> tags;

    @Override
    public String toString() {
        return format(
                "{id=%d, name='%s', description='%s', price=%s, duration=%d, tags=%s}",
                id, name, description, price, duration, tags);
    }
    @JsonCreator
    public CertificateDto(@JsonProperty("id") Long id,
                          @JsonProperty("name") String name,
                          @JsonProperty("description") String description,
                          @JsonProperty("price") BigDecimal price,
                          @JsonProperty("createDate") Instant createDate,
                          @JsonProperty("lastUpdateDate") Instant lastUpdateDate,
                          @JsonProperty("duration") int duration,
                          @JsonProperty("tags") Set<TagDto> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.duration = duration;
        this.tags = tags;
    }
}
