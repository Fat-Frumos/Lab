package com.epam.esm.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.singletonList;

/**
 * User class representing a user in the system.
 * <p>
 * Implements UserDetails interface for Spring Security integration.
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@NamedEntityGraph(
        name = "User.orders.certificates.tags",
        attributeNodes = {
                @NamedAttributeNode(value = "orders", subgraph = "orderGraph")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "orderGraph",
                        attributeNodes = {
                                @NamedAttributeNode(
                                        value = "certificates",
                                        subgraph = "certificateGraph"
                                )
                        }
                ),
                @NamedSubgraph(
                        name = "certificateGraph",
                        attributeNodes = {
                                @NamedAttributeNode("tags")
                        }
                )
        }
)
public class User implements UserDetails {
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
     * The password of the user.
     */
    @Column(name = "password", nullable = false)
    private String password;

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

    /**
     * The role of a user.
     */
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * Retrieves the authorities granted to the user.
     *
     * @return A collection of GrantedAuthority objects representing the user's authorities.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return singletonList(new SimpleGrantedAuthority(
                String.format("ROLE_%s", role.name())));
    }

    /**
     * Checks if the user's account is not expired.
     *
     * @return true if the account is not expired, false otherwise.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Checks if the user's account is not locked.
     *
     * @return true if the account is not locked, false otherwise.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Checks if the user's credentials are not expired.
     *
     * @return true if the credentials are not expired, false otherwise.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Checks if the user is enabled.
     *
     * @return true if the user is enabled, false otherwise.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
