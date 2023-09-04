package com.r2s.sample.user.services;

import com.r2s.sample.category.entities.Category;
import com.r2s.sample.category.repositories.CategoryRepository;
import com.r2s.sample.user.entities.User;
import com.r2s.sample.user.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Represents a user service
 * @author kyle
 * @since 2023-08-31
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    /**
     * Create a Repository
     */
    @Autowired
    private UserRepository userRepository;

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
    public void save(User user) {
        userRepository.save(user);
    }


    /**
     * This method is used to delete a user by id
     *
     * @param user This is user
     */
    public void delete(User user) {
        userRepository.delete(user);
    }
}