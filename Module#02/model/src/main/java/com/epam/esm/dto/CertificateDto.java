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

/**
 * This is a data transfer object (DTO) class for certificates.
 * It is used to transfer certificate data
 * between different layers of the application.
 * <p>
 * The class contains fields for certificate attributes
 * such as name, description, price, duration, and tags.
 */
@Data
@Builder
public class CertificateDto implements Serializable {

    /**
     * The unique identifier for the certificate.
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
     * The creation date of the certificate.
     */
    @JsonFormat(timezone = "GMT+03:00", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Instant createDate;
    /**
     * The last update date of the certificate.
     */
    @JsonFormat(timezone = "GMT+03:00", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Instant lastUpdateDate;
    /**
     * The duration of the certificate in days.
     */
    @NotNull(message = "Duration cannot be null")
    private int duration;
    /**
     * The set of tags associated with the certificate.
     */
    @EqualsAndHashCode.Exclude
    private Set<TagDto> tags;

    /**
     * Constructs a new CertificateDto with the specified values.
     *
     * @param id             the unique identifier for the certificate
     * @param name           the name of the certificate
     * @param description    the description of the certificate
     * @param price          the price of the certificate
     * @param createDate     the creation date of the certificate
     * @param lastUpdateDate the last update date of the certificate
     * @param duration       the duration of the certificate in days
     * @param tags           the set of tags associated with the certificate
     */
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
