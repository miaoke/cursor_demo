package org.example.service;

import org.example.entity.Order;
import org.example.mapper.OrderMapper;
import org.example.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder() {
        Order order = new Order();
        order.setUserId(1);
        order.setAmount(new BigDecimal("100.00"));
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());

        when(orderMapper.insert(any(Order.class))).thenReturn(1);

        Order result = orderService.createOrder(order);

        assertNotNull(result);
        assertEquals(new BigDecimal("100.00"), result.getAmount());
        verify(orderMapper).insert(order);
    }

    @Test
    void getOrderById() {
        Order order = new Order();
        order.setOrderId(1);
        order.setAmount(new BigDecimal("100.00"));

        when(orderMapper.selectById(1)).thenReturn(order);

        Order result = orderService.getOrderById(1);

        assertNotNull(result);
        assertEquals(1, result.getOrderId());
        assertEquals(new BigDecimal("100.00"), result.getAmount());
    }

    @Test
    void getAllOrders() {
        List<Order> orders = Arrays.asList(
            new Order(),
            new Order()
        );

        when(orderMapper.selectAll()).thenReturn(orders);

        List<Order> result = orderService.getAllOrders();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void updateOrder() {
        Order order = new Order();
        order.setOrderId(1);
        order.setAmount(new BigDecimal("200.00"));
        order.setStatus("COMPLETED");

        when(orderMapper.update(any(Order.class))).thenReturn(1);

        boolean result = orderService.updateOrder(order);

        assertTrue(result);
        verify(orderMapper).update(order);
    }

    @Test
    void deleteOrder() {
        when(orderMapper.delete(1)).thenReturn(1);

        boolean result = orderService.deleteOrder(1);

        assertTrue(result);
        verify(orderMapper).delete(1);
    }

    @Test
    void getOrdersByUserId() {
        List<Order> orders = Arrays.asList(
            new Order(),
            new Order()
        );

        when(orderMapper.selectByUserId(1)).thenReturn(orders);

        List<Order> result = orderService.getOrdersByUserId(1);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getOrdersByStatus() {
        List<Order> orders = Arrays.asList(
            new Order(),
            new Order()
        );

        when(orderMapper.selectByStatus("PENDING")).thenReturn(orders);

        List<Order> result = orderService.getOrdersByStatus("PENDING");

        assertNotNull(result);
        assertEquals(2, result.size());
    }
} 