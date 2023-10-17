//package com.r2s.mobilestore;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.r2s.mobilestore.promotion.controllers.PromotionController;
//import com.r2s.mobilestore.promotion.dtos.SearchPromotionDTO;
//import com.r2s.mobilestore.promotion.entities.Promotion;
//import com.r2s.mobilestore.promotion.service.PromotionService;
//import org.apache.catalina.Authenticator;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//
//import java.util.*;
//
//
//import static org.hamcrest.Matchers.hasSize;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
///**
// * I will define PromotionControllerTests to unit test the Rest API endpoints
// * which has the following methods: POST, GET, PUT and DELETE
// *
// * @author xuanmai
// * @since 2023-10-04
// */
//
//
//@WebMvcTest(PromotionController.class)
//@AutoConfigureMockMvc(addFilters = false) //Ignore spring security
//public class PromotionControllerTests {
//    @MockBean
//    private PromotionService promotionService;
//
//
//    @Autowired
//    protected MockMvc mockMvc;
//
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//
//    @Value("${promotion}")
//    private String endpoint;
//
//
//    @Value("${promotion}/{id}")
//    private String getEndpoint;
//    private Authenticator authenticationManager;
//
//
//    @Test
//    void shouldCreatePromotion() throws Exception {
//        Promotion promotion = new Promotion(1, "999abc", 1000, 35.0, 100,
//                new Date(2023, 9, 12), "junit test", true);
//
//
//        mockMvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(promotion)))
//                .andExpect(status().isCreated())
//                .andDo(print());
//    }
//
//
//    @Test
//    void shouldReturnPromotion() throws Exception {
//        Long id = 1L;
//        Promotion promotion = new Promotion(1, "999abc", 1000, 35.0, 100,
//                new Date(2023, 9, 12), "junit test", true);
//        when(promotionService.getPromotionById(id)).thenReturn(Optional.of(promotion));
//        mockMvc.perform(get(getEndpoint, id)).andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(id))
//                .andExpect(jsonPath("$.discountCode").value(promotion.getDiscountCode()))
//                .andDo(print());
//    }
//
//
//    @Test
//    void shouldReturnNotFoundPromotion() throws Exception {
//        long id = 1L;
//
//
//        when(promotionService.getPromotionById(id)).thenReturn(Optional.empty());
//        mockMvc.perform(get(getEndpoint, id))
//                .andExpect(status().isNotFound())
//                .andDo(print());
//    }
//
//
////    @Test
////    void shouldReturnPageOfPromotions() throws Exception {
////
////        List<Promotion> promotionList = Arrays.asList(
////                new Promotion(1, "999abc", 1000, 35.0, 100,
////                        new Date(2023, 3, 10), "junit test", true),
////                new Promotion(2, "888abc", 2000, 40.0, 200,
////                        new Date(2023, 9, 12), "junit test", false)
////        );
////
////        SearchPromotionDTO searchPromotionDTO = new SearchPromotionDTO(null);
////        Page<Promotion> promotions = new PageImpl<>(promotionList);
////
////        when(promotionService.listAll(0, 2)).thenReturn(promotions);
////        mockMvc.perform(get(endpoint)
////                        .param("pageNumber", "0")
////                        .param("pageSize", "2"))
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$.content", hasSize(2)))
////                .andDo(print());
////    }
//
//
////    @Test
////    void shouldReturnPageOfPromotionWithFilter() throws Exception {
////
////        List<Promotion> promotionList = Arrays.asList(
////                new Promotion(1, "999abc", 1000, 35.0, 100,
////                        new Date(2023, 3, 10), "junit test", true),
////                new Promotion(2, "888abc", 2000, 40.0, 200,
////                        new Date(2023, 9, 12), "junit test", false)
////        );
////
////        Page<Promotion> promotions = new PageImpl<>(promotionList);
////
////        SearchPromotionDTO searchPromotionDTO = new SearchPromotionDTO("abc");
////
////        when(promotionService.filterByDiscountCode(searchPromotionDTO, 0, 2)).thenReturn(promotions);
////        mockMvc.perform(get(endpoint)
////                        .param("pageNumber", "0")
////                        .param("pageSize", "2")
////                        .content("{\"discountCode\":\"abc\"}")
////                        .contentType(MediaType.APPLICATION_JSON))
////                .andExpect(status().isOk())
////                .andDo(print());
////
////        promotionList = Collections.emptyList();
////        Page<Promotion> promotionEmpty = new PageImpl<>(promotionList);
////
////        when(promotionService.filterByDiscountCode(searchPromotionDTO, 0, 2)).thenReturn(promotionEmpty);
////        mockMvc.perform(get(endpoint)
////                        .param("pageNumber", "0")
////                        .param("pageSize", "2")
////                        .content("{\"discountCode\":\"abc\"}")
////                        .contentType(MediaType.APPLICATION_JSON))
////                .andExpect(status().isNoContent())
////                .andDo(print());
////    }
//
//
//    @Test
//    void shouldUpdatePromotion() throws Exception {
//        Long id = 1L;
//
//
//        Promotion promotion = new Promotion(1, "999abc", 1000, 35.0, 100,
//                new Date(2023, 3, 10), "junit test", true);
//        Promotion updatePromotion = new Promotion(2, "888abc", 2000, 40.0, 200,
//                new Date(2023, 9, 12), "junit test", false);
//
//        when(promotionService.getPromotionById(id)).thenReturn(Optional.of(promotion));
//        when(promotionService.save(any(Promotion.class))).thenReturn(updatePromotion);
//
//        mockMvc.perform(put(getEndpoint, id).contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updatePromotion)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.discountCode").value(updatePromotion.getDiscountCode()))
//                .andExpect(jsonPath("$.totalPurchase").value(updatePromotion.getTotalPurchase()))
//                .andExpect(jsonPath("$.discount").value(updatePromotion.getDiscount()))
//                .andExpect(jsonPath("$.maxPromotionGet").value(updatePromotion.getMaxPromotionGet()))
//                .andDo(print());
//    }
//
//
//    @Test
//    void shouldDeletePromotion() throws Exception {
//        Long id = 1L;
//        Promotion promotion = new Promotion(1, "999abc", 1000, 35.0, 100,
//                new Date(2023, 3, 10), "junit test", true);
//
//        when(promotionService.getPromotionById(id)).thenReturn(Optional.of(promotion));
//
//        doNothing().when(promotionService).delete(id);
//        mockMvc.perform(delete(getEndpoint, id))
//                .andExpect(status().isNoContent())
//                .andDo(print());
//
//        id = 2L;
//
//        doNothing().when(promotionService).delete(id);
//        mockMvc.perform(delete(getEndpoint, id))
//                .andExpect(status().isNotFound())
//                .andDo(print());
//    }
//
//}
