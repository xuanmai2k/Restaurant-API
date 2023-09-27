package com.r2s.sample.user.controllers;

import com.r2s.sample.dtos.ResponseDTO;
import com.r2s.sample.enums.Response;
import com.r2s.sample.user.dtos.RegisterDTO;
import com.r2s.sample.user.dtos.UserInfoDTO;
import com.r2s.sample.user.entities.Role;
import com.r2s.sample.user.entities.User;
import com.r2s.sample.user.models.ERole;
import com.r2s.sample.user.repositories.RoleRepository;
import com.r2s.sample.user.services.UserService;
import com.r2s.sample.utils.Constants;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Represents a user controller
 *
 * @author kyle
 * @since 2023-09-02
 */
@RestController
@RequestMapping("${user.user}")
public class UserController {

    /**
     * Create a Service
     */
    @Autowired
    private UserService userService;

    /**
     * Read properties Using the MessageSource Object with parameters
     */
    @Autowired
    private MessageSource messageSource;

    /**
     * Read properties Using the Environment Object without parameters
     */
    @Autowired
    private Environment env;

    /**
     * This will help to convert Entity to DTO and vice versa automatically.
     */
    @Autowired
    private ModelMapper mapper;

    /**
     * Logging in Spring Boot
     */
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    private final ResponseDTO body = ResponseDTO.getInstance();

    /**
     * REST API methods for Retrieval operations
     *
     * @return list all of users
     */
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> listOfUsers = userService.listAll();
            List<UserInfoDTO> listOfUsersDTO = new ArrayList<>();

            // Found
            if (!listOfUsers.isEmpty()) {
                //  To convert Entity to DTO
                for (User user: listOfUsers) {
                    listOfUsersDTO.add(mapper.map(user, UserInfoDTO.class));
                }

                // Successfully
                return new ResponseEntity<>(listOfUsersDTO, HttpStatus.OK);
            }

            // Not found
            body.setResponse(Response.Key.STATUS, Response.Value.NOT_FOUND);
            return new  ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logger.info(ex.getMessage());

            // Failed
            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Build create user REST API
     *
     * @param registerDTO This is a user
     * @return Response entity
     */
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody RegisterDTO registerDTO) {
        try {
            // Username is duplicated
            if (userService.existsByUsername(registerDTO.getUsername())) {
                body.setResponse(Response.Key.STATUS, Response.Value.DUPLICATED);
                return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
            }

            // Email is duplicated
            if (userService.existsByEmail(registerDTO.getEmail())) {
                body.setResponse(Response.Key.STATUS, Response.Value.DUPLICATED);
                return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
            }

            // Convert DTO to an entity
            User user = mapper.map(registerDTO, User.class);

            // Password hashing
//            user.setPassword(Helpers.hashPassword(user.getPassword()));
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            user.setRoles(getRoles(registerDTO));

            // Create
            userService.save(user);

            // Successfully
            body.setResponse(Response.Key.STATUS, Response.Value.SUCCESSFULLY);
            return new ResponseEntity<>(body, HttpStatus.CREATED);
        } catch (Exception ex) {
            logger.info(ex.getMessage());

            // Failed
            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get all roles
     *
     * @param registerDTO This information to sign up
     * @return Set<Role> List of roles
     */
    private Set<Role> getRoles(RegisterDTO registerDTO) throws Exception{
        Set<Role> roles = new HashSet<>();
        Set<String> strRoles = registerDTO.getRoles();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER.toString())
                    .orElseThrow(() -> new RuntimeException(env.getProperty(Constants.ROLE_NOT_FOUND)));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case Constants.ADMIN -> {
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN.toString())
                                .orElseThrow(() -> new RuntimeException(env.getProperty(Constants.ROLE_NOT_FOUND)));
                        roles.add(adminRole);
                    }
                    case Constants.MOD -> {
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR.toString())
                                .orElseThrow(() -> new RuntimeException(env.getProperty(Constants.ROLE_NOT_FOUND)));
                        roles.add(modRole);
                    }
                    default -> {
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER.toString())
                                .orElseThrow(() -> new RuntimeException(env.getProperty(Constants.ROLE_NOT_FOUND)));
                        roles.add(userRole);
                    }
                }
            });
        }

        return roles;
    }

    /**
     * Update user profile
     *
     * @param id This user id
     * @param userInfoDTO This user profile
     * @return Response entity
     */
    @PutMapping("{id}")
    public ResponseEntity<?> updateUserProfile(@PathVariable long id, @RequestBody UserInfoDTO userInfoDTO) {
        try {
            Optional<User> userOptional = userService.get(id);

            // Found
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // Update info
                user.setEmail(userInfoDTO.getEmail());
                user.setMobileNo(userInfoDTO.getMobileNo());
                user.setCity(userInfoDTO.getCity());

                // Save
                userService.save(user);

                // Successfully
                body.setResponse(Response.Key.STATUS, Response.Value.SUCCESSFULLY);
                return new ResponseEntity<>(body, HttpStatus.OK);
            }

            // Not found
            body.setResponse(Response.Key.STATUS, Response.Value.NOT_FOUND);
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);

        } catch (Exception ex) {
            logger.info(ex.getMessage());

            // Exception
            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
