package com.shutterfly.services.example.services.api;

import com.shutterfly.services.example.model.User;

import java.util.List;

/**
 * Service to manage the User resource
 */
public interface IUserService {
    /**
     * Find and return all users in the system.
     */
    List<User> findAllUsers();

    /**
     * Find a specific user by id
     */
    User findUserById(final String userId);
}
