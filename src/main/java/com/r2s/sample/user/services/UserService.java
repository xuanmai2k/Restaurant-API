package com.r2s.sample.user.services;

import com.r2s.sample.user.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

/**
 * Represents a user service
 * @author kyle
 * @since 2023-09-02
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

    //====== ADD 2023/09/07 kyle START ======//
    /**
     * This method is used to get user details service
     *
     * @param username This is username
     * @return user This is user
     */
    public Optional<User> getUserByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
    //====== ADD 2023/09/07 kyle END ======//
}