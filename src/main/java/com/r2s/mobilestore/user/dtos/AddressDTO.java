package com.r2s.mobilestore.user.dtos;

import com.r2s.mobilestore.user.entities.User;
import lombok.Data;

/**
 * Represents create, update address
 *
 * @author KhanhBD
 * @since 2023-10-09
 */
@Data
public class AddressDTO {
    private String name;
    private String phoneNumber;
    private String deliveryAddress;
    private String province;
    private String district;
    private String ward;
    private String label;
    private String isDefault;
}
