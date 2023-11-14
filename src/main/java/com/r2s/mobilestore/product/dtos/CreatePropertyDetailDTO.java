package com.r2s.mobilestore.product.dtos;

import com.r2s.mobilestore.product.entities.Property;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreatePropertyDetailDTO {
    private List<Property> propertyList;
}
