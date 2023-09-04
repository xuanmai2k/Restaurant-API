package com.r2s.sample.user.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * User data transfer object
 * Data transfer objects are just data holders which are used to transport data between layers and tiers.
 *
 * @author kyle
 * @since 2023-09-02
 */
@Data
public class UserInfoDTO {
    /**
     * User id
     */
    private long id;

    /**
     * Represents the username
     */
    private String username;

    /**
     * Represents the mobile number
     */
    private String mobileNo;

    /**
     * Represents the email address
     */
    private String emailId;

    /**
     * Represent the city
     */
    private String city;
}
