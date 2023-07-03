package com.epam.esm.service;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.RoleDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserSlimDto;
import com.epam.esm.entity.RoleType;
import com.epam.esm.entity.User;
import com.epam.esm.exception.UnauthorizedAccessException;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of the UserService interface.
 * <p>
 * Provides methods for managing user data.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final UserMapper mapper;

    /**
     * Get a user DTO by ID.
     *
     * @param id       the user ID
     * @param username name
     * @return the user DTO
     * @throws UserNotFoundException       if the user is not found
     * @throws UnauthorizedAccessException has a different ID than the specified userId.
     */
    @Override
    @Transactional(readOnly = true)
    public UserDto getById(final Long id, String username) {
        User user = findById(id);
        if (!user.getUsername().equals(username)) {
            throw new UnauthorizedAccessException("Unauthorized Access :" + username);
        }
        return mapper.toDto(user);
    }

    /**
     * Get a user entity by ID.
     *
     * @param id the user ID
     * @return the user entity
     * @throws UserNotFoundException if the user is not found
     */
    @Override
    @Transactional(readOnly = true)
    public User findById(final Long id) {
        return userDao.getById(id).orElseThrow(() ->
                new UserNotFoundException(
                        String.format("User not found with id %d", id)));
    }

    /**
     * Get all users with pagination.
     *
     * @param pageable the pageable information
     * @return the page of user DTOs
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> getAll(final Pageable pageable) {
        List<UserDto> dtos = mapper.toDtoList(
                userDao.getAllBy(pageable));
        return new PageImpl<>(dtos, pageable, dtos.size());
    }

    /**
     * Saves a new user.
     *
     * @param dto The UserDto object containing the user information to save.
     * @return The UserDto object of the saved user.
     */
    @Override
    @Transactional
    public UserDto save(
            final UserSlimDto dto) {
        dto.setRole(RoleDto.builder()
                .permission(RoleType.USER).build());
        User user = userDao.save(
                mapper.toEntity(dto));
        return mapper.toDto(user);
    }

    /**
     * Updates an existing user.
     *
     * @param dto The UserSlimDto object containing the updated user information.
     * @return The UserDto object of the updated user.
     */
    @Override
    @Transactional
    public UserDto update(
            final UserSlimDto dto) {
        User user = userDao.update(
                mapper.toEntity(dto));
        return mapper.toDto(user);
    }

    /**
     * Deletes a user by ID.
     *
     * @param id The ID of the user to delete.
     */
    @Override
    @Transactional
    public void delete(
            final Long id) {
        userDao.delete(id);
    }
}
