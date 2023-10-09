package com.r2s.mobilestore.user.services;

import com.r2s.mobilestore.user.entities.User;

import java.util.List;
import java.util.Optional;

/**
 * Represents a user service
 * @author KhanhBD
 * @since 2023-10-03
 */
public interface UserService {

    /**
     * This method is used to list all users
     *
     * @return list of users
     */
    public List<User> listAll();

    /**
     * This method is used to get a user base on id
     *
     * @param id This is user id
     * @return user base on id
     */
    public Optional<User> get(long id);

    /**
     * This method is used to create a user
     *
     * @param user This is a user
     */
    public User save(User user);

    /**
     * This method is used to delete a user by id
     *
     * @param user This is a user
     */
    public void delete(User user);

    /**
     * This method is used to confirm exists Email
     *
     * @param email This is email
     * @return boolean
     */
    Boolean existsByEmail(String email);

    /**
     * This method is used to get a user base on email
     *
     * @param email This is user email
     * @return user base on email
     */
    Optional<User> getUserByEmail(String email);
}