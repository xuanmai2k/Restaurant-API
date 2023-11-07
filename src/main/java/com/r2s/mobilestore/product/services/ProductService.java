//package com.r2s.mobilestore.product.services;
//
//import com.r2s.mobilestore.dtos.PageDTO;
//import com.r2s.mobilestore.product.dtos.CreateProductDTO;
//import com.r2s.mobilestore.product.dtos.SearchProductDTO;
//import com.r2s.mobilestore.product.entities.Product;
//import org.springframework.data.domain.Page;
//
//import java.io.IOException;
//import java.util.Optional;
//
//public interface ProductService {
//
//    /**
//     * This method is used to list all products
//     *
//     * @param pageDTO This is a page
//     * @return list of products
//     */
//    public Page<Product> getAllProducts(PageDTO pageDTO);
//
//    /**
//     * This method is used to get a product base on id
//     *
//     * @param id This is product id
//     * @return product base on id
//     */
//    public Optional<Product> getProductById(Long id);
//
//    /**
//     * This method is used to create a product
//     *
//     * @param createProductDTO This is product
//     * @return new product
//     */
//    public Product createProduct(CreateProductDTO createProductDTO) throws IOException;
//
//
//    /**
//     * This method is used to update a product
//     *
//     * @param createProductDTO This is product
//     * @param id               This is product id
//     * @return updated product
//     */
//    public Product updateProduct(CreateProductDTO createProductDTO, Long id) throws IOException;
//
//    /**
//     * This method is used to delete a product by id
//     *
//     * @param product This is product
//     */
//    public void deleteProduct(Product product) throws IOException;
//
//    /**
//     * This method is used to random product code
//     *
//     * @param length This is length of product code
//     */
//    public String getRandomProductCode(int length);
//
//    /**
//     * This method is used to search product
//     *
//     * @param searchProductDTO There are keyword, category and manufacturer
//     * @return list of products
//     */
//    public Page<Product> search(SearchProductDTO searchProductDTO);
//
//}
