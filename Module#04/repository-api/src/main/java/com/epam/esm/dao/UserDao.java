package com.epam.esm.dao;

import com.epam.esm.entity.User;

/**
 * Data access interface for managing users.
 */
public interface UserDao extends Dao<User> {
    /**
     * Updates the specified User.
     *
     * @param user the User to update
     * @return the updated User
     */
    User update(User user);
}
