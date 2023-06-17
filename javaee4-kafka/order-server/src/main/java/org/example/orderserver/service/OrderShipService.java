package org.example.orderserver.service;

import org.example.orderserver.mapper.OrderShipMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderShipService implements OrderShipMapper {

    @Autowired
    private OrderShipMapper orderShipMapper;

    @Override
    public int insertOne(Integer orderId, Integer shipId) {
        return orderShipMapper.insertOne(orderId,shipId);
    }

    @Override
    public int acceptOrder(Integer orderId, Integer shipId) {
        return orderShipMapper.acceptOrder(orderId,shipId);
    }
}
