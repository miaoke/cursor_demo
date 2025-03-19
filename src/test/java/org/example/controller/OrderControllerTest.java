package org.example.controller;

import org.example.entity.Order;
import org.example.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private Order testOrder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testOrder = new Order();
        testOrder.setOrderId(1L);
        testOrder.setUserId(1L);
        testOrder.setAmount(new BigDecimal("99.99"));
        testOrder.setStatus("PENDING");
        testOrder.setCreatedAt(System.currentTimeMillis());
    }

    @Test
    void createOrder() {
        when(orderService.createOrder(any(Order.class))).thenReturn(testOrder);
        
        ResponseEntity<Order> response = orderController.createOrder(new Order());
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testOrder, response.getBody());
        verify(orderService, times(1)).createOrder(any(Order.class));
    }

    @Test
    void getOrderById() {
        when(orderService.getOrderById(anyLong())).thenReturn(testOrder);
        
        ResponseEntity<Order> response = orderController.getOrderById(1L);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testOrder, response.getBody());
        verify(orderService, times(1)).getOrderById(1L);
    }
    
    @Test
    void getOrderById_NotFound() {
        when(orderService.getOrderById(anyLong())).thenReturn(null);
        
        ResponseEntity<Order> response = orderController.getOrderById(1L);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(orderService, times(1)).getOrderById(1L);
    }

    @Test
    void getAllOrders() {
        List<Order> orders = Arrays.asList(testOrder);
        when(orderService.getAllOrders()).thenReturn(orders);
        
        ResponseEntity<List<Order>> response = orderController.getAllOrders();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orders, response.getBody());
        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void updateOrder() {
        when(orderService.updateOrder(any(Order.class))).thenReturn(testOrder);
        
        ResponseEntity<Order> response = orderController.updateOrder(1L, new Order());
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testOrder, response.getBody());
        verify(orderService, times(1)).updateOrder(any(Order.class));
    }
    
    @Test
    void updateOrder_NotFound() {
        when(orderService.updateOrder(any(Order.class))).thenReturn(null);
        
        ResponseEntity<Order> response = orderController.updateOrder(1L, new Order());
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(orderService, times(1)).updateOrder(any(Order.class));
    }

    @Test
    void deleteOrder() {
        doNothing().when(orderService).deleteOrder(anyLong());
        
        ResponseEntity<Void> response = orderController.deleteOrder(1L);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(orderService, times(1)).deleteOrder(1L);
    }
    
    @Test
    void getOrdersByUserId() {
        List<Order> orders = Arrays.asList(testOrder);
        when(orderService.getOrdersByUserId(anyLong())).thenReturn(orders);
        
        ResponseEntity<List<Order>> response = orderController.getOrdersByUserId(1L);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orders, response.getBody());
        verify(orderService, times(1)).getOrdersByUserId(1L);
    }
    
    @Test
    void getOrdersByStatus() {
        List<Order> orders = Arrays.asList(testOrder);
        when(orderService.getOrdersByStatus(anyString())).thenReturn(orders);
        
        ResponseEntity<List<Order>> response = orderController.getOrdersByStatus("PENDING");
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orders, response.getBody());
        verify(orderService, times(1)).getOrdersByStatus("PENDING");
    }
} 