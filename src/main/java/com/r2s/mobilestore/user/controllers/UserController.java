package com.r2s.mobilestore.user.controllers;

import com.r2s.mobilestore.category.entities.Category;
import com.r2s.mobilestore.dtos.ResponseDTO;
import com.r2s.mobilestore.enums.Response;
import com.r2s.mobilestore.exceptions.ResourceNotFoundException;
import com.r2s.mobilestore.user.dtos.ChangePasswordDTO;
import com.r2s.mobilestore.user.dtos.EmailDTO;
import com.r2s.mobilestore.user.dtos.RegisterDTO;
import com.r2s.mobilestore.user.dtos.UpdateUserDTO;
import com.r2s.mobilestore.user.entities.Role;
import com.r2s.mobilestore.user.entities.User;
import com.r2s.mobilestore.user.models.ERole;
import com.r2s.mobilestore.user.repositories.RoleRepository;
import com.r2s.mobilestore.user.services.EmailService;
import com.r2s.mobilestore.user.services.OTPService;
import com.r2s.mobilestore.user.services.UserService;
import com.r2s.mobilestore.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents a user controller
 *
 * @author KhanhBD
 * @since 2023-10-03
 */
@RestController
@RequiredArgsConstructor
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

    private final AuthenticationManager authManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OTPService otpService;

    private final ResponseDTO body = ResponseDTO.getInstance();

    /**
     * Build get User By id to view information
     *
     * @param userId This is user id
     * @return status get user
     */
    @GetMapping("{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        try {
            Optional<User> user = userService.getUserById(userId);

            // Found
            if (user.isPresent()) {
                // Successfully
                return new ResponseEntity<>(user.get(), HttpStatus.OK);
            }

            // Not found
            body.setResponse(Response.Key.STATUS, Response.Value.NOT_FOUND);
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logger.info(ex.getMessage());

            // Failed
            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Build update user REST API
     *
     * @param id       This is user id
     * @param updateUserDTO This user details
     * @return user is updated
     */
    @PutMapping("{id}")
    public ResponseEntity<?> updateUser(@PathVariable long id, @RequestBody UpdateUserDTO updateUserDTO) {
        try {
            Optional<User> updateUser = userService.get(id);

            // Found
            if (updateUser.isPresent()) {
                User user = updateUser.get();

                // Update new user
                user.setFullName(updateUserDTO.getFullName());
                user.setUsername(updateUserDTO.getUsername());
                user.setEmail(updateUserDTO.getEmail());
                user.setGender(updateUserDTO.getGender());
                user.setDateOfBirth(updateUserDTO.getDateOfBirth());

                return new ResponseEntity<>(userService.save(user), HttpStatus.OK);
            }

            // Not found
            body.setResponse(Response.Key.STATUS, Response.Value.NOT_FOUND);
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logger.info(ex.getMessage());

            // Failed
            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Build send Otp when user register
     *
     * @param emailDTO This is user email
     * @return status send email
     */
    @PostMapping
    @Transactional
    public ResponseEntity<?> sendOTP(@RequestBody EmailDTO emailDTO) {
        try {
            // cleanup Expired Otp
            otpService.cleanupExpiredOTP();

            // Generate an Otp (One-Time Password)
            String otp = otpService.createOrUpdateOTP(emailDTO.getEmail()).getOtpCode();

            // Send an email with the Otp
            emailService.sendEmail(emailDTO.getEmail(),
                    messageSource.getMessage(
                            Constants.EMAIL_SUBJECT, null, Locale.ENGLISH),
                    messageSource.getMessage(
                            Constants.EMAIL_MESSAGE, new Object[] {otp}, Locale.ENGLISH));

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
     * Build create User when user verify email by otp code
     *
     * @param registerDTO This is a verifyDTO
     * @return status register
     */
    @PostMapping("${user.create}")
    public ResponseEntity<?> createUser(@RequestBody RegisterDTO registerDTO) {
        try {
            // Email is duplicated
            if (userService.existsByEmail(registerDTO.getEmail())) {
                body.setResponse(Response.Key.STATUS,  messageSource.getMessage(
                        Constants.EMAIL_EXIST, null, Locale.ENGLISH));
                return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
            }


            // Check Otp authentication code
            boolean isOTPValid = otpService.isOTPValid(registerDTO.getEmail(), registerDTO.getOtpCode());

            if (!isOTPValid) {
                // The Otp code is invalid or has expired
                body.setResponse(Response.Key.STATUS, Response.Value.INVALID_OTP);
                return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
            }

            // Convert DTO to an entity
            User user = mapper.map(registerDTO, User.class);

            // Password hashing
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            Set<Role> roles = new HashSet<>();
            Role userRole = roleRepository.findByName(ERole.ROLE_USER.toString())
                    .orElseThrow(() -> new RuntimeException(env.getProperty(Constants.ROLE_NOT_FOUND)));
            roles.add(userRole);
            user.setRoles(roles);

            user.setCreatedAt(LocalDateTime.now());
            user.setModifiedAt(LocalDateTime.now());

            // Delete Otp code after use (optional)
            otpService.deleteOTP(registerDTO.getEmail());

            // Save a user into db
            userService.save(user);

            // The user has been created successfully
            body.setResponse(Response.Key.STATUS, Response.Value.SUCCESSFULLY);
            return new ResponseEntity<>(body, HttpStatus.CREATED);
        } catch (Exception ex) {
            logger.info(ex.getMessage());

            // Failed
            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("${user.change-password}/{id}")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody ChangePasswordDTO changePasswordDTO) {
        try {
            // Check old password
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(changePasswordDTO.getEmail(),
                            changePasswordDTO.getOldPassword()));

            // Check Otp authentication code
            boolean isOTPValid = otpService.isOTPValid(changePasswordDTO.getEmail(), changePasswordDTO.getOtpCode());

            if (!isOTPValid) {
                // The Otp code is invalid or has expired
                body.setResponse(Response.Key.STATUS, Response.Value.INVALID_OTP);
                return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
            }

            // find user by id
            Optional<User> updateUser = userService.get(id);

            // Delete Otp code
            otpService.deleteOTP(changePasswordDTO.getEmail());

            // Save a user into db
            // Found
            if (updateUser.isPresent()) {

                User user = updateUser.get();
                user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));

                userService.save(user);

                body.setResponse(Response.Key.STATUS, Response.Value.SUCCESSFULLY);
                return new ResponseEntity<>(body, HttpStatus.OK);
            }

            // Not found
            body.setResponse(Response.Key.STATUS, Response.Value.NOT_FOUND);
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logger.info(ex.getMessage());

            // Failed
            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
