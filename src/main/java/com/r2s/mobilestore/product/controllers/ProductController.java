//package com.r2s.mobilestore.product.controllers;
//
//import com.r2s.mobilestore.dtos.PageDTO;
//import com.r2s.mobilestore.dtos.ResponseDTO;
//import com.r2s.mobilestore.enums.Response;
//import com.r2s.mobilestore.product.dtos.CreateProductDTO;
//import com.r2s.mobilestore.product.dtos.SearchProductDTO;
//import com.r2s.mobilestore.product.entities.Product;
//import com.r2s.mobilestore.product.services.ProductService;
//import jakarta.validation.Valid;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//import java.util.Optional;
//
///**
// * Represents a product controller
// *
// * @author xuanmai
// * @since 2023-10-06
// */
//@RestController
//@RequestMapping("${product}")
//public class ProductController {
//    @Autowired
//    private ProductService productService;
//
//    private final ResponseDTO body = ResponseDTO.getInstance();
//
//    /**
//     * Logging in Spring Boot
//     */
//    Logger logger = LoggerFactory.getLogger(ProductController.class);
//
//    /**
//     * REST API methods for Retrieval operations
//     *
//     * @param pageDTO This is a page
//     * @return list all of products
//     */
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
//    @GetMapping()
//    public ResponseEntity<?> getAllProducts(@RequestBody PageDTO pageDTO) {
//        try {
//            Page<Product> productList = productService.getAllProducts(pageDTO);
//
//            //Not found
//            if (productList.isEmpty()) {
//
//                //No content
//                body.setResponse(Response.Key.STATUS, Response.Value.NOT_FOUND);
//                return new ResponseEntity<>(body, HttpStatus.NO_CONTENT);
//            }
//
//            //Found
//            return new ResponseEntity<>(productList, HttpStatus.OK);
//
//        } catch (Exception ex) {
//            logger.info(ex.getMessage());
//
//            //Failed
//            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
//            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    /**
//     * Build get product by id REST API
//     *
//     * @param id This is product id
//     * @return a product
//     */
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
//    @GetMapping("{id}")
//    public ResponseEntity<?> getProductById(@PathVariable Long id) {
//        try {
//            Optional<Product> product = productService.getProductById(id);
//
//            //Found
//            if (product.isPresent()) {
//                //Successfully
//                return new ResponseEntity<>(product.get(), HttpStatus.OK);
//            }
//
//            //Not found
//            body.setResponse(Response.Key.STATUS, Response.Value.NOT_FOUND);
//            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
//
//        } catch (Exception ex) {
//            logger.info(ex.getMessage());
//
//            // Failed
//            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
//            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    /**
//     * Build create product REST API
//     *
//     * @param createProductDTO This is a product
//     * @return a product is inserted into database
//     */
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    @PostMapping
//    public ResponseEntity<?> createProduct(@ModelAttribute @Valid CreateProductDTO createProductDTO) throws IOException {
//        try {
//            Product product = productService.createProduct(createProductDTO);
//
//            //Successfully
//            body.setResponse(Response.Key.STATUS, Response.Value.SUCCESSFULLY);
//            return new ResponseEntity<>(body, HttpStatus.CREATED);
//        } catch (Exception ex) {
//            logger.info(ex.getMessage());
//
//            //Failed
//            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
//            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    /**
//     * Build update product REST API
//     *
//     * @param id               This is product id
//     * @param createProductDTO This product details
//     * @return product is updated
//     */
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    @PutMapping("{id}")
//    public ResponseEntity<?> updateProduct(@PathVariable Long id, @ModelAttribute @Valid CreateProductDTO createProductDTO) {
//        try {
//            Optional<Product> product = productService.getProductById(id);
//
//            //Product is found
//            if (product.isPresent()) {
//                Product updateProduct = productService.updateProduct(createProductDTO, id);
//
//                // Successfully
//                return new ResponseEntity<>(updateProduct, HttpStatus.OK);
//            }
//
//            // Product is not found
//            body.setResponse(Response.Key.STATUS, Response.Value.NOT_FOUND);
//            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
//        } catch (Exception ex) {
//            logger.info(ex.getMessage());
//
//            // Failed
//            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
//            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    /**
//     * Build delete product REST API
//     *
//     * @param id This is product id
//     * @return http status
//     */
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    @DeleteMapping("{id}")
//    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
//        try {
//            Optional<Product> product = productService.getProductById(id);
//
//            //Found
//            if (product.isPresent()) {
//                productService.deleteProduct(product.get());
//                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//            }
//
//            // Not Found
//            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
//
//        } catch (Exception ex) {
//            logger.info(ex.getMessage());
//
//            // Failed
//            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
//            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    /**
//     * Build search product REST API
//     *
//     * @param searchProductDTO There are keyword, category and manufacturer
//     * @return list of products
//     */
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
//    @GetMapping("/search")
//    public ResponseEntity<?> search(@RequestBody SearchProductDTO searchProductDTO) {
//        try {
//            Page<Product> productList = productService.search(searchProductDTO);
//
//            //Not found
//            if (productList.isEmpty()) {
//                //No content
//                body.setResponse(Response.Key.STATUS, Response.Value.NOT_FOUND);
//                return new ResponseEntity<>(body, HttpStatus.NO_CONTENT);
//            }
//
//            //Found
//            return new ResponseEntity<>(productList, HttpStatus.OK);
//
//        } catch (Exception ex) {
//            logger.info(ex.getMessage());
//
//            //Failed
//            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
//            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//}
