package com.bernerus.boxy;

import com.bernerus.boxy.api.v1.ArticleQuantityV1;
import com.bernerus.boxy.api.v1.BoxSizeV1;
import com.bernerus.boxy.api.v1.CalculateBoxSizeRequestV1;
import com.bernerus.boxy.api.v1.CalculateBoxSizeResponseV1;
import com.bernerus.boxy.api.v1.Endpoints;
import com.bernerus.boxy.api.v1.ErrorResponse;
import com.bernerus.boxy.api.v1.ItemSizeV1;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static com.bernerus.boxy.api.v1.ItemSizeV1.ITEM_1;
import static com.bernerus.boxy.api.v1.ItemSizeV1.ITEM_2;
import static com.bernerus.boxy.api.v1.ItemSizeV1.ITEM_3;
import static com.bernerus.boxy.api.v1.ItemSizeV1.ITEM_4;
import static com.bernerus.boxy.api.v1.ItemSizeV1.ITEM_5;
import static com.bernerus.boxy.api.v1.ItemSizeV1.ITEM_6;
import static com.bernerus.boxy.api.v1.ItemSizeV1.ITEM_7;
import static com.bernerus.boxy.api.v1.ItemSizeV1.ITEM_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes = BoxyServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class BoxControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCalculateBoxSize_WithValidRequest_ReturnsCorrectBoxSizeNr1() throws Exception {

        CalculateBoxSizeRequestV1 request = CalculateBoxSizeRequestV1.builder()
                .items(List.of(
                        ArticleQuantityV1.builder().item(ITEM_1).quantity(5).build() // 1x1 size, 5 items
                ))
                .build();


        MvcResult result = mockMvc.perform(post(Endpoints.Box.CALCULATE_BOX_SIZE_SUB_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();


        CalculateBoxSizeResponseV1 response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CalculateBoxSizeResponseV1.class
        );

        assertNotNull(response);
        assertNotNull(response.getBoxLabel());
        Assertions.assertEquals(BoxSizeV1.SIZE_4X5.getLabel(), response.getBoxLabel());
    }
    
    @Test
    void testCalculateBoxSize_WithValidRequest_ReturnsCorrectBoxSizeNr2() throws Exception {

        CalculateBoxSizeRequestV1 request = CalculateBoxSizeRequestV1.builder()
                .items(List.of(
                        ArticleQuantityV1.builder().item(ITEM_1).quantity(5).build(),
                        ArticleQuantityV1.builder().item(ITEM_3).quantity(2).build(),
                        ArticleQuantityV1.builder().item(ITEM_5).quantity(1).build() 
                ))
                .fillFactor(0.75)
                .build();


        MvcResult result = mockMvc.perform(post(Endpoints.Box.CALCULATE_BOX_SIZE_SUB_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();


        CalculateBoxSizeResponseV1 response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CalculateBoxSizeResponseV1.class
        );

        assertNotNull(response);
        assertNotNull(response.getBoxLabel());
        Assertions.assertEquals(BoxSizeV1.SIZE_8X12.getLabel(), response.getBoxLabel());
    }
    
    @Test
    void testCalculateBoxSize_WithValidRequest_ReturnsCorrectBoxSizeNr3() throws Exception {

        CalculateBoxSizeRequestV1 request = CalculateBoxSizeRequestV1.builder()
                .items(List.of(
                        ArticleQuantityV1.builder().item(ITEM_1).quantity(5).build(),
                        ArticleQuantityV1.builder().item(ITEM_3).quantity(2).build(),
                        ArticleQuantityV1.builder().item(ITEM_4).quantity(4).build(),
                        ArticleQuantityV1.builder().item(ITEM_6).quantity(2).build(),
                        ArticleQuantityV1.builder().item(ITEM_8).quantity(1).build()
                ))
                .fillFactor(1.0)
                .extraFillAttempts(100)
                .build();


        MvcResult result = mockMvc.perform(post(Endpoints.Box.CALCULATE_BOX_SIZE_SUB_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();


        CalculateBoxSizeResponseV1 response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CalculateBoxSizeResponseV1.class
        );

        assertNotNull(response);
        assertNotNull(response.getBoxLabel());
        Assertions.assertEquals(BoxSizeV1.SIZE_12X20.getLabel(), response.getBoxLabel());
    }

    @Test
    void testCalculateBoxSize_WithValidFullRequest_ReturnsCorrectBoxSizeNr3() throws Exception {

        CalculateBoxSizeRequestV1 request = CalculateBoxSizeRequestV1.builder()
                .items(List.of(
                        ArticleQuantityV1.builder().item(ITEM_1).quantity(5).build(),
                        ArticleQuantityV1.builder().item(ITEM_2).quantity(5).build(),
                        ArticleQuantityV1.builder().item(ITEM_3).quantity(7).build(),
                        ArticleQuantityV1.builder().item(ITEM_4).quantity(9).build(),
                        ArticleQuantityV1.builder().item(ITEM_5).quantity(3).build(),
                        ArticleQuantityV1.builder().item(ITEM_6).quantity(8).build(),
                        ArticleQuantityV1.builder().item(ITEM_7).quantity(2).build(),
                        ArticleQuantityV1.builder().item(ITEM_8).quantity(4).build()
                ))
                .fillFactor(1.0)
                .extraFillAttempts(100)
                .build();
        
        MvcResult result = mockMvc.perform(post(Endpoints.Box.CALCULATE_BOX_SIZE_SUB_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();


        CalculateBoxSizeResponseV1 response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CalculateBoxSizeResponseV1.class
        );

        assertNotNull(response);
        assertNotNull(response.getBoxLabel());
        Assertions.assertEquals(BoxSizeV1.SIZE_12X20.getLabel(), response.getBoxLabel());
    }

    @Test
    void testCalculateBoxSize_WithEmptyItemsList_ReturnsBadRequest() throws Exception {
        CalculateBoxSizeRequestV1 request = CalculateBoxSizeRequestV1.builder()
                .items(List.of())
                .fillFactor(0.75)
                .build();

        MvcResult result = mockMvc.perform(post(Endpoints.Box.CALCULATE_BOX_SIZE_SUB_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        ErrorResponse errorResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ErrorResponse.class
        );

        assertNotNull(errorResponse);
        assertEquals("Bad Request", errorResponse.getError());
        assertEquals("No items provided", errorResponse.getMessage());
    }

    @Test
    void testCalculateBoxSize_WithLargeNumberOfItems_ReturnsCorrectBoxSize() throws Exception {

        CalculateBoxSizeRequestV1 request = CalculateBoxSizeRequestV1.builder()
                .items(List.of(
                        ArticleQuantityV1.builder().item(ITEM_1).quantity(20).build(),
                        ArticleQuantityV1.builder().item(ITEM_4).quantity(10).build(),
                        ArticleQuantityV1.builder().item(ITEM_7).quantity(5).build()
                ))
                .fillFactor(0.8)
                .build();


        MvcResult result = mockMvc.perform(post(Endpoints.Box.CALCULATE_BOX_SIZE_SUB_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();


        CalculateBoxSizeResponseV1 response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CalculateBoxSizeResponseV1.class
        );

        assertNotNull(response);
        assertNotNull(response.getBoxLabel());
    }

    @Test
    void testCalculateBoxSize_WithInvalidArticleId_ReturnsBadRequest() throws Exception {

        CalculateBoxSizeRequestV1 request = CalculateBoxSizeRequestV1.builder()
                .items(List.of(
                        ArticleQuantityV1.builder().item(null).quantity(5).build() // Invalid article ID
                ))
                .fillFactor(0.75)
                .build();

        mockMvc.perform(post(Endpoints.Box.CALCULATE_BOX_SIZE_SUB_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCalculateBoxSize_WithInvalidFillFactor_ReturnsBadRequest() throws Exception {
        CalculateBoxSizeRequestV1 request = CalculateBoxSizeRequestV1.builder()
                .items(List.of(
                        ArticleQuantityV1.builder().item(ItemSizeV1.ITEM_1).quantity(5).build()
                ))
                .fillFactor(1.5) // Invalid
                .build();

        MvcResult result = mockMvc.perform(post(Endpoints.Box.CALCULATE_BOX_SIZE_SUB_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        ErrorResponse errorResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ErrorResponse.class
        );

        assertNotNull(errorResponse);
        assertEquals("Validation Error", errorResponse.getError());
        assertTrue(errorResponse.getMessage().contains("fillFactor"));
    }
}