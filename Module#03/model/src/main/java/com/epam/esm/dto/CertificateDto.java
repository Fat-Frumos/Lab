package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

@Data
@Builder
public class CertificateDto implements Linkable {
    @NotNull(message = "Id cannot be blank")
    private Long id;
    @NotNull(message = "Name cannot be blank")
    private String name;
    @NotNull(message = "Description cannot be blank")
    private String description;
    @NotNull(message = "Price cannot be null")
    private BigDecimal price;
    @JsonFormat(timezone = "GMT+03:00", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Timestamp createDate;
    @JsonFormat(timezone = "GMT+03:00", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Timestamp lastUpdateDate;
    @NotNull(message = "Duration cannot be null")
    private int duration;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<TagDto> tags;
}
