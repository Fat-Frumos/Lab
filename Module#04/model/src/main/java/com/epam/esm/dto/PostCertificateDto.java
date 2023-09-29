package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

/**
 * Represents a DTO (Data Transfer Object) for creating or updating a certificate.
 * It extends the RepresentationModel class to include HATEOAS support.
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class PostCertificateDto extends RepresentationModel<CertificateDto> {

    /**
     * The ID of the certificate.
     */
    private Long id;

    /**
     * The name of the certificate.
     */
    @Size(min = 1, max = 512)
    @NotNull(message = "Name cannot be blank")
    private String name;

    /**
     * The description of the certificate.
     */
    @Size(min = 1, max = 1024)
    @NotNull(message = "Description cannot be blank")
    private String description;

    /**
     * The price of the certificate.
     */
    @DecimalMin(value = "0.00", inclusive = false, message = "Price must be greater than 0.00")
    @DecimalMax(value = "10000.00", inclusive = false, message = "Price must be less than 10000.00")
    private BigDecimal price;

    /**
     * The duration of the certificate.
     */
    @Min(value = 0, message = "Duration must be a positive number or zero.")
    @Max(value = 365, message = "Duration must be less than or equal to 365.")
    private Integer duration;

    /**
     * The timestamp representing the creation date of the certificate.
     */
    @JsonFormat(timezone = "GMT+03:00", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Timestamp createDate;

    /**
     * The timestamp representing the last update date of the certificate.
     */
    @JsonFormat(timezone = "GMT+03:00", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Timestamp lastUpdateDate;

    /**
     * The set of tags associated with the certificate.
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<TagDto> tags;
}
