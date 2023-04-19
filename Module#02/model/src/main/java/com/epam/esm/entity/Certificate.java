package com.epam.esm.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

/**
 * This class represents a certificate which can be purchased by a customer.
 * <p>
 * It contains information such as the name, description, price, duration,
 * and tags associated with the certificate.
 */
@Data
@Builder
public class Certificate implements Serializable {

    /**
     * The unique identifier of the certificate.
     */
    private Long id;
    /**
     * The name of the certificate.
     */
    private String name;
    /**
     * A brief description of the certificate.
     */
    private String description;
    /**
     * The price of the certificate.
     */
    private BigDecimal price;
    /**
     * The date on which the certificate was created.
     */
    private Instant createDate;
    /**
     * The date on which the certificate was last updated.
     */
    private Instant lastUpdateDate;
    /**
     * The duration of the certificate in days.
     */
    private Integer duration;
    /**
     * The set of tags associated with the certificate.
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Tag> tags;
}
