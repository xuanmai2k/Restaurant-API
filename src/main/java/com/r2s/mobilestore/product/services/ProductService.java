package com.r2s.mobilestore.product.services;

import com.r2s.mobilestore.product.dtos.CreateProductDTO;
import com.r2s.mobilestore.product.entities.Product;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.Optional;

public interface ProductService {

    /**
     * This method is used to list all products
     *
     * @param pageNumber This is number of page
     * @param pageSize   This is size of page
     * @return list of products
     */
    public Page<Product> getAllProducts(int pageNumber, int pageSize);

    /**
     * This method is used to get a product base on id
     *
     * @param id This is product id
     * @return product base on id
     */
    public Optional<Product> getProductById(Long id);

    /**
     * This method is used to create a product
     *
     * @param createProductDTO This is product
     * @return new product
     */
    public Product createProduct(CreateProductDTO createProductDTO) throws IOException;


    /**
     * This method is used to update a product
     *
     * @param createProductDTO This is product
     * @param id               This is product id
     * @return updated product
     */
    public Product updateProduct(CreateProductDTO createProductDTO, Long id) throws IOException;

    /**
     * This method is used to delete a product by id
     *
     * @param product This is product
     */
    public void deleteProduct(Product product) throws IOException;

    /**
     * This method is used to random product code
     *
     * @param length This is length of product code
     */
    public String randomProductCode(int length);

    /**
     * This method is used to search product
     *
     * @param name         This is name of product
     * @param manufacturer This is manufacturer of product
     * @param idCategory   This is category id of product
     * @param pageNumber   This is number of page
     * @param pageSize     This is size of page
     * @return list of products
     */
    public Page<Product> search(String name, String manufacturer, String idCategory, int pageNumber, int pageSize);

}
