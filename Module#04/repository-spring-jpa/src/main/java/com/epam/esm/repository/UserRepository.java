package com.epam.esm.repository;

import com.epam.esm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
    public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = { "orders" })
    Optional<User> findByUsername(String name);

        Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = {"orders", "orders.certificates", "orders.certificates.tags", "role", "role.authorities"})
    Page<User> findAll(Pageable pageable);
}
