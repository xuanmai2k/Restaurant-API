package com.r2s.mobilestore;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.r2s.mobilestore.category.controllers.CategoryController;
import com.r2s.mobilestore.category.entities.Category;
import com.r2s.mobilestore.category.services.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * I will define CategoryControllerTests to unit test the Rest API endpoints
 * which has the following methods: POST, GET, PUT and DELETE
 *
 * @author kyle
 * @since 2023-09-09
 */
@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false) //Ignore spring security
public class CategoryControllerTests {

    @MockBean
    private CategoryService categoryService;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${category}")
    private String endpoint;

    @Value("${category}/{id}")
    private String getEndpoint;

    @Test
    void shouldCreateCategory() throws Exception {
        Category category = new Category(1, "Laptop");

        mockMvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void shouldReturnCategory() throws Exception {
        long id = 1L;
        Category category = new Category(id, "Laptop");

        when(categoryService.get(id)).thenReturn(Optional.of(category));
        mockMvc.perform(get(getEndpoint, id)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.categoryName").value(category.getCategoryName()))
                .andDo(print());
    }

    @Test
    void shouldReturnNotFoundCategory() throws Exception {
        long id = 1L;

        when(categoryService.get(id)).thenReturn(Optional.empty());
        mockMvc.perform(get(getEndpoint, id))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldReturnListOfCategories() throws Exception {
        List<Category> categories = new ArrayList<>(
                Arrays.asList(new Category( 1, "Laptop"),
                        new Category(2, "PC"),
                        new Category(3, "Printer")));

        when(categoryService.listAll()).thenReturn(categories);
        mockMvc.perform(get(endpoint))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(categories.size()))
                .andDo(print());
    }

    @Test
    void shouldReturnListOfCategoriesWithFilter() throws Exception {
        List<Category> categories = new ArrayList<>(
                Arrays.asList(new Category(1, "PC for Relax"),
                        new Category(3, "PC for Official")));

        String categoryName = "PC";
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("categoryName", categoryName);

        when(categoryService.filterByName(categoryName)).thenReturn(categories);
        mockMvc.perform(get(endpoint).params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(categories.size()))
                .andDo(print());

        categories = Collections.emptyList();

        when(categoryService.filterByName(categoryName)).thenReturn(categories);
        mockMvc.perform(get(endpoint).params(paramsMap))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void shouldReturnNoContentWhenFilter() throws Exception {
        String categoryName = "R2S";
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("categoryName", categoryName);

        List<Category> categories = Collections.emptyList();

        when(categoryService.filterByName(categoryName)).thenReturn(categories);
        mockMvc.perform(get(endpoint).params(paramsMap))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void shouldUpdateCategory() throws Exception {
        long id = 1L;

        Category category = new Category(id, "Laptop");
        Category updatedCategory = new Category(id, "Updated");

        when(categoryService.get(id)).thenReturn(Optional.of(category));
        when(categoryService.save(any(Category.class))).thenReturn(updatedCategory);

        mockMvc.perform(put(getEndpoint, id).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCategory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName").value(updatedCategory.getCategoryName()))
                .andDo(print());
    }

    @Test
    void shouldDeleteCategory() throws Exception {
        long id = 1L;

        doNothing().when(categoryService).delete(id);
        mockMvc.perform(delete(getEndpoint, id))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
