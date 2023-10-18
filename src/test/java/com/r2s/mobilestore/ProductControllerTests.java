package com.r2s.mobilestore;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.r2s.mobilestore.category.entities.Category;
import com.r2s.mobilestore.category.services.CategoryService;
import com.r2s.mobilestore.product.controllers.ProductController;
import com.r2s.mobilestore.product.dtos.CreateProductDTO;
import com.r2s.mobilestore.product.dtos.SearchProductDTO;
import com.r2s.mobilestore.product.entities.Manufacturer;
import com.r2s.mobilestore.product.entities.Product;
import com.r2s.mobilestore.product.repositories.ManufacturerRepository;
import com.r2s.mobilestore.product.services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * I will define PromotionControllerTests to unit test the Rest API endpoints
 * which has the following methods: POST, GET, PUT and DELETE
 *
 * @author xuanmai
 * @since 2023-10-10
 */

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false) //Ignore spring security
public class ProductControllerTests {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${product}")
    private String endpoint;

    @Value("${product}/{id}")
    private String getEndpoint;

    @MockBean
    private ProductService productService;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private ManufacturerRepository manufacturerRepository;

    @Test
    public void shouldCreateProduct() throws Exception {
        List<String> image = new ArrayList<>();
        image.add("123.jpg");
        image.add("456.jpg");

        Category category = new Category(1L, "phone");
        when(categoryService.save(category)).thenReturn(category);

        List<Manufacturer> manufacturer = new ArrayList<>();
        Manufacturer manufacturer1 = new Manufacturer(1L,"apple");
        manufacturer.add(0, manufacturer1);

        when(manufacturerRepository.findByManufacturerNameContaining("apple")).thenReturn(manufacturer);

        Product product = new Product(1L,"ABCD1234", "iphone", 1000.0, 100, "great", "64GB",
                "silver", "new", manufacturer1, category, image);

        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void getProductById() throws Exception {
        long id = 1L;
        List<String> image = new ArrayList<>();
        image.add("123.jpg");
        image.add("456.jpg");

        Category category = new Category(1L, "phone");
        when(categoryService.save(category)).thenReturn(category);

        List<Manufacturer> manufacturer = new ArrayList<>();
        Manufacturer manufacturer1 = new Manufacturer(1L,"apple");
        manufacturer.add(0, manufacturer1);

        when(manufacturerRepository.findByManufacturerNameContaining("apple")).thenReturn(manufacturer);

        Product product = new Product(1L,"ABCD1234", "iphone", 1000.0, 100, "great", "64GB",
                "silver", "new", manufacturer1, category, image);

        when(productService.getProductById(id)).thenReturn(Optional.of(product));
        mockMvc.perform(get(getEndpoint, id))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void returnNotFoundProductById() throws Exception {
        long id = 1L;

        when(productService.getProductById(id)).thenReturn(Optional.empty());
        mockMvc.perform(get(getEndpoint, id))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        long id = 1L;
        List<String> image = new ArrayList<>();
        image.add("123.jpg");
        image.add("456.jpg");

        Category category = new Category(1L, "phone");
        when(categoryService.save(category)).thenReturn(category);

        List<Manufacturer> manufacturer = new ArrayList<>();
        Manufacturer manufacturer1 = new Manufacturer(1L,"apple");
        manufacturer.add(0, manufacturer1);

        when(manufacturerRepository.findByManufacturerNameContaining("apple")).thenReturn(manufacturer);

        Product product = new Product(1L,"ABCD1234", "iphone", 1000.0, 100, "great", "64GB",
                "silver", "new", manufacturer1, category, image);

        when(productService.getProductById(id)).thenReturn(Optional.of(product));
        mockMvc.perform(delete(getEndpoint, id))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void returnListOfProducts() throws Exception {
        List<String> image = new ArrayList<>();
        image.add("123.jpg");
        image.add("456.jpg");

        Category category = new Category(1L, "phone");
        when(categoryService.save(category)).thenReturn(category);

        List<Manufacturer> manufacturer = new ArrayList<>();
        Manufacturer manufacturer1 = new Manufacturer(1L,"apple");
        manufacturer.add(0, manufacturer1);

        when(manufacturerRepository.findByManufacturerNameContaining("apple")).thenReturn(manufacturer);

        List<Product> productList = new ArrayList<>(Arrays.asList(
                new Product(1L,"ABCD1234", "iphone1", 1000.0, 100, "great", "64GB",
                "silver", "new", manufacturer1, category, image),
                new Product(2L,"ABCD1234", "iphone2", 1000.0, 100, "great", "64GB",
                "silver", "new", manufacturer1, category, image)));

        Integer pageNumber = 0;
        Integer pageSize = 2;

        Page<Product> productPage = new PageImpl<>(productList);

        when(productService.getAllProducts(pageNumber, pageSize)).thenReturn(productPage);
        mockMvc.perform(get(endpoint)
                        .param("pageNumber", "0")
                        .param("pageSize", "2"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void updateProduct() throws Exception {
        long id = 1L;
        List<String> image = new ArrayList<>();
        image.add("123.jpg");
        image.add("456.jpg");

        Category category = new Category(1L, "phone");
        when(categoryService.save(category)).thenReturn(category);

        List<Manufacturer> manufacturer = new ArrayList<>();
        Manufacturer manufacturer1 = new Manufacturer(1L,"apple");
        manufacturer.add(0, manufacturer1);

        when(manufacturerRepository.findByManufacturerNameContaining("apple")).thenReturn(manufacturer);

        Product product = new Product(1L,"ABCD1234", "iphone", 1000.0, 100, "great", "64GB",
                "silver", "new", manufacturer1, category, image);

        Product updateProduct = new Product(1L,"ABCD1234", "iphone11", 1000.0, 100, "great", "64GB",
                "silver", "new", manufacturer1, category, image);

        when(productService.getProductById(eq(id))).thenReturn(Optional.of(product));
        when(productService.updateProduct(any(CreateProductDTO.class), eq(id))).thenReturn(updateProduct);

        mockMvc.perform(put(getEndpoint, id, updateProduct).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updateProduct.getName()))
                .andDo(print());
        assertEquals("iphone11", updateProduct.getName());
    }

    @Test
    void returnListOfProductsUsingSearch() throws Exception {
        List<String> image = new ArrayList<>();
        image.add("123.jpg");
        image.add("456.jpg");

        Category category = new Category(1L, "phone");
        when(categoryService.save(category)).thenReturn(category);

        List<Manufacturer> manufacturer = new ArrayList<>();
        Manufacturer manufacturer1 = new Manufacturer(1L,"apple");
        manufacturer.add(0, manufacturer1);

        when(manufacturerRepository.findByManufacturerNameContaining("apple")).thenReturn(manufacturer);

        List<Product> productList = new ArrayList<>(Arrays.asList(
                new Product(1L,"ABCD1234", "iphone1", 1000.0, 100, "great", "64GB",
                        "silver", "new", manufacturer1, category, image),
                new Product(2L,"ABCD1234", "iphone2", 1000.0, 100, "great", "64GB",
                        "silver", "new", manufacturer1, category, image)));

        Integer pageNumber = 0;
        Integer pageSize = 2;

        Page<Product> productPage = new PageImpl<>(productList);

        SearchProductDTO searchProductDTO = new SearchProductDTO("iphone","apple","phone");

        when(productService.search(searchProductDTO,pageNumber, pageSize)).thenReturn(productPage);
        mockMvc.perform(get(endpoint + "/search")
                        .param("pageNumber", "0")
                        .param("pageSize", "2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchProductDTO)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void returnListOfProductsUsingSearchWithoutManufacturer() throws Exception {
        List<String> image = new ArrayList<>();
        image.add("123.jpg");
        image.add("456.jpg");

        Category category = new Category(1L, "phone");
        when(categoryService.save(category)).thenReturn(category);

        List<Manufacturer> manufacturer = new ArrayList<>();
        Manufacturer manufacturer1 = new Manufacturer(1L,"apple");
        manufacturer.add(0, manufacturer1);

        when(manufacturerRepository.findByManufacturerNameContaining("apple")).thenReturn(manufacturer);

        List<Product> productList = new ArrayList<>(Arrays.asList(
                new Product(1L,"ABCD1234", "iphone1", 1000.0, 100, "great", "64GB",
                        "silver", "new", manufacturer1, category, image),
                new Product(2L,"ABCD1234", "iphone2", 1000.0, 100, "great", "64GB",
                        "silver", "new", manufacturer1, category, image)));

        Integer pageNumber = 0;
        Integer pageSize = 2;

        Page<Product> productPage = new PageImpl<>(productList);

        SearchProductDTO searchProductDTO = new SearchProductDTO("iphone","","phone");

        when(productService.search(searchProductDTO,pageNumber, pageSize)).thenReturn(productPage);
        mockMvc.perform(get(endpoint + "/search")
                        .param("pageNumber", "0")
                        .param("pageSize", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchProductDTO)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void returnListOfProductsUsingSearchWithoutCategory() throws Exception {
        List<String> image = new ArrayList<>();
        image.add("123.jpg");
        image.add("456.jpg");

        Category category = new Category(1L, "phone");
        when(categoryService.save(category)).thenReturn(category);

        List<Manufacturer> manufacturer = new ArrayList<>();
        Manufacturer manufacturer1 = new Manufacturer(1L,"apple");
        manufacturer.add(0, manufacturer1);

        when(manufacturerRepository.findByManufacturerNameContaining("apple")).thenReturn(manufacturer);

        List<Product> productList = new ArrayList<>(Arrays.asList(
                new Product(1L,"ABCD1234", "iphone1", 1000.0, 100, "great", "64GB",
                        "silver", "new", manufacturer1, category, image),
                new Product(2L,"ABCD1234", "iphone2", 1000.0, 100, "great", "64GB",
                        "silver", "new", manufacturer1, category, image)));

        Integer pageNumber = 0;
        Integer pageSize = 2;

        Page<Product> productPage = new PageImpl<>(productList);

        SearchProductDTO searchProductDTO = new SearchProductDTO("iphone","apple","");

        when(productService.search(searchProductDTO,pageNumber, pageSize)).thenReturn(productPage);
        mockMvc.perform(get(endpoint + "/search")
                        .param("pageNumber", "0")
                        .param("pageSize", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchProductDTO)))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
