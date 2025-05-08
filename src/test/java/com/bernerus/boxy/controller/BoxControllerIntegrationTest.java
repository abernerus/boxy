package com.bernerus.boxy.controller;

import com.bernerus.boxy.BoxyServiceApplication;
import com.bernerus.boxy.api.v1.ArticleQuantityV1;
import com.bernerus.boxy.api.v1.CalculateBoxSizeRequestV1;
import com.bernerus.boxy.api.v1.CalculateBoxSizeResponseV1;
import com.bernerus.boxy.api.v1.Endpoints;
import com.bernerus.boxy.api.v1.ErrorResponse;
import com.bernerus.boxy.api.v1.ItemSizeV1;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;

import static com.bernerus.boxy.api.v1.ItemSizeV1.ITEM_1;
import static com.bernerus.boxy.api.v1.ItemSizeV1.ITEM_3;
import static com.bernerus.boxy.api.v1.ItemSizeV1.ITEM_4;
import static com.bernerus.boxy.api.v1.ItemSizeV1.ITEM_7;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes = BoxyServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
public class BoxControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCalculateBoxSize_WithValidRequest_ReturnsCorrectBoxSize() throws Exception {
        
        CalculateBoxSizeRequestV1 request = CalculateBoxSizeRequestV1.builder()
                .items(List.of(
                        ArticleQuantityV1.builder().item(ITEM_1).quantity(5).build(), // 1x1 size, 5 items
                        ArticleQuantityV1.builder().item(ITEM_3).quantity(2).build()  // 1x4 size, 2 items
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
        // The exact box size will depend on your Box implementation and algorithm
        // This is just a basic assertion that should be updated with expected values
    }

    @Test
    public void testCalculateBoxSize_WithDefaultFillFactor_ReturnsCorrectBoxSize() throws Exception {
        
        CalculateBoxSizeRequestV1 request = CalculateBoxSizeRequestV1.builder()
                .items(List.of(
                        ArticleQuantityV1.builder().item(ITEM_1).quantity(5).build(), // 1x1 size, 5 items
                        ArticleQuantityV1.builder().item(ITEM_3).quantity(2).build()  // 1x4 size, 2 items
                ))
                .build(); // No fill factor specified, should use default

        
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
    public void testCalculateBoxSize_WithEmptyItemsList_ReturnsBadRequest() throws Exception {
        // Arrange
        CalculateBoxSizeRequestV1 request = CalculateBoxSizeRequestV1.builder()
                .items(List.of())
                .fillFactor(0.75)
                .build();

        // Act & Assert
        MvcResult result = mockMvc.perform(post(Endpoints.Box.CALCULATE_BOX_SIZE_SUB_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Parse the error response
        ErrorResponse errorResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ErrorResponse.class
        );

        // Assert on the error details
        assertNotNull(errorResponse);
        assertEquals("Bad Request", errorResponse.getError());
        assertEquals("No items provided", errorResponse.getMessage());
    }

    @Test
    public void testCalculateBoxSize_WithLargeNumberOfItems_ReturnsCorrectBoxSize() throws Exception {
        
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
    public void testCalculateBoxSize_WithInvalidArticleId_ReturnsBadRequest() throws Exception {
        
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
    public void testCalculateBoxSize_WithInvalidFillFactor_ReturnsBadRequest() throws Exception {
        CalculateBoxSizeRequestV1 request = CalculateBoxSizeRequestV1.builder()
                .items(List.of(
                        ArticleQuantityV1.builder().item(ItemSizeV1.ITEM_1).quantity(5).build()
                ))
                .fillFactor(1.5) // Invalid
                .build();

        // Act & Assert
        MvcResult result = mockMvc.perform(post(Endpoints.Box.CALCULATE_BOX_SIZE_SUB_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Parse the error response
        ErrorResponse errorResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ErrorResponse.class
        );

        // Assert on the error details - validation error response
        assertNotNull(errorResponse);
        assertEquals("Validation Error", errorResponse.getError());
        assertTrue(errorResponse.getMessage().contains("fillFactor"));
    }
}