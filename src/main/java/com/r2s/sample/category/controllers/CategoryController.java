package com.r2s.sample.category.controllers;


import com.r2s.sample.category.entities.Category;
import com.r2s.sample.category.services.CategoryService;
import com.r2s.sample.enums.Response;
import com.r2s.sample.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * Represents a category controller
 *
 * @author kyle
 * @since 2023-08-31
 */
@CrossOrigin("${"+Constants.SERVER_NAME+"}")
@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Read properties Using the MessageSource Object with parameters
     */
    @Autowired
    private MessageSource messageSource;

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
    public ResponseEntity<?> getAllCategories() {
        Map<String, Object> jsonResponse = new LinkedHashMap<>();
        List<Category> listOfCategories = categoryService.listAll();

        // For testing
        logger.info("server name: " + env.getProperty(Constants.SERVER_NAME));

        // Category is found
        if (!listOfCategories.isEmpty()) {
            // Response
            jsonResponse.put(Response.ResponseKey.STATUS.getValue(), Response.ResponseValue.SUCCESSFULLY.getValue());
            jsonResponse.put(Response.ResponseKey.DATA.getValue(), listOfCategories);

            // Successfully
            return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        }

        // Category is not found
        jsonResponse.put(Response.ResponseKey.STATUS.getValue(), Response.ResponseValue.NOT_FOUND.getValue());
        jsonResponse.put(Response.ResponseKey.MESSAGE.getValue(), env.getProperty(Constants.DATA_NOT_FOUND));

        return new ResponseEntity<>(jsonResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Build create category REST API
     *
     * @param category This is a category
     * @return a category is inserted into database
     */
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        Map<String, Object> jsonResponse = new LinkedHashMap<>();
        try {
            categoryService.save(category);

            // Response
            jsonResponse.put(Response.ResponseKey.STATUS.getValue(), Response.ResponseValue.SUCCESSFULLY.getValue());
            jsonResponse.put(Response.ResponseKey.MESSAGE.getValue(), env.getProperty(Constants.DATA_SAVE_SUCCESSFULLY));

            // Successfully
            return new ResponseEntity<>(jsonResponse, HttpStatus.CREATED);
        } catch (Exception ex) {
            logger.info(ex.getMessage());

            // Failed
            jsonResponse.put(Response.ResponseKey.STATUS.getValue(), Response.ResponseValue.FAILURE.getValue());
            jsonResponse.put(Response.ResponseKey.MESSAGE.getValue(), env.getProperty(Constants.DATA_SAVE_FAILED));

            // Exception
            return new ResponseEntity<>(jsonResponse, HttpStatus.EXPECTATION_FAILED);
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
        Map<String, Object> jsonResponse = new LinkedHashMap<>();
        Optional<Category> category = categoryService.get(id);

        // Found
        if (category.isPresent()) {
            jsonResponse.put(Response.ResponseKey.STATUS.getValue(), Response.ResponseValue.SUCCESSFULLY.getValue());
            jsonResponse.put(Response.ResponseKey.DATA.getValue(), category.get());

            // Successfully
            return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        }

        // Not found
        jsonResponse.put(Response.ResponseKey.STATUS.getValue(), Response.ResponseValue.NOT_FOUND.getValue());
        jsonResponse.put(Response.ResponseKey.MESSAGE.getValue(),
                messageSource.getMessage(Constants.DATA_ID_NOT_FOUND, new Object[] {id}, Locale.ENGLISH));

        return new ResponseEntity<>(jsonResponse, HttpStatus.NOT_FOUND);
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
        Map<String, Object> jsonResponse = new LinkedHashMap<>();
        try {
            Optional<Category> updateCategory = categoryService.get(id);

            // Found
            if (updateCategory.isPresent()) {
                // Update new category name
                updateCategory.get().setCategoryName(category.getCategoryName());

                // Save category into database
                categoryService.save(updateCategory.get());

                // Response
                jsonResponse.put(Response.ResponseKey.STATUS.getValue(), Response.ResponseValue.SUCCESSFULLY.getValue());
                jsonResponse.put(Response.ResponseKey.DATA.getValue(), categoryService.get(id));

                // Successfully
                return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
            }

            // Not found
            jsonResponse.put(Response.ResponseKey.STATUS.getValue(), Response.ResponseValue.NOT_FOUND);
            jsonResponse.put(Response.ResponseKey.MESSAGE.getValue(),
                    messageSource.getMessage(Constants.DATA_ID_NOT_FOUND, new Object[] {id}, Locale.ENGLISH));

            return new ResponseEntity<>(jsonResponse, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logger.info(ex.getMessage());

            // Failed
            jsonResponse.put(Response.ResponseKey.STATUS.getValue(), Response.ResponseValue.FAILURE);
            jsonResponse.put(Response.ResponseKey.MESSAGE.getValue(), env.getProperty(Constants.DATA_SAVE_FAILED));

            // Exception
            return new ResponseEntity<>(jsonResponse, HttpStatus.EXPECTATION_FAILED);
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
        Map<String, Object> jsonResponse = new LinkedHashMap<>();
        try {
            Optional<Category> category = categoryService.get(id);

            // Found
            if (category.isPresent()) {
                categoryService.delete(category.get());

                // Response
                jsonResponse.put(Response.ResponseKey.STATUS.getValue(), Response.ResponseValue.SUCCESSFULLY.getValue());
                jsonResponse.put(Response.ResponseKey.MESSAGE.getValue(),
                        env.getProperty(Constants.DATA_DELETE_SUCCESSFULLY));

                // Successfully
                return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
            }

            // Not found
            jsonResponse.put(Response.ResponseKey.STATUS.getValue(), Response.ResponseValue.NOT_FOUND);
            jsonResponse.put(Response.ResponseKey.MESSAGE.getValue(),
                    messageSource.getMessage(Constants.DATA_ID_NOT_FOUND, new Object[] {id}, Locale.ENGLISH));

            return new ResponseEntity<>(jsonResponse, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logger.info(ex.getMessage());

            // Failed
            jsonResponse.put(Response.ResponseKey.STATUS.getValue(), Response.ResponseValue.FAILURE);
            jsonResponse.put(Response.ResponseKey.MESSAGE.getValue(), env.getProperty(Constants.DATA_DELETE_FAILED));

            // Exception
            return new ResponseEntity<>(jsonResponse, HttpStatus.EXPECTATION_FAILED);
        }
    }
}
