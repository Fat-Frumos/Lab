package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
public class CertificateWithoutTagDto implements Linkable {
    @NotNull(message = "Id cannot be blank")
    private Long id;
    @Size(min = 1, max = 512)
    @NotNull(message = "Name cannot be blank")
    private String name;
    @Size(min = 1, max = 1024)
    @NotNull(message = "Description cannot be blank")
    private String description;
    @Size(min = 1, max = 10000)
    @NotNull(message = "Price cannot be null")
    private BigDecimal price;
    @Size(min = 1, max = 365)
    @NotNull(message = "Duration cannot be null")
    private int duration;
    @JsonFormat(timezone = "GMT+03:00", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Timestamp createDate;
    @JsonFormat(timezone = "GMT+03:00", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Timestamp lastUpdateDate;
}
