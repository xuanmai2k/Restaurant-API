package com.r2s.mobilestore;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.r2s.mobilestore.dtos.PageDTO;
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
        LocalDate manufactureDate = LocalDate.parse("2026-12-12");
        LocalDate expireDate = LocalDate.parse("2026-12-20");

        Promotion promotion = new Promotion(1,"ABC123","great",15,0,
                manufactureDate,expireDate,10.0,null,100,
                9000,"pending","vip");

        mockMvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(promotion)))
                .andExpect(status().isCreated())
                .andDo(print());
    }


    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void shouldCreatePromotionForbidden() throws Exception {
        LocalDate manufactureDate = LocalDate.parse("2026-12-12");
        LocalDate expireDate = LocalDate.parse("2026-12-20");

        Promotion promotion = new Promotion(1,"ABC123","great",15,0,
                manufactureDate,expireDate,10.0,null,100,
                9000,"pending","vip");

        mockMvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(promotion)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }


    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void shouldReturnPromotionById() throws Exception {
        Long id = 1L;
        LocalDate manufactureDate = LocalDate.parse("2026-12-12");
        LocalDate expireDate = LocalDate.parse("2026-12-20");

        Promotion promotion = new Promotion(1,"ABC123","great",15,0,
                manufactureDate,expireDate,10.0,null,100,
                9000,"pending","vip");

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
    @WithMockUser(authorities = "ROLE_ADMIN")
    void shouldGenerateDiscountCode() throws Exception {

        String randomCode = "ABC";

        when(promotionService.getRandomDiscountCode(8)).thenReturn(randomCode);
        mockMvc.perform(post(endpoint + "/generate-code"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void shouldReturnPageOfPromotions() throws Exception {

        LocalDate manufactureDate = LocalDate.parse("2026-12-12");
        LocalDate expireDate = LocalDate.parse("2026-12-20");

        List<Promotion> promotionList = Arrays.asList(
                new Promotion(1,"ABC123","great",15,0,
                        manufactureDate,expireDate,10.0,null,100,
                        9000,"pending","vip"),
                new Promotion(2,"XYZ456","great",15,0,
                        manufactureDate,expireDate,10.0,null,100,
                        9000,"pending","vip")
        );

        Page<Promotion> promotions = new PageImpl<>(promotionList);

        PageDTO pageDTO = new PageDTO();

        when(promotionService.listFollowByStatus("pending",pageDTO)).thenReturn(promotions);
        mockMvc.perform(get(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(pageDTO))
                                .param("status", "pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andDo(print());
    }


    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void shouldReturnPageOfPromotionUsingFullFilter() throws Exception {

        LocalDate manufactureDate = LocalDate.parse("2026-12-12");
        LocalDate expireDate = LocalDate.parse("2026-12-20");

        List<Promotion> promotionList = Arrays.asList(
                new Promotion(1,"ABC123","great",15,0,
                        manufactureDate,expireDate,10.0,null,100,
                        9000,"pending","vip"),
                new Promotion(2,"ABC456","great",15,0,
                        manufactureDate,expireDate,10.0,null,100,
                        9000,"pending","vip")
        );

        Page<Promotion> promotions = new PageImpl<>(promotionList);

        PageDTO pageDTO = new PageDTO();

        SearchPromotionDTO searchPromotionDTO = new SearchPromotionDTO("ABC","pending","Vip",
                true,manufactureDate,"equal",0,pageDTO);

        when(promotionService.search(searchPromotionDTO)).thenReturn(promotions);
        mockMvc.perform(get(endpoint + "/search").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchPromotionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andDo(print());
    }


    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void shouldReturnPageOfPromotionUsingFilterWithoutManufactureDate() throws Exception {

        LocalDate manufactureDate = LocalDate.parse("2026-12-12");
        LocalDate expireDate = LocalDate.parse("2026-12-20");

        List<Promotion> promotionList = Arrays.asList(
                new Promotion(1,"ABC123","great",15,0,
                        manufactureDate,expireDate,10.0,null,100,
                        9000,"pending","vip"),
                new Promotion(2,"XYZ456","great",15,0,
                        manufactureDate,expireDate,10.0,null,100,
                        9000,"pending","vip")
        );

        Page<Promotion> promotions = new PageImpl<>(promotionList);

        PageDTO pageDTO = new PageDTO();

        SearchPromotionDTO searchPromotionDTO = new SearchPromotionDTO("ABC123","pending","Vip",
                null,null,"equal",0,pageDTO);

        when(promotionService.search(searchPromotionDTO)).thenReturn(promotions);
        mockMvc.perform(get(endpoint + "/search").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchPromotionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andDo(print());
    }


    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void shouldReturnPageOfPromotionUsingFilterWithoutUsed() throws Exception {

        LocalDate manufactureDate = LocalDate.parse("2026-12-12");
        LocalDate expireDate = LocalDate.parse("2026-12-20");

        List<Promotion> promotionList = Arrays.asList(
                new Promotion(1,"ABC123","great",15,0,
                        manufactureDate,expireDate,10.0,null,100,
                        9000,"pending","vip"),
                new Promotion(2,"XYZ456","great",15,0,
                        manufactureDate,expireDate,10.0,null,100,
                        9000,"pending","vip")
        );

        Page<Promotion> promotions = new PageImpl<>(promotionList);

        PageDTO pageDTO = new PageDTO();

        SearchPromotionDTO searchPromotionDTO = new SearchPromotionDTO("ABC123","pending","Vip",
                true,manufactureDate,null,null,pageDTO);

        when(promotionService.search(searchPromotionDTO)).thenReturn(promotions);
        mockMvc.perform(get(endpoint + "/search").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchPromotionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andDo(print());
    }


    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void shouldReturnPageOfPromotionUsingFilterWithoutManufactureDateAndUsed() throws Exception {

        LocalDate manufactureDate = LocalDate.parse("2026-12-12");
        LocalDate expireDate = LocalDate.parse("2026-12-20");

        List<Promotion> promotionList = Arrays.asList(
                new Promotion(1,"ABC123","great",15,0,
                        manufactureDate,expireDate,10.0,null,100,
                        9000,"pending","vip"),
                new Promotion(2,"XYZ456","great",15,0,
                        manufactureDate,expireDate,10.0,null,100,
                        9000,"pending","vip")
        );

        Page<Promotion> promotions = new PageImpl<>(promotionList);

        PageDTO pageDTO = new PageDTO();

        SearchPromotionDTO searchPromotionDTO = new SearchPromotionDTO("ABC123","pending","Vip",
                null,null,null,null,pageDTO);

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

        PageDTO pageDTO = new PageDTO();

        LocalDate manufactureDate = LocalDate.parse("2026-12-12");

        SearchPromotionDTO searchPromotionDTO = new SearchPromotionDTO("ABC","pending","Vip",
                true,manufactureDate,"equal",0,pageDTO);

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

        LocalDate manufactureDate = LocalDate.parse("2026-12-12");
        LocalDate expireDate = LocalDate.parse("2026-12-20");

        Promotion promotion = new Promotion(1,"ABC123","great",15,0,
                manufactureDate,expireDate,10.0,null,100,
                9000,"pending","vip");
        Promotion updatePromotion = new Promotion(1,"XYZ456","great",15,0,
                manufactureDate,expireDate,20.0,null,100,
                9000,"activated","pro");

        when(promotionService.getPromotionById(id)).thenReturn(Optional.of(promotion));
        when(promotionService.save(any(Promotion.class))).thenReturn(updatePromotion);

        mockMvc.perform(put(getEndpoint, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePromotion)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.discountCode").value(updatePromotion.getDiscountCode()))
                .andExpect(jsonPath("$.status").value(updatePromotion.getStatus()))
                .andExpect(jsonPath("$.customerGroup").value(updatePromotion.getCustomerGroup()))
                .andExpect(jsonPath("$.percentageDiscount").value(updatePromotion.getPercentageDiscount()))
                .andDo(print());
    }


    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void shouldUpdatePromotionForbidden() throws Exception {
        Long id = 1L;

        LocalDate manufactureDate = LocalDate.parse("2026-12-12");
        LocalDate expireDate = LocalDate.parse("2026-12-20");

        Promotion promotion = new Promotion(1,"ABC123","great",15,0,
                manufactureDate,expireDate,10.0,null,100,
                9000,"pending","vip");
        Promotion updatePromotion = new Promotion(1,"XYZ456","great",15,0,
                manufactureDate,expireDate,20.0,null,100,
                9000,"activated","pro");

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
        LocalDate manufactureDate = LocalDate.parse("2026-12-12");
        LocalDate expireDate = LocalDate.parse("2026-12-20");

        Promotion promotion = new Promotion(1,"ABC123","great",15,0,
                manufactureDate,expireDate,10.0,null,100,
                9000,"pending","vip");

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
        LocalDate manufactureDate = LocalDate.parse("2026-12-12");
        LocalDate expireDate = LocalDate.parse("2026-12-20");

        Promotion promotion = new Promotion(1,"ABC123","great",15,0,
                manufactureDate,expireDate,10.0,null,100,
                9000,"pending","vip");

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
