package org.example.orderserver.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderShipMapperTest {

    @Autowired
    private OrderShipMapper orderShipMapper;

    @Test
    void insertOne() {
        Integer orderId = 6;
        Integer shipId = 1;
        int i = orderShipMapper.insertOne(orderId,shipId);
        assertTrue(i>0);
    }

    @Test
    void acceptOrder() {
        Integer orderId = 6;
        Integer shipId = 1;
        int i = orderShipMapper.acceptOrder(orderId,shipId);
        assertTrue(i>0);
    }
}