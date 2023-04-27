package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "gift_certificates")
//@NamedEntityGraph(
//        name = "certificate.tags.user",
//        attributeNodes = {
//                @NamedAttributeNode("tags"),
//                @NamedAttributeNode("orders")})
public class Certificate implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private String description;
    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;
    @Column(name = "last_update_date", nullable = false)
    private Timestamp lastUpdateDate;
    @Column(nullable = false)
    private Integer duration;

    @PrePersist
    public void prePersist() {
        createDate = new Timestamp(System.currentTimeMillis());
        lastUpdateDate = createDate;
    }

    @PreUpdate
    public void preUpdate() {
        lastUpdateDate = new Timestamp(System.currentTimeMillis());
    }

    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "gift_certificate_tag",
            joinColumns = @JoinColumn(name = "gift_certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @JsonManagedReference
    private Set<Tag> tags = new HashSet<>();


//    @Builder.Default
//    @ToString.Exclude
//    @EqualsAndHashCode.Exclude
//    @ManyToMany(
//            mappedBy = "certificates",
//            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    private Set<Order> orders = new HashSet<>();

    public Certificate addTag(
            final Tag tag) {
        if (tags == null) {
            this.tags = new HashSet<>();
        } else {
            this.tags.add(tag);
        }
        return this;
    }
}
