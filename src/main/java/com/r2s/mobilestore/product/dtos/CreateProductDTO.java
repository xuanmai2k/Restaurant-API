//package com.r2s.mobilestore.product.dtos;
//
//import jakarta.validation.constraints.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//
//import static com.r2s.mobilestore.utils.Constants.*;
//
///**
// * Custom response status
// *
// * @author xuanmai
// * @since 2023-10-06
// */
//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//public class CreateProductDTO {
//
//    /**
//     * Represents the product’s code.
//     */
//    private String productCode;
//
//    /**
//     * Represents the response name.
//     */
//    @NotBlank(message = NOT_REQUIRE)
//    @Size(max = 255, message = PRODUCT_NAME_MAX_CHARACTERS)
//    private String name;
//
//    /**
//     * Represents the response price.
//     */
//    @NotNull(message = NOT_REQUIRE)
//    @DecimalMin(value = "0", inclusive = true, message = PRODUCT_PRICE_MIN_VALUE)
//    @DecimalMax(value = "999999.99", inclusive = true, message = PRODUCT_PRICE_MAX_VALUE)
//    private Double price;
//
//    /**
//     * Represents the response discount price.
//     */
//    @DecimalMin(value = "0", inclusive = true, message = PRODUCT_PRICE_MIN_VALUE)
//    @DecimalMax(value = "999999.99", inclusive = true, message = PRODUCT_PRICE_MAX_VALUE)
//    private Double discountPrice;
//
//    /**
//     * Represents the response quantity.
//     */
//    @NotNull(message = NOT_REQUIRE)
//    @Min(value = 0, message = PRODUCT_QUANTITY_MIN_VALUE)
//    @Max(value = 999999, message = PRODUCT_QUANTITY_MAX_VALUE)
//    private Integer quantity;
//
//    /**
//     * Represents the response description.
//     */
//    @NotBlank(message = NOT_REQUIRE)
//    @Size(min = 1, max = 1000, message = PRODUCT_MAX_MIN_CHARACTERS)
//    private String description;
//
//    /**
//     * Represents the response types of ROM.
//     */
//    @NotBlank(message = NOT_REQUIRE)
//    @Size(min = 1, max = 1000, message = PRODUCT_MAX_MIN_CHARACTERS)
//    private String memoryTypes;
//
//    /**
//     * Represents the response color.
//     */
//    @NotBlank(message = NOT_REQUIRE)
//    @Size(min = 1, max = 1000, message = PRODUCT_MAX_MIN_CHARACTERS)
//    private String color;
//
//    /**
//     * Represents the response condition.
//     */
//    @NotBlank(message = NOT_REQUIRE)
//    @Size(min = 1, max = 20, message = PRODUCT_CONDITION_MAX_MIN_CHARACTERS)
//    private String productCondition;
//
//    /**
//     * Represents the response category.
//     */
//    private String category;
//
//    /**
//     * Represents the response manufacturer.
//     */
//    private String manufacturer;
//
//    /**
//     * Represents the response images.
//     */
//    private List<MultipartFile> images;
//}
