package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@EqualsAndHashCode(callSuper=false)
public class CertificateWithoutTagDto extends BaseDto {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    @JsonFormat(timezone = "GMT+03:00")
    private Instant createDate;
    @JsonFormat(timezone = "GMT+03:00")
    private Instant lastUpdateDate;
    private int duration;
}
