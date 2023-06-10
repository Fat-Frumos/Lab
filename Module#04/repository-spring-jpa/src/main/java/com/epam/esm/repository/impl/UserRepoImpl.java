package com.epam.esm.repository.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.User;
import com.epam.esm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toCollection;

@RequiredArgsConstructor
public class UserRepoImpl implements UserDao {

    private final UserRepository userRepository;

    /**
     * Retrieves all entities.
     *
     * @param pageable the pagination information
     * @return a list of entities
     */
    @Override
    public List<User> getAllBy(final Pageable pageable) {
        return userRepository.findAll(pageable)
                .stream()
                .collect(toCollection(ArrayList::new));
    }

    /**
     * Retrieves an entity by its ID.
     *
     * @param id the ID of the entity
     * @return an optional containing the entity,
     * or an empty optional if not found
     */
    @Override
    public Optional<User> getById(final Long id) {
        return userRepository.findById(id);
    }

    /**
     * Retrieves an entity by its name.
     *
     * @param name the name of the entity
     * @return an optional containing the entity,
     * or an empty optional if not found
     */
    @Override
    public Optional<User> findByUsername(final String name) {
        return userRepository.findByUsername(name);
    }

    /**
     * Saves an entity.
     *
     * @param user the entity to save
     * @return the saved entity
     */
    @Override
    public User save(final User user) {
        return userRepository.save(user);
    }

    /**
     * Deletes an entity by its ID.
     *
     * @param id the ID of the entity to delete
     */
    @Override
    public void delete(final Long id) {
        userRepository.deleteById(id);
    }
}
