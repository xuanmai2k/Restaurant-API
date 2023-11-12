package com.r2s.mobilestore.product.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateProductDTO {
    private String productName;
    private Long category;
    private Long manufacturer;
    private String productCondition;
    private String description;
    private List<String> capacityList;
    private List<String> colorList;
    private List<String> configurationList;
    private List<String> connectionSupportList;
    private List<MultipartFile> images;
}
