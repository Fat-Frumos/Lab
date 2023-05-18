package com.epam.esm.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements Serializable {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "email", nullable = false)
    private String email;

    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<Order> orders = new HashSet<>();

    public void addOrder(Order order) {
        if (orders == null) {
            this.orders = new HashSet<>();
        }
        if (order != null) {
            this.orders.add(order);
        }
    }
}
