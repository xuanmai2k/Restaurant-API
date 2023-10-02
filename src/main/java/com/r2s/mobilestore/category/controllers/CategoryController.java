package com.r2s.mobilestore.category.controllers;

import com.r2s.mobilestore.category.entities.Category;
import com.r2s.mobilestore.category.services.CategoryService;
import com.r2s.mobilestore.dtos.ResponseDTO;
import com.r2s.mobilestore.enums.Response;
import com.r2s.mobilestore.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * Represents a category controller
 *
 * @author kyle
 * @since 2023-08-31
 */
@RestController
@RequestMapping("${category}")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Read properties Using the MessageSource Object with parameters
     */
    @Autowired
    private MessageSource messageSource;

    private final ResponseDTO body = ResponseDTO.getInstance();

    /**
     * Logging in Spring Boot
     */
    Logger logger = LoggerFactory.getLogger(CategoryController.class);

    /**
     * Read properties Using the Environment Object without parameters
     */
    @Autowired
    private Environment env;

    /**
     * REST API methods for Retrieval operations
     *
     * @return list all of categories
     */
    @GetMapping
    public ResponseEntity<?> getAllCategories(@RequestParam(required = false) String categoryName) {
        try {
            List<Category> listOfCategories = new ArrayList<>();
            // Get all
            if (categoryName == null) {
                listOfCategories = categoryService.listAll();
            } else { // Filter by category name
                listOfCategories = categoryService.filterByName(categoryName);
            }

            // For testing
            logger.info("server name: " + env.getProperty(Constants.SERVER_NAME));

            // Category is found
            if (!listOfCategories.isEmpty()) {
                return new ResponseEntity<>(listOfCategories, HttpStatus.OK);
            }

            // No content
            body.setResponse(Response.Key.STATUS, Response.Value.NOT_FOUND);
            return new ResponseEntity<>(body, HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            logger.info(ex.getMessage());

            // Failed
            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Build create category REST API
     *
     * @param category This is a category
     * @return a category is inserted into database
     */
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    @PreAuthorize(value = "isAuthenticated()")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MODERATOR')")
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        try {
            categoryService.save(category);

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
     * Build get category by id REST API
     *
     * @param id This is category id
     * @return a category
     */
    @GetMapping("{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable long id) {
        try {
            Optional<Category> category = categoryService.get(id);

            // Found
            if (category.isPresent()) {
                // Successfully
                return new ResponseEntity<>(category.get(), HttpStatus.OK);
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
     * Build update category REST API
     *
     * @param id       This is category id
     * @param category This category details
     * @return category is updated
     */
    @PutMapping("{id}")
    public ResponseEntity<?> updateCategory(@PathVariable long id, @RequestBody Category category) {
        try {
            Optional<Category> updateCategory = categoryService.get(id);

            // Found
            if (updateCategory.isPresent()) {
                Category _category = updateCategory.get();
                // Update new category name
                _category.setCategoryName(category.getCategoryName());

                // Save category into database
//                categoryService.save(_category);

                // Save category into database Successfully
//                body.setResponse(Response.Key.STATUS, Response.Value.SUCCESSFULLY);
                return new ResponseEntity<>(categoryService.save(_category), HttpStatus.OK);
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
     * Build delete category REST API
     *
     * @param id This is category
     * @return http status
     */
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable long id) {
        try {
            categoryService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            logger.info(ex.getMessage());

            // Failed
            body.setResponse(Response.Key.STATUS, Response.Value.FAILURE);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
