package com.r2s.mobilestore;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.r2s.mobilestore.promotion.dtos.PageDTO;
import com.r2s.mobilestore.promotion.dtos.SearchPromotionDTO;
import com.r2s.mobilestore.promotion.entities.Promotion;
import com.r2s.mobilestore.promotion.service.PromotionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDate;
import java.util.*;


import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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
 * @since 2023-10-04
 */

@SpringBootTest
@AutoConfigureMockMvc
public class PromotionControllerTests {
    @MockBean
    private PromotionService promotionService;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${promotion}")
    private String endpoint;

    @Value("${promotion}/{id}")
    private String getEndpoint;

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void shouldCreatePromotion() throws Exception {
        LocalDate expireDate = LocalDate.parse("2023-12-12");

        Promotion promotion = new Promotion(1, "999abc", 1000, 35.0, 100,
                expireDate, "junit test", true);

        mockMvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(promotion)))
                .andExpect(status().isCreated())
                .andDo(print());
    }


    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void shouldCreatePromotionForbidden() throws Exception {
        LocalDate expireDate = LocalDate.parse("2023-12-12");

        Promotion promotion = new Promotion(1, "999abc", 1000, 35.0, 100,
                expireDate, "junit test", true);

        mockMvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(promotion)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }


    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void shouldReturnPromotionById() throws Exception {
        Long id = 1L;
        LocalDate expireDate = LocalDate.parse("2023-12-12");
        Promotion promotion = new Promotion(1, "999abc", 1000, 35.0, 100,
                expireDate, "junit test", true);

        when(promotionService.getPromotionById(id)).thenReturn(Optional.of(promotion));

        mockMvc.perform(get(getEndpoint, id)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.discountCode").value(promotion.getDiscountCode()))
                .andDo(print());
    }


    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void shouldReturnNotFoundPromotion() throws Exception {
        long id = 1L;

        when(promotionService.getPromotionById(id)).thenReturn(Optional.empty());
        mockMvc.perform(get(getEndpoint, id))
                .andExpect(status().isNotFound())
                .andDo(print());
    }


    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void shouldReturnPageOfPromotions() throws Exception {

        LocalDate expireDate = LocalDate.parse("2023-12-12");

        List<Promotion> promotionList = Arrays.asList(
                new Promotion(1, "999abc", 1000, 35.0, 100,
                        expireDate, "junit test", true),
                new Promotion(2, "888abc", 2000, 40.0, 200,
                        expireDate, "junit test", true)
        );

        Page<Promotion> promotions = new PageImpl<>(promotionList);

        PageDTO pageDTO = new PageDTO(0, 2);

        when(promotionService.listAll(pageDTO)).thenReturn(promotions);
        mockMvc.perform(get(endpoint).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pageDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andDo(print());
    }


    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void shouldReturnPageOfPromotionUsingFullFilter() throws Exception {

        LocalDate expireDate = LocalDate.parse("2023-12-12");

        List<Promotion> promotionList = Arrays.asList(
                new Promotion(1, "999abc", 1000, 35.0, 100,
                        expireDate, "junit test", true),
                new Promotion(2, "888abc", 2000, 40.0, 200,
                        expireDate, "junit test", true)
        );

        Page<Promotion> promotions = new PageImpl<>(promotionList);

        PageDTO pageDTO = new PageDTO(0, 2);

        SearchPromotionDTO searchPromotionDTO = new SearchPromotionDTO("abc", expireDate,
                true, 0, 100, pageDTO);

        when(promotionService.search(searchPromotionDTO)).thenReturn(promotions);
        mockMvc.perform(get(endpoint + "/search").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchPromotionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andDo(print());
    }


    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void shouldReturnPageOfPromotionUsingFilterWithoutExpireDate() throws Exception {

        LocalDate expireDate = LocalDate.parse("2023-12-12");

        List<Promotion> promotionList = Arrays.asList(
                new Promotion(1, "999abc", 1000, 35.0, 100,
                        expireDate, "junit test", true),
                new Promotion(2, "888abc", 2000, 40.0, 200,
                        expireDate, "junit test", true)
        );

        Page<Promotion> promotions = new PageImpl<>(promotionList);

        PageDTO pageDTO = new PageDTO(0, 2);

        SearchPromotionDTO searchPromotionDTO = new SearchPromotionDTO("abc", null,
                true, 0, 100, pageDTO);

        when(promotionService.search(searchPromotionDTO)).thenReturn(promotions);
        mockMvc.perform(get(endpoint + "/search").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchPromotionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andDo(print());
    }


    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void shouldReturnPageOfPromotionUsingFilterWithoutDiscountAvailable() throws Exception {

        LocalDate expireDate = LocalDate.parse("2023-12-12");

        List<Promotion> promotionList = Arrays.asList(
                new Promotion(1, "999abc", 1000, 35.0, 100,
                        expireDate, "junit test", true),
                new Promotion(2, "888abc", 2000, 40.0, 200,
                        expireDate, "junit test", true)
        );

        Page<Promotion> promotions = new PageImpl<>(promotionList);

        PageDTO pageDTO = new PageDTO(0, 2);

        SearchPromotionDTO searchPromotionDTO = new SearchPromotionDTO("abc", expireDate,
                null, 0, 100, pageDTO);

        when(promotionService.search(searchPromotionDTO)).thenReturn(promotions);
        mockMvc.perform(get(endpoint + "/search").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchPromotionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andDo(print());
    }


    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void shouldReturnPageOfPromotionUsingFilterWithoutDiscountAvailableAndExpireDate() throws Exception {

        LocalDate expireDate = LocalDate.parse("2023-12-12");

        List<Promotion> promotionList = Arrays.asList(
                new Promotion(1, "999abc", 1000, 35.0, 100,
                        expireDate, "junit test", true),
                new Promotion(2, "888abc", 2000, 40.0, 200,
                        expireDate, "junit test", false)
        );

        Page<Promotion> promotions = new PageImpl<>(promotionList);

        PageDTO pageDTO = new PageDTO(0, 2);

        SearchPromotionDTO searchPromotionDTO = new SearchPromotionDTO("abc", null,
                null, 0, 100, pageDTO);

        when(promotionService.search(searchPromotionDTO)).thenReturn(promotions);
        mockMvc.perform(get(endpoint + "/search").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchPromotionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andDo(print());
    }


    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void shouldReturnNoContentPromotionUsingFilterNoContent() throws Exception {

        List<Promotion> promotionList = Arrays.asList();

        Page<Promotion> promotions = new PageImpl<>(promotionList);

        PageDTO pageDTO = new PageDTO(0, 2);

        SearchPromotionDTO searchPromotionDTO = new SearchPromotionDTO("abc", null,
                null, 0, 100, pageDTO);

        when(promotionService.search(searchPromotionDTO)).thenReturn(promotions);
        mockMvc.perform(get(endpoint + "/search").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchPromotionDTO)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }


    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void shouldUpdatePromotion() throws Exception {
        Long id = 1L;

        LocalDate expireDate = LocalDate.parse("2023-12-12");

        Promotion promotion = new Promotion(1, "999abc", 1000, 35.0, 100,
                expireDate, "junit test", true);
        Promotion updatePromotion = new Promotion(2, "888abc", 2000, 40.0, 200,
                expireDate, "junit test", false);

        when(promotionService.getPromotionById(id)).thenReturn(Optional.of(promotion));
        when(promotionService.save(any(Promotion.class))).thenReturn(updatePromotion);

        mockMvc.perform(put(getEndpoint, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePromotion)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.discountCode").value(updatePromotion.getDiscountCode()))
                .andExpect(jsonPath("$.totalPurchase").value(updatePromotion.getTotalPurchase()))
                .andExpect(jsonPath("$.discount").value(updatePromotion.getDiscount()))
                .andExpect(jsonPath("$.maxPromotionGet").value(updatePromotion.getMaxPromotionGet()))
                .andDo(print());
    }


    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void shouldUpdatePromotionForbidden() throws Exception {
        Long id = 1L;

        LocalDate expireDate = LocalDate.parse("2023-12-12");

        Promotion promotion = new Promotion(1, "999abc", 1000, 35.0, 100,
                expireDate, "junit test", true);
        Promotion updatePromotion = new Promotion(2, "888abc", 2000, 40.0, 200,
                expireDate, "junit test", false);

        when(promotionService.getPromotionById(id)).thenReturn(Optional.of(promotion));
        when(promotionService.save(any(Promotion.class))).thenReturn(updatePromotion);

        mockMvc.perform(put(getEndpoint, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePromotion)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }


    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void shouldDeletePromotion() throws Exception {
        Long id = 1L;
        LocalDate expireDate = LocalDate.parse("2023-12-12");

        Promotion promotion = new Promotion(1, "999abc", 1000, 35.0, 100,
                expireDate, "junit test", true);

        when(promotionService.getPromotionById(id)).thenReturn(Optional.of(promotion));

        doNothing().when(promotionService).delete(id);
        mockMvc.perform(delete(getEndpoint, id))
                .andExpect(status().isNoContent())
                .andDo(print());
    }


    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void shouldDeletePromotionForbidden() throws Exception {
        Long id = 1L;
        LocalDate expireDate = LocalDate.parse("2023-12-12");

        Promotion promotion = new Promotion(1, "999abc", 1000, 35.0, 100,
                expireDate, "junit test", true);

        when(promotionService.getPromotionById(id)).thenReturn(Optional.of(promotion));

        doNothing().when(promotionService).delete(id);
        mockMvc.perform(delete(getEndpoint, id))
                .andExpect(status().isForbidden())
                .andDo(print());
    }


    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void shouldDeleteNotFound() throws Exception {
        Long id = 2L;

        doNothing().when(promotionService).delete(id);
        mockMvc.perform(delete(getEndpoint, id))
                .andExpect(status().isNotFound())
                .andDo(print());
    }


}
