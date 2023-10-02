package com.r2s.mobilestore.category.services;

import com.r2s.mobilestore.category.entities.Category;
import com.r2s.mobilestore.category.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Represents a category service
 * @author kyle
 * @since 2023-08-31
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * This method is used to list all categories
     *
     * @return list of categories
     */
    @Override
    public List<Category> listAll() {
        return categoryRepository.findAll();
    }

    /**
     * This method is used to get a category base on id
     *
     * @param id This is category id
     * @return category base on id
     */
    public Optional<Category> get(long id) {
        return categoryRepository.findById(id);
    }

    /**
     * Get all categories containing name
     *
     * @param name This is category name
     * @return List of categories
     */
    @Override
    public List<Category> filterByName(String name) {
        return categoryRepository.findByCategoryNameContaining(name);
    }

    /**
     * This method is used to create a category
     *
     * @param category This is a category
     */
    @Override
    public Category save(Category category) {
       return categoryRepository.save(category);
    }


    /**
     * This method is used to delete a category by id
     *
     * @param id This is category id
     */
    public void delete(long id) {
        categoryRepository.deleteById(id);
    }
}