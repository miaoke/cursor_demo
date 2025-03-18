package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entity.Order;
import org.example.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createOrder() throws Exception {
        Order order = new Order();
        order.setUserId(1);
        order.setAmount(new BigDecimal("100.00"));
        order.setStatus("PENDING");

        when(orderService.createOrder(any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.amount").value(100.00))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void getOrderById() throws Exception {
        Order order = new Order();
        order.setOrderId(1);
        order.setAmount(new BigDecimal("100.00"));

        when(orderService.getOrderById(1)).thenReturn(order);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1))
                .andExpect(jsonPath("$.amount").value(100.00));
    }

    @Test
    void getAllOrders() throws Exception {
        List<Order> orders = Arrays.asList(
            new Order(),
            new Order()
        );

        when(orderService.getAllOrders()).thenReturn(orders);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void updateOrder() throws Exception {
        Order order = new Order();
        order.setAmount(new BigDecimal("200.00"));
        order.setStatus("COMPLETED");

        when(orderService.updateOrder(any(Order.class))).thenReturn(true);

        mockMvc.perform(put("/api/orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteOrder() throws Exception {
        when(orderService.deleteOrder(1)).thenReturn(true);

        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getOrdersByUserId() throws Exception {
        List<Order> orders = Arrays.asList(
            new Order(),
            new Order()
        );

        when(orderService.getOrdersByUserId(1)).thenReturn(orders);

        mockMvc.perform(get("/api/orders/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getOrdersByStatus() throws Exception {
        List<Order> orders = Arrays.asList(
            new Order(),
            new Order()
        );

        when(orderService.getOrdersByStatus("PENDING")).thenReturn(orders);

        mockMvc.perform(get("/api/orders/status/PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
} 