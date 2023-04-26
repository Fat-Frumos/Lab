package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Data
@Builder
public class CertificateDto implements Serializable {

        private Long id;
        @NotBlank(message = "Name cannot be blank")
    private String name;
        @NotBlank(message = "Description cannot be blank")
    private String description;
        @NotNull(message = "Price cannot be null")
    private BigDecimal price;
        @JsonFormat(timezone = "GMT+03:00", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Instant createDate;
        @JsonFormat(timezone = "GMT+03:00", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Instant lastUpdateDate;
        @NotNull(message = "Duration cannot be null")
    private int duration;
        @EqualsAndHashCode.Exclude
    private Set<TagDto> tags;

        @JsonCreator
    public CertificateDto(
            @JsonProperty("id") final Long id,
            @JsonProperty("name") final String name,
            @JsonProperty("description") final String description,
            @JsonProperty("price") final BigDecimal price,
            @JsonProperty("createDate") final Instant createDate,
            @JsonProperty("lastUpdateDate") final Instant lastUpdateDate,
            @JsonProperty("duration") final int duration,
            @JsonProperty("tags") final Set<TagDto> tags) {
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
