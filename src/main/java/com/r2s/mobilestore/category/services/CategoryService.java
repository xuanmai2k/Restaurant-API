package com.r2s.mobilestore.category.services;

import com.r2s.mobilestore.category.entities.Category;
import java.util.List;
import java.util.Optional;

/**
 * Represents a category service
 *
 * @author kyle
 * @since 2023-09-02
 */
public interface CategoryService {

    /**
     * This method is used to list all categories
     *
     * @return list of categories
     */
    public List<Category> listAll();

    /**
     * This method is used to get a category base on id
     *
     * @param id This is category id
     * @return category base on id
     */
    public Optional<Category> get(long id);

    /**
     * Find category by category name
     *
     * @param name This is category name
     * @return List of categories
     */
    public List<Category> filterByName(String name);

    /**
     * This method is used to create a category
     *
     * @param category This is a category
     */
    public Category save(Category category);


    /**
     * This method is used to delete a category by id
     *
     * @param category This is category
     */
    public void delete(long id);
}