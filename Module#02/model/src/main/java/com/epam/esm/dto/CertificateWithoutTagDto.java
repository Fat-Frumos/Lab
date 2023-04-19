package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * This class represents a certificate DTO without its associated tags.
 * <p>
 * It includes basic information about the certificate
 * such as its id, name, description, price,
 * create and last update dates, and duration.
 */
@Data
@Builder
public class CertificateWithoutTagDto implements Serializable {

    /**
     * The unique identifier of the certificate.
     */
    private Long id;
    /**
     * The name of the certificate.
     */
    @NotBlank(message = "Name cannot be blank")
    private String name;
    /**
     * The description of the certificate.
     */
    @NotBlank(message = "Description cannot be blank")
    private String description;
    /**
     * The price of the certificate.
     */
    @NotNull(message = "Price cannot be null")
    private BigDecimal price;
    /**
     * The date and time when the certificate was created.
     */
    @JsonFormat(timezone = "GMT+03:00", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Instant createDate;
    /**
     * The date and time when the certificate was last updated.
     */
    @JsonFormat(timezone = "GMT+03:00", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Instant lastUpdateDate;
    /**
     * The duration of the certificate in days.
     */
    @NotNull(message = "Duration cannot be null")
    private int duration;
}
