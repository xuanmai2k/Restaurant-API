package com.r2s.sample.user.controllers;

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

    /**
     * REST API methods for Retrieval operations
     *
     * @return list all of users
     */
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        Map<String, Object> jsonResponse = new LinkedHashMap<>();
        List<User> listOfUsers = userService.listAll();
        List<UserInfoDTO> listOfUsersDTO = new ArrayList<>();

        // Found
        if (!listOfUsers.isEmpty()) {
            //  To convert Entity to DTO
            for (User user: listOfUsers) {
                listOfUsersDTO.add(mapper.map(user, UserInfoDTO.class));
            }

            // Successfully
            jsonResponse.put(Response.ResponseKey.STATUS.getValue(), Response.ResponseValue.SUCCESSFULLY.getValue());
            jsonResponse.put(Response.ResponseKey.DATA.getValue(), listOfUsersDTO);

            return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        }

        // Not found
        jsonResponse.put(Response.ResponseKey.STATUS.getValue(), Response.ResponseValue.NOT_FOUND.getValue());
        jsonResponse.put(Response.ResponseKey.MESSAGE.getValue(), env.getProperty(Constants.DATA_NOT_FOUND));

        return new  ResponseEntity<>(jsonResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Build create user REST API
     *
     * @param registerDTO This is a user
     * @return Response entity
     */
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody RegisterDTO registerDTO) {
        Map<String, Object> jsonResponse = new LinkedHashMap<>();
        try {
            // Username is duplicated
            if (userService.existsByUsername(registerDTO.getUsername())) {
                jsonResponse.put(Response.ResponseKey.STATUS.getValue(), Response.ResponseValue.DUPLICATED.getValue());
                jsonResponse.put(Response.ResponseKey.MESSAGE.getValue(), env.getProperty(Constants.USERNAME_EXIST));

                return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
            }

            if (userService.existsByEmail(registerDTO.getEmail())) {
                jsonResponse.put(Response.ResponseKey.STATUS.getValue(), Response.ResponseValue.DUPLICATED.getValue());
                jsonResponse.put(Response.ResponseKey.MESSAGE.getValue(), env.getProperty(Constants.EMAIL_EXIST));

                return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
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
            jsonResponse.put(Response.ResponseKey.STATUS.getValue(), Response.ResponseValue.SUCCESSFULLY.getValue());
            jsonResponse.put(Response.ResponseKey.MESSAGE.getValue(), env.getProperty(Constants.DATA_SAVE_SUCCESSFULLY));

            return new ResponseEntity<>(jsonResponse, HttpStatus.CREATED);
        } catch (Exception ex) {
            logger.info(ex.getMessage());

            // Failed
            jsonResponse.put(Response.ResponseKey.STATUS.getValue(), Response.ResponseValue.FAILURE.getValue());
            jsonResponse.put(Response.ResponseKey.MESSAGE.getValue(), env.getProperty(Constants.DATA_SAVE_FAILED));

            return new ResponseEntity<>(jsonResponse, HttpStatus.EXPECTATION_FAILED);
        }
    }

    private Set<Role> getRoles(RegisterDTO registerDTO) {
        Set<Role> roles = new HashSet<>();
        Set<String> strRoles = registerDTO.getRoles();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException(env.getProperty(Constants.ROLE_NOT_FOUND)));
           roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case Constants.ADMIN -> {
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException(env.getProperty(Constants.ROLE_NOT_FOUND)));
                        roles.add(adminRole);
                    }
                    case Constants.MOD -> {
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException(env.getProperty(Constants.ROLE_NOT_FOUND)));
                        roles.add(modRole);
                    }
                    default -> {
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
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
        Map<String, Object> jsonMap = new LinkedHashMap<>();
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
                jsonMap.put(Response.ResponseKey.STATUS.getValue(), Response.ResponseValue.SUCCESSFULLY.getValue());
                jsonMap.put(Response.ResponseKey.DATA.getValue(), userService.get(id));

                return new ResponseEntity<>(jsonMap, HttpStatus.OK);
            }

            // Not found
            jsonMap.put(Response.ResponseKey.STATUS.getValue(), Response.ResponseValue.NOT_FOUND.getValue());
            jsonMap.put(Response.ResponseKey.MESSAGE.getValue(), env.getProperty(Constants.DATA_NOT_FOUND));

            return new ResponseEntity<>(jsonMap, HttpStatus.NOT_FOUND);

        } catch (Exception ex) {
            logger.info(ex.getMessage());

            // Exception
            jsonMap.put(Response.ResponseKey.STATUS.getValue(), Response.ResponseValue.FAILURE.getValue());
            jsonMap.put(Response.ResponseKey.MESSAGE.getValue(), env.getProperty(Constants.DATA_SAVE_FAILED));

            return new ResponseEntity<>(jsonMap, HttpStatus.EXPECTATION_FAILED);
        }
    }
}
