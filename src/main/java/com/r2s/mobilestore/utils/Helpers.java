package com.r2s.mobilestore.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

/**
 * A helper class provides functionalities necessary for the overall running of a Java program.
 * Helper classes contain methods used by other classes to perform repetitive tasks,
 * which aren't the core purpose of an application.
 *
 * @author kyle
 * @since 2023-09-03
 */
public class Helpers {

    /**
     * Password hashing function
     *
     * @param plainPassword Users' passwords before hashing
     * @return Password hashing
     */
    public static String hashPassword(String plainPassword) {
        int strength = 10; // work factor of bcrypt
        BCryptPasswordEncoder bCryptPasswordEncoder =
                new BCryptPasswordEncoder(strength, new SecureRandom());

        return bCryptPasswordEncoder.encode(plainPassword);
    }
}
