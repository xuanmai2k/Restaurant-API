package com.r2s.sample.user.services;

import com.r2s.sample.user.entities.User;
import com.r2s.sample.user.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
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

    //====== ADD 2023/09/05 kyle START ======//
    @Autowired
    private ModelMapper modelMapper;
    //====== ADD 2023/09/05 kyle END ======//

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

    //====== ADD 2023/09/05 kyle START ======//
    /**
     * This method is used to get user details service
     *
     * @param username This is username
     * @return user This is user
     */
    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    //====== ADD 2023/09/05 kyle END ======//
}