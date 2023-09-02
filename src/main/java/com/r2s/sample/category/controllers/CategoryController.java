package com.r2s.sample.category.controllers;


import com.r2s.sample.category.entities.Category;
import com.r2s.sample.category.services.CategoryService;
import com.r2s.sample.dtos.ResponseDto;
import com.r2s.sample.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Represents a category controller
 *
 * @author kyle
 * @since 2023-08-31
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Get message from messages.properties.properties file
     */
    @Autowired
    private MessageSource messageSource;

//    private ModelMapper mapper;

    /**
     * REST API methods for Retrieval operations
     *
     * @return list all of categories
     */
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.listAll();
    }

    /**
     * Build create employee REST API
     *
     * @param category This is a category
     * @return a category is inserted into database
     */
    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryService.save(category);
    }

    /**
     * Build get category by id REST API
     *
     * @param id This is category id
     * @return a category
     */
    @GetMapping("{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable long id) {
        Optional<Category> category = categoryService.get(id);

        // Found
        if (category.isPresent()) {
            return ResponseEntity.ok(category);
        }

        // Not found
        return ResponseEntity.ok(new ResponseDto<HttpStatus>(HttpStatus.NOT_FOUND,
                messageSource.getMessage(Constants.DATA_ID_NOT_FOUND, new Object[] {id}, Locale.ENGLISH)));
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
        Optional<Category> updateCategory = categoryService.get(id);

        // Found
        if (updateCategory.isPresent()) {
            // Update new category name
            updateCategory.get().setCategoryName(category.getCategoryName());
            // Save category into database
            categoryService.save(updateCategory.get());

            return ResponseEntity.ok(updateCategory);
        }

        // Not found
        return ResponseEntity.ok(new ResponseDto<HttpStatus>(HttpStatus.NOT_FOUND,
                messageSource.getMessage(Constants.DATA_ID_NOT_FOUND, new Object[] {id}, Locale.ENGLISH)));
    }

    /**
     * Build delete category REST API
     *
     * @param id This is category
     * @return http status
     */
    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteCategory(@PathVariable long id) {
        Optional<Category> category = categoryService.get(id);

        // Found
        if (category.isPresent()) {
            categoryService.delete(category.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }

        // Not found
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
