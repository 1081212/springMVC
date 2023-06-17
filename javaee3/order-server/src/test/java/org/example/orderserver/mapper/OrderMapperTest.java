package org.example.orderserver.mapper;

import org.example.orderserver.entity.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderMapperTest {

    @Autowired
    private OrderMapper orderMapper;


    @Test
    void getOrderById() {
        Order order = orderMapper.getOrderById(6);
        assertTrue(order.getOrderId().equals(6));
    }

    @Test
    void payOrder() {
        int i = orderMapper.payOrder(8);
        assertTrue(i>0);
    }
}