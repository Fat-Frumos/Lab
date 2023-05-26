package com.epam.esm.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
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

/**
 * Represents a user entity in the system.
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements Serializable {
    /**
     * The unique identifier of the user.
     */
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_id_seq", allocationSize = 1)
    private Long id;

    /**
     * The username of the user.
     */
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    /**
     * The email address of the user.
     */
    @Column(name = "email", nullable = false)
    private String email;

    /**
     * The set of orders associated with the user.
     */
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Order> orders = new HashSet<>();

    /**
     * Adds an order to the user's set of orders.
     *
     * @param order the order to add
     */
    public void addOrder(final Order order) {
        if (orders == null) {
            this.orders = new HashSet<>();
        }
        if (order != null) {
            this.orders.add(order);
        }
    }

    /**
     * Removes an order from the user's set of orders.
     *
     * @param order the order to remove
     * @return the removed order
     */
    public Order removeOrder(final Order order) {
        if (order != null) {
            this.orders.remove(order);
        }
        return order;
    }
}
