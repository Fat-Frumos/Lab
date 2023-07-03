package com.epam.esm.service;

import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserSlimDto;
import com.epam.esm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing users.
 */
public interface UserService {

    /**
     * Find a user by ID.
     *
     * @param id the user ID
     * @return the user entity
     */
    User findById(Long id);

    /**
     * Get a user by ID.
     *
     * @param id       the user ID
     * @param username the name of user
     * @return the user DTO
     */
    UserDto getById(Long id, String username);

    /**
     * Get all users with pagination.
     *
     * @param pageable the pageable information
     * @return the page of user DTOs
     */
    Page<UserDto> getAll(Pageable pageable);

    UserDto save(UserSlimDto dto);

    UserDto update(UserSlimDto dto);

    void delete(Long id);
}
