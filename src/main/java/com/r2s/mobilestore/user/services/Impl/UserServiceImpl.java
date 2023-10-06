package com.r2s.mobilestore.user.services.Impl;

import com.r2s.mobilestore.user.entities.User;
import com.r2s.mobilestore.user.repositories.OTPRepository;
import com.r2s.mobilestore.user.repositories.UserRepository;
import com.r2s.mobilestore.user.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Represents a user service
 * @author KhanhBD
 * @since 2023-10-03
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    /**
     * Create a Repository
     */
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OTPRepository otpRepository;

    /**
     * This method is used to list all users
     *
     * @return list of users
     */
    public List<User> listAll() {
        return userRepository.findAll();
    }

    /**
     * This method is used to get a user base on id
     *
     * @param id This is user id
     * @return user base on id
     */
    public Optional<User> get(long id) {
        return userRepository.findById(id);
    }

    /**
     * This method is used to create a user
     *
     * @param user This is a user
     */
    public User save(User user) {
        return userRepository.save(user);
    }


    /**
     * This method is used to delete a user by id
     *
     * @param user This is user
     */
    public void delete(User user) {
        userRepository.delete(user);
    }

    /**
     * This method is used to get user details service
     *
     * @param username This is username
     * @return user This is user
     */
    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByFullName(username);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByFullName(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

}