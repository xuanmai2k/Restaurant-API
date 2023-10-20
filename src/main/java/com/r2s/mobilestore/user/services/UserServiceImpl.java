package com.r2s.mobilestore.user.services;

import com.r2s.mobilestore.dtos.PageDTO;
import com.r2s.mobilestore.user.entities.User;
import com.r2s.mobilestore.user.repositories.OTPRepository;
import com.r2s.mobilestore.user.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    private MessageSource messageSource;

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
     * This method is used to confirm exists Email
     *
     * @param email This is email
     * @return boolean
     */
    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * This method is used to get a user base on email
     *
     * @param email This is user email
     * @return user base on email
     */
    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * This method is used to get a user base on id
     *
     * @param userId This is user id
     * @return user base on id
     */
    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.findUserById(userId);
    }

    /**
     * This method is used to get all user
     *
     * @return user list
     */
    @Override
    public Page<User> getAllUsers(PageDTO pageDTO) {
        return userRepository.findAll(PageRequest.of(pageDTO.getPageNumber(), pageDTO.getPageSize()));
    }

    /**
     * This method is used to delete a user base on id
     *
     * @param userId This is userId
     */
    @Override
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    /**
     * Searches for users matching the provided search term with pagination.
     *
     * @param pageDTO     The PageDTO containing pagination information (pageNumber, pageSize).
     * @param searchTerm  The search term to filter users by (can be a name, email, or phone number).
     * @return            A Page of User entities that match the search criteria.
     */
    @Override
    public Page<User> searchUsersWithPagination(PageDTO pageDTO, String searchTerm) {
        // Extract page number and page size from PageDTO
        int pageNumber = pageDTO.getPageNumber();
        int pageSize = pageDTO.getPageSize();

        // Create a pageable object for pagination
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        // Use the UserRepository to perform a case-insensitive search based on various user fields
        return userRepository
                .searchUsersWithPagination(
                searchTerm, pageable);
    }

}