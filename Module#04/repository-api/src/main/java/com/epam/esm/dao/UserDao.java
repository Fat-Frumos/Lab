package com.epam.esm.dao;

import com.epam.esm.entity.User;
import org.springframework.stereotype.Repository;

/**
 * Data access interface for managing users.
 */
@Repository
public interface UserDao extends Dao<User> {
}
